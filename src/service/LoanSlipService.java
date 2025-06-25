package service;

import dao.BookCopyDAO;
import dao.LoanSlipDAO;
import model.BookCopy;
import model.LoanSlip;

import javax.swing.*;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class LoanSlipService {
    private final LoanSlipDAO loanSlipDAO;
    private final BookCopyDAO bookCopyDAO;

    public LoanSlipService() {
        this.loanSlipDAO = new LoanSlipDAO();
        this.bookCopyDAO = new BookCopyDAO();
    }

    public boolean addLoanSlip(LoanSlip loanSlip) {
        if (loanSlip == null || loanSlip.getMaPhieuMuon().isEmpty()) {
            return false;
        }
        if (loanSlipDAO.findById(loanSlip.getMaPhieuMuon()) != null) {
            System.out.println("Loan slip already exists.");
            return false;
        }
        return loanSlipDAO.insert(loanSlip);
    }

    public boolean muonSach(String maNguoiMuon, String maSach) {
        int soLuong = bookCopyDAO.getSoLuongSach(maSach);
        if (soLuong <= 0) {
            JOptionPane.showMessageDialog(null, "Sách này đã hết! Không thể mượn.");
            return false;
        }

        // Giảm số lượng sách
        boolean success = bookCopyDAO.giamSoLuong(maSach);
        if (!success) return false;

        // Cập nhật trạng thái nếu số lượng về 0
        int conLai = bookCopyDAO.getSoLuongSach(maSach);
        if (conLai == 0) {
            bookCopyDAO.updateTrangThai(maSach, "Hết");
        }

        // Cập nhật lại số lượng đầu sách
        BookCopy sach = bookCopyDAO.findById(maSach);
        if (sach != null) {
            bookCopyDAO.capNhatSoLuongDauSach(sach.getMaDauSach());
        }

        // Tạo mã phiếu
        String maPhieu = "PM" + System.currentTimeMillis();
        Date ngayMuon = new Date();
        Date hanTra = new Date(System.currentTimeMillis() + 14L * 24 * 60 * 60 * 1000);

        LoanSlip phieu = new LoanSlip(maPhieu, ngayMuon, hanTra, maSach, maNguoiMuon, null);
        return loanSlipDAO.insert(phieu);
    }

    public boolean traSach(String maPhieuMuon) {
        LoanSlip phieu = loanSlipDAO.findById(maPhieuMuon);
        if (phieu == null || phieu.getNgayTra() != null) {
            return false;
        }

        String maSach = phieu.getMaSach();
        boolean success = bookCopyDAO.tangSoLuong(maSach);
        if (!success) return false;

        // Nếu sách được trả lại và số lượng > 0 → cập nhật trạng thái
        bookCopyDAO.updateTrangThai(maSach, "Còn");

        // Cập nhật lại số lượng trong đầu sách
        BookCopy sach = bookCopyDAO.findById(maSach);
        if (sach != null) {
            bookCopyDAO.capNhatSoLuongDauSach(sach.getMaDauSach());
        }

        // Cập nhật ngày trả
        return loanSlipDAO.updateNgayTra(maPhieuMuon, new Date());
    }

    public boolean updateLoanSlip(LoanSlip loanSlip) {
        if (loanSlip == null || loanSlip.getMaPhieuMuon().isEmpty()) {
            return false;
        }
        return loanSlipDAO.update(loanSlip);
    }

    public boolean deleteLoanSlip(String maPhieuMuon) {
        if (maPhieuMuon == null || maPhieuMuon.isEmpty()) {
            return false;
        }
        return loanSlipDAO.delete(maPhieuMuon);
    }

    public Optional<LoanSlip> getLoanSlipById(String maPhieuMuon) {
        return Optional.ofNullable(loanSlipDAO.findById(maPhieuMuon));
    }

    public List<LoanSlip> getAllLoanSlips() {
        return loanSlipDAO.getAll();
    }

    public List<LoanSlip> getLoanSlipsByBorrower(String maNguoiMuon) {
        return loanSlipDAO.getAll().stream()
                .filter(p -> p.getMaNguoiMuon().equalsIgnoreCase(maNguoiMuon))
                .toList();
    }

    public List<LoanSlip> getOverdueLoanSlips() {
        return loanSlipDAO.getAll().stream()
                .filter(p -> p.getNgayTra() == null) // hoặc kiểm tra hạn trả < ngày hiện tại
                .toList();
    }
}
