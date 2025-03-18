// Declares the package name for the project, grouping related classes together.
package com.swen_646_project_1.exceptions;

/*
 * Throws NullAccount_Exception if an account is missing or not found.
 *    - User tries to perform an action on a non-existent account. ("Account not found.")
 *    - User tries to create a directory or save data for a missing account. ("Cannot proceed with a missing account.")
 * The generated exception message should indicate the account number (if available) and why it failed.
 */
public class NullAccount_Exception extends RuntimeException {

    /**
     * Constructor for NullAccount_Exception.
     * @param accountNumber The account number associated with the error (or "N/A" if unknown).
     * @param reason The reason why the operation is not allowed.
     */
    public NullAccount_Exception(String accountNumber, String reason) {
        super("Account error: " + reason + " | Account: " + accountNumber);
    } // End NullAccount_Exception constructor

} // End NullAccount_Exception class
