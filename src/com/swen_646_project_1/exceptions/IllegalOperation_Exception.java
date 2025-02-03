// Declares the package name for the project, grouping related classes together.
package com.swen_646_project_1.exceptions;

/**
 * Exception thrown when an illegal or unsupported operation is attempted.
 * - cancelling or completing reservation if it is not finalized.
 * The generated exception message should indicate the operation that was attempted,
 * account ID, reservation number, and details why exactly it failed.
 */
public class IllegalOperation_Exception extends RuntimeException {
    public IllegalOperation_Exception(String operation, String accountNumber, String reservationNumber, String details) {
        super("Illegal operation: " + operation + " | Account: " + accountNumber + ", Reservation: " + reservationNumber + " | " + details);
    } // End IllegalOperation_Exception constructor

    /**
     * Returns a string representation of the exception.
     * @return A formatted string containing the exception message.
     */
    @Override
    public String toString() {
        return getMessage();
    } // End toString method
} // End IllegalOperation_Exception class

