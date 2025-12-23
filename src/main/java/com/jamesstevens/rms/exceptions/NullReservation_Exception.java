package com.jamesstevens.rms.exceptions;

/**
 * Runtime exception thrown when a reservation is missing, not found, or otherwise unavailable.
 * <p>
 * Common cases include attempting to access or modify a reservation that does not exist, or attempting
 * to add a {@code null} reservation reference.
 * </p>
 */
public class NullReservation_Exception extends RuntimeException {

  /**
   * Creates a new {@code NullReservation_Exception}.
   *
   * @param accountNumber      the account number associated with the failure
   * @param reservationNumber  the reservation number associated with the failure, or {@code "N/A"} if unknown
   * @param reason             a human-readable explanation of why the operation is not allowed
   */
  public NullReservation_Exception(String accountNumber, String reservationNumber, String reason) {
    super("Reservation error: " + reason + " | Account: " + accountNumber + ", Reservation: " + reservationNumber);
  }
}
