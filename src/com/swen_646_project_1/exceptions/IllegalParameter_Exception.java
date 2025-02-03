// Declares the package name for the project, grouping related classes together.
package com.swen_646_project_1.exceptions;

/**
 * Throws IllegalParameter_Exception if there are invalid parameters:
 *      - null values when a parameter is required
 *      - newEmail is null or does not contain '@'.
 *      - numNights value is zero or negative
 * The generated exception message should indicate the account number and/or reservation number and why it failed.
 */
public class IllegalParameter_Exception extends RuntimeException {
    public IllegalParameter_Exception(String accountNumber, String reservationNumber, String reason) {
        super("Invalid Parameter: " + reason + " | Account: " + accountNumber + ", Reservation: " + reservationNumber);
    } // End IllegalParameter_Exception constructor

    /**
     * Returns a string representation of the exception.
     * @return A formatted string containing the exception message.
     */
    @Override
    public String toString() {
        return getMessage();
    } // End toString method
} // End IllegalParameter_Exception class

