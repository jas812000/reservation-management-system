// Declares the package name for the project, grouping related classes together.
package com.swen_646_project_1;

// Imports the Reservation class to allow the Manager class to work with different types of reservations.
import com.swen_646_project_1.reservation.Reservation;

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
     * Retrieves a list of all accounts in the system.
     * @return A list of Account objects.
     */
    public List<Account> getAccounts() {return null;} // End getAccounts method

    /**
     * Retrieves an account based on the provided account number.
     * @param accountNumber The unique identifier of the account.
     * @return The Account object associated with the given account number.
     */
    public Account getAccount(String accountNumber) {return null;} // End getAccount method

    /**
     * Adds a new account to the system.
     * Throws DuplicateObjectException if the account already exists.
     * @param account The Account object to be added.
     */
    public void addAccount(Account account) {} // End method

    /**
     * Updates account details based on the provided account number.
     * Throws IllegalArgumentException if the account does not exist.
     * @param accountNumber The unique identifier of the account to update.
     */
    public void updateAccount(String accountNumber) {} // End updateAccount method

    /**
     * Adds a reservation to an existing account.
     * Throws IllegalStateException if the account does not exist or the reservation is invalid.
     * @param accountNumber The unique identifier of the account.
     * @param reservation The Reservation object to be added.
     */
    public void addReservation(String accountNumber, Reservation reservation) {} // End addReservation method

    /**
     * Marks a reservation as completed.
     * Throws IllegalOperationException if the reservation cannot be finalized.
     * @param accountNumber The unique identifier of the account.
     * @param reservationNumber The unique identifier of the reservation.
     */
    public void completeReservation(String accountNumber, String reservationNumber) {} // End completeReservation method

    /**
     * Cancels an existing reservation.
     * Throws IllegalStateException if the reservation is already cancelled or completed.
     * @param accountNumber The unique identifier of the account.
     * @param reservationNumber The unique identifier of the reservation.
     */
    public void cancelReservation(String accountNumber, String reservationNumber) {} // End cancelReservation method

    /**
     * Updates an existing reservation with new data.
     * Throws IllegalStateException if the reservation is completed or cancelled.
     * @param accountNumber The unique identifier of the account.
     * @param reservationNumber The unique identifier of the reservation to update.
     * @param newReservationData The new Reservation object containing updated details.
     */
    public void updateReservation(String accountNumber, String reservationNumber, Reservation newReservationData) {} // End updateReservation method

    /**
     * Calculates the price per night for a given reservation.
     * @param accountNumber The unique identifier of the account.
     * @param reservationNumber The unique identifier of the reservation.
     * @return The price per night as a double.
     */
    public double calculatePricePerNight(String accountNumber, String reservationNumber) {return 0.0d;} // End calculatePricePerNight method

    /**
     * Calculates the total price of a given reservation.
     * @param accountNumber The unique identifier of the account.
     * @param reservationNumber The unique identifier of the reservation.
     * @return The total price as a double.
     */
    public double calculateTotalPrice(String accountNumber, String reservationNumber){return 0.0d;} // End calculateTotalPrice method

    /**
     * Loads all accounts and their associated reservations from the data storage.
     * This method is called during initialization.
     */
    private void loadAccountsAndReservations(){} // End loadAccountsAndReservations method

    /**
     * Saves an account's details to a file for persistence.
     * @param account The Account object to be saved.
     */
    private void saveAccountToFile(Account account){} // End saveAccountToFile method

    /**
     * Saves a reservation's details to a file for persistence.
     * @param reservation The Reservation object to be saved.
     */
    private void saveReservationToFile(Reservation reservation){} // End saveReservationToFile method

} // end class Manager
