package ui;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import util.DBConnect;

public class Menu_user extends JFrame {
    private String maNguoiMuon;
    private String tenNguoiMuon;

    public Menu_user(String maNguoiMuon) {
        this.maNguoiMuon = maNguoiMuon;
        this.tenNguoiMuon = fetchTenNguoiMuon(maNguoiMuon);

        setTitle("Giao diện chính - Người mượn");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel lblWelcome = new JLabel("Xin chào, " + tenNguoiMuon, SwingConstants.CENTER);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblWelcome, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();

        JMenu menuChucNang = new JMenu("Hệ Thống");
        JMenuItem itemFindBook = new JMenuItem("Tìm sách");
        JMenuItem itemViewBook = new JMenuItem("Kho sách");
        JMenuItem itemBorrowBook = new JMenuItem("Mượn sách");
        JMenuItem itemExtendLoan = new JMenuItem("Gia hạn mượn sách");
        JMenuItem itemBill = new JMenuItem("Hóa đơn mượn sách");

        JMenu menuTaiKhoan = new JMenu("Tài khoản");
        JMenuItem itemLogout = new JMenuItem("Đăng xuất");

        menuChucNang.add(itemFindBook);
        menuChucNang.add(itemBorrowBook);
        menuChucNang.add(itemViewBook);
        menuTaiKhoan.add(itemLogout);
        menuChucNang.add(itemBill);
        menuBar.add(menuChucNang);
        menuBar.add(menuTaiKhoan);
        menuChucNang.add(itemExtendLoan);
        setJMenuBar(menuBar);

        itemFindBook.addActionListener(e -> findBook());
        itemBorrowBook.addActionListener(e -> borrowBook());
        itemViewBook.addActionListener(e -> viewBook());
        itemLogout.addActionListener(e -> {
            this.dispose();
            new Login_user().setVisible(true);
        });
        itemBill.addActionListener(e -> showBill());
        itemExtendLoan.addActionListener(e -> extendLoan());
    }

    private String fetchTenNguoiMuon(String maNguoiMuon) {
        String ten = "Người dùng";
        try (Connection conn = DBConnect.getConnection()) {
            String sql = "SELECT TenNguoiMuon FROM tb_nguoimuon WHERE MaNguoiMuon = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, maNguoiMuon);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ten = rs.getString("TenNguoiMuon");
            }
            rs.close();
            stmt.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ten;
    }

    private void findBook() {
        JTextField tenField = new JTextField();
        JTextField tacGiaField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("Tên sách:"));
        panel.add(tenField);
        panel.add(new JLabel("Tác giả:"));
        panel.add(tacGiaField);

        int result = JOptionPane.showOptionDialog(
                this,
                panel,
                "Nhập thông tin tìm sách",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null, null, null);

