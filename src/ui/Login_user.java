package ui;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import util.DBConnect;

public class Login_user extends JFrame {
    private JTextField txtEmail;
    private JPasswordField txtPassword;

    public Login_user() {
        setTitle("Đăng nhập người mượn");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2, 10, 10));

        add(new JLabel("Email:"));
        txtEmail = new JTextField();
        add(txtEmail);

        add(new JLabel("Mật khẩu:"));
        txtPassword = new JPasswordField();
        add(txtPassword);

        JButton btnLogin = new JButton("Đăng nhập");
        JButton btnSignUp = new JButton("Đăng ký");
        add(btnLogin);
        add(btnSignUp);

        btnLogin.addActionListener(e -> login());
        btnSignUp.addActionListener(e -> {
            this.dispose();
            new SignUp_user().setVisible(true);
        });
    }

    private void login() {
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        try (Connection conn = DBConnect.getConnection()) {
            String sql = "SELECT MaNguoiMuon FROM tb_nguoimuon WHERE Gmail = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String maNguoiMuon = rs.getString("MaNguoiMuon");
                JOptionPane.showMessageDialog(this, "Đăng nhập thành công!");
                this.dispose();
                new Menu_user(maNguoiMuon).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Sai email hoặc mật khẩu!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi đăng nhập: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login_user().setVisible(true));
    }
}
