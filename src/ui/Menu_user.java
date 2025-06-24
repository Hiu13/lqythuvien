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

        btnFindBook.addActionListener(e -> findBook());
        btnBorrowBook.addActionListener(e -> borrowBook());
        btnViewBook.addActionListener(e -> viewBook());
    }

    private void findBook() {
        JOptionPane.showMessageDialog(this, "Chức năng Tìm sách đang phát triển.");
        // TODO: Viết chức năng tìm sách (query từ tb_sach + tb_dausach)
    }

    private void borrowBook() {
        JOptionPane.showMessageDialog(this, "Chức năng Mượn sách đang phát triển.");
        // TODO: Viết chức năng mượn sách (insert tb_phieumuon)
    }

    private void viewBook() {
        JOptionPane.showMessageDialog(this, "Chức năng Xem thông tin sách đang phát triển.");
        // TODO: Viết chức năng xem thông tin sách (hiển thị list sách)
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Menu_user("NM123").setVisible(true)); // test thử
    }
}
