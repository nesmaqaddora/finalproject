package controllers;

import utils.DatabaseConnection;
import utils.UserSession;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;

public class CoordinatorDashboardController {

    @FXML private Label lblFamilyCount;
    @FXML private Label lblServedCount;
    @FXML private Label lblNotServedCount;

    @FXML
    public void initialize() {

        Platform.runLater(this::loadStatistics);
    }

    private void loadStatistics() {
        int totalFamilies = 0, servedFamilies = 0;

        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            Statement stmt = conn.createStatement();


            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM tbl_family");
            if (rs.next()) totalFamilies = rs.getInt(1);


            rs = stmt.executeQuery("SELECT COUNT(DISTINCT family_id) FROM tbl_aid_distribution");
            if (rs.next()) servedFamilies = rs.getInt(1);

            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        int notServedFamilies = totalFamilies - servedFamilies;


        if (lblFamilyCount != null) lblFamilyCount.setText(String.valueOf(totalFamilies));
        if (lblServedCount != null) lblServedCount.setText(String.valueOf(servedFamilies));
        if (lblNotServedCount != null) lblNotServedCount.setText(String.valueOf(notServedFamilies));
    }

    @FXML
    public void handleManageFamilies(ActionEvent event) {
        loadScreen(event, "/views/ManageFamilies.fxml", "Manage Families");
    }

    @FXML
    public void handleDistributeAid(ActionEvent event) {
        loadScreen(event, "/views/ManageAid.fxml", "Distribute Aid");
    }

    @FXML
    public void handleMyProfile(ActionEvent event) {
        loadScreen(event, "/views/UserProfile.fxml", "My Profile");
    }

    @FXML
    public void handleLogout(ActionEvent event) {
        UserSession.setRole("");
        UserSession.setUsername("");
        loadScreen(event, "/views/Login.fxml", "Login");
    }

    private void loadScreen(ActionEvent event, String fxmlPath, String title) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("GHADS System - " + title);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}