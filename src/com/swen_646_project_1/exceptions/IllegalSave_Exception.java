// Declares the package name for the project, grouping related classes together.
package com.swen_646_project_1.exceptions;

/**
 * Exception thrown when an error occurs while saving data to a file.
 * - Occurs when account or reservation data cannot be written to a file.
 * - The generated exception message should indicate what failed (account file vs reservation file)
 *   and the filename that could not be saved.
 * - The message should include the account number and relevant file details.
 */
public class IllegalSave_Exception extends RuntimeException {

  /**
   * Constructor for IllegalSave_Exception.
   * @param failedObject The type of object that failed to save (Account or Reservation).
   * @param fileName The name of the file where saving failed.
   * @param accountNumber The account number related to the operation.
   */
  public IllegalSave_Exception(String failedObject, String fileName, String accountNumber) {
    super(STR."Failed to save \{failedObject} to file: \{fileName} | Account: \{accountNumber}");
  } // End IllegalSave_Exception constructor

  /**
   * Returns a string representation of the exception.
   * @return A formatted string containing the exception message.
   */
  @Override
  public String toString() {
    return getMessage();
  } // End toString method
} // End IllegalSave_Exception class
