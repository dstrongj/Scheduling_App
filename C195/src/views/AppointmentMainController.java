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
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import models.Appointment;
import models.AppointmentDB;
import models.Customer;
import models.CustomerAll;
import models.CustomerDB;

/**
 * FXML Controller class
 *
 * @author dstron7 <dstron7@wgu.edu>
 */

public class AppointmentMainController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private AnchorPane appointmentMain;
    
    @FXML
    private TableView<CustomerAll> customerAllTable;
    
    @FXML
    private TableColumn<CustomerAll, Integer> customerAllActive;
    
    @FXML
    private TableColumn<CustomerAll, String> customerAllId;
    
    @FXML
    private TableColumn<CustomerAll, String> customerAllName;
    
    @FXML
    private Label monthCustomerLabel;
    
    @FXML
    private TableView<Appointment> monthAptTable;
    
    @FXML
    private TableColumn<Appointment, String> monthDescription;
    
    @FXML
    private TableColumn<Appointment, String> monthContact;
    
    @FXML
    private TableColumn<Appointment, String> monthLocation;
    
    @FXML
    private TableColumn<Appointment, String> monthStart;
    
    @FXML
    private TableColumn<Appointment, String> monthEnd;
    
    @FXML
    private Label weekCustomerLabel;
    
    @FXML
    private TableView<Appointment> weekAptTable;
    
    @FXML
    private TableColumn<Appointment, String> weekDescription;
    
    @FXML
    private TableColumn<Appointment, String> weekContact;
    
    @FXML
    private TableColumn<Appointment, String> weekLocation;
    
    @FXML
    private TableColumn<Appointment, String> weekStart;
    
    @FXML
    private TableColumn<Appointment, String> weekEnd;
    
    @FXML 
    private Tab monthly;
    
    private CustomerAll selectedCustomer;
    
    private Appointment selectedAppointment;
    
    private boolean isMonthly;
    
    //updates monthly & weekly appointment tables based on customer id when
    // clicking on the client name
    @FXML
    public void handleCustomerClick(MouseEvent event) {
        selectedCustomer = customerAllTable.getSelectionModel().getSelectedItem();
        int id = selectedCustomer.getCustomerAllId();
        monthCustomerLabel.setText(selectedCustomer.getCustomerAllName());
        weekCustomerLabel.setText(selectedCustomer.getCustomerAllName());
        monthAptTable.setItems(AppointmentDB.getMonthlyAppointments(id));
        weekAptTable.setItems(AppointmentDB.getWeeklyAppoinments(id));
    }
    //handles adding new appointments "schedule appointment" button
    @FXML 
    public void handleAddButton() {
        if(customerAllTable.getSelectionModel().getSelectedItem() != null) {
            selectedCustomer = customerAllTable.getSelectionModel().getSelectedItem();
        } else {
            return;
        }
        Dialog<ButtonType> dialog = new Dialog();
        dialog.initOwner(appointmentMain.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("AppointmentAdd.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch(IOException e) {
            System.out.println("AppointmentAdd Error: " + e.getMessage());
        }
        ButtonType save = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(save);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        AppointmentAddController controller = fxmlLoader.getController();
        //handles customer selection from table to populate monthly / weekly report tabs
        controller.populateCustomerAllName(selectedCustomer.getCustomerAllName());
        dialog.showAndWait().ifPresent((response -> {
            if(response == save) {
                if(controller.handleAddAppointment(selectedCustomer.getCustomerAllId())) {
                    monthAptTable.setItems(AppointmentDB.getMonthlyAppointments(selectedCustomer.getCustomerAllId()));
                    weekAptTable.setItems(AppointmentDB.getWeeklyAppoinments(selectedCustomer.getCustomerAllId()));
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Add Appointment Error");
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
    
    @FXML
    public void handleModifyButton() {
        if(monthly.isSelected()) {
            if(monthAptTable.getSelectionModel().getSelectedItem() != null) {
                selectedAppointment = monthAptTable.getSelectionModel().getSelectedItem();
            } else {
                return;
            }
        } else {
            if(weekAptTable.getSelectionModel().getSelectedItem() != null) {
                selectedAppointment = weekAptTable.getSelectionModel().getSelectedItem();
            } else {
                return;
            }
        }
        Dialog<ButtonType> dialog = new Dialog();
        dialog.initOwner(appointmentMain.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("AppointmentModify.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch(IOException e) {
            System.out.println("AppointmentModify Error: " + e.getMessage());
        }
        ButtonType save = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(save);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        AppointmentModifyController controller = fxmlLoader.getController();
        controller.populateFields(selectedCustomer.getCustomerAllName(), selectedAppointment);
        dialog.showAndWait().ifPresent((response -> {
            if(response == save) {
                if(controller.handleModifyAppointment(selectedAppointment.getAptId())) {
                    monthAptTable.setItems(AppointmentDB.getMonthlyAppointments(selectedCustomer.getCustomerAllId()));
                    weekAptTable.setItems(AppointmentDB.getWeeklyAppoinments(selectedCustomer.getCustomerAllId()));
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Change Appointment Error");
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
    
    @FXML
    public void handleBackButton(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
    
    @FXML
    public void handleDeleteButton() {
        if(monthly.isSelected()) {
            isMonthly = true;
            if(monthAptTable.getSelectionModel().getSelectedItem() != null) {
                selectedAppointment = monthAptTable.getSelectionModel().getSelectedItem();
            } else {
                return;
            }
        } else {
            isMonthly = false;
            if(weekAptTable.getSelectionModel().getSelectedItem() != null) {
                selectedAppointment = weekAptTable.getSelectionModel().getSelectedItem();
            } else {
                return;
            }
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel");
        alert.setHeaderText("Cancel Appointment");
        alert.setContentText("Confirm Appointment Cancellation");
        alert.showAndWait().ifPresent((response -> {
            if(response == ButtonType.OK) {
                AppointmentDB.deleteAppointment(selectedAppointment.getAptId());
                if(isMonthly) {
                   monthAptTable.setItems(AppointmentDB.getMonthlyAppointments(selectedCustomer.getCustomerAllId())); 
                } else {
                    weekAptTable.setItems(AppointmentDB.getWeeklyAppoinments(selectedCustomer.getCustomerAllId()));
                }
            }
        }));
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      
        customerAllId.setCellValueFactory(new PropertyValueFactory<>("customerAllId"));
        customerAllName.setCellValueFactory(new PropertyValueFactory<>("customerAllName"));
        customerAllTable.setItems(CustomerDB.getAllActiveCustomers());
        
        
        // lambda to return values for weekly customer appointments
        monthDescription.setCellValueFactory(cellData -> {
            return cellData.getValue().getAptDescriptionProperty();
        });
        monthContact.setCellValueFactory(cellData -> {
            return cellData.getValue().getAptContactProperty();
        });
        monthLocation.setCellValueFactory(cellData -> {
            return cellData.getValue().getAptLocationProperty();
        });
        monthStart.setCellValueFactory(cellData -> {
            return cellData.getValue().getAppointmentStart();
        });
        monthEnd.setCellValueFactory(cellData -> {
            return cellData.getValue().getAppointmentEnd();
        });
       
        // lambda to return values for weekly customer appointments
        weekDescription.setCellValueFactory(cellData -> {
            return cellData.getValue().getAptDescriptionProperty();
        });
        weekContact.setCellValueFactory(cellData -> {
            return cellData.getValue().getAptContactProperty();
        });
        weekLocation.setCellValueFactory(cellData -> {
            return cellData.getValue().getAptLocationProperty();
        });
        weekStart.setCellValueFactory(cellData -> {
            return cellData.getValue().getAppointmentStart();
        });
        weekEnd.setCellValueFactory(cellData -> {
            return cellData.getValue().getAppointmentEnd();
        });
    }    
    
}
