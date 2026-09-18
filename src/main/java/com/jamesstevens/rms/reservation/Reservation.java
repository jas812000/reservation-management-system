package com.jamesstevens.rms.reservation;

import com.jamesstevens.rms.Address;
import com.jamesstevens.rms.enums.ReservationStatus;
import com.jamesstevens.rms.exceptions.IllegalLoadException;
import com.jamesstevens.rms.exceptions.IllegalOperationException;
import com.jamesstevens.rms.exceptions.IllegalParameterException;
import com.jamesstevens.rms.exceptions.IllegalStateException;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Abstract base class representing a reservation.
 * <p>
 * A reservation belongs to an account and describes the lodging details, start date, duration,
 * price, and current status. Concrete reservation types (e.g., cabin, hotel, house) extend this
 * class and implement pricing and serialization/deserialization logic.
 * </p>
 *
 * <h2>Address rules</h2>
 * <ul>
 *   <li><b>Hotel/House:</b> lodging mailing address must always equal lodging physical address.</li>
 *   <li><b>Cabin:</b> lodging mailing address may differ from lodging physical address.</li>
 * </ul>
 * <p>
 * This behavior is controlled by {@link #supportsSeparateMailingAddress()} and enforced by
 * {@link #enforceMailingAddressRule()}.
 * </p>
 */
public abstract class Reservation {

    protected final String reservationNumber;
    protected final String accountNumber;

    protected Address lodgingPhysicalAddress;
    protected Address lodgingMailingAddress;

    protected LocalDate startDate;
    protected int numNights;
    protected int numBeds;
    protected int numBedrooms;
    protected int numBathrooms;
    protected int lodgingSizeSqFt;
    protected double lodgingPrice;

    protected ReservationStatus status;

    /**
     * Constructs a new {@code Reservation}.
     *
     * @param reservationNumber      unique reservation identifier (required)
     * @param accountNumber          associated account number (required)
     * @param lodgingPhysicalAddress lodging physical address (required)
     * @param lodgingMailingAddress  lodging mailing address (optional for types that allow it)
     * @param startDate              reservation start date (required)
     * @param numNights              number of nights (must be positive)
     * @param numBeds                number of beds (must be positive)
     * @param numBedrooms            number of bedrooms (must be positive)
     * @param numBathrooms           number of bathrooms (must be positive)
     * @param lodgingSizeSqFt        lodging square footage (must be positive)
     * @param lodgingPrice           lodging price per night (must be non-negative)
     * @throws IllegalParameterException if any required input is invalid
     */
    public Reservation(
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
            double lodgingPrice
    ) {
        if (reservationNumber == null || reservationNumber.isBlank()) {
            throw new IllegalParameterException("N/A", "N/A", "Reservation number cannot be empty.");
        }

        reservationNumber = reservationNumber.trim();

        if (!reservationNumber.matches("res-(CAB|HOT|HOU)\\d{8}")) {
            throw new IllegalParameterException(
                    accountNumber,
                    reservationNumber,
                    "Reservation number must use the format res-CAB########, res-HOT########, or res-HOU########."
            );
        }
        if (accountNumber == null || accountNumber.isBlank()) {
            throw new IllegalParameterException("N/A", "N/A", "Account number cannot be empty.");
        }

        accountNumber = accountNumber.trim().toUpperCase();

        if (lodgingPhysicalAddress == null) {
            throw new IllegalParameterException("N/A", "N/A", "Lodging physical address cannot be null.");
        }
        if (startDate == null) {
            throw new IllegalParameterException("N/A", "N/A", "Start date cannot be null.");
        }
        if (numNights <= 0) {
            throw new IllegalParameterException("N/A", "N/A", "Number of nights must be positive.");
        }
        if (numBeds <= 0) {
            throw new IllegalParameterException("N/A", "N/A", "Number of beds must be positive.");
        }
        if (numBedrooms <= 0) {
            throw new IllegalParameterException("N/A", "N/A", "Number of bedrooms must be positive.");
        }
        if (numBathrooms <= 0) {
            throw new IllegalParameterException("N/A", "N/A", "Number of bathrooms must be positive.");
        }
        if (lodgingSizeSqFt <= 0) {
            throw new IllegalParameterException("N/A", "N/A", "Lodging size must be positive.");
        }
        if (lodgingPrice < 0) {
            throw new IllegalParameterException("N/A", "N/A", "Lodging price cannot be negative.");
        }

        this.reservationNumber = reservationNumber;
        this.accountNumber = accountNumber;

        // Defensive copies (Address is mutable).
        this.lodgingPhysicalAddress = copyAddress(lodgingPhysicalAddress);
        this.lodgingMailingAddress = (lodgingMailingAddress == null) ? null : copyAddress(lodgingMailingAddress);

        this.startDate = startDate;
        this.numNights = numNights;
        this.numBeds = numBeds;
        this.numBedrooms = numBedrooms;
        this.numBathrooms = numBathrooms;
        this.lodgingSizeSqFt = lodgingSizeSqFt;
        this.lodgingPrice = lodgingPrice;

        this.status = ReservationStatus.DRAFT;

        enforceMailingAddressRule();
    }

    /**
     * Indicates whether this reservation type supports a mailing address that differs from the physical address.
     * <p>
     * Default behavior is {@code false} (Hotel/House behavior). Cabin overrides this to {@code true}.
     * </p>
     *
     * @return {@code true} if mailing address may differ; otherwise {@code false}
     */
    protected boolean supportsSeparateMailingAddress() {
        return false;
    }

    /**
     * Enforces the address rule for this reservation type.
     * <p>
     * For Hotel/House (default), mailing is forced to match physical.
     * For Cabin, if mailing is missing, it is defaulted to physical.
     * </p>
     */
    protected final void enforceMailingAddressRule() {
        if (!supportsSeparateMailingAddress()) {
            lodgingMailingAddress = copyAddress(lodgingPhysicalAddress);
        } else {
            if (lodgingMailingAddress == null && lodgingPhysicalAddress != null) {
                lodgingMailingAddress = copyAddress(lodgingPhysicalAddress);
            }
        }
    }

    /**
     * Creates a defensive copy of an {@link Address}.
     *
     * @param a address to copy (must not be null)
     * @return a new {@code Address} instance with the same field values
     */
    protected final Address copyAddress(Address a) {
        return new Address(a.getStreet(), a.getCity(), a.getState(), a.getZipCode());
    }

    /**
     * Returns the unique reservation number.
     *
     * @return reservation number
     */
    public String getReservationNumber() {
        return reservationNumber;
    }

    /**
     * Returns the account number associated with this reservation.
     *
     * @return account number
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * Returns the current reservation status.
     *
     * @return reservation status
     */
    public ReservationStatus getStatus() {
        return status;
    }

    /**
     * Restores the reservation status from persisted data.
     * <p>
     * This method is intended only for deserialization. Normal lifecycle
     * transitions must use {@link #completeReservation()} or
     * {@link #cancelReservation()}.
     * </p>
     *
     * @param status persisted reservation status
     * @throws IllegalParameterException if {@code status} is null
     */
    protected void restoreStatus(ReservationStatus status) {
        if (status == null) {
            throw new IllegalParameterException(
                    accountNumber,
                    reservationNumber,
                    "Status cannot be null."
            );
        }

        this.status = status;
    }

    /**
     * Returns the lodging physical address.
     *
     * @return physical address
     */
    public Address getLodgingPhysicalAddress() {
        return lodgingPhysicalAddress;
    }

    /**
     * Returns the lodging mailing address.
     *
     * @return mailing address
     */
    public Address getLodgingMailingAddress() {
        return lodgingMailingAddress;
    }

    /**
     * Updates the lodging physical address.
     * <p>
     * If this reservation does not support a separate mailing address, the mailing address is also updated
     * to match the physical address.
     * </p>
     *
     * @param street street line (required)
     * @param city   city (required)
     * @param state  2-letter state code (required)
     * @param zip    5-digit zip code
     * @throws IllegalStateException     if the reservation is locked (completed/canceled)
     * @throws IllegalParameterException if any address component is invalid
     */
    public void setLodgingPhysicalAddress(String street, String city, String state, String zip) {
        ensureModifiable();

        Address updated = new Address(street, city, state, zip);
        this.lodgingPhysicalAddress = updated;

        if (!supportsSeparateMailingAddress()) {
            this.lodgingMailingAddress = updated;
        } else if (this.lodgingMailingAddress == null) {
            this.lodgingMailingAddress = updated;
        }
    }

    /**
     * Updates the lodging mailing address.
     * <p>
     * For reservation types that do not support separate mailing addresses (Hotel/House),
     * this setter forces mailing to match the current physical address.
     * </p>
     *
     * @param street street line
     * @param city   city
     * @param state  2-letter state code
     * @param zip    5-digit zip code
     * @throws IllegalStateException     if the reservation is locked (completed/canceled)
     * @throws IllegalOperationException if separate mailing addresses are not supported
     * @throws IllegalParameterException if any address component is invalid
     */
    public void setLodgingMailingAddress(String street, String city, String state, String zip) {
        ensureModifiable();

        if (!supportsSeparateMailingAddress()) {
            throw new IllegalOperationException(
                    "Update Mailing Address",
                    accountNumber,
                    reservationNumber,
                    "This reservation type does not support a separate mailing address."
            );
        }

        this.lodgingMailingAddress = new Address(street, city, state, zip);
    }

    /**
     * Returns the reservation start date.
     *
     * @return start date
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Updates the reservation start date.
     *
     * @param startDate new start date (must not be null)
     * @throws IllegalParameterException if {@code startDate} is null
     */
    public void setStartDate(LocalDate startDate) {
        ensureModifiable();

        if (startDate == null) {
            throw new IllegalParameterException(accountNumber, reservationNumber, "Start date cannot be null.");
        }
        this.startDate = startDate;
    }

    /**
     * Returns the number of nights.
     *
     * @return number of nights
     */
    public int getNumNights() {
        return numNights;
    }

    /**
     * Updates the number of nights.
     *
     * @param numNights new number of nights (must be positive)
     * @throws IllegalParameterException if {@code numNights} is not positive
     */
    public void setNumNights(int numNights) {
        ensureModifiable();

        if (numNights <= 0) {
            throw new IllegalParameterException(accountNumber, reservationNumber, "Number of nights must be positive.");
        }
        this.numNights = numNights;
    }

    /**
     * Returns the number of beds.
     *
     * @return number of beds
     */
    public int getNumBeds() {
        return numBeds;
    }

    /**
     * Updates the number of beds.
     *
     * @param numBeds new number of beds (must be positive)
     * @throws IllegalParameterException if {@code numBeds} is not positive
     */
    public void setNumBeds(int numBeds) {
        ensureModifiable();

        if (numBeds <= 0) {
            throw new IllegalParameterException(accountNumber, reservationNumber, "Number of beds must be positive.");
        }
        this.numBeds = numBeds;
    }

    /**
     * Returns the number of bedrooms.
     *
     * @return number of bedrooms
     */
    public int getNumBedrooms() {
        return numBedrooms;
    }

    /**
     * Updates the number of bedrooms.
     *
     * @param numBedrooms new number of bedrooms (must be positive)
     * @throws IllegalParameterException if {@code numBedrooms} is not positive
     */
    public void setNumBedrooms(int numBedrooms) {
        ensureModifiable();

        if (numBedrooms <= 0) {
            throw new IllegalParameterException(accountNumber, reservationNumber, "Number of bedrooms must be positive.");
        }
        this.numBedrooms = numBedrooms;
    }

    /**
     * Returns the number of bathrooms.
     *
     * @return number of bathrooms
     */
    public int getNumBathrooms() {
        return numBathrooms;
    }

    /**
     * Updates the number of bathrooms.
     *
     * @param numBathrooms new number of bathrooms (must be positive)
     * @throws IllegalParameterException if {@code numBathrooms} is not positive
     */
    public void setNumBathrooms(int numBathrooms) {
        ensureModifiable();

        if (numBathrooms <= 0) {
            throw new IllegalParameterException(accountNumber, reservationNumber, "Number of bathrooms must be positive.");
        }
        this.numBathrooms = numBathrooms;
        this.lodgingPrice = calculatePricePerNight();
    }

    /**
     * Returns the lodging square footage.
     *
     * @return square footage
     */
    public int getLodgingSizeSqFt() {
        return lodgingSizeSqFt;
    }

    /**
     * Updates the lodging square footage.
     *
     * @param sizeSqFt new square footage (must be positive)
     * @throws IllegalParameterException if {@code sizeSqFt} is not positive
     */
    public void setLodgingSizeSqFt(int sizeSqFt) {
        ensureModifiable();

        if (sizeSqFt <= 0) {
            throw new IllegalParameterException(accountNumber, reservationNumber, "Lodging size must be positive.");
        }
        this.lodgingSizeSqFt = sizeSqFt;
        this.lodgingPrice = calculatePricePerNight();
    }

    /**
     * Returns the lodging price per night.
     *
     * @return price per night
     */
    public double getLodgingPrice() {
        return lodgingPrice;
    }

    /**
     * Marks the reservation as completed.
     *
     * @throws IllegalStateException if the reservation is canceled or already completed
     */
    public void completeReservation() {
        if (status == ReservationStatus.COMPLETED || status == ReservationStatus.CANCELLED) {
            throw new IllegalStateException(
                    accountNumber,
                    reservationNumber,
                    "Cannot complete a cancelled or already completed reservation."
            );
        }

        status = ReservationStatus.COMPLETED;
    }

    /**
     * Cancels the reservation.
     *
     * @throws IllegalStateException if the reservation is completed or already canceled
     */
    public void cancelReservation() {
        if (status == ReservationStatus.COMPLETED || status == ReservationStatus.CANCELLED) {
            throw new IllegalStateException(
                    accountNumber,
                    reservationNumber,
                    "Cannot cancel a completed or already cancelled reservation."
            );
        }

        status = ReservationStatus.CANCELLED;
    }

    /**
     * Updates this reservation's mutable fields from another reservation
     * of the same runtime type.
     *
     * @param updatedReservation reservation containing new values
     * @return {@code true} if at least one field was changed; otherwise {@code false}
     * @throws IllegalArgumentException if the reservation types do not match
     */
    public boolean updateDetailsFrom(Reservation updatedReservation) {
        if (updatedReservation == null) {
            return false;
        }

        if (!getClass().equals(updatedReservation.getClass())) {
            throw new IllegalArgumentException(
                    "Updated reservation must be the same type as the current reservation."
            );
        }

        ensureModifiable();

        boolean changed = false;

        Address updatedPhysical = updatedReservation.getLodgingPhysicalAddress();

        if (updatedPhysical != null
                && addressesDiffer(lodgingPhysicalAddress, updatedPhysical)) {

            setLodgingPhysicalAddress(
                    updatedPhysical.getStreet(),
                    updatedPhysical.getCity(),
                    updatedPhysical.getState(),
                    updatedPhysical.getZipCode()
            );

            changed = true;
        }

        Address updatedMailing = updatedReservation.getLodgingMailingAddress();

        if (supportsSeparateMailingAddress()) {
            if (updatedMailing == null && lodgingMailingAddress != null) {
                lodgingMailingAddress = null;
                changed = true;
            } else if (updatedMailing != null
                    && addressesDiffer(lodgingMailingAddress, updatedMailing)) {

                setLodgingMailingAddress(
                        updatedMailing.getStreet(),
                        updatedMailing.getCity(),
                        updatedMailing.getState(),
                        updatedMailing.getZipCode()
                );

                changed = true;
            }
        }

        if (!Objects.equals(startDate, updatedReservation.getStartDate())) {
            setStartDate(updatedReservation.getStartDate());
            changed = true;
        }

        if (numNights != updatedReservation.getNumNights()) {
            setNumNights(updatedReservation.getNumNights());
            changed = true;
        }

        if (numBeds != updatedReservation.getNumBeds()) {
            setNumBeds(updatedReservation.getNumBeds());
            changed = true;
        }

        if (numBedrooms != updatedReservation.getNumBedrooms()) {
            setNumBedrooms(updatedReservation.getNumBedrooms());
            changed = true;
        }

        if (numBathrooms != updatedReservation.getNumBathrooms()) {
            setNumBathrooms(updatedReservation.getNumBathrooms());
            changed = true;
        }

        if (lodgingSizeSqFt != updatedReservation.getLodgingSizeSqFt()) {
            setLodgingSizeSqFt(updatedReservation.getLodgingSizeSqFt());
            changed = true;
        }

        if (this instanceof CabinReservation current
                && updatedReservation instanceof CabinReservation updated) {

            if (current.isFullKitchenAvailable() != updated.isFullKitchenAvailable()) {
                current.setFullKitchenAvailable(updated.isFullKitchenAvailable());
                changed = true;
            }

            if (current.isLoftAvailable() != updated.isLoftAvailable()) {
                current.setLoftAvailable(updated.isLoftAvailable());
                changed = true;
            }
        }

        if (this instanceof HotelReservation current
                && updatedReservation instanceof HotelReservation updated) {

            if (current.hasKitchenette() != updated.hasKitchenette()) {
                current.setKitchenetteAvailable(updated.hasKitchenette());
                changed = true;
            }
        }

        if (this instanceof HouseReservation current
                && updatedReservation instanceof HouseReservation updated) {

            if (current.getNumFloors() != updated.getNumFloors()) {
                current.setNumFloors(updated.getNumFloors());
                changed = true;
            }
        }

        enforceMailingAddressRule();

        return changed;
    }

    /**
     * Compares two {@link Address} instances by value.
     *
     * @param a first address, which may be {@code null}
     * @param b second address, which may be {@code null}
     * @return {@code true} if the addresses differ; otherwise {@code false}
     */
    private boolean addressesDiffer(Address a, Address b) {
        if (a == b) {
            return false;
        }

        if (a == null || b == null) {
            return true;
        }

        return !Objects.equals(a.getStreet(), b.getStreet())
                || !Objects.equals(a.getCity(), b.getCity())
                || !Objects.equals(a.getState(), b.getState())
                || !Objects.equals(a.getZipCode(), b.getZipCode());
    }

    /**
     * Indicates whether the reservation is locked from modification.
     *
     * @return {@code true} if status is COMPLETED or CANCELLED; otherwise {@code false}
     */
    public boolean isLocked() {
        return status == ReservationStatus.COMPLETED
                || status == ReservationStatus.CANCELLED;
    }

    /**
     * Ensures this reservation can still be modified.
     *
     * @throws IllegalStateException if the reservation is completed or cancelled
     */
    protected final void ensureModifiable() {
        if (isLocked()) {
            throw new IllegalStateException(
                    accountNumber,
                    reservationNumber,
                    "Completed or cancelled reservations cannot be modified."
            );
        }
    }

    /**
     * Calculates the lodging price per night for this reservation type.
     *
     * @return computed price per night
     */
    public abstract double calculatePricePerNight();

    /**
     * Returns the serialized reservation record used for persistence.
     * <p>
     * Concrete subclasses define the exact format and must ensure
     * {@code fromString(String)} can parse the value returned here.
     * </p>
     *
     * @return formatted reservation record
     */
    @Override
    public abstract String toString();

    /**
     * Parses a persisted reservation record into the correct {@link Reservation} subtype.
     * <p>
     * The first CSV token must identify the concrete reservation type, such as
     * {@code CabinReservation}, {@code HotelReservation}, or {@code HouseReservation}.
     * Invalid or malformed persisted data is reported as an
     * {@link IllegalLoadException}. If parsing or domain validation fails,
     * the original exception is preserved as the cause.
     * </p>
     *
     * @param data persisted reservation record line
     * @return parsed reservation object
     * @throws IllegalLoadException if the reservation record cannot be loaded or parsed
     */
    public static Reservation fromString(String data) throws IllegalLoadException {
        try {
            if (data == null || data.trim().isEmpty()) {
                throw new IllegalLoadException(
                        "Reservation Data",
                        "N/A",
                        "Empty reservation record."
                );
            }

            String[] parts = data.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

            if (parts.length == 0) {
                throw new IllegalLoadException(
                        "Reservation Data",
                        "N/A",
                        "Reservation record could not be parsed."
                );
            }

            String type = parts[0].trim();

            return switch (type) {
                case "CabinReservation" -> CabinReservation.fromString(data);
                case "HotelReservation" -> HotelReservation.fromString(data);
                case "HouseReservation" -> HouseReservation.fromString(data);
                default -> throw new IllegalLoadException(
                        "Reservation Data",
                        "N/A",
                        "Unknown reservation type: " + type
                );
            };

        } catch (IllegalLoadException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new IllegalLoadException(
                    "Reservation Data",
                    "N/A",
                    "N/A",
                    e
            );
        }
    }
}