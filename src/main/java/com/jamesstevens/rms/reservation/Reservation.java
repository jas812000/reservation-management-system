package com.jamesstevens.rms.reservation;

import com.jamesstevens.rms.Address;
import com.jamesstevens.rms.Manager;
import com.jamesstevens.rms.enums.ReservationStatus;
import com.jamesstevens.rms.exceptions.*;

import java.lang.reflect.Field;
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
    protected String accountNumber;

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
     * @throws IllegalParameter_Exception if any required input is invalid
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
        if (reservationNumber == null || reservationNumber.isEmpty()) {
            throw new IllegalParameter_Exception("N/A", "N/A", "Reservation number cannot be empty.");
        }
        if (accountNumber == null || accountNumber.isEmpty()) {
            throw new IllegalParameter_Exception("N/A", "N/A", "Account number cannot be empty.");
        }
        if (lodgingPhysicalAddress == null) {
            throw new IllegalParameter_Exception("N/A", "N/A", "Lodging physical address cannot be null.");
        }
        if (startDate == null) {
            throw new IllegalParameter_Exception("N/A", "N/A", "Start date cannot be null.");
        }
        if (numNights <= 0) {
            throw new IllegalParameter_Exception("N/A", "N/A", "Number of nights must be positive.");
        }
        if (numBeds <= 0) {
            throw new IllegalParameter_Exception("N/A", "N/A", "Number of beds must be positive.");
        }
        if (numBedrooms <= 0) {
            throw new IllegalParameter_Exception("N/A", "N/A", "Number of bedrooms must be positive.");
        }
        if (numBathrooms <= 0) {
            throw new IllegalParameter_Exception("N/A", "N/A", "Number of bathrooms must be positive.");
        }
        if (lodgingSizeSqFt <= 0) {
            throw new IllegalParameter_Exception("N/A", "N/A", "Lodging size must be positive.");
        }
        if (lodgingPrice < 0) {
            throw new IllegalParameter_Exception("N/A", "N/A", "Lodging price cannot be negative.");
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
     * Updates the account number reference for this reservation.
     * <p>
     * This is typically used during deserialization or internal bookkeeping.
     * </p>
     *
     * @param accountNumber new account number (must be non-null/non-empty)
     */
    public void setAccountNumber(String accountNumber) {
        if (accountNumber != null && !accountNumber.isEmpty()) {
            this.accountNumber = accountNumber;
        }
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
     * Updates the reservation status.
     *
     * @param status new status (must not be null)
     * @throws IllegalParameter_Exception if {@code status} is null
     */
    public void setStatus(ReservationStatus status) {
        if (status == null) {
            throw new IllegalParameter_Exception("N/A", "N/A", "Status cannot be null.");
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
     * Updates the lodging physical address and persists the reservation.
     * <p>
     * If this reservation does not support a separate mailing address, the mailing address is also updated
     * to match the physical address.
     * </p>
     *
     * @param street street line (required)
     * @param city city (required)
     * @param state 2-letter state code (required)
     * @param zip 5-digit zip code
     * @throws IllegalState_Exception if the reservation is locked (completed/canceled)
     * @throws IllegalParameter_Exception if any address component is invalid
     * @throws IllegalSave_Exception if persistence fails
     */
    public void setLodgingPhysicalAddress(String street, String city, String state, int zip) {
        if (isLocked()) {
            throw new IllegalState_Exception(accountNumber, reservationNumber,
                    "Cannot update addresses for a completed or cancelled reservation.");
        }

        Address updated = new Address(street, city, state, zip);
        this.lodgingPhysicalAddress = updated;

        if (!supportsSeparateMailingAddress()) {
            this.lodgingMailingAddress = updated;
        } else if (this.lodgingMailingAddress == null) {
            this.lodgingMailingAddress = updated;
        }

        Manager.saveReservationToFile(this);
    }

    /**
     * Updates the lodging mailing address and persists the reservation.
     * <p>
     * For reservation types that do not support separate mailing addresses (Hotel/House),
     * this setter forces mailing to match the current physical address.
     * </p>
     *
     * @param street street line
     * @param city city
     * @param state 2-letter state code
     * @param zip 5-digit zip code
     * @throws IllegalState_Exception if the reservation is locked (completed/canceled)
     * @throws IllegalOperation_Exception if separate mailing addresses are not supported
     * @throws IllegalParameter_Exception if any address component is invalid
     * @throws IllegalSave_Exception if persistence fails
     */
    public void setLodgingMailingAddress(String street, String city, String state, int zip) {
        if (isLocked()) {
            throw new IllegalState_Exception(accountNumber, reservationNumber,
                    "Cannot update addresses for a completed or cancelled reservation.");
        }

        if (!supportsSeparateMailingAddress()) {
            throw new IllegalOperation_Exception(
                    "Update Mailing Address",
                    accountNumber,
                    reservationNumber,
                    "This reservation type does not support a separate mailing address."
            );
        }

        this.lodgingMailingAddress = new Address(street, city, state, zip);
        Manager.saveReservationToFile(this);
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
     * @throws IllegalParameter_Exception if {@code startDate} is null
     */
    public void setStartDate(LocalDate startDate) {
        if (startDate == null) {
            throw new IllegalParameter_Exception(accountNumber, reservationNumber, "Start date cannot be null.");
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
     * @throws IllegalParameter_Exception if {@code numNights} is not positive
     */
    public void setNumNights(int numNights) {
        if (numNights <= 0) {
            throw new IllegalParameter_Exception(accountNumber, reservationNumber, "Number of nights must be positive.");
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
     * @throws IllegalParameter_Exception if {@code numBeds} is not positive
     */
    public void setNumBeds(int numBeds) {
        if (numBeds <= 0) {
            throw new IllegalParameter_Exception(accountNumber, reservationNumber, "Number of beds must be positive.");
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
     * @throws IllegalParameter_Exception if {@code numBedrooms} is not positive
     */
    public void setNumBedrooms(int numBedrooms) {
        if (numBedrooms <= 0) {
            throw new IllegalParameter_Exception(accountNumber, reservationNumber, "Number of bedrooms must be positive.");
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
     * @throws IllegalParameter_Exception if {@code numBathrooms} is not positive
     */
    public void setNumBathrooms(int numBathrooms) {
        if (numBathrooms <= 0) {
            throw new IllegalParameter_Exception(accountNumber, reservationNumber, "Number of bathrooms must be positive.");
        }
        this.numBathrooms = numBathrooms;
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
     * @throws IllegalParameter_Exception if {@code sizeSqFt} is not positive
     */
    public void setLodgingSizeSqFt(int sizeSqFt) {
        if (sizeSqFt <= 0) {
            throw new IllegalParameter_Exception(accountNumber, reservationNumber, "Lodging size must be positive.");
        }
        this.lodgingSizeSqFt = sizeSqFt;
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
     * Updates the lodging price per night.
     *
     * @param lodgingPrice new price per night (must be non-negative)
     * @throws IllegalParameter_Exception if {@code lodgingPrice} is negative
     */
    public void setLodgingPrice(double lodgingPrice) {
        if (lodgingPrice < 0) {
            throw new IllegalParameter_Exception(accountNumber, reservationNumber, "Price cannot be negative.");
        }
        this.lodgingPrice = lodgingPrice;
    }

    /**
     * Marks the reservation as completed and persists the update.
     *
     * @throws IllegalState_Exception if the reservation is canceled or already completed
     */
    public void completeReservation() {
        if (status == ReservationStatus.COMPLETED || status == ReservationStatus.CANCELLED) {
            throw new IllegalState_Exception(
                    accountNumber,
                    reservationNumber,
                    "Cannot complete a cancelled or already completed reservation."
            );
        }

        status = ReservationStatus.COMPLETED;

        try {
            Manager.saveReservationToFile(this);
        } catch (IllegalSave_Exception e) {
            System.out.println("Error saving updated reservation: " + e.getMessage());
        }
    }

    /**
     * Cancels the reservation and persists the update.
     *
     * @throws IllegalState_Exception if the reservation is completed or already canceled
     */
    public void cancelReservation() {
        if (status == ReservationStatus.COMPLETED || status == ReservationStatus.CANCELLED) {
            throw new IllegalState_Exception(
                    accountNumber,
                    reservationNumber,
                    "Cannot cancel a completed or already cancelled reservation."
            );
        }

        status = ReservationStatus.CANCELLED;
        lodgingPrice = 0.00;

        try {
            Manager.saveReservationToFile(this);
        } catch (IllegalSave_Exception e) {
            System.out.println("Error saving updated reservation: " + e.getMessage());
        }
    }

    /**
     * Updates this reservation's mutable fields from another reservation of the same runtime type.
     * <p>
     * Identity fields ({@code reservationNumber}, {@code accountNumber}) are not modified.
     * Lodging addresses are deep-copied to prevent sharing mutable {@link Address} references.
     * </p>
     * <p>
     * After updates, {@link #enforceMailingAddressRule()} is applied to preserve the correct address invariant
     * for the reservation type.
     * </p>
     *
     * @param updatedReservation reservation containing new values
     * @return {@code true} if at least one field was changed; otherwise {@code false}
     * @throws IllegalArgumentException if {@code updatedReservation} is not the same runtime type
     * @throws RuntimeException         if a reflection access error occurs
     */
    public boolean updateDetailsFrom(Reservation updatedReservation) {
        if (updatedReservation == null) {
            return false;
        }

        if (!getClass().equals(updatedReservation.getClass())) {
            throw new IllegalArgumentException("Updated reservation must be the same type as the current reservation.");
        }

        boolean changed = false;

        Address updatedPhysical = updatedReservation.getLodgingPhysicalAddress();
        if (updatedPhysical != null && addressesDiffer(lodgingPhysicalAddress, updatedPhysical)) {
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
            if (updatedMailing == null) {
                if (lodgingMailingAddress != null) {
                    // If you want to allow clearing mailing for Cabin:
                    lodgingMailingAddress = null;
                    changed = true;
                }
            } else if (addressesDiffer(lodgingMailingAddress, updatedMailing)) {
                setLodgingMailingAddress(
                        updatedMailing.getStreet(),
                        updatedMailing.getCity(),
                        updatedMailing.getState(),
                        updatedMailing.getZipCode()
                );
                changed = true;
            }
        }

        try {
            Class<?> clazz = getClass();
            while (clazz != null) {
                for (Field field : clazz.getDeclaredFields()) {
                    field.setAccessible(true);

                    String name = field.getName();

                    if ("reservationNumber".equals(name) || "accountNumber".equals(name)) {
                        continue;
                    }
                    if ("lodgingPhysicalAddress".equals(name) || "lodgingMailingAddress".equals(name)) {
                        continue;
                    }

                    Object oldValue = field.get(this);
                    Object newValue = field.get(updatedReservation);

                    if (!Objects.equals(oldValue, newValue)) {
                        field.set(this, newValue);
                        changed = true;
                    }
                }
                clazz = clazz.getSuperclass();
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Error updating reservation: " + e.getMessage());
        }

        // Enforce Hotel/House invariant after updates
        Address beforeMailing = lodgingMailingAddress;
        enforceMailingAddressRule();
        if (beforeMailing != null && lodgingMailingAddress != null && addressesDiffer(beforeMailing, lodgingMailingAddress)) {
            changed = true;
        }

        return changed;
    }

    /**
     * Compares two {@link Address} instances by value and returns {@code true} if they differ.
     *
     * @param a first address (non-null)
     * @param b second address (non-null)
     * @return {@code true} if any address field differs; otherwise {@code false}
     */
    private boolean addressesDiffer(Address a, Address b) {
        return !Objects.equals(a.getStreet(), b.getStreet())
                || !Objects.equals(a.getCity(), b.getCity())
                || !Objects.equals(a.getState(), b.getState())
                || a.getZipCode() != b.getZipCode();
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
     * The first CSV token must be the concrete type name, such as:
     * {@code CabinReservation}, {@code HotelReservation}, or {@code HouseReservation}.
     * </p>
     *
     * @param data persisted reservation record line
     * @return parsed reservation object
     * @throws IllegalLoad_Exception if the record is null, blank, or the type token is invalid
     */
    public static Reservation fromString(String data) {
        if (data == null || data.trim().isEmpty()) {
            throw new IllegalLoad_Exception("Reservation Data", "N/A", "Empty reservation record.");
        }

        String[] parts = data.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        if (parts.length == 0) {
            throw new IllegalLoad_Exception("Reservation Data", "N/A", "Reservation record could not be parsed.");
        }

        String type = parts[0].trim();

        return switch (type) {
            case "CabinReservation" -> CabinReservation.fromString(data);
            case "HotelReservation" -> HotelReservation.fromString(data);
            case "HouseReservation" -> HouseReservation.fromString(data);
            default -> throw new IllegalLoad_Exception("Reservation Data", "N/A",
                    "Unknown reservation type: " + type);
        };
    }
}