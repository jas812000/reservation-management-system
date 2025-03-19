// Imports all classes from the TestPackage inside the com.swen_646_project_1 package.
package com.swen_646_project_1.TestPackage;

/*
 * Imports necessary classes for managing and testing reservations.
 * - `Account`: Required to retrieve and manage reservations associated with an account.
 * - `Manager`: Manages accounts and reservations in the system.
 * - `Reservation`: Represents a generic reservation and provides base functionality.
 */
import com.swen_646_project_1.Account;
import com.swen_646_project_1.Manager;
import com.swen_646_project_1.reservation.Reservation;

/**
 * Handles cancellation and completion of reservations in a single class.
 * This class allows users to:
 * - Select an account and a reservation.
 * - Modify the reservation's status (Cancel or Complete).
 * - Handle exceptions if the modification is not permitted.
 */
public class ReservationStatusTest {
    /**
     * Tests modifying a reservation's status (Cancel or Complete).
     * - Cancels a reservation if it's in the future.
     * - Completes a reservation if it meets the conditions.
     * - Prompts the user to select an account and a reservation.
     * - Based on the provided operation, attempts to cancel or complete the reservation.
     * - Handles any exceptions if the modification is invalid.
     * @param operation The operation to perform ("cancel" or "complete").
     */
    public static void testModifyReservationStatus(String operation) {
        // Prompt user to select an account
        String reservationNumber = TestHelper.getValidatedReservation();
        if (reservationNumber == null) return; // Exits early if no valid reservation is selected

        // Retrieve the associated account from the reservation number
        Account account = TestHelper.getAccountFromReservation(reservationNumber);
        if (account == null) return; // Exit if account is not found

        // Retrieve the actual reservation
        Reservation reservation = account.getReservation(reservationNumber);
        if (reservation == null) {
            System.out.println("Error: Reservation not found.");
            return;
        } // End if statement


        try {
            if (operation.equalsIgnoreCase("cancel")) {
                reservation.cancelReservation();
                System.out.println("     ***** Reservation cancelled successfully. *****     ");
            } else if (operation.equalsIgnoreCase("complete")) {
                reservation.completeReservation();
                System.out.println("     ***** Reservation completed successfully. *****     ");
            } // End if-else statements

            // Save the updated reservation status
            Manager.saveReservationToFile(reservation);

        } catch (Exception e) {
            TestHelper.handleException(e, operation + " reservation");
        } // End try-catch statements
    } // End testModifyReservationStatus method
} // End ReservationStatusTest class