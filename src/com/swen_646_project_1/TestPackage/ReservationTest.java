package com.swen_646_project_1.TestPackage;

import com.swen_646_project_1.Address;
//import com.swen_646_project_1.enums.ReservationStatus;
import com.swen_646_project_1.reservation.HotelReservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ReservationTest {
    private HotelReservation reservation;

    @BeforeEach
    public void setUp() {
        Address address = new Address("123 Main St", "New York", "NY", 10001);
        reservation = new HotelReservation(
                "RES123",
                "ACC100",
                address,
                address,
                LocalDate.of(2025, 6, 15),
                5,
                2,
                1,
                1,
                500,
                150.0,
                true  // Kitchenette available
        );
    }

    @Test
    public void testGetLodgingMailingAddress() {
        Address mailingAddress = reservation.getLodgingMailingAddress();
        assertEquals("123 Main St", mailingAddress.getStreet());
        assertEquals("New York", mailingAddress.getCity());
        assertEquals("NY", mailingAddress.getState());
        assertEquals(10001, mailingAddress.getZipCode());
    }

    @Test
    public void testSetLodgingMailingAddress() {
        Address newAddress = new Address("456 Elm St", "Los Angeles", "CA", 90001);
        reservation.setLodgingMailingAddress(newAddress);
        assertEquals("456 Elm St", reservation.getLodgingMailingAddress().getStreet());
        assertEquals("Los Angeles", reservation.getLodgingMailingAddress().getCity());
        assertEquals("CA", reservation.getLodgingMailingAddress().getState());
        assertEquals(90001, reservation.getLodgingMailingAddress().getZipCode());
    }

    @Test
    public void testGetNumBeds() {
        assertEquals(2, reservation.getNumBeds());
    }

    @Test
    public void testGetNumBedrooms() {
        assertEquals(1, reservation.getNumBedrooms());
    }

    @Test
    public void testGetNumBathrooms() {
        assertEquals(1, reservation.getNumBathrooms());
    }

    @Test
    public void testGetLodgingSizeSqFt() {
        assertEquals(500, reservation.getLodgingSizeSqFt());
    }
}
