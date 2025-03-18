// Declares the package name for the project, grouping related classes together.
package com.swen_646_project_1.exceptions;

/*
 * Exception thrown when an illegal or unsupported operation is attempted.
 * - cancelling or completing reservation if it is not finalized.
 * The generated exception message should indicate the operation that was attempted,
 * account ID, reservation number, and details why exactly it failed.
 */
public class IllegalOperation_Exception extends RuntimeException {

    /**
     * Constructor for IllegalOperation_Exception.
     * @param operation The operation that was attempted (e.g., "Cancel", "Complete").
     * @param accountNumber The account number associated with the operation.
     * @param reservationNumber The reservation number associated with the operation.
     * @param details Additional details explaining why the operation failed.
     */
    public IllegalOperation_Exception(String operation, String accountNumber, String reservationNumber,
                                      String details) {
        super("Illegal operation: " + operation + " | Account: " + accountNumber + ", Reservation: " +
                reservationNumber + " | " + details);
    } // End IllegalOperation_Exception constructor

} // End IllegalOperation_Exception class

