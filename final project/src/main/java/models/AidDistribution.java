package models;

import java.time.LocalDate;

public class AidDistribution {
    private int aidId;
    private int familyId;
    private String familyName;
    private String aidType;
    private int quantity;
    private LocalDate distributionDate;

    public AidDistribution(int aidId, int familyId, String familyName, String aidType, int quantity, LocalDate distributionDate) {
        this.aidId = aidId;
        this.familyId = familyId;
        this.familyName = familyName;
        this.aidType = aidType;
        this.quantity = quantity;
        this.distributionDate = distributionDate;
    }

    public int getAidId() { return aidId; }
    public void setAidId(int aidId) { this.aidId = aidId; }

    public int getFamilyId() { return familyId; }
    public void setFamilyId(int familyId) { this.familyId = familyId; }

    public String getFamilyName() { return familyName; }
    public void setFamilyName(String familyName) { this.familyName = familyName; }

    public String getAidType() { return aidType; }
    public void setAidType(String aidType) { this.aidType = aidType; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public LocalDate getDistributionDate() { return distributionDate; }
    public void setDistributionDate(LocalDate distributionDate) { this.distributionDate = distributionDate; }
}