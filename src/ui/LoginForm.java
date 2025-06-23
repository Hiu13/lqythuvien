package ui;

import service.AdminService;

import javax.swing.*;
import java.awt.*;

public class LoginForm extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private final AdminService adminService;

    public LoginForm() {
        adminService = new AdminService();
        initUI();
    }

    private void initUI() {
        setTitle("Đăng nhập quản trị");
        setSize(700, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel chính
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 10, 50));

        panel.add(new JLabel("Tên đăng nhập:"));
        txtUsername = new JTextField();
        panel.add(txtUsername);

        panel.add(new JLabel("Mật khẩu:"));
        txtPassword = new JPasswordField();
        panel.add(txtPassword);

        JButton btnLogin = new JButton("Đăng nhập");
        JButton btnSignin = new JButton("Đăng ký");
        JButton btnExit = new JButton("Thoát");
        panel.add(btnLogin);
        panel.add(btnSignin);
        panel.add(btnExit);

        add(panel, BorderLayout.CENTER);

        // Sự kiện
        btnLogin.addActionListener(e -> login());
        btnSignin.addActionListener(e -> openSignUpForm());
        btnExit.addActionListener(e -> System.exit(0));
    }

    private void login() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        boolean valid = adminService.checkLogin(username, password);
        if (valid) {
            JOptionPane.showMessageDialog(this, "Đăng nhập thành công!");
            this.dispose(); // đóng login
            new MainMenuForm().setVisible(true); // mở main menu
        } else {
            JOptionPane.showMessageDialog(this, "Tên đăng nhập hoặc mật khẩu không đúng!");
        }
    }
    private void openSignUpForm() {
    this.dispose();  // Đóng form login (hoặc dùng setVisible(false) nếu muốn giữ lại)
    new SignUp().setVisible(true);
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}