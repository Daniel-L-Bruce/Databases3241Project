import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Project code.
 *
 * @author Daniel Bruce
 */
public final class MainProgram {

    /**
     * No argument constructor--private to prevent instantiation.
     */
    private MainProgram() {
        // no code needed here
    }
    /*
     * Path to the database file
     */
    private static final String DATABASE = "ProjectDatabaseUpdated.db";

    /**
     * This variable is used as a key for which string means what in the
     * customer array value of the map.
     */
    public static final String[] CUSTOMER_ORDER_KEY = { "Customer ID", "First Name",
            "Last Name", "Address", "Phone", "Email", "Start Date",
            "Warehouse Distance" };

    /**
     * This variable is used as a key for which string means what in the
     * warehouse array value of the map.
     */
    public static final String[] WAREHOUSE_ORDER_KEY = { "Warehouse Id", "City",
            "Address", "Phone", "Manager Name", "Storage Capacity", "Drone Capacity" };

    /**
     * This function simply prints out the options available to the user.
     */
    public static void printOptions() {
        System.out.println("1. Customers");
        System.out.println("2. Drones");
        System.out.println("3. Equipment");
        System.out.println("4. Orders");
        System.out.println("5. Reviews");
        System.out.println("6. Warehouses");
        System.out.println("7. Useful Reports");
        System.out.println("Type anything else to terminate program");
        System.out
                .print("Please enter your selection as the number next to the option: ");
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
     * This function simply prints out the options for the customer available to
     * the user.
     */
    public static void printOptionsCustomer() {
        System.out.println("1. Add new customer");
        System.out.println("2. Edit Customer");
        System.out.println("3. Search Customer");
        System.out.println("4. Delete Customer");
        System.out.println("Type anything else to terminate program");
        System.out
                .print("Please enter your selection as the number next to the option: ");
    }

    /**
     * This function simply prints out the options for the warehouse available
     * to the user.
     */
    public static void printOptionsWarehouse() {
        System.out.println("1. Add new Warehouse");
        System.out.println("2. Edit Warehouse");
        System.out.println("3. Search Warehouse");
        System.out.println("4. Delete Warehouse");
        System.out.println("Type anything else to terminate program");
        System.out
                .print("Please enter your selection as the number next to the option: ");
    }

    /**
     * This function will read in the option entered, return it if valid, or -1
     * if not.
     *
     * @param readIn
     *            Scanner object where to read in the value
     * @param numberOfOptions
     *            Integer with the number of options, allowing for the reuse of
     *            this function for different option questions
     * @return The option read in if valid, -1 if not
     */
    public static int parseOption(Scanner readIn, int numberOfOptions) {
        // If the option read in is not one of the 5, return negative one
        try {
            int option = Integer.parseInt(readIn.nextLine());
            if (option <= numberOfOptions && option >= 1) {
                return option;
            } else {
                return -1;
            }
        } catch (NumberFormatException e) {
            return -1;
        }
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
                System.out.print(
                        "Please enter the serial number of the peice of equipment you wish to rent: ");
                // Grab the number entered as the serial number
                equipmentSerialNumber = parseNumber(readIn);
                System.out.println("You have selected to rent: " + equipmentSerialNumber);
                // More to come in future.
                break;
            case 2:
                // This option is for return equipment;
                System.out.print(
                        "Please enter the serial number of the peice of equipment you wish to return: ");
                // Grab the number entered as the serial number
                equipmentSerialNumber = parseNumber(readIn);
                System.out
                        .println("You have selected to return: " + equipmentSerialNumber);
                // More to come in future.
                break;
            case 3:
                // This option is for delivery of equipment;
                System.out.print(
                        "Please enter the serial number of the peice of equipment you wish to get delivered: ");
                // Grab the number entered as the serial number
                equipmentSerialNumber = parseNumber(readIn);
                System.out.print(
                        "Please enter the serial number of the drone you wish to do the delivery: ");
                // Grab the number entered as the serial number
                droneSerialNumber = parseNumber(readIn);
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
                equipmentSerialNumber = parseNumber(readIn);
                System.out.print(
                        "Please enter the serial number of the drone you wish to do the pickup the equipment: ");
                // Grab the number entered as the serial number
                droneSerialNumber = parseNumber(readIn);
                System.out.println("You have selected to pickup the equipment: "
                        + equipmentSerialNumber);
                System.out.println("You have selected to have this drone pickup: "
                        + droneSerialNumber);
                break;
            default:
                // Do not do anything
        }
    }

