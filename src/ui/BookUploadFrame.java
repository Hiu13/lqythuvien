package ui;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class BookUploadFrame extends JFrame {
    private JTextField txtTen, txtTacGia, txtSoLuong, txtAnh;
    private File selectedFile;

    public BookUploadFrame() {
        setTitle("Thêm sách mới");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2, 10, 10));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        txtTen = new JTextField();
        txtTacGia = new JTextField();
        txtSoLuong = new JTextField();
        txtAnh = new JTextField();
        txtAnh.setEditable(false);

        JButton btnChonAnh = new JButton("Chọn ảnh");
        JButton btnThem = new JButton("Thêm sách");

        add(new JLabel("Tên sách:"));
        add(txtTen);

        add(new JLabel("Tác giả:"));
        add(txtTacGia);

        add(new JLabel("Số lượng:"));
        add(txtSoLuong);

        add(btnChonAnh);
        add(txtAnh);

        add(new JLabel());  // khoảng trắng
        add(btnThem);

        btnChonAnh.addActionListener(e -> chooseImage());
        btnThem.addActionListener(e -> insertBook());
    }

    private void chooseImage() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedFile = chooser.getSelectedFile();
            txtAnh.setText(selectedFile.getAbsolutePath());
        }
    }

    private void insertBook() {
        String ten = txtTen.getText().trim();
        String tacGia = txtTacGia.getText().trim();
        String soLuongStr = txtSoLuong.getText().trim();

        if (ten.isEmpty() || tacGia.isEmpty() || soLuongStr.isEmpty() || selectedFile == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin và chọn ảnh.");
            return;
        }

        try {
            int soLuong = Integer.parseInt(soLuongStr);

            try (Connection conn = util.DBConnect.getConnection()) {
                String sql = "INSERT INTO tb_sach (TenSach, TacGia, SoLuong, AnhSach) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, ten);
                stmt.setString(2, tacGia);
                stmt.setInt(3, soLuong);
                stmt.setString(4, selectedFile.getAbsolutePath());

                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Thêm sách thành công!");
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm sách thất bại.");
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số lượng phải là số nguyên.");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }
}
