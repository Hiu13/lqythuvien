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

        // Label chào mừng
        JLabel lblWelcome = new JLabel("Xin chào, " + tenNguoiMuon, SwingConstants.CENTER);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblWelcome, BorderLayout.CENTER);

        // Tạo menu bar
        JMenuBar menuBar = new JMenuBar();

        JMenu menuChucNang = new JMenu("Hệ Thống");
        JMenuItem itemFindBook = new JMenuItem("Tìm sách");
        JMenuItem itemBorrowBook = new JMenuItem("Mượn sách");
        JMenuItem itemReturnBook = new JMenuItem("Trả sách"); // ✅ mới thêm
        JMenuItem itemViewBook = new JMenuItem("Xem thông tin sách");
        JMenuItem itemBill = new JMenuItem("Hóa đơn mượn sách");
        JMenuItem itemPayBill = new JMenuItem("Thanh toán hóa đơn");

        JMenu menuTaiKhoan = new JMenu("Tài khoản");
        JMenuItem itemLogout = new JMenuItem("Đăng xuất");

        // Gắn các item vào menu
        menuChucNang.add(itemFindBook);
        menuChucNang.add(itemBorrowBook);
        menuChucNang.add(itemReturnBook); // ✅ mới thêm
        menuChucNang.add(itemViewBook);
        menuChucNang.add(itemPayBill);
        menuChucNang.add(itemBill);

        menuTaiKhoan.add(itemLogout);

        menuBar.add(menuChucNang);
        menuBar.add(menuTaiKhoan);
        setJMenuBar(menuBar);

        // Sự kiện
        itemFindBook.addActionListener(e -> findBook());
        itemBorrowBook.addActionListener(e -> borrowBook());
        itemReturnBook.addActionListener(e -> new ReturnBookForm().setVisible(true)); // ✅ mới thêm
        itemViewBook.addActionListener(e -> viewBook());
        itemPayBill.addActionListener(e -> showQRCode());
        itemBill.addActionListener(e -> showBill());
        itemLogout.addActionListener(e -> {
            this.dispose();
            new Login_user().setVisible(true);
        });
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

        JLabel lblMaSach = new JLabel("Mã sách:");
        JTextField txtMaSach = new JTextField();

        JLabel lblNgayMuon = new JLabel("Ngày mượn (yyyy-MM-dd):");
        JTextField txtNgayMuon = new JTextField();

        JButton btnSubmit = new JButton("Xác nhận mượn");

        panel.add(lblMaSach);
        panel.add(txtMaSach);
        panel.add(lblNgayMuon);
        panel.add(txtNgayMuon);
        panel.add(new JLabel());
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
                dao.LoanSlipDAO dao = new dao.LoanSlipDAO();
                boolean success = dao.borrowBook(maNguoiMuon, maSach, ngayMuon);

                if (success) {
                    JOptionPane.showMessageDialog(frame, "Mượn sách thành công!");
                    frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(frame, "Mượn sách thất bại! Có thể sách đã hết.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Lỗi khi mượn sách: " + ex.getMessage());
            }
        });
    }

    private void showBill() {
        try (Connection conn = DBConnect.getConnection()) {
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

    private void showQRCode() {
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/ui/qr.png"));
            Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            icon = new ImageIcon(img);

            JLabel label = new JLabel(icon);
            JOptionPane.showMessageDialog(this, label, "Quét mã QR để thanh toán", JOptionPane.PLAIN_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Không tìm thấy ảnh mã QR!");
        }
    }

    private void viewBook() {
        new BookListFrame().setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Menu_user("S001").setVisible(true));
    }
}
