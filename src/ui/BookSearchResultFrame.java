package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.awt.image.BufferedImage;


public class BookSearchResultFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;

    public BookSearchResultFrame(String keyword) {
        setTitle("Kết quả tìm sách");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"Ảnh", "Tên sách", "Tác giả", "Số lượng"}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? ImageIcon.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // không cho sửa
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(100);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        loadBooks(keyword);
    }

    private void loadBooks(String keyword) {
        try (Connection conn = util.DBConnect.getConnection()) {
            String sql = "SELECT TenSach, TacGia, SoLuong, AnhSach FROM tb_sach WHERE TenSach LIKE ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + keyword + "%");

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String ten = rs.getString("TenSach");
                String tacGia = rs.getString("TacGia");
                int soLuong = rs.getInt("SoLuong");
                String path = rs.getString("AnhSach"); // đường dẫn ảnh từ DB

                ImageIcon icon;
                if (path != null && !path.isEmpty()) {
                    ImageIcon imgIcon = new ImageIcon(path);
                    Image img = imgIcon.getImage().getScaledInstance(70, 100, Image.SCALE_SMOOTH);
                    icon = new ImageIcon(img);
                } else {
                    icon = new ImageIcon(new BufferedImage(70, 100, BufferedImage.TYPE_INT_RGB)); // ảnh trắng
                }

                tableModel.addRow(new Object[]{icon, ten, tacGia, String.valueOf(soLuong)});
            }

            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy sách nào phù hợp!");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu sách: " + ex.getMessage());
        }
    }
}
