// Declares the package name for the project, grouping related classes together.
package com.swen_646_project_1;

/*
 * Imports the following:
 * - Reservation class to allow the Manager class to work with different types of reservations.
 * - Custom exception classes to handle various error scenarios related to reservations.
 * - Java utility classes for handling data structures and operations like lists, maps, etc.
 */
import com.swen_646_project_1.exceptions.*;
import com.swen_646_project_1.reservation.Reservation;
import java.util.*;
import java.lang.reflect.Field;

/**
 * Represents a user account in the system.
 * Each account has a unique ID, contact details, and a list of associated reservation numbers.
 */
public class Account {

    // Encapsulated Attributes
    private String accountNumber;                         // Unique identifier for account that cannot be changed
    private Address address;                              // Stores the address object
    private String phoneNumber;                           // Stores the phone number
    private String email;                                 // Stores the email address
    private final Map<String, Reservation> reservations;  // Map of reservation numbers to Reservation objects

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
     * Creates an Account object from a comma-separated string.
     * The data string should contain at least 7 elements:
     * account ID, address (street, city, state, ZIP), email, and phone number.
     *
     * @param data A comma-separated string representing an account.
     * @return A new Account object populated with parsed details.
     * @throws IllegalLoad_Exception if the data format is invalid or incomplete.
     */
    public static Account fromString(String data) throws IllegalLoad_Exception {
        /*
         * parse data string
         * extract account details
         * return new Account object with extracted details
         */
        // Split the data string into parts based on commas
        String[] parts = data.split(",");
        // Validate the expected number of parts
        if (parts.length < 7){
            throw new IllegalLoad_Exception("Account", "N/A", "Data corrupted");
        } // End if statement

        // Parse address information from the extracted parts
        Address address = new Address(parts[1], parts[2], parts[3], Integer.parseInt(parts[4]));
        // Create and return a new Account object
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
     * Sets the account number for this account.
     * Only used when assigning a generated account number.
     * @param accountNumber The new account number.
     */
    public void setAccountNumber(String accountNumber) {
        if (this.accountNumber != null) {
            throw new IllegalStateException("Account number cannot be changed once assigned.");
        }// End if statement
        this.accountNumber = accountNumber;
    } // End setAccountNumber method


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
        if (reservationNumber == null) return null;


        String lookupKey = reservationNumber.trim().toUpperCase();
        System.out.println("🔎 Looking up reservation with key: [" + lookupKey + "]");

        return reservations.get(lookupKey);
    } // End getReservation method


    /**
     * Getter for Address.
     * @return Address object
     */
    public Address getAddress() {
        return this.address;
    } // End getAddress method


    /**
     * Updates the address using an Address object.
     * Ensures the new address follows validation rules before applying.
     * @param newAddress New Address object to replace the existing address.
     * @throws IllegalParameter_Exception if the new address is null.
     */
    public void updateAddress(Address newAddress) {
        if (newAddress == null) {
            throw new IllegalParameter_Exception(this.accountNumber, "N/A", "Address cannot be null.");
        } // End if statement
        this.address.setAddress(newAddress.getStreet(), newAddress.getCity(),
                newAddress.getState(), newAddress.getZipCode());
    } // End updateAddress method


    /**
     * Updates the address by setting new values directly.
     * Uses the `setAddress()` method from the Address class to ensure validation.
     * @param street New street name
     * @param city New city
     * @param state New state
     * @param zipCode New zip code
     */
    public void updateAddress(String street, String city, String state, int zipCode) {
        this.address.setAddress(street, city, state, zipCode);
    } // End updateAddress method


    /**
     * Retrieves the phone number associated with the account.
     * @return The phone number as a string.
     */
    public String getPhoneNumber() {
        return this.phoneNumber;
    } // End getPhoneNumber method


    /**
     * Updates the phone number for the account.
     * Ensures the new phone number is not empty before applying.
     * @param newPhoneNumber The new phone number.
     * @throws IllegalParameter_Exception if the phone number is empty or null.
     */
    public void setPhoneNumber(String newPhoneNumber) {
        if (newPhoneNumber == null || newPhoneNumber.isEmpty()) {
            throw new IllegalParameter_Exception(this.accountNumber, "N/A", "Phone number cannot be empty.");
        } // End if statement
        this.phoneNumber = newPhoneNumber;
    } // End setPhoneNumber method


    /**
     * Retrieves the email address associated with the account.
     * @return The email address as a string.
     */
    public String getEmail() {
        return this.email;
    } // End getEmail method


