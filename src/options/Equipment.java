package options;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import app.MainProgram;

import sql.SQL;

public class Equipment {

    

    /**
     * Checks if the input string contains suspicious SQL injection characters/patterns.
     *
     * @param input The string to check
     * @return true if suspicious content is found, false otherwise
     */
    public static boolean containsSqlInjectionRisk(String input) {
        // List of suspicious substrings often used in SQL injection
        List<String> sqlInjectionList = Arrays.asList(
        "--", ";", "'", "\"", "/*", "*/",
        "drop", "delete", "insert", "update", "select",
        "exec", "execute", "alter", "create", "shutdown"
        );
        if (input == null) return false;

        String lowerInput = input.toLowerCase();
        for (String pattern : sqlInjectionList) {
            if (lowerInput.contains(pattern)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Prompt the user for a date string in MM/DD/YYYY format.
     * @param scanner
     * @return 
     *          the date string
     */
    public static String promptForDateString(Scanner scanner) {
        String date = null;
        // Create regex for date validation to ensure MM/DD/YYYY format with valid month/day
        String regex = "^(0[1-9]|1[0-2])/([0-2][0-9]|3[01])/\\d{4}$";

        while (date == null) {
            System.out.print("Please enter the date MM/DD/YYYY: ");
            String input = scanner.nextLine();
        // Validate input against regex until valid date is entered
            if (input.matches(regex)) {
                date = input;
            } else {
                System.out.println("Invalid date format. Please try again.");
            }
        }

        return date;
    }
        
    /**
     * This function will read in the integer entered, return it if valid, or -1
     * if not.
     *
     * @param readIn
     *            Scanner object where to read in the value
     * @return The integer read in if valid, -1 if not
     */
    public static int parseNumber(Scanner readIn) {
        // If the option read in is not able to be an int, return negative one
        try {
            int num = Integer.parseInt(readIn.nextLine());
            return num;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * This function simply prints out the options for the equipment available
     * to the user.
     */
    public static void printOptionsEquipment() {
        System.out.println("1. Rent Equipment");
        System.out.println("2. Return Equipment");
        System.out.println("3. Delivery of Equipment");
        System.out.println("4. Pickup of Equipment");
        System.out.println("Type anything else to terminate program");
        System.out
                .print("Please enter your selection as the number next to the option: ");
    }

     /**
     * This function processes the option chosen by user for equipment.
     *
     * @param option
     *            The option chosen by the user
     * @param readIn
     *            Scanner object where to read in the value
     */
    public static void processEquipmentRequest(int option, Scanner readIn) {
        int equipmentSerialNumber = 0;
        int droneSerialNumber = 0;
        switch (option) {
            case 1:
                // This option is for renting equipment;
                createRental(readIn);
                break;
            case 2:
                // This option is for return equipment;
                registerReturn(readIn);
                break;
            case 3:
                // This option is for delivery of equipment;
                scheduleDelivery(readIn);
                break;
            case 4:
                // This option is for pickup of equipment;
                schedulePickup(readIn);
                break;
            default:
                // Do not do anything
        }
    }

    /**
     * Create a rental record in the database by asking the user for correct information.
     * @param in 
     */
    public static void createRental(Scanner in) {
        // Get information for the new order by prompting user
        System.out.println("Please enter the serial number of the equipment: ");
        String equipmentSerialNumber = in.nextLine();
        while (!SQL.isInEquipment(equipmentSerialNumber) || containsSqlInjectionRisk(equipmentSerialNumber)) {
            System.out.println(
                    "The serial number you entered does not exist in the equipment table. Please enter a valid serial number: ");
            equipmentSerialNumber = in.nextLine();
        }
        // Ask user for email and then find the customer ID with that email
        System.out.println("Please enter your customer registered email: ");
        String email = in.nextLine();
        while (!sql.SQL.isEmailInCustomer(email) || containsSqlInjectionRisk(email)) {
            System.out
                    .println("The quantity must be at least 1. Please enter a valid quantity: ");
            email = in.nextLine();
        }
        int customerID = SQL.getCustomerIdFromEmail(email);
        // Ask user for the date
        String date = promptForDateString(in);
        // Create the order in the database
        String[] orderData = {date, Integer.toString(customerID), equipmentSerialNumber};
        String query = "INSERT INTO RENTALS (date_rented, customer_id, serial_number) VALUES (?, ?, ?)";
        SQL.ps_CreateRental(query, orderData);
        System.out.println("Rental created successfully! Please schedule a delivery for the equipment.");
    }


    /**
     * Register the return of a rental in the database by asking the user for correct information.
     * @param in 
     */
    public static void registerReturn(Scanner in) {
        // Get information for the return by prompting user
        System.out.println("Please enter the Rental Number of the equipment being returned: ");
        int rentalNumber = parseNumber(in);
        while (rentalNumber < 0 || !SQL.isInRental(rentalNumber)) {
            System.out.println(
                    "The rental number you entered does not exist in the rentals table. Please enter a valid rental number: ");
            rentalNumber = parseNumber(in);
        }
        // Ask user for the date
        String returnDate = promptForDateString(in);
        // Register the return in the database
        String query = "UPDATE RENTALS SET date_returned = ? WHERE rental_number = ?";
        SQL.ps_RegisterReturn(query, returnDate, rentalNumber);
        System.out.println("Return registered successfully! Please schedule a pickup for the equipment.");
    }

    /**
     * Schedule a delivery for a rental in the database by asking the user for correct information.
     * @param in
     */
    public static void scheduleDelivery(Scanner in) {
        // Get information for the delivery by prompting user
        System.out.println("Please enter the Rental Number of the equipment being delivered: ");
        int rentalNumber = parseNumber(in);
        while (rentalNumber < 0 || !SQL.isInRental(rentalNumber)) {
            System.out.println(
                    "The rental number you entered does not exist in the rentals table. Please enter a valid rental number: ");
            rentalNumber = parseNumber(in);
        }
        // Ask the user for the drone serial number they wish to complete the delivery
        System.out.println("Please enter the serial number of the drone to deliver the equipment: ");
        String droneSerialNumber = in.nextLine();
        while (!SQL.isInDrone(droneSerialNumber) || containsSqlInjectionRisk(droneSerialNumber)) {
            System.out.println(
                    "The serial number you entered does not exist in the drone table. Please enter a valid serial number: ");
            droneSerialNumber = in.nextLine();
        }
        // Grab the customer ID from the rental number
        int customerID = SQL.getCustomerIdFromRental(rentalNumber);
        // Schedule the delivery in the database
        String SQL = "INSERT INTO DELIVER_TO VALUES (?, ?, ?);";
        sql.SQL.ps_ScheduleDelivery(SQL, droneSerialNumber, customerID, rentalNumber);
        System.out.println("Delivery scheduled successfully!");
    }


    public static void schedulePickup(Scanner in) {
        // Get information for the pickeup by prompting user
        System.out.println("Please enter the Rental Number of the equipment being picked up: ");
        int rentalNumber = parseNumber(in);
        while (rentalNumber < 0 || !SQL.isInRental(rentalNumber)) {
            System.out.println(
                    "The rental number you entered does not exist in the rentals table. Please enter a valid rental number: ");
            rentalNumber = parseNumber(in);
        }
        // Ask the user for the drone serial number they wish to complete the delivery
        System.out.println("Please enter the serial number of the drone to pick up the equipment: ");
        String droneSerialNumber = in.nextLine();
        while (!SQL.isInDrone(droneSerialNumber) || containsSqlInjectionRisk(droneSerialNumber)) {
            System.out.println(
                    "The serial number you entered does not exist in the drone table. Please enter a valid serial number: ");
            droneSerialNumber = in.nextLine();
        }
        // Grab the customer ID from the rental number
        int customerID = SQL.getCustomerIdFromRental(rentalNumber);
        // Schedule the delivery in the database
        String SQL = "INSERT INTO PICKUP_RENTAL VALUES (?, ?, ?);";
        sql.SQL.ps_ScheduleDelivery(SQL, droneSerialNumber, customerID, rentalNumber);
        System.out.println("Pickup scheduled successfully!");
    }
}


