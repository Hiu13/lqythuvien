package service;

import dao.BorrowerDAO;
import model.Borrower;
import java.util.List;
import java.util.Optional;

public class BorrowerService {
    private final BorrowerDAO borrowerDAO;

    public BorrowerService() {
        this.borrowerDAO = new BorrowerDAO();
    }

    public boolean addBorrower(Borrower borrower) {
        if (borrower == null || borrower.getMaNguoiMuon().isEmpty()) {
            return false;
        }
        if (borrowerDAO.findById(borrower.getMaNguoiMuon()) != null) {
            System.out.println("Borrower already exists.");
            return false;
        }
        return borrowerDAO.insert(borrower);
    }

    public boolean updateBorrower(Borrower borrower) {
        if (borrower == null || borrower.getMaNguoiMuon().isEmpty()) {
            return false;
        }
        return borrowerDAO.update(borrower);
    }

    public boolean deleteBorrower(String maNguoiMuon) {
        if (maNguoiMuon == null || maNguoiMuon.isEmpty()) {
            return false;
        }
        return borrowerDAO.delete(maNguoiMuon);
    }

    public Optional<Borrower> getBorrowerById(String maNguoiMuon) {
        return Optional.ofNullable(borrowerDAO.findById(maNguoiMuon));
    }

    public List<Borrower> getAllBorrowers() {
        return borrowerDAO.getAll();
    }

    public List<Borrower> searchBorrowersByName(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return getAllBorrowers();
        }
        return borrowerDAO.getAll().stream()
                .filter(b -> b.getTenNguoiMuon().toLowerCase().contains(keyword.toLowerCase()))
                .toList();
    }
}
