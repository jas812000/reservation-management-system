package com.jamesstevens.rms.reservation;

import com.jamesstevens.rms.Address;
import com.jamesstevens.rms.enums.ReservationStatus;
import com.jamesstevens.rms.exceptions.IllegalLoadException;
import com.jamesstevens.rms.exceptions.IllegalParameterException;

import java.time.LocalDate;

/**
 * Reservation subtype representing a cabin reservation.
 * <p>
 * Adds feature flags for full-kitchen and loft availability. Full-kitchen availability affects pricing.
 * </p>
 * <p>
 * Address rule: cabins support a mailing address that may differ from the physical address.
 * </p>
 */
public class CabinReservation extends Reservation {

    private boolean fullKitchenAvailable;
    private boolean loftAvailable;

    /**
     * Constructs a new {@code CabinReservation}.
     *
     * @param reservationNumber      unique reservation identifier
     * @param accountNumber          account number for the reservation
     * @param lodgingPhysicalAddress physical lodging address (required)
     * @param lodgingMailingAddress  mailing lodging address (may differ from physical)
     * @param startDate              reservation start date (required)
     * @param numNights              number of nights (positive)
     * @param numBeds                number of beds (positive)
     * @param numBedrooms            number of bedrooms (positive)
     * @param numBathrooms           number of bathrooms (positive)
     * @param lodgingSizeSqFt        square footage (positive)
     * @param lodgingPrice           price per night (non-negative)
     * @param fullKitchenAvailable   whether a full kitchen is available
     * @param loftAvailable          whether a loft is available
     */
    public CabinReservation(
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
            boolean fullKitchenAvailable,
            boolean loftAvailable
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

        if (!reservationNumber.trim().startsWith("res-CAB")) {
            throw new IllegalParameterException(
                    accountNumber,
                    reservationNumber,
                    "Cabin reservation number must use the CAB prefix."
            );
        }

        this.fullKitchenAvailable = fullKitchenAvailable;
        this.loftAvailable = loftAvailable;
    }

    /**
     * Indicates that cabin reservations support a mailing address separate from the physical address.
     *
     * @return {@code true}
     */
    @Override
    protected boolean supportsSeparateMailingAddress() {
        return true;
    }

    /**
     * Indicates whether a full kitchen is available.
     *
     * @return {@code true} if a full kitchen is available; otherwise {@code false}
     */
    public boolean isFullKitchenAvailable() {
        return fullKitchenAvailable;
    }

    /**
     * Indicates whether a loft is available.
     *
     * @return {@code true} if a loft is available; otherwise {@code false}
     */
    public boolean isLoftAvailable() {
        return loftAvailable;
    }

    /**
     * Updates full kitchen availability.
     *
     * @param fullKitchenAvailable new value
     */
    public void setFullKitchenAvailable(boolean fullKitchenAvailable) {
        ensureModifiable();
        this.fullKitchenAvailable = fullKitchenAvailable;
        this.lodgingPrice = calculatePricePerNight();
    }

    /**
     * Updates loft availability.
     *
     * @param loftAvailable new value
     */
    public void setLoftAvailable(boolean loftAvailable) {
        ensureModifiable();
        this.loftAvailable = loftAvailable;
    }

    /**
     * Calculates the price per night for a cabin reservation.
     *
     * @return computed nightly price
     */
    @Override
    public double calculatePricePerNight() {

        double basePrice = 120.0;
        if (lodgingSizeSqFt > 900) {
            basePrice += 15.0;
        }
        if (fullKitchenAvailable) {
            basePrice += 20.0;
        }

        basePrice += (numBathrooms * 5);
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
                "CabinReservation,%s,%s,\"%s\"%s,%s,%d,%d,%d,%d,%d,%.2f,%s,%b,%b",
                reservationNumber,
                accountNumber,
                String.join(";", lodgingPhysicalAddress.getStreet(), lodgingPhysicalAddress.getCity(),
                        lodgingPhysicalAddress.getState(), String.valueOf(lodgingPhysicalAddress.getZipCode())),
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
                fullKitchenAvailable,
                loftAvailable
        );
    }

    /**
     * Parses a persisted {@code CabinReservation} record into an object.
     *
     * @param data persisted record line
     * @return parsed {@code CabinReservation}
     * @throws IllegalLoadException if the persisted record structure is invalid
     */
    public static CabinReservation fromString(String data) {
        String[] parts = splitCsvPreservingQuotes(data);
        if (parts.length < 15) {
            throw new IllegalLoadException("CabinReservation Data", "N/A",
                    "Invalid data format. Found: " + parts.length);
        }

        Address physical = parsePhysicalAddress(parts);
        Address mailing = parseMailingAddress(parts);
        ReservationStatus parsedStatus = ReservationStatus.valueOf(parts[12].trim());

        CabinReservation reservation = new CabinReservation(
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
                Boolean.parseBoolean(parts[13].trim()),
                Boolean.parseBoolean(parts[14].trim())
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
                throw new IllegalLoadException("CabinReservation Address", "N/A", "Invalid mailing address format.");
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
            throw new IllegalLoadException("CabinReservation Address", "N/A", "Invalid physical address format.");
        }
        return new Address(
                physicalAddressParts[0],
                physicalAddressParts[1],
                physicalAddressParts[2],
                physicalAddressParts[3]
        );
    }
}
