/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.Database;

/**
 * this class controls the customer table in database
 * Provide the ability to add, update, and deactivate customer records in the database,
 * including name, address, and phone number.
 * @author dstron7 <dstron7@wgu.edu>
 */

public class CustomerDB {
    
    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    private static ObservableList<CustomerAll> allActiveCustomers = FXCollections.observableArrayList();
  
    
    public static Customer getCustomer(int id) {
        try {
            Statement statement = Database.getConnection().createStatement();
            String query = "SELECT * FROM customer WHERE customerId='" + id + "'";
            ResultSet results = statement.executeQuery(query);
            if(results.next()) {
                Customer customer = new Customer();
                customer.setCustomerName(results.getString("customerName"));
                statement.close();
                return customer;
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return null;
    }
    
    // Returns all Customers in Database
    public static ObservableList<Customer> getAllCustomers() {
        allCustomers.clear();
        try {
            try (Statement statement = Database.getConnection().createStatement()) {
                String query = "SELECT customer.customerId, customer.active, customer.customerName, address.address, address.phone, address.postalCode, city.city"
                        + " FROM customer INNER JOIN address ON customer.addressId = address.addressId "
                        + "INNER JOIN city ON address.cityId = city.cityId";
                ResultSet results = statement.executeQuery(query);
                while(results.next()) {
                    Customer customer = new Customer(
                            results.getInt("customerId"),
                            results.getString("customerName"),
                            results.getString("address"),
                            results.getString("city"),
                            results.getString("phone"),
                            results.getString("postalCode"),
                            results.getInt("active"));   
                    allCustomers.add(customer);
                }
            }
            return allCustomers;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
    }
    
    // Saves new Customer to Database by inserting into customer and address databases
    public static boolean saveCustomer(String name, String address, int cityId, String zip, String phone) {
        try {
            Statement statement = Database.getConnection().createStatement();
            String queryOne = "INSERT INTO address SET address2= 'NULL', createDate= NOW(), createdBy= 'NULL', lastUpdate= NOW(), lastUpdateBy= 'NULL', address='" + address + "', phone='" + phone + "', postalCode='" + zip + "', cityId=" + cityId;
            int updateOne = statement.executeUpdate(queryOne);
            if(updateOne == 1) {
               int addressId = allCustomers.size() + 1;
                String queryTwo = "INSERT INTO customer SET active = 1, createDate = NOW(), createdBy = 'NULL', lastUpdate = NOW(), lastUpdateBy = 'NULL', customerName='" + name + "', addressId=" + addressId;
                int updateTwo = statement.executeUpdate(queryTwo);
                if(updateTwo == 1) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return false;
    }
    
    // Update existing Customer in Database
    public static boolean updateCustomer(int id, String name, String address, int cityId, String zip, String phone) {
        try {
            Statement statement = Database.getConnection().createStatement();
            String queryOne = "UPDATE address SET address='" + address + "', address2='NULL', createDate= NOW(),createdBy='NULL', lastUpdate= NOW(),lastUpdateBy='NULL', cityId=" + cityId + ", postalCode='" + zip + "', phone='" + phone + "' "
                + "WHERE addressId=" + id;
            int updateOne = statement.executeUpdate(queryOne);
            if(updateOne == 1) {
                String queryTwo = "UPDATE customer SET customerName='" + name + "',lastUpdate= NOW(),lastUpdateBy='NULL', addressId=" + id + " WHERE customerId=" + id;
                int updateTwo = statement.executeUpdate(queryTwo);
                if(updateTwo == 1) {
                    return true;
                }
            }
        } catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return false;
    }
    
    // updates customer to inactive in customer database
        public static boolean deactivateCustomer(int id) {
        try {
            Statement statement = Database.getConnection().createStatement();
            String queryOne = "UPDATE customer SET active = 0 WHERE customerId=" + id;
            int updateOne = statement.executeUpdate(queryOne);
            if(updateOne == 1){
                return true;
                }
        } catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return false;
    }
        
          // Returns all ACTIVE Customers in Database
        public static ObservableList<CustomerAll> getAllActiveCustomers() {
        allActiveCustomers.clear();
        try {
        try (Statement statement = Database.getConnection().createStatement()) {
        String query = "SELECT customer.customerId, customer.customerName,customer.active, address.address, address.phone, address.postalCode, city.city"
        + " FROM customer INNER JOIN address ON customer.addressId = address.addressId "
        + "INNER JOIN city ON address.cityId = city.cityId WHERE (customer.active = 1)";
        ResultSet results = statement.executeQuery(query);
        while(results.next()) {
        CustomerAll customerAll = new CustomerAll(
        results.getInt("customerId"),
        results.getString("customerName"),
        results.getString("address"),
        results.getString("city"),
        results.getString("phone"),
        results.getString("postalCode"),
        results.getInt("active"));
        allActiveCustomers.add(customerAll);
        }
        }
        return allActiveCustomers;
        } catch (SQLException e) {
        System.out.println("SQLException: " + e.getMessage());
        return null;
        }
    }
}
