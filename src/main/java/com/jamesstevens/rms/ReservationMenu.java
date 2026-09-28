package com.jamesstevens.rms;

import com.jamesstevens.rms.reservation.CabinReservation;
import com.jamesstevens.rms.reservation.HotelReservation;
import com.jamesstevens.rms.reservation.HouseReservation;
import com.jamesstevens.rms.reservation.Reservation;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 * Provides the console-based menu interface for the Reservation Management System.
 * <p>
 * This class handles user interaction, input validation, menu selection,
 * confirmation prompts, and presentation of account and reservation data.
 * Business logic is delegated to the appropriate application and domain classes.
 * </p>
 */
public class ReservationMenu {

    private final Scanner scanner;
    private final Manager manager;

    /**
     * Creates a reservation management menu using standard input
     * and a new {@link Manager} instance.
     */
    public ReservationMenu() {
        scanner = new Scanner(System.in);
        manager = new Manager();
    }

    /**
     * Starts the Reservation Management System menu.
     * <p>
     * The menu continues displaying until the user selects the exit option.
     * Application errors are reported without terminating the menu.
     * </p>
     */
    public void start() {
        while (true) {
            displayMenu();
            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1" -> createAccount();
                    case "2" -> updateAccount();
                    case "3" -> addReservation();
                    case "4" -> updateReservation();
                    case "5" -> cancelReservation();
                    case "6" -> completeReservation();
                    case "7" -> viewAccount();
                    case "8" -> viewReservation();
                    case "9" -> {
                        System.out.println("Exiting system. Goodbye!");
                        return;
                    }
                    default -> System.out.println(
                            "Invalid option. Please select 1-9."
                    );
                }
            } catch (RuntimeException e) {
                System.out.println(
                        "\nUnable to complete operation: " + e.getMessage()
                );
            }

