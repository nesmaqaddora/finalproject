package controllers;

import dao.OrganizationDAO;
import models.Organization;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.Optional;

public class ManageOrganizationsController {

    @FXML private TextField txtName;
    @FXML private ComboBox<String> cmbType;
    @FXML private TextField txtContact;
    @FXML private TableView<Organization> tableOrganizations;
    @FXML private TableColumn<Organization, Integer> colId;
    @FXML private TableColumn<Organization, String> colName;
    @FXML private TableColumn<Organization, String> colType;
    @FXML private TableColumn<Organization, String> colContact;

    private OrganizationDAO orgDAO = new OrganizationDAO();

    @FXML
    public void initialize() {

        cmbType.setItems(FXCollections.observableArrayList("NGO", "UN", "Local", "Government"));


        colId.setCellValueFactory(new PropertyValueFactory<>("orgId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contactInfo"));

        loadTableData();


        tableOrganizations.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtName.setText(newSelection.getName());
                cmbType.setValue(newSelection.getType());
                txtContact.setText(newSelection.getContactInfo());
            }
        });
    }

    @FXML
    public void handleRefresh() {
        loadTableData();
    }

    private void loadTableData() {
        ObservableList<Organization> orgList = FXCollections.observableArrayList(orgDAO.getAllOrganizations());
        tableOrganizations.setItems(orgList);
    }

    @FXML
    public void handleAdd(ActionEvent event) {
        if (isInputInvalid()) return;

        Organization org = new Organization(0, txtName.getText(), cmbType.getValue(), txtContact.getText());
        if (orgDAO.addOrganization(org)) {
            showAlert(Alert.AlertType.INFORMATION, "نجاح", "تم إضافة المؤسسة بنجاح.");
            handleReset();
            loadTableData();
        } else {
            showAlert(Alert.AlertType.ERROR, "خطأ", "فشل إضافة المؤسسة.");
        }
    }

    @FXML
    public void handleUpdate(ActionEvent event) {
        Organization selectedOrg = tableOrganizations.getSelectionModel().getSelectedItem();
        if (selectedOrg == null) {
            showAlert(Alert.AlertType.WARNING, "تنبيه", "الرجاء تحديد مؤسسة من الجدول لتعديلها.");
            return;
        }
        if (isInputInvalid()) return;

        selectedOrg.setName(txtName.getText());
        selectedOrg.setType(cmbType.getValue());
        selectedOrg.setContactInfo(txtContact.getText());

        if (orgDAO.updateOrganization(selectedOrg)) {
            showAlert(Alert.AlertType.INFORMATION, "نجاح", "تم التعديل بنجاح.");
            handleReset();
            loadTableData();
        } else {
            showAlert(Alert.AlertType.ERROR, "خطأ", "فشل التعديل.");
        }
    }

    @FXML
    public void handleDelete(ActionEvent event) {
        Organization selectedOrg = tableOrganizations.getSelectionModel().getSelectedItem();
        if (selectedOrg == null) {
            showAlert(Alert.AlertType.WARNING, "تنبيه", "الرجاء تحديد مؤسسة من الجدول لحذفها.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("تأكيد الحذف");
        confirm.setHeaderText("هل أنت متأكد من حذف المؤسسة؟");
        confirm.setContentText("سيتم حذف المؤسسة وكل البيانات المرتبطة بها.");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (orgDAO.deleteOrganization(selectedOrg.getOrgId())) {
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
        txtName.clear();
        cmbType.setValue(null);
        txtContact.clear();
        tableOrganizations.getSelectionModel().clearSelection();
    }

    private boolean isInputInvalid() {
        if (txtName.getText().isEmpty() || cmbType.getValue() == null || txtContact.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "تنبيه", "الرجاء تعبئة جميع الحقول.");
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