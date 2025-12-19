// Declares the package name for the project, grouping related classes together.
package com.jamesstevens.rms;

/*
 * Imports the following:
 * - Reservation class to allow the Manager class to work with different types of reservations.
 * - Custom exception classes to handle various error scenarios related to reservations.
 * - ReservationStatus enum to manage different states of reservations.
 * - Java I/O classes for file operations such as saving and loading reservation data.
 * - Java utility classes for handling data structures and operations like lists, maps, etc.
 */
import com.jamesstevens.rms.exceptions.*;
import com.jamesstevens.rms.reservation.CabinReservation;
import com.jamesstevens.rms.reservation.HotelReservation;
import com.jamesstevens.rms.reservation.HouseReservation;
import com.jamesstevens.rms.reservation.Reservation;
import java.io.*;
import java.util.*;

/**
 * The Manager class serves as the main controller for managing accounts and reservations.
 * It provides methods to add, retrieve, update, and delete accounts and reservations.
 * Data persistence is handled through file storage.
 */
public class Manager {

    // Data path
    private static final String DATA_DIRECTORY =
            System.getProperty("user.dir") + "/src/com/resources/Accounts";

    // Initialize a map storing accounts, where the key is the account number and the value is the Account object.
    private final Map<String, Account> accounts;

    // Single instance of Manager
    private static Manager instance;

    // Initialize variable
    static String prefix;
    /**
     * Constructor to initialize the Manager object.
     * Responsible for loading existing accounts and reservations from storage.
     */
    public Manager() {
        this.accounts = new HashMap<>();    // Initialize the accounts map.

        // Load all the existing accounts and reservations from storage.
        loadAccountsAndReservations();

    } // End Manager constructor


    // Singleton pattern to ensure only one instance of Manager exists
    public static Manager getInstance() {
        if (instance == null) {
            instance = new Manager();
        } // End if statement
        return instance;
    } // End getInstance method


    /**
     * Loads all accounts and their associated reservations from the data storage.
     * This method is called during initialization.
     * @throws IllegalLoad_Exception If the file cannot be read or is corrupted.
     */
    private void loadAccountsAndReservations(){
        /*
         * 1. Access the data directory where accounts are stored.
         * 2. If the directory does not exist, attempt to create it.
         *    a) If directory creation fails, throw an IllegalLoad_Exception.
         * 3. Retrieve a list of all account files that start with "acc-" and end with ".txt".
         * 4. If account files exist:
         *    a) Iterate through each account file:
         *        i) Attempt to load the account from the file.
         *        ii) Store the account object in the accounts map using its account number as the key.
         *        iii) Construct the corresponding account directory path.
         *        iv) Attempt to load reservations for this account from its directory.
         *        v) If an exception occurs while loading reservations, print the error message.
         *    b) If an exception occurs while loading an account, print the error message.
         * 5. If an exception occurs at the outer level, print the error message.
         */

        File dataDir = new File(DATA_DIRECTORY);

        // If the directory does not exist, attempt to create it
        if (!dataDir.exists()) {
            System.out.println("Accounts directory does NOT exist! Creating directory...");
            dataDir.mkdirs();
        } // End if statement

        // Search in all subdirectories to retrieve all account files
        File[] accountFiles = dataDir.listFiles(File::isDirectory);

        // If no account files exist, print message and return
        if (accountFiles == null || accountFiles.length == 0) {
            System.out.println("No account files found in: " + DATA_DIRECTORY);
            return;
        } // End if statement

        // Load each account file
        for (File accountDir : accountFiles) {
            File accountFile = new File(accountDir, "acc-" + accountDir.getName() + ".txt");
            if (!accountFile.exists()) {
                System.out.println("No account file found in: " + accountDir.getAbsolutePath());
                continue;
            } // End if statement
            try {
                // Load account from file
                Account account = loadAccountFromFile(accountFile);
                System.out.println("Successfully loaded account: " + account.getAccountNumber());

                // Normalize account number
                addAccountToMemory(account);

                // Ensure the account folder exists
                ensureAccountDirectory(account);

                // Load reservations for this account
                loadReservationsForAccount(account, new File(DATA_DIRECTORY, account.getAccountNumber()));
            } catch (IllegalLoad_Exception e) {
                System.out.println("ERROR: Failed to load account from file: "
                        + accountFile.getName() + " | " + e.getMessage());
            } // End try-catch statement
        } // End for loop
    } // End loadAccountsAndReservations method


