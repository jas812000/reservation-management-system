// Imports all classes from the TestPackage inside the com.swen_646_project_1 package.
package com.jamesstevens.rms.tests;

/*
 * Import necessary classes for unit testing:
 * - Core application classes (Account, Manager, Address) for managing reservations and accounts.
 * - Custom exceptions (`NullReservation_Exception`) for handling error scenarios in reservations.
 * - Specific reservation types (`CabinReservation`, `HotelReservation`, `HouseReservation`) to test their behavior.
 * - JUnit 5 framework for testing (`BeforeEach`, `Test`) to set up and execute test cases.
 * - Java time utilities (`LocalDate`) for handling reservation dates.
 * - Reflection (`Field`) to modify private fields during testing if needed.
 * - JUnit assertions (`assertEquals`, `assertThrows`, `assertTrue`, `assertNotNull`, `fail`) for validating expected outcomes.
 * - Java Collections (`List`) for handling groups of reservations in tests.
 */
import com.jamesstevens.rms.*;
import com.jamesstevens.rms.exceptions.NullReservation_Exception;
import com.jamesstevens.rms.reservation.CabinReservation;
import com.jamesstevens.rms.reservation.HotelReservation;
import com.jamesstevens.rms.reservation.HouseReservation;
import com.jamesstevens.rms.reservation.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the `findReservation` method in the Manager class.
 * Ensures that Cabin, Hotel, and House reservations can be located correctly.
 */
public class FindReservationTest {
    private Manager manager;
    private Account testAccount;
    private CabinReservation testCabinReservation;
    private HotelReservation testHotelReservation;
    private HouseReservation testHouseReservation;

    @BeforeEach
    public void setUp() {

        try {
            // Access the private static `instance` field of the Manager singleton class using reflection
            Field instanceField = Manager.class.getDeclaredField("instance");
            // Allow modification of the private field
            instanceField.setAccessible(true);
            // Set the singleton instance to null, effectively resetting the Manager instance
            instanceField.set(null, null);
            // Ensure manager is initialized
            manager = Manager.getInstance();
        } catch (Exception e) {
            fail("Failed to reset Manager singleton: " + e.getMessage());
        } // End try-catch statements

        // Define test address for reservations
        Address physicalAddress = new Address("43-179 Day Mountain Road",
                "Temple", "ME", 4984);
        Address mailingAddress = new Address("PO Box 43179",
                "Waterville", "ME", 4901);

        // Retrieve or create the test account in Manager
        testAccount = manager.getAccount("A900000000");

        // Verify that the test account exists after resetting the Manager instance
        if (testAccount == null) {
            fail("Test account not found in Manager after reset.");
        } // End if statement

        // Print all reservations currently loaded from Manager
        List<Reservation> reservationsBefore = testAccount.getAllReservations();
        if (reservationsBefore.isEmpty()) {
            System.out.println("No reservations found in testAccount!");
        } else {
            for (Reservation res : reservationsBefore) {
                System.out.println("  - " + res.getReservationNumber());
            } // End for loop
        }  // End if-else statements

        // Initialize test reservations
        testCabinReservation = new CabinReservation(
                "res-CAB90000000", "A900000000", physicalAddress, mailingAddress,
                LocalDate.of(2025, 7, 10), 7, 3, 2,
                2, 800, 200.0, true, true
        );

        testHotelReservation = new HotelReservation(
                "res-HOT90000000", "A900000000", physicalAddress, mailingAddress,
                LocalDate.of(2025, 6, 15), 5, 2,
                1, 1, 500, 150.0, true
        );

        testHouseReservation = new HouseReservation(
                "res-HOU90000000", "A900000000", physicalAddress, mailingAddress,
                LocalDate.of(2025, 8, 1), 10, 4,
                3, 3, 1200, 300.0, 2
        );

        // Add the reservation
        testAccount.addReservation(testCabinReservation);
        System.out.println("Cabin reservation added to testAccount.");

        testAccount.addReservation(testHotelReservation);
        System.out.println("Hotel reservation added to testAccount.");

        testAccount.addReservation(testHouseReservation);
        System.out.println("House reservation added to testAccount.");

        // Print the reservations after adding to ensure it's actually added
        System.out.println("\nReservations after adding to testAccount:");
        List<Reservation> reservationsAfter = testAccount.getAllReservations();
        if (reservationsAfter.isEmpty()) {
            System.out.println("No reservations found in testAccount after addition!");
        } else {
            for (Reservation res : reservationsAfter) {
                System.out.println("  - " + res.getReservationNumber());
            }  // End for loop
        }  // End if-else statements

        // Retrieve the stored account from Manager before adding reservations
        Account storedAccount = manager.getAccount(testAccount.getAccountNumber());
        if (storedAccount == null) {
            fail("Test account not found in Manager after addition!");
        }  // End if statement

        List<Reservation> managerReservations = storedAccount.getAllReservations();
        if (managerReservations.isEmpty()) {
            System.out.println("No reservations found in Manager after addition!");
        } else {
            for (Reservation res : managerReservations) {
                System.out.println("  - " + res.getReservationNumber());
            } // End for loop
        }  // End if-else statements

        // Validate that the reservation exists in Manager
        assertNotNull(
                storedAccount.getReservation("res-CAB90000000"),
                "Cabin reservation not found in Manager after setup."
        );

        System.out.println("\n***** Test account and reservations setup complete. *****");
    } // End setup method

