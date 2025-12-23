package com.jamesstevens.rms.tests;

import com.jamesstevens.rms.Account;
import com.jamesstevens.rms.Manager;
import com.jamesstevens.rms.reservation.Reservation;

/**
 * Interactive/manual test utility for changing a reservation's status.
 * <p>
 * Supports cancelling or completing an existing reservation selected by the user.
 * </p>
 */
@SuppressWarnings("unused")
public class ReservationStatusTest {

    /**
     * Attempts to modify the status of a reservation.
     * <p>
     * Supported operations:
     * <ul>
     *   <li>{@code "cancel"} - cancels the reservation</li>
     *   <li>{@code "complete"} - completes the reservation</li>
     * </ul>
     * After applying the status change, the reservation is persisted to storage.
     * </p>
     *
     * @param operation operation to perform ({@code "cancel"} or {@code "complete"})
     */
    public static void testModifyReservationStatus(String operation) {
        String reservationNumber = TestHelper.getValidatedReservation();
        if (reservationNumber == null) {
            return;
        }

        Account account = TestHelper.getAccountFromReservation(reservationNumber);
        if (account == null) {
            return;
        }

        Reservation reservation = account.getReservation(reservationNumber);
        if (reservation == null) {
            System.out.println("Error: Reservation not found.");
            return;
        }

        try {
            if ("cancel".equalsIgnoreCase(operation)) {
                reservation.cancelReservation();
                System.out.println("     ***** Reservation cancelled successfully. *****     ");
            } else if ("complete".equalsIgnoreCase(operation)) {
                reservation.completeReservation();
                System.out.println("     ***** Reservation completed successfully. *****     ");
            } else {
                System.out.println("Error: Unsupported operation: " + operation);
                return;
            }

            Manager.saveReservationToFile(reservation);

        } catch (Exception e) {
            TestHelper.handleException(e, operation + " reservation");
        }
    }
}