    /**
     * This Method processes the users request for the customer category.
     *
     * @param option
     *            This is the option the user selected
     * @param readIn
     *            This is an input stream
     * @param map
     *            This is the map with all the user data for customer
     */
    public static void processCustomerRequest(int option, Scanner readIn,
            Map<String, String[]> map) {
        switch (option) {
            case 1:
                // This is for adding a customer
                // Create a values string for the values to be entered
                String[] values = new String[CUSTOMER_ORDER_KEY.length];
                for (int i = 0; i < values.length; i++) {
                    System.out.print(
                            "Please enter the value for " + CUSTOMER_ORDER_KEY[i] + ":");
                    values[i] = readIn.nextLine();
                }
                String key = values[0];
                if (!map.containsKey(key)) {
                    map.put(key, values);
                }
                break;
            case 2:
                // This option is for Editing a customers information;
                System.out.print(
                        "Please enter the customer ID for which you would like to edit: ");
                String editKey = readIn.nextLine();
                // If the key is not contained, alert user, and break
                if (!map.containsKey(editKey)) {
                    System.out.println("No customer has the id: " + editKey);
                    break;
                }
                System.out.println(
                        "Avaiable fields: " + Arrays.toString(CUSTOMER_ORDER_KEY));
                System.out.print(
                        "Please enter the field for which you would like to edit: ");
                String editField = readIn.nextLine();
                // Find what
                int holder = 0;
                while (holder < CUSTOMER_ORDER_KEY.length
                        && !editField.equals(CUSTOMER_ORDER_KEY[holder])) {
                    holder++;
                }
                // If holder == the length of the example array, the field entered
                // is not one of the option, alert the user and exit
                if (holder == CUSTOMER_ORDER_KEY.length) {
                    System.out.println("The field entered does not exist: " + editField);
                    break;
                }
                System.out.print("Please enter the new value: ");
                String newValue = readIn.nextLine();
                String[] editValues = map.get(editKey);
                editValues[holder] = newValue;
                System.out.println("Value updated");
                break;
            case 3:
                // This function simply searches the values and returns the tuple related
                System.out.print(
                        "Please enter the customer ID for which you would like to search: ");
                String searchKey = readIn.nextLine();
                // If the key is not contained, alert user, and break
                if (!map.containsKey(searchKey)) {
                    System.out.println("No customer has the id: " + searchKey);
                    break;
                } else {
                    System.out.println("The values associated are: "
                            + Arrays.toString(map.get(searchKey)));
                }

                break;
            case 4:
                // This function simply searches removes the tuple being deleted
                System.out.print(
                        "Please enter the customer ID for which you would like to delete: ");
                String removeKey = readIn.nextLine();
                // If the key is not contained, alert user, and break
                if (!map.containsKey(removeKey)) {
                    System.out.println("No customer has the id: " + removeKey);
                    break;
                } else {
                    map.remove(removeKey);
                }

                break;
            default:
                // Do not do anything
        }
    }

