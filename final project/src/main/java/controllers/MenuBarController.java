package controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuBar;

public class MenuBarController {

    @FXML
    private MenuBar menuBar;


    private static int currentFontSize = 14;
    private static String currentFontFamily = "System";
    private static boolean isDarkMode = false;

    @FXML
    public void initialize() {

        Platform.runLater(this::applyStyles);
    }

    @FXML
    public void handleExit(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    public void handleFontSmall(ActionEvent event) {
        currentFontSize = 12;
        applyStyles();
    }

    @FXML
    public void handleFontMedium(ActionEvent event) {
        currentFontSize = 16;
        applyStyles();
    }

    @FXML
    public void handleFontLarge(ActionEvent event) {
        currentFontSize = 20;
        applyStyles();
    }

    @FXML
    public void handleFontSystem(ActionEvent event) {
        currentFontFamily = "System";
        applyStyles();
    }

    @FXML
    public void handleFontArial(ActionEvent event) {
        currentFontFamily = "Arial";
        applyStyles();
    }

    @FXML
    public void handleFontCourier(ActionEvent event) {
        currentFontFamily = "Courier New";
        applyStyles();
    }

    @FXML
    public void handleToggleTheme(ActionEvent event) {
        isDarkMode = !isDarkMode;
        applyStyles();
    }

    @FXML
    public void handleAbout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About System");
        alert.setHeaderText("GHADS - Gaza Humanitarian Aid Distribution System");
        alert.setContentText("هذا النظام يهدف إلى إدارة وتوزيع المساعدات الإنسانية على العائلات النازحة في قطاع غزة لضمان التوزيع العادل ومنع التكرار.\n\nDeveloped by: Nesma Sufyan Abdullah Qaddora\nCourse: Programming 3 Lab - Final Project");
        alert.showAndWait();
    }


    private void applyStyles() {
        if (menuBar != null && menuBar.getScene() != null) {
            Scene scene = menuBar.getScene();
            String style = "-fx-font-size: " + currentFontSize + "px; -fx-font-family: '" + currentFontFamily + "';";

            if (isDarkMode) {

                style += " -fx-base: #2b2b2b; -fx-background: #2b2b2b; -fx-control-inner-background: #3c3f41;";
            } else {

                style += " -fx-base: #ececec; -fx-background: #f4f4f4; -fx-control-inner-background: white;";
            }

            scene.getRoot().setStyle(style);
        }
    }
}