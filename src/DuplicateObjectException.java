/**
 * Exception thrown when a duplicate object is encountered.
 * This can occur when attempting to insert an object that already exists
 * in a collection, database, or any system that enforces uniqueness.
 * The generated exception message should indicate the account number and/or reservation number and why it failed.
 */
public class DuplicateObjectException extends RuntimeException {}
