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
import com.swen_646_project_1.reservation.Reservation;
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
        System.out.println("🔄 Resetting Manager singleton using Reflection...");

        try {
            Field instanceField = Manager.class.getDeclaredField("instance");
            instanceField.setAccessible(true);
            instanceField.set(null, null);
            manager = Manager.getInstance(); // ✅ Ensure manager is initialized
        } catch (Exception e) {
            fail("❌ Failed to reset Manager singleton: " + e.getMessage());
        }

        System.out.println("✅ Manager singleton reset successfully.");

        // Define test address for reservations
        Address physicalAddress = new Address("43-179 Day Mountain Road", "Temple", "ME", 4984);
        Address mailingAddress = new Address("PO Box 43179", "Waterville", "ME", 4901);

        // Retrieve or create the test account in Manager
        testAccount = manager.getAccount("A900000000");


        if (testAccount == null) {
            fail("❌ Test account not found in Manager after reset.");
        }

        System.out.println("✅ Test account found in Manager: " + testAccount.getAccountNumber());


        // Print all reservations currently loaded from Manager
        System.out.println("📋 Reservations currently loaded for test account:");
        List<Reservation> reservationsBefore = testAccount.getAllReservations();
        if (reservationsBefore.isEmpty()) {
            System.out.println("⚠️ No reservations found in testAccount!");
        } else {
            for (Reservation res : reservationsBefore) {
                System.out.println("  - " + res.getReservationNumber());
            }
        }






        // Initialize test reservations
        testCabinReservation = new CabinReservation(
                "res-CAB90000000", "A900000000", physicalAddress, mailingAddress,
                LocalDate.of(2025, 7, 10), 7, 3, 2, 2, 800, 200.0, true, true
        );

        testHotelReservation = new HotelReservation(
                "res-HOT90000000", "A900000000", physicalAddress, mailingAddress,
                LocalDate.of(2025, 6, 15), 5, 2, 1, 1, 500, 150.0, true
        );

        testHouseReservation = new HouseReservation(
                "res-HOU90000000", "A900000000", physicalAddress, mailingAddress,
                LocalDate.of(2025, 8, 1), 10, 4, 3, 3, 1200, 300.0, 2
        );







        // Add the reservation
        testAccount.addReservation(testCabinReservation);
        System.out.println("✅ Cabin reservation added to testAccount.");

        testAccount.addReservation(testHotelReservation);
        System.out.println("✅ Hotel reservation added to testAccount.");

        testAccount.addReservation(testHouseReservation);
        System.out.println("✅ House reservation added to testAccount.");


        //System.out.println("\n📋 Reservation Keys Stored in Account:");
        //for (String key : storedAccount.getReservationNumbers()) {
            //System.out.println("  - Key: [" + key + "]");
       // }






        // Print the reservations after adding to ensure it's actually added
        System.out.println("\n📋 Reservations after adding to testAccount:");
        List<Reservation> reservationsAfter = testAccount.getAllReservations();
        if (reservationsAfter.isEmpty()) {
            System.out.println("⚠️ No reservations found in testAccount after addition!");
        } else {
            for (Reservation res : reservationsAfter) {
                System.out.println("  - " + res.getReservationNumber());
            }
        }




        // Retrieve the stored account from Manager before adding reservations
        Account storedAccount = manager.getAccount(testAccount.getAccountNumber());
        if (storedAccount == null) {
            fail("❌ Test account not found in Manager after addition!");
        }



        System.out.println("testAccount.getAccountNumber(): " + testAccount.getAccountNumber());
        System.out.println("manager.getAccount(testAccount.getAccountNumber()): " + manager.getAccount(testAccount.getAccountNumber()));





        System.out.println("\n📋 Reservations stored in Manager's version of testAccount:");
        List<Reservation> managerReservations = storedAccount.getAllReservations();
        if (managerReservations.isEmpty()) {
            System.out.println("⚠️ No reservations found in Manager after addition!");
        } else {
            for (Reservation res : managerReservations) {
                System.out.println("  - " + res.getReservationNumber());
            }
        }


        System.out.println("  testAccount HashCode: " + System.identityHashCode(testAccount));
        System.out.println("storedAccount HashCode: " + System.identityHashCode(storedAccount));





        System.out.println("testAccount.getAccountNumber(): " + testAccount.getAccountNumber());


        System.out.println("manager.getAccount(testAccount.getAccountNumber(): "
                + manager.getAccount(testAccount.getAccountNumber()));


        System.out.println("testCabinReservation.getReservationNumber(): "
                + testCabinReservation.getReservationNumber());

        System.out.println("storedAccount.getReservation(testCabinReservation.getReservationNumber(): "
                + storedAccount.getReservation(testCabinReservation.getReservationNumber()));



        System.out.println("📋 Reservation Keys Stored in Account:");
        for (String key : storedAccount.getReservationNumbers()) {
            System.out.println("  - Key: " + key);
        }




        System.out.println("\n📋 Reservations stored in Manager's version of testAccount:");
        for (String key : storedAccount.getReservationNumbers()) {
            System.out.println("  - Key: [" + key + "]");
        }

        String lookupKey = testCabinReservation.getReservationNumber().trim().toUpperCase();
        System.out.println("🔎 Looking for reservation with key: [" + lookupKey + "]");

        Reservation debugReservation = storedAccount.getReservation(lookupKey);
        System.out.println("🔍 Direct getReservation() call result: " + debugReservation);





        // Validate that the reservation exists in Manager
        assertNotNull(
                storedAccount.getReservation("res-CAB90000000"),
                "❌ Cabin reservation not found in Manager after setup."
        );
