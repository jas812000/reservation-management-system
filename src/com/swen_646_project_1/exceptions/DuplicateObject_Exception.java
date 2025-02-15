// Declares the package name for the project, grouping related classes together.
package com.swen_646_project_1.exceptions;

/**
 * Exception thrown when a duplicate object is encountered.
 * This can occur when attempting to insert an object that already exists
 * in a collection, database, or any system that enforces uniqueness.
 * The generated exception message should indicate the account number and/or reservation number and why it failed.
 */
public class DuplicateObject_Exception extends RuntimeException {
    public DuplicateObject_Exception(String accountNumber, String reservationNumber) {
        super("Duplicate entry detected | Account: " + accountNumber + ", Reservation: " + reservationNumber);
    } // End DuplicateObject_Exception constructor

    /**
     * Returns a string representation of the exception.
     * @return A formatted string containing the exception message.
     */
    @Override
    public String toString() {
        return getMessage();
    } // End toString method
} // End DuplicateObject_Exception class

