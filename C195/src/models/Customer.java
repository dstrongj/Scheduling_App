/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author dstron7 <dstron7@wgu.edu>
 */
public final class Customer {
    private final SimpleIntegerProperty customerId = new SimpleIntegerProperty();
    private final SimpleStringProperty customerName = new SimpleStringProperty();
    private final SimpleStringProperty customerAddress = new SimpleStringProperty();
    private final SimpleStringProperty customerCity = new SimpleStringProperty();
    private final SimpleStringProperty customerZip = new SimpleStringProperty();
    private final SimpleStringProperty customerPhone = new SimpleStringProperty();
    // adding status
    private final SimpleIntegerProperty customerActive = new SimpleIntegerProperty();
    
    public Customer() {}
    
    public Customer(int id, String name, String address, String city, String phone, String zip, int active) {
        setCustomerId(id);
        setCustomerName(name);
        setCustomerAddress(address);
        setCustomerCity(city);
        setCustomerPhone(phone);
        setCustomerZip(zip);
        setCustomerActive(active);
    }
    
    public int getCustomerId() {
        return customerId.get();
    }
    
    public String getCustomerName() {
        return customerName.get();
    }
    
    public String getCustomerAddress() {
        return customerAddress.get();
    }
    
    public String getCustomerCity() {
        return customerCity.get();
    }
    
    public String getCustomerPhone() {
        return customerPhone.get();
    }
    
    public String getCustomerZip() {
        return customerZip.get();
    }
    
    //status method
    public int getCustomerActive() {
        return customerActive.get();
    }
    
    public void setCustomerActive(int active) {
        this.customerActive.set(active);
    }
    //
    
    public void setCustomerId(int customerId) {
        this.customerId.set(customerId);
    }
    
    public void setCustomerName(String customerName) {
        this.customerName.set(customerName);
    }
    
    public void setCustomerAddress(String customerAddress) {
        this.customerAddress.set(customerAddress);
    }
    
    public void setCustomerCity(String customerCity) {
        this.customerCity.set(customerCity);
    }
    
    public void setCustomerPhone(String customerPhone) {
        this.customerPhone.set(customerPhone);
    }
    
    public void setCustomerZip(String customerZip) {
        this.customerZip.set(customerZip);
    }
}
