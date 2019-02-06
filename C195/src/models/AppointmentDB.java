/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.Database;


/**
 *
 * @author dstron7 <dstron7@wgu.edu>
 */
public class AppointmentDB {
     
    public static ObservableList<Appointment> getMonthlyAppointments (int id) {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        Appointment appointment;
        LocalDate begin = LocalDate.now();
        LocalDate end = LocalDate.now().plusMonths(12);
        try {
            Statement statement = Database.getConnection().createStatement();
            String query = "SELECT * FROM appointment WHERE customerId = '" + id + "' AND " + 
                "start >= '" + begin + "' AND start <= '" + end + "'"; 
            ResultSet results = statement.executeQuery(query);
            while(results.next()) {
                appointment = new Appointment(results.getInt("appointmentId"), results.getInt("customerId"), results.getString("start"),
                    results.getString("end"), results.getString("title"), results.getString("description"),
                    results.getString("location"), results.getString("contact"));
                appointments.add(appointment);
            }
            statement.close();
            return appointments;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
    }
    //weekly appointments list view today + 7 day view
    public static ObservableList<Appointment> getWeeklyAppoinments(int id) {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        Appointment appointment;
        LocalDate begin = LocalDate.now();
        LocalDate end = LocalDate.now().plusWeeks(1);
        try {
            Statement statement = Database.getConnection().createStatement();
            String query = "SELECT * FROM appointment WHERE customerId = '" + id + "' AND " + 
                "start >= '" + begin + "' AND start <= '" + end + "'";
            ResultSet results = statement.executeQuery(query);
            while(results.next()) {
                appointment = new Appointment(results.getInt("appointmentId"), results.getInt("customerId"), results.getString("start"),
                    results.getString("end"), results.getString("title"), results.getString("description"),
                    results.getString("location"), results.getString("contact"));
                appointments.add(appointment);
            }
            statement.close();
            return appointments;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
    }
    
    //use local time +15 minutes to set notice
    public static Appointment appointmentIn15Min() {
        Appointment appointment;
        LocalDateTime now = LocalDateTime.now();
        ZoneId zid = ZoneId.systemDefault();
        ZonedDateTime zdt = now.atZone(zid);
        LocalDateTime ldt = zdt.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
        LocalDateTime ldt2 = ldt.plusMinutes(15);
        String user = UserDB.getCurrentUser().getUsername();
        try {
            Statement statement = Database.getConnection().createStatement();
            String query = "SELECT * FROM appointment WHERE start BETWEEN '" + ldt + "' AND '" + ldt2 + "' AND " + 
                "contact='" + user + "'";
            ResultSet results = statement.executeQuery(query);
            if(results.next()) {
                appointment = new Appointment(results.getInt("appointmentId"), results.getInt("customerId"), results.getString("start"),
                    results.getString("end"), results.getString("title"), results.getString("description"),
                    results.getString("location"), results.getString("contact"));
                return appointment;
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return null;
    }
    
    //saves new appointment splits out title and desctiption based off of :
    public static boolean saveAppointment(int id, String type, String contact, String location, String date, String time) {
        String title = type.split(":")[0];
        String description = type.split(":")[1];
        String tsStart = createTimeStamp(date, time, location, true);
        String tsEnd = createTimeStamp(date, time, location, false);
        try {
            Statement statement = Database.getConnection().createStatement();
            String query = "INSERT INTO appointment SET customerId='" + id + "', title='" + title + "', description='" +
                description + "', contact='" + contact + "', location='" + location + "', start='" + tsStart + "', end='" + 
                tsEnd + "', url='', createDate=NOW(), createdBy='', lastUpdate=NOW(), lastUpdateBy=''";
            int update = statement.executeUpdate(query);
            if(update == 1) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return false;
    }
    
    public static boolean updateAppointment(int id, String type, String contact, String location, String date, String time) {
        String title = type.split(":")[0];
        String description = type.split(":")[1];
        String tsStart = createTimeStamp(date, time, location, true);
        String tsEnd = createTimeStamp(date, time, location, false);
        try {
            Statement statement = Database.getConnection().createStatement();
            String query = "UPDATE appointment SET title='" + title + "', description='" + description + "', contact='" +
                contact + "', location='" + location + "', start='" + tsStart + "', end='" + tsEnd + "' WHERE " +
                "appointmentId='" + id + "'";
            int update = statement.executeUpdate(query);
            if(update == 1) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return false;
    }
    
    public static boolean overlappingAppointment(int id, String location, String date, String time) {
        String start = createTimeStamp(date, time, location, true);
        try {
            Statement statement = Database.getConnection().createStatement();
            String query = "SELECT * FROM appointment WHERE start = '" + start + "' AND location = '" + location + "'";
            ResultSet results = statement.executeQuery(query);
            if(results.next()) {
                if(results.getInt("appointmentId") == id) {
                    statement.close();
                    return false;
                }
                statement.close();
                return true;
            } else {
                statement.close();
                return false;
            }
        } catch (SQLException e) {
            System.out.println("SQLExcpection: " + e.getMessage());
            return true;
        }
    }
    //deletes appointment from database when cancelled
    public static boolean deleteAppointment(int id) {
        try {
            Statement statement = Database.getConnection().createStatement();
            String query = "DELETE FROM appointment WHERE appointmentId = " + id;
            int update = statement.executeUpdate(query);
            if(update == 1) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return false;
    }
    //creates timestamp based on UTC location time
    public static String createTimeStamp(String date, String time, String location, boolean startMode) {
        String h = time.split(":")[0];
        int rawH = Integer.parseInt(h);
        if(rawH < 9) {
            rawH += 12;
        }
        if(!startMode) {
            rawH += 1;
        }
        String rawD = String.format("%s %02d:%s", date, rawH, "00");
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm");
        LocalDateTime ldt = LocalDateTime.parse(rawD, df);
        ZoneId zid;
        if(location.equals("New York")) {
            zid = ZoneId.of("America/New_York");
        } else if(location.equals("Denver")) {
            zid = ZoneId.of("America/Phoenix");
        } else if(location.equals("Seattle")) {
            zid = ZoneId.of("America/Los_Angeles");
        } else if(location.equals("Los Angeles")) {
            zid = ZoneId.of("America/Los_Angeles");
        } else if(location.equals("Detroit")) {
            zid = ZoneId.of("America/New_York");
        }else {
            zid = ZoneId.of("America/Chicago");
        }
        ZonedDateTime zdt = ldt.atZone(zid);
        ZonedDateTime utcDate = zdt.withZoneSameInstant(ZoneId.of("UTC"));
        ldt = utcDate.toLocalDateTime();
        Timestamp ts = Timestamp.valueOf(ldt); 
        return ts.toString();
    }
    
}
