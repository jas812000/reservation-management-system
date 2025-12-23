package com.jamesstevens.rms.exceptions;

/**
 * Runtime exception thrown when a duplicate object is encountered where uniqueness is required.
 * <p>
 * This commonly occurs when attempting to insert an object that already exists in a collection,
 * database, or any system enforcing unique keys.
 * </p>
 */
public class DuplicateObject_Exception extends RuntimeException {

    /**
     * Creates a new {@code DuplicateObject_Exception}.
     *
     * @param accountNumber     the account number associated with the duplicate object
     * @param reservationNumber the reservation number associated with the duplicate object
     */
    public DuplicateObject_Exception(String accountNumber, String reservationNumber) {
        super("Duplicate entry detected | Account: " + accountNumber + ", Reservation: " + reservationNumber);
    }
}


