package app;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import options.*;

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

    public static Connection dbConnection = null;

    /*
     * Path to the database file
     */
    private static final String DATABASE = "RentalSystem.db";

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
            System.out.println("Connecting to DB file: " 
    + new java.io.File(databaseFileName).getAbsolutePath());

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
                        Customer.printOptionsCustomer();
                        int optionCustomer = parseOption(cin, 4);
                        Customer.processCustomerRequest(optionCustomer, cin, customers);
                        break;
                    case 2:
                        // This option is for Drones
                        break;
                    case 3:
                        // This option is for Equipment
                        Equipment.printOptionsEquipment();
                        int optionEquipment = parseOption(cin, 4);
                        Equipment.processEquipmentRequest(optionEquipment, cin);
                        break;
                    case 4:
                        // This option is for Orders
                        break;
                    case 5:
                        // This option is for Reviews
                        break;
                    case 6:
                        // This option is for Warehouse
                        Warehouse.printOptionsWarehouse();
                        int optionWarehouse = parseOption(cin, 4);
                        Warehouse.processWarehouseRequest(optionWarehouse, cin, warehouse);
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
        dbConnection = initializeDB(DATABASE);

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
