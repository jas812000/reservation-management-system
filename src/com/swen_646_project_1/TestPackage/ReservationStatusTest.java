// Imports all classes from the TestPackage inside the com.swen_646_project_1 package.
package com.swen_646_project_1.TestPackage;

/*
 * Imports necessary class for testing reservation statuses.
 * - `Account`: Required to retrieve and manage reservations associated with an account.
 */
import com.swen_646_project_1.Account;

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

        try {
            if (operation.equalsIgnoreCase("cancel")) {
                account.cancelReservation(reservationNumber);
                System.out.println("Reservation cancelled successfully.");
            } else if (operation.equalsIgnoreCase("complete")) {
                account.completeReservation(reservationNumber);
                System.out.println("Reservation completed successfully.");
            } // End if-else statements
        } catch (Exception e) {
            TestHelper.handleException(e, operation + " reservation");
        } // End try-catch statements
    } // End testModifyReservationStatus method
} // End ReservationStatusTest class