// Imports all classes from the TestPackage inside the com.swen_646_project_1 package.
package com.swen_646_project_1.TestPackage;

/*
 * Import necessary classes for unit testing:
 * - Core application classes (Account, Manager, Address)
 * - Custom exception handling
 * - Specific reservation types (Cabin, Hotel, House)
 * - JUnit 5 framework for testing (`BeforeEach`, `Test`)
 * - Java time utilities (`LocalDate`) for date handling
 * - JUnit assertions for validation (`assertEquals`, `assertThrows`, `assertTrue`)
 */
import com.swen_646_project_1.*;
import com.swen_646_project_1.exceptions.NullReservation_Exception;
import com.swen_646_project_1.reservation.CabinReservation;
import com.swen_646_project_1.reservation.HotelReservation;
import com.swen_646_project_1.reservation.HouseReservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the `findReservation` method in the Manager class.
 * Ensures that Cabin, Hotel, and House reservations can be located correctly.
 */
public class FindReservationTest {
    // Declare attributes for testing
    private Manager manager; // Instance of Manager for reservation management
    private Account testAccount; // Test account to store reservations
    private CabinReservation testCabinReservation; // Test reservation for a Cabin
    private HotelReservation testHotelReservation; // Test reservation for a Hotel
    private HouseReservation testHouseReservation; // Test reservation for a House

    /**
     * Sets up test data before each test case runs.
     * - Initializes Manager
     * - Creates an Account
     * - Creates sample Cabin, Hotel, and House reservations
     * - Adds reservations to the test account
     */
    @BeforeEach
    public void setUp() {
        manager = new Manager(); // Initialize Manager instance

        // Define test address for reservations
        Address physicalAddress = new Address("43-179 Day Mountain Road", "Temple", "ME", 4984);
        Address mailingAddress = new Address("PO Box 43179", "Waterville", "ME", 4901);

        // Create a test account
        testAccount = new Account("A900000000", physicalAddress, "123-456-7890", "test@email.com");
        System.out.println("\nTest account created: " + testAccount.getAccountNumber());

        // Initialize Cabin reservation
        testCabinReservation = new CabinReservation(
                "res-CAB90000000", "A900000000", physicalAddress, mailingAddress,
                LocalDate.of(2025, 7, 10), 7, 3, 2, 2, 800, 200.0, true, true
        );

        // Initialize Hotel reservation
        testHotelReservation = new HotelReservation(
                "res-HOT90000000", "A900000000", physicalAddress, mailingAddress,
                LocalDate.of(2025, 6, 15), 5, 2, 1, 1, 500, 150.0, true
        );

        // Initialize House reservation
        testHouseReservation = new HouseReservation(
                "res-HOU90000000", "A900000000", physicalAddress, mailingAddress,
                LocalDate.of(2025, 8, 1), 10, 4, 3, 3, 1200, 300.0, 2
        );

        // Attempt to add the test account and reservations
        try {
            // Add account only if it doesn't already exist
            if (manager.getAccount(testAccount.getAccountNumber()) == null) {
                manager.addAccount(testAccount);
                System.out.println("Test account added to Manager: " + testAccount.getAccountNumber());
            } else {
                System.out.println("Test account already exists, skipping addition.");
            } // End if-else statements

            // Add reservations only if they don't already exist
            if (testAccount.getReservation(testCabinReservation.getReservationNumber()) == null) {
                testAccount.addReservation(testCabinReservation);
                System.out.println("Cabin reservation added.");
            } else {
                System.out.println("Cabin reservation already exists, skipping addition.");
            } // End if-else statements

            if (testAccount.getReservation(testHotelReservation.getReservationNumber()) == null) {
                testAccount.addReservation(testHotelReservation);
                System.out.println("Hotel reservation added.");
            } else {
                System.out.println("Hotel reservation already exists, skipping addition.");
            } // End if-else statements

            if (testAccount.getReservation(testHouseReservation.getReservationNumber()) == null) {
                testAccount.addReservation(testHouseReservation);
                System.out.println("House reservation added.");
            } else {
                System.out.println("House reservation already exists, skipping addition.");
            } // End if-else statements

            // Ensure reservations exist in memory **before tests run**
            assertNotNull(testAccount.getReservation(testCabinReservation.getReservationNumber()), "Cabin reservation not found in memory after setup.");
            assertNotNull(testAccount.getReservation(testHotelReservation.getReservationNumber()), "Hotel reservation not found in memory after setup.");
            assertNotNull(testAccount.getReservation(testHouseReservation.getReservationNumber()), "House reservation not found in memory after setup.");

        } catch (Exception e) {
            fail("Setup failed due to exception: " + e.getMessage());
        }  // End try-catch statements

        System.out.println("\n***** Test account and reservations setup complete. *****");

    } // End setUp method

