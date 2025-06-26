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
        JMenuItem itemBorrowBook = new JMenuItem("Mượn sách");
        JMenuItem itemViewBook = new JMenuItem("Xem thông tin sách");
        JMenuItem itemBill = new JMenuItem("Hóa đơn mượn sách");
        JMenuItem itemPayBill = new JMenuItem("Thanh toán hóa đơn");
        JMenuItem itemReturnBook = new JMenuItem("Trả sách");

        JMenu menuTaiKhoan = new JMenu("Tài khoản");
        JMenuItem itemLogout = new JMenuItem("Đăng xuất");

        menuChucNang.add(itemFindBook);
        menuChucNang.add(itemBorrowBook);
        menuChucNang.add(itemViewBook);
        menuTaiKhoan.add(itemLogout);
        menuChucNang.add(itemPayBill);
        menuChucNang.add(itemBill);
        menuChucNang.add(itemReturnBook);
        menuBar.add(menuChucNang);
        menuBar.add(menuTaiKhoan);
        setJMenuBar(menuBar);

        itemFindBook.addActionListener(e -> findBook());
        itemBorrowBook.addActionListener(e -> borrowBook());
        itemViewBook.addActionListener(e -> viewBook());
        itemPayBill.addActionListener(e -> showQRCode());
        itemLogout.addActionListener(e -> {
            this.dispose();
            new Login_user().setVisible(true);
        });
        itemReturnBook.addActionListener(e -> returnBook());
        itemBill.addActionListener(e -> showBill());
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
        JTextField keywordField = new JTextField();
        int result = JOptionPane.showConfirmDialog(this, keywordField, "Nhập từ khóa tìm sách",
                JOptionPane.OK_CANCEL_OPTION);
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
                // Tìm Mã sách + Số lượng
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

                // Đếm số lượng sách đã mượn
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

                // Tiếp tục mượn sách
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

                // Tính phí nếu quá hạn
                long phi = 0;
                java.time.LocalDate hanTraDate = java.time.LocalDate.parse(hanTra);
                java.time.LocalDate today = java.time.LocalDate.now();
                if (today.isAfter(hanTraDate)) {
                    long daysLate = java.time.temporal.ChronoUnit.DAYS.between(hanTraDate, today);
                    phi = daysLate * 5000; // Ví dụ: 5000 đồng/ngày quá hạn
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

    private void showQRCode() {
        try {
            // Giả sử bạn có file qr.png nằm trong thư mục project (hoặc src/ui)
            ImageIcon icon = new ImageIcon(getClass().getResource("/ui/qr.png"));

            // Scale cho đẹp
            Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            icon = new ImageIcon(img);

            JLabel label = new JLabel(icon);
            JOptionPane.showMessageDialog(this, label, "Quét mã QR để thanh toán", JOptionPane.PLAIN_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Không tìm thấy ảnh mã QR!");
        }
    }

    private void returnBook() {
        JFrame frame = new JFrame("Trả sách");
        frame.setSize(400, 200);
        frame.setLocationRelativeTo(this);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JComboBox<String> comboBooks = new JComboBox<>();

        try (Connection conn = util.DBConnect.getConnection()) {
            String sql = "SELECT MaPhieuMuon, MaSach FROM tb_phieumuon WHERE MaNguoiMuon = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, maNguoiMuon);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String maPhieu = rs.getString("MaPhieuMuon");
                String maSach = rs.getString("MaSach");
                comboBooks.addItem(maPhieu + " - " + maSach);
            }

            rs.close();
            stmt.close();

            if (comboBooks.getItemCount() == 0) {
                JOptionPane.showMessageDialog(frame, "Không có sách nào để trả!");
                frame.dispose();
                return;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Lỗi khi truy vấn sách đang mượn: " + ex.getMessage());
            frame.dispose();
            return;
        }

        JButton btnReturn = new JButton("Xác nhận trả sách");

        panel.add(comboBooks, BorderLayout.CENTER);
        panel.add(btnReturn, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setVisible(true);

        btnReturn.addActionListener(e -> {
            String selected = (String) comboBooks.getSelectedItem();
            if (selected != null) {
                String maPhieu = selected.split(" - ")[0];

                try (Connection conn = util.DBConnect.getConnection()) {
                    String sql = "DELETE FROM tb_phieumuon WHERE MaPhieuMuon = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, maPhieu);
                    int rows = stmt.executeUpdate();

                    if (rows > 0) {
                        JOptionPane.showMessageDialog(frame, "Trả sách thành công!");
                        frame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Trả sách thất bại!");
                    }

                    stmt.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Lỗi khi trả sách: " + ex.getMessage());
                }
            }
        });
    }

    private void viewBook() {
        new BookListFrame().setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Menu_user("S001").setVisible(true)); // test thử
    }
}
