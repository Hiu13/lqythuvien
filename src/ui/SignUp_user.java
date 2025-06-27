package ui;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.regex.Pattern;
import util.DBConnect;

public class SignUp_user extends JFrame {
    private JTextField txtTen;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JTextField txtDiaChi;
    private JTextField txtSDT;

    public SignUp_user() {
        setTitle("Đăng ký người mượn");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Tên người mượn:"));
        txtTen = new JTextField();
        panel.add(txtTen);

        panel.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        panel.add(txtEmail);

        panel.add(new JLabel("Mật khẩu:"));
        txtPassword = new JPasswordField();
        panel.add(txtPassword);

        panel.add(new JLabel("Địa chỉ:"));
        txtDiaChi = new JTextField();
        panel.add(txtDiaChi);

        panel.add(new JLabel("Số điện thoại:"));
        txtSDT = new JTextField();
        panel.add(txtSDT);

        JButton btnDangKy = new JButton("Đăng ký");
        JButton btnQuayLai = new JButton("Quay lại");
        panel.add(btnDangKy);
        panel.add(btnQuayLai);

        add(panel);

        btnDangKy.addActionListener(e -> signUp());
        btnQuayLai.addActionListener(e -> {
            this.dispose();
            new Login_user().setVisible(true);
        });
    }

    private void signUp() {
        String ten = txtTen.getText().trim();
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        String diaChi = txtDiaChi.getText().trim();
        String sdt = txtSDT.getText().trim();

        if (ten.isEmpty() || email.isEmpty() || password.isEmpty() || diaChi.isEmpty() || sdt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        if (!Checkmail(email)) {
            JOptionPane.showMessageDialog(this, "Email không hợp lệ!");
            return;
        }

        try (Connection conn = DBConnect.getConnection()) {
            String maMoi = generateMaNguoiMuon(conn);

            String sql = "INSERT INTO tb_nguoimuon (MaNguoiMuon, TenNguoiMuon, Gmail, password, DiaChi, SDT) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, maMoi);
            stmt.setString(2, ten);
            stmt.setString(3, email);
            stmt.setString(4, password);
            stmt.setString(5, diaChi);
            stmt.setString(6, sdt);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Đăng ký thành công!\nMã người mượn: " + maMoi);
                this.dispose();
                new Login_user().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Đăng ký thất bại!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi đăng ký: " + ex.getMessage());
        }
    }

    private boolean Checkmail(String email) {
        String gmailRegex = "^[a-zA-Z0-9]+@gmail\\.com$";
        return Pattern.matches(gmailRegex, email);
    }

    private String generateMaNguoiMuon(Connection conn) throws Exception {
        String maMoi;
        int so = 1;
        boolean tonTai;

        do {
            maMoi = String.format("NM%03d", so);
            String sqlCheck = "SELECT MaNguoiMuon FROM tb_nguoimuon WHERE MaNguoiMuon = ?";
            PreparedStatement stmt = conn.prepareStatement(sqlCheck);
            stmt.setString(1, maMoi);
            ResultSet rs = stmt.executeQuery();
            tonTai = rs.next();
            rs.close();
            stmt.close();
            so++;
        } while (tonTai);

        return maMoi;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SignUp_user().setVisible(true));
    }
}
