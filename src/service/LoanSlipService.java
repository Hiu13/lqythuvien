package service;

import dao.BookCopyDAO;
import dao.LoanSlipDAO;
import model.LoanSlip;

import javax.swing.JOptionPane;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Date;

public class LoanSlipService {
    private final LoanSlipDAO loanSlipDAO;
    private final BookCopyDAO bookCopyDAO;

    public LoanSlipService() {
        this.loanSlipDAO = new LoanSlipDAO();
        this.bookCopyDAO = new BookCopyDAO(); // Khởi tạo BookCopyDAO
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

        // Trừ số lượng sách
        boolean success = bookCopyDAO.giamSoLuong(maSach);
        if (!success) return false;

        // ✅ Nếu số lượng sau khi trừ = 0 → cập nhật trạng thái "Hết"
        int soLuongConLai = bookCopyDAO.getSoLuongSach(maSach);
        if (soLuongConLai == 0) {
            bookCopyDAO.updateTrangThai(maSach, "Hết");
        }

        // Tạo mã phiếu mượn tự động
        String maPhieuMuon = "PM" + System.currentTimeMillis();

        // Thêm phiếu mượn
        return loanSlipDAO.insert(new LoanSlip(
            maPhieuMuon,
            new Date(),                              // ngày mượn
            new Date(System.currentTimeMillis() + 14L * 24 * 60 * 60 * 1000), // hạn trả
            maSach,
            maNguoiMuon,
            null
        ));
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
                .filter(p -> p.getNgayTra() == null) // hoặc kiểm tra ngày hiện tại > hạn trả
                .toList();
    }
}
