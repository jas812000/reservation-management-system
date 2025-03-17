// Imports all classes from the TestPackage inside the com.swen_646_project_1 package.
package com.swen_646_project_1.TestPackage;

/*
 * Imports necessary classes for testing reservation updates.
 * - `Account`: Needed to retrieve and modify reservation data.
 * - `IllegalState_Exception`: Exception thrown when modifying a reservation that is already completed or canceled.
 * - `IllegalOperation_Exception`: Exception thrown when an update is attempted on a non-existent reservation.
 */
import com.swen_646_project_1.Account;
import com.swen_646_project_1.exceptions.IllegalState_Exception;
import com.swen_646_project_1.exceptions.IllegalOperation_Exception;

/**
 * Test class for updating reservations.
 * Ensures proper validation when modifying reservations.
 */
public class UpdateReservationTest {
    /**
     * Tests updating an existing reservation.
     * - Prompts the user to select an account.
     * - Retrieves the selected reservation.
     * - Attempts to update the reservation, handling exceptions if the update is not allowed.
     */
    public static void testUpdateReservation() {
        // Select an account from available accounts
        String reservationNumber = TestHelper.getValidatedReservation();
        if (reservationNumber == null) return; // Exits early if no valid reservation is selected

        // Retrieve the associated account from the reservation number
        Account account = TestHelper.getAccountFromReservation(reservationNumber);
        if (account == null) return; // Exit if account is not found

        try {
            account.updateReservation(reservationNumber, null); // Now 'account' is defined
            System.out.println("Reservation updated successfully.");
        } catch (IllegalState_Exception | IllegalOperation_Exception e) {
            System.out.println("Error updating reservation: " + e.getMessage());
        } // End try-catch statements
    } // End testUpdateReservation method
} // End UpdateReservationTest class
