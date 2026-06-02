package dao;

import models.Organization;
import utils.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrganizationDAO {
    private Connection conn;

    public OrganizationDAO() {
        conn = DatabaseConnection.getInstance().getConnection();
    }

    public boolean addOrganization(Organization org) {
        String query = "INSERT INTO tbl_organization (name, type, contact_info) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, org.getName());
            stmt.setString(2, org.getType());
            stmt.setString(3, org.getContactInfo());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateOrganization(Organization org) {
        String query = "UPDATE tbl_organization SET name=?, type=?, contact_info=? WHERE org_id=?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, org.getName());
            stmt.setString(2, org.getType());
            stmt.setString(3, org.getContactInfo());
            stmt.setInt(4, org.getOrgId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteOrganization(int orgId) {
        String query = "DELETE FROM tbl_organization WHERE org_id=?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, orgId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Organization> getAllOrganizations() {
        List<Organization> list = new ArrayList<>();
        String query = "SELECT * FROM tbl_organization";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                list.add(new Organization(
                        rs.getInt("org_id"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getString("contact_info")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}