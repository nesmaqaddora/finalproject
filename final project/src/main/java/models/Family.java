package models;

import java.time.LocalDate;

public class Family {
    private int familyId;
    private String nationalId;
    private String householdName;
    private String phone;
    private String location;
    private int familySize;
    private String vulnerabilityLevel;
    private LocalDate registrationDate;

    public Family(int familyId, String nationalId, String householdName, String phone, String location, int familySize, String vulnerabilityLevel, LocalDate registrationDate) {
        this.familyId = familyId;
        this.nationalId = nationalId;
        this.householdName = householdName;
        this.phone = phone;
        this.location = location;
        this.familySize = familySize;
        this.vulnerabilityLevel = vulnerabilityLevel;
        this.registrationDate = registrationDate;
    }

    public int getFamilyId() { return familyId; }
    public void setFamilyId(int familyId) { this.familyId = familyId; }

    public String getNationalId() { return nationalId; }
    public void setNationalId(String nationalId) { this.nationalId = nationalId; }

    public String getHouseholdName() { return householdName; }
    public void setHouseholdName(String householdName) { this.householdName = householdName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public int getFamilySize() { return familySize; }
    public void setFamilySize(int familySize) { this.familySize = familySize; }

    public String getVulnerabilityLevel() { return vulnerabilityLevel; }
    public void setVulnerabilityLevel(String vulnerabilityLevel) { this.vulnerabilityLevel = vulnerabilityLevel; }

    public LocalDate getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; }
}