// Imports all classes from the TestPackage inside the com.swen_646_project_1 package.
package com.swen_646_project_1.TestPackage;

/*
 * Imports necessary classes for managing reservations and conducting unit tests.
 * - `Address`: Represents the physical address associated with reservations.
 * **Testing Imports:**
 * - `BeforeEach`: JUnit 5 annotation to run setup methods before each test.
 * - `Test`: JUnit 5 annotation to define test cases.
 * - `Assertions.*`: Provides assertion methods for validating expected behavior in tests.
 */
import com.swen_646_project_1.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test class for the Address entity.
 * Ensures address details can be updated and validated correctly.
 */
public class AddressTest {
    // Declares address object
    private Address address;

    /**
     * Sets up an initial address before each test.
     */
    @BeforeEach
    public void setUp() {
        address = new Address("123 Main St", "New York", "NY", 10001);
    } // End setUp method

    /**
     * Tests setting a new address and verifying the update.
     */
    @Test
    public void testSetAddress() {
        // Print current address before update
        System.out.println("Current Address before update: " + address);

        // Update address with new values
        address.setAddress("456 Elm St", "Los Angeles", "CA", 90001);
        System.out.println("Updated Address: " + address);

        // Validate the updates using assertions
        System.out.println("Retrieved Address: " + address);
        assertEquals("456 Elm St", address.getStreet());
        assertEquals("Los Angeles", address.getCity());
        assertEquals("CA", address.getState());
        assertEquals(90001, address.getZipCode());
    } // End testSetAddress method
} // End AddressTest class