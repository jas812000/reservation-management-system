package com.jamesstevens.rms.exceptions;

/**
 * Runtime exception thrown when invalid input parameters are supplied.
 * <p>
 * Examples include required parameters being {@code null}, an invalid email format, or numeric
 * values that violate constraints (e.g., non-positive number of nights).
 * </p>
 */
public class IllegalParameter_Exception extends RuntimeException {

    /**
     * Creates a new {@code IllegalParameter_Exception}.
     *
     * @param accountNumber     the account number associated with the invalid parameter
     * @param reservationNumber the reservation number associated with the invalid parameter
     * @param reason            a human-readable explanation of why the parameter is invalid
     */
    public IllegalParameter_Exception(String accountNumber, String reservationNumber, String reason) {
        super("Invalid Parameter: " + reason + " | Account: " + accountNumber + ", Reservation: " + reservationNumber);
    }
}