    @Test
    public void testFindCabinReservation() {
        System.out.println("\n=============================== Running testFindCabinReservation =====" +
                "==========================");

        // Ensure `manager` is initialized before proceeding
        if (manager == null) {
            setUp();
        } // End if statement

        // Ensure testAccount is retrieved again
        testAccount = manager.getAccount("A900000000");
        assertNotNull(testAccount, "testAccount is null after setup.");
        assertNotNull(manager.getAccount(testAccount.getAccountNumber()),
                "Test account should exist in manager");

        // Ensure reservation exists before finding
        assertNotNull(testAccount.getReservation(testCabinReservation.getReservationNumber()),
                "Cabin reservation should exist in memory before finding.");

        try {
            manager.findReservation(testAccount.getAccountNumber(), testCabinReservation.getReservationNumber());

            assertEquals("res-CAB90000000", testCabinReservation.getReservationNumber());
            assertEquals("A900000000", testCabinReservation.getAccountNumber());

            System.out.println("Cabin reservation found successfully.");
        } catch (NullReservation_Exception e) {
            fail("Cabin reservation not found: " + e.getMessage());
        } // End try-catch statements
    } // End testFindCabinReservation method

    @Test
    public void testFindHotelReservation() {
        System.out.println("\n=============================== Running testFindHotelReservation =========" +
                "======================");

        if (manager == null) {
            setUp();
        } // End if statement

        testAccount = manager.getAccount("A900000000");
        assertNotNull(testAccount, "testAccount is null after setup.");

        try {
            manager.findReservation(testAccount.getAccountNumber(), testHotelReservation.getReservationNumber());

            assertEquals("res-HOT90000000", testHotelReservation.getReservationNumber());
            assertEquals("A900000000", testHotelReservation.getAccountNumber());

            System.out.println("Hotel reservation found successfully.");
        } catch (NullReservation_Exception e) {
            fail("Hotel reservation not found: " + e.getMessage());
        } // End try-catch statements
    } // End testFindHotelReservation method

    @Test
    public void testFindHouseReservation() {
        System.out.println("\n=============================== Running testFindHouseReservation =========" +
                "======================");

        if (manager == null) {
            setUp();
        } // End if statement

        testAccount = manager.getAccount("A900000000");
        assertNotNull(testAccount, "testAccount is null after setup.");

        try {
            manager.findReservation(testAccount.getAccountNumber(), testHouseReservation.getReservationNumber());

            assertEquals("res-HOU90000000", testHouseReservation.getReservationNumber());
            assertEquals("A900000000", testHouseReservation.getAccountNumber());

            System.out.println("House reservation found successfully.");
        } catch (NullReservation_Exception e) {
            fail("House reservation not found: " + e.getMessage());
        } // End try-catch statements
    }

    @Test
    public void testFindNonExistentReservation() {
        if (manager == null) {
            setUp();
        }  // End if statement

        testAccount = manager.getAccount("A900000000");
        assertNotNull(testAccount, "testAccount is null after setup.");

        Exception exception = assertThrows(NullReservation_Exception.class, () ->
                manager.findReservation(testAccount.getAccountNumber(), "res-NONEXISTENT")
        );
        assertTrue(exception.getMessage().contains("Reservation not found"));
    } // End testFindNonExistentReservation method
} // End FindReservationTest class
