package com.jamesstevens.rms;

import com.jamesstevens.rms.exceptions.*;
import com.jamesstevens.rms.reservation.Reservation;

import java.util.*;

/**
 * Represents a user account in the reservation management system.
 * <p>
 * An account maintains contact information and a collection of reservations
 * associated with a unique account number.
 * </p>
 */
public class Account {

    private String accountNumber;
    private final Address address;
    private String phoneNumber;
    private String email;
    private final Map<String, Reservation> reservations;

    /**
     * Constructs a new {@code Account}.
     *
     * @param accountNumber unique account identifier
     * @param address       mailing address associated with the account
     * @param phoneNumber   contact phone number
     * @param email         contact email address
     * @throws IllegalParameter_Exception if any required parameter is invalid
     */
    public Account(String accountNumber, Address address, String phoneNumber, String email) {
        if (accountNumber == null || accountNumber.isEmpty()) {
            throw new IllegalParameter_Exception("N/A", "N/A", "Account number cannot be empty.");
        }
        if (address == null) {
            throw new IllegalParameter_Exception("N/A", "N/A", "Mailing address cannot be empty.");
        }
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            throw new IllegalParameter_Exception("N/A", "N/A", "Phone number cannot be empty.");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalParameter_Exception("N/A", "N/A", "Invalid email address.");
        }

        this.accountNumber = accountNumber;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.reservations = new HashMap<>();
    }

    /**
     * Creates an {@code Account} instance from a serialized account record.
     *
     * @param data comma-separated account data
     * @return parsed {@code Account} instance
     * @throws IllegalLoad_Exception if the data format is invalid
     */
    public static Account fromString(String data) throws IllegalLoad_Exception {
        String[] parts = data.split(",");
        if (parts.length < 7) {
            throw new IllegalLoad_Exception("Account", "N/A", "Data corrupted");
        }

        Address address = new Address(
                parts[1],
                parts[2],
                parts[3],
                Integer.parseInt(parts[4])
        );

        return new Account(parts[0], address, parts[5], parts[6]);
    }

    /**
     * Returns the unique account number.
     *
     * @return account number
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * Assigns the account number if it has not already been set.
     *
     * @param accountNumber generated account number
     * @throws IllegalStateException if the account number is already assigned
     */
    public void setAccountNumber(String accountNumber) {
        if (this.accountNumber != null) {
            throw new IllegalStateException("Account number cannot be changed once assigned.");
        }
        this.accountNumber = accountNumber;
    }

    /**
     * Returns all reservation numbers associated with this account.
     *
     * @return list of reservation numbers
     */
    public List<String> getReservationNumbers() {
        return new ArrayList<>(reservations.keySet());
    }

    /**
     * Returns all reservations associated with this account.
     *
     * @return list of reservations
     */
    public List<Reservation> getAllReservations() {
        return new ArrayList<>(reservations.values());
    }

    /**
     * Retrieves a reservation by reservation number.
     *
     * @param reservationNumber reservation identifier
     * @return reservation if found; otherwise {@code null}
     */
    public Reservation getReservation(String reservationNumber) {
        if (reservationNumber == null) {
            return null;
        }
        return reservations.get(reservationNumber.trim().toUpperCase());
    }

    /**
     * Returns the mailing address associated with this account.
     *
     * @return address
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Updates the account mailing address.
     *
     * @param newAddress new address
     * @throws IllegalParameter_Exception if {@code newAddress} is null
     */
    public void updateAddress(Address newAddress) {
        if (newAddress == null) {
            throw new IllegalParameter_Exception(accountNumber, "N/A", "Address cannot be null.");
        }

        address.setAddress(
                newAddress.getStreet(),
                newAddress.getCity(),
                newAddress.getState(),
                newAddress.getZipCode()
        );
    }

    /**
     * Updates the account mailing address using individual fields.
     *
     * @param street  street name
     * @param city    city
     * @param state   state
     * @param zipCode zip code
     */
    public void updateAddress(String street, String city, String state, int zipCode) {
        address.setAddress(street, city, state, zipCode);
    }

    /**
     * Returns the account phone number.
     *
     * @return phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Updates the account phone number.
     *
     * @param newPhoneNumber new phone number
     * @throws IllegalParameter_Exception if the value is invalid
     */
    public void setPhoneNumber(String newPhoneNumber) {
        if (newPhoneNumber == null || newPhoneNumber.isEmpty()) {
            throw new IllegalParameter_Exception(accountNumber, "N/A", "Phone number cannot be empty.");
        }
        phoneNumber = newPhoneNumber;
    }

    /**
     * Returns the account email address.
     *
     * @return email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Updates the account email address.
     *
     * @param newEmail new email address
     * @throws IllegalParameter_Exception if the email is invalid
     */
    public void setEmail(String newEmail) {
        if (newEmail == null || !newEmail.contains("@")) {
            throw new IllegalParameter_Exception(accountNumber, "N/A", "Invalid email address.");
        }
        email = newEmail;
    }

    /**
     * Adds a reservation to this account.
     *
     * @param reservation reservation to add
     * @throws IllegalParameter_Exception if {@code reservation} is null
     */
    public void addReservation(Reservation reservation) {
        if (reservation == null) {
            throw new IllegalParameter_Exception("N/A", "N/A", "Reservation cannot be null.");
        }

        String key = reservation.getReservationNumber().trim().toUpperCase();
        if (reservations.containsKey(key)) {
            return;
        }

        reservations.put(key, reservation);

        try {
            Manager.saveReservationToFile(reservation);
        } catch (IllegalSave_Exception e) {
            System.out.println("Error saving reservation: " + e.getMessage());
        }
    }

    /**
     * Updates an existing reservation with new details.
     *
     * @param reservationNumber reservation identifier
     * @param updatedReservation reservation containing updated values
     * @throws IllegalState_Exception if the reservation cannot be updated
     * @throws IllegalOperation_Exception if the reservation does not exist
     */
    public void updateReservation(String reservationNumber, Reservation updatedReservation)
            throws IllegalState_Exception, IllegalOperation_Exception {

        if (reservationNumber == null || reservationNumber.trim().isEmpty()) {
            throw new IllegalOperation_Exception(
                    "Update Reservation",
                    accountNumber,
                    "N/A",
                    "Reservation number is required."
            );
        }

        String key = reservationNumber.trim().toUpperCase();
        Reservation currentReservation = reservations.get(key);

        if (currentReservation == null) {
            throw new IllegalOperation_Exception(
                    "Update Reservation",
                    accountNumber,
                    key,
                    "Reservation does not exist."
            );
        }

        if (currentReservation.isLocked()) {
            throw new IllegalState_Exception(
                    accountNumber,
                    key,
                    "Cannot update a completed or cancelled reservation."
            );
        }

        boolean changed = currentReservation.updateDetailsFrom(updatedReservation);
        if (!changed) {
            return;
        }

        try {
            Manager.saveReservationToFile(currentReservation);
        } catch (IllegalSave_Exception e) {
            System.out.println("Error saving updated reservation: " + e.getMessage());
        }
    }

    /**
     * Returns the serialized account record used for persistence.
     *
     * @return formatted account record
     */
    @Override
    public String toString() {
        return String.format(
                "%s,%s,%s,%s,%d,%s,%s",
                accountNumber,
                address.getStreet(),
                address.getCity(),
                address.getState(),
                address.getZipCode(),
                phoneNumber,
                email
        );
    }
}