    /**
     * Updates the email address for the account.
     * Ensures the new email contains a valid "@" character before applying.
     * @param newEmail The new email address.
     * @throws IllegalParameter_Exception if the email is invalid.
     */
    public void setEmail(String newEmail) {
        if (newEmail == null || !newEmail.contains("@")) {
            throw new IllegalParameter_Exception(this.accountNumber, "N/A", "Invalid email address.");
        }  // End if statement
        this.email = newEmail;
    } // End setEmail method


    /**
     * Adds a new reservation to the account if it does not already exist.
     *
     * @param reservation The Reservation object to be added.
     * @throws DuplicateObject_Exception If the reservation already exists.
     * @throws IllegalParameter_Exception If the reservation object is null.
     */
    public void addReservation(Reservation reservation) throws DuplicateObject_Exception {
        /*
         * Ensure the reservation object is not null.
         * Prevent adding duplicate reservations.
         * Store the reservation in the map.
         * Conditionally save to file only if required.
         */
        // Checks if the reservation is not null
        if (reservation == null) {
            throw new IllegalParameter_Exception("N/A", "N/A", "Reservation number cannot be empty.");
        } // End if statement

        // Normalize the reservation key by trimming spaces and converting to uppercase.
        String normalizedKey = reservation.getReservationNumber().trim().toUpperCase();

        // Checks if the reservation already exists
        if (reservations.containsKey(normalizedKey)) {
            System.out.println("\n\t\tSkipping duplicate reservation: " + reservation.getReservationNumber());
            return; // Exits if the reservation already exists
        } // End if statement

        // Store the reservation in the account's map
        System.out.println("📝 Storing reservation with key: [" + normalizedKey + "]");
        reservations.put(normalizedKey, reservation);

        // Save the reservation to a file only if it is new
        try {
            Manager.saveReservationToFile(reservation);
            System.out.println("Reservation " + reservation.getReservationNumber() + " successfully saved to file.");
        } catch (IllegalSave_Exception e) {
            System.out.println("Error saving reservation: " + e.getMessage());
        } // End try-catch statements

    } // End addReservation method


    /**
     * Updates an existing reservation with new data.
     *
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

        // Retrieve the reservation
        Reservation currentReservation = reservations.get(reservationNumber);

        // Ensure the reservation exists
        if (currentReservation == null) {
            throw new IllegalOperation_Exception("Update Reservation", this.accountNumber, reservationNumber,
                    "Reservation does not exist.");
        } // End if statement

        // Validate if the reservation can be updated (Delegated to Reservation class)
        if (!currentReservation.canBeUpdated()) {
            throw new IllegalState_Exception(this.accountNumber, reservationNumber,
                    "Cannot update a completed or cancelled reservation.");
        } // End if statement

        // Update the reservation details (Ensures only actual changes are applied)
        boolean hasChanged = currentReservation.updateDetailsFrom(updatedReservation);

        if (!hasChanged) {
            return; // Exit early if no updates were made
        } // End if statement

        // Save only if changes were made
        try {
            Manager.saveReservationToFile(updatedReservation);
            System.out.println("Updated reservation saved to file: " + reservationNumber);
        } catch (IllegalSave_Exception e) {
            System.out.println("Error saving updated reservation: " + e.getMessage());
        } // End try-catch statements

    } // End updateReservation method


    /**
     * Compares two reservations field by field using reflection.
     * Ensures that updates are performed only if there are actual changes.
     * @param res1 The first reservation object (current).
     * @param res2 The second reservation object (updated).
     * @return True if there are differences, false otherwise.
     */
    private boolean areReservationsDifferent(Reservation res1, Reservation res2) {
        // Handle null cases to avoid NullPointerException
        if (res1 == null || res2 == null) return true;

        try {
            // Get the runtime class of the first reservation, allowing comparison of
            // specific subclass fields (CabinReservation, HotelReservation, HouseReservation)
            Class<?> clazz = res1.getClass();
            // Traverse the class hierarchy to check fields of the base class as well
            while (clazz != null) {
                // Iterate through each declared field in the class
                for (Field field : clazz.getDeclaredFields()) {
                    field.setAccessible(true);  // Allow access to private fields

                    // Retrieve values of the field from both reservation objects
                    Object value1 = field.get(res1);
                    Object value2 = field.get(res2);

                    // Check if the field values are different
                    if (!Objects.equals(value1, value2)) {
                        return true;
                    } // End if statement
                } // End for loop
                // Move to the superclass to check inherited fields
                clazz = clazz.getSuperclass();
            } // End while loop
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Error comparing reservations: " + e.getMessage());
        } // End try-catch statements

        return false; // No differences found
    } // End areReservationsDifferent method


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
