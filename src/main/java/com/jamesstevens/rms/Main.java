// Declares the package name for the project, grouping related classes together.
package com.jamesstevens.rms;

/*
 * Imports necessary classes for managing reservations, accounts, and user interactions.
 * - `com.swen_646_project_1.TestPackage.*`: Includes unit test classes for:
 *   - Account creation
 *   - Reservation management
 *   - Data integrity validation
 * - 'io': file handling
 * - `Scanner`: Captures user input for interactive menu selection in test cases.
 */
import java.util.Scanner;

/**
 * Main class that serves as the entry point for the Reservation Management System.
 * Provides a console-based menu for managing accounts and reservations.
 */
public class Main {

    public static void main(String[] args) {

        // Creates a scanner object
        Scanner scanner = new Scanner(System.in);

        // Loop that continues until the user select a correct option
        while (true) {
            // Display main menu options
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

            // Handle menu selection
            switch (choice) {
                case "9" -> { 
                    System.out.println("Exiting system. Goodbye!");
                    return;
                } // End case "9" statement
                default -> System.out.println("CLI options not implemented yet. Run tests using: mvn test");
            } // End switch statements
        } // End while loop
    } // End Main method
} // End Main Class
