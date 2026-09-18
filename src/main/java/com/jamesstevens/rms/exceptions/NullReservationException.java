package com.jamesstevens.rms.exceptions;

/**
 * Runtime exception thrown when a required reservation cannot be found
 * or is otherwise unavailable.
 * <p>
 * This is typically raised when an operation targets a reservation
 * identifier that does not exist for the specified account.
 * </p>
 */
public class NullReservationException extends RuntimeException {

  /**
   * Creates a new {@code NullReservationException}.
   *
   * @param accountNumber      the account number associated with the failure
   * @param reservationNumber  the reservation number associated with the failure, or {@code "N/A"} if unknown
   * @param reason             a human-readable explanation of why the operation is not allowed
   */
  public NullReservationException(String accountNumber, String reservationNumber, String reason) {
    super("Reservation error: " + reason + " | Account: " + accountNumber + ", Reservation: " + reservationNumber);
  }
}
