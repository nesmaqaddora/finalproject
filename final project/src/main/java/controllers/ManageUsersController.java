package controllers;

import dao.OrganizationDAO;
import dao.UserDAO;
import models.Organization;
import models.User;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.util.List;
import java.util.Optional;

public class ManageUsersController {

    @FXML private TextField txtFullName;
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private TextField txtEmail;
    @FXML private ComboBox<Organization> cmbOrganization;
    @FXML private TableView<User> tableUsers;
    @FXML private TableColumn<User, Integer> colId;
    @FXML private TableColumn<User, String> colName;
    @FXML private TableColumn<User, String> colUsername;
    @FXML private TableColumn<User, String> colEmail;

    private UserDAO userDAO = new UserDAO();
    private OrganizationDAO orgDAO = new OrganizationDAO();

    @FXML
    public void initialize() {

        cmbOrganization.setConverter(new StringConverter<Organization>() {
            @Override
            public String toString(Organization org) {
                return org == null ? "" : org.getName();
            }
            @Override
            public Organization fromString(String string) { return null; }
        });

        List<Organization> orgs = orgDAO.getAllOrganizations();
        cmbOrganization.setItems(FXCollections.observableArrayList(orgs));

        colId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        loadTableData();

        tableUsers.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtFullName.setText(newSelection.getFullName());
                txtUsername.setText(newSelection.getUsername());
                txtPassword.setText(newSelection.getPassword());
                txtEmail.setText(newSelection.getEmail());

                for (Organization org : cmbOrganization.getItems()) {
                    if (org.getOrgId() == newSelection.getOrgId()) {
                        cmbOrganization.setValue(org);
                        break;
                    }
                }
            }
        });
    }

    private void loadTableData() {

        List<User> allUsers = userDAO.getAllUsers();
        allUsers.removeIf(u -> u.getRole().equals("ADMIN"));
        tableUsers.setItems(FXCollections.observableArrayList(allUsers));
    }

    @FXML
    public void handleAdd(ActionEvent event) {
        if (isInputInvalid()) return;

        User user = new User(0, txtUsername.getText(), txtPassword.getText(), txtFullName.getText(), txtEmail.getText(), "COORDINATOR", cmbOrganization.getValue().getOrgId(), "default.png");
        if (userDAO.addUser(user)) {
            showAlert(Alert.AlertType.INFORMATION, "نجاح", "تم إضافة المنسق بنجاح.");
            handleReset();
            loadTableData();
        } else {
            showAlert(Alert.AlertType.ERROR, "خطأ", "اسم المستخدم أو الإيميل مكرر.");
        }
    }

    @FXML
    public void handleUpdate(ActionEvent event) {
        User selectedUser = tableUsers.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert(Alert.AlertType.WARNING, "تنبيه", "الرجاء تحديد منسق للتعديل.");
            return;
        }
        if (isInputInvalid()) return;

        selectedUser.setFullName(txtFullName.getText());
        selectedUser.setPassword(txtPassword.getText());
        selectedUser.setEmail(txtEmail.getText());

        if (userDAO.updateUser(selectedUser)) {
            showAlert(Alert.AlertType.INFORMATION, "نجاح", "تم التعديل بنجاح.");
            handleReset();
            loadTableData();
        } else {
            showAlert(Alert.AlertType.ERROR, "خطأ", "فشل التعديل.");
        }
    }

    @FXML
    public void handleDelete(ActionEvent event) {
        User selectedUser = tableUsers.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert(Alert.AlertType.WARNING, "تنبيه", "الرجاء تحديد منسق للحذف.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("تأكيد الحذف");
        confirm.setHeaderText("هل أنت متأكد من حذف المنسق؟");
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (userDAO.deleteUser(selectedUser.getUserId())) {
                showAlert(Alert.AlertType.INFORMATION, "نجاح", "تم الحذف بنجاح.");
                handleReset();
                loadTableData();
            } else {
                showAlert(Alert.AlertType.ERROR, "خطأ", "فشل الحذف.");
            }
        }
    }

    @FXML
    public void handleReset() {
        txtFullName.clear();
        txtUsername.clear();
        txtPassword.clear();
        txtEmail.clear();
        cmbOrganization.setValue(null);
        tableUsers.getSelectionModel().clearSelection();
    }

    private boolean isInputInvalid() {
        if (txtFullName.getText().isEmpty() || txtUsername.getText().isEmpty() || txtPassword.getText().isEmpty() || txtEmail.getText().isEmpty() || cmbOrganization.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "تنبيه", "الرجاء تعبئة جميع الحقول.");
            return true;
        }
        if (txtPassword.getText().length() < 8) {
            showAlert(Alert.AlertType.WARNING, "تنبيه", "كلمة المرور يجب أن تكون 8 رموز على الأقل.");
            return true;
        }
        return false;
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    public void handleBackToDashboard(ActionEvent event) {
        try {
            javafx.scene.Parent root = javafx.fxml.FXMLLoader.load(java.util.Objects.requireNonNull(getClass().getResource("/views/AdminDashboard.fxml")));
            javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(root));
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}