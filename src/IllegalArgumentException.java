/**
 * Updates the email address for the account.
 * Throws IllegalArgumentException if there are invalid parameters:
 *      - null values when a paraeter is required
 *      - newEmail is null or does not contain '@'.
 *      - numNights value is zero or negative
 * The generated exception message should indicate the account number and/or reservation number and why it failed.
 *
 * @param newEmail The new email address to update.
 * @param numNights The number of nights for the stay.
 */
public class IllegalArgumentException extends RuntimeException {}