    /**
     * Adds the account to memory and ensures consistent formatting.
     */
    private void addAccountToMemory(Account account) {
        String accountNumber = account.getAccountNumber().trim().toUpperCase();
        accounts.put(accountNumber, account);
    }  // End addAccountToMemory method


    /**
     * Ensures that the account directory exists within the data directory.
     */
    private void ensureAccountDirectory(Account account) throws IllegalLoad_Exception {
        File accountDir = new File(DATA_DIRECTORY, account.getAccountNumber());
        if (!accountDir.exists() && !accountDir.mkdirs()) {
            throw new IllegalLoad_Exception("Account Directory",
                    accountDir.getAbsolutePath(), account.getAccountNumber());
        }  // End if statement
    } // End ensureAccountDirectory method


    /**
     * Loads an account from a file and returns an Account object.
     * @param file The file containing the account data.
     * @return The Account object created from the file data.
     * @throws IllegalLoad_Exception If the file cannot be read or is corrupted.
     */
    private Account loadAccountFromFile(File file) throws IllegalLoad_Exception {
        /*
         * 1. Open the file using BufferedReader to read account data.
         * 2. Read the first line from the file (assumed to contain account data).
         * 3. Convert the retrieved data into an Account object using the fromString() method.
         * 4. Determine the directory where reservations for this account are stored.
         * 5. Call loadReservationsForAccount() to load reservations for this account.
         * 6. Return the created Account object.
         * 7. If an error occurs while reading the file, throw an IllegalLoad_Exception with file details.
         */
        /* Open the specified file for reading using BufferedReader.
         * BufferedReader reads the file efficiently, line by line, to optimize memory usage.
         * FileReader is used to read character data from the file.
         * The try-with-resources statement ensures that the BufferedReader closes after use.
         */
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String data = reader.readLine(); // Read account data from file

            // Ensure data is not null or empty
            if (data == null || data.trim().isEmpty()) {
                throw new IllegalLoad_Exception("Account File", file.getName(), "Empty account file.");
            } // End if statement

            // Convert the stored data into an Account object
            Account account = Account.fromString(data);

            // Normalize the account number by trimming whitespace and converting it to uppercase
            String accountNumber = account.getAccountNumber().trim().toUpperCase();

            // Store account in memory
            accounts.put(accountNumber, account);

            // Determine account directory (where Reservations should be located)
            File accountDir = new File(DATA_DIRECTORY, accountNumber);

            // Ensure the account folder exists (if not, create it)
            if (!accountDir.exists()) {
                System.out.println("Account directory does not exist. Creating: " + accountDir.getAbsolutePath());
                if (!accountDir.mkdirs()) {
                    throw new IllegalLoad_Exception("Account Directory", accountDir.getAbsolutePath(), accountNumber);
                } else {
                    System.out.println("Account directory successfully created: " + accountDir.getAbsolutePath());
                } // End if-else statements
            } // End if statement

            // Load reservations from the `Reservations/` folder inside the account directory
            loadReservationsForAccount(account, accountDir);

            return account;
        } catch (IOException e) {
            throw new IllegalLoad_Exception("Account File", file.getName(),
                    "Unknown IO error while reading the file.");
        } // End try-catch statements
    } // End loadAccountFromFile method


    /**
     * Loads reservations for a specific account from its associated directory.
     * @param account The Account object whose reservations are being loaded.
     * @param accountDir The directory where the account's reservations are stored.
     */
    private void loadReservationsForAccount(Account account, File accountDir) {
        /*
         * 1. Get a list of reservation files in the account's directory.
         * 2. If reservation files exist:
         * 		a) For each reservation file:
         * 			i) Open and read the file.
         * 			ii) Convert the stored data into a Reservation object.
         * 			iii) Link the reservation to the corresponding account.
         * 3. Handle potential errors while reading files.
         */

        // Check if reservations are already loaded
        if (!account.getAllReservations().isEmpty()) {
            System.out.println("Reservations already loaded for account: " + account.getAccountNumber() + "\n");
            return;
        } // End if statement

        // Define the Reservations directory inside the account folder
        File reservationsDir = new File(accountDir, "Reservations");

        // Ensure the Reservations directory exists
        if (!reservationsDir.exists() || !reservationsDir.isDirectory()) {
            System.out.println("Reservations directory missing for account: " + account.getAccountNumber());
            return; // Exit if no reservations exist
        } // End if statement

        // Retrieve all reservation files from the Reservations directory
        File[] reservationFiles = reservationsDir.listFiles((dir, name) ->
                name.startsWith("res-") && name.endsWith(".txt"));

        // Check if reservation files exist
        if (reservationFiles == null || reservationFiles.length == 0) {
            System.out.println("No reservation files found for account: " + account.getAccountNumber());
            return;
        } // End if statement

        // List to store reservations before sorting
        List<Reservation> reservationList = new ArrayList<>();

        // Iterate through each reservation file
        for (File file : reservationFiles) {
            // Open the specified file for reading using BufferedReader.
            // BufferedReader reads the file efficiently, line by line, to optimize memory usage.
            // FileReader is used to read character data from the file.
            // The try-with-resources statement ensures that the BufferedReader closes after use.
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                // Read the first line of the file, which contains reservation data
                String data = reader.readLine();
                if (data == null || data.trim().isEmpty()) {
                    continue; // Skip empty files
                } // End if statement

                // Splits the reservation data string into an array using a comma (",") as the delimiter.
                String[] parts = data.split(",");

                if (parts.length == 0) {
                    System.out.println("Malformed reservation file: " + file.getName());
                    continue;
                } // End if statement

                // Extracts the reservation type from the first element of the split array.
                // The reservation type helps determine whether the reservation is for a Cabin, Hotel, or House.
                String reservationType = parts[0];

                // Parses the reservation data based on the reservation type retrieved from the file.
                // Uses a switch expression to create the appropriate Reservation subclass object.
                Reservation reservation = switch (reservationType) {
                    // If the reservation type is "CabinReservation", parse it into a CabinReservation object.
                    case "CabinReservation" -> CabinReservation.fromString(data);
                    // If the reservation type is "HotelReservation", parse it into a HotelReservation object.
                    case "HotelReservation" -> HotelReservation.fromString(data);
                    // If the reservation type is "HouseReservation", parse it into a HouseReservation object.
                    case "HouseReservation" -> HouseReservation.fromString(data);
                    // If the reservation type does not match any known types, throw an exception.
                    default -> throw new IllegalLoad_Exception("Unknown Reservation Type",
                            file.getName(), account.getAccountNumber());
                }; // End switch Statement

                // Add to list before checking duplicates
                reservationList.add(reservation);

            } catch (IOException e) {
                System.out.println("Error reading reservation file: " + file.getName() + " | " + e.getMessage());
                throw new IllegalLoad_Exception("Reservation File", file.getName(), account.getAccountNumber());
            } catch (IllegalLoad_Exception e) {
                System.out.println("Error parsing reservation file: " + file.getName() + " | " + e.getMessage());
            } // End try-catch statements
        } // End for loop

        // Sort reservations by numerical value of reservation number
        if (!reservationList.isEmpty()) {

            // Sort the reservation list numerically by extracting digits from the reservation number
            // and converting them to long for proper numerical sorting
            reservationList.sort(Comparator.comparing(reservation -> {
                String reservationNum = reservation.getReservationNumber().replaceAll("\\D+", "");
                return Long.parseLong(reservationNum); // Convert to long for numerical sorting
            }));
        } // End if statement

        // Add sorted reservations to account
        for (Reservation res : reservationList) {
            if (account.getReservation(res.getReservationNumber()) == null) {
                account.addReservation(res);
                System.out.println("Added reservation to account: " + res.getReservationNumber());
            } else {
                System.out.println("Skipping duplicate reservation: " + res.getReservationNumber());
            } // End if statement
        } // End for loop
    } // End loadReservationsForAccount method


    /**
     * Loads a reservation from a file and returns a Reservation object.
     * @param accountNumber The account number associated with the reservation.
     * @param reservationNumber The reservation number.
     * @return The Reservation object created from the file data.
     * @throws IllegalLoad_Exception If the file cannot be read or is corrupted.
     */
    private Reservation loadReservationFromFile(String accountNumber, String reservationNumber)
            throws IllegalLoad_Exception {

        // New file path for the reservation file based on account and reservation numbers
        File reservationFile = new File(DATA_DIRECTORY + "/" + accountNumber + "/Reservations/" +
                reservationNumber + ".txt");

        // Check if the reservation file exists; if not, throw an exception
        if (!reservationFile.exists()) {
            throw new IllegalLoad_Exception("Reservation File", reservationFile.getName(), accountNumber);
        } // End if statement

        // Open the specified reservation file for reading using BufferedReader.
        // BufferedReader reads the file efficiently, line by line, to reduce memory usage.
        // FileReader is used to read character data from the file.
        // The try-with-resources statement ensures the BufferedReader closes after use.
        try (BufferedReader reader = new BufferedReader(new FileReader(reservationFile))) {
            String data = reader.readLine(); // Read reservation data from file

            if (data == null || data.trim().isEmpty()) {
                throw new IllegalLoad_Exception("Reservation File",
                        reservationFile.getName(), "Empty reservation file.");
            } // End if statement

            // Split the input data string by commas
            // Extract the reservation type from the first element
            String[] parts = data.split(",");
            String reservationType = parts[0];

            // Determine the reservation type based on the data prefix and parse accordingly
            return switch (reservationType) {
                case "CabinReservation" -> CabinReservation.fromString(data);
                case "HotelReservation" -> HotelReservation.fromString(data);
                case "HouseReservation" -> HouseReservation.fromString(data);
                default -> throw new IllegalLoad_Exception("Unknown Reservation Type",
                        reservationFile.getName(), accountNumber);
            };

        } catch (IOException e) {
            // If an error occurs while reading the file, throw an IllegalLoad_Exception
            throw new IllegalLoad_Exception("Reservation File", reservationFile.getName(), accountNumber);
        } // End try-catch statements
    } // End loadReservationFromFile


    /**
     * Getter that retrieves a list of all accounts in the system.
     * @return An immutable list of Account numbers and associated account objects.
     */
    public List<Account> getAccounts() {
        /*
         * Return an immutable list containing all account objects.
         * This ensures that the original collection cannot be modified externally.
         */

        // Ensure accounts are reloaded before returning the list
        reloadAccounts();

        return List.copyOf(accounts.values());
    } // End getAccounts method


    /**
     * getter that retrieves an account based on the provided account number.
     * @param accountNumber The unique identifier of the account.
     * @return The Account object associated with the given account number.
     */
    public Account getAccount(String accountNumber) {
        /*
         * if account exists in accounts map,
         *      return corresponding Account object
         * else
         * 	    return null
         */

        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            return null;
        } // End if statement

        // Normalize the account number format, trim leading/trailing spaces
        // and convert to uppercase for case-insensitive lookup.
        String normalizedAccountNumber = accountNumber.trim().toUpperCase();

        // Retrieve the account from the accounts map
        Account foundAccount = accounts.get(normalizedAccountNumber);
        if (foundAccount != null) {
            System.out.println("\nFound account: " + foundAccount.getAccountNumber());
        } // End if statement

        return foundAccount;
    } // End getAccount method


    /**
     * Finds and displays the account number along with its associated reservations.
     * Utilizes `getReservationNumbers()` from Account to fetch reservation numbers.
     * @param accountNumber The account number to be found.
     * @throws NullAccount_Exception If the account is not found.
     */
    public void findAccount(String accountNumber) throws NullAccount_Exception {

        // Retrieve account from Manager's account list
        Account account = getAccount(accountNumber);

        // If account is null, try loading it from storage
        if (account == null) {
            account = loadAccountIfExists(accountNumber);
        } // End if statement

        // If still null, throw an exception
        if (account == null) {
            throw new NullAccount_Exception(accountNumber, "Account not found.");
        } // End if statement

        // Display account number
        System.out.println("\nAccount Number: " + account.getAccountNumber());

        // Retrieve and display associated reservations
        List<String> reservationNumbers = account.getReservationNumbers();
        if (reservationNumbers.isEmpty()) {
            System.out.println("No reservations found for this account.");
            return;
        } // End if statement

        System.out.println("Associated Reservations:");
        for (String reservationNumber : reservationNumbers) {
            System.out.println("- " + reservationNumber);
        } // End for loop
    } // End findAccount method


    /**
     * Finds and displays the reservation number along with its associated account number.
     * Uses `getReservation()` from Account to retrieve the reservation.
     * @param accountNumber The account number associated with the reservation.
     * @param reservationNumber The reservation number to be found.
     * @throws NullReservation_Exception If the reservation is not found.
     */
    public void findReservation(String accountNumber, String reservationNumber) throws NullReservation_Exception {

        // Normalize reservation number
        reservationNumber = reservationNumber.trim().toUpperCase();

        // Retrieve account from Manager's list
        Account account = accounts.get(accountNumber);

        // If account is null, try loading it from storage
        if (account == null) {
            account = loadAccountIfExists(accountNumber);
        } // End if statement

        // If still null, throw an exception
        if (account == null) {
            throw new NullReservation_Exception(accountNumber, reservationNumber,
                    "Account not found for this reservation.");
        } // End if statement

        // Print all reservations before searching
        for (Reservation res : account.getAllReservations()) {
            System.out.println("  - " + res.getReservationNumber());
        } // End for loop

        // Retrieve reservation
        Reservation reservation = account.getReservation(reservationNumber);

        // If reservation is not found in memory, attempt to load from file
        if (reservation == null) {
            try {
                reservation = loadReservationFromFile(accountNumber, reservationNumber);
                account.addReservation(reservation); // Store in memory after loading
            } catch (IllegalLoad_Exception e) {
                throw new NullReservation_Exception(accountNumber, reservationNumber, "Reservation not found.");
            } // End try-catch statements
        } // End if statement

        // Display reservation and associated account number
        System.out.println("\nReservation Number: " + reservationNumber);
        System.out.println("Associated Account Number: " + accountNumber);
    } // End findReservation method


    /**
     * Loads an account from storage if it exists.
     * - This helper method ensures that we don't repeatedly access files.
     * @param accountNumber The account number to load.
     * @return The Account object if found, otherwise null.
     */
    private Account loadAccountIfExists(String accountNumber) {
        File accountFile = new File(DATA_DIRECTORY + "/acc-" + accountNumber + ".txt");

        // Check if file exists before attempting to load
        if (!accountFile.exists()) {
            return null; // Account file does not exist
        } // End if statement

        try {
            Account account = loadAccountFromFile(accountFile);
            accounts.put(accountNumber, account); // Store in memory after loading
            return account;
        } catch (IllegalLoad_Exception e) {
            System.out.println("Failed to load account from file: " + accountNumber);
            return null;
        } // End try-catch statements
    } // End loadAccountIfExists method


    /**
     * Generates the next available account number following the format "acc-A100000000".
     * - Account numbers start at 100000000 and increment sequentially.
     * - Ensures uniqueness by checking existing accounts.
     * @return A new unique account number.
     */
    private String generateAccountNumber() {
        // Starting number for accounts
        long nextNumber = 100000000;

        // Iterate to find the next available number
        while (accounts.containsKey("A" + nextNumber)) {
            nextNumber++;
        } // End while loop

        return "A" + nextNumber;
    } // End generateAccountNumber


    /**
     * Public method to get a new unique account number.
     * Calls the private `generateAccountNumber()` method.
     * @return A new unique account number.
     */
    public String getNewAccountNumber() {
        return generateAccountNumber();
    } // End getNewAccountNumber method

    /**
     * Adds a new account to the system.
     * Ensures the account number follows the "acc-A(digits)" format.
     * @param account The Account object to be added.
     * @throws DuplicateObject_Exception If the account already exists.
     * @throws IllegalSave_Exception If the account cannot be saved to storage.
     */
    public void addAccount(Account account) throws DuplicateObject_Exception, IllegalSave_Exception {
        /*
         * Generate a unique account number before creating the Account object
         * Create a new Account instance with the generated account number
         * if account number exists in accounts map
         * 	    throw DuplicateObject_Exception
         * else
         * 	    add account to accounts map
         * 	    save account details to file
         * Handle exceptions related to duplicate accounts or saving errors.
         */
        // Variable to store the newly assigned or updated account number
        String newAccountNumber;

        // Use manually assigned test account number if it's already set
        if (account.getAccountNumber().startsWith("A9")) {
            newAccountNumber = account.getAccountNumber();
        } else {
            // Generate sequential account numbers for non-test accounts
            newAccountNumber = generateAccountNumber();
        } // End if-else statements

        // Check if the account number already exists in the system.
        // If the account number is found in the accounts map, throw a DuplicateObject_Exception.
        // Prevents duplicate accounts from being created.
        if (accounts.containsKey(newAccountNumber)) {
            throw new DuplicateObject_Exception(newAccountNumber, "N/A");
        } // End if statement

        // Create the new account object
        Account newAccount = new Account(newAccountNumber, account.getAddress(),
                account.getPhoneNumber(), account.getEmail());

        // Add the account to the system
        accounts.put(newAccountNumber, newAccount);

        // Save the account details to a file
        saveAccountToFile(newAccount);

        // Ensure the account is available in memory
        reloadAccounts();

    } // End addAccount method


    /**
     * Updates account details based on the provided account number.
     * Throws IllegalArgumentException if the account does not exist.
     * @param accountNumber The unique identifier of the account to update.
     */
    public void updateAccount(String accountNumber) throws IllegalArgumentException, IllegalSave_Exception {
        /*
         * if account exists in accounts map
         * 	    retrieve account details
         * 	    save updated details to file
         * else
         * 	    throw IllegalArgumentException indicating the account does not exist.
         */

        // Check if the account exists in the system
        if (!accounts.containsKey(accountNumber)) {
            // Throw an IllegalArgumentException when an operation is attempted on an account that does not exist.
            throw new IllegalArgumentException("Account does not exist.");
        } // End if statement

        // Retrieve the account object
        Account account = accounts.get(accountNumber);

        // Save the updated account details to a file
        saveAccountToFile(account);
    } // End updateAccount method


    /**
     * Saves an account's details to a file for persistence.
     * @param account The Account object to be saved.
     * @throws IllegalSave_Exception If there is an issue writing the account to a file.
     */
    private void saveAccountToFile(Account account) throws IllegalSave_Exception {
        /*
         * Convert the Account object into a formatted string for storage.
         * Write the formatted data to a file in the data directory.
         *
         * The file name follows the convention: ACCOUNTNUMBER.txt
         */
        // Define the directory structure.
        // - The account's directory is stored inside the main DATA_DIRECTORY.
        // - The reservation files will be placed inside a "Reservations" subfolder.
        File accountDir = new File(DATA_DIRECTORY, account.getAccountNumber()); // Account folder
        File reservationsDir = new File(accountDir, "Reservations"); // Reservations subfolder

        //Ensure that the account's directory exists.
        // - If the directory does not exist, attempt to create it.
        // - If creation fails, throw an IllegalSave_Exception.
        if (!accountDir.exists() && !accountDir.mkdirs()) {
            throw new IllegalSave_Exception("Account Directory", accountDir.getAbsolutePath(),
                    account.getAccountNumber());
        }  else {
            System.out.println("Account directory exists or was successfully created: "
                    + accountDir.getAbsolutePath());
        } // End if-else statements

        // Ensure that the Reservations subfolder exists.
        // - If it does not exist, create it.
        // - This folder will store all reservation files associated with this account.
        if (!reservationsDir.exists() && !reservationsDir.mkdirs()) {
            throw new IllegalSave_Exception("Reservations Directory",
                    reservationsDir.getAbsolutePath(), account.getAccountNumber());
        } else {
            System.out.println("Reservations directory exists or was successfully created: "
                    + reservationsDir.getAbsolutePath());
        } // End if-else statements

        // Create the file path for the account file
        File accountFile = new File(accountDir, "acc-" + account.getAccountNumber() + ".txt");

        // Open the file for writing using BufferedWriter.
        // If the file exists, this will overwrite its contents.
        // If the file does not exist, it will be created automatically.
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(accountFile))) {
            // Convert Account to string and write to file
            writer.write(account.toString()); // Convert Account to string and write to file
            writer.newLine();
        } catch (IOException e) {
            throw new IllegalSave_Exception("Account", accountFile.getName(), account.getAccountNumber());
        } // End Try-Catch statements
    } // End saveAccountToFile method


    /**
     * Generates a unique reservation number based on the type of reservation.
     * - Cabin reservations use the prefix "CAB" (e.g., res-CAB10000000).
     * - Hotel reservations use the prefix "HOT" (e.g., res-HOT10000000).
     * - House reservations use the prefix "HOU" (e.g., res-HOU10000000).
     * Ensures uniqueness by checking existing reservations under the account.
     * @param reservationType The type of reservation ("Cabin", "Hotel", "House").
     * @return A new unique reservation number.
     */
    private String generateReservationNumber(String reservationType) {
        long nextNumber = 10000000; // Start from 10000000

        String prefix;
        switch (reservationType) {
            case "Cabin" -> prefix = "CAB";
            case "Hotel" -> prefix = "HOT";
            case "House" -> prefix = "HOU";
            default -> throw new IllegalArgumentException("Unknown reservation type: " + reservationType);
        } // End switch statements

        // Generate unique reservation number for this type
        while (reservationExists("res-" + prefix + nextNumber)) {
            nextNumber++;
        } // End while loop

        return "res-" + prefix + nextNumber;
    } // End generateReservationNumber method


    /**
     * Public method that calls generateReservationNumber() for testing.
     * @param reservationType The type of reservation (Cabin, Hotel, House)
     * @return A unique reservation number.
     */
    public String getNewReservationNumber(String reservationType) {
        return generateReservationNumber(reservationType);
    } // End getNewReservationNumber


    /**
     * Checks if a reservation with the given number already exists.
     * @param reservationNumber The reservation number to check.
     * @return True if the reservation exists, false otherwise.
     */
    private boolean reservationExists(String reservationNumber) {
        for (Account account : accounts.values()) {
            if (account.getReservation(reservationNumber) != null) {
                return true;
            } // End if statement
        } // End for loop
        return false;
    } // End reservationExists method


    /**
     * Saves a reservation's details to a file for persistence.
     * @param reservation The Reservation object to be saved.
     * @throws IllegalSave_Exception If there is an issue writing the reservation to a file.
     */
    public static void saveReservationToFile(Reservation reservation)
            throws IllegalSave_Exception {
        /*
         * Convert the Reservation object into a formatted string for storage.
         * Write the formatted data to a file in the data directory.
         * The file follows the convention: ACCOUNTNUMBER_reservations.txt
         * Each reservation is stored on a new line within the file.
         */

        // Check if the reservation is not null
        if (reservation == null) {
            throw new IllegalSave_Exception("Reservation", "Unknown",
                    "Cannot save a null reservation.");
        } // End if statement

        // Ensure account directories exist and retrieve the Reservations directory
        File reservationsDir = ensureAccountDirectoriesExist(reservation.getAccountNumber());

        /* Determine the appropriate prefix based on the reservation type.
         * Using a switch expression, we check the instance type of the reservation:
         * - CabinReservation -> Prefix "CAB"         * - HotelReservation -> Prefix "HOT"
         * - HouseReservation -> Prefix "HOU"
         * If the reservation type does not match any of these, an
         *  IllegalParameter_Exception is thrown.
         */


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
        }// End if-else statements

        // Normalize the reservation number** to ensure correct format
        String formattedReservationNumber =
                normalizeReservationNumber(reservation.getReservationNumber(), prefix);

        // Construct the reservation filename
        File reservationFile = new File(reservationsDir, formattedReservationNumber + ".txt");

        /* Open the file for writing using BufferedWriter in append mode.
         * If the file exists, new data will be appended instead of overwriting it.
         * If the file does not exist, it will be created automatically.
         */
        try(BufferedWriter writer = new BufferedWriter(
                new FileWriter(reservationFile, false))) {
            // Convert Reservation to string and write to file
            writer.write(reservation.toString());
            writer.newLine();
        } catch (IOException e) {
            // If an error occurs while writing,
            // throw an IllegalSave_Exception with relevant details
            throw new IllegalSave_Exception("Reservation", reservationFile.getName(),
                    reservation.getAccountNumber());
        } // End Try-Catch statements
    } // End saveReservationToFile method


    /**
     * Ensures that the account directory and its Reservations subfolder exist.
     * If they don't exist, they will be created.
     *
     * @param accountNumber The account number for which directories should be ensured.
     * @return The File object representing the Reservations directory.
     * @throws IllegalSave_Exception If there is an issue creating the directories.
     */
    private static File ensureAccountDirectoriesExist(String accountNumber) throws IllegalSave_Exception {
        // Step 1: Define the account directory
        File accountDir = new File(DATA_DIRECTORY, accountNumber);

        // Step 2: Define the Reservations subfolder inside the account folder
        File reservationsDir = new File(accountDir, "Reservations");

        // Step 3: Ensure the account directory exists
        if (!accountDir.exists() && !accountDir.mkdirs()) {
            throw new IllegalSave_Exception("Account Directory", accountDir.getAbsolutePath(), accountNumber);
        } // End if statement

        // Step 4: Ensure the Reservations directory exists inside the account folder
        if (!reservationsDir.exists() && !reservationsDir.mkdirs()) {
            throw new IllegalSave_Exception("Reservations Directory",
                    reservationsDir.getAbsolutePath(), accountNumber);
        } // End if statement

        return reservationsDir; // Return the Reservations directory path
    } // End ensureAccountDirectoriesExist method


    /**
     * Ensures that the reservation number follows the correct format:
     * - The "res-" prefix is always in lowercase.
     * - The reservation type prefix (CAB, HOT, HOU) is always in uppercase.
     * - The numeric portion of the reservation number remains unchanged.
     *
     * @param reservationNumber The original reservation number (e.g., "RES-cab10000000").
     * @param prefix The type prefix derived from the reservation class ("CAB", "HOT", or "HOU").
     * @return A properly formatted reservation number in the form "res-CAB10000000".
     */
    private static String normalizeReservationNumber(String reservationNumber, String prefix) {
        if (reservationNumber == null || reservationNumber.isEmpty()) return reservationNumber;

        // Ensure "res-" is lowercase and prefix is uppercase
        return "res-" + prefix.toUpperCase() + reservationNumber.substring(7);
    } // End normalizeReservationNumber method


    /**
     * Reloads all accounts from storage.
     * This method is used to refresh the account list by:
     * 1. Printing a message to indicate the reload process.
     * 2. Clearing the existing accounts map to remove outdated data.
     * 3. Calling `loadAccountsAndReservations()` to repopulate the accounts from file storage.
     * This ensures that the system always has the most up-to-date account information.
     */
    public void reloadAccounts() {
        // Clear existing accounts before reloading
        accounts.clear();
        // Now manually reloads accounts
        loadAccountsAndReservations();
    } // End reloadAccounts

} // end class Manager
