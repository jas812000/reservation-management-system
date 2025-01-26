// Declares the package name for the project, grouping related classes together.
package com.swen_646_project_1;

// Imports the Reservation class to allow the Manager class to work with different types of reservations.
import com.swen_646_project_1.reservation.Reservation;

import java.io.File;        // Import used to handle file and directory operations.
import java.util.HashMap;   // Import used to store and manage key-value pairs (accounts and account numbers).
import java.util.List;      // Import used to store and manage collections of accounts.
import java.util.Map;       // Import used to store accounts using a key-value pair structure.

/**
 * The Manager class serves as the main controller for managing accounts and reservations.
 * It provides methods to add, retrieve, update, and delete accounts and reservations.
 * Data persistence is handled through file storage.
 */
public class Manager {

    // Attributes
    // Constant representing the directory path where account and reservation data is stored
    private static final String DATA_DIRECTORY = "/path/to/data";

    // A map storing accounts, where the key is the account number and the value is the Account object
    private Map<String, Account> accounts;

    /**
     * Constructor to initialize the Manager object.
     * Responsible for loading existing accounts and reservations from storage.
     */
    public Manager() {
        this.accounts = new HashMap<>();    // Initialize the accounts map

        // Load all the existing accounts and reservations from storage
        loadAccountsAndReservations();

    } // End Manager constructor

    /**
     * Loads all accounts and their associated reservations from the data storage.
     * This method is called during initialization.
     */
    private void loadAccountsAndReservations(){

        /*
         * 1. Access the data directory where accounts are stored.
         * 2. If the directory does not exist, create it.
         * 3. Get a list of all account directories.
         * 4. If account directories exist:
         * 		a) Iterate through each account directory:
         * 				i) Read the account file (acc-ACCOUNTNUMBER.txt).
         * 				ii) Convert the stored data into an Account object.
         * 				iii) Store the account object in the accounts map.
         * 				iv) Call loadReservationsForAccount() to load reservations for this account.
         * 5. Handle potential errors while reading files.
         */

    } // End loadAccountsAndReservations method

    /**
     * Loads reservations for a specific account from its associated directory.
     * @param account The Account object whose reservations are being loaded.
     * @param accountDir The directory where the account's reservations are stored.
     */
    private void loadReservationsForAccount(Account account, File accountDir) {

        /*
         * 1. Get a list of reservation files in the account's directory.
         * 2. If reservation files exist:
         * 			a) For each reservation file:
         * 					i) Open and read the file.
         * 					ii) Convert the stored data into a Reservation object.
         * 					iii) Link the reservation to the corresponding account.
         * 3. Handle potential errors while reading files.
         */

    } // End loadReservationsForAccount method

    /**
     * Retrieves a list of all accounts in the system.
     * @return A list of Account objects.
     */
    public List<Account> getAccounts() {

        /*
         * return list of all accounts from the system
         */
        return null;

    } // End getAccounts method

    /**
     * Retrieves an account based on the provided account number.
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
        return null;

    } // End getAccount method

    /**
     * Adds a new account to the system.
     * Throws DuplicateObjectException if the account already exists.
     * @param account The Account object to be added.
     */
    public void addAccount(Account account) {

        /*
         * if account number exists in accounts map
         * 	    throw DuplicateObjectException
         * else
         * 	    add account to accounts map
         * 	    save account details to file
         */

    } // End addAccount method

    /**
     * Updates account details based on the provided account number.
     * Throws IllegalArgumentException if the account does not exist.
     * @param accountNumber The unique identifier of the account to update.
     */
    public void updateAccount(String accountNumber) {

        /*
         * if account exists in accounts map
         * 	    update account details
         * 	    save updated details to file
         * else
         * 	    throw IllegalArgumentException
         */

    } // End updateAccount method

    /**
     * Adds a reservation to an existing account.
     * Throws IllegalStateException if the account does not exist or the reservation is invalid.
     * @param accountNumber The unique identifier of the account.
     * @param reservation The Reservation object to be added.
     */
    public void addReservation(String accountNumber, Reservation reservation) {

        /*
         * if account exists in accounts map
         *      if reservation is valid
         * 		    add reservation to account
         * 		    save reservation to file
         * 	    else
         * 		    throw IllegalStateException
         * else
         * 	    throw IllegalStateException
         */

    } // End addReservation method

    /**
     * Marks a reservation as completed.
     * Throws IllegalOperationException if the reservation cannot be finalized.
     * @param accountNumber The unique identifier of the account.
     * @param reservationNumber The unique identifier of the reservation.
     */
    public void completeReservation(String accountNumber, String reservationNumber) {

        /*
         * if reservation exists and can be completed
         * 	    update reservation status to completed
         * 	    save updated reservation to file
         * else
         * 	    throw IllegalOperationException
         */

    } // End completeReservation method

    /**
     * Cancels an existing reservation.
     * Throws IllegalStateException if the reservation is already cancelled or completed.
     * @param accountNumber The unique identifier of the account.
     * @param reservationNumber The unique identifier of the reservation.
     */
    public void cancelReservation(String accountNumber, String reservationNumber) {

        /*
         * if reservation exists
         * 	    if reservation is already cancelled or completed
         * 		    throw IllegalStateException
         * 	    else
         * 		    update reservation status to cancelled
         * 		    save updated reservation to file
         * else
         * 	    throw IllegalStateException
         */

    } // End cancelReservation method

    /**
     * Updates an existing reservation with new data.
     * Throws IllegalStateException if the reservation is completed or cancelled.
     * @param accountNumber The unique identifier of the account.
     * @param reservationNumber The unique identifier of the reservation to update.
     * @param newReservationData The new Reservation object containing updated details.
     */
    public void updateReservation(String accountNumber, String reservationNumber, Reservation newReservationData) {

        /*
         * if reservation exists
         * 	    if reservation is completed or cancelled
         * 		    throw IllegalStateException
         * 	    else
         * 		    update reservation details with newReservationData
         * 		    save updated reservation to file
         * else
         * 	    throw IllegalStateException
         */

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
     */
    private void saveAccountToFile(Account account){

        /*
         * convert Account object into format to be stored
         * write formatted data to file in data directory
         */

    } // End saveAccountToFile method

    /**
     * Saves a reservation's details to a file for persistence.
     * @param reservation The Reservation object to be saved.
     */
    private void saveReservationToFile(Reservation reservation){

        /*
         * convert Reservation object into format to be stored
         * write formatted data to file in data directory
         */

    } // End saveReservationToFile method

} // end class Manager
