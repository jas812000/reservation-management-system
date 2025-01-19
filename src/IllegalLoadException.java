/**
 * Exception thrown when an illegal load operation is performed.
 * This could indicate that an attempt was made to load an invalid or
 * disallowed resource, object, or data into a system.
 * - the file does not exist.
 * The exception message should indicate what failed (account file versus reservation file) and
 * the filename that could not be loaded. The message should include the account’s number that was being loaded.
 */
public class IllegalLoadException extends RuntimeException {}