/**
        // Ensure reservations exist in Manager **before tests run**
        assertNotNull(storedAccount.getReservation(testCabinReservation.getReservationNumber()),
                "❌ Cabin reservation not found in Manager after setup.");
        assertNotNull(storedAccount.getReservation(testHotelReservation.getReservationNumber()),
                "❌ Hotel reservation not found in Manager after setup.");
        assertNotNull(storedAccount.getReservation(testHouseReservation.getReservationNumber()),
                "❌ House reservation not found in Manager after setup.");
*/
        System.out.println("\n***** Test account and reservations setup complete. *****");
    }

    @Test
    public void testFindCabinReservation() {
        System.out.println("\n=============================== Running testFindCabinReservation ===============================");

        // ✅ **Ensure `manager` is initialized before proceeding**
        if (manager == null) {
            System.out.println("⚠️ `manager` was null, re-initializing...");
            setUp();
        }

        // ✅ **Ensure testAccount is retrieved again**
        testAccount = manager.getAccount("A900000000");
        assertNotNull(testAccount, "❌ testAccount is null after setup.");
        assertNotNull(manager.getAccount(testAccount.getAccountNumber()), "❌ Test account should exist in manager");

        // ✅ **Ensure reservation exists before finding**
        assertNotNull(testAccount.getReservation(testCabinReservation.getReservationNumber()),
                "❌ Cabin reservation should exist in memory before finding.");

        try {
            manager.findReservation(testAccount.getAccountNumber(), testCabinReservation.getReservationNumber());

            assertEquals("res-CAB90000000", testCabinReservation.getReservationNumber());
            assertEquals("A900000000", testCabinReservation.getAccountNumber());

            System.out.println("✅ Cabin reservation found successfully.");
        } catch (NullReservation_Exception e) {
            fail("❌ Cabin reservation not found: " + e.getMessage());
        }
    }

    @Test
    public void testFindHotelReservation() {
        System.out.println("\n=============================== Running testFindHotelReservation ===============================");

        if (manager == null) {
            System.out.println("⚠️ `manager` was null, re-initializing...");
            setUp();
        }

        testAccount = manager.getAccount("A900000000");
        assertNotNull(testAccount, "❌ testAccount is null after setup.");

        try {
            manager.findReservation(testAccount.getAccountNumber(), testHotelReservation.getReservationNumber());

            assertEquals("res-HOT90000000", testHotelReservation.getReservationNumber());
            assertEquals("A900000000", testHotelReservation.getAccountNumber());

            System.out.println("✅ Hotel reservation found successfully.");
        } catch (NullReservation_Exception e) {
            fail("❌ Hotel reservation not found: " + e.getMessage());
        }
    }

    @Test
    public void testFindHouseReservation() {
        System.out.println("\n=============================== Running testFindHouseReservation ===============================");

        if (manager == null) {
            System.out.println("⚠️ `manager` was null, re-initializing...");
            setUp();
        }

        testAccount = manager.getAccount("A900000000");
        assertNotNull(testAccount, "❌ testAccount is null after setup.");

        try {
            manager.findReservation(testAccount.getAccountNumber(), testHouseReservation.getReservationNumber());

            assertEquals("res-HOU90000000", testHouseReservation.getReservationNumber());
            assertEquals("A900000000", testHouseReservation.getAccountNumber());

            System.out.println("✅ House reservation found successfully.");
        } catch (NullReservation_Exception e) {
            fail("❌ House reservation not found: " + e.getMessage());
        }
    }

    @Test
    public void testFindNonExistentReservation() {
        if (manager == null) {
            System.out.println("⚠️ `manager` was null, re-initializing...");
            setUp();
        }

        testAccount = manager.getAccount("A900000000");
        assertNotNull(testAccount, "❌ testAccount is null after setup.");

        Exception exception = assertThrows(NullReservation_Exception.class, () ->
                manager.findReservation(testAccount.getAccountNumber(), "res-NONEXISTENT")
        );
        assertTrue(exception.getMessage().contains("Reservation not found"));
    }
} // End FindReservationTest class
