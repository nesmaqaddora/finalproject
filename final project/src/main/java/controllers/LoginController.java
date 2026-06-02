package controllers;

import dao.UserDAO;
import models.User;
import utils.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LoginController {

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;

    private UserDAO userDAO = new UserDAO();

    @FXML
    public void handleLogin(ActionEvent event) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "خطأ", "الرجاء إدخال اسم المستخدم وكلمة المرور.");
            return;
        }

        User user = userDAO.authenticate(username, password);

        if (user != null) {

            UserSession.setRole(user.getRole());
            UserSession.setUsername(user.getUsername());

            try {
                Parent root;
                String title;
                if (user.getRole().equals("ADMIN")) {
                    root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/AdminDashboard.fxml")));
                    title = "GHADS System - Admin Dashboard";
                } else {
                    root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/CoordinatorDashboard.fxml")));
                    title = "GHADS System - Coordinator Dashboard";
                }

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle(title);
                stage.centerOnScreen();

            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "خطأ بالتحميل", "حدث خطأ أثناء فتح الشاشة.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "فشل تسجيل الدخول", "اسم المستخدم أو كلمة المرور غير صحيحة.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}