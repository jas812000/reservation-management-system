// Declares the package name for the project, grouping related classes together.
package com.swen_646_project_1;
/*
 * Imports the following:
 * - Reservation class to allow the Manager class to work with different types of reservations.
 * - Custom exception classes to handle various error scenarios related to reservations.
 * - ReservationStatus enum to manage different states of reservations.
 * - Java utility classes for handling data structures and operations like lists, maps, etc.
 */
import com.swen_646_project_1.exceptions.IllegalLoad_Exception;
import com.swen_646_project_1.exceptions.IllegalSave_Exception;
import com.swen_646_project_1.reservation.Reservation;
import com.swen_646_project_1.exceptions.IllegalParameter_Exception;
import com.swen_646_project_1.exceptions.IllegalState_Exception;
import com.swen_646_project_1.enums.ReservationStatus;
import java.util.*;

/**
 * Represents a user account in the system.
 * Each account has a unique ID, contact details, and a list of associated reservation numbers.
 */
public class Account {

    // Encapsulated Attributes
    private final String accountNumber;         // Unique identifier for account that cannot be changed
    private Address address;                    // Stores the address object
    private String phoneNumber;                 // Stores the phone number
    private String email;                       // Stores the email address
    private List<String> reservationNumbers;    // List of reservation numbers associated with this account

    /**
     * Constructor to initialize an Account object with required details.
     * Validates input parameters to ensure no null or empty values are entered.
     * Assigns the provided attribute to the instance variable.
     * Initialize an empty list to store reservation numbers associated with this account.
     * @param accountNumber Unique identifier for the account
     * @param address Address object containing street, city, state and zip code
     * @param phoneNumber Contact phone number of the user
     * @param email Email address of the user
     */
    public Account(String accountNumber, Address address, String phoneNumber, String email) {

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

        // Validate input parameters to ensure they are not null or empty.
        if (accountNumber == null || accountNumber.isEmpty()) {
            throw new IllegalParameter_Exception("N/A", "N/A", "Account number cannot be empty.");
        } // End if statement
        if (address == null) {
            throw new IllegalParameter_Exception("N/A", "N/A", "Mailing address cannot be empty.");
        } // End if statement
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            throw new IllegalParameter_Exception("N/A", "N/A", "Phone number cannot be empty.");
        } // End if statement
        if (email == null || !email.contains("@")) {
            throw new IllegalParameter_Exception("N/A", "N/A", "Invalid email address.");
        } // End if statement

