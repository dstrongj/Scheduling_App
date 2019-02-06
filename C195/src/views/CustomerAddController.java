/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import models.CustomerDB;

/**
 * FXML Controller class
 *
 * @author dstron7 <dstron7@wgu.edu>
 */


public class CustomerAddController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private TextField name;
    
    @FXML
    private TextField address;
    
    @FXML
    private ComboBox city;
    
    @FXML
    private TextField country;
    
    @FXML
    private TextField zip;
    
    @FXML
    private TextField phone;
    
    private ObservableList<String> cities = FXCollections.observableArrayList(
    "New York","Los Angeles","Seattle","Detroit","Dallas","Denver","Mexico City","Tijuana","Juárez","Guadalajara");
    
    private ObservableList<String> errors = FXCollections.observableArrayList();
    
    @FXML
    public void setCountry() {
        String currentCity = city.getSelectionModel().getSelectedItem().toString();
        if(currentCity.equals("Mexico City")||currentCity.equals("Tijuana")||currentCity.equals("Juárez")||currentCity.equals("Guadalajara")) {
            country.setText("Mexico");
        } else {
            country.setText("United States");       
        }
    }
    //updated all 2/5
    public boolean handleAddCustomer() {
        errors.clear();
        String customerAllName = name.getText();
        String customerAllAddress = address.getText();
        int customerAllCity = city.getSelectionModel().getSelectedIndex() + 1;
        String customerAllZip = zip.getText();
        String customerAllPhone = phone.getText();
        if(!validateName(customerAllName)||!validateAddress(customerAllAddress)||!validateCity(customerAllCity)||
                !validateZip(customerAllZip)||!validatePhone(customerAllPhone)){
            return false;
        } else {
            return CustomerDB.saveCustomer(customerAllName, customerAllAddress, customerAllCity, customerAllZip, customerAllPhone);
        }
    }
    
    public boolean validateName(String name) {
        if(name.isEmpty()) {
            errors.add("Name cannot be empty");
            return false;
        } else {
            return true;
        }
    }
    
    public boolean validateAddress(String address) {
        if(address.isEmpty()) {
            errors.add("Address cannot be empty");
            return false;
        } else {
            return true;
        }
    }
    
    public boolean validateCity(int city) {
        if(city == 0) {
            errors.add("City must be selected");
            return false;
        } else {
            return true;
        }
    }
    
    public boolean validateZip(String zip) {
        if(zip.isEmpty()) {
            errors.add("Zip cannot be empty");
            return false;
        } else {
            return true;
        }
    }
    
    public boolean validatePhone(String phone) {
        if(phone.isEmpty()) {
            errors.add("Phone cannot be empty");
            return false;
        } else {
            return true;
        }
    }
    
    public String displayErrors(){
        String s = "";
        if(errors.size() > 0) {
            for(String err : errors) {
                s = s.concat(err);
            }
            return s;
        } else {
            s = "Database Error";
            return s;
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        city.setItems(cities);
    }    
    
}
