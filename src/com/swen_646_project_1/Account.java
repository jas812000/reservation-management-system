// Declares the package name for the project, grouping related classes together.
package com.swen_646_project_1;

import java.util.ArrayList; // Import used for storing dynamic lists of reservations.
import java.util.List;      // Import to handle a list of reservation numbers.

/**
 * Represents a user account in the system.
 * Each account has a unique ID, contact details, and a list of associated reservation numbers.
 */
public class Account {

    // Attributes
    private final String accountNumber;         // Unique identifier for account that cannot be changed
    private String mailingAddress;              // Stores the mailing address
    private String phoneNumber;                 // Stores the phone number
    private String email;                       // Stores the email address
    private List<String> reservationNumbers;    // List of reservation numbers associated with this account

    /**
     * Constructor to initialize an Account object with required details.
     * Validates input parameters to ensure no null or empty values are entered.
     * Assigns the provided attribute to the instance variable.
     * Initialize an empty list to store reservation numbers associated with this account.
     * @param accountNumber Unique identifier for the account
     * @param mailingAddress Mailing address of the user
     * @param phoneNumber Contact phone number of the user
     * @param email Email address of the user
     */
    public Account(String accountNumber, String mailingAddress, String phoneNumber, String email) {

        /*
         * Validate input parameters to ensure they are not null or empty.
		 *
 		 * 1. Check if the account number is null or empty.
 		 *      If true, throw an IllegalArgumentException with a message
 		 *          indicating the account number cannot be empty.
 		 *
 		 * 2. Check if the mailing address is null or empty.
 		 *      If true, throw an IllegalArgumentException with a message
 		 *          indicating the mailing address cannot be empty.
 		 *
 		 * 3. Check if the phone number is null or empty.
 		 *      If true, throw an IllegalArgumentException with a message
 		 *          indicating the phone number cannot be empty.
 	     *
 		 * 4. Check if the email is null or empty.
 		 *      If true, throw an IllegalArgumentException with a message
 		 *          indicating the email cannot be empty.
 	     */

        // Assign values after validation
        this.accountNumber = accountNumber;
        this.mailingAddress = mailingAddress;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.reservationNumbers = new ArrayList<>();

    } // End Account constructor

    /**
     * Retrieves the unique account number.
     * @return Account number as a String
     */
    public String getAccountNumber() {

        /*
         * return the account number
         */
        return null;

    } // End getAccountNumber method

    /**
     * Updates the mailing address of the account holder.
     * @param newMailingAddress The new mailing address to be set
     */
    public void updateMailingAddress(String newMailingAddress) {

        /*
         * if newMailingAddress is not null or empty
         *      update mailingAddress attribute
         */

    } // End updateMailingAddress method

    /**
     * Updates the phone number associated with the account.
     * @param newPhoneNumber The new phone number to be set
     */
    public void updatePhoneNumber(String newPhoneNumber) {

        /*
         * if newPhoneNumber is not null or empty
         *      update phoneNumber attribute
         */

    } // End updatePhoneNumber method

    /**
     * Updates the email address associated with the account.
     * @param newEmail The new email address to be set
     */
    public void updateEmail(String newEmail) {

        /*
         * if newEmail is not null or empty
         *      update email attribute
         */

    } // End updateEmail method

    /**
     * Adds a new reservation number to the list of associated reservations.
     * @param reservationNumber The reservation number to be added
     */
    public void addReservation(String reservationNumber) {

        /*
         * if reservationNumber is not null or empty
         *      add reservationNumber to reservationNumbers list
         */

    } // End addReservation method

    /**
     * Returns a string representation of the account details.
     * @return A formatted string containing account details
     */
    @Override
    public String toString() {

        /*
         * format and return a string containing account details
         */
        return null;

    } // End toString method

    /**
     * Creates an Account object from a formatted string.
     * @param data A string containing account details in a predefined format
     * @return An Account object constructed from the provided data
     */
    public static Account fromString(String data) {

        /*
         * parse data string
         * extract account details
         * return new Account object with extracted details
         */
        return null;

    } // End fromString method

} // end class Account
