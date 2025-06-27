package service;

import dao.BookDAO;
import model.Book;

import java.util.List;

public class BookService {
    private BookDAO dao = new BookDAO();

    public List<Book> getAllBooks() {
        return dao.getAllBooks();
    }

    public boolean addBook(Book book) {
        return dao.addBook(book);
    }

    public boolean updateBook(Book book) {
        return dao.updateBook(book);
    }

    public boolean deleteBook(String maSach) {
        return dao.deleteBook(maSach);
    }
}
