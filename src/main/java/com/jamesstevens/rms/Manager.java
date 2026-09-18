package com.jamesstevens.rms;

import com.jamesstevens.rms.exceptions.*;
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

    /**
     * Returns the base directory where RMS data is stored.
     * <p>
     * If the system property {@code RMS_DATA_DIR} is set, it is used; otherwise,
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
            throw new IllegalLoadException(
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

            loadAccountFromFile(accountFile);
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
     * @throws IllegalLoadException if the file is unreadable or invalid
     */
    private Account loadAccountFromFile(File file) throws IllegalLoadException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String data = reader.readLine();
            if (data == null || data.trim().isEmpty()) {
                throw new IllegalLoadException("Account File", file.getName(), "Empty account file.");
            }

            Account account = Account.fromString(data);
            String accountNumber = account.getAccountNumber().trim().toUpperCase();
            accounts.put(accountNumber, account);

            File accountDir = new File(dataDirectory(), accountNumber);
            if (!accountDir.exists() && !accountDir.mkdirs()) {
                throw new IllegalLoadException("Account Directory", accountDir.getAbsolutePath(), accountNumber);
            }

            loadReservationsForAccount(account, accountDir);
            return account;
        } catch (IOException e) {
            throw new IllegalLoadException(
                    "Account File",
                    file.getName(),
                    "N/A",
                    e
            );
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
     * @throws IllegalLoadException if reservation files cannot be read
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
                throw new IllegalLoadException(
                        "Reservation File",
                        file.getName(),
                        account.getAccountNumber(),
                        e
                );
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
                account.addLoadedReservation(res);
            }
        }
    }

    /**
     * Loads a specific reservation by account/reservation number from disk.
     *
     * @param accountNumber     target account number
     * @param reservationNumber target reservation number
     * @return loaded reservation instance
     * @throws IllegalLoadException if the file does not exist or cannot be read/parsed
     */
    private Reservation loadReservationFromFile(String accountNumber, String reservationNumber)
            throws IllegalLoadException {

        File reservationFile = new File(dataDirectory() + "/" + accountNumber + "/Reservations/" +
                reservationNumber + ".txt");

        if (!reservationFile.exists()) {
            throw new IllegalLoadException("Reservation File", reservationFile.getName(), accountNumber);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(reservationFile))) {
            String data = reader.readLine();
            if (data == null || data.trim().isEmpty()) {
                throw new IllegalLoadException("Reservation File",
                        reservationFile.getName(), "Empty reservation file.");
            }

            return Reservation.fromString(data);

        } catch (IOException e) {
            throw new IllegalLoadException(
                    "Reservation File",
                    reservationFile.getName(),
                    accountNumber,
                    e
            );
        }
    }

    /**
     * Returns an immutable snapshot of all accounts.
     *
     * @return immutable list of accounts
     */
    public List<Account> getAccounts() {
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
     * @throws NullAccountException if the account cannot be found
     */
    public void findAccount(String accountNumber) throws NullAccountException {
        Account account = getAccount(accountNumber);
        if (account == null) {
            account = loadAccountIfExists(accountNumber);
        }
        if (account == null) {
            throw new NullAccountException(accountNumber, "Account not found.");
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
     * @throws NullReservationException if the account or reservation cannot be found
     */
    public void findReservation(String accountNumber, String reservationNumber) throws NullReservationException {
        String normalizedAccountNumber = accountNumber.trim().toUpperCase();
        String normalizedReservationNumber = reservationNumber.trim().toUpperCase();

        Account account = accounts.get(normalizedAccountNumber);
        if (account == null) {
            account = loadAccountIfExists(normalizedAccountNumber);
        }
        if (account == null) {
            throw new NullReservationException(accountNumber, reservationNumber,
                    "Account not found for this reservation.");
        }

        Reservation reservation = account.getReservation(normalizedReservationNumber);
        if (reservation == null) {
            File reservationFile = new File(
                    dataDirectory()
                            + "/"
                            + normalizedAccountNumber
                            + "/Reservations/"
                            + normalizedReservationNumber
                            + ".txt"
            );

            if (!reservationFile.exists()) {
                throw new NullReservationException(
                        accountNumber,
                        reservationNumber,
                        "Reservation not found."
                );
            }

            reservation = loadReservationFromFile(
                    normalizedAccountNumber,
                    normalizedReservationNumber
            );

            account.addLoadedReservation(reservation);
        }

        System.out.println("\nReservation Number: " + reservationNumber);
        System.out.println("Associated Account Number: " + accountNumber);
    }

    /**
     * Attempts to load an account from disk if the expected account file exists.
     *
     * @param accountNumber account number to load
     * @return loaded account, or {@code null} if the account file does not exist
     * @throws IllegalLoadException if the account file exists but cannot be loaded
     */
    private Account loadAccountIfExists(String accountNumber) {
        String normalizedAccountNumber = accountNumber.trim().toUpperCase();

        File accountFile = new File(
                dataDirectory()
                        + "/"
                        + normalizedAccountNumber
                        + "/acc-"
                        + normalizedAccountNumber
                        + ".txt"
        );

        if (!accountFile.exists()) {
            return null;
        }

        return loadAccountFromFile(accountFile);
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
     * Adds a new account to the system and persists it.
     *
     * @param account account to add
     * @throws DuplicateObjectException if the account number already exists
     * @throws IllegalSaveException if the account cannot be persisted
     */
    public void addAccount(Account account)
            throws DuplicateObjectException, IllegalSaveException {

        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null.");
        }

        String accountNumber =
                account.getAccountNumber().trim().toUpperCase();

        if (accounts.containsKey(accountNumber)) {
            throw new DuplicateObjectException(
                    "Account already exists.",
                    accountNumber
            );
        }

        saveAccountToFile(account);
        accounts.put(accountNumber, account);
    }

    /**
     * Persists an existing account to disk.
     *
     * @param accountNumber account to update
     * @throws IllegalArgumentException if the account is not present in memory
     * @throws IllegalSaveException    if persistence fails
     */
    public void updateAccount(String accountNumber) throws IllegalArgumentException, IllegalSaveException {
        String normalizedAccountNumber = accountNumber.trim().toUpperCase();

        if (!accounts.containsKey(normalizedAccountNumber)) {
            throw new IllegalArgumentException("Account does not exist.");
        }

        Account account = accounts.get(normalizedAccountNumber);
        saveAccountToFile(account);
    }

    /**
     * Writes an account record to disk, ensuring required directories exist.
     *
     * @param account account to persist
     * @throws IllegalSaveException if writing fails
     */
    private void saveAccountToFile(Account account) throws IllegalSaveException {
        File accountDir = new File(dataDirectory(), account.getAccountNumber());
        File reservationsDir = new File(accountDir, "Reservations");

        if (!accountDir.exists() && !accountDir.mkdirs()) {
            throw new IllegalSaveException("Account Directory", accountDir.getAbsolutePath(),
                    account.getAccountNumber());
        }

        if (!reservationsDir.exists() && !reservationsDir.mkdirs()) {
            throw new IllegalSaveException("Reservations Directory",
                    reservationsDir.getAbsolutePath(), account.getAccountNumber());
        }

        File accountFile = new File(accountDir, "acc-" + account.getAccountNumber() + ".txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(accountFile))) {
            writer.write(account.toString());
            writer.newLine();
        } catch (IOException e) {
            throw new IllegalSaveException(
                    "Account",
                    accountFile.getName(),
                    account.getAccountNumber(),
                    e
            );
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
     * @throws IllegalSaveException if saving fails
     */
    public static void saveReservationToFile(Reservation reservation)
            throws IllegalSaveException {

        if (reservation == null) {
            throw new IllegalSaveException("Reservation", "Unknown",
                    "Cannot save a null reservation.");
        }

        File reservationsDir = ensureAccountDirectoriesExist(reservation.getAccountNumber());

        File reservationFile = new File(
                reservationsDir,
                reservation.getReservationNumber() + ".txt"
        );

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(reservationFile, false))) {
            writer.write(reservation.toString());
            writer.newLine();
        } catch (IOException e) {
            throw new IllegalSaveException(
                    "Reservation",
                    reservationFile.getName(),
                    reservation.getAccountNumber(),
                    e
            );
        }
    }

    /**
     * Ensures the account directory and {@code Reservations/} subdirectory exist for the given account.
     *
     * @param accountNumber account number
     * @return the {@code Reservations/} directory file handle
     * @throws IllegalSaveException if directory creation fails
     */
    private static File ensureAccountDirectoriesExist(String accountNumber) throws IllegalSaveException {
        File accountDir = new File(dataDirectory(), accountNumber);
        File reservationsDir = new File(accountDir, "Reservations");

        if (!accountDir.exists() && !accountDir.mkdirs()) {
            throw new IllegalSaveException("Account Directory", accountDir.getAbsolutePath(), accountNumber);
        }

        if (!reservationsDir.exists() && !reservationsDir.mkdirs()) {
            throw new IllegalSaveException("Reservations Directory",
                    reservationsDir.getAbsolutePath(), accountNumber);
        }

        return reservationsDir;
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
