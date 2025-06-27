package ui;

import model.Book;
import service.BookService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BookForm extends JFrame {
    private BookService service = new BookService();
    private DefaultTableModel model;
    private JTable table;

    private JTextField txtMaSach, txtTenSach, txtTrangThai, txtAnhSach, txtTacGia, txtSoLuong;

    public BookForm() {
        setTitle("Quản lý sách");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initUI();
        loadData();
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(15, 15, 15, 15)); // Thêm padding 15px

        // Bảng hiển thị
        model = new DefaultTableModel(
                new Object[] { "Mã sách", "Tên sách", "Trạng thái", "Ảnh sách", "Tác giả", "Số lượng" }, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Form nhập liệu
        JPanel form = new JPanel(new GridLayout(6, 2, 10, 10));
        txtMaSach = new JTextField();
        txtTenSach = new JTextField();
        txtTrangThai = new JTextField();
        txtAnhSach = new JTextField();
        txtTacGia = new JTextField();
        txtSoLuong = new JTextField();

        form.add(new JLabel("Mã sách:"));
        form.add(txtMaSach);
        form.add(new JLabel("Tên sách:"));
        form.add(txtTenSach);
        form.add(new JLabel("Trạng thái:"));
        form.add(txtTrangThai);
        form.add(new JLabel("Ảnh sách:"));
        form.add(txtAnhSach);
        form.add(new JLabel("Tác giả:"));
        form.add(txtTacGia);
        form.add(new JLabel("Số lượng:"));
        form.add(txtSoLuong);

        panel.add(form, BorderLayout.NORTH);

        // Các nút chức năng
        JPanel buttons = new JPanel();
        JButton btnThem = new JButton("Thêm");
        JButton btnSua = new JButton("Sửa");
        JButton btnXoa = new JButton("Xóa");

        buttons.add(btnThem);
        buttons.add(btnSua);
        buttons.add(btnXoa);

        panel.add(buttons, BorderLayout.SOUTH);
        add(panel);

        // Sự kiện
        btnThem.addActionListener(e -> themSach());
        btnSua.addActionListener(e -> suaSach());
        btnXoa.addActionListener(e -> xoaSach());

        table.getSelectionModel().addListSelectionListener(e -> chonDong());
    }

    private void loadData() {
        model.setRowCount(0);
        List<Book> list = service.getAllBooks();
        for (Book b : list) {
            model.addRow(new Object[] {
                    b.getMaSach(), b.getTenSach(), b.getTrangThai(), b.getAnhSach(),
                    b.getTacGia(), b.getSoLuong()
            });
        }
    }

    private void themSach() {
        Book b = getInput();
        if (service.addBook(b)) {
            JOptionPane.showMessageDialog(this, "Thêm sách thành công!");
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm sách thất bại!");
        }
    }

    private void suaSach() {
        Book b = getInput();
        if (service.updateBook(b)) {
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
        }
    }

    private void xoaSach() {
        String ma = txtMaSach.getText().trim();
        if (service.deleteBook(ma)) {
            JOptionPane.showMessageDialog(this, "Xóa sách thành công!");
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Xóa sách thất bại!");
        }
    }

    private void chonDong() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            txtMaSach.setText(model.getValueAt(row, 0).toString());
            txtTenSach.setText(model.getValueAt(row, 1).toString());
            txtTrangThai.setText(model.getValueAt(row, 2).toString());
            txtAnhSach.setText(model.getValueAt(row, 3).toString());
            txtTacGia.setText(model.getValueAt(row, 4).toString());
            txtSoLuong.setText(model.getValueAt(row, 5).toString());
        }
    }

    private Book getInput() {
        return new Book(
                txtMaSach.getText().trim(),
                txtTenSach.getText().trim(),
                txtTrangThai.getText().trim(),
                txtAnhSach.getText().trim(),
                txtTacGia.getText().trim(),
                Integer.parseInt(txtSoLuong.getText().trim()));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BookForm().setVisible(true));
    }
}
