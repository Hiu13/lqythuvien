package service;

import dao.LoanSlipDAO;
import model.LoanSlip;
import java.util.List;
import java.util.Optional;

public class LoanSlipService {
    private final LoanSlipDAO loanSlipDAO;

    public LoanSlipService() {
        this.loanSlipDAO = new LoanSlipDAO();
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
