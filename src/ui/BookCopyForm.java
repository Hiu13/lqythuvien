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

    private JTextField txtMaSach, txtTenSach, txtTinhTrang, txtAnhSach, txtSoLuong;
    private JComboBox<String> cbMaDauSach;

    public BookCopyForm() {
        bookCopyService = new BookCopyService();
        bookTitleService = new BookTitleService();
        initUI();
        loadMaDauSach();
        loadData();
    }

    private void initUI() {
        setTitle("Quản lý Bản sao sách");
        setSize(950, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Table
        model = new DefaultTableModel(new Object[]{"Mã Sách", "Tên Sách", "Tình Trạng", "Mã Đầu Sách", "Ảnh Sách", "Số Lượng"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Form
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin bản sao"));

        txtMaSach = new JTextField();
        txtTenSach = new JTextField();
        txtTinhTrang = new JTextField();
        txtAnhSach = new JTextField();
        txtSoLuong = new JTextField();
        cbMaDauSach = new JComboBox<>();

        cbMaDauSach.addActionListener(e -> {
            updateTenSach();
            updateTrangThai();
        });

        formPanel.add(new JLabel("Mã Sách:"));
        formPanel.add(txtMaSach);
        formPanel.add(new JLabel("Mã Đầu Sách:"));
        formPanel.add(cbMaDauSach);
        formPanel.add(new JLabel("Tên Sách:"));
        formPanel.add(txtTenSach);
        formPanel.add(new JLabel("Tình Trạng:"));
        formPanel.add(txtTinhTrang);
        formPanel.add(new JLabel("Ảnh Sách (path):"));
        formPanel.add(txtAnhSach);
        formPanel.add(new JLabel("Số Lượng:"));
        formPanel.add(txtSoLuong);

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
        updateTenSach();
        updateTrangThai();
    }

    private void updateTenSach() {
        String maDauSach = (String) cbMaDauSach.getSelectedItem();
        if (maDauSach != null) {
            BookTitle title = bookTitleService.findById(maDauSach);
            if (title != null) {
                txtTenSach.setText(title.getTenSach());
            }
        }
    }

    private void updateTrangThai() {
        String maDauSach = (String) cbMaDauSach.getSelectedItem();
        if (maDauSach != null) {
            BookTitle title = bookTitleService.findById(maDauSach);
            if (title != null) {
                int soLuong = title.getSoLuong();
                txtTinhTrang.setText(soLuong > 0 ? "Còn" : "Không còn");
            }
        }
    }

    private void loadData() {
        model.setRowCount(0);
        List<BookCopy> list = bookCopyService.getAllBookCopies();
        for (BookCopy b : list) {
            model.addRow(new Object[]{
                    b.getMaSach(),
                    b.getTenSach(),
                    b.getTrangThai(),
                    b.getMaDauSach(),
                    b.getAnhSach(),
                    b.getSoLuong()
            });
        }
    }

    private void addBook() {
        BookCopy book = getInputBook();
        if (book == null) return;
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
        if (book == null) return;
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
            txtTenSach.setText(model.getValueAt(row, 1).toString());
            txtTinhTrang.setText(model.getValueAt(row, 2).toString());
            cbMaDauSach.setSelectedItem(model.getValueAt(row, 3).toString());
            txtAnhSach.setText(model.getValueAt(row, 4).toString());
            txtSoLuong.setText(model.getValueAt(row, 5).toString());
        }
    }

    private void clearForm() {
        txtMaSach.setText("");
        txtTinhTrang.setText("");
        txtAnhSach.setText("");
        txtSoLuong.setText("");
        cbMaDauSach.setSelectedIndex(0);
        updateTenSach();
        updateTrangThai();
    }

    private BookCopy getInputBook() {
        int soLuong;
        try {
            soLuong = Integer.parseInt(txtSoLuong.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số lượng phải là số nguyên!");
            return null;
        }

        return new BookCopy(
                txtMaSach.getText().trim(),
                txtTenSach.getText().trim(),
                txtTinhTrang.getText().trim(),
                (String) cbMaDauSach.getSelectedItem(),
                txtAnhSach.getText().trim(),
                soLuong
        );
    }
}
