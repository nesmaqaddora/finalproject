package dao;

import models.Family;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FamilyDAO {
    private Connection conn;

    public FamilyDAO() {
        conn = DatabaseConnection.getInstance().getConnection();
    }

    public boolean isNationalIdExists(String nationalId) {
        String query = "SELECT * FROM tbl_family WHERE national_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nationalId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean addFamily(Family family) {
        if (isNationalIdExists(family.getNationalId())) return false;

        String query = "INSERT INTO tbl_family (national_id, household_name, phone, location, family_size, vulnerability_level, registration_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, family.getNationalId());
            stmt.setString(2, family.getHouseholdName());
            stmt.setString(3, family.getPhone());
            stmt.setString(4, family.getLocation());
            stmt.setInt(5, family.getFamilySize());
            stmt.setString(6, family.getVulnerabilityLevel());
            stmt.setDate(7, Date.valueOf(family.getRegistrationDate()));
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean updateFamily(Family family) {
        String query = "UPDATE tbl_family SET national_id=?, household_name=?, phone=?, location=?, family_size=?, vulnerability_level=? WHERE family_id=?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, family.getNationalId());
            stmt.setString(2, family.getHouseholdName());
            stmt.setString(3, family.getPhone());
            stmt.setString(4, family.getLocation());
            stmt.setInt(5, family.getFamilySize());
            stmt.setString(6, family.getVulnerabilityLevel());
            stmt.setInt(7, family.getFamilyId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean deleteFamily(int familyId) {
        String query = "DELETE FROM tbl_family WHERE family_id=?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, familyId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public List<Family> getAllFamilies() {
        List<Family> list = new ArrayList<>();
        String query = "SELECT * FROM tbl_family";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                list.add(new Family(
                        rs.getInt("family_id"),
                        rs.getString("national_id"),
                        rs.getString("household_name"),
                        rs.getString("phone"),
                        rs.getString("location"),
                        rs.getInt("family_size"),
                        rs.getString("vulnerability_level"),
                        rs.getDate("registration_date") != null ? rs.getDate("registration_date").toLocalDate() : java.time.LocalDate.now()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}