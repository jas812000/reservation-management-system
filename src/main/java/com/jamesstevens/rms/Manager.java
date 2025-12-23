package com.jamesstevens.rms;

import com.jamesstevens.rms.exceptions.*;
import com.jamesstevens.rms.reservation.CabinReservation;
import com.jamesstevens.rms.reservation.HotelReservation;
import com.jamesstevens.rms.reservation.HouseReservation;
import com.jamesstevens.rms.reservation.Reservation;

import java.io.*;
import java.util.*;

/**
 * Central controller for managing accounts and reservations.
 * <p>
 * The manager loads persisted account/reservation data at startup, provides in-memory retrieval,
 * and persists changes to disk.
 * </p>
 */
public class Manager {

    private final Map<String, Account> accounts;
    static String prefix;

    /**
     * Returns the base directory where RMS data is stored.
     * <p>
     * If the environment variable {@code RMS_DATA_DIR} is set, it is used; otherwise,
     * a {@code /data} directory under the working directory is used.
     * </p>
     *
     * @return absolute or relative path to the data directory
     */
    private static String dataDirectory() {
        return System.getProperty("RMS_DATA_DIR",
                System.getProperty("user.dir") + "/data");
    }

    /**
     * Test/support helper that clears in-memory accounts.
     * <p>
     * This does not delete any persisted files.
     * </p>
     */
    public void clearAccounts() {
        accounts.clear();
    }

    /**
     * Constructs a new {@code Manager} and loads persisted accounts/reservations.
     */
    public Manager() {
        this.accounts = new HashMap<>();
        loadAccountsAndReservations();
    }

    /**
     * Loads all accounts and their reservations from the data directory.
     * <p>
     * Account folders are scanned; for each folder, an account file is loaded and then
     * reservations are loaded from the account's {@code Reservations/} subfolder.
     * </p>
     */
    private void loadAccountsAndReservations() {
        File dataDir = new File(dataDirectory());
        if (!dataDir.exists() && !dataDir.mkdirs()) {
            throw new IllegalLoad_Exception(
                    "Data Directory",
                    dataDir.getAbsolutePath(),
                    "Unable to create data directory."
            );
        }

        File[] accountDirs = dataDir.listFiles(File::isDirectory);
        if (accountDirs == null) {
            return;
        }

        for (File accountDir : accountDirs) {
            File accountFile = new File(accountDir, "acc-" + accountDir.getName() + ".txt");
            if (!accountFile.exists()) {
                continue;
            }

            try {
                Account account = loadAccountFromFile(accountFile);
                addAccountToMemory(account);
                ensureAccountDirectory(account);
                loadReservationsForAccount(account, new File(dataDirectory(), account.getAccountNumber()));
            } catch (IllegalLoad_Exception e) {
                System.out.println("ERROR: Failed to load account from file: "
                        + accountFile.getName() + " | " + e.getMessage());
            }
        }
    }

    /**
     * Stores an account in the in-memory map using a normalized account number key.
     *
     * @param account account to store
     */
    private void addAccountToMemory(Account account) {
        String accountNumber = account.getAccountNumber().trim().toUpperCase();
        accounts.put(accountNumber, account);
    }

    /**
     * Ensures the account directory exists within the data directory.
     *
     * @param account account whose directory must exist
     * @throws IllegalLoad_Exception if the directory cannot be created
     */
    private void ensureAccountDirectory(Account account) throws IllegalLoad_Exception {
        File accountDir = new File(dataDirectory(), account.getAccountNumber());
        if (!accountDir.exists() && !accountDir.mkdirs()) {
            throw new IllegalLoad_Exception("Account Directory",
                    accountDir.getAbsolutePath(), account.getAccountNumber());
        }
    }

    /**
     * Loads an {@link Account} from the provided account file.
     * <p>
     * Also ensures the account folder exists and loads that account's reservations.
     * </p>
     *
     * @param file account file (e.g., {@code acc-A100000000.txt})
     * @return loaded account instance
     * @throws IllegalLoad_Exception if the file is unreadable or invalid
     */
    private Account loadAccountFromFile(File file) throws IllegalLoad_Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String data = reader.readLine();
            if (data == null || data.trim().isEmpty()) {
                throw new IllegalLoad_Exception("Account File", file.getName(), "Empty account file.");
            }

            Account account = Account.fromString(data);
            String accountNumber = account.getAccountNumber().trim().toUpperCase();
            accounts.put(accountNumber, account);

            File accountDir = new File(dataDirectory(), accountNumber);
            if (!accountDir.exists() && !accountDir.mkdirs()) {
                throw new IllegalLoad_Exception("Account Directory", accountDir.getAbsolutePath(), accountNumber);
            }

