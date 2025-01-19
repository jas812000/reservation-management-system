/**
 * Throws IllegalStateException if the reservation is already complete, cancelled or for a past date.
 *      - user tries to modify/change a completed reservation. ("Cannot modify a completed reservation.")
 *      - user tries to modify/change a cancelled reservation. ("Cannot modify a cancelled reservation.")
 *      - user tries to modify/change a past reservation. ("Cannot modify a past reservation.")
 * The generated exception message should indicate the account number and/or reservation number and why it failed.
 */

public class IllegalStateException extends RuntimeException{}
