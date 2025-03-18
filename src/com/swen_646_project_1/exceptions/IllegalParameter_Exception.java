// Declares the package name for the project, grouping related classes together.
package com.swen_646_project_1.exceptions;

/*
 * Throws IllegalParameter_Exception if there are invalid parameters:
 *      - null values when a parameter is required
 *      - newEmail is null or does not contain '@'.
 *      - numNights value is zero or negative
 * The generated exception message should indicate the account number and/or reservation number and why it failed.
 */
public class IllegalParameter_Exception extends RuntimeException {

    /**
     * Constructor for IllegalParameter_Exception.
     * @param accountNumber The account number associated with the invalid parameter.
     * @param reservationNumber The reservation number associated with the invalid parameter.
     * @param reason The reason why the parameter is invalid.
     */
    public IllegalParameter_Exception(String accountNumber, String reservationNumber, String reason) {
        super("Invalid Parameter: " + reason + " | Account: " + accountNumber + ", Reservation: " + reservationNumber);
    } // End IllegalParameter_Exception constructor

} // End IllegalParameter_Exception class

