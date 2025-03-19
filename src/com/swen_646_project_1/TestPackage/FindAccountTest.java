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
import java.util.List;
import java.io.File;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


/**
 * Unit tests for the findAccount method.
 */
public class FindAccountTest {
    // Create an instance of Manager and Account
    private Manager manager;
    private Account testAccount;
    private static final String TEST_ACCOUNT_NUMBER = "A900000000";
    private static final String BASE_DIRECTORY = System.getProperty("user.dir") + "/src/com/resources/Accounts";

    /**
     * Initializes test data before each test case runs.
     * - Creates a test account with a known account number.
     * - Adds the test account to the Manager instance.
     */
    @BeforeEach
    public void setUp() {
        manager = new Manager();  // Initialize Manager

        // Define test account directory path
        File testAccountDir = new File(BASE_DIRECTORY, TEST_ACCOUNT_NUMBER);

        // Ensure the directory exists
        if (!testAccountDir.exists() && !testAccountDir.mkdirs()) {
            System.err.println("Error: Failed to create test account directory at " + testAccountDir.getAbsolutePath());
            fail("Setup failed due to directory creation error.");
        } // End if statement

        // Create a test account
        Address address = new Address("123 Main St", "Dallas", "TX", 75001);
        testAccount = new Account(TEST_ACCOUNT_NUMBER, address, "123-456-7890", "test@email.com");

        // Add the test account to the Manager
        try {
            manager.addAccount(testAccount);
            manager.reloadAccounts();
        } catch (Exception e) {
            System.out.println("Error adding account: " + e.getMessage());
            fail("Setup failed due to exception: " + e.getMessage());
        } // End try-catch statements
    } // End setUp method

    /**
     * Tests the `findAccount` method to ensure it retrieves the correct account.
     * - Calls `findAccount` with a known account number.
     * - Verifies that the account exists and has the expected attributes.
     * - Ensures the retrieved account starts fresh without prior reservations.
     */
    @Test
    public void testFindAccount() {
        assertNotNull(testAccount, "Test account should not be null");  // Ensures setup is working

        try {
            manager.reloadAccounts();
            Account foundAccount = manager.getAccount(TEST_ACCOUNT_NUMBER);

            assertNotNull(foundAccount, "Failed to find test account in system.");
            assertEquals(TEST_ACCOUNT_NUMBER, foundAccount.getAccountNumber());

            // Retrieve reservations and verify they are empty after reset
            List<String> reservations = foundAccount.getReservationNumbers();
            assertTrue(reservations.isEmpty(), "Test account should have no reservations initially.");

        } catch (NullAccount_Exception e) {
            fail("Account not found: " + e.getMessage());  // Fail test if account is not found
        } // End try-catch statements
    } // End testFindAccount method
} // End FindAccountTest class
