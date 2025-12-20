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
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;
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

    @TempDir
    Path tempDir;

    private static final String TEST_ACCOUNT_NUMBER = "A900000000";

    @BeforeEach
    public void setUp() {
    	// Point persistence at an isolated temp folder
    	System.setProperty("RMS_DATA_DIR", tempDir.toString());

    	manager = new Manager();
    	manager.clearAccounts();

    	// Create a test account
    	Address physicalAddress = new Address("43-179 Day Mountain Road", "Temple", "ME", 4984);
    	Address mailingAddress  = new Address("PO Box 43179", "Waterville", "ME", 4901);

    	testAccount = new Account(TEST_ACCOUNT_NUMBER, mailingAddress, "123-456-7890", "test@email.com");

    	// Add account to manager (this should persist it in tempDir via your file layer)
    	manager.addAccount(testAccount);

    	// Create reservations and attach them to the account
	testCabinReservation = new CabinReservation(
        	"res-CAB900000000",
        	TEST_ACCOUNT_NUMBER,
        	physicalAddress,
        	mailingAddress,
        	LocalDate.of(2025, 12, 12),
        	3,   // numNights
        	2,   // numBeds
        	1,   // numBedrooms
        	1,   // numBathrooms
        	900, // lodgingSize (or whatever your class expects)
        	0.0, // lodgingPrice (can be recalculated later)
        	true,
        	false
	);

	testHotelReservation = new HotelReservation(
        	"res-HOT900000000",
        	TEST_ACCOUNT_NUMBER,
        	physicalAddress,
        	mailingAddress,
        	LocalDate.of(2025, 12, 12),
        	2,    // numNights
        	1,    // numBeds
        	1,    // numBedrooms
        	1,    // numBathrooms
        	300,  // lodgingSizeSqFt (or similar)
        	0.0,  // lodgingPrice
        	true  // kitchenette
	);

	testHouseReservation = new HouseReservation(
        	"res-HOU900000000",
        	TEST_ACCOUNT_NUMBER,
        	physicalAddress,
        	mailingAddress,
        	LocalDate.of(2025, 12, 12),
        	4,     // numNights
        	3,     // numBeds
        	2,     // numBedrooms
        	2,     // numBathrooms
        	2000,  // lodgingSizeSqFt (or similar)
        	0.0,   // lodgingPrice
        	2      // numFloors
	);

    	testAccount.addReservation(testCabinReservation);
    	testAccount.addReservation(testHotelReservation);
    	testAccount.addReservation(testHouseReservation);

    	// Reload to simulate a real load path (optional but good)
    	//manager.reloadAccounts();

    	// Sanity checks
    	assertNotNull(manager.getAccount(TEST_ACCOUNT_NUMBER), "Test account should exist in manager."); 

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
	try {
    	    manager.findReservation(
            	testAccount.getAccountNumber(),
            	testCabinReservation.getReservationNumber()
    	    );

    	    // findReservation() is void, so fetch it from the account afterward
    	    Reservation found = testAccount.getReservation(testCabinReservation.getReservationNumber());
    	    if (found == null) {
        	found = testAccount.getReservation(testCabinReservation.getReservationNumber().toUpperCase());
    	    }

    	    assertNotNull(found, "Reservation should exist after findReservation");
    	    assertEquals("res-CAB900000000", found.getReservationNumber());
    	    assertEquals("A900000000", found.getAccountNumber());

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

            assertEquals("res-HOT900000000", testHotelReservation.getReservationNumber());
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

            assertEquals("res-HOU900000000", testHouseReservation.getReservationNumber());
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
