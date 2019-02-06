/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.UserDB;

/**
 * this is used to control the log in screen
 * validates log in attempts with database
 * reports errors as needed
 * @author dstron7 <dstron7@wgu.edu>
 */

public class LoginController implements Initializable {
    
    @FXML
    private TextField usernameTxt;
    
    @FXML
    private PasswordField passwordTxt;
    
    @FXML
    private Label usernameLabel;
    
    @FXML
    private Label passwordLabel;
    
    @FXML 
    private Label languageMessage;
    
    @FXML
    private Button loginButton;
    
    private String errorHeader;
    private String errorTitle;
    private String errorText;
    
    //loads username and or password and validates with database
    //handles errors if incorrect login
    @FXML
    public void handleLogin(ActionEvent event) throws IOException {
        String username = usernameTxt.getText();
        String password = passwordTxt.getText();
        boolean validUser = UserDB.login(username, password);
        if(validUser) {
            ((Node) (event.getSource())).getScene().getWindow().hide();
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(errorTitle);
            alert.setHeaderText(errorHeader);
            alert.setContentText(errorText);
            alert.showAndWait();
        }
    }
    
    //loads login screen with messages from languages properties files
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //set locale as default location for .properties files
        Locale locale = Locale.getDefault();
        rb = ResourceBundle.getBundle("languages/login", locale);
        
        /*        //test resource bundle for spanish
        Locale.setDefault(new Locale("es", "ES"));
        rb = ResourceBundle.getBundle("languages/login");*/
        
        usernameLabel.setText(rb.getString("username"));
        passwordLabel.setText(rb.getString("password"));
        loginButton.setText(rb.getString("login"));
        languageMessage.setText(rb.getString("language"));
        errorHeader = rb.getString("errorheader");
        errorTitle = rb.getString("errortitle");
        errorText = rb.getString("errortext");
    }    
    
}
