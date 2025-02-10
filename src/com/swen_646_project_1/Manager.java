// Declares the package name for the project, grouping related classes together.
package com.swen_646_project_1;
/*
 * Imports the following:
 * - Reservation class to allow the Manager class to work with different types of reservations.
 * - Custom exception classes to handle various error scenarios related to reservations.
 * - ReservationStatus enum to manage different states of reservations.
 * - Java I/O classes for file operations such as saving and loading reservation data.
 * - Java utility classes for handling data structures and operations like lists, maps, etc.
 */
import com.swen_646_project_1.reservation.Reservation;
import com.swen_646_project_1.exceptions.DuplicateObject_Exception;
import com.swen_646_project_1.exceptions.IllegalLoad_Exception;
import com.swen_646_project_1.exceptions.IllegalOperation_Exception;
import com.swen_646_project_1.exceptions.IllegalSave_Exception;
import com.swen_646_project_1.exceptions.IllegalState_Exception;
import com.swen_646_project_1.exceptions.NullAccount_Exception;
import com.swen_646_project_1.exceptions.NullReservation_Exception;
import com.swen_646_project_1.enums.ReservationStatus;
import java.io.*;
import java.util.*;

/**
 * The Manager class serves as the main controller for managing accounts and reservations.
 * It provides methods to add, retrieve, update, and delete accounts and reservations.
 * Data persistence is handled through file storage.
 */
public class Manager {

    // Encapsulated Attributes
    // Constant representing the directory path where account and reservation data is stored.
    // Internal use only (no setter method needed)
    private static final String DATA_DIRECTORY = "/path/to/data";

    // Initialize a map storing accounts, where the key is the account number and the value is the Account object.
    private Map<String, Account> accounts;