    /**
     * This Method processes the users request for the Warehouse category.
     *
     * @param option
     *            This is the option the user selected
     * @param readIn
     *            This is an input stream
     * @param map
     *            This is the map with all the user data for Warehouse
     */
    public static void processWarehouseRequest(int option, Scanner readIn,
            Map<String, String[]> map) {
        switch (option) {
            case 1:
                // This is for adding a customer
                // Create a values string for the values to be entered
                String[] values = new String[WAREHOUSE_ORDER_KEY.length];
                for (int i = 0; i < values.length; i++) {
                    System.out.print(
                            "Please enter the value for " + WAREHOUSE_ORDER_KEY[i] + ":");
                    values[i] = readIn.nextLine();
                }
                String key = values[0];
                if (!map.containsKey(key)) {
                    map.put(key, values);
                }
                break;
            case 2:
                // This option is for Editing a customers information;
                System.out.print(
                        "Please enter the Warehouse ID for which you would like to edit: ");
                String editKey = readIn.nextLine();
                // If the key is not contained, alert user, and break
                if (!map.containsKey(editKey)) {
                    System.out.println("No Warehouse has the id: " + editKey);
                    break;
                }
                System.out.println(
                        "Avaiable fields: " + Arrays.toString(WAREHOUSE_ORDER_KEY));
                System.out.print(
                        "Please enter the field for which you would like to edit: ");
                String editField = readIn.nextLine();
                // Find what
                int holder = 0;
                while (holder < WAREHOUSE_ORDER_KEY.length
                        && !editField.equals(WAREHOUSE_ORDER_KEY[holder])) {
                    holder++;
                }
                // If holder == the length of the example array, the field entered
                // is not one of the option, alert the user and exit
                if (holder == WAREHOUSE_ORDER_KEY.length) {
                    System.out.println("The field entered does not exist: " + editField);
                    break;
                }
                System.out.print("Please enter the new value: ");
                String newValue = readIn.nextLine();
                String[] editValues = map.get(editKey);
                editValues[holder] = newValue;
                System.out.println("Value updated");
                break;
            case 3:
                // This function simply searches the values and returns the tuple related
                System.out.print(
                        "Please enter the Warehouse ID for which you would like to search: ");
                String searchKey = readIn.nextLine();
                // If the key is not contained, alert user, and break
                if (!map.containsKey(searchKey)) {
                    System.out.println("No Warehouse has the id: " + searchKey);
                    break;
                } else {
                    System.out.println("The values associated are: "
                            + Arrays.toString(map.get(searchKey)));
                }

                break;
            case 4:
                // This function simply removes the tuple being deleted
                System.out.print(
                        "Please enter the Warehouse ID for which you would like to delete: ");
                String removeKey = readIn.nextLine();
                // If the key is not contained, alert user, and break
                if (!map.containsKey(removeKey)) {
                    System.out.println("No Warehouse has the id: " + removeKey);
                    break;
                } else {
                    map.remove(removeKey);
                }

                break;
            default:
                // Do not do anything
        }
    }

    /**
     * Taken from the in-class Lab 3 example.
     * 
     * Connects to the database if it exists, creates it if it does not, and returns the connection object.
     * 
     * @param databaseFileName the database file name
     * @return a connection object to the designated database
     */
    public static Connection initializeDB(String databaseFileName) {

        String url = "jdbc:sqlite:" + databaseFileName;

        Connection conn = null; // If you create this variable inside the Try block it will be out of scope
        try {
            conn = DriverManager.getConnection(url);
            if (conn != null) {
            	// Provides some positive assurance the connection and/or creation was successful.
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("The connection to the database was successful.");
            } else {
            	// Provides some feedback in case the connection failed but did not throw an exception.
            	System.out.println("Null Connection");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("There was a problem connecting to the database.");
        }
        return conn;
    }
    /*
     * The Method that prints the main menu
     * 
     * @param cin Scanner object for input
     * 
     */

    public static void printMainMenu(Scanner cin) {
        // Create all necessary maps so far
        Map<String, String[]> customers = new HashMap<>();
        Map<String, String[]> warehouse = new HashMap<>();

        // Welcome the user
        System.out.println("Hello! Welcome to the Database Application");
        int option = 0;
        while (option >= 0) {
            printOptions();
            // Call parse options with 5 as the number of options for the main prompt
            option = parseOption(cin, 7);
            if (option > 0) {
                switch (option) {
                    case 1:
                        // This option is for Customers
                        printOptionsCustomer();
                        int optionCustomer = parseOption(cin, 4);
                        processCustomerRequest(optionCustomer, cin, customers);
                        break;
                    case 2:
                        // This option is for Drones
                        break;
                    case 3:
                        // This option is for Equipment
                        printOptionsEquipment();
                        int optionEquipment = parseOption(cin, 4);
                        processEquipmentRequest(optionEquipment, cin);
                        break;
                    case 4:
                        // This option is for Orders
                        break;
                    case 5:
                        // This option is for Reviews
                        break;
                    case 6:
                        // This option is for Warehouse
                        printOptionsWarehouse();
                        int optionWarehouse = parseOption(cin, 4);
                        processWarehouseRequest(optionWarehouse, cin, warehouse);
                        break;
                    case 7:
                        // This option is for Useful Reports
                        break;
                    default:
                        // Do not do anything, just exit the program.
                }

            }
        }
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments; unused here
     */
    public static void main(String[] args) {
        Connection dbConnection = initializeDB(DATABASE);

        Scanner cin = new Scanner(System.in);
        printMainMenu(cin);
        cin.close();

        try {
			dbConnection.close();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}

        System.out.println("Bye");
    }
}
