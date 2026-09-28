package com.jamesstevens.rms.tests;

import com.jamesstevens.rms.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.jamesstevens.rms.exceptions.IllegalParameterException;
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
        address = new Address("123 Main St", "New York", "NY", "10001");
    }

    /**
     * Verifies that {@link Address#setAddress(String, String, String, String)}
     * updates all fields.
     */
    @Test
    public void testSetAddress() {
        address.setAddress("456 Elm St", "Los Angeles", "CA", "90001");

        assertEquals("456 Elm St", address.getStreet());
        assertEquals("Los Angeles", address.getCity());
        assertEquals("CA", address.getState());
        assertEquals("90001", address.getZipCode());
    }

    /**
     * Verifies that address values are trimmed and normalized.
     */
    @Test
    void testAddressNormalizesValues() {
        Address address = new Address(
                "  123 mAIn sTreet  ",
                "  dAllas  ",
                " tx ",
                " 75001 "
        );

        assertEquals("123 Main Street", address.getStreet());
        assertEquals("Dallas", address.getCity());
        assertEquals("TX", address.getState());
        assertEquals("75001", address.getZipCode());
    }

    /**
     * Verifies that normalization preserves and correctly capitalizes
     * words following hyphens and apostrophes.
     */
    @Test
    void testAddressNormalizesHyphensAndApostrophes() {
        Address address = new Address(
                "100 north o'cONNOR rd",
                "wINSTON-sALEM",
                "nc",
                "27101"
        );

        assertEquals("100 North O'Connor Rd", address.getStreet());
        assertEquals("Winston-Salem", address.getCity());
        assertEquals("NC", address.getState());
        assertEquals("27101", address.getZipCode());
    }

    /**
     * Verifies that the standard PO Box abbreviation remains uppercase
     * during address normalization.
     */
    @Test
    void testAddressNormalizesPoBox() {
        Address address = new Address(
                "po box 43179",
                "wATERVILLE",
                "me",
                "04901"
        );

        assertEquals("PO Box 43179", address.getStreet());
        assertEquals("Waterville", address.getCity());
        assertEquals("ME", address.getState());
        assertEquals("04901", address.getZipCode());
    }

    @Test
    void testAddressPreservesLeadingZeroZipCode() {
        Address address = new Address(
                "43-179 Day Mountain Road",
                "Temple",
                "ME",
                "04984"
        );

        assertEquals("04984", address.getZipCode());
    }

    @Test
    void testAddressRejectsBlankStreet() {
        assertThrows(
                IllegalParameterException.class,
                () -> new Address("   ", "Dallas", "TX", "75001")
        );
    }

    @Test
    void testAddressRejectsBlankCity() {
        assertThrows(
                IllegalParameterException.class,
                () -> new Address("123 Main Street", "   ", "TX", "75001")
        );
    }

    @Test
    void testAddressRejectsInvalidStateLength() {
        assertThrows(
                IllegalParameterException.class,
                () -> new Address("123 Main Street", "Dallas", "Texas", "75001")
        );
    }

    @Test
    void testAddressRejectsNonAlphabeticState() {
        assertThrows(
                IllegalParameterException.class,
                () -> new Address("123 Main Street", "Dallas", "T1", "75001")
        );
    }

    @Test
    void testAddressRejectsShortZipCode() {
        assertThrows(
                IllegalParameterException.class,
                () -> new Address("123 Main Street", "Dallas", "TX", "7500")
        );
    }

    @Test
    void testAddressRejectsLongZipCode() {
        assertThrows(
                IllegalParameterException.class,
                () -> new Address("123 Main Street", "Dallas", "TX", "750011")
        );
    }

    @Test
    void testAddressRejectsNonNumericZipCode() {
        assertThrows(
                IllegalParameterException.class,
                () -> new Address("123 Main Street", "Dallas", "TX", "75A01")
        );
    }
}
