package com.jamesstevens.rms;

import com.jamesstevens.rms.exceptions.*;
import com.jamesstevens.rms.exceptions.IllegalStateException;
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

    private final String accountNumber;
    private final Name name;
    private final Address address;
    private String phoneNumber;
    private String email;
    private final Map<String, Reservation> reservations;

    /**
     * Constructs a new {@code Account}.
     *
     * @param accountNumber unique account identifier
     * @param name          name associated with the account
     * @param address       mailing address associated with the account
     * @param phoneNumber   contact phone number
     * @param email         contact email address
     * @throws IllegalParameterException if any required parameter is invalid
     */
    public Account(String accountNumber, Name name, Address address, String phoneNumber, String email) {
        if (name == null) {
            throw new IllegalParameterException(
                    "N/A",
                    "N/A",
                    "Name cannot be empty."
            );
        }

        if (address == null) {
            throw new IllegalParameterException(
                    "N/A",
                    "N/A",
                    "Mailing address cannot be empty."
            );
        }

        this.accountNumber = validateAccountNumber(accountNumber);
        this.name = name;
        this.address = address;
        this.phoneNumber = validatePhoneNumber(phoneNumber, this.accountNumber);
        this.email = validateEmail(email, this.accountNumber);
        this.reservations = new HashMap<>();
    }

    /**
     * Creates an {@code Account} instance from a serialized account record.
     * <p>
     * Invalid or malformed persisted data is reported as an
     * {@link IllegalLoadException}. If parsing or domain validation fails,
     * the original exception is preserved as the cause.
     * </p>
     *
     * @param data comma-separated account data
     * @return parsed {@code Account} instance
     * @throws IllegalLoadException if the account record cannot be loaded or parsed
     */
    public static Account fromString(String data) throws IllegalLoadException {
        try {
            String[] parts = data.split(",");

            if (parts.length < 9) {
                throw new IllegalLoadException("Account", "N/A", "Data corrupted");
            }

            Name name = new Name(
                    parts[1],
                    parts[2]
            );

            Address address = new Address(
                    parts[3],
                    parts[4],
                    parts[5],
                    parts[6]
            );

            return new Account(
                    parts[0],
                    name,
                    address,
                    parts[7],
                    parts[8]
            );

        } catch (IllegalLoadException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new IllegalLoadException(
                    "Account",
                    "N/A",
                    "N/A",
                    e
            );
        }
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
     * Returns the name associated with this account.
     *
     * @return account holder's name
     */
    public Name getName() { return name; }

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
     * @throws IllegalParameterException if {@code newAddress} is null
     */
    public void updateAddress(Address newAddress) {
        if (newAddress == null) {
            throw new IllegalParameterException(accountNumber, "N/A", "Address cannot be null.");
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
     * @param state   two-letter state abbreviation
     * @param zipCode five-digit ZIP code
     * @throws IllegalParameterException if any address field is invalid
     */
    public void updateAddress(String street, String city, String state, String zipCode) {
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
     * @throws IllegalParameterException if the phone number is null, blank, or does not contain exactly 10 digits
     *
     */
    public void setPhoneNumber(String newPhoneNumber) {
        phoneNumber = validatePhoneNumber(newPhoneNumber, accountNumber);
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
     * @throws IllegalParameterException if the email address is null, blank, or has an invalid format
     *
     */
    public void setEmail(String newEmail) {
        email = validateEmail(newEmail, accountNumber);
    }

    /**
     * Adds a reservation to this account and persists it to storage.
     *
     * @param reservation reservation to add
     * @throws IllegalParameterException if {@code reservation} is null or belongs to a different account
     * @throws DuplicateObjectException if the reservation already exists
     * @throws IllegalSaveException if the reservation cannot be persisted
     */
    public void addReservation(Reservation reservation) {
        if (reservation == null) {
            throw new IllegalParameterException("N/A", "N/A", "Reservation cannot be null.");
        }

        if (!accountNumber.equals(reservation.getAccountNumber())) {
            throw new IllegalParameterException(
                    accountNumber,
                    reservation.getReservationNumber(),
                    "Reservation account number does not match this account."
            );
        }

        String key = reservation.getReservationNumber().trim().toUpperCase();
        if (reservations.containsKey(key)) {
            throw new DuplicateObjectException(accountNumber, key);
        }

        reservations.put(key, reservation);

        try {
            Manager.saveReservationToFile(reservation);
        } catch (IllegalSaveException e) {
            reservations.remove(key);
            throw e;
        }
    }

    /**
     * Adds an existing persisted reservation to this account in memory
     * without writing the reservation back to disk.
     *
     * @param reservation reservation loaded from persistent storage
     * @throws IllegalParameterException if {@code reservation} is null or belongs to a different account
     */
    void addLoadedReservation(Reservation reservation) {
        if (reservation == null) {
            throw new IllegalParameterException("N/A", "N/A", "Reservation cannot be null.");
        }

        if (!accountNumber.equals(reservation.getAccountNumber())) {
            throw new IllegalParameterException(
                    accountNumber,
                    reservation.getReservationNumber(),
                    "Reservation account number does not match this account."
            );
        }

        String key = reservation.getReservationNumber().trim().toUpperCase();

        if (reservations.containsKey(key)) {
            return;
        }

        reservations.put(key, reservation);
    }

    /**
     * Updates an existing reservation with new details.
     *
     * @param reservationNumber  reservation identifier
     * @param updatedReservation reservation containing updated values
     * @throws IllegalParameterException   if the reservation number is null or blank
     * @throws NullReservationException    if the reservation does not exist
     * @throws IllegalStateException       if the reservation cannot be updated in its current state
     */
    public void updateReservation(String reservationNumber, Reservation updatedReservation)
            throws IllegalParameterException, NullReservationException, IllegalStateException {

        if (reservationNumber == null || reservationNumber.trim().isEmpty()) {
            throw new IllegalParameterException(
                    accountNumber,
                    "N/A",
                    "Reservation number is required."
            );
        }

        String key = reservationNumber.trim().toUpperCase();
        Reservation currentReservation = reservations.get(key);

        if (currentReservation == null) {
            throw new NullReservationException(
                    accountNumber,
                    key,
                    "Reservation does not exist."
            );
        }

        if (currentReservation.isLocked()) {
            throw new IllegalStateException(
                    accountNumber,
                    key,
                    "Cannot update a completed or cancelled reservation."
            );
        }

        Reservation updatedCopy = Reservation.fromString(currentReservation.toString());

        boolean changed = updatedCopy.updateDetailsFrom(updatedReservation);

        if (!changed) {
            return;
        }

        Manager.saveReservationToFile(updatedCopy);
        reservations.put(key, updatedCopy);

    }

    private static String validateAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.isBlank()) {
            throw new IllegalParameterException(
                    "N/A",
                    "N/A",
                    "Account number cannot be empty."
            );
        }

        return accountNumber.trim().toUpperCase();
    }

    private static String validatePhoneNumber(String phoneNumber, String accountNumber) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalParameterException(
                    accountNumber,
                    "N/A",
                    "Phone number cannot be empty."
            );
        }

        String normalizedPhoneNumber = phoneNumber.trim();
        String digits = normalizedPhoneNumber.replaceAll("\\D", "");

        if (digits.length() != 10) {
            throw new IllegalParameterException(
                    accountNumber,
                    "N/A",
                    "Phone number must contain exactly 10 digits."
            );
        }

        return normalizedPhoneNumber;
    }

    private static String validateEmail(String email, String accountNumber) {
        if (email == null || email.isBlank()) {
            throw new IllegalParameterException(
                    accountNumber,
                    "N/A",
                    "Invalid email address."
            );
        }

        String normalizedEmail = email.trim();

        if (!normalizedEmail.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalParameterException(
                    accountNumber,
                    "N/A",
                    "Invalid email address."
            );
        }

        return normalizedEmail;
    }

    /**
     * Returns the serialized account record used for persistence.
     *
     * @return formatted account record
     */
    @Override
    public String toString() {
        return String.format(
                "%s,%s,%s,%s,%s,%s,%s,%s,%s",
                accountNumber,
                name.getFirstName(),
                name.getLastName(),
                address.getStreet(),
                address.getCity(),
                address.getState(),
                address.getZipCode(),
                phoneNumber,
                email
        );
    }
}

