package com.jamesstevens.rms.tests;

import com.jamesstevens.rms.Account;
import com.jamesstevens.rms.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Account} account profile updates.
 */
public class AccountTest {

    private Account account;

    /**
     * Creates a baseline account before each test.
     */
    @BeforeEach
    public void setUp() {
        Address address = new Address("123 Main St", "New York", "NY", 10001);
        account = new Account("A900000000", address, "123-456-7890", "test@example.com");
    }

    /**
     * Verifies that updating an account address works using both overloads:
     * updating via {@link Address} and updating via individual address fields.
     */
    @Test
    public void testUpdateAddress() {
        Address newAddress = new Address("456 Elm St", "Los Angeles", "CA", 90001);
        account.updateAddress(newAddress);

        assertEquals("456 Elm St", account.getAddress().getStreet());
        assertEquals("Los Angeles", account.getAddress().getCity());
        assertEquals("CA", account.getAddress().getState());
        assertEquals(90001, account.getAddress().getZipCode());

        Address secondUpdate = new Address("789 Pine St", "San Francisco", "CA", 94102);
        account.updateAddress(
                secondUpdate.getStreet(),
                secondUpdate.getCity(),
                secondUpdate.getState(),
                secondUpdate.getZipCode()
        );

        assertEquals("789 Pine St", account.getAddress().getStreet());
        assertEquals("San Francisco", account.getAddress().getCity());
        assertEquals("CA", account.getAddress().getState());
        assertEquals(94102, account.getAddress().getZipCode());
    }
}
