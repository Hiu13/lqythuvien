package ui;

import javax.swing.*;
import java.awt.*;

public class Menu_user extends JFrame {
    private String maNguoiMuon; // Mã người mượn, truyền vào khi login

    public Menu_user(String maNguoiMuon) {
        this.maNguoiMuon = maNguoiMuon;
        setTitle("Giao diện chính - Người mượn");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Label chào mừng
        JLabel lblWelcome = new JLabel("Xin chào, " + maNguoiMuon, SwingConstants.CENTER);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblWelcome, BorderLayout.NORTH);

        // Panel chức năng
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Chức năng"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            )
        );

        JButton btnFindBook = new JButton("Tìm sách");
        JButton btnBorrowBook = new JButton("Mượn sách");
        JButton btnViewBook = new JButton("Xem thông tin sách");

        panel.add(btnFindBook);
        panel.add(btnBorrowBook);
        panel.add(btnViewBook);

        add(panel, BorderLayout.CENTER);

        // Nút đăng xuất
        JButton btnLogout = new JButton("Đăng xuất");
        add(btnLogout, BorderLayout.SOUTH);

        // Sự kiện
        btnLogout.addActionListener(e -> {
            this.dispose();
            new Login_user().setVisible(true);
        });

        // Liên kết các chức năng
        btnFindBook.addActionListener(e -> findBook());
        btnBorrowBook.addActionListener(e -> borrowBook());
        btnViewBook.addActionListener(e -> viewBook());
    }

    private void findBook() {
        JTextField keywordField = new JTextField();
        int result = JOptionPane.showConfirmDialog(this, keywordField, "Nhập từ khóa tìm sách", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String keyword = keywordField.getText().trim();
            if (!keyword.isEmpty()) {
                new BookSearchResultFrame(keyword).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập từ khóa!");
            }
        }
    }

    private void borrowBook() {
    JFrame frame = new JFrame("Mượn sách");
    frame.setSize(400, 300);
    frame.setLocationRelativeTo(this);
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    JLabel lblMaSach = new JLabel("Mã sách:");
    JTextField txtMaSach = new JTextField();

    JLabel lblNgayMuon = new JLabel("Ngày mượn (yyyy-MM-dd):");
    JTextField txtNgayMuon = new JTextField();

    JButton btnSubmit = new JButton("Xác nhận mượn");

    panel.add(lblMaSach);
    panel.add(txtMaSach);
    panel.add(lblNgayMuon);
    panel.add(txtNgayMuon);
    panel.add(new JLabel()); // trống
    panel.add(btnSubmit);

    frame.add(panel);
    frame.setVisible(true);

    btnSubmit.addActionListener(e -> {
        String maSach = txtMaSach.getText().trim();
        String ngayMuon = txtNgayMuon.getText().trim();

        if (maSach.isEmpty() || ngayMuon.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        try {
            // Ví dụ dùng DAO để insert phiếu mượn (tùy tên bảng và cột thực tế của bạn)
            dao.LoanSlipDAO dao = new dao.LoanSlipDAO();
            boolean success = dao.borrowBook(maNguoiMuon, maSach, ngayMuon); // viết phương thức này

            if (success) {
                JOptionPane.showMessageDialog(frame, "Mượn sách thành công!");
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Mượn sách thất bại! Có thể sách đã được mượn.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Lỗi khi mượn sách: " + ex.getMessage());
        }
    });
}


    private void viewBook() {
        new BookListFrame().setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Menu_user("Xin chào người dùng đăng nhập").setVisible(true)); // test thử
    }
}
