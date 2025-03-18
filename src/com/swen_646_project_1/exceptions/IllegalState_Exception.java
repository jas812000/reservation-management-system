// Declares the package name for the project, grouping related classes together.
package com.swen_646_project_1.exceptions;

/*
 * Throws IllegalState_Exception if the reservation is already complete, cancelled or for a past date.
 *      - user tries to modify/change a completed reservation. ("Cannot modify a completed reservation.")
 *      - user tries to modify/change a cancelled reservation. ("Cannot modify a cancelled reservation.")
 *      - user tries to modify/change a past reservation. ("Cannot modify a past reservation.")
 * The generated exception message should indicate the account number and/or reservation number and why it failed.
 */
public class IllegalState_Exception extends RuntimeException {

    /**
     * Constructor for IllegalState_Exception.
     * @param accountNumber The account number associated with the error.
     * @param reservationNumber The reservation number associated with the error.
     * @param reason The reason why the operation is not allowed.
     */
    public IllegalState_Exception(String accountNumber, String reservationNumber, String reason) {
        super("Illegal operation: " + reason + " | Account: " + accountNumber + ", Reservation: " + reservationNumber);
    } // End IllegalState_Exception constructor

} // End IllegalState_Exception class
