package controllers;

import dao.AidDistributionDAO;
import dao.FamilyDAO;
import models.AidDistribution;
import models.Family;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ManageAidController {

    @FXML private ComboBox<String> cmbFilter;
    @FXML private ComboBox<Family> cmbFamily;
    @FXML private ComboBox<String> cmbAidType;
    @FXML private TextField txtQuantity;

    @FXML private TableView<AidDistribution> tableAid;
    @FXML private TableColumn<AidDistribution, Integer> colId;
    @FXML private TableColumn<AidDistribution, String> colFamily;
    @FXML private TableColumn<AidDistribution, String> colType;
    @FXML private TableColumn<AidDistribution, Integer> colQuantity;
    @FXML private TableColumn<AidDistribution, LocalDate> colDate;

    private AidDistributionDAO aidDAO = new AidDistributionDAO();
    private FamilyDAO familyDAO = new FamilyDAO();
    private List<Family> allFamilies;

    @FXML
    public void initialize() {
        cmbAidType.setItems(FXCollections.observableArrayList("Food Parcel", "Medical Supplies", "Financial Cash", "Winter Clothes"));


        cmbFilter.setItems(FXCollections.observableArrayList("All Families (الكل)", "HIGH Vulnerability (الأكثر حاجة)", "Not Served Yet (لم يتلقوا مساعدة)"));
        cmbFilter.setValue("All Families (الكل)");

        cmbFamily.setConverter(new StringConverter<Family>() {
            @Override
            public String toString(Family family) {
                return family == null ? "" : family.getHouseholdName() + " (ID: " + family.getNationalId() + ") - " + family.getVulnerabilityLevel();
            }
            @Override
            public Family fromString(String string) { return null; }
        });


        allFamilies = familyDAO.getAllFamilies();
        cmbFamily.setItems(FXCollections.observableArrayList(allFamilies));

        colId.setCellValueFactory(new PropertyValueFactory<>("aidId"));
        colFamily.setCellValueFactory(new PropertyValueFactory<>("familyName"));
        colType.setCellValueFactory(new PropertyValueFactory<>("aidType"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("distributionDate"));

        loadTableData();
    }


    @FXML
    public void handleFilterChange(ActionEvent event) {
        String filter = cmbFilter.getValue();
        if (filter == null) return;

        List<Family> filteredList = new ArrayList<>();
        List<AidDistribution> allAids = aidDAO.getAllAid();

        for (Family f : allFamilies) {
            if (filter.contains("HIGH")) {
                if ("HIGH".equalsIgnoreCase(f.getVulnerabilityLevel())) {
                    filteredList.add(f);
                }
            } else if (filter.contains("Not Served")) {

                boolean served = false;
                for (AidDistribution aid : allAids) {
                    if (aid.getFamilyId() == f.getFamilyId()) {
                        served = true;
                        break;
                    }
                }
                if (!served) filteredList.add(f);
            } else {
                filteredList.add(f);
            }
        }

        cmbFamily.setItems(FXCollections.observableArrayList(filteredList));
        cmbFamily.getSelectionModel().clearSelection();
    }

    private void loadTableData() {
        tableAid.setItems(FXCollections.observableArrayList(aidDAO.getAllAid()));
    }

    @FXML
    public void handleDistribute(ActionEvent event) {
        if (cmbFamily.getValue() == null || cmbAidType.getValue() == null || txtQuantity.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "تنبيه", "الرجاء تعبئة جميع الحقول.");
            return;
        }

        if (!txtQuantity.getText().matches("\\d+")) {
            showAlert(Alert.AlertType.WARNING, "خطأ إدخال", "الكمية يجب أن تكون أرقاماً فقط.");
            return;
        }

        int quantity = Integer.parseInt(txtQuantity.getText());
        Family selectedFamily = cmbFamily.getValue();
        LocalDate today = LocalDate.now();

        if (!aidDAO.isEligibleForAid(selectedFamily.getFamilyId(), cmbAidType.getValue())) {
            showAlert(Alert.AlertType.ERROR, "مرفوض", "عذراً! هذه العائلة مستوى ضعفها LOW/MEDIUM واستلمت نفس نوع المساعدة (" + cmbAidType.getValue() + ") خلال الـ 30 يوماً الماضية.");
            return;
        }

        AidDistribution newAid = new AidDistribution(0, selectedFamily.getFamilyId(), selectedFamily.getHouseholdName(), cmbAidType.getValue(), quantity, today);

        if (aidDAO.addAid(newAid)) {
            showAlert(Alert.AlertType.INFORMATION, "تم بنجاح", "تم تسجيل المساعدة للعائلة بنجاح.");
            handleReset();
            loadTableData();

            handleFilterChange(null);
        } else {
            showAlert(Alert.AlertType.ERROR, "خطأ بالاتصال", "حدث خطأ في قاعدة البيانات، يرجى مراجعة شاشة IntelliJ (Run).");
        }
    }

    @FXML
    public void handleReset() {
        cmbFilter.setValue("All Families (الكل)");
        handleFilterChange(null);
        cmbFamily.setValue(null);
        cmbAidType.setValue(null);
        txtQuantity.clear();
        tableAid.getSelectionModel().clearSelection();
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