package com.swen_646_project_1.TestPackage;

import java.util.Scanner;

public class OptionSelectionTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n>> Reservation Management System <<");
            System.out.println("1. Create a New Account");
            System.out.println("2. Update Existing Account");
            System.out.println("3. Add a Reservation");
            System.out.println("4. Update a Reservation");
            System.out.println("5. Cancel a Reservation");
            System.out.println("6. Complete a Reservation");
            System.out.println("7. Exit");

            System.out.print("Select an option (1-7): ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> CreateAccountTest.testCreateNewAccount();
                case "2" -> UpdateAccountTest.testUpdateExistingAccount();
                case "3" -> AddReservationTest.testAddReservation();
                case "4" -> UpdateReservationTest.testUpdateReservation();
                case "5" -> CancelReservationTest.testCancelReservation();
                case "6" -> CompleteReservationTest.testCompleteReservation();
                case "7" -> {
                    System.out.println("Exiting system. Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice. Please select a valid option.");
            }
        }
    }
}
