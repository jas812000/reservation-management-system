package com.jamesstevens.rms.exceptions;

/**
 * Runtime exception thrown when a load operation is illegal or fails to complete.
 * <p>
 * This may indicate a missing file, an invalid resource, or an attempt to load disallowed data.
 * The exception message should identify what failed (e.g., account vs. reservation) and the file
 * involved.
 * </p>
 */
public class IllegalLoad_Exception extends RuntimeException {

    /**
     * Creates a new {@code IllegalLoad_Exception}.
     *
     * @param failedObject  the type of object that failed to load (e.g., {@code "Account"} or {@code "Reservation"})
     * @param fileName      the file name or path that could not be loaded
     * @param accountNumber the account number associated with the failed operation
     */
    public IllegalLoad_Exception(String failedObject, String fileName, String accountNumber) {
        super("Failed to load " + failedObject + " from file: " + fileName + " | Account: " + accountNumber);
    }
}