        // Assign values after validation
        this.accountNumber = accountNumber;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.reservationNumbers = new ArrayList<>();

    } // End Account constructor

    /**
     * Creates an Account object from a formatted string.
     * @param data A string containing account details in a predefined format
     * @return An Account object constructed from the provided data
     */
    public static Account fromString(String data) throws IllegalLoad_Exception {

        /*
         * parse data string
         * extract account details
         * return new Account object with extracted details
         */
        String[] parts = data.split(",");
        if (parts.length < 7){
            throw new IllegalLoad_Exception("Account", "N/A", "Data corrupted");
        }
        Address address = new Address(parts[1], parts[2], parts[3], Integer.parseInt(parts[4]));
        return new Account(parts[0], address, parts[5], parts[6]);

    } // End fromString method

    /**
     * Getter that retrieves the unique account number.
     * @return Account number as a String
     */
    public String getAccountNumber() {
        return this.accountNumber;
    } // End getAccountNumber method

    /**
     * Getter that retrieves the account's address
     */
    public Address getAddress() { return address; } // End getAddress method

    /**
     * Updates the address of the account holder.
     * @param newAddress The new mailing address to be set.
     */
    public void setAddress(Address newAddress) {
        /*
         * if newAddress is not null or empty
         *      throw IllegalParameter_Exception with a message indication the address cannot be empty.
         * update mailingAddress attribute
         */
        if (newAddress == null) {
            throw new IllegalParameter_Exception("N/A", "N/A", "Address cannot be empty.");
        } // End if-else statements
        this.address = newAddress;

    } // End setAddressAddress method

    /**
     * Getter that retrieves the phone number.
     */
    public String getPhoneNumber() {return this.phoneNumber;} // End getPhoneNumber method

    /**
     * Updates the phone number associated with the account.
     * @param newPhoneNumber The new phone number to be set
     */
    public void setPhoneNumber(String newPhoneNumber) {
        /*
         * if newPhoneNumber is not null or empty
         *      throw IllegalParameter_Exception with a message indication the phone number cannot be empty.
         * update phoneNumber attribute
         */
        if (newPhoneNumber == null || newPhoneNumber.isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be empty.");
        } // End if-else statements
        this.phoneNumber = newPhoneNumber;
    } // End setPhoneNumber method

    /**
     * Getter that retrieves the email address.
     */
    public String getEmail() {return this.email;} // End getEmail method

    /**
     * Updates the email address associated with the account.
     * @param newEmail The new email address to be set
     */
    public void setEmail(String newEmail) {
        /*
         * if newEmail is not null or empty
         *      throw IllegalParameter_Exception with a message indication the email cannot be empty.
         * update email attribute
         */
        if (newEmail == null || !newEmail.contains("@")) {
            throw new IllegalParameter_Exception(this.accountNumber, "N/A", "Invalid email format.");
        } // End if statements
        this.email = newEmail;
    } // End setEmail method

    /**
     * Getter that retrieves the list of reservation numbers.
     */
    public List<String> getReservationNumbers() {
        return new ArrayList<>(this.reservationNumbers);
    } // End getReservationNumbers method

    /**
     * Adds a new reservation number to the list of associated reservations.
     * @param reservationNumber The reservation number to be added
     */
    public void addReservation(String reservationNumber) {

        /*
         * if reservationNumber is not null or empty
         *      throw IllegalParameter_Exception with a message indication the reservationNumber cannot be empty.
         * add reservationNumber to reservationNumbers list
         */
        if (reservationNumber == null || reservationNumber.isEmpty()) {
            throw new IllegalParameter_Exception("N/A", "N/A", "Reservation number cannot be empty.");
        } // End if-else statements
        this.reservationNumbers.add(reservationNumber);

    } // End addReservation method

    /**
     * Cancels an existing reservation for this account.
     * @param reservationNumber The reservation number to be cancelled.
     * @throws IllegalState_Exception if the reservation does not exist or is already cancelled/completed.
     */
    public void cancelReservation(String reservationNumber) {

        /*
         * 1. Check if reservation exists in the account's reservation list
         * 2. Load the Reservation object from storage
         * 3. If reservation is already cancelled or completed, throw IllegalState_Exception
         * 4. Otherwise, update status to CANCELLED and save it
         */
        if (!reservationNumbers.contains(reservationNumber)) {
            throw new IllegalState_Exception(this.accountNumber, reservationNumber, "Reservation does not exist.");
        } // End if statement

        try{
            // Load the actual Reservation object
            Reservation reservation = Manager.loadReservationFromFile(this.accountNumber, reservationNumber);

            if (reservation.getStatus() == ReservationStatus.COMPLETED ||
                    reservation.getStatus() == ReservationStatus.CANCELLED) {
                throw new IllegalState_Exception(this.accountNumber, reservationNumber,
                        "Cannot cancel a completed or already cancelled reservation.");
            } // End if statement

            // Mark reservation as cancelled
            reservation.cancelReservation();
            Manager.saveReservationToFile(reservation); // Save the updated reservation

        } catch (IllegalLoad_Exception e) {
            System.out.println("Error loading reservation: " + e.getMessage());
        } catch (IllegalSave_Exception e) {
            System.out.println("Error saving reservation: " + e.getMessage());
        } // End try-catch statements

    } // End cancelReservation method

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

} // end class Account
