// Declares the package name for the project, grouping related classes together.
package com.swen_646_project_1;

/*
 * Imports necessary classes for managing reservations, accounts, and user interactions.
 * - `com.swen_646_project_1.TestPackage.*`: Includes unit test classes for:
 *   - Account creation
 *   - Reservation management
 *   - Data integrity validation
 * - `com.swen_646_project_1.exceptions.*`: Handles custom exceptions, including:
 *   - `NullAccount_Exception`: Thrown when an account is not found.
 *   - `NullReservation_Exception`: Thrown when a reservation does not exist.
 *   - `DuplicateObject_Exception`: Prevents duplicate accounts or reservations.
 *   - `IllegalOperation_Exception`: Ensures valid reservation/account operations.
 *   - `IllegalParameter_Exception`: Guards against invalid method parameters.
 *   - `IllegalSave_Exception`: Manages errors when saving account/reservation data.
 * - `Scanner`: Captures user input for interactive menu selection in test cases.
 */
import com.swen_646_project_1.TestPackage.*;
import com.swen_646_project_1.exceptions.*;
import java.util.Scanner;

/**
 * Main class that serves as the entry point for the Reservation Management System.
 * Provides a console-based menu for managing accounts and reservations.
 */
public class Main {

    private static final Manager manager = Manager.getInstance();




    public static void main(String[] args) {

        // Run AccountTests, ReservationTests and AddressTests first
        System.out.println("Initializing system tests...");
        runAccountTests();
        runReservationTests();
        runAddressTests();
        runFindAccountTests();
        runFindReservationTests();
        System.out.println("\n     ***** All system tests completed successfully. *****     ");
        System.out.println("=================================================================================================\n");
        System.out.println("=================================================================================================\n");

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
                case "1" -> CreateAccountTest.testCreateNewAccount();
                case "2" -> ModifyAccountTest.testUpdateExistingAccount();
                case "3" -> AddReservationTest.testAddReservation();
                case "4" -> UpdateReservationTest.testUpdateReservation();
                case "5" -> ReservationStatusTest.testModifyReservationStatus("cancel");
                case "6" -> ReservationStatusTest.testModifyReservationStatus("complete");
                case "7" -> InteractiveFindTest.findAccountInteractive();
                case "8" -> InteractiveFindTest.findReservationInteractive();
                case "9" -> {
                    System.out.println("Exiting system. Goodbye!");
                    return;
                } // End case "9" statement
                default -> System.out.println("Invalid choice. Please select a valid option.");
            } // End switch statements
        } // End while loop
    } // End Main method

    /**
     * Runs unit tests for Account class.
     */
    private static void runAccountTests() {
        System.out.println("\n========================================= Running Account Tests ========================================\n");
        // Creates an instance of the AccountTest class to run unit tests.
        AccountTest accountTest = new AccountTest();
        // Ensures test data is ready
        accountTest.setUp();
        accountTest.testUpdateAddress();
        System.out.println("\n     ***** Account tests completed successfully. *****     ");
    } // End runAccountTests method

    /**
     * Runs unit tests for different types of reservations.
     */
    private static void runReservationTests() {
        System.out.println("\n============================================ Running Reservation Tests ============================================ ");
        // Get a prepared instance of ReservationTest
        ReservationTest reservationTest = getPreparedReservationTest();

        // ========= CABIN TESTS ==========
        System.out.println("========================================= Running Cabin Reservation Tests ========================================\n");
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
        System.out.println("\n***** Cabin tests completed successfully. *****");

        // ========== HOTEL TESTS ==========
        System.out.println("\n ======================================== Running Hotel Reservation Tests ========================================\n");
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
        System.out.println("\n***** Hotel tests completed successfully. *****");

        // ========== HOUSE TESTS ==========
        System.out.println("\n======================================== Running House Reservation Tests ========================================\n");
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
        System.out.println("\n***** House tests completed successfully. *****");

        System.out.println("\n     ***** Reservation tests completed successfully. *****     ");
    } // End runReservationTests method

    /**
     * Runs unit tests for Address class.
     */
    private static void runAddressTests() {
        System.out.println("\n ======================================== Running Address Tests ========================================\n");
        // Creates an instance of the AddressTest class to run unit tests.
        AddressTest addressTest = new AddressTest();
        // Ensures test data is ready
        addressTest.setUp();
        addressTest.testSetAddress();
        System.out.println("\n     ***** Address tests completed successfully. *****     ");
    } // End runAddressTests method

    /**
     * Runs unit tests for finding an account.
     */
    private static void runFindAccountTests() {
        System.out.println("\n======================================== Running Find Account Tests ========================================\n");
        // Creates an instance of the FindAccountTest class to run unit tests.
        FindAccountTest findAccountTest = new FindAccountTest();
        // Ensures test data is ready
        findAccountTest.setUp();
        findAccountTest.testFindAccount();
        System.out.println("\n     ***** Find Account tests completed successfully. *****     ");
    } // End runFindAccountTests method

    /**
     * Runs unit tests for finding a reservation.
     */
    private static void runFindReservationTests() {
        System.out.println("\n======================================== Running Find Reservation Tests ========================================");
        // Creates an instance of the FindReservationTest class to run unit tests.
        FindReservationTest findReservationTest = new FindReservationTest();
        findReservationTest.testFindCabinReservation();
        findReservationTest.testFindHotelReservation();
        findReservationTest.testFindHouseReservation();
        System.out.println("\n     ***** Find Reservation tests completed successfully. *****     ");
    } // End runFindReservationTests method

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
     * Simulated method to fetch an account by its number.
     * In a real system, this would query a database or a data store.
     * @param accountNumber The account number to search for.
     * @return The corresponding Account object if found, otherwise null.
     */
    private static Account getAccountByNumber(String accountNumber) {
        return null;  // Simulating an account not found scenario.
    } // end getAccountByNumber method

} // End Main Class
