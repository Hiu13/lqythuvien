package dao;

import model.Borrower;
import util.DBConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BorrowerDAO {

    public boolean insert(Borrower user) {
        String sql = "INSERT INTO tb_nguoimuon (MaNguoiMuon, TenNguoiMuon, DiaChi, Gmail, SDT) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getMaNguoiMuon());
            stmt.setString(2, user.getTenNguoiMuon());
            stmt.setString(3, user.getDiaChi());
            stmt.setString(4, user.getGmail());
            stmt.setString(5, user.getSdt());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Borrower user) {
        String sql = "UPDATE tb_nguoimuon SET TenNguoiMuon=?, DiaChi=?, SDT=?, Gmail=? WHERE MaNguoiMuon=?";
        try (Connection conn = DBConnect.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getTenNguoiMuon());
            stmt.setString(2, user.getDiaChi());
            stmt.setString(3, user.getSdt());
            stmt.setString(4, user.getGmail());
            stmt.setString(5, user.getMaNguoiMuon());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(String maNguoiMuon) {
        String sql = "DELETE FROM tb_nguoimuon WHERE MaNguoiMuon=?";
        try (Connection conn = DBConnect.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maNguoiMuon);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Borrower> getAll() {
        List<Borrower> list = new ArrayList<>();
        String sql = "SELECT * FROM tb_nguoimuon";
        try (Connection conn = DBConnect.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Borrower b = new Borrower(
                        rs.getString("MaNguoiMuon"),
                        rs.getString("TenNguoiMuon"),
                        rs.getString("DiaChi"),
                        rs.getString("Gmail"),
                        rs.getString("SDT"));
                list.add(b);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Borrower findById(String maNguoiMuon) {
        String sql = "SELECT * FROM tb_nguoimuon WHERE MaNguoiMuon=?";
        try (Connection conn = DBConnect.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maNguoiMuon);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Borrower(
                        rs.getString("MaNguoiMuon"),
                        rs.getString("TenNguoiMuon"),
                        rs.getString("DiaChi"),
                        rs.getString("Gmail"),
                        rs.getString("SDT"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
