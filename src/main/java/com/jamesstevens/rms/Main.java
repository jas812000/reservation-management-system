package com.jamesstevens.rms;

import java.util.Scanner;

/**
 * Application entry point for the Reservation Management System.
 * <p>
 * This class provides a simple console-based menu interface intended primarily
 * for manual testing and demonstration. Most system functionality is exercised
 * through automated unit tests.
 * </p>
 */
public class Main {

    /**
     * Launches the Reservation Management System CLI.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("========== Reservation Management System ==========");
            System.out.println("\t1. Create a New Account");
            System.out.println("\t2. Update Existing Account");
            System.out.println("\t3. Add a Reservation");
            System.out.println("\t4. Update a Reservation");
            System.out.println("\t5. Cancel a Reservation");
            System.out.println("\t6. Complete a Reservation");
            System.out.println("\t7. Find an Account");
            System.out.println("\t8. Find a Reservation");
            System.out.println("\t9. Exit");
            System.out.print("\n\tSelect an option (1-9): ");

            String choice = scanner.nextLine().trim();

            if ("9".equals(choice)) {
                System.out.println("Exiting system. Goodbye!");
                return;
            } else {
                System.out.println(
                        "CLI options are not implemented. Please run automated tests using: mvn test"
                );
            }
        }
    }
}
