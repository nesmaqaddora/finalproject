package dao;

import models.AidDistribution;
import utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AidDistributionDAO {
    private Connection conn;

    public AidDistributionDAO() {
        conn = DatabaseConnection.getInstance().getConnection();
    }


    public boolean isEligibleForAid(int familyId, String aidType) {
        String vulnerabilityLevel = "LOW";


        String vulnQuery = "SELECT vulnerability_level FROM tbl_family WHERE family_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(vulnQuery)) {
            stmt.setInt(1, familyId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                vulnerabilityLevel = rs.getString("vulnerability_level");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if ("HIGH".equalsIgnoreCase(vulnerabilityLevel)) {
            return true;
        }


        String query = "SELECT MAX(distribution_date) as last_date FROM tbl_aid_distribution WHERE family_id = ? AND aid_type = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, familyId);
            stmt.setString(2, aidType);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Date lastDate = rs.getDate("last_date");
                if (lastDate != null) {
                    LocalDate lastAidDate = lastDate.toLocalDate();
                    LocalDate today = LocalDate.now();
                    return lastAidDate.plusDays(30).isBefore(today) || lastAidDate.plusDays(30).isEqual(today);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean addAid(AidDistribution aid) {

        if (!isEligibleForAid(aid.getFamilyId(), aid.getAidType())) return false;

        String query = "INSERT INTO tbl_aid_distribution (family_id, aid_type, quantity, distribution_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, aid.getFamilyId());
            stmt.setString(2, aid.getAidType());
            stmt.setInt(3, aid.getQuantity());
            stmt.setDate(4, Date.valueOf(aid.getDistributionDate()));
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<AidDistribution> getAllAid() {
        List<AidDistribution> list = new ArrayList<>();
        String query = "SELECT a.aid_id, a.family_id, f.household_name, a.aid_type, a.quantity, a.distribution_date " +
                "FROM tbl_aid_distribution a JOIN tbl_family f ON a.family_id = f.family_id";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                list.add(new AidDistribution(
                        rs.getInt("aid_id"),
                        rs.getInt("family_id"),
                        rs.getString("household_name"),
                        rs.getString("aid_type"),
                        rs.getInt("quantity"),
                        rs.getDate("distribution_date").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}