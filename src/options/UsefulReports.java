package options;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

import app.MainProgram;
import sql.SQL;

public class UsefulReports {

    /**
     * Print the Useful Reports menu options.
     */
    public static void printOptionsUsefulReports() {
        System.out.println("1. Renting checkouts for a member");
        System.out.println("2. Most popular equipment item");
        System.out.println("3. Most popular manufacturer");
        System.out.println("4. Most used drone");
        System.out.println("5. Member who has rented the most items");
        System.out.println("6. Equipment by type released before a given year");
        System.out.println("Type anything else to return to the main menu");
        System.out.print("Please enter your selection as the number next to the option: ");
    }

    /**
     * Process the user's selection in the Useful Reports menu.
     *
     * @param option the chosen option
     * @param in     scanner for user input
     */
    public static void processUsefulReportsRequest(int option, Scanner in) {
        switch (option) {
            case 1:
                rentingCheckoutsForMember(in);
                break;
            case 2:
                popularItem();
                break;
            case 3:
                popularManufacturer();
                break;
            case 4:
                popularDrone();
                break;
            case 5:
                memberWithMostItems();
                break;
            case 6:
                equipmentByTypeBeforeYear(in);
                break;
            default:
                // anything else just returns to the main menu
                break;
        }
    }

    /**
     * Helper to read an integer, returning -1 on failure.
     */
    private static int parseNumber(Scanner readIn) {
        try {
            int num = Integer.parseInt(readIn.nextLine());
            return num;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * 1) Renting checkouts: total number of equipment items rented by a given member.
     */
    private static void rentingCheckoutsForMember(Scanner in) {
        System.out.print("Please enter the member's email address: ");
        String email = in.nextLine();

        while (email == null || email.trim().isEmpty()) {
            System.out.print("Email cannot be empty. Please enter the member's email address: ");
            email = in.nextLine();
        }

        int customerID = SQL.getCustomerIdFromEmail(email);
        if (customerID <= 0) {
            System.out.println("No customer found with email: " + email);
            return;
        }

        String query =
            "SELECT c.customer_id, c.first_name, c.last_name, c.email, " +
            "       COUNT(r.serial_number) AS total_items_rented " +
            "FROM CUSTOMERS c " +
            "LEFT JOIN RENTALS r ON c.customer_id = r.customer_id " +
            "WHERE c.customer_id = ? " +
            "GROUP BY c.customer_id, c.first_name, c.last_name, c.email;";

        try {
            PreparedStatement ps = MainProgram.dbConnection.prepareStatement(query);
            ps.setInt(1, customerID);
            SQL.sqlQuery(MainProgram.dbConnection, ps);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (NullPointerException e) {
        }
    }

    /**
     * 2) Popular item: find the most popular equipment item in the database.
     */
    private static void popularItem() {
        String query =
            "SELECT e.serial_number, em.description, em.type, " +
            "       COUNT(r.rental_number) AS times_rented " +
            "FROM EQUIPMENT e " +
            "JOIN RENTALS r ON e.serial_number = r.serial_number " +
            "JOIN EQUIPMENT_MODEL em ON e.e_model_id = em.e_model_id " +
            "GROUP BY e.serial_number, em.description, em.type " +
            "ORDER BY times_rented DESC " +
            "LIMIT 1;";

        try {
            PreparedStatement ps = MainProgram.dbConnection.prepareStatement(query);
            SQL.sqlQuery(MainProgram.dbConnection, ps);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (NullPointerException e) {
        }
    }

    /**
     * 3) Popular manufacturer: manufacturer whose equipment has been rented the most.
     */
    private static void popularManufacturer() {
        String query =
            "SELECT em.manufacturer, COUNT(r.rental_number) AS total_rented_units " +
            "FROM RENTALS r " +
            "JOIN EQUIPMENT e ON r.serial_number = e.serial_number " +
            "JOIN EQUIPMENT_MODEL em ON e.e_model_id = em.e_model_id " +
            "GROUP BY em.manufacturer " +
            "ORDER BY total_rented_units DESC " +
            "LIMIT 1;";

        try {
            PreparedStatement ps = MainProgram.dbConnection.prepareStatement(query);
            SQL.sqlQuery(MainProgram.dbConnection, ps);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (NullPointerException e) {
        }
    }

    /**
     * 4) Popular drone: find the drone that has travelled the greatest total member distance
     */
    private static void popularDrone() {
        String query =
            "SELECT d.d_serial_number, " +
            "       COUNT(dt.rental_number) AS num_deliveries, " +
            "       SUM(CAST(c.warehouse_distance AS REAL)) AS total_member_distance " +
            "FROM DRONES d " +
            "JOIN DELIVER_TO dt ON d.d_serial_number = dt.d_serial_number " +
            "JOIN CUSTOMERS c ON dt.customer_id = c.customer_id " +
            "GROUP BY d.d_serial_number " +
            "ORDER BY total_member_distance DESC, num_deliveries DESC " +
            "LIMIT 1;";

        try {
            PreparedStatement ps = MainProgram.dbConnection.prepareStatement(query);
            SQL.sqlQuery(MainProgram.dbConnection, ps);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (NullPointerException e) {
        }
    }

    /**
     * 5) Items checked out: member who has rented out the most items.
     */
    private static void memberWithMostItems() {
        String query =
            "SELECT c.customer_id, c.first_name, c.last_name, c.email, " +
            "       COUNT(r.serial_number) AS total_items_rented " +
            "FROM CUSTOMERS c " +
            "JOIN RENTALS r ON c.customer_id = r.customer_id " +
            "GROUP BY c.customer_id, c.first_name, c.last_name, c.email " +
            "ORDER BY total_items_rented DESC " +
            "LIMIT 1;";

        try {
            PreparedStatement ps = MainProgram.dbConnection.prepareStatement(query);
            SQL.sqlQuery(MainProgram.dbConnection, ps);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (NullPointerException e) {
        }
    }

    /**
     * 6) Equipment by Type: find equipment descriptions by type released before a given year.
     */
    private static void equipmentByTypeBeforeYear(Scanner in) {
        System.out.print("Please enter the equipment type (e.g., conveyor, forklift): ");
        String type = in.nextLine();
        while (type == null || type.trim().isEmpty()) {
            System.out.print("Type cannot be empty. Please enter the equipment type: ");
            type = in.nextLine();
        }

        System.out.print("Please enter the year (only equipment released BEFORE this year will be shown): ");
        int year = parseNumber(in);
        while (year <= 0) {
            System.out.print("Invalid year. Please enter a valid year (e.g., 2020): ");
            year = parseNumber(in);
        }

        String query =
            "SELECT em.description, em.type, em.year " +
            "FROM EQUIPMENT_MODEL em " +
            "WHERE em.type = ? AND em.year < ? " +
            "ORDER BY em.year ASC;";

        try {
            PreparedStatement ps = MainProgram.dbConnection.prepareStatement(query);
            ps.setString(1, type);
            ps.setInt(2, year);
            SQL.sqlQuery(MainProgram.dbConnection, ps);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (NullPointerException e) {
        }
    }
}
