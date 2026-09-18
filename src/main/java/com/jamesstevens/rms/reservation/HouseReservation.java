package com.jamesstevens.rms.reservation;

import com.jamesstevens.rms.Address;
import com.jamesstevens.rms.enums.ReservationStatus;
import com.jamesstevens.rms.exceptions.IllegalLoadException;
import com.jamesstevens.rms.exceptions.IllegalParameterException;

import java.time.LocalDate;

/**
 * Reservation subtype representing a house reservation.
 * <p>
 * A house reservation includes an additional {@code numFloors} attribute.
 * </p>
 * <p>
 * Address rule: for houses, the mailing address is always enforced to match the physical address
 * by the base {@link Reservation} implementation.
 * </p>
 */
public class HouseReservation extends Reservation {

    private int numFloors;

    /**
     * Constructs a new {@code HouseReservation}.
     *
     * @param reservationNumber      unique reservation identifier
     * @param accountNumber          account number for the reservation
     * @param lodgingPhysicalAddress physical lodging address (required)
     * @param lodgingMailingAddress  mailing lodging address (ignored for houses; forced to match physical)
     * @param startDate              reservation start date (required)
     * @param numNights              number of nights (positive)
     * @param numBeds                number of beds (positive)
     * @param numBedrooms            number of bedrooms (positive)
     * @param numBathrooms           number of bathrooms (positive)
     * @param lodgingSizeSqFt        square footage (positive)
     * @param lodgingPrice           price per night (non-negative)
     * @param numFloors              number of floors (positive)
     * @throws IllegalParameterException if {@code numFloors} is not positive
     */
    public HouseReservation(
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
            int numFloors
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

        if (!reservationNumber.trim().startsWith("res-HOU")) {
            throw new IllegalParameterException(
                    accountNumber,
                    reservationNumber,
                    "House reservation number must use the HOU prefix."
            );
        }

        if (numFloors <= 0) {
            throw new IllegalParameterException(
                    "N/A",
                    "N/A",
                    "A house cannot have zero or negative floors. Please enter a valid number."
            );
        }

        this.numFloors = numFloors;
    }

    /**
     * Returns the number of floors in the reserved house.
     *
     * @return number of floors
     */
    public int getNumFloors() {
        return numFloors;
    }

    /**
     * Updates the number of floors.
     *
     * @param numFloors new number of floors (must be positive)
     * @throws IllegalParameterException if {@code numFloors} is not positive
     */
    public void setNumFloors(int numFloors) {
        ensureModifiable();

        if (numFloors <= 0) {
            throw new IllegalParameterException(
                    "N/A",
                    "N/A",
                    "A house cannot have zero or negative floors. Please enter a valid number."
            );
        }
        this.numFloors = numFloors;
    }

    /**
     * Calculates the price per night for a house reservation.
     *
     * @return computed nightly price
     */
    @Override
    public double calculatePricePerNight() {

        double basePrice = 120.0;
        if (lodgingSizeSqFt > 900) {
            basePrice += 15.0;
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
                "HouseReservation,%s,%s,\"%s\"%s,%s,%d,%d,%d,%d,%d,%.2f,%s,%d",
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
                numFloors
        );
    }

    /**
     * Parses a persisted {@code HouseReservation} record into an object.
     *
     * @param data persisted record line
     * @return parsed {@code HouseReservation}
     * @throws IllegalLoadException if the persisted record structure is invalid
     */
    public static HouseReservation fromString(String data) {
        String[] parts = splitCsvPreservingQuotes(data);
        if (parts.length < 14) {
            throw new IllegalLoadException(
                    "HouseReservation Data",
                    "N/A",
                    "Invalid data format. Found: " + parts.length
            );
        }

        Address physical = parsePhysicalAddress(parts);
        Address mailing = parseMailingAddress(parts);
        ReservationStatus parsedStatus = ReservationStatus.valueOf(parts[12].trim());

        HouseReservation reservation = new HouseReservation(
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
                Integer.parseInt(parts[13].trim())
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
                throw new IllegalLoadException("HouseReservation Address", "N/A", "Invalid mailing address format.");
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
            throw new IllegalLoadException("HouseReservation Address", "N/A", "Invalid physical address format.");
        }
        return new Address(
                physicalAddressParts[0],
                physicalAddressParts[1],
                physicalAddressParts[2],
                physicalAddressParts[3]
        );
    }
}
