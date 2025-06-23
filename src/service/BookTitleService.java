package service;

import dao.BookTitleDAO;
import model.BookTitle;
import java.util.List;
import java.util.Optional;

public class BookTitleService {
    private final BookTitleDAO bookTitleDAO;

    public BookTitleService() {
        this.bookTitleDAO = new BookTitleDAO();
    }

    public boolean addBookTitle(BookTitle bookTitle) {
        if (bookTitle == null || bookTitle.getMaDauSach().isEmpty()) {
            return false;
        }
        if (bookTitleDAO.findById(bookTitle.getMaDauSach()) != null) {
            System.out.println("Book title already exists.");
            return false;
        }
        return bookTitleDAO.insert(bookTitle);
    }

    public boolean updateBookTitle(BookTitle bookTitle) {
        if (bookTitle == null || bookTitle.getMaDauSach().isEmpty()) {
            return false;
        }
        return bookTitleDAO.update(bookTitle);
    }

    public boolean deleteBookTitle(String maDauSach) {
        if (maDauSach == null || maDauSach.isEmpty()) {
            return false;
        }
        return bookTitleDAO.delete(maDauSach);
    }

    public Optional<BookTitle> getBookTitleById(String maDauSach) {
        return Optional.ofNullable(bookTitleDAO.findById(maDauSach));
    }

    public List<BookTitle> getAllBookTitles() {
        return bookTitleDAO.getAll();
    }

    public List<BookTitle> searchBookTitles(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return getAllBookTitles();
        }
        return getAllBookTitles().stream()
                .filter(b -> b.getMaDauSach().toLowerCase().contains(keyword.toLowerCase()))
                .toList();
    }
}
