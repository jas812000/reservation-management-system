package com.jamesstevens.rms.reservation;

import com.jamesstevens.rms.Address;
import com.jamesstevens.rms.enums.ReservationStatus;
import com.jamesstevens.rms.exceptions.IllegalLoadException;
import com.jamesstevens.rms.exceptions.IllegalParameterException;

import java.time.LocalDate;

/**
 * Reservation subtype representing a hotel reservation.
 * <p>
 * Adds a {@code kitchenetteAvailable} feature which can affect pricing.
 * </p>
 * <p>
 * Address rule: for hotels, the mailing address is always enforced to match the physical address
 * by the base {@link Reservation} implementation.
 * </p>
 */
public class HotelReservation extends Reservation {

    private boolean kitchenetteAvailable;

    /**
     * Constructs a new {@code HotelReservation}.
     *
     * @param reservationNumber      unique reservation identifier
     * @param accountNumber          account number for the reservation
     * @param lodgingPhysicalAddress physical lodging address (required)
     * @param lodgingMailingAddress  mailing lodging address (ignored for hotels; forced to match physical)
     * @param startDate              reservation start date (required)
     * @param numNights              number of nights (positive)
     * @param numBeds                number of beds (positive)
     * @param numBedrooms            number of bedrooms (positive)
     * @param numBathrooms           number of bathrooms (positive)
     * @param lodgingSizeSqFt        square footage (positive)
     * @param lodgingPrice           price per night (non-negative)
     * @param kitchenetteAvailable   whether kitchenette is available
     */
    public HotelReservation(
            String reservationNumber,
            String accountNumber,
            Address lodgingPhysicalAddress,
            Address lodgingMailingAddress,
            LocalDate startDate,
            int numNights,
            int numBeds,
            int numBedrooms,
            int numBathrooms,
            int lodgingSizeSqFt,
            double lodgingPrice,
            boolean kitchenetteAvailable
    ) {
        super(
                reservationNumber,
                accountNumber,
                lodgingPhysicalAddress,
                lodgingMailingAddress,
                startDate,
                numNights,
                numBeds,
                numBedrooms,
                numBathrooms,
                lodgingSizeSqFt,
                lodgingPrice
        );

        if (!reservationNumber.trim().startsWith("res-HOT")) {
            throw new IllegalParameterException(
                    accountNumber,
                    reservationNumber,
                    "Hotel reservation number must use the HOT prefix."
            );
        }

        this.kitchenetteAvailable = kitchenetteAvailable;
    }

    /**
     * Indicates whether a kitchenette is available.
     *
     * @return {@code true} if kitchenette is available; otherwise {@code false}
     */
    public boolean hasKitchenette() {
        return kitchenetteAvailable;
    }

    /**
     * Updates kitchenette availability.
     *
     * @param kitchenetteAvailable new value
     */
    public void setKitchenetteAvailable(boolean kitchenetteAvailable) {
        ensureModifiable();
        this.kitchenetteAvailable = kitchenetteAvailable;
        this.lodgingPrice = calculatePricePerNight();
    }

    /**
     * Calculates the price per night for a hotel reservation.
     *
     * @return computed nightly price
     */
    @Override
    public double calculatePricePerNight() {

        double basePrice = 120.0;
        if (lodgingSizeSqFt > 900) {
            basePrice += 15.0;
        }

        basePrice += 50.0;
        if (kitchenetteAvailable) {
            basePrice += 10.0;
        }

        return basePrice;
    }

