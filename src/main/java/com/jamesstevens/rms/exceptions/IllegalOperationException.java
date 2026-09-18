package com.jamesstevens.rms.exceptions;

/**
 * Runtime exception thrown when an operation is unsupported or not permitted
 * for the requested object or context.
 * <p>
 * For example, attempting to assign a separate mailing address to a
 * reservation type that does not support one.
 * </p>
 */
public class IllegalOperationException extends RuntimeException {

    /**
     * Creates a new {@code IllegalOperationException}.
     *
     * @param operation         the operation that was attempted (e.g., {@code "Cancel"}, {@code "Complete"})
     * @param accountNumber     the account number associated with the operation
     * @param reservationNumber the reservation number associated with the operation
     * @param details           additional details explaining why the operation failed
     */
    public IllegalOperationException(
            String operation,
            String accountNumber,
            String reservationNumber,
            String details
    ) {
        super("Illegal operation: " + operation + " | Account: " + accountNumber + ", Reservation: "
                + reservationNumber + " | " + details);
    }
}


