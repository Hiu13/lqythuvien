package ui;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import util.DBConnect;

public class SignUp extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;

    public SignUp() {
        setTitle("Đăng ký tài khoản");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Panel chính với border + padding
        JPanel mainPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        mainPanel.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Thông tin đăng ký"),
                BorderFactory.createEmptyBorder(30, 50, 10, 50)
            )
        );

        // Thành phần giao diện
        mainPanel.add(new JLabel("Tên đăng nhập:"));
        txtUsername = new JTextField();
        mainPanel.add(txtUsername);

        mainPanel.add(new JLabel("Mật khẩu:"));
        txtPassword = new JPasswordField();
        mainPanel.add(txtPassword);

        JButton btnRegister = new JButton("Đăng ký");
        JButton btnExit = new JButton("Thoát");
        mainPanel.add(btnRegister);
        mainPanel.add(btnExit);

        // Thêm panel vào frame
        add(mainPanel);

        // Sự kiện
        btnRegister.addActionListener(e -> register());
        btnExit.addActionListener(e -> System.exit(0));
    }

    private void register() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conn = DBConnect.getConnection()) {
            String sql = "INSERT INTO admin (username, password) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Đăng ký thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            this.dispose(); 
            // Bạn có thể mở LoginForm ở đây nếu muốn
            new LoginForm().setVisible(true);
        } catch (SQLException ex) {
            if (ex.getErrorCode() == 1062) {
                JOptionPane.showMessageDialog(this, "Tên đăng nhập đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            } else {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi đăng ký!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SignUp().setVisible(true));
    }
}
