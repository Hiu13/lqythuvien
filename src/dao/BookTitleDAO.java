package dao;

import model.BookTitle;
import util.DBConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookTitleDAO {

    public boolean insert(BookTitle book) {
        String sql = "INSERT INTO tb_dausach (MaSach, TenSach, Soluong, TheLoai, TacGia, NXB, NamXB) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, book.getMaSach());
            stmt.setString(2, book.getTenSach());
            stmt.setInt(3, book.getSoLuong());
            stmt.setString(4, book.getTheLoai());
            stmt.setString(5, book.getTacGia());
            stmt.setString(6, book.getNxb());
            stmt.setInt(7, book.getNamXB());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(BookTitle book) {
        String sql = "UPDATE tb_dausach SET TenSach=?, Soluong=?, TheLoai=?, TacGia=?, NXB=?, NamXB=? WHERE MaSach=?";
        try (Connection conn = DBConnect.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, book.getTenSach());
            stmt.setInt(2, book.getSoLuong());
            stmt.setString(3, book.getTheLoai());
            stmt.setString(4, book.getTacGia());
            stmt.setString(5, book.getNxb());
            stmt.setInt(6, book.getNamXB());
            stmt.setString(7, book.getMaSach());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(String MaSach) {
        String sql = "DELETE FROM tb_dausach WHERE MaSach=?";
        try (Connection conn = DBConnect.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, MaSach);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<BookTitle> getAll() {
        List<BookTitle> list = new ArrayList<>();
        String sql = "SELECT * FROM tb_dausach";
        try (Connection conn = DBConnect.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                BookTitle book = new BookTitle(
                        rs.getString("MaSach"),
                        rs.getString("TenSach"),
                        rs.getInt("Soluong"),
                        rs.getString("TheLoai"),
                        rs.getString("TacGia"),
                        rs.getString("NXB"),
                        rs.getInt("NamXB"));
                list.add(book);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public BookTitle findById(String MaSach) {
        String sql = "SELECT * FROM tb_dausach WHERE MaSach=?";
        try (Connection conn = DBConnect.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, MaSach);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new BookTitle(
                        rs.getString("MaSach"),
                        rs.getString("TenSach"),
                        rs.getInt("Soluong"),
                        rs.getString("TheLoai"),
                        rs.getString("TacGia"),
                        rs.getString("NXB"),
                        rs.getInt("NamXB"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean capNhatSoLuong(String MaSach) {
        String sql = "UPDATE tb_dausach SET Soluong = (SELECT COUNT(*) FROM tb_sach WHERE MaDauSach = ?) WHERE MaSach = ?";
        try (Connection conn = DBConnect.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, MaSach);
            stmt.setString(2, MaSach);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