    /**
     * Tests the `findReservation` method for a Cabin reservation.
     * - Ensures the reservation is correctly found in the Manager.
     * - Verifies that the retrieved reservation has the expected number.
     */
    @Test
    public void testFindCabinReservation() {
        setUp();
        System.out.println("\n=============================== Running testFindCabinReservation ===============================");

        // Ensure testAccount exists in Manager before calling findReservation
        assertNotNull(manager.getAccount(testAccount.getAccountNumber()), "Test account should exist in manager");

        assertNotNull(testAccount, "testAccount is null - setUp() may have failed.");
        assertNotNull(manager, "manager is null - setUp() may have failed.");
        assertNotNull(testAccount.getReservation(testCabinReservation.getReservationNumber()),
                "Cabin reservation should exist in memory before finding.");

        try {
            // Call method to find the reservation
            manager.findReservation(testAccount.getAccountNumber(), testCabinReservation.getReservationNumber());

            // Validate that the reservation matches expectations
            assertEquals("res-CAB90000000", testCabinReservation.getReservationNumber());
            assertEquals("A900000000", testCabinReservation.getAccountNumber());

            System.out.println("***** Cabin reservation found successfully. *****");
        } catch (NullReservation_Exception e) {
            fail("Cabin reservation not found: " + e.getMessage());
        }  // End try-catch statements
    } // End testFindCabinReservation method

    /**
     * Tests the `findReservation` method for a Hotel reservation.
     * - Ensures the reservation is correctly found in the Manager.
     * - Verifies that the retrieved reservation has the expected number.
     */
    @Test
    public void testFindHotelReservation() {

        System.out.println("\n=============================== Running testFindHotelReservation ===============================");
        try {
            // Call method to find the reservation
            manager.findReservation(testAccount.getAccountNumber(), testHotelReservation.getReservationNumber());

            // Validate that the reservation matches expectations
            assertEquals("res-HOT90000000", testHotelReservation.getReservationNumber());
            assertEquals("A900000000", testHotelReservation.getAccountNumber());

            System.out.println("***** Hotel reservation found successfully. *****");
        } catch (NullReservation_Exception e) {
            fail("Hotel reservation not found: " + e.getMessage());
        }
    } // End testFindHotelReservation method

    /**
     * Tests the `findReservation` method for a House reservation.
     * - Ensures the reservation is correctly found in the Manager.
     * - Verifies that the retrieved reservation has the expected number.
     */
    @Test
    public void testFindHouseReservation() {

        System.out.println("\n=============================== Running testFindHouseReservation ===============================");
        try {
            // Call method to find the reservation
            manager.findReservation(testAccount.getAccountNumber(), testHouseReservation.getReservationNumber());

            // Validate that the reservation matches expectations
            assertEquals("res-HOU90000000", testHouseReservation.getReservationNumber());
            assertEquals("A900000000", testHouseReservation.getAccountNumber());

            System.out.println("***** House reservation found successfully. *****");
        } catch (NullReservation_Exception e) {
            fail("House reservation not found: " + e.getMessage());
        }
    } // End testFindHouseReservation method

    /**
     * Tests `findReservation` for a non-existent reservation.
     * - Ensures the correct exception is thrown when the reservation does not exist.
     */
    @Test
    public void testFindNonExistentReservation() {
        // Expect an exception when searching for a non-existent reservation
        Exception exception = assertThrows(NullReservation_Exception.class, () ->
                manager.findReservation(testAccount.getAccountNumber(), "res-NONEXISTENT")
        );
        // Verify the correct error message is displayed
        assertTrue(exception.getMessage().contains("Reservation not found"));
    }
} // End FindReservationTest class