    /**
     * Serializes this reservation into the persistence format.
     *
     * @return formatted string record
     */
    @Override
    public String toString() {
        return String.format(
                "HotelReservation,%s,%s,\"%s\"%s,%s,%d,%d,%d,%d,%d,%.2f,%s,%b",
                reservationNumber,
                accountNumber,
                String.join(";", lodgingPhysicalAddress.getStreet(),
                        lodgingPhysicalAddress.getCity(),
                        lodgingPhysicalAddress.getState(),
                        String.valueOf(lodgingPhysicalAddress.getZipCode())),
                (lodgingMailingAddress != null
                        ? "," + "\"" + String.join(";", lodgingMailingAddress.getStreet(),
                        lodgingMailingAddress.getCity(),
                        lodgingMailingAddress.getState(),
                        String.valueOf(lodgingMailingAddress.getZipCode())) + "\""
                        : ",N/A"),
                startDate,
                numNights,
                numBeds,
                numBedrooms,
                numBathrooms,
                lodgingSizeSqFt,
                lodgingPrice,
                status,
                kitchenetteAvailable
        );
    }

    /**
     * Parses a persisted {@code HotelReservation} record into an object.
     *
     * @param data persisted record line
     * @return parsed {@code HotelReservation}
     * @throws IllegalLoadException if the persisted record structure is invalid
     */
    public static HotelReservation fromString(String data) {
        String[] parts = splitCsvPreservingQuotes(data);
        if (parts.length < 14) {
            throw new IllegalLoadException("HotelReservation Data", "N/A",
                    "Invalid data format. Found: " + parts.length);
        }

        Address physical = parsePhysicalAddress(parts);
        Address mailing = parseMailingAddress(parts);
        ReservationStatus parsedStatus = ReservationStatus.valueOf(parts[12].trim());

        HotelReservation reservation = new HotelReservation(
                parts[1].trim(),
                parts[2].trim(),
                physical,
                mailing,
                LocalDate.parse(parts[5].trim()),
                Integer.parseInt(parts[6].trim()),
                Integer.parseInt(parts[7].trim()),
                Integer.parseInt(parts[8].trim()),
                Integer.parseInt(parts[9].trim()),
                Integer.parseInt(parts[10].trim()),
                Double.parseDouble(parts[11].trim()),
                Boolean.parseBoolean(parts[13].trim())
        );

        reservation.restoreStatus(parsedStatus);
        return reservation;
    }

    /**
     * Splits a CSV record while preserving quoted sections.
     *
     * @param data raw CSV line
     * @return array of fields
     */
    private static String[] splitCsvPreservingQuotes(String data) {
        return data.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
    }

    /**
     * Parses and returns the lodging mailing address from the serialized reservation fields.
     * <p>
     * The mailing address field is expected at index 4. If the value is {@code "N/A"}, this method
     * returns {@code null}. Otherwise, the field is expected to be quoted and formatted as:
     * {@code "street;city;state;zip"}.
     * </p>
     *
     * @param parts tokenized reservation record
     * @return parsed mailing {@link Address}, or {@code null} if not provided
     * @throws IllegalLoadException if the field is present but not in the expected format
     */
    private static Address parseMailingAddress(String[] parts) {
        if (!parts[4].trim().equals("N/A")) {
            String[] mailingAddressParts = parts[4].replace("\"", "").split(";");
            if (mailingAddressParts.length < 4) {
                throw new IllegalLoadException("HotelReservation Address", "N/A", "Invalid mailing address format.");
            }
            return new Address(
                    mailingAddressParts[0],
                    mailingAddressParts[1],
                    mailingAddressParts[2],
                    mailingAddressParts[3]
            );
        }
        return null;
    }

    /**
     * Parses and returns the lodging physical address from the serialized reservation fields.
     * <p>
     * The physical address field is expected at index 3 and formatted as:
     * {@code "street;city;state;zip"} (quoted).
     * </p>
     *
     * @param parts tokenized reservation record
     * @return parsed physical {@link Address}
     * @throws IllegalLoadException if the physical address field is missing or not in the expected format
     */
    private static Address parsePhysicalAddress(String[] parts) {
        String[] physicalAddressParts = parts[3].replace("\"", "").split(";");
        if (physicalAddressParts.length < 4) {
            throw new IllegalLoadException("HotelReservation Address", "N/A", "Invalid physical address format.");
        }
        return new Address(
                physicalAddressParts[0],
                physicalAddressParts[1],
                physicalAddressParts[2],
                physicalAddressParts[3]
        );
    }
}
