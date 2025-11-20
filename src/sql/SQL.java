package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import app.MainProgram;

/**
 * 
 * All database connectivity should be handled from within here.
 *
 */
public class SQL {
	
	private static PreparedStatement ps;

    /**
     * Queries the database and prints the results.
     * 
     * @param conn a connection object
     * @param sql a SQL statement that returns rows:
     * 		this query is written with the PrepareStatement class, typically 
     * 		used for dynamic SQL SELECT statements.
     */
    public static void sqlQuery(Connection conn, PreparedStatement sql){
        try {
        	ResultSet rs = sql.executeQuery();
        	ResultSetMetaData rsmd = rs.getMetaData();
        	int columnCount = rsmd.getColumnCount();
        	for (int i = 1; i <= columnCount; i++) {
        		String value = rsmd.getColumnName(i);
        		System.out.print(value);
        		if (i < columnCount) System.out.print(",  ");
        	}
			System.out.print("\n");
        	while (rs.next()) {
        		for (int i = 1; i <= columnCount; i++) {
        			String columnValue = rs.getString(i);
            		System.out.print(columnValue);
            		if (i < columnCount) System.out.print(",  ");
        		}
    			System.out.print("\n");
        	}
        	rs.close();
        	ps.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     *Create PreparedStatement to search a custonmer by customerID.
     * 
     * @param sql query for prepared statement
     * 
     * @param customerID customer ID to search by 
     */
    public static void ps_SearchCustomer(String sql, int customerID){
    	try {
    		ps = MainProgram.dbConnection.prepareStatement(sql);
    		ps.setInt(1, customerID);
    	} catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    	sqlQuery(MainProgram.dbConnection, ps);
    }

    /**
     *Create PreparedStatement to insert a new custonmer.
     * 
     * @param sql query for prepared statement
     * 
     * @param customerData array of customer data to insert
     */
    public static void ps_AddCustomer(String sql, String [] customerData){
    	try {
    		ps = MainProgram.dbConnection.prepareStatement(sql);
    		for (int i = 1; i <= customerData.length; i++) {
                if(i == 8) {
                    ps.setInt(i, Integer.parseInt(customerData[i-1]));
                    continue;
                }
                //tried the autoincrement and it generated a vlaue for customerid but it wasn't correct
    			ps.setString(i, customerData[i-1]);
    		}

            ps.executeUpdate();
            System.out.println("Customer added successfully.");
            ps.close();

    	} catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     *Create PreparedStatement to delete a custonmer.
     * 
     * @param sql query for prepared statement
     * 
     * @param customerID ID of customer to delete
     */
    public static void ps_RemoveCustomer(String sql, int customerID){
    	try {
    		ps = MainProgram.dbConnection.prepareStatement(sql);
    		ps.setInt(1, customerID);

            ps.executeUpdate();
            System.out.println("Customer deleted successfully.");
            ps.close();

    	} catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     *Create PreparedStatement to edit a custonmer.
     * 
     * @param sql query for prepared statement
     * 
     * @param customerID ID of customer to delete
     */
    public static void ps_EditCustomer(String sql, int customerID, String [] newValues){
    	try {
    		ps = MainProgram.dbConnection.prepareStatement(sql);
    		for (int i = 1; i <= newValues.length; i++) {
                System.out.print("Setting parameter " + i + " to value " + newValues[i - 1] + "\n");
                ps.setString(i, newValues[i - 1]);
            }
            
            ps.setInt(newValues.length + 1, customerID);

            System.out.print(sql);
            ps.executeUpdate();
            System.out.println("Customer updated successfully.");
            ps.close();

    	} catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

        /**
     * Create PreparedStatement to insert a new order.
     * 
     * @param sql query for prepared statement
     * 
     * @param orderData array of Order data to insert
     */
    public static void ps_CreateOrder(String sql, String [] orderData){
    	try {
    		ps = MainProgram.dbConnection.prepareStatement(sql);
    		for (int i = 1; i <= orderData.length; i++) {
                if(i == 2 || i == 6) {
                    ps.setInt(i, Integer.parseInt(orderData[i-1]));
                    continue;
                }
                //tried the autoincrement and it generated a vlaue for customerid but it wasn't correct
    			ps.setString(i, orderData[i-1]);
    		}

            ps.executeUpdate();
            System.out.println("Order added successfully.");
            ps.close();

    	} catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * Check if equipment with given serial number exists in the database.
     * @param serialNumber
     * @return True if equipment exists, false otherwise
     */
    public static boolean isInEquipment(String serialNumber) {
        // SQL query to check for existence
        String sql = "SELECT COUNT(*) FROM equipment WHERE serial_number = ?;";
        boolean exists = false;
        try  {
            // Grab database connection from the main program
            Connection conn = MainProgram.dbConnection;
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, serialNumber);
            // After preparing the statement, execute the query
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    if (count > 0) {
                        exists = true;
                    } 
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }

    /**
     * Check if drone with given serial number exists in the database.
     * @param serialNumber
     * @return True if drone exists, false otherwise
     */
    public static boolean isInDrone(String serialNumber) {
        // SQL query to check for existence
        String sql = "SELECT COUNT(*) FROM DRONES WHERE d_serial_number = ?;";
        boolean exists = false;
        try  {
            // Grab database connection from the main program
            Connection conn = MainProgram.dbConnection;
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, serialNumber);
            // After preparing the statement, execute the query
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    if (count > 0) {
                        exists = true;
                    } 
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }

    /**
     * Check if email exists in the customer database.
     * @param email
     * @return True if equipment exists, false otherwise
     */
    public static boolean isEmailInCustomer(String email) {
        // SQL query to check for existence
        String sql = "SELECT COUNT(*) FROM CUSTOMERS WHERE email = ?;";
        boolean exists = false;
        try  {
            // Grab database connection from the main program
            Connection conn = MainProgram.dbConnection;
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            // After preparing the statement, execute the query
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    if (count > 0) {
                        exists = true;
                    } 
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }

    /**
     * Check if rental with given rental number exists in the database.
     * @param rentalNumber
     * @return
     */
    public static boolean isInRental(int rentalNumber) {
        // SQL query to check for existence
        String sql = "SELECT COUNT(*) FROM RENTALS WHERE rental_number = ?;";
        boolean exists = false;
        try  {
            // Grab database connection from the main program
            Connection conn = MainProgram.dbConnection;
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, rentalNumber);
            // After preparing the statement, execute the query
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    if (count > 0) {
                        exists = true;
                    } 
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }


    /**
     * Get the equipment type for the equipment with the given serial number.
     * @param serialNumber
     * @return equipment type, or null if not found
     */
    public static String getEquipmentType(String serialNumber) {
        String sql = "SELECT type FROM EQUIPMENT AS E, EQUIPMENT_MODEL AS EM WHERE E.serial_number = ? AND E.e_model_id = EM.e_model_id;";
        String equipmentType = null;
        try  {
            // Grab database connection from the main program
            Connection conn = MainProgram.dbConnection;
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, serialNumber);
            // After preparing the statement, execute the query
            try (ResultSet rs = ps.executeQuery()) {
                // Retrieve the equipment type from the result set
                if (rs.next()) {
                    equipmentType = rs.getString("type");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return equipmentType;
    }

    /**
     * Get the warehouse ID that stores the equipment with the given serial number.
     * @param serialNumber
     * @return warehouse ID, or 0 if not found
     */
    public static int getWarehouseIDForEquipment(String serialNumber) {
    String sql = "SELECT warehouse_id FROM STORES WHERE serial_number = ?;";
    int warehouseID = 0;
    try  {
        // Grab database connection from the main program
        Connection conn = MainProgram.dbConnection;
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, serialNumber);
        // After preparing the statement, execute the query
        try (ResultSet rs = ps.executeQuery()) {
            // Retrieve the warehouse ID from the result set
            if (rs.next()) {
                warehouseID = rs.getInt("warehouse_id");
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return warehouseID;
}

/**
 * Get the customer ID associated with the given email.
 * @param email
 * @return customer ID, or -1 if not found
 */
public static int getCustomerIdFromEmail(String email) {
        String sql = "SELECT customer_id FROM CUSTOMERS WHERE email = ?;";
        int customerID = -1;
        try  {
            // Grab database connection from the main program
            Connection conn = MainProgram.dbConnection;
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            // After preparing the statement, execute the query
            try (ResultSet rs = ps.executeQuery()) {
                // Retrieve the equipment type from the result set
                if (rs.next()) {
                    customerID = rs.getInt("customer_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerID;
    }

    /**
     * Get the customer ID associated with the given rental number.
     * @param rentalNumber
     * @return customer ID, or -1 if not found
     */
    public static int getCustomerIdFromRental(int rentalNumber) {
        String sql = "SELECT customer_id FROM RENTALS WHERE rental_number = ?;";
        int customerID = -1;
        try  {
            // Grab database connection from the main program
            Connection conn = MainProgram.dbConnection;
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, rentalNumber);
            // After preparing the statement, execute the query
            try (ResultSet rs = ps.executeQuery()) {
                // Retrieve the equipment type from the result set
                if (rs.next()) {
                    customerID = rs.getInt("customer_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerID;
    }

    /**
     * Create a rental record in the database based on the provided data.
     * @param sql
     * @param orderData array of order data, going in the order of date_rented, customer_id, serial_number
     */
    public static void ps_CreateRental(String sql, String [] orderData){
    	try {
    		ps = MainProgram.dbConnection.prepareStatement(sql);
            // Fill in the prepared statement parameters
    		for (int i = 1; i <= orderData.length; i++) {
                if(i == 2) {
                    ps.setInt(i, Integer.parseInt(orderData[i-1]));
                    continue;
                }
    			ps.setString(i, orderData[i-1]);
    		}

            ps.executeUpdate();
            System.out.println("Rental added successfully.");
            String rentalNumberQuery = "SELECT COUNT(*) AS rental_number FROM RENTALS;";
            PreparedStatement rentalPs = MainProgram.dbConnection.prepareStatement(rentalNumberQuery);
            ResultSet rs = rentalPs.executeQuery();
            int rentalNumber = rs.getInt("rental_number");
            System.out.println("Your rental number is: " + rentalNumber);
            rentalPs.close();
            ps.close();

    	} catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * Register the return of a rental in the database based on the provided data.
     * @param sql
     * @param returnDate
     * @param rentalNumber
     */
    public static void ps_RegisterReturn(String sql, String returnDate, int rentalNumber){
    	try {
    		ps = MainProgram.dbConnection.prepareStatement(sql);
            // Fill in the prepared statement parameters
            ps.setString(1, returnDate);
            ps.setInt(2, rentalNumber);

            ps.executeUpdate();
            ps.close();

    	} catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Schedule a delivery in the database based on the provided data.
     * @param sql
     * @param droneSerialNumber
     * @param customerID
     * @param rentalNumber
     */
    public static void ps_ScheduleDelivery(String sql, String droneSerialNumber, int customerID, int rentalNumber) {
    	try {
    		ps = MainProgram.dbConnection.prepareStatement(sql);
            // Fill in the prepared statement parameters
            ps.setString(1, droneSerialNumber);
            ps.setInt(2, customerID);
            ps.setInt(3, rentalNumber);
            // Excute the insert
            ps.executeUpdate();
            ps.close();
    	} catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Schedule a pickup in the database based on the provided data.
     * @param sql
     * @param droneSerialNumber
     * @param customerID
     * @param rentalNumber
     */
    public static void ps_SchedulePickup(String sql, String droneSerialNumber, int customerID, int rentalNumber) {
    	try {
    		ps = MainProgram.dbConnection.prepareStatement(sql);
            // Fill in the prepared statement parameters
            ps.setString(1, droneSerialNumber);
            ps.setInt(2, customerID);
            ps.setInt(3, rentalNumber);
            // Excute the insert
            ps.executeUpdate();
            ps.close();
    	} catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}






