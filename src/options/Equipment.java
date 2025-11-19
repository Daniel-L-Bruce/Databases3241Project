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
        // If the option read in is not one of the 5, return negative one
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
                createOrder(readIn);
                break;
            case 2:
                // This option is for return equipment;
                System.out.print(
                        "Please enter the serial number of the peice of equipment you wish to return: ");
                // Grab the number entered as the serial number
                equipmentSerialNumber = MainProgram.parseNumber(readIn);
                System.out
                        .println("You have selected to return: " + equipmentSerialNumber);
                // More to come in future.
                break;
            case 3:
                // This option is for delivery of equipment;
                System.out.print(
                        "Please enter the serial number of the peice of equipment you wish to get delivered: ");
                // Grab the number entered as the serial number
                equipmentSerialNumber = MainProgram.parseNumber(readIn);
                System.out.print(
                        "Please enter the serial number of the drone you wish to do the delivery: ");
                // Grab the number entered as the serial number
                droneSerialNumber = MainProgram.parseNumber(readIn);
                System.out.println("You have selected to deliver the equipment: "
                        + equipmentSerialNumber);
                System.out.println("You have selected to have this drone deliver: "
                        + droneSerialNumber);
                // More to come in future.
                break;
            case 4:
                // This option is for pickup of equipment;
                System.out.print(
                        "Please enter the serial number of the peice of equipment you wish to get picked up: ");
                // Grab the number entered as the serial number
                equipmentSerialNumber = MainProgram.parseNumber(readIn);
                System.out.print(
                        "Please enter the serial number of the drone you wish to do the pickup the equipment: ");
                // Grab the number entered as the serial number
                droneSerialNumber = MainProgram.parseNumber(readIn);
                System.out.println("You have selected to pickup the equipment: "
                        + equipmentSerialNumber);
                System.out.println("You have selected to have this drone pickup: "
                        + droneSerialNumber);
                break;
            default:
                // Do not do anything
        }
    }

    public static void createOrder(Scanner in) {
        // Get information for the new order by prompting user
        System.out.println("Please enter the serial number of the equipment: ");
        String equipmentSerialNumber = in.nextLine();
        while (!SQL.isInEquipment(equipmentSerialNumber) || containsSqlInjectionRisk(equipmentSerialNumber)) {
            System.out.println(
                    "The serial number you entered does not exist in the equipment table. Please enter a valid serial number: ");
            equipmentSerialNumber = in.nextLine();
        }
        System.out.println("Please enter the quantity: ");
        int quantity = parseNumber(in);
        while (quantity < 1) {
            System.out
                    .println("The quantity must be at least 1. Please enter a valid quantity: ");
            quantity = parseNumber(in);
        }
        String date = promptForDateString(in);
        System.out.println("When do you need the order delivered by?");
        String deliveryDate = promptForDateString(in);
        // Grab the equipment type and warehouse ID for the order from the database for the order
        String type = SQL.getEquipmentType(equipmentSerialNumber);
        int warehouseID = SQL.getWarehouseIDForEquipment(equipmentSerialNumber);
        // Create the order in the database
        String[] orderData = { type, Integer.toString(quantity), deliveryDate, date, equipmentSerialNumber, Integer.toString(warehouseID) };
        String query = "INSERT INTO orders (element_type, quantity, estimated_arrival_date, date, serial_number, warehouse_id) VALUES (?, ?, ?, ?, ?, ?)";
        SQL.ps_CreateOrder(query, orderData);
    }
}


