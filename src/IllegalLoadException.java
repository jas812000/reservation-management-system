/**
 * Exception thrown when an illegal load operation is performed.
 * This could indicate that an attempt was made to load an invalid or
 * disallowed resource, object, or data into a system.
 * - the specified file does not exist.
 * - An attempt is made to load an invalid or disallowed resource.
 * The exception message should indicate what failed (account file versus reservation file) and
 * the filename that could not be loaded. The message should include the account’s number that was being loaded.
 */
public class IllegalLoadException extends RuntimeException {}
