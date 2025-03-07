package com.swen_646_project_1;

import com.swen_646_project_1.TestPackage.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Reservation Management System ===");
            System.out.println("1. Create a New Account");
            System.out.println("2. Update Existing Account");
            System.out.println("3. Add a Reservation");
            System.out.println("4. Update a Reservation");
            System.out.println("5. Cancel a Reservation");
            System.out.println("6. Complete a Reservation");
            System.out.println("7. Run Unit Tests");
            System.out.println("8. Exit");

            System.out.print("\nSelect an option (1-8): ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> CreateAccountTest.testCreateNewAccount();
                case "2" -> UpdateAccountTest.testUpdateExistingAccount();
                case "3" -> AddReservationTest.testAddReservation();
                case "4" -> UpdateReservationTest.testUpdateReservation();
                case "5" -> CancelReservationTest.testCancelReservation();
                case "6" -> CompleteReservationTest.testCompleteReservation();
                case "7" -> runUnitTests();
                case "8" -> {
                    System.out.println("Exiting system. Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice. Please select a valid option.");
            }
        }
    }

    /**
     * Runs all unit tests related to Account, Address, and Reservation classes.
     */
    private static void runUnitTests() {
        System.out.println("\n=== Running Unit Tests ===");
        System.out.println("1. Test Address Methods");
        System.out.println("2. Test Account Methods");
        System.out.println("3. Test Reservation Methods");
        System.out.println("4. Run All Tests");
        System.out.println("5. Return to Main Menu");

        Scanner scanner = new Scanner(System.in);
        System.out.print("\nSelect a test to run (1-5): ");
        String testChoice = scanner.nextLine().trim();

        switch (testChoice) {
            case "1" -> runAddressTests();
            case "2" -> runAccountTests();
            case "3" -> runReservationTests();
            case "4" -> {
                runAddressTests();
                runAccountTests();
                runReservationTests();
            }
            case "5" -> System.out.println("Returning to Main Menu.");
            default -> System.out.println("Invalid choice. Returning to Main Menu.");
        }
    }

    /**
     * Runs unit tests for Address class.
     */
    private static void runAddressTests() {
        System.out.println("\nRunning Address Tests...");
        AddressTest addressTest = new AddressTest();
        addressTest.setUp();
        addressTest.testSetAddress();
        System.out.println("Address tests completed successfully.");
    }

    /**
     * Runs unit tests for Account class.
     */
    private static void runAccountTests() {
        System.out.println("\nRunning Account Tests...");
        AccountTest accountTest = new AccountTest();
        accountTest.setUp();
        accountTest.testGetAllReservations();
        accountTest.testUpdateAddress();
        System.out.println("Account tests completed successfully.");
    }

    /**
     * Runs unit tests for Reservation class.
     */
    private static void runReservationTests() {
        System.out.println("\nRunning Reservation Tests...");
        ReservationTest reservationTest = new ReservationTest();
        reservationTest.setUp();
        reservationTest.testGetLodgingMailingAddress();
        reservationTest.testSetLodgingMailingAddress();
        reservationTest.testGetNumBeds();
        reservationTest.testGetNumBedrooms();
        reservationTest.testGetNumBathrooms();
        reservationTest.testGetLodgingSizeSqFt();
        System.out.println("Reservation tests completed successfully.");
    }
}
