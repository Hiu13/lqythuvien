package dao;

import model.LoanSlip;
import util.DBConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class thao tác với bảng tb_phieumuon.
 */
public class LoanSlipDAO {public boolean borrowBook(String maNguoiMuon, String maSach, String ngayMuon) {
    String sql = "INSERT INTO tb_phieumuon (MaNguoiMuon, MaSach, NgayMuon) VALUES (?, ?, ?)";
    try (Connection conn = DBConnect.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, maNguoiMuon);
        stmt.setString(2, maSach);
        stmt.setString(3, ngayMuon);

        return stmt.executeUpdate() > 0;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}


    public boolean insert(LoanSlip slip) {
        String sql = "INSERT INTO tb_phieumuon (MaPhieuMuon, NgayMuon, HanTra, MaSach, MaNguoiMuon, NgayTra) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setString(1, slip.getMaPhieuMuon());
            stmt.setDate(2, new java.sql.Date(slip.getNgayMuon().getTime()));
            stmt.setDate(3, new java.sql.Date(slip.getHanTra().getTime()));
            stmt.setString(4, slip.getMaSach());
            stmt.setString(5, slip.getMaNguoiMuon());
            if (slip.getNgayTra() != null)
                stmt.setDate(6, new java.sql.Date(slip.getNgayTra().getTime()));
            else
                stmt.setNull(6, Types.DATE);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(LoanSlip slip) {
        String sql = "UPDATE tb_phieumuon SET NgayMuon=?, HanTra=?, MaSach=?, MaNguoiMuon=?, NgayTra=? WHERE MaPhieuMuon=?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setDate(1, new java.sql.Date(slip.getNgayMuon().getTime()));
            stmt.setDate(2, new java.sql.Date(slip.getHanTra().getTime()));
            stmt.setString(3, slip.getMaSach());
            stmt.setString(4, slip.getMaNguoiMuon());
            if (slip.getNgayTra() != null)
                stmt.setDate(5, new java.sql.Date(slip.getNgayTra().getTime()));
            else
                stmt.setNull(5, Types.DATE);
            stmt.setString(6, slip.getMaPhieuMuon());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(String maPhieuMuon) {
        String sql = "DELETE FROM tb_phieumuon WHERE MaPhieuMuon=?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setString(1, maPhieuMuon);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<LoanSlip> getAll() {
        List<LoanSlip> list = new ArrayList<>();
        String sql = "SELECT * FROM tb_phieumuon";
        try (Connection conn = DBConnect.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
             
            while (rs.next()) {
                LoanSlip l = new LoanSlip(
                    rs.getString("MaPhieuMuon"),
                    rs.getDate("NgayMuon"),
                    rs.getDate("HanTra"),
                    rs.getString("MaSach"),
                    rs.getString("MaNguoiMuon"),
                    rs.getDate("NgayTra")
                );
                list.add(l);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public LoanSlip findById(String maPhieuMuon) {
        String sql = "SELECT * FROM tb_phieumuon WHERE MaPhieuMuon=?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setString(1, maPhieuMuon);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new LoanSlip(
                    rs.getString("MaPhieuMuon"),
                    rs.getDate("NgayMuon"),
                    rs.getDate("HanTra"),
                    rs.getString("MaSach"),
                    rs.getString("MaNguoiMuon"),
                    rs.getDate("NgayTra")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

