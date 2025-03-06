// Declares the package name for the project, grouping related classes together.
package com.swen_646_project_1;
/*
 * Imports the following:
 * - Reservation class to allow the Manager class to work with different types of reservations.
 * - Custom exception classes to handle various error scenarios related to reservations.
 * - ReservationStatus enum to manage different states of reservations.
 * - Java utility classes for handling data structures and operations like lists, maps, etc.
 */
import com.swen_646_project_1.exceptions.*;
import com.swen_646_project_1.reservation.Reservation;
import com.swen_646_project_1.enums.ReservationStatus;
import java.util.*;

/**
 * Represents a user account in the system.
 * Each account has a unique ID, contact details, and a list of associated reservation numbers.
 */
public class Account {

    // Encapsulated Attributes
    private final String accountNumber;                     // Unique identifier for account that cannot be changed
    private final Address address;                                // Stores the address object
    private final String phoneNumber;                             // Stores the phone number
    private final String email;                                   // Stores the email address
    private final Map<String, Reservation> reservations;    // Map of reservation numbers to Reservation objects

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
        this.reservations = new HashMap<>();

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
     * Getter that retrieves the list of reservation numbers.
     * @return List of reservation numbers associated with this account.
     */
    public List<String> getReservationNumbers() {
        return new ArrayList<>(this.reservations.keySet());
    } // End getReservationNumbers method

    /**
     * Retrieves a list of all reservations associated with this account.
     * @return A list of Reservation objects.
     */
    public List<Reservation> getAllReservations() {
        return new ArrayList<>(this.reservations.values());
    } // End getAllReservations method

    /**
     * Retrieves a reservation based on its reservation number.
     * @param reservationNumber The unique identifier of the reservation.
     * @return The Reservation object if found, otherwise null.
     */
    public Reservation getReservation(String reservationNumber) {
        return reservations.get(reservationNumber);
    } // End getReservation method

    /**
     * Adds a new reservation to the account.
     * @param reservation The Reservation object to be added.
     * @throws DuplicateObject_Exception If the reservation already exists.
     */
    public void addReservation(Reservation reservation) throws DuplicateObject_Exception {
        /*
         * Ensure the reservation object is not null.
         * Prevent adding duplicate reservations.
         * Store the reservation in the map.
         * Conditionally save to file only if required.
         */
        if (reservation == null) {
            throw new IllegalParameter_Exception("N/A", "N/A", "Reservation number cannot be empty.");
        } // End if statement

        boolean isNewReservation = !reservations.containsKey(reservation.getReservationNumber());

        if (!isNewReservation) {
            System.out.println("Skipping duplicate reservation: " + reservation.getReservationNumber());
            return; // Exits if the reservation already exists
        } // End if statement

        // Store the reservation in the account's map
        reservations.put(reservation.getReservationNumber(), reservation);

        // Debugging: Print that reservation is stored
        System.out.println("Reservation stored in account: " + reservation.getReservationNumber());

        // Save the reservation to a file only if it is new
        try {
            Manager.saveReservationToFile(reservation);
            System.out.println("Reservation " + reservation.getReservationNumber() + " successfully saved to file. \n");
        } catch (IllegalSave_Exception e) {
            System.out.println("Error saving reservation: " + e.getMessage());
        } // End try-catch statements

    } // End addReservation method

    /**
     * Updates an existing reservation with new data.
     * @param reservationNumber The unique identifier of the reservation.
     * @param updatedReservation The new Reservation object containing updated details.
     * @throws IllegalState_Exception If the reservation is completed or cancelled.
     * @throws IllegalOperation_Exception If the reservation does not exist.
     */
    public void updateReservation(String reservationNumber, Reservation updatedReservation)
            throws IllegalState_Exception, IllegalOperation_Exception {
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
        if (!reservations.containsKey(reservationNumber)) {
            throw new IllegalOperation_Exception("Update Reservation", this.accountNumber, reservationNumber,
                    "Reservation does not exist.");
        } // End if statement

        Reservation currentReservation = reservations.get(reservationNumber);

        // Prevent updates to completed or cancelled reservations
        if (currentReservation.getStatus() == ReservationStatus.COMPLETED ||
                currentReservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new IllegalState_Exception(this.accountNumber, reservationNumber,
                    "Cannot update a completed or cancelled reservation.");
        }

        // Check if the new reservation is actually different before updating
        if (!currentReservation.equals(updatedReservation)) {
            reservations.put(reservationNumber, updatedReservation);
            System.out.println("Updated reservation: " + reservationNumber);

            // Save only if changes were made
            try {
                Manager.saveReservationToFile(updatedReservation);
                System.out.println("Updated reservation saved to file: " + reservationNumber);
            } catch (IllegalSave_Exception e) {
                System.out.println("Error saving updated reservation: " + e.getMessage());
            }
        } else {
            System.out.println("No changes detected for reservation: " + reservationNumber);
        }
    } // End updateReservation method

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
        Reservation reservation = reservations.get(reservationNumber);

        if (reservation == null) {
            throw new IllegalState_Exception(this.accountNumber, reservationNumber, "Reservation does not exist.");
        } // End if statement
        if (reservation.getStatus() == ReservationStatus.CANCELLED ||
                reservation.getStatus() == ReservationStatus.COMPLETED) {
            throw new IllegalState_Exception(this.accountNumber, reservationNumber,
                    "Cannot cancel a completed or already cancelled reservation.");
        } // End if statement

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservation.setLodgingPrice(0.00); // Price should be zero after cancellation

    } // End cancelReservation method

    /**
     * Completes an existing reservation.
     * @param reservationNumber The reservation number to be marked as completed.
     * @throws IllegalState_Exception If the reservation is already completed or cancelled.
     * @throws IllegalOperation_Exception If the reservation does not exist.
     */
    public void completeReservation(String reservationNumber) throws IllegalState_Exception, IllegalOperation_Exception {
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
        Reservation reservation = reservations.get(reservationNumber);

        if (reservation == null) {
            throw new IllegalOperation_Exception("Complete Reservation", this.accountNumber, reservationNumber,
                    "Reservation does not exist.");
        } // End if statement
        if (reservation.getStatus() == ReservationStatus.COMPLETED ||
                reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new IllegalState_Exception(this.accountNumber, reservationNumber,
                    "Cannot complete a cancelled or already completed reservation.");
        } // End if statement

        reservation.setStatus(ReservationStatus.COMPLETED);
    } // End completeReservation method

    /**
     * Returns a string representation of the account details.
     * @return A formatted string containing account details
     */

    @Override
    public String toString() {
        /*
         * format and return a string containing account details
         */
        return String.format("%s,%s,%s,%s,%d,%s,%s",
                accountNumber, address.getStreet(), address.getCity(), address.getState(), address.getZipCode(),
                phoneNumber, email);

    } // End toString method

} // end class Account
