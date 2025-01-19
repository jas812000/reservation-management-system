/**
 * Exception thrown when an illegal or unsupported operation is attempted.
 * - cancelling or completing reservation if it is not finalized.
 * The generated exception message should indicate the operation that was attempted,
 * account ID, reservation number, and details why exactly it failed.
 */
public class IllegalOperationException extends RuntimeException {}
