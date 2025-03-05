// Declares the package name for the project, grouping related classes together.
package com.swen_646_project_1.reservation;
// Imports the custom exception class for handling invalid parameter inputs.
import com.swen_646_project_1.exceptions.IllegalParameter_Exception;
// Import for handing reservation start dates
import java.time.LocalDate;
// Import Address class to handle lodging and mailing addresses in reservations
import com.swen_646_project_1.Address;

/**
 * Represents a house reservation with multiple floors.
 * Inherits from Reservation.
 * Includes an additional attribute for number of floors.
 */
public class HouseReservation extends Reservation {

    // Attributes
    private int numFloors;      // Number of floors in the reserved house

    /**
     * Constructor to initialize a HouseReservation object.
     *
     * @param reservationNumber      Unique identifier for the reservation (cannot be null or empty).
     * @param accountNumber          Account number associated with the reservation (cannot be null or empty).
     * @param lodgingPhysicalAddress Physical address of the reserved house (cannot be null or empty).
     * @param lodgingMailingAddress  Mailing address of the lodging (optional, can be null).
     * @param startDate              The start date of the reservation (cannot be null).
     * @param numNights              Number of nights for the reservation (must be positive).
     * @param numBeds                Number of beds in the house.
     * @param numBedrooms            Number of bedrooms in the house.
     * @param numBathrooms           Number of bathrooms in the house.
     * @param lodgingSizeSqFt        Size of the lodging in square feet (must be positive).
     * @param lodgingPrice           Price per night for the lodging (must be positive).
     * @param numFloors              Number of floors in the reserved house (must be positive).
     */
    public HouseReservation(String reservationNumber, String accountNumber, Address lodgingPhysicalAddress,
                            Address lodgingMailingAddress, LocalDate startDate, int numNights, int numBeds,
                            int numBedrooms, int numBathrooms, int lodgingSizeSqFt, double lodgingPrice,
                            int numFloors){

        // Calls the superclass (Reservation) constructor to initialize common reservation attributes.
        super(reservationNumber, accountNumber, lodgingPhysicalAddress, lodgingMailingAddress, startDate, numNights,
                numBeds, numBedrooms, numBathrooms, lodgingSizeSqFt, lodgingPrice);

        // Validate numFloors to ensure it is a positive number.
        /*
         * if numFloors is less than or equal to 0
         *      throw an IllegalArgumentException: "Number of floors must be positive."
         */
        if (numFloors <= 0) {
            throw new IllegalParameter_Exception("N/A", "N/A",
                    "A house cannot have zero or negative floors. Please enter a valid number..");
        } // End if statement

        // Assign specific attributes for HouseReservation.
        this.numFloors = numFloors;

    } // End HouseReservation constructor

    /**
     * Retrieves the number of floors in the reserved house.
     * @return The number of floors.
     */
    public int getNumFloors() {
        return numFloors;
    } // End getNumFloors method

    /**
     * Updates the number of floors in the reserved house.
     * @param numFloors The new number of floors.
     */
    public void setNumFloors(int numFloors) {
        if (numFloors <= 0) {
            throw new IllegalParameter_Exception("N/A", "N/A",
                    "A house cannot have zero or negative floors. Please enter a valid number..");
        }
        this.numFloors = numFloors;
    } // End setNumFloors method

    /**
     * Calculates the price per night for the house reservation.
     * Implementation will likely depend on specific pricing logic.
     * @return The price per night as a double
     */
    @Override
    public double calculatePricePerNight(){

        /*
         * return price per night for the house
         * may include additional cost based on the number of floors
         */
        return 0.0d;

    } // End calculatePricePerNight method

    /**
     * Returns a string representation of the house reservation details.
     * @return A formatted string containing reservation details
     */
    @Override
    public String toString(){

        /*
         * format and return house reservation details as a string
         */
        return "ReservationNumber: " + this.reservationNumber +
                ", AccountNumber: " + this.accountNumber +
                ", LodgingPhysicalAddress: " + this.lodgingPhysicalAddress +
                ", LodgingMailingAddress: " + (this.lodgingMailingAddress != null ? this.lodgingMailingAddress : "N/A") +
                ", StartDate: " + this.startDate +
                ", NumNights: " + this.numNights +
                ", NumBeds: " + this.numBeds +
                ", NumBedrooms: " + this.numBedrooms +
                ", NumBathrooms: " + this.numBathrooms +
                ", LodgingSizeSqFt: " + this.lodgingSizeSqFt +
                ", LodgingPrice: " + this.lodgingPrice +
                ", Status: " + this.status +
                ", NumFloors: " + this.numFloors;
    } // End toString method

    /**
     * Creates a HouseReservation object from a formatted string.
     * @param data A string containing house reservation details in a predefined format
     * @return A HouseReservation object created from the provided data
     */
    public static HouseReservation fromString(String data){

        /*
         * parse data string
         * extract house reservation details
         * return new HouseReservation object with extracted details
         */
        String[] parts = data.split(",");
        if (parts.length < 12) { // FIXED: Ensure enough parameters exist
            throw new IllegalArgumentException("Invalid data format for HouseReservation.");
        } // End If statement

        Address lodgingPhysicalAddress = new Address(parts[2], parts[3], parts[4], Integer.parseInt(parts[5]));
        Address lodgingMailingAddress = parts[6].equals("null") ? null :
                new Address(parts[6], parts[7], parts[8], Integer.parseInt(parts[9]));

        return new HouseReservation(parts[0], parts[1], lodgingPhysicalAddress, lodgingMailingAddress,
                LocalDate.parse(parts[10]), Integer.parseInt(parts[11]), Integer.parseInt(parts[12]),
                Integer.parseInt(parts[13]), Integer.parseInt(parts[14]), Integer.parseInt(parts[15]),
                Double.parseDouble(parts[16]), Integer.parseInt(parts[17]));
    } // End fromString method

} // End class HouseReservation