    /**
     * Constructor to initialize the Manager object.
     * Responsible for loading existing accounts and reservations from storage.
     */
    public Manager() {
        this.accounts = new HashMap<>();    // Initialize the accounts map.

        // Load all the existing accounts and reservations from storage.
        loadAccountsAndReservations();

    } // End Manager constructor

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
        try {
            // Access the data directory where accounts are stored
            File dataDir = new File(DATA_DIRECTORY);

            // If the directory does not exist, attempt to create it
            if (!dataDir.exists()) {
                if (!dataDir.mkdirs()) {
                    // If the directory creation fails, throw an exception
                    throw new IllegalLoad_Exception("Account Data Directory", DATA_DIRECTORY, "N/A");
                } // End if statement
            } // End if statement

            // Retrieve a list of all account files in the data directory.
            // The files must follow the naming pattern "acc-ACCOUNTNUMBER.txt" to be recognized as valid account files.
            File[] accountFiles = dataDir.listFiles((dir, name) -> name.startsWith("acc-") && name.endsWith(".txt"));

            // If account files exist, process each one
            if (accountFiles != null) {
                for (File file : accountFiles) {

                    try {
                        // Load the account object from the file
                        Account account = loadAccountFromFile(file);

                        // Store the account object in the accounts map using its account number as the key
                        accounts.put(account.getAccountNumber(), account);

                        // Construct the corresponding account directory path
                        File accountDir = new File(DATA_DIRECTORY + "/" + account.getAccountNumber());
                        try {
                            // Load reservations for this account
                            loadReservationsForAccount(account, accountDir);

                        } catch (IllegalLoad_Exception e) {
                            // Handle errors while loading reservations and print the error message
                            System.out.println(e.getMessage());
                        } // End try-catch statements

                    } catch (IllegalLoad_Exception e) {
                        // Handle errors while loading an account and print the error message
                        System.out.println(e.getMessage());
                    } // End try-catch statements

                } // End for loop

            } // End if statement

        } catch (IllegalLoad_Exception e) {
            // Handle errors occurring at the outer level and print the error message
            System.out.println(e.getMessage());
        } // End try-catch statements
    } // End loadAccountsAndReservations method

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

        // Open the specified file for reading using BufferedReader.
        // BufferedReader reads the file efficiently, line by line, to optimize memory usage.
        // FileReader is used to read character data from the file.
        // The try-with-resources statement ensures that the BufferedReader closes after use.
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String data = reader.readLine(); // Read account data from file

            // Convert the stored data into an Account object
            Account account = Account.fromString(data);

            // Check if account is null before accessing its methods
            if (account == null) {
                // Throw a NullAccount_Exception when an account is expected but not found.
                throw new NullAccount_Exception("N/A", "Account information is missing. Unable to create the directory.");

            } // End if statement

            // Load reservations associated with this account
            File accountDir = new File(DATA_DIRECTORY + "/" + account.getAccountNumber());
            loadReservationsForAccount(account, accountDir);

            return account;
        } catch (IOException e) {
            throw new IllegalLoad_Exception("Account File", file.getName(), "Unknown");
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

        // Ensure the account's directory exists and is a valid directory
        if (!accountDir.exists() || !accountDir.isDirectory()) {

            // Throw an IllegalLoad_Exception when the system fails to load an account's reservation directory.
            throw new IllegalLoad_Exception("Account Reservation Directory", accountDir.getAbsolutePath(), account.getAccountNumber());
        } // End if statement

        // Retrieve all reservation files from the account's directory
        // Reservation files must follow the naming convention "res-RESERVATIONNUMBER.txt"
        File[] reservationFiles = accountDir.listFiles((dir, name) -> name.startsWith("res-") && name.endsWith(".txt"));

        // Check if reservation files exist
        if (reservationFiles != null) {
            // Iterate through each reservation file
            for (File file : reservationFiles) {

                // Open the specified file for reading using BufferedReader.
                // BufferedReader reads the file efficiently, line by line, to optimize memory usage.
                // FileReader is used to read character data from the file.
                // The try-with-resources statement ensures that the BufferedReader closes after use.
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    // Read the first line of the file, which contains reservation data
                    String data = reader.readLine();

                    // Convert the stored data into a Reservation object
                    Reservation reservation = Reservation.fromString(data);

                    // Check if reservation is null before accessing its methods
                    if (reservation == null) {
                        // Throw a NullReservation_Exception when attempting to add a reservation that does not exist.
                        throw new NullReservation_Exception(account.getAccountNumber(), "N/A", "No reservation found. Unable to add it to account.");
                    } // End if Statement

                    // Link the reservation to the account
                    account.addReservation(reservation.getReservationNumber());
                } catch (IOException e) {
                    throw new IllegalLoad_Exception("Reservation File", file.getName(), account.getAccountNumber());
                } // End try-catch statements
            } // End for loop
        } // End if statement
    } // End loadReservationsForAccount method

    /**
     * Loads a reservation from a file and returns a Reservation object.
     * @param accountNumber The account number associated with the reservation.
     * @param reservationNumber The reservation number.
     * @return The Reservation object created from the file data.
     * @throws IllegalLoad_Exception If the file cannot be read or is corrupted.
     */
    protected static Reservation loadReservationFromFile(String accountNumber, String reservationNumber) throws IllegalLoad_Exception {

        // New file path for the reservation file based on account and reservation numbers
        File reservationFile = new File(DATA_DIRECTORY + "/" + accountNumber + "/res-" + reservationNumber + ".txt");

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

            // Convert the stored data into a Reservation object
            return Reservation.fromString(data);
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
        return accounts.getOrDefault(accountNumber, null);
    } // End getAccount method

    /**
     * Finds a reservation by account number and reservation number.
     * @param accountNumber The account number associated with the reservation.
     * @param reservationNumber The reservation number.
     * @return The Reservation object if found, otherwise null.
     */
    private Reservation findReservation(String accountNumber, String reservationNumber) {

        // Retrieve the Account object from the accounts map using the account number
        Account account = accounts.get(accountNumber);

        // If the account does not exist, return null
        if (account == null) {
            return null;
        } // End if statement

        // Loop through reservations linked to the account
        for (String resNum : account.getReservationNumbers()) {
            // If a matching reservation number is found, load the reservation from the file
            if (resNum.equals(reservationNumber)) {
                return loadReservationFromFile(accountNumber, reservationNumber);
            } // End if statement
        } // End for loop

        return null; // Reservation not found
    } // End findReservation method

    /**
     * Adds a new account to the system.
     * Throws DuplicateObject_Exception if the account already exists.
     * @param account The Account object to be added.
     */
    public void addAccount(Account account) throws DuplicateObject_Exception, IllegalSave_Exception {
        /*
         * if account number exists in accounts map
         * 	    throw DuplicateObject_Exception
         * else
         * 	    add account to accounts map
         * 	    save account details to file
         * Handle exceptions related to duplicate accounts or saving errors.
         */
        try {
            // Check if the account already exists
            if (accounts.containsKey(account.getAccountNumber())) {
                throw new DuplicateObject_Exception(account.getAccountNumber(), "N/A");
            } // End if statement

            // Add the account to the system
            accounts.put(account.getAccountNumber(), account);

            // Save the account details to a file
            saveAccountToFile(account);

        } catch (DuplicateObject_Exception e) {
            System.out.println(e.getMessage()); // Print error message if the account is duplicate
        } catch (IllegalSave_Exception e) {
            System.out.println(e.getMessage()); // Print error message if the account cannot be saved
        } // End try-catch statements

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
     * Adds a reservation to an existing account.
     * Throws IllegalState_Exception if the account does not exist or the reservation is invalid.
     * @param accountNumber The unique identifier of the account.
     * @param reservation The Reservation object to be added.
     */
    public void addReservation(String accountNumber, Reservation reservation) throws IllegalState_Exception, IllegalSave_Exception {
        /*
         * if account exists in accounts map
         *      ensures reservation is valid
         * 		save reservation to file
         * else
         * 	    throw IllegalState_Exception
         * Handle any errors that occur while saving the reservation.
         */
        try {
            // Retrieve the account object
            Account account = accounts.get(accountNumber);

            // If the account does not exist, throw an exception
            if (account == null) {
                throw new IllegalState_Exception(accountNumber, reservation.getReservationNumber(), "Account does not exist.");
            } // End if statement

            // Save the reservation details to a file
            saveReservationToFile(reservation);

        } catch (IllegalSave_Exception e) {
            System.out.println(e.getMessage()); // Print error message if reservation saving fails
        } // End try-catch statements
    } // End addReservation method

    /**
     * Marks a reservation as completed.
     * Throws IllegalOperation_Exception if the reservation cannot be finalized.
     * @param accountNumber The unique identifier of the account.
     * @param reservationNumber The unique identifier of the reservation.
     */
    public void completeReservation(String accountNumber, String reservationNumber) throws IllegalState_Exception, IllegalOperation_Exception, IllegalSave_Exception {
        /*
         * Retrieve the account from the system.
         * If the account does not exist, throw an IllegalState_Exception.
         *
         * Find the reservation within the account.
         * If the reservation does not exist, throw an IllegalOperation_Exception.
         *
         * If the reservation is already completed or cancelled:
         *      - Throw an IllegalState_Exception indicating it cannot be completed.
         *
         * Otherwise:
         *      - Mark the reservation as completed.
         *      - Save the updated reservation to file.
         *
         * Handle any errors that occur and print appropriate error messages.
         */

        // Retrieve the account associated with the reservation
        Account account = accounts.get(accountNumber);

        // If the account does not exist, throw an exception
        if (account == null) {
            throw new IllegalState_Exception(accountNumber, "N/A", "Account does not exist.");
        } // End if statement

        try {
            // Find the reservation associated with the account
            Reservation reservation = findReservation(accountNumber, reservationNumber);

            // If the reservation does not exist, throw an exception
            if (reservation == null) {
                throw new IllegalOperation_Exception("Complete Reservation", accountNumber, reservationNumber, "Reservation does not exist.");
            } // End if statement

            // If the reservation is already completed or cancelled, throw an exception
            if (reservation.getStatus() == ReservationStatus.COMPLETED || reservation.getStatus() == ReservationStatus.CANCELLED) {
                throw new IllegalState_Exception(accountNumber, reservationNumber, "Cannot complete a cancelled or already completed reservation.");
            } // End if statement

            // Mark the reservation as completed
            reservation.completeReservation();

            // Save the updated reservation details to the file
            saveReservationToFile(reservation);

        } catch (IllegalState_Exception | IllegalOperation_Exception e) {
            System.out.println("Error: " + e.getMessage());  // Print only the custom error message
        } // End try-catch statements

    } // End completeReservation method

    /**
     * Cancels an existing reservation.
     * Throws IllegalState_Exception if the reservation is already cancelled or completed.
     * @param accountNumber The unique identifier of the account.
     * @param reservationNumber The unique identifier of the reservation.
     */
    public void cancelReservation(String accountNumber, String reservationNumber) throws IllegalState_Exception, IllegalSave_Exception {
        /*
         * Retrieve the account from the system.
         * If the account does not exist, throw an IllegalState_Exception.
         *
         * Call the account's cancelReservation method to update the reservation status.
         * If the reservation is already cancelled or completed, an IllegalState_Exception is thrown.
         */
        // Retrieve the account associated with the reservation
        Account account = accounts.get(accountNumber);

        // If the account does not exist, throw an exception
        if (account == null) {
            // Throw an IllegalState_Exception when an operation is attempted on an account that does not exist.
            throw new IllegalState_Exception(accountNumber, reservationNumber, "Account does not exist.");
        } // End if statement

        // Call the account's method to cancel the reservation
        account.cancelReservation(reservationNumber);

    } // End cancelReservation method

    /**
     * Updates an existing reservation with new data.
     * Throws IllegalState_Exception if the reservation is completed or cancelled.
     * @param accountNumber The unique identifier of the account.
     * @param reservationNumber The unique identifier of the reservation to update.
     * @param newReservationData The new Reservation object containing updated details.
     */
    public void updateReservation(String accountNumber, String reservationNumber, Reservation newReservationData) throws IllegalState_Exception, IllegalOperation_Exception, IllegalSave_Exception {
        /*
         * Retrieve the reservation using accountNumber and reservationNumber.
         * If the reservation does not exist, throw an IllegalOperation_Exception.
         *
         * If the reservation is already completed or cancelled:
         *      - Throw an IllegalState_Exception indicating it cannot be updated.
         *
         * Otherwise:
         *      - Update the reservation details with newReservationData.
         *      - Save the updated reservation to file.
         *
         * Handle any errors that occur and print appropriate error messages.
         */

        try {
            // Find the reservation associated with the account
            Reservation reservation = findReservation(accountNumber, reservationNumber);

            // If the reservation does not exist, throw an exception
            if (reservation == null) {
                throw new IllegalOperation_Exception("Update Reservation", accountNumber, reservationNumber, "Reservation does not exist.");
            } // End if statement

            // If the reservation is already completed or cancelled, throw an exception
            if (reservation.getStatus() == ReservationStatus.COMPLETED || reservation.getStatus() == ReservationStatus.CANCELLED) {
                throw new IllegalState_Exception(accountNumber, reservationNumber, "Cannot update a completed or cancelled reservation.");
            } // End if statement

            // Update the reservation details with new reservation data
            reservation.updateReservation(newReservationData);

            // Save the updated reservation details to the file
            saveReservationToFile(reservation);

        } catch (IllegalState_Exception | IllegalOperation_Exception e) {
            System.out.println("Error: " + e.getMessage());  // Print only the custom error message
        } // End try-catch statements

    } // End updateReservation method

    /**
     * Calculates the price per night for a given reservation.
     * @param accountNumber The unique identifier of the account.
     * @param reservationNumber The unique identifier of the reservation.
     * @return The price per night as a double.
     */
    public double calculatePricePerNight(String accountNumber, String reservationNumber) {

        /*
         * retrieve reservation using accountNumber and reservationNumber
         * return reservation's nightly price
         */
        return 0.0d;

    } // End calculatePricePerNight method

    /**
     * Calculates the total price of a given reservation.
     * @param accountNumber The unique identifier of the account.
     * @param reservationNumber The unique identifier of the reservation.
     * @return The total price as a double.
     */
    public double calculateTotalPrice(String accountNumber, String reservationNumber){

        /*
         * retrieve reservation using accountNumber and reservationNumber
         * calculate total cost: nightly price * number of nights
         * return total cost
         */
        return 0.0d;

    } // End calculateTotalPrice method

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
        // Create the file path for the account file
        File file = new File(DATA_DIRECTORY + "/" + account.getAccountNumber() + ".txt");

        // Open the file for writing using BufferedWriter.
        // If the file exists, this will overwrite its contents.
        // If the file does not exist, it will be created automatically.
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

            // Convert Account to string and write to file
            writer.write(account.toString()); // Convert Account to string and write to file

        } catch (IOException e) {
            // If an error occurs while writing, throw an IllegalSave_Exception with relevant details
            throw new IllegalSave_Exception("Account", file.getName(), account.getAccountNumber());
        } // End Try-Catch statements
    } // End saveAccountToFile method

    /**
     * Saves a reservation's details to a file for persistence.
     * @param reservation The Reservation object to be saved.
     * @throws IllegalSave_Exception If there is an issue writing the reservation to a file.
     */
    protected static void saveReservationToFile(Reservation reservation) throws IllegalSave_Exception {
        /*
         * Convert the Reservation object into a formatted string for storage.
         * Write the formatted data to a file in the data directory.
         *
         * The file follows the convention: ACCOUNTNUMBER_reservations.txt
         * Each reservation is stored on a new line within the file.
         */
        // Create the file path for the reservation file
        File file = new File(DATA_DIRECTORY + "/" + reservation.getAccountNumber() +
                "_reservations.txt");

        // Open the file for writing using BufferedWriter in append mode.
        // If the file exists, new data will be appended instead of overwriting it.
        // If the file does not exist, it will be created automatically.
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {

            // Convert Reservation to string and write to file
            writer.write(reservation.toString());

            // Ensure each reservation is on a new line
            writer.newLine();

        } catch (IOException e) {
            // If an error occurs while writing, throw an IllegalSave_Exception with relevant details
            throw new IllegalSave_Exception("Reservation", file.getName(), reservation.getAccountNumber());
        } // End Try-Catch statements

    } // End saveReservationToFile method

} // end class Manager
