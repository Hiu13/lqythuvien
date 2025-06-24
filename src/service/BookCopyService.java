package service;

import dao.BookCopyDAO;
import model.BookCopy;
import java.util.List;
import java.util.Optional;

public class BookCopyService {
    private final BookCopyDAO bookCopyDAO;

    public BookCopyService() {
        this.bookCopyDAO = new BookCopyDAO();
    }

    public boolean addBookCopy(BookCopy bookCopy) {
        if (bookCopy == null || bookCopy.getMaSach().isEmpty()) {
            return false;
        }
        if (bookCopyDAO.findById(bookCopy.getMaSach()) != null) {
            System.out.println("Book copy already exists.");
            return false;
        }
        return bookCopyDAO.insert(bookCopy);
    }

    public boolean updateBookCopy(BookCopy bookCopy) {
        if (bookCopy == null || bookCopy.getMaSach().isEmpty()) {
            return false;
        }
        return bookCopyDAO.update(bookCopy);
    }

    public boolean deleteBookCopy(String maSach) {
        if (maSach == null || maSach.isEmpty()) {
            return false;
        }
        return bookCopyDAO.delete(maSach);
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
