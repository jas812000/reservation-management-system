// Declares the package name for the project, grouping related classes together.
package com.swen_646_project_1.exceptions;

/*
 * Exception thrown when an illegal load operation is performed.
 * This could indicate that an attempt was made to load an invalid or
 * disallowed resource, object, or data into a system.
 * - the specified file does not exist.
 * - An attempt is made to load an invalid or disallowed resource.
 * The exception message should indicate what failed (account file versus reservation file) and
 * the filename that could not be loaded. The message should include the account’s number that was being loaded.
 */
public class IllegalLoad_Exception extends RuntimeException {

    /**
     * Constructor for IllegalLoad_Exception.
     * @param failedObject The type of object that failed to load (Account or Reservation).
     * @param fileName The name of the file that could not be loaded.
     * @param accountNumber The account number associated with the failed operation.
     */
    public IllegalLoad_Exception(String failedObject, String fileName, String accountNumber) {
        super("Failed to load " + failedObject + " from file: " + fileName + " | Account: " + accountNumber);
    } // End IllegalLoad_Exception constructor

} // End IllegalLoad_Exception class

