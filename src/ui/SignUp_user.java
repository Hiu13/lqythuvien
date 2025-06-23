package ui;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import util.DBConnect;

public class SignUp_user extends JFrame {
    private JTextField txtTenNguoiMuon;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;

    public SignUp_user() {
        setTitle("Đăng ký người mượn");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Tạo panel chính
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Thông tin đăng ký"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            )
        );

        panel.add(new JLabel("Tên người mượn:"));
        txtTenNguoiMuon = new JTextField();
        panel.add(txtTenNguoiMuon);

        panel.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        panel.add(txtEmail);

        panel.add(new JLabel("Mật khẩu:"));
        txtPassword = new JPasswordField();
        panel.add(txtPassword);

        panel.add(new JLabel("Nhắc lại mật khẩu:"));
        txtConfirmPassword = new JPasswordField();
        panel.add(txtConfirmPassword);

        JButton btnRegister = new JButton("Đăng ký");
        JButton btnExit = new JButton("Thoát");
        panel.add(btnRegister);
        panel.add(btnExit);

        add(panel); // Thêm panel vào frame

        btnRegister.addActionListener(e -> register());
        btnExit.addActionListener(e -> System.exit(0));
    }

    private void register() {
        String tenNguoiMuon = txtTenNguoiMuon.getText().trim();
        String email = txtEmail.getText().trim();
        String pass = new String(txtPassword.getPassword()).trim();
        String confirmPass = new String(txtConfirmPassword.getPassword()).trim();

        if (tenNguoiMuon.isEmpty() || email.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        if (!pass.equals(confirmPass)) {
            JOptionPane.showMessageDialog(this, "Mật khẩu và nhắc lại mật khẩu không khớp!");
            return;
        }

        try (Connection conn = DBConnect.getConnection()) {
            String sql = "INSERT INTO tb_nguoimuon (MaNguoiMuon, TenNguoiMuon, Gmail, password) VALUES (?, ?, ?, ?)";
            String maNguoiMuon = generateMaNguoiMuon();

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, maNguoiMuon);
            stmt.setString(2, tenNguoiMuon);
            stmt.setString(3, email);
            stmt.setString(4, pass);

            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Đăng ký thành công! Mã người mượn: " + maNguoiMuon);
            this.dispose();
            // new UserLogin().setVisible(true); // Mở form login nếu muốn
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi đăng ký: " + ex.getMessage());
        }
    }

    private String generateMaNguoiMuon() {
        int random = (int)(Math.random() * 900 + 100);
        return "NM" + random;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SignUp_user().setVisible(true));
    }
}
