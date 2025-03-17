// Declares the package name for the project, grouping related classes together.
package com.swen_646_project_1;

// Imports all classes from the TestPackage inside the com.swen_646_project_1 package.
// This package likely contains unit test classes for validating different functionalities
// such as account creation, reservation management, and data integrity.
import com.swen_646_project_1.TestPackage.*;

// Imports all custom exception classes from the com.swen_646_project_1.exceptions package.
// These exceptions handle various error scenarios such as invalid parameters, duplicate objects,
// illegal operations, missing accounts, and more.
import com.swen_646_project_1.exceptions.*;

// Imports the Scanner class from the java.util package.
// Used to capture user input from the console for interactive menu selection.
import java.util.Scanner;

/**
 * Main class that serves as the entry point for the Reservation Management System.
 * Provides a console-based menu for managing accounts and reservations.
 */
public class Main {
    public static void main(String[] args) {

        // Run AccountTests, ReservationTests and AddressTests first
        System.out.println("Initializing system tests...");
        runAccountTests();
        runReservationTests();
        runAddressTests();
        System.out.println("All system tests completed successfully.");

        // Creates a scanner object
        Scanner scanner = new Scanner(System.in);

        // Loop that continues until the user select a correct option
        while (true) {
            // Display main menu options
            System.out.println("\n=== Reservation Management System ===");
            System.out.println("1. Create a New Account");
            System.out.println("2. Update Existing Account");
            System.out.println("3. Add a Reservation");
            System.out.println("4. Update a Reservation");
            System.out.println("5. Cancel a Reservation");
            System.out.println("6. Complete a Reservation");
            System.out.println("7. Update Customer Information");
            System.out.println("8. Exit");
            System.out.print("\nSelect an option (1-8): ");
            String choice = scanner.nextLine().trim();

            // Handle menu selection
            switch (choice) {
                case "1" -> CreateAccountTest.testCreateNewAccount();
                case "2" -> ModifyAccountTest.testUpdateExistingAccount();
                case "3" -> AddReservationTest.testAddReservation();
                case "4" -> UpdateReservationTest.testUpdateReservation();
                case "5" -> ReservationStatusTest.testModifyReservationStatus("cancel");
                case "6" -> ReservationStatusTest.testModifyReservationStatus("complete");
                case "7" -> updateCustomerInfo();
                case "8" -> {
                    System.out.println("Exiting system. Goodbye!");
                    return;
                } // End case "8" statement
                default -> System.out.println("Invalid choice. Please select a valid option.");
            } // End switch statements
        } // End while loop
    } // End Main method

    /**
     * Runs unit tests for Account class.
     */
    private static void runAccountTests() {
        System.out.println("\nRunning Account Tests...");
        AccountTest accountTest = new AccountTest();
        accountTest.setUp();
        accountTest.testUpdateAddress();
        System.out.println("Account tests completed successfully.");
    } // End runAccountTests method

    /**
     * Runs unit tests for different types of reservations.
     */
    private static void runReservationTests() {
        System.out.println("\nRunning Reservation Tests...");

        // Get a prepared instance of ReservationTest
        ReservationTest reservationTest = getPreparedReservationTest();

        // ====================== CABIN TESTS ======================
        System.out.println("\nRunning Cabin Reservation Tests...");
        reservationTest.testCabinGetReservationNumber();
        reservationTest.testCabinGetAccountNumber();
        reservationTest.testCabinGetPhysicalAddress();
        reservationTest.testCabinGetMailingAddress();
        reservationTest.testCabinGetReservationDate();
        reservationTest.testCabinGetNumNights();
        reservationTest.testCabinGetNumBeds();
        reservationTest.testCabinGetNumBedrooms();
        reservationTest.testCabinGetNumBathrooms();
        reservationTest.testCabinGetLodgingSize();
        reservationTest.testCabinGetLodgingPrice();
        reservationTest.testCabinHasFullKitchen();
        reservationTest.testCabinHasLoft();

        // ====================== HOTEL TESTS ======================
        System.out.println("\nRunning Hotel Reservation Tests...");
        reservationTest.testHotelGetReservationNumber();
        reservationTest.testHotelGetAccountNumber();
        reservationTest.testHotelGetPhysicalAddress();
        reservationTest.testHotelGetReservationDate();
        reservationTest.testHotelGetNumNights();
        reservationTest.testHotelGetNumBeds();
        reservationTest.testHotelGetNumBedrooms();
        reservationTest.testHotelGetNumBathrooms();
        reservationTest.testHotelGetLodgingSizeSqFt();
        reservationTest.testHotelGetLodgingPrice();
        reservationTest.testHotelHasFullKitchenette();

        // ====================== HOUSE TESTS ======================
        System.out.println("\nRunning House Reservation Tests...");
        reservationTest.testHouseGetReservationNumber();
        reservationTest.testHouseGetAccountNumber();
        reservationTest.testHouseGetPhysicalAddress();
        reservationTest.testHouseGetReservationDate();
        reservationTest.testHouseGetNumNights();
        reservationTest.testHouseGetNumBeds();
        reservationTest.testHouseGetNumBedrooms();
        reservationTest.testHouseGetNumBathrooms();
        reservationTest.testHouseGetLodgingSizeSqFt();
        reservationTest.testHouseGetLodgingPrice();
        reservationTest.testHouseGetNumFloors();

        System.out.println("Reservation tests completed successfully.");
    } // End runReservationTests method

