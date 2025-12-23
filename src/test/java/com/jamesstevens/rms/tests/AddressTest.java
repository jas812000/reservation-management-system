package com.jamesstevens.rms.tests;

import com.jamesstevens.rms.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Address}.
 */
public class AddressTest {

    private Address address;

    /**
     * Initializes a baseline address before each test.
     */
    @BeforeEach
    public void setUp() {
        address = new Address("123 Main St", "New York", "NY", 10001);
    }

    /**
     * Verifies that {@link Address#setAddress(String, String, String, int)} updates all fields.
     */
    @Test
    public void testSetAddress() {
        address.setAddress("456 Elm St", "Los Angeles", "CA", 90001);

        assertEquals("456 Elm St", address.getStreet());
        assertEquals("Los Angeles", address.getCity());
        assertEquals("CA", address.getState());
        assertEquals(90001, address.getZipCode());
    }
}
