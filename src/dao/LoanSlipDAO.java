package dao;

import model.LoanSlip;
import util.DBConnect;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LoanSlipDAO {
    public boolean insert(LoanSlip loanSlip) {
        String sql = "INSERT INTO tb_phieumuon (MaPhieuMuon, NgayMuon, HanTra, MaSach, MaNguoiMuon, NgayTra) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, loanSlip.getMaPhieuMuon());
            stmt.setDate(2, new java.sql.Date(loanSlip.getNgayMuon().getTime()));
            stmt.setDate(3, new java.sql.Date(loanSlip.getHanTra().getTime()));
            stmt.setString(4, loanSlip.getMaSach());
            stmt.setString(5, loanSlip.getMaNguoiMuon());
            if (loanSlip.getNgayTra() != null) {
                stmt.setDate(6, new java.sql.Date(loanSlip.getNgayTra().getTime()));
            } else {
                stmt.setNull(6, Types.DATE);
            }
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(LoanSlip loanSlip) {
        String sql = "UPDATE tb_phieumuon SET NgayMuon=?, HanTra=?, MaSach=?, MaNguoiMuon=?, NgayTra=? WHERE MaPhieuMuon=?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, new java.sql.Date(loanSlip.getNgayMuon().getTime()));
            stmt.setDate(2, new java.sql.Date(loanSlip.getHanTra().getTime()));
            stmt.setString(3, loanSlip.getMaSach());
            stmt.setString(4, loanSlip.getMaNguoiMuon());
            if (loanSlip.getNgayTra() != null) {
                stmt.setDate(5, new java.sql.Date(loanSlip.getNgayTra().getTime()));
            } else {
                stmt.setNull(5, Types.DATE);
            }
            stmt.setString(6, loanSlip.getMaPhieuMuon());
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

    public List<LoanSlip> getAll() {
        List<LoanSlip> list = new ArrayList<>();
        String sql = "SELECT * FROM tb_phieumuon";
        try (Connection conn = DBConnect.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                LoanSlip loanSlip = new LoanSlip(
                        rs.getString("MaPhieuMuon"),
                        rs.getDate("NgayMuon"),
                        rs.getDate("HanTra"),
                        rs.getString("MaSach"),
                        rs.getString("MaNguoiMuon"),
                        rs.getDate("NgayTra")
                );
                list.add(loanSlip);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean borrowBook(String maNguoiMuon, String maSach, String ngayMuonStr) {
        try {
            java.util.Date ngayMuon = java.sql.Date.valueOf(ngayMuonStr);
            java.util.Date hanTra = new java.util.Date(ngayMuon.getTime() + 14L * 24 * 60 * 60 * 1000);
            String maPhieuMuon = "PM" + System.currentTimeMillis();
            LoanSlip loanSlip = new LoanSlip(maPhieuMuon, ngayMuon, hanTra, maSach, maNguoiMuon, null);
            return insert(loanSlip);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Cập nhật ngày trả khi trả sách
public boolean updateNgayTra(String maPhieuMuon, Date ngayTra) {
    String sql = "UPDATE tb_phieumuon SET NgayTra = ? WHERE MaPhieuMuon = ?";
    try (Connection conn = DBConnect.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setDate(1, new java.sql.Date(ngayTra.getTime()));
        stmt.setString(2, maPhieuMuon);

        return stmt.executeUpdate() > 0;

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

}
