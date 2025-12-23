package com.jamesstevens.rms.exceptions;

/**
 * Runtime exception thrown when an operation is attempted in an invalid state.
 * <p>
 * Common examples include attempting to modify a reservation that is already completed, canceled,
 * or associated with a past date where modifications are disallowed.
 * </p>
 */
public class IllegalState_Exception extends RuntimeException {

    /**
     * Creates a new {@code IllegalState_Exception}.
     *
     * @param accountNumber     the account number associated with the failure
     * @param reservationNumber the reservation number associated with the failure
     * @param reason            a human-readable explanation of why the operation is not allowed
     */
    public IllegalState_Exception(String accountNumber, String reservationNumber, String reason) {
        super("Illegal operation: " + reason + " | Account: " + accountNumber + ", Reservation: " + reservationNumber);
    }
}