    /**
     * Runs unit tests for Address class.
     */
    private static void runAddressTests() {
        System.out.println("\nRunning Address Tests...");
        AddressTest addressTest = new AddressTest();
        addressTest.setUp();
        addressTest.testSetAddress();
        System.out.println("Address tests completed successfully.");
    } // End runAddressTests method

    /**
     * Creates and initializes a `ReservationTest` instance.
     * Ensures that test data is set up before running the tests.
     * @return A properly initialized `ReservationTest` instance.
     */
    private static ReservationTest getPreparedReservationTest() {
        // Creates an instance of the ReservationTest class to run unit tests.
        ReservationTest reservationTest = new ReservationTest();
        // Ensures test data is ready
        reservationTest.setUp();
        return reservationTest;
    } // End getPreparedReservationTest method

    /**
     * Updates customer information such as address, phone number, or email.
     * Prompts the user to select which detail to update.
     */
    private static void updateCustomerInfo() {
        Scanner scanner = new Scanner(System.in);

        // Prompt for account number
        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine().trim().toUpperCase();

        // Fetch the account (Simulating with a placeholder method)
        Account account = getAccountByNumber(accountNumber);
        if (account == null) {
            throw new NullAccount_Exception(accountNumber, "Account not found. Please check the account number.");
        } // End if statement

        // Display update options
        System.out.println("\n=== Update Customer Information ===");
        System.out.println("1. Update Address");
        System.out.println("2. Update Phone Number");
        System.out.println("3. Update Email");
        System.out.println("4. Return to Main Menu");
        System.out.print("Select an option (1-4): ");
        String choice = scanner.nextLine().trim();

        // Handle update selection
        switch (choice) {
            case "1" -> {
                // Prompt for new address details
                System.out.print("Enter new street: ");
                String street = scanner.nextLine().trim();
                System.out.print("Enter new city: ");
                String city = scanner.nextLine().trim();
                System.out.print("Enter new state: ");
                String state = scanner.nextLine().trim().toUpperCase();
                System.out.print("Enter new zip code: ");
                int zipCode = Integer.parseInt(scanner.nextLine().trim());

                // Update the address
                Address newAddress = new Address(street, city, state, zipCode);
                account.updateAddress(newAddress);
                System.out.println("Address updated successfully.");
            }
            case "2" -> {
                // Prompt for new phone number
                System.out.print("Enter new phone number: ");
                String newPhone = scanner.nextLine().trim();
                account.setPhoneNumber(newPhone);
                System.out.println("Phone number updated successfully.");
            }
            case "3" -> {
                // Prompt for new email
                System.out.print("Enter new email: ");
                String newEmail = scanner.nextLine().trim();
                account.setEmail(newEmail);
                System.out.println("Email updated successfully.");
            }
            case "4" -> System.out.println("Returning to Main Menu.");
            default -> System.out.println("Invalid choice. Returning to Main Menu.");
        } // End switch statements
    } // End updateCustomerInfo method

    /**
     * Simulated method to fetch an account by its number.
     * In a real system, this would query a database or a data store.
     * @param accountNumber The account number to search for.
     * @return The corresponding Account object if found, otherwise null.
     */
    private static Account getAccountByNumber(String accountNumber) {
        return null;  // Simulating an account not found scenario.
    } // end getAccountByNumber method

} // End Main Class
