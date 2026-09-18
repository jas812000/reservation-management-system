package com.jamesstevens.rms.exceptions;

/**
 * Runtime exception thrown when a duplicate object is encountered where uniqueness is required.
 * <p>
 * This commonly occurs when attempting to insert an object that already exists in a collection,
 * database, or any system enforcing unique keys.
 * </p>
 */
public class DuplicateObjectException extends RuntimeException {

    /**
     * Creates a new {@code DuplicateObjectException}.
     *
     * @param accountNumber     the account number associated with the duplicate object
     * @param reservationNumber the reservation number associated with the duplicate object
     */
    public DuplicateObjectException(String accountNumber, String reservationNumber) {
        super("Duplicate entry detected | Account: " + accountNumber + ", Reservation: " + reservationNumber);
    }
}


