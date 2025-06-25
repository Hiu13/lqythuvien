package dao;

import model.BookCopy;
import util.DBConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookCopyDAO {

    public boolean insert(BookCopy copy) {
        String sql = "INSERT INTO tb_sach (MaSach, TenSach, TrangThai, MaDauSach, AnhSach, SoLuong) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, copy.getMaSach());
            stmt.setString(2, copy.getTenSach());
            stmt.setString(3, copy.getTrangThai());
            stmt.setString(4, copy.getMaDauSach());
            stmt.setString(5, copy.getAnhSach());
            stmt.setInt(6, copy.getSoLuong());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(BookCopy copy) {
        String sql = "UPDATE tb_sach SET TenSach=?, TrangThai=?, MaDauSach=?, AnhSach=?, SoLuong=? WHERE MaSach=?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, copy.getTenSach());
            stmt.setString(2, copy.getTrangThai());
            stmt.setString(3, copy.getMaDauSach());
            stmt.setString(4, copy.getAnhSach());
            stmt.setInt(5, copy.getSoLuong());
            stmt.setString(6, copy.getMaSach());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(String maSach) {
        String sql = "DELETE FROM tb_sach WHERE MaSach=?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maSach);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<BookCopy> getAll() {
        List<BookCopy> list = new ArrayList<>();
        String sql = "SELECT * FROM tb_sach";
        try (Connection conn = DBConnect.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                BookCopy copy = new BookCopy(
                        rs.getString("MaSach"),
                        rs.getString("TenSach"),
                        rs.getString("TrangThai"),
                        rs.getString("MaDauSach"),
                        rs.getString("AnhSach"),
                        rs.getInt("SoLuong")
                );
                list.add(copy);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int getSoLuongSach(String maSach) {
        String sql = "SELECT SoLuong FROM tb_sach WHERE MaSach = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maSach);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("SoLuong");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean giamSoLuong(String maSach) {
        String sql = "UPDATE tb_sach SET SoLuong = SoLuong - 1 WHERE MaSach = ? AND SoLuong > 0";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maSach);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ✅ Hàm thêm để cập nhật trạng thái sách (dùng khi sách hết số lượng)
    public boolean updateTrangThai(String maSach, String trangThai) {
        String sql = "UPDATE tb_sach SET TrangThai = ? WHERE MaSach = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, trangThai);
            stmt.setString(2, maSach);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public BookCopy findById(String maSach) {
        String sql = "SELECT * FROM tb_sach WHERE MaSach=?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maSach);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new BookCopy(
                        rs.getString("MaSach"),
                        rs.getString("TenSach"),
                        rs.getString("TrangThai"),
                        rs.getString("MaDauSach"),
                        rs.getString("AnhSach"),
                        rs.getInt("SoLuong")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
