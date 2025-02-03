// Declares the package name for the project, grouping related classes together.
package com.swen_646_project_1.exceptions;

/**
 * Throws NullReservation_Exception if a reservation is missing or not found.
 *      - User tries to access or modify a non-existent reservation. ("Reservation not found.")
 *      - User tries to add a reservation that is null. ("Cannot add a missing reservation.")
 * The generated exception message should indicate the account number and/or reservation number and why it failed.
 */
public class NullReservation_Exception extends RuntimeException {

  /**
   * Constructor for NullReservation_Exception.
   * @param accountNumber The account number associated with the error.
   * @param reservationNumber The reservation number associated with the error (or "N/A" if unknown).
   * @param reason The reason why the operation is not allowed.
   */
  public NullReservation_Exception(String accountNumber, String reservationNumber, String reason) {
    super("Reservation error: " + reason + " | Account: " + accountNumber + ", Reservation: " + reservationNumber);
  } // End NullReservation_Exception constructor

  /**
   * Returns a string representation of the exception.
   * @return A formatted string containing the exception message.
   */
  @Override
  public String toString() {
    return getMessage();
  } // End toString method
} // End NullReservation_Exception class