            System.out.println();
        }
    }

    /**
     * Displays the available reservation management options.
     */
    private void displayMenu() {
        System.out.println();
        System.out.println();
        System.out.println("========== Reservation Management System ==========");
        System.out.println("\t1. Create a New Account");
        System.out.println("\t2. Update Existing Account");
        System.out.println("\t3. Add a Reservation");
        System.out.println("\t4. Update a Reservation");
        System.out.println("\t5. Cancel a Reservation");
        System.out.println("\t6. Complete a Reservation");
        System.out.println("\t7. View Existing Account");
        System.out.println("\t8. View a Reservation");
        System.out.println("\t9. Exit");
        System.out.print("\n\tSelect an option (1-9): ");
    }

    /**
     * Prompts the user for account information and creates a new account.
     */
    private void createAccount() {
        System.out.println("\n========== Create New Account ==========");

        String firstName = readRequiredText("First Name: ");
        String lastName = readRequiredText("Last Name: ");
        Name name = new Name(firstName, lastName);

        Address address = readAddress();
        String phoneNumber = readPhoneNumber();
        String email = readEmail();

        String accountNumber = manager.getNewAccountNumber();

        Account account = new Account(
                accountNumber,
                name,
                address,
                phoneNumber,
                email
        );

        manager.addAccount(account);

        System.out.println("\nAccount created successfully.");
        System.out.println("Account Number: " + accountNumber);
    }

    /**
     * Prompts the user for an existing account number and updated
     * account information.
     */
    private void updateAccount() {
        System.out.println("\n========== Update Existing Account ==========");

        Account account = readExistingAccount();

        if (account == null) {
            return;
        }

        System.out.println("\nCurrent Account Information:");
        displayAccount(account);

        System.out.println("\nEnter new account information.");

        Address address = readAddress();
        String phoneNumber = readPhoneNumber();
        String email = readEmail();

        account.updateAddress(address);
        account.setPhoneNumber(phoneNumber);
        account.setEmail(email);

        manager.updateAccount(account.getAccountNumber());

        System.out.println("\nAccount updated successfully.");
    }

    /**
     * Prompts the user for reservation information and adds a reservation
     * to an existing account.
     */
    private void addReservation() {
        System.out.println("\n========== Add Reservation ==========");

        Account account = readExistingAccount();

        if (account == null) {
            return;
        }

        String reservationType = readReservationType();

        Reservation reservation = readReservation(
                account.getAccountNumber(),
                reservationType,
                null
        );

        account.addReservation(reservation);

        System.out.println("\nReservation created successfully.");
        System.out.println(
                "Reservation Number: " + reservation.getReservationNumber()
        );
    }

    /**
     * Prompts the user for an existing reservation and replaces its mutable
     * details with newly entered values.
     * <p>
     * Completed and cancelled reservations are rejected before any updated
     * reservation information is requested.
     * </p>
     */
    private void updateReservation() {
        System.out.println("\n========== Update Reservation ==========");

        Reservation currentReservation = readExistingReservation();

        if (currentReservation == null) {
            return;
        }

        if (isTerminal(currentReservation)) {
            System.out.println(
                    "\nReservation cannot be updated because its status is "
                            + currentReservation.getStatus() + "."
            );
            return;
        }

        displayReservation(currentReservation);

        String reservationType = getReservationType(currentReservation);

        System.out.println("\nEnter the updated reservation information.");

        Reservation updatedReservation = readReservation(
                currentReservation.getAccountNumber(),
                reservationType,
                currentReservation.getReservationNumber()
        );

        Account account = manager.getAccount(
                currentReservation.getAccountNumber()
        );

        account.updateReservation(
                currentReservation.getReservationNumber(),
                updatedReservation
        );

        System.out.println("\nReservation updated successfully.");
    }

    /**
     * Displays an existing reservation and requests confirmation before
     * cancelling it.
     */
    private void cancelReservation() {
        System.out.println("\n========== Cancel Reservation ==========");

        Reservation reservation = readExistingReservation();

        if (reservation == null) {
            return;
        }

        if (isTerminal(reservation)) {
            System.out.println(
                    "\nReservation cannot be cancelled because its status is "
                            + reservation.getStatus() + "."
            );
            return;
        }

        System.out.println("\nReservation to Cancel:");
        displayReservation(reservation);

        if (!readBoolean("Are you sure you want to cancel this reservation? (y/n): ")) {
            System.out.println("\nReservation was not cancelled.");
            return;
        }

        reservation.cancelReservation();
        Manager.saveReservationToFile(reservation);

        System.out.println("\nReservation cancelled successfully.");
    }

    /**
     * Displays an existing reservation and requests confirmation before
     * completing it.
     */
    private void completeReservation() {
        System.out.println("\n========== Complete Reservation ==========");

        Reservation reservation = readExistingReservation();

        if (reservation == null) {
            return;
        }

        if (isTerminal(reservation)) {
            System.out.println(
                    "\nReservation cannot be completed because its status is "
                            + reservation.getStatus() + "."
            );
            return;
        }

        System.out.println("\nReservation to Complete:");
        displayReservation(reservation);

        if (!readBoolean("Are you sure you want to complete this reservation? (y/n): ")) {
            System.out.println("\nReservation was not completed.");
            return;
        }

        reservation.completeReservation();
        Manager.saveReservationToFile(reservation);

        System.out.println("\nReservation completed successfully.");
    }

    /**
     * Prompts for an account number and displays the matching account information.
     * Reservation information is not displayed as part of the account lookup.
     */
    /**
     * Retrieves and displays an existing account without modifying it.
     */
    private void viewAccount() {
        System.out.println("\n========== View Existing Account ==========");

        Account account = readExistingAccount();

        if (account == null) {
            return;
        }

        System.out.println("\nAccount Information:");
        displayAccount(account);
    }

    /**
     * Provides options for viewing a reservation.
     * <p>
     * A reservation may be viewed directly by reservation number or selected
     * from a compact list of reservations associated with an account.
     * </p>
     */
    private void viewReservation() {
        System.out.println("\n========== View Reservation ==========");

        while (true) {
            System.out.println("\nView Reservation By:");
            System.out.println("\t1. Reservation Number");
            System.out.println("\t2. Account Number");
            System.out.println("\t3. Return to Main Menu");
            System.out.print("Select an option (1-3): ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> {
                    viewReservationByNumber();
                    return;
                }
                case "2" -> {
                    viewReservationByAccount();
                    return;
                }
                case "3" -> {
                    return;
                }
                default -> System.out.println(
                        "Invalid option. Please select 1-3."
                );
            }
        }
    }

    /**
     * Prompts for a reservation number and displays the complete matching
     * reservation.
     * <p>
     * Reservation numbers are unique across the system, so an account number
     * is not required for this lookup.
     * </p>
     */
    private void viewReservationByNumber() {
        System.out.print("\nReservation Number: ");
        String reservationNumber = scanner.nextLine().trim();

        Reservation reservation = findReservationByNumber(reservationNumber);

        if (reservation == null) {
            System.out.println("Reservation not found.");
            return;
        }

        System.out.println("\nReservation Information:");
        displayReservation(reservation);
    }

    /**
     * Prompts for an account number, displays a compact list of that account's
     * reservations, and allows the user to select one for full display.
     */
    private void viewReservationByAccount() {
        System.out.println();

        Account account = readExistingAccount();

        if (account == null) {
            return;
        }

        List<Reservation> reservations = account.getAllReservations()
                .stream()
                .sorted(
                        Comparator.comparing(Reservation::getStartDate)
                                .thenComparing(Reservation::getReservationNumber)
                )
                .toList();

        if (reservations.isEmpty()) {
            System.out.println(
                    "No reservations found for account "
                            + account.getAccountNumber() + "."
            );
            return;
        }

        System.out.println(
                "\nReservations for Account "
                        + account.getAccountNumber() + ":"
        );

        displayReservationSummary(reservations);

        while (true) {
            System.out.print(
                    "\nReservation Number to View "
                            + "(or press Enter to return): "
            );

            String reservationNumber = scanner.nextLine().trim();

            if (reservationNumber.isBlank()) {
                return;
            }

            Reservation reservation =
                    account.getReservation(reservationNumber);

            if (reservation == null) {
                System.out.println(
                        "Reservation not found for this account."
                );
                continue;
            }

            System.out.println("\nReservation Information:");
            displayReservation(reservation);
            return;
        }
    }

    /**
     * Searches all loaded accounts for a reservation number.
     *
     * @param reservationNumber reservation number to locate
     * @return matching reservation, or {@code null} when no match is found
     */
    private Reservation findReservationByNumber(String reservationNumber) {
        if (reservationNumber == null
                || reservationNumber.trim().isEmpty()) {
            return null;
        }

        String normalizedReservationNumber =
                reservationNumber.trim().toUpperCase();

        for (Account account : manager.getAccounts()) {
            Reservation reservation =
                    account.getReservation(normalizedReservationNumber);

            if (reservation != null) {
                return reservation;
            }
        }

        return null;
    }

    /**
     * Displays a compact summary of reservations for selection.
     *
     * @param reservations reservations to display
     */
    private void displayReservationSummary(
            List<Reservation> reservations
    ) {
        System.out.printf(
                "%-20s %-10s %-12s %-12s%n",
                "Reservation Number",
                "Type",
                "Start Date",
                "Status"
        );

        System.out.printf(
                "%-20s %-10s %-12s %-12s%n",
                "--------------------",
                "----------",
                "------------",
                "------------"
        );

        for (Reservation reservation : reservations) {
            System.out.printf(
                    "%-20s %-10s %-12s %-12s%n",
                    reservation.getReservationNumber(),
                    getReservationType(reservation),
                    reservation.getStartDate(),
                    reservation.getStatus()
            );
        }
    }

    /**
     * Prompts for an existing account.
     *
     * @return matching account, or {@code null} if no account is found
     */
    private Account readExistingAccount() {
        System.out.print("Account Number: ");
        String accountNumber = scanner.nextLine().trim();

        Account account = manager.getAccount(accountNumber);

        if (account == null) {
            System.out.println("Account not found.");
        }

        return account;
    }

    /**
     * Prompts for an existing account and reservation.
     *
     * @return matching reservation, or {@code null} if the account or
     * reservation cannot be found
     */
    private Reservation readExistingReservation() {
        Account account = readExistingAccount();

        if (account == null) {
            return null;
        }

        System.out.print("Reservation Number: ");
        String reservationNumber = scanner.nextLine().trim();

        Reservation reservation = account.getReservation(reservationNumber);

        if (reservation == null) {
            System.out.println("Reservation not found.");
        }

        return reservation;
    }

    /**
     * Prompts the user for a supported reservation type until a valid
     * selection is entered.
     *
     * @return {@code Cabin}, {@code Hotel}, or {@code House}
     */
    private String readReservationType() {
        while (true) {
            System.out.println("\nReservation Type:");
            System.out.println("\t1. Cabin");
            System.out.println("\t2. Hotel");
            System.out.println("\t3. House");
            System.out.print("Select a reservation type (1-3): ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    return "Cabin";
                case "2":
                    return "Hotel";
                case "3":
                    return "House";
                default:
                    System.out.println(
                            "Invalid reservation type. Please select 1-3."
                    );
            }
        }
    }

    /**
     * Creates a reservation from validated console input.
     *
     * @param accountNumber account associated with the reservation
     * @param reservationType Cabin, Hotel, or House
     * @param reservationNumber existing reservation number when updating;
     *                          {@code null} when creating
     * @return newly constructed reservation
     */
    private Reservation readReservation(
            String accountNumber,
            String reservationType,
            String reservationNumber
    ) {
        if (reservationNumber == null) {
            reservationNumber =
                    manager.getNewReservationNumber(reservationType);
        }

        System.out.println("\nPhysical Address:");
        Address physicalAddress = readAddress();

        Address mailingAddress = physicalAddress;

        if ("Cabin".equals(reservationType)) {
            if (readBoolean(
                    "Is the mailing address different? (y/n): "
            )) {
                System.out.println("\nMailing Address:");
                mailingAddress = readAddress();
            }
        }

        LocalDate startDate = readDate(
                "Start Date (YYYY-MM-DD): "
        );

        int numNights = readInt(
                "Number of Nights: ",
                value -> value > 0,
                "Number of nights must be greater than 0."
        );

        int numBeds = readInt(
                "Number of Beds: ",
                value -> value >= 0,
                "Number of beds cannot be negative."
        );

        int numBedrooms = readInt(
                "Number of Bedrooms: ",
                value -> value >= 0,
                "Number of bedrooms cannot be negative."
        );

        int numBathrooms = readInt(
                "Number of Bathrooms: ",
                value -> value >= 0,
                "Number of bathrooms cannot be negative."
        );

        int lodgingSizeSqFt = readInt(
                "Lodging Size (sq ft): ",
                value -> value > 0,
                "Lodging size must be greater than 0."
        );

        double lodgingPrice = readDouble(
                "Lodging Price per Night: ",
                value -> value >= 0,
                "Lodging price cannot be negative."
        );

        return switch (reservationType) {
            case "Cabin" -> {
                boolean fullKitchenAvailable = readBoolean(
                        "Full Kitchen Available? (y/n): "
                );

                boolean loftAvailable = readBoolean(
                        "Loft Available? (y/n): "
                );

                yield new CabinReservation(
                        reservationNumber,
                        accountNumber,
                        physicalAddress,
                        mailingAddress,
                        startDate,
                        numNights,
                        numBeds,
                        numBedrooms,
                        numBathrooms,
                        lodgingSizeSqFt,
                        lodgingPrice,
                        fullKitchenAvailable,
                        loftAvailable
                );
            }

            case "Hotel" -> {
                boolean kitchenetteAvailable = readBoolean(
                        "Kitchenette Available? (y/n): "
                );

                yield new HotelReservation(
                        reservationNumber,
                        accountNumber,
                        physicalAddress,
                        mailingAddress,
                        startDate,
                        numNights,
                        numBeds,
                        numBedrooms,
                        numBathrooms,
                        lodgingSizeSqFt,
                        lodgingPrice,
                        kitchenetteAvailable
                );
            }

            case "House" -> {
                int numFloors = readInt(
                        "Number of Floors: ",
                        value -> value > 0,
                        "Number of floors must be greater than 0."
                );

                yield new HouseReservation(
                        reservationNumber,
                        accountNumber,
                        physicalAddress,
                        mailingAddress,
                        startDate,
                        numNights,
                        numBeds,
                        numBedrooms,
                        numBathrooms,
                        lodgingSizeSqFt,
                        lodgingPrice,
                        numFloors
                );
            }

            default -> throw new IllegalArgumentException(
                    "Unsupported reservation type."
            );
        };
    }

    /**
     * Determines the menu reservation type for an existing reservation.
     *
     * @param reservation reservation to inspect
     * @return Cabin, Hotel, or House
     */
    private String getReservationType(Reservation reservation) {
        if (reservation instanceof CabinReservation) {
            return "Cabin";
        }

        if (reservation instanceof HotelReservation) {
            return "Hotel";
        }

        if (reservation instanceof HouseReservation) {
            return "House";
        }

        throw new IllegalArgumentException(
                "Unsupported reservation type."
        );
    }

    /**
     * Determines whether a reservation has reached a terminal lifecycle state.
     *
     * @param reservation reservation to inspect
     * @return {@code true} when the reservation is completed or cancelled
     */
    private boolean isTerminal(Reservation reservation) {
        String status = reservation.getStatus().name();

        return "COMPLETED".equals(status)
                || "CANCELLED".equals(status);
    }

    /**
     * Displays account information.
     *
     * @param account account to display
     */
    private void displayAccount(Account account) {
        System.out.println(
                "Account Number: " + account.getAccountNumber()
        );

        System.out.println(
                "Name: " + account.getName()
        );

        displayAddress("Address", account.getAddress());

        System.out.println(
                "Phone Number: " + account.getPhoneNumber()
        );

        System.out.println("Email: " + account.getEmail());
    }

    /**
     * Displays all relevant information for a reservation, including
     * reservation-type-specific details.
     *
     * @param reservation reservation to display
     */
    private void displayReservation(Reservation reservation) {
        System.out.println(
                "Reservation Number: "
                        + reservation.getReservationNumber()
        );

        System.out.println(
                "Account Number: "
                        + reservation.getAccountNumber()
        );

        Account account = manager.getAccount(
                reservation.getAccountNumber()
        );

        if (account != null) {
            System.out.println(
                    "Customer Name: " + account.getName()
            );
        }

        System.out.println(
                "Reservation Type: "
                        + getReservationType(reservation)
        );

        System.out.println(
                "Status: " + reservation.getStatus()
        );

        displayAddress(
                "Physical Address",
                reservation.getLodgingPhysicalAddress()
        );

        displayAddress(
                "Mailing Address",
                reservation.getLodgingMailingAddress()
        );

        System.out.println(
                "Start Date: " + reservation.getStartDate()
        );

        System.out.println(
                "Number of Nights: " + reservation.getNumNights()
        );

        System.out.println(
                "Number of Beds: " + reservation.getNumBeds()
        );

        System.out.println(
                "Number of Bedrooms: "
                        + reservation.getNumBedrooms()
        );

        System.out.println(
                "Number of Bathrooms: "
                        + reservation.getNumBathrooms()
        );

        System.out.println(
                "Lodging Size (sq ft): "
                        + reservation.getLodgingSizeSqFt()
        );

        System.out.printf(
                "Lodging Price per Night: $%.2f%n",
                reservation.getLodgingPrice()
        );

        if (reservation instanceof CabinReservation cabin) {
            System.out.println(
                    "Full Kitchen Available: "
                            + yesNo(cabin.isFullKitchenAvailable())
            );

            System.out.println(
                    "Loft Available: "
                            + yesNo(cabin.isLoftAvailable())
            );
        } else if (reservation instanceof HotelReservation hotel) {
            System.out.println(
                    "Kitchenette Available: "
                            + yesNo(hotel.hasKitchenette())
            );
        } else if (reservation instanceof HouseReservation house) {
            System.out.println(
                    "Number of Floors: " + house.getNumFloors()
            );
        }
    }

    /**
     * Displays an address with the supplied label.
     *
     * @param label label identifying the address
     * @param address address to display
     */
    private void displayAddress(String label, Address address) {
        System.out.println(
                label + ": "
                        + address.getStreet() + ", "
                        + address.getCity() + ", "
                        + address.getState() + " "
                        + address.getZipCode()
        );
    }

    /**
     * Prompts for address information and validates each field immediately.
     *
     * @return validated address
     */
    private Address readAddress() {
        while (true) {
            String street = readRequiredText("Street: ");
            String city = readRequiredText("City: ");
            String state = readState();
            String zipCode = readZipCode();

            try {
                return new Address(
                        street,
                        city,
                        state,
                        zipCode
                );
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
                System.out.println(
                        "Please re-enter the address."
                );
            }
        }
    }

    /**
     * Reads a nonblank text value.
     *
     * @param prompt prompt displayed to the user
     * @return trimmed nonblank text
     */
    private String readRequiredText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();

            if (!value.isBlank()) {
                return value;
            }

            System.out.println(
                    "Value cannot be blank. Please try again."
            );
        }
    }

    /**
     * Reads and validates a two-letter state abbreviation.
     *
     * @return uppercase state abbreviation
     */
    private String readState() {
        while (true) {
            System.out.print("State: ");
            String value = scanner.nextLine().trim();

            if (value.matches("[A-Za-z]{2}")) {
                return value.toUpperCase();
            }

            System.out.println(
                    "Invalid state. Please enter a 2-letter abbreviation."
            );
        }
    }

    /**
     * Reads and validates a five-digit ZIP code.
     *
     * @return five-digit ZIP code
     */
    private String readZipCode() {
        while (true) {
            System.out.print("ZIP Code: ");
            String value = scanner.nextLine().trim();

            if (value.matches("\\d{5}")) {
                return value;
            }

            System.out.println(
                    "Invalid ZIP Code. Please enter exactly 5 digits."
            );
        }
    }

    /**
     * Reads and validates an account phone number using the Account
     * domain validation rules.
     *
     * @return validated phone number
     */
    private String readPhoneNumber() {
        while (true) {
            System.out.print("Phone Number: ");
            String value = scanner.nextLine().trim();

            if (value.replaceAll("\\D", "").length() == 10) {
                return value;
            }

            System.out.println(
                    "Invalid phone number. Please enter a 10-digit phone number."
            );
        }
    }

    /**
     * Reads and validates an email address.
     *
     * @return validated email address
     */
    private String readEmail() {
        while (true) {
            System.out.print("Email: ");
            String value = scanner.nextLine().trim();

            if (value.matches(
                    "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
            )) {
                return value;
            }

            System.out.println(
                    "Invalid email address. Please try again."
            );
        }
    }

    /**
     * Reads a date until a valid ISO-8601 date is entered.
     *
     * @param prompt prompt displayed to the user
     * @return parsed date
     */
    private LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt);

            try {
                return LocalDate.parse(
                        scanner.nextLine().trim()
                );
            } catch (DateTimeParseException e) {
                System.out.println(
                        "Invalid date. Please use YYYY-MM-DD format."
                );
            }
        }
    }

    /**
     * Reads an integer until a valid value satisfying the supplied
     * validation rule is entered.
     *
     * @param prompt prompt displayed to the user
     * @param validator validation rule
     * @param validationMessage message displayed for an invalid value
     * @return validated integer
     */
    private int readInt(
            String prompt,
            IntValidator validator,
            String validationMessage
    ) {
        while (true) {
            System.out.print(prompt);

            try {
                int value = Integer.parseInt(
                        scanner.nextLine().trim()
                );

                if (validator.isValid(value)) {
                    return value;
                }

                System.out.println(validationMessage);
            } catch (NumberFormatException e) {
                System.out.println(
                        "Invalid number. Please enter a whole number."
                );
            }
        }
    }

    /**
     * Reads a decimal number until a valid value satisfying the supplied
     * validation rule is entered.
     *
     * @param prompt prompt displayed to the user
     * @param validator validation rule
     * @param validationMessage message displayed for an invalid value
     * @return validated decimal value
     */
    private double readDouble(
            String prompt,
            DoubleValidator validator,
            String validationMessage
    ) {
        while (true) {
            System.out.print(prompt);

            try {
                double value = Double.parseDouble(
                        scanner.nextLine().trim()
                );

                if (validator.isValid(value)) {
                    return value;
                }

                System.out.println(validationMessage);
            } catch (NumberFormatException e) {
                System.out.println(
                        "Invalid number. Please enter a numeric value."
                );
            }
        }
    }

    /**
     * Reads a yes/no response until a valid response is entered.
     *
     * @param prompt prompt displayed to the user
     * @return {@code true} for yes; {@code false} for no
     */
    private boolean readBoolean(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();

            if (value.equalsIgnoreCase("y")
                    || value.equalsIgnoreCase("yes")) {
                return true;
            }

            if (value.equalsIgnoreCase("n")
                    || value.equalsIgnoreCase("no")) {
                return false;
            }

            System.out.println(
                    "Invalid response. Please enter y or n."
            );
        }
    }

    /**
     * Converts a boolean value to a user-friendly yes/no value.
     *
     * @param value boolean value
     * @return Yes or No
     */
    private String yesNo(boolean value) {
        return value ? "Yes" : "No";
    }

    /**
     * Defines validation for integer console input.
     */
    @FunctionalInterface
    private interface IntValidator {

        /**
         * Determines whether an integer value is valid.
         *
         * @param value value to validate
         * @return {@code true} when valid
         */
        boolean isValid(int value);
    }

    /**
     * Defines validation for decimal console input.
     */
    @FunctionalInterface
    private interface DoubleValidator {

        /**
         * Determines whether a decimal value is valid.
         *
         * @param value value to validate
         * @return {@code true} when valid
         */
        boolean isValid(double value);
    }
}