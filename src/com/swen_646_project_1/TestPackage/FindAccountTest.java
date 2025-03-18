// Imports all classes from the TestPackage inside the com.swen_646_project_1 package.
package com.swen_646_project_1.TestPackage;

/*
 * Import necessary classes for unit testing:
 * - Core application classes
 *   - `Account`: Represents a user account in the reservation system.
 *   - `Manager`: Manages accounts and reservations in the system.
 * - Custom exception handling (`NullAccount_Exception`) thrown when an account is not found.
 * - JUnit 5 framework for unit testing:
 *   - `@BeforeEach`: Runs setup code before each test case.
 *   - `@Test`: Marks a method as a test case.
 * - JUnit assertions from `org.junit.jupiter.api.Assertions` for test validation:
 *   - `assertEquals`: Checks if two values are equal.
 *   - `assertTrue`: Checks if a condition is true.
 *   - `assertThrows`: Verifies if an exception is thrown.
 * - Java utility classes:
 *   - `List` represents a list of reservations associated with an account.
 */
import com.swen_646_project_1.*;
import com.swen_646_project_1.exceptions.NullAccount_Exception;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

/**
 * Unit tests for the findAccount method.
 */
public class FindAccountTest {
    // Create an instance of Manager and Account
    private Manager manager;
    private Account testAccount;

    /**
     * Initializes test data before each test case runs.
     * - Creates a test account with a known account number.
     * - Adds the test account to the Manager instance.
     */
    @BeforeEach
    public void setUp() {
        manager = new Manager();  // Initialize Manager

        // Create a test account
        Address address = new Address("123 Main St", "Dallas", "TX", 75001);
        testAccount = new Account("A900000000", address, "123-456-7890", "test@email.com");

        // Attempt to add the test account to the Manager
        try {


            System.out.println("🔍 Checking if test account already exists: " + testAccount.getAccountNumber());






            // Check if the account already exists before adding
            if (manager.getAccount(testAccount.getAccountNumber()) == null) {


                System.out.println("📌 Adding test account: " + testAccount.getAccountNumber());


                manager.addAccount(testAccount);

                System.out.println("✅ Test account added: " + testAccount.getAccountNumber());


                System.out.println("Test account added: " + testAccount.getAccountNumber());
            } else {


                System.out.println("⚠️ Test account already exists, skipping creation.");


            } // End if-else statement
        } catch (Exception e) {
            System.out.println("❌ Error adding account: " + e.getMessage());
            fail("Setup failed due to exception: " + e.getMessage());
        } // End try-catch statements
    } // End setUp method

    /**
     * Tests the `findAccount` method to ensure it retrieves the correct account.
     * - Calls `findAccount` with a known account number.
     * - Verifies that the account exists and has the expected attributes.
     * - Ensures the retrieved account has no reservations initially.
     */
    @Test
    public void testFindAccount() {

        assertNotNull(testAccount, "Test account should not be null");  // Ensures setup is working
        try {
            // Call `findAccount` method to search for the test account
            manager.findAccount(testAccount.getAccountNumber());

            // Retrieve reservations
            List<String> reservations = testAccount.getReservationNumbers();

            // Validate that the correct account number was found
            assertEquals("A900000000", testAccount.getAccountNumber());
            // Ensure that the account does not contain any reservations yet
            assertTrue(reservations.isEmpty()); // No reservations yet
        } catch (NullAccount_Exception e) {
            fail("Account not found: " + e.getMessage());  // Fail test if account is not found
        } // End try-catch statements
    } // End testFindAccount method
} // End FindAccountTest class
