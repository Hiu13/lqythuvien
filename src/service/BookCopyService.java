package service;

import dao.BookCopyDAO;
import dao.BookTitleDAO;
import model.BookCopy;

import java.util.List;
import java.util.Optional;

public class BookCopyService {
    private final BookCopyDAO bookCopyDAO;
    private final BookTitleDAO bookTitleDAO;

    public BookCopyService() {
        this.bookCopyDAO = new BookCopyDAO();
        this.bookTitleDAO = new BookTitleDAO();
    }

    public boolean addBookCopy(BookCopy bookCopy) {
        if (bookCopy == null || bookCopy.getMaSach().isEmpty()) {
            return false;
        }
        if (bookCopyDAO.findById(bookCopy.getMaSach()) != null) {
            System.out.println("Book copy already exists.");
            return false;
        }
        boolean result = bookCopyDAO.insert(bookCopy);
        if (result) {
            bookTitleDAO.capNhatSoLuong(bookCopy.getMaDauSach());
        }
        return result;
    }

    public boolean updateBookCopy(BookCopy bookCopy) {
        if (bookCopy == null || bookCopy.getMaSach().isEmpty()) {
            return false;
        }
        boolean result = bookCopyDAO.update(bookCopy);
        if (result) {
            bookTitleDAO.capNhatSoLuong(bookCopy.getMaDauSach());
        }
        return result;
    }

    public boolean deleteBookCopy(String maSach) {
        if (maSach == null || maSach.isEmpty()) {
            return false;
        }
        BookCopy existing = bookCopyDAO.findById(maSach);
        boolean result = bookCopyDAO.delete(maSach);
        if (result && existing != null) {
            bookTitleDAO.capNhatSoLuong(existing.getMaDauSach());
        }
        return result;
    }
    
    public int getSoLuongSach(String maSach) {
        return bookCopyDAO.getSoLuongSach(maSach);
    }

    public boolean giamSoLuong(String maSach) {
        return bookCopyDAO.giamSoLuong(maSach);
    }

    public Optional<BookCopy> getBookCopyById(String maSach) {
        return Optional.ofNullable(bookCopyDAO.findById(maSach));
    }

    public List<BookCopy> getAllBookCopies() {
        return bookCopyDAO.getAll();
    }

    public List<BookCopy> getBookCopiesByTitleId(String maDauSach) {
        return bookCopyDAO.getAll().stream()
                .filter(b -> b.getMaDauSach().equalsIgnoreCase(maDauSach))
                .toList();
    }

    public List<BookCopy> getAvailableCopies() {
        return bookCopyDAO.getAll().stream()
                .filter(b -> b.getTrangThai().equalsIgnoreCase("available"))
                .toList();
    }
}
