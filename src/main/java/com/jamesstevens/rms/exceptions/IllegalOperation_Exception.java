package com.jamesstevens.rms.exceptions;

/**
 * Runtime exception thrown when an illegal or unsupported operation is attempted.
 * <p>
 * For example, cancelling or completing a reservation that has not been finalized.
 * </p>
 */
public class IllegalOperation_Exception extends RuntimeException {

    /**
     * Creates a new {@code IllegalOperation_Exception}.
     *
     * @param operation         the operation that was attempted (e.g., {@code "Cancel"}, {@code "Complete"})
     * @param accountNumber     the account number associated with the operation
     * @param reservationNumber the reservation number associated with the operation
     * @param details           additional details explaining why the operation failed
     */
    public IllegalOperation_Exception(
            String operation,
            String accountNumber,
            String reservationNumber,
            String details
    ) {
        super("Illegal operation: " + operation + " | Account: " + accountNumber + ", Reservation: "
                + reservationNumber + " | " + details);
    }
}


