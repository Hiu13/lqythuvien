package ui;

import model.BookTitle;
import service.BookTitleService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BookTitleForm extends JFrame {
    private final BookTitleService bookTitleService;
    private JTable table;
    private DefaultTableModel model;

    private JTextField txtMaDauSach, txtTenSach, txtSoLuong, txtTheLoai, txtTacGia, txtNhaXuatBan, txtNamXB;

    public BookTitleForm() {
        bookTitleService = new BookTitleService();
        initUI();
        loadData();
    }

    private void initUI() {
        setTitle("Quản lý Đầu sách");
        setSize(950, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Bảng
        model = new DefaultTableModel(
                new Object[]{"Mã", "Tên", "Số lượng", "Trạng thái", "Thể loại", "Tác giả", "NXB", "Năm XB"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Form nhập liệu
        JPanel formPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin đầu sách"));

        txtMaDauSach = new JTextField();
        txtTenSach = new JTextField();
        txtSoLuong = new JTextField();
        txtTheLoai = new JTextField();
        txtTacGia = new JTextField();
        txtNhaXuatBan = new JTextField();
        txtNamXB = new JTextField();

        formPanel.add(new JLabel("Mã đầu sách:"));
        formPanel.add(txtMaDauSach);
        formPanel.add(new JLabel("Tên sách:"));
        formPanel.add(txtTenSach);
        formPanel.add(new JLabel("Số lượng:"));
        formPanel.add(txtSoLuong);
        formPanel.add(new JLabel("Thể loại:"));
        formPanel.add(txtTheLoai);
        formPanel.add(new JLabel("Tác giả:"));
        formPanel.add(txtTacGia);
        formPanel.add(new JLabel("Nhà xuất bản:"));
        formPanel.add(txtNhaXuatBan);
        formPanel.add(new JLabel("Năm xuất bản:"));
        formPanel.add(txtNamXB);

        // Nút chức năng
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnAdd = new JButton("Thêm");
        JButton btnUpdate = new JButton("Sửa");
        JButton btnDelete = new JButton("Xoá");
        JButton btnRefresh = new JButton("Làm mới");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);

        // Gắn sự kiện
        btnAdd.addActionListener(e -> addBookTitle());
        btnUpdate.addActionListener(e -> updateBookTitle());
        btnDelete.addActionListener(e -> deleteBookTitle());
        btnRefresh.addActionListener(e -> loadData());
        table.getSelectionModel().addListSelectionListener(e -> loadSelectedRow());

        // Gắn vào frame
        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadData() {
        model.setRowCount(0);
        List<BookTitle> list = bookTitleService.getAllBookTitles();
        for (BookTitle b : list) {
            String trangThai = b.getSoLuong() > 0 ? "Còn" : "Hết";
            model.addRow(new Object[]{
                    b.getMaDauSach(),
                    b.getTenSach(),
                    b.getSoLuong(),
                    trangThai,
                    b.getTheLoai(),
                    b.getTacGia(),
                    b.getNxb(),
                    b.getNamXB()
            });
        }
    }

    private void addBookTitle() {
        BookTitle book = getInputBookTitle();
        if (bookTitleService.addBookTitle(book)) {
            JOptionPane.showMessageDialog(this, "Thêm đầu sách thành công");
            loadData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại");
        }
    }

    private void updateBookTitle() {
        BookTitle book = getInputBookTitle();
        if (bookTitleService.updateBookTitle(book)) {
            JOptionPane.showMessageDialog(this, "Cập nhật thành công");
            loadData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại");
        }
    }

    private void deleteBookTitle() {
        String ma = txtMaDauSach.getText().trim();
        if (bookTitleService.deleteBookTitle(ma)) {
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
            txtMaDauSach.setText(model.getValueAt(row, 0).toString());
            txtTenSach.setText(model.getValueAt(row, 1).toString());
            txtSoLuong.setText(model.getValueAt(row, 2).toString());
            txtTheLoai.setText(model.getValueAt(row, 4).toString());
            txtTacGia.setText(model.getValueAt(row, 5).toString());
            txtNhaXuatBan.setText(model.getValueAt(row, 6).toString());
            txtNamXB.setText(model.getValueAt(row, 7).toString());
        }
    }

    private void clearForm() {
        txtMaDauSach.setText("");
        txtTenSach.setText("");
        txtSoLuong.setText("");
        txtTheLoai.setText("");
        txtTacGia.setText("");
        txtNhaXuatBan.setText("");
        txtNamXB.setText("");
    }

    private BookTitle getInputBookTitle() {
        return new BookTitle(
                txtMaDauSach.getText().trim(),
                txtTenSach.getText().trim(),
                Integer.parseInt(txtSoLuong.getText().trim()),
                txtTheLoai.getText().trim(),
                txtTacGia.getText().trim(),
                txtNhaXuatBan.getText().trim(),
                Integer.parseInt(txtNamXB.getText().trim())
        );
    }
}