        if (result == JOptionPane.OK_OPTION) {
            String ten = tenField.getText().trim();
            String tacGia = tacGiaField.getText().trim();

            if (!ten.isEmpty() && !tacGia.isEmpty()) {
                new BookSearchResultFrame(ten, tacGia).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ tên sách và tác giả.");
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

        JLabel lblTenSach = new JLabel("Tên sách:");
        JTextField txtTenSach = new JTextField();

        JLabel lblTacGia = new JLabel("Tác giả:");
        JTextField txtTacGia = new JTextField();

        JButton btnSubmit = new JButton("Xác nhận mượn");

        panel.add(lblTenSach);
        panel.add(txtTenSach);
        panel.add(lblTacGia);
        panel.add(txtTacGia);
        panel.add(new JLabel());
        panel.add(btnSubmit);

        frame.add(panel);
        frame.setVisible(true);

        btnSubmit.addActionListener(e -> {
            String tenSach = txtTenSach.getText().trim();
            String tacGia = txtTacGia.getText().trim();

            if (tenSach.isEmpty() || tacGia.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Vui lòng nhập đầy đủ thông tin!");
                return;
            }

            try (Connection conn = util.DBConnect.getConnection()) {
                String sqlFind = "SELECT MaSach, SoLuong FROM tb_dausach WHERE TenSach = ? AND TacGia = ?";
                PreparedStatement stmtFind = conn.prepareStatement(sqlFind);
                stmtFind.setString(1, tenSach);
                stmtFind.setString(2, tacGia);
                ResultSet rs = stmtFind.executeQuery();

                if (!rs.next()) {
                    JOptionPane.showMessageDialog(frame, "Không tìm thấy sách với thông tin đã nhập.");
                    return;
                }

                String maSach = rs.getString("MaSach");
                int soLuong = rs.getInt("SoLuong");
                rs.close();
                stmtFind.close();

                String sqlCount = "SELECT COUNT(*) AS DaMuon FROM tb_phieumuon WHERE MaSach = ?";
                PreparedStatement stmtCount = conn.prepareStatement(sqlCount);
                stmtCount.setString(1, maSach);
                rs = stmtCount.executeQuery();

                int daMuon = 0;
                if (rs.next()) {
                    daMuon = rs.getInt("DaMuon");
                }
                rs.close();
                stmtCount.close();

                if (daMuon >= soLuong) {
                    JOptionPane.showMessageDialog(frame, "Sách này đã được mượn hết, không thể mượn thêm.");
                    return;
                }

                String ngayMuon = java.time.LocalDate.now().toString();
                String hanTra = java.time.LocalDate.now().plusDays(7).toString(); // VD: hạn trả sau 7 ngày

                String sqlInsert = "INSERT INTO tb_phieumuon (MaNguoiMuon, MaSach, NgayMuon, HanTra) VALUES (?, ?, ?, ?)";
                PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert);
                stmtInsert.setString(1, maNguoiMuon);
                stmtInsert.setString(2, maSach);
                stmtInsert.setString(3, ngayMuon);
                stmtInsert.setString(4, hanTra);

                int rows = stmtInsert.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(frame,
                            "Mượn sách thành công!\nNgày mượn: " + ngayMuon + "\nHạn trả: " + hanTra);
                    frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(frame, "Mượn sách thất bại!");
                }

                stmtInsert.close();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Lỗi khi mượn sách: " + ex.getMessage());
            }
        });
    }

    private void showBill() {
        try (Connection conn = util.DBConnect.getConnection()) {
            String sql = "SELECT MaSach, NgayMuon, HanTra FROM tb_phieumuon WHERE MaNguoiMuon = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, maNguoiMuon);
            ResultSet rs = stmt.executeQuery();

            if (!rs.isBeforeFirst()) {
                JOptionPane.showMessageDialog(this, "Bạn chưa có hóa đơn mượn sách nào.");
                return;
            }

            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                String maSach = rs.getString("MaSach");
                String ngayMuon = rs.getString("NgayMuon");
                String hanTra = rs.getString("HanTra");

                long phi = 0;
                java.time.LocalDate hanTraDate = java.time.LocalDate.parse(hanTra);
                java.time.LocalDate today = java.time.LocalDate.now();
                if (today.isAfter(hanTraDate)) {
                    long daysLate = java.time.temporal.ChronoUnit.DAYS.between(hanTraDate, today);
                    phi = daysLate * 5000;
                }

                sb.append("Mã sách: ").append(maSach)
                        .append("\nNgày mượn: ").append(ngayMuon)
                        .append("\nHạn trả: ").append(hanTra)
                        .append("\nPhí quá hạn: ").append(phi).append(" VNĐ")
                        .append("\n-----------------------\n");

            }

            rs.close();
            stmt.close();

            JTextArea textArea = new JTextArea(sb.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));

            JOptionPane.showMessageDialog(this, scrollPane, "Hóa đơn mượn sách", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi lấy hóa đơn: " + ex.getMessage());
        }
    }

    private void extendLoan() {
        JFrame frame = new JFrame("Gia hạn mượn sách");
        frame.setSize(400, 200);
        frame.setLocationRelativeTo(this);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JComboBox<String> comboLoans = new JComboBox<>();

        try (Connection conn = util.DBConnect.getConnection()) {
            String sql = "SELECT MaPhieuMuon, MaSach, NgayTra FROM tb_phieumuon WHERE MaNguoiMuon = ? AND NgayTra < CURDATE()";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, maNguoiMuon);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String maPhieu = rs.getString("MaPhieuMuon");
                String maSach = rs.getString("MaSach");
                String ngayTra = rs.getString("NgayTra");
                comboLoans.addItem(maPhieu + " - " + maSach + " (Hạn cũ: " + ngayTra + ")");
            }

            rs.close();
            stmt.close();

            if (comboLoans.getItemCount() == 0) {
                JOptionPane.showMessageDialog(frame, "Bạn không có sách nào cần gia hạn!");
                frame.dispose();
                return;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Lỗi khi kiểm tra phiếu mượn: " + ex.getMessage());
            frame.dispose();
            return;
        }

        JButton btnExtend = new JButton("Xác nhận gia hạn");
        panel.add(comboLoans, BorderLayout.CENTER);
        panel.add(btnExtend, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setVisible(true);

        btnExtend.addActionListener(e -> {
            String selected = (String) comboLoans.getSelectedItem();
            if (selected != null) {
                String maPhieu = selected.split(" - ")[0].trim();

                try (Connection conn = util.DBConnect.getConnection()) {
                    String sql = "UPDATE tb_phieumuon SET NgayTra = DATE_ADD(NgayTra, INTERVAL 7 DAY) WHERE MaPhieuMuon = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, maPhieu);
                    int rows = stmt.executeUpdate();

                    if (rows > 0) {
                        JOptionPane.showMessageDialog(frame, "Gia hạn thành công!");
                        frame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Gia hạn thất bại!");
                    }

                    stmt.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Lỗi khi gia hạn: " + ex.getMessage());
                }
            }
        });
    }

    private void viewBook() {
        new BookListFrame().setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Menu_user("S001").setVisible(true));
    }
}
