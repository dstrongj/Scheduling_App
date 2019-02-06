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
public final class CustomerAll {
    private final SimpleIntegerProperty customerAllId = new SimpleIntegerProperty();
    private final SimpleStringProperty customerAllName = new SimpleStringProperty();
    private final SimpleStringProperty customerAllAddress = new SimpleStringProperty();
    private final SimpleStringProperty customerAllCity = new SimpleStringProperty();
    private final SimpleStringProperty customerAllZip = new SimpleStringProperty();
    private final SimpleStringProperty customerAllPhone = new SimpleStringProperty();
    // adding status
    private final SimpleIntegerProperty customerAllActive = new SimpleIntegerProperty();
    
    public CustomerAll() {}
    
    public CustomerAll(int id, String name, String address, String city, String phone, String zip, int active) {
        setCustomerAllId(id);
        setCustomerAllName(name);
        setCustomerAllAddress(address);
        setCustomerAllCity(city);
        setCustomerAllPhone(phone);
        setCustomerAllZip(zip);
        setCustomerAllActive(active);
    }
    
    public int getCustomerAllId() {
        return customerAllId.get();
    }
    
    public String getCustomerAllName() {
        return customerAllName.get();
    }
    
    public String getCustomerAllAddress() {
        return customerAllAddress.get();
    }
    
    public String getCustomerAllCity() {
        return customerAllCity.get();
    }
    
    public String getCustomerAllPhone() {
        return customerAllPhone.get();
    }
    
    public String getCustomerAllZip() {
        return customerAllZip.get();
    }
    
    //status method
    public int getCustomerAllActive() {
        return customerAllActive.get();
    }
    
    public void setCustomerAllActive(int active) {
        this.customerAllActive.set(active);
    }
    //
    
    public void setCustomerAllId(int customerId) {
        this.customerAllId.set(customerId);
    }
    
    
    public void setCustomerAllName(String customerName) {
        this.customerAllName.set(customerName);
    }
    
    public void setCustomerAllAddress(String customerAddress) {
        this.customerAllAddress.set(customerAddress);
    }
    
    public void setCustomerAllCity(String customerCity) {
        this.customerAllCity.set(customerCity);
    }
    
    public void setCustomerAllPhone(String customerPhone) {
        this.customerAllPhone.set(customerPhone);
    }
    
    public void setCustomerAllZip(String customerZip) {
        this.customerAllZip.set(customerZip);
    }
}
