package controllers;

import dao.FamilyDAO;
import models.Family;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.Optional;

public class ManageFamiliesController {

    @FXML private TextField txtNationalId;
    @FXML private TextField txtHeadName;
    @FXML private TextField txtPhone;
    @FXML private TextField txtMembersCount;
    @FXML private ComboBox<String> cmbVulnerability;
    @FXML private TextField txtAddress;

    @FXML private TableView<Family> tableFamilies;
    @FXML private TableColumn<Family, Integer> colId;
    @FXML private TableColumn<Family, String> colNationalId;
    @FXML private TableColumn<Family, String> colName;
    @FXML private TableColumn<Family, String> colPhone;
    @FXML private TableColumn<Family, Integer> colMembers;
    @FXML private TableColumn<Family, String> colVulnerability;
    @FXML private TableColumn<Family, String> colAddress;

    private FamilyDAO familyDAO = new FamilyDAO();

    @FXML
    public void initialize() {
        cmbVulnerability.setItems(FXCollections.observableArrayList("LOW", "MEDIUM", "HIGH"));

        colId.setCellValueFactory(new PropertyValueFactory<>("familyId"));
        colNationalId.setCellValueFactory(new PropertyValueFactory<>("nationalId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("householdName"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colMembers.setCellValueFactory(new PropertyValueFactory<>("familySize"));
        colVulnerability.setCellValueFactory(new PropertyValueFactory<>("vulnerabilityLevel"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("location"));

        loadTableData();

        tableFamilies.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtNationalId.setText(newSelection.getNationalId());
                txtHeadName.setText(newSelection.getHouseholdName());
                txtPhone.setText(newSelection.getPhone());
                txtMembersCount.setText(String.valueOf(newSelection.getFamilySize()));
                cmbVulnerability.setValue(newSelection.getVulnerabilityLevel());
                txtAddress.setText(newSelection.getLocation());
            }
        });
    }

    private void loadTableData() {
        tableFamilies.setItems(FXCollections.observableArrayList(familyDAO.getAllFamilies()));
    }

    @FXML
    public void handleAdd(ActionEvent event) {
        try {

            if (isInputInvalid()) return;


            int members = Integer.parseInt(txtMembersCount.getText());
            java.time.LocalDate today = java.time.LocalDate.now();
            Family family = new Family(0, txtNationalId.getText(), txtHeadName.getText(), txtPhone.getText(), txtAddress.getText(), members, cmbVulnerability.getValue(), today);


            boolean success = familyDAO.addFamily(family);


            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "نجاح", "تم إضافة العائلة بنجاح.");
                handleReset();
                loadTableData();
            } else {
                showAlert(Alert.AlertType.ERROR, "خطأ", "فشل الإضافة! رقم الهوية مسجل مسبقاً لعائلة أخرى.");
            }

        } catch (Exception e) {

            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "خطأ بالنظام", "حدث خطأ غير متوقع: " + e.getMessage());
        }
    }

    @FXML
    public void handleUpdate(ActionEvent event) {
        Family selectedFamily = tableFamilies.getSelectionModel().getSelectedItem();
        if (selectedFamily == null) {
            showAlert(Alert.AlertType.WARNING, "تنبيه", "الرجاء تحديد عائلة للتعديل.");
            return;
        }
        if (isInputInvalid()) return;

        selectedFamily.setNationalId(txtNationalId.getText());
        selectedFamily.setHouseholdName(txtHeadName.getText());
        selectedFamily.setPhone(txtPhone.getText());
        selectedFamily.setFamilySize(Integer.parseInt(txtMembersCount.getText()));
        selectedFamily.setVulnerabilityLevel(cmbVulnerability.getValue());
        selectedFamily.setLocation(txtAddress.getText());

        if (familyDAO.updateFamily(selectedFamily)) {
            showAlert(Alert.AlertType.INFORMATION, "نجاح", "تم التعديل بنجاح.");
            handleReset();
            loadTableData();
        } else {
            showAlert(Alert.AlertType.ERROR, "خطأ", "فشل التعديل.");
        }
    }

    @FXML
    public void handleDelete(ActionEvent event) {
        Family selectedFamily = tableFamilies.getSelectionModel().getSelectedItem();
        if (selectedFamily == null) {
            showAlert(Alert.AlertType.WARNING, "تنبيه", "الرجاء تحديد عائلة للحذف.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("تأكيد الحذف");
        confirm.setHeaderText("هل أنت متأكد من حذف العائلة؟");
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (familyDAO.deleteFamily(selectedFamily.getFamilyId())) {
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
        txtNationalId.clear();
        txtHeadName.clear();
        txtPhone.clear();
        txtMembersCount.clear();
        cmbVulnerability.setValue(null);
        txtAddress.clear();
        tableFamilies.getSelectionModel().clearSelection();
    }

    private boolean isInputInvalid() {
        if (txtNationalId.getText().isEmpty() || txtHeadName.getText().isEmpty() || txtPhone.getText().isEmpty() || txtMembersCount.getText().isEmpty() || cmbVulnerability.getValue() == null || txtAddress.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "تنبيه", "الرجاء تعبئة جميع الحقول.");
            return true;
        }

        if (!txtMembersCount.getText().matches("\\d+")) {
            showAlert(Alert.AlertType.WARNING, "تنبيه", "عدد الأفراد يجب أن يكون رقماً صحيحاً فقط.");
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
            String fxmlPath = utils.UserSession.getRole().equals("ADMIN") ? "/views/AdminDashboard.fxml" : "/views/CoordinatorDashboard.fxml";
            javafx.scene.Parent root = javafx.fxml.FXMLLoader.load(java.util.Objects.requireNonNull(getClass().getResource(fxmlPath)));
            javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(root));
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}