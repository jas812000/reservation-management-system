// Declares the package name for the project, grouping related classes together.
package com.swen_646_project_1.exceptions;

/**
 * Updates the email address for the account.
 * Throws IllegalArgumentException if there are invalid parameters:
 *      - null values when a paraeter is required
 *      - newEmail is null or does not contain '@'.
 *      - numNights value is zero or negative
 * The generated exception message should indicate the account number and/or reservation number and why it failed.
 *
 * @param phoneNumber Contact phone number of the user
 * @param newEmail The new email address to update.
 * @param numNights The number of nights for the stay.
 * @param startDate The start date of the reservation
 * @param lodgingSizeSqFt Size of the lodging in square feet.
 */
public class IllegalArgumentException extends RuntimeException {}
