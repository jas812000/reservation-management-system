// Imports all classes from the TestPackage inside the com.swen_646_project_1 package.
package com.swen_646_project_1.TestPackage;

/*
 * Imports necessary classes for testing the Reservation Management System.
 * - `Account`, `Address`: Core entities representing user accounts and addresses.
 * - `exceptions.*`: Handles various error scenarios such as invalid parameters and duplicate objects.
 * - `reservation.*`: Includes different types of reservations (Hotel, House, Cabin, etc.).
 * - `BeforeEach`, `Test`: JUnit 5 annotations for setting up and running test cases.
 * - `LocalDate`: Used to handle reservation dates.
 * - `List`: Utilized for managing multiple accounts and reservations.
 * - `Assertions.*`: Provides assertion methods for unit testing.
 */
import com.swen_646_project_1.Account;
import com.swen_646_project_1.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test class for the Account entity.
 * Tests account-related functionalities such as updating address and retrieving reservations.
 */
public class AccountTest {
    // Declares variables for account and reservation
    private Account account;

    /**
     * Initializes test data before each test method runs.
     */
    @BeforeEach
    public void setUp() {
        Address address = new Address("123 Main St", "New York", "NY", 10001);
        account = new Account("A100000000", address,
                "123-456-7890", "test@example.com");
    } // End setUp method

    /**
     * Tests updating an account's address and verifying the update.
     */
    @Test
    public void testUpdateAddress() {
        // Print original address
        System.out.println("Original Address: " + formatAddress(account.getAddress()));

        // First update using Address object
        Address newAddress = new Address("456 Elm St", "Los Angeles", "CA", 90001);
        System.out.println("Updating address to: " + newAddress);
        account.updateAddress(newAddress);

        // Print updated address
        System.out.println("Updated Address: " + formatAddress(account.getAddress()));
        assertEquals("456 Elm St", account.getAddress().getStreet());
        assertEquals("Los Angeles", account.getAddress().getCity());
        assertEquals("CA", account.getAddress().getState());
        assertEquals(90001, account.getAddress().getZipCode());

        // Second update using individual fields
        Address secondUpdate = new Address("789 Pine St", "San Francisco", "CA", 94102);
        System.out.println("\nUpdating address again to: " + formatAddress(secondUpdate));
        account.updateAddress(secondUpdate.getStreet(), secondUpdate.getCity(),
                secondUpdate.getState(), secondUpdate.getZipCode());

        // Print second updated address
        System.out.println("Final Updated Address: " + formatAddress(account.getAddress()));
        assertEquals("789 Pine St", account.getAddress().getStreet());
        assertEquals("San Francisco", account.getAddress().getCity());
        assertEquals("CA", account.getAddress().getState());
        assertEquals(94102, account.getAddress().getZipCode());
    } // End testUpdateAddress method

    /**
     * Formats the address into a readable string.
     * @param address Address object to format
     * @return Formatted string representation of the address
     */
    private String formatAddress(Address address) {
        return address.getStreet() + ", " + address.getCity() + ", " +
                address.getState() + ", " + address.getZipCode();
    } // End formatAddress method

} // End AccountTest class