            loadReservationsForAccount(account, accountDir);
            return account;
        } catch (IOException e) {
            throw new IllegalLoad_Exception("Account File", file.getName(),
                    "Unknown IO error while reading the file.");
        }
    }

    /**
     * Loads and attaches reservation objects for a given account from its {@code Reservations/} directory.
     * <p>
     * Reservations are parsed via {@link Reservation#fromString(String)} and then added to the account
     * in numeric reservation-number order.
     * </p>
     *
     * @param account    account to populate
     * @param accountDir account directory containing {@code Reservations/}
     * @throws IllegalLoad_Exception if reservation files cannot be read
     */
    private void loadReservationsForAccount(Account account, File accountDir) {
        if (!account.getAllReservations().isEmpty()) {
            return;
        }

        File reservationsDir = new File(accountDir, "Reservations");
        if (!reservationsDir.exists() || !reservationsDir.isDirectory()) {
            return;
        }

        File[] reservationFiles = reservationsDir.listFiles((dir, name) ->
                name.startsWith("res-") && name.endsWith(".txt"));

        if (reservationFiles == null || reservationFiles.length == 0) {
            return;
        }

        List<Reservation> reservationList = new ArrayList<>();

        for (File file : reservationFiles) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String data = reader.readLine();
                if (data == null || data.trim().isEmpty()) {
                    continue;
                }

                Reservation reservation = Reservation.fromString(data);
                reservationList.add(reservation);

            } catch (IOException e) {
                throw new IllegalLoad_Exception("Reservation File", file.getName(), account.getAccountNumber());
            } catch (RuntimeException e) {
                System.out.println("Error parsing reservation file: " + file.getName() + " | " + e.getMessage());
            }
        }

        if (!reservationList.isEmpty()) {
            reservationList.sort(Comparator.comparing(reservation -> {
                String reservationNum = reservation.getReservationNumber().replaceAll("\\D+", "");
                return Long.parseLong(reservationNum);
            }));
        }

        for (Reservation res : reservationList) {
            if (account.getReservation(res.getReservationNumber()) == null) {
                account.addReservation(res);
            }
        }
    }

    /**
     * Loads a specific reservation by account/reservation number from disk.
     *
     * @param accountNumber     target account number
     * @param reservationNumber target reservation number
     * @return loaded reservation instance
     * @throws IllegalLoad_Exception if the file does not exist or cannot be read/parsed
     */
    private Reservation loadReservationFromFile(String accountNumber, String reservationNumber)
            throws IllegalLoad_Exception {

        File reservationFile = new File(dataDirectory() + "/" + accountNumber + "/Reservations/" +
                reservationNumber + ".txt");

        if (!reservationFile.exists()) {
            throw new IllegalLoad_Exception("Reservation File", reservationFile.getName(), accountNumber);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(reservationFile))) {
            String data = reader.readLine();
            if (data == null || data.trim().isEmpty()) {
                throw new IllegalLoad_Exception("Reservation File",
                        reservationFile.getName(), "Empty reservation file.");
            }

            return Reservation.fromString(data);

        } catch (IOException e) {
            throw new IllegalLoad_Exception("Reservation File", reservationFile.getName(), accountNumber);
        }
    }

    /**
     * Returns an immutable snapshot of all accounts.
     * <p>
     * This triggers a reload to reflect persisted state.
     * </p>
     *
     * @return immutable list of accounts
     */
    public List<Account> getAccounts() {
        reloadAccounts();
        return List.copyOf(accounts.values());
    }

    /**
     * Retrieves an account from memory by account number (case-insensitive).
     *
     * @param accountNumber account number to look up
     * @return the account if present in memory; otherwise {@code null}
     */
    public Account getAccount(String accountNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            return null;
        }

        String normalized = accountNumber.trim().toUpperCase();
        return accounts.get(normalized);
    }

    /**
     * Displays an account and its associated reservation numbers.
     *
     * @param accountNumber account number to find
     * @throws NullAccount_Exception if the account cannot be found
     */
    public void findAccount(String accountNumber) throws NullAccount_Exception {
        Account account = getAccount(accountNumber);
        if (account == null) {
            account = loadAccountIfExists(accountNumber);
        }
        if (account == null) {
            throw new NullAccount_Exception(accountNumber, "Account not found.");
        }

        System.out.println("\nAccount Number: " + account.getAccountNumber());

        List<String> reservationNumbers = account.getReservationNumbers();
        if (reservationNumbers.isEmpty()) {
            System.out.println("No reservations found for this account.");
            return;
        }

        System.out.println("Associated Reservations:");
        for (String reservationNumber : reservationNumbers) {
            System.out.println("- " + reservationNumber);
        }
    }

    /**
     * Displays a reservation and its associated account number.
     *
     * @param accountNumber     account number associated with the reservation
     * @param reservationNumber reservation number to find
     * @throws NullReservation_Exception if the account or reservation cannot be found
     */
    public void findReservation(String accountNumber, String reservationNumber) throws NullReservation_Exception {
        String normalizedAccountNumber = accountNumber.trim().toUpperCase();
        String normalizedReservationNumber = reservationNumber.trim().toUpperCase();

        Account account = accounts.get(normalizedAccountNumber);
        if (account == null) {
            account = loadAccountIfExists(accountNumber);
        }
        if (account == null) {
            throw new NullReservation_Exception(accountNumber, reservationNumber,
                    "Account not found for this reservation.");
        }

        Reservation reservation = account.getReservation(normalizedReservationNumber);
        if (reservation == null) {
            try {
                reservation = loadReservationFromFile(accountNumber, reservationNumber);
                account.addReservation(reservation);
            } catch (IllegalLoad_Exception e) {
                throw new NullReservation_Exception(accountNumber, reservationNumber, "Reservation not found.");
            }
        }

        System.out.println("\nReservation Number: " + reservationNumber);
        System.out.println("Associated Account Number: " + accountNumber);
    }

    /**
     * Attempts to load an account from disk if the expected account file exists.
     *
     * @param accountNumber account number to load
     * @return loaded account, or {@code null} if not found or load fails
     */
    private Account loadAccountIfExists(String accountNumber) {
        File accountFile = new File(dataDirectory() + "/" + accountNumber + "/acc-" + accountNumber + ".txt");

        if (!accountFile.exists()) {
            return null;
        }

        try {
            Account account = loadAccountFromFile(accountFile);
            accounts.put(accountNumber, account);
            return account;
        } catch (IllegalLoad_Exception e) {
            return null;
        }
    }

    /**
     * Generates the next available sequential account number using the format {@code A#########}.
     *
     * @return new unique account number
     */
    private String generateAccountNumber() {
        long nextNumber = 100000000;
        while (accounts.containsKey("A" + nextNumber)) {
            nextNumber++;
        }
        return "A" + nextNumber;
    }

    /**
     * Public wrapper that returns a new unique account number.
     *
     * @return new unique account number
     */
    public String getNewAccountNumber() {
        return generateAccountNumber();
    }

    /**
     * Adds an account to the system and persists it.
     *
     * @param account input account (used as a source of address/phone/email)
     * @throws DuplicateObject_Exception if the account number already exists
     * @throws IllegalSave_Exception     if persistence fails
     */
    public void addAccount(Account account) throws DuplicateObject_Exception, IllegalSave_Exception {
        String newAccountNumber;
        if (account.getAccountNumber().startsWith("A9")) {
            newAccountNumber = account.getAccountNumber();
        } else {
            newAccountNumber = generateAccountNumber();
        }

        if (accounts.containsKey(newAccountNumber)) {
            throw new DuplicateObject_Exception(newAccountNumber, "N/A");
        }

        Account newAccount = new Account(newAccountNumber, account.getAddress(),
                account.getPhoneNumber(), account.getEmail());

        accounts.put(newAccountNumber, newAccount);
        saveAccountToFile(newAccount);
        reloadAccounts();
    }

    /**
     * Persists an existing account to disk.
     *
     * @param accountNumber account to update
     * @throws IllegalArgumentException if the account is not present in memory
     * @throws IllegalSave_Exception    if persistence fails
     */
    public void updateAccount(String accountNumber) throws IllegalArgumentException, IllegalSave_Exception {
        if (!accounts.containsKey(accountNumber)) {
            throw new IllegalArgumentException("Account does not exist.");
        }

        Account account = accounts.get(accountNumber);
        saveAccountToFile(account);
    }

    /**
     * Writes an account record to disk, ensuring required directories exist.
     *
     * @param account account to persist
     * @throws IllegalSave_Exception if writing fails
     */
    private void saveAccountToFile(Account account) throws IllegalSave_Exception {
        File accountDir = new File(dataDirectory(), account.getAccountNumber());
        File reservationsDir = new File(accountDir, "Reservations");

        if (!accountDir.exists() && !accountDir.mkdirs()) {
            throw new IllegalSave_Exception("Account Directory", accountDir.getAbsolutePath(),
                    account.getAccountNumber());
        }

        if (!reservationsDir.exists() && !reservationsDir.mkdirs()) {
            throw new IllegalSave_Exception("Reservations Directory",
                    reservationsDir.getAbsolutePath(), account.getAccountNumber());
        }

        File accountFile = new File(accountDir, "acc-" + account.getAccountNumber() + ".txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(accountFile))) {
            writer.write(account.toString());
            writer.newLine();
        } catch (IOException e) {
            throw new IllegalSave_Exception("Account", accountFile.getName(), account.getAccountNumber());
        }
    }

    /**
     * Generates a unique reservation number based on reservation type.
     *
     * @param reservationType expected values: {@code Cabin}, {@code Hotel}, {@code House}
     * @return new unique reservation number
     */
    private String generateReservationNumber(String reservationType) {
        long nextNumber = 10000000;

        String localPrefix;
        switch (reservationType) {
            case "Cabin" -> localPrefix = "CAB";
            case "Hotel" -> localPrefix = "HOT";
            case "House" -> localPrefix = "HOU";
            default -> throw new IllegalArgumentException("Unknown reservation type: " + reservationType);
        }

        while (reservationExists("res-" + localPrefix + nextNumber)) {
            nextNumber++;
        }

        return "res-" + localPrefix + nextNumber;
    }

    /**
     * Public wrapper that returns a unique reservation number for a reservation type.
     *
     * @param reservationType {@code Cabin}, {@code Hotel}, or {@code House}
     * @return new unique reservation number
     */
    public String getNewReservationNumber(String reservationType) {
        return generateReservationNumber(reservationType);
    }

    /**
     * Checks whether a reservation number exists in any loaded account.
     *
     * @param reservationNumber reservation number to check
     * @return {@code true} if found; otherwise {@code false}
     */
    private boolean reservationExists(String reservationNumber) {
        for (Account account : accounts.values()) {
            if (account.getReservation(reservationNumber) != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Persists a reservation record to disk.
     * <p>
     * The reservation file is overwritten with the reservation's current serialized state.
     * </p>
     *
     * @param reservation reservation to persist
     * @throws IllegalSave_Exception if saving fails
     */
    public static void saveReservationToFile(Reservation reservation)
            throws IllegalSave_Exception {

        if (reservation == null) {
            throw new IllegalSave_Exception("Reservation", "Unknown",
                    "Cannot save a null reservation.");
        }

        File reservationsDir = ensureAccountDirectoriesExist(reservation.getAccountNumber());

        if (reservation instanceof CabinReservation) {
            prefix = "CAB";
        } else if (reservation instanceof HotelReservation) {
            prefix = "HOT";
        } else if (reservation instanceof HouseReservation) {
            prefix = "HOU";
        } else {
            throw new IllegalParameter_Exception(reservation.getAccountNumber(),
                    reservation.getReservationNumber(),
                    "Unknown reservation type: " + reservation.getClass().getSimpleName());
        }

        String formattedReservationNumber =
                normalizeReservationNumber(reservation.getReservationNumber(), prefix);

        File reservationFile = new File(reservationsDir, formattedReservationNumber + ".txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(reservationFile, false))) {
            writer.write(reservation.toString());
            writer.newLine();
        } catch (IOException e) {
            throw new IllegalSave_Exception("Reservation", reservationFile.getName(),
                    reservation.getAccountNumber());
        }
    }

    /**
     * Ensures the account directory and {@code Reservations/} subdirectory exist for the given account.
     *
     * @param accountNumber account number
     * @return the {@code Reservations/} directory file handle
     * @throws IllegalSave_Exception if directory creation fails
     */
    private static File ensureAccountDirectoriesExist(String accountNumber) throws IllegalSave_Exception {
        File accountDir = new File(dataDirectory(), accountNumber);
        File reservationsDir = new File(accountDir, "Reservations");

        if (!accountDir.exists() && !accountDir.mkdirs()) {
            throw new IllegalSave_Exception("Account Directory", accountDir.getAbsolutePath(), accountNumber);
        }

        if (!reservationsDir.exists() && !reservationsDir.mkdirs()) {
            throw new IllegalSave_Exception("Reservations Directory",
                    reservationsDir.getAbsolutePath(), accountNumber);
        }

        return reservationsDir;
    }

    /**
     * Normalizes reservation number formatting to: {@code res-<PREFIX><digits>}.
     * <p>
     * Ensures {@code res-} is lowercase and the type prefix (CAB/HOT/HOU) is uppercase.
     * </p>
     *
     * @param reservationNumber raw reservation number
     * @param prefix            type prefix (CAB/HOT/HOU)
     * @return normalized reservation number
     */
    private static String normalizeReservationNumber(String reservationNumber, String prefix) {
        if (reservationNumber == null || reservationNumber.isEmpty()) {
            return reservationNumber;
        }
        return "res-" + prefix.toUpperCase() + reservationNumber.substring(7);
    }

    /**
     * Reloads all accounts and reservations from disk.
     * <p>
     * Clears in-memory state and re-runs the full load routine.
     * </p>
     */
    public void reloadAccounts() {
        accounts.clear();
        loadAccountsAndReservations();
    }
}
