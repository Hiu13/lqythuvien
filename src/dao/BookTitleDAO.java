package dao;

import model.BookTitle;
import util.DBConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class thao tác với bảng tb_dausach.
 */
public class BookTitleDAO {

    // Thêm một đầu sách mới
    public boolean insert(BookTitle book) {
        String sql = "INSERT INTO tb_dausach (MaDauSach, TenSach, Soluong, TheLoai, TacGia, NXB, NamXB) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, book.getMaDauSach());
            stmt.setString(2, book.getTenSach());
            stmt.setInt(3, book.getSoLuong());
            stmt.setString(4, book.getTheLoai());
            stmt.setString(5, book.getTacGia());
            stmt.setString(6, book.getNxb());
            stmt.setInt(7, book.getNamXB());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace(); // có thể log ra file sau này
        }
        return false;
    }

    // Cập nhật đầu sách
    public boolean update(BookTitle book) {
        String sql = "UPDATE tb_dausach SET TenSach=?, Soluong=?, TheLoai=?, TacGia=?, NXB=?, NamXB=? WHERE MaDauSach=?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, book.getTenSach());
            stmt.setInt(2, book.getSoLuong());
            stmt.setString(3, book.getTheLoai());
            stmt.setString(4, book.getTacGia());
            stmt.setString(5, book.getNxb());
            stmt.setInt(6, book.getNamXB());
            stmt.setString(7, book.getMaDauSach());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xoá đầu sách theo mã
    public boolean delete(String maDauSach) {
        String sql = "DELETE FROM tb_dausach WHERE MaDauSach=?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maDauSach);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Lấy toàn bộ đầu sách
    public List<BookTitle> getAll() {
        List<BookTitle> list = new ArrayList<>();
        String sql = "SELECT * FROM tb_dausach";
        try (Connection conn = DBConnect.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                BookTitle book = new BookTitle(
                        rs.getString("MaDauSach"),
                        rs.getString("TenSach"),
                        rs.getInt("Soluong"),
                        rs.getString("TheLoai"),
                        rs.getString("TacGia"),
                        rs.getString("NXB"),
                        rs.getInt("NamXB")
                );
                list.add(book);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Tìm đầu sách theo mã
    public BookTitle findById(String maDauSach) {
        String sql = "SELECT * FROM tb_dausach WHERE MaDauSach=?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maDauSach);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new BookTitle(
                        rs.getString("MaDauSach"),
                        rs.getString("TenSach"),
                        rs.getInt("Soluong"),
                        rs.getString("TheLoai"),
                        rs.getString("TacGia"),
                        rs.getString("NXB"),
                        rs.getInt("NamXB")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Cập nhật số lượng dựa theo bảng bản sao sách
    public boolean capNhatSoLuong(String maDauSach) {
        String sql = "UPDATE tb_dausach SET Soluong = (SELECT COUNT(*) FROM tb_sach WHERE MaDauSach = ?) WHERE MaDauSach = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maDauSach);
            stmt.setString(2, maDauSach);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}