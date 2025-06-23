package ui;

import model.BookCopy;
import model.BookTitle;
import service.BookCopyService;
import service.BookTitleService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BookCopyForm extends JFrame {
    private final BookCopyService bookCopyService;
    private final BookTitleService bookTitleService;

    private JTable table;
    private DefaultTableModel model;

    private JTextField txtMaSach, txtTinhTrang, txtTenSach;
    private JComboBox<String> cbMaDauSach;

    public BookCopyForm() {
        bookCopyService = new BookCopyService();
        bookTitleService = new BookTitleService();
        initUI();
        loadData();
        loadMaDauSach();
    }

    private void initUI() {
        setTitle("Quản lý Bản sao sách");
        setSize(750, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Table
        model = new DefaultTableModel(new Object[]{"Mã Sách", "Mã Đầu Sách", "Tình Trạng", "Tên Sách"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Form
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin bản sao"));

        txtMaSach = new JTextField();
        cbMaDauSach = new JComboBox<>();
        txtTinhTrang = new JTextField();
        txtTenSach = new JTextField();

        formPanel.add(new JLabel("Mã Sách:"));
        formPanel.add(txtMaSach);
        formPanel.add(new JLabel("Mã Đầu Sách:"));
        formPanel.add(cbMaDauSach);
        formPanel.add(new JLabel("Tình Trạng:"));
        formPanel.add(txtTinhTrang);
        formPanel.add(new JLabel("Tên Sách:"));
        formPanel.add(txtTenSach);


        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnAdd = new JButton("Thêm");
        JButton btnUpdate = new JButton("Sửa");
        JButton btnDelete = new JButton("Xoá");
        JButton btnRefresh = new JButton("Làm mới");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);

        // Events
        btnAdd.addActionListener(e -> addBook());
        btnUpdate.addActionListener(e -> updateBook());
        btnDelete.addActionListener(e -> deleteBook());
        btnRefresh.addActionListener(e -> loadData());
        table.getSelectionModel().addListSelectionListener(e -> loadSelectedRow());

        // Layout
        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadMaDauSach() {
        cbMaDauSach.removeAllItems();
        List<BookTitle> titles = bookTitleService.getAllBookTitles();
        for (BookTitle t : titles) {
            cbMaDauSach.addItem(t.getMaDauSach());
        }
    }

    private void loadData() {
        model.setRowCount(0);
        List<BookCopy> list = bookCopyService.getAllBookCopies();
        for (BookCopy b : list) {
            model.addRow(new Object[]{
                    b.getMaSach(),
                    b.getMaDauSach(),
                    b.getTrangThai(),
                    b.getTenSach()
            });
        }
    }

    private void addBook() {
        BookCopy book = getInputBook();
        if (bookCopyService.addBookCopy(book)) {
            JOptionPane.showMessageDialog(this, "Thêm thành công");
            loadData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại");
        }
    }

    private void updateBook() {
        BookCopy book = getInputBook();
        if (bookCopyService.updateBookCopy(book)) {
            JOptionPane.showMessageDialog(this, "Cập nhật thành công");
            loadData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại");
        }
    }

    private void deleteBook() {
        String maSach = txtMaSach.getText().trim();
        if (bookCopyService.deleteBookCopy(maSach)) {
            JOptionPane.showMessageDialog(this, "Xoá thành công");
            loadData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Xoá thất bại");
        }
    }

    private void loadSelectedRow() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            txtMaSach.setText(model.getValueAt(row, 0).toString());
            cbMaDauSach.setSelectedItem(model.getValueAt(row, 1).toString());
            txtTinhTrang.setText(model.getValueAt(row, 2).toString());
            txtTenSach.setText(model.getValueAt(row, 3).toString());
        }
    }

    private void clearForm() {
        txtMaSach.setText("");
        txtTinhTrang.setText("");
        cbMaDauSach.setSelectedIndex(0);
    }

    private BookCopy getInputBook() {
    return new BookCopy(
            txtMaSach.getText().trim(),
            txtTinhTrang.getText().trim(),
            (String) cbMaDauSach.getSelectedItem(),
            txtTenSach.getText().trim()
    );
}
}
