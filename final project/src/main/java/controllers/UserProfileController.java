package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import utils.DatabaseConnection;
import utils.UserSession;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserProfileController {

    @FXML private TextField txtUsername;
    @FXML private TextField txtFullName;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private ImageView imgProfile;

    private String currentPhotoPath = null;

    @FXML
    public void initialize() {
        loadUserData();
    }

    private void loadUserData() {
        String username = UserSession.getUsername();
        txtUsername.setText(username);

        String query = "SELECT full_name, email, password, photo_path FROM tbl_user WHERE username = ?";

        try {

            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                txtFullName.setText(rs.getString("full_name"));
                txtEmail.setText(rs.getString("email"));
                txtPassword.setText(rs.getString("password"));

                String photoPath = rs.getString("photo_path");
                if (photoPath != null && !photoPath.isEmpty()) {
                    currentPhotoPath = photoPath;
                    try {
                        File imgFile = new File(photoPath);
                        if(imgFile.exists()) {
                            imgProfile.setImage(new Image(imgFile.toURI().toString()));
                        }
                    } catch (Exception e) {
                        System.out.println("Could not load image.");
                    }
                }
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleChooseImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Picture");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        File selectedFile = fileChooser.showOpenDialog(txtUsername.getScene().getWindow());
        if (selectedFile != null) {
            currentPhotoPath = selectedFile.getAbsolutePath();
            imgProfile.setImage(new Image(selectedFile.toURI().toString()));
        }
    }

    @FXML
    public void handleSaveProfile(ActionEvent event) {
        String query = "UPDATE tbl_user SET full_name = ?, email = ?, password = ?, photo_path = ? WHERE username = ?";

        try {

            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, txtFullName.getText());
            stmt.setString(2, txtEmail.getText());
            stmt.setString(3, txtPassword.getText());
            stmt.setString(4, currentPhotoPath);
            stmt.setString(5, UserSession.getUsername());

            if (stmt.executeUpdate() > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "تم تحديث بيانات البروفايل والصورة بنجاح!");
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "حدث خطأ أثناء حفظ البيانات.");
        }
    }

    @FXML
    public void handleBackToDashboard(ActionEvent event) {
        try {
            String fxmlPath = UserSession.getRole().equals("ADMIN") ? "/views/AdminDashboard.fxml" : "/views/CoordinatorDashboard.fxml";
            javafx.scene.Parent root = javafx.fxml.FXMLLoader.load(java.util.Objects.requireNonNull(getClass().getResource(fxmlPath)));
            javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(root));
        } catch (java.io.IOException e) {
            e.printStackTrace();
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