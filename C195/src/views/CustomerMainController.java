/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import models.Customer;
import models.CustomerAll;
import models.CustomerDB;

/**
 * FXML Controller class
 *
 * @author dstron7 <dstron7@wgu.edu>
 */

public class CustomerMainController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private AnchorPane customerMain;
    
    @FXML
    private TableView<Customer> customerTable;
    
    
    @FXML
    private TableColumn<Customer, Integer> customerId;
    
    @FXML
    private TableColumn<Customer, String> customerName;
    
    //status field in cusomer table
    @FXML
    private TableColumn<Customer, Integer> customerActive;
    
    
    private CustomerAll selectedCustomer;
    
    // new table
    @FXML
    private TableView<CustomerAll> customerAllTable;
    
    @FXML
    private TableColumn<CustomerAll, Integer> customerAllActive;
    
    @FXML
    private TableColumn<CustomerAll, String> customerAllId;
    
    @FXML
    private TableColumn<CustomerAll, String> customerAllName;
    
    
    
    
    @FXML
    public void handleBackButton(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
    
    @FXML
    public void handleAddButton() {
        Dialog<ButtonType> dialog = new Dialog();
        dialog.initOwner(customerMain.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("CustomerAdd.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch(IOException e) {
            System.out.println("CustomerAdd Error: " + e.getMessage());
        }
        ButtonType save = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(save);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        CustomerAddController controller = fxmlLoader.getController();
        dialog.showAndWait().ifPresent((response -> {
            
           //lambda to update customerDB with changes 
            if(response == save) {
                if(controller.handleAddCustomer()) {
                    customerTable.setItems(CustomerDB.getAllCustomers());
                    customerAllTable.setItems(CustomerDB.getAllActiveCustomers());
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Add Customer Error");
                    alert.setContentText(controller.displayErrors());
                    alert.showAndWait().ifPresent((response2 -> {
                        if(response2 == ButtonType.OK) {
                            handleAddButton();
                        }
                    }));
                }
            }
        }));
    }
    
    //handles customer modifications uses lambda to set modifications in 
    // customer database once saved
    @FXML
    public void handleModifyButton() {
        if(customerAllTable.getSelectionModel().getSelectedItem() != null) {
            selectedCustomer = customerAllTable.getSelectionModel().getSelectedItem();
        } else {
            return;
        }
        Dialog<ButtonType> dialog = new Dialog();
        dialog.initOwner(customerMain.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("CustomerModify.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch(IOException e) {
            System.out.println("CustomerModify Error: " + e.getMessage());
        }
        ButtonType save = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(save);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        CustomerModifyController controller = fxmlLoader.getController();
        controller.populateFields(selectedCustomer);
        dialog.showAndWait().ifPresent((response -> {
       
        //lambda to update customerDB with modified changes
            if(response == save) {
                if(controller.handleModifyCustomer()) {
                    customerTable.setItems(CustomerDB.getAllCustomers());
                    customerAllTable.setItems(CustomerDB.getAllActiveCustomers());
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Modify Customer Error");
                    alert.setContentText(controller.displayErrors());
                    alert.showAndWait().ifPresent((response2 -> {
                        if(response2 == ButtonType.OK) {
                            handleModifyButton();
                        }
                    }));
                }
            }
        }));
    }
    
    //makes customer inactive in database
    @FXML
    public void handleDeleteButton() {
        if(customerAllTable.getSelectionModel().getSelectedItem() != null) {
            selectedCustomer = customerAllTable.getSelectionModel().getSelectedItem();
        } else {
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Make Inactive");
        alert.setHeaderText("WARNING: Make Client Inactive");
        alert.setContentText("Deactivate Client: " + selectedCustomer.getCustomerAllName() + " ?");
        alert.showAndWait().ifPresent((response -> {
            
            //lambda to update customerDB with changes 
            if(response == ButtonType.OK) {
                CustomerDB.deactivateCustomer(selectedCustomer.getCustomerAllId());
                customerTable.setItems(CustomerDB.getAllCustomers());
                customerAllTable.setItems(CustomerDB.getAllActiveCustomers());
            }
        }));
    }
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // sets table values with query for all  customers
        customerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerActive.setCellValueFactory(new PropertyValueFactory<>("customerActive"));
        customerTable.setItems(CustomerDB.getAllCustomers());
        
        // sets table values with query for all active customers
        customerAllId.setCellValueFactory(new PropertyValueFactory<>("customerAllId"));
        customerAllName.setCellValueFactory(new PropertyValueFactory<>("customerAllName"));
        customerAllTable.setItems(CustomerDB.getAllActiveCustomers());
        
    }    
    
}
