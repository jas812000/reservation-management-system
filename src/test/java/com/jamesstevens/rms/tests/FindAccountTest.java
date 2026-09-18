package com.jamesstevens.rms.tests;

import com.jamesstevens.rms.Account;
import com.jamesstevens.rms.Address;
import com.jamesstevens.rms.Manager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for verifying account retrieval and persistence via {@link Manager}.
 */
public class FindAccountTest {

    private String testAccountNumber;

    private Manager manager;

    @TempDir
    Path tempDir;

    /**
     * Creates an isolated test data directory and persists a single test account.
     */
    @BeforeEach
    public void setUp() {
        System.setProperty("RMS_DATA_DIR", tempDir.toString());

        manager = new Manager();
        testAccountNumber = manager.getNewAccountNumber();

        Address address = new Address("123 Main St", "Dallas", "TX", "75001");
        Account testAccount = new Account(
                testAccountNumber,
                address,
                "123-456-7890",
                "test@email.com"
        );

        manager.addAccount(testAccount);
    }

    /**
     * Verifies that the test account can be reloaded and retrieved and that it has no reservations initially.
     */
    @Test
    public void testFindAccount() {
        manager.reloadAccounts();

        Account foundAccount = manager.getAccount(testAccountNumber);
        assertNotNull(foundAccount, "Failed to find test account in system.");
        assertEquals(testAccountNumber, foundAccount.getAccountNumber());

        List<String> reservations = foundAccount.getReservationNumbers();
        assertTrue(reservations.isEmpty(), "Test account should have no reservations initially.");
    }
}
