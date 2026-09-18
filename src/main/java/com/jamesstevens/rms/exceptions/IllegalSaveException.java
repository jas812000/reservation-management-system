package com.jamesstevens.rms.exceptions;

/**
 * Runtime exception thrown when saving data to a file fails.
 * <p>
 * This may occur when account or reservation data cannot be written due to I/O errors, missing
 * directories, permissions issues, or invalid file paths.
 * </p>
 */
public class IllegalSaveException extends RuntimeException {

  /**
   * Creates a new {@code IllegalSaveException}.
   *
   * @param failedObject  the type of object that failed to save (e.g., {@code "Account"} or {@code "Reservation"})
   * @param fileName      the file name or path where saving failed
   * @param accountNumber the account number related to the operation
   */
  public IllegalSaveException(String failedObject, String fileName, String accountNumber) {
    super("Failed to save " + failedObject + " to file: " + fileName + " | Account: " + accountNumber);
  }

  /**
   * Creates a new {@code IllegalSaveException} while preserving the
   * underlying cause of the save failure.
   *
   * @param failedObject  the type of object that failed to save
   * @param fileName      the file name or path where saving failed
   * @param accountNumber the account number associated with the failed operation
   * @param cause         the underlying cause of the save failure
   */
  public IllegalSaveException(
          String failedObject,
          String fileName,
          String accountNumber,
          Throwable cause) {

    super(
            "Failed to save " + failedObject + " to file: " + fileName
                    + " | Account: " + accountNumber,
            cause
    );
  }
}
