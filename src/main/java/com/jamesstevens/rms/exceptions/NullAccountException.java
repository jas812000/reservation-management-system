package com.jamesstevens.rms.exceptions;

/**
 * Runtime exception thrown when an account is missing, not found, or otherwise unavailable.
 * <p>
 * This is typically raised when an operation is attempted on a non-existent account, or when
 * persistence-related actions require an account that is not present.
 * </p>
 */
public class NullAccountException extends RuntimeException {

    /**
     * Creates a new {@code NullAccountException}.
     *
     * @param accountNumber the account number associated with the failure, or {@code "N/A"} if unknown
     * @param reason        a human-readable explanation of why the operation is not allowed
     */
    public NullAccountException(String accountNumber, String reason) {
        super("Account error: " + reason + " | Account: " + accountNumber);
    }
}
