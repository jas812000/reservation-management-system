// Imports all classes from the TestPackage inside the com.swen_646_project_1 package.
package com.swen_646_project_1.TestPackage;

/*
 * Imports necessary classes for managing accounts, reservations, and user interactions.
 * - `Manager`: Provides system-wide access to accounts and reservations.
 * - `NullAccount_Exception`: Thrown when an account cannot be found in the system.
 * - `NullReservation_Exception`: Thrown when a reservation is not associated with an account.
 * - `Scanner`: Captures user input for interactive menu selection in test cases.
 */
import com.swen_646_project_1.Manager;
import com.swen_646_project_1.exceptions.NullAccount_Exception;
import com.swen_646_project_1.exceptions.NullReservation_Exception;
import java.util.Scanner;

/**
 * Interactive test for findAccount and findReservation using menu system.
 */
public class InteractiveFindTest {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Manager manager = new Manager();

    /**
     * Interactive test for finding an account.
     */
    public static void findAccountInteractive() {
        System.out.print("\nEnter Account Number to search: ");
        String accountNumber = scanner.nextLine().trim();

        try {
            manager.findAccount(accountNumber);
        } catch (NullAccount_Exception e) {
            System.out.println("Error: " + e.getMessage());
        } // End try-catch statements
    } // End findAccountInteractive method

    /**
     * Interactive test for finding a reservation.
     */
    public static void findReservationInteractive() {
        System.out.print("\nEnter Account Number for the reservation: ");
        String accountNumber = scanner.nextLine().trim();

        System.out.print("Enter Reservation Number: ");
        String reservationNumber = scanner.nextLine().trim();

        try {
            manager.findReservation(accountNumber, reservationNumber);
        } catch (NullReservation_Exception e) {
            System.out.println("Error: " + e.getMessage());
        } // End try-catch statements
    } // End findReservationInteractive method
} // End InteractiveFindTest class
