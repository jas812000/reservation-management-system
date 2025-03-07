package com.swen_646_project_1.TestPackage;

import com.swen_646_project_1.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AddressTest {
    private Address address;

    @BeforeEach
    public void setUp() {
        address = new Address("123 Main St", "New York", "NY", 10001);
    }

    @Test
    public void testSetAddress() {
        address.setAddress("456 Elm St", "Los Angeles", "CA", 90001);

        assertEquals("456 Elm St", address.getStreet());
        assertEquals("Los Angeles", address.getCity());
        assertEquals("CA", address.getState());
        assertEquals(90001, address.getZipCode());
    }
}
