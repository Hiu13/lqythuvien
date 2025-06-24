package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.awt.image.BufferedImage;


public class BookListFrame extends JFrame {
    private JTable table;
    private DefaultTableModel model;

    public BookListFrame() {
        setTitle("Danh sách tất cả sách");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new Object[]{"Ảnh", "Tên sách", "Tác giả", "Số lượng"}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? ImageIcon.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(100);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadBooks();
    }

    private void loadBooks() {
        try (Connection conn = util.DBConnect.getConnection()) {
            String sql = "SELECT TenSach, TacGia, SoLuong, AnhSach FROM tb_sach";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String ten = rs.getString("TenSach");
                String tacGia = rs.getString("TacGia");
                int soLuong = rs.getInt("SoLuong");
                String path = rs.getString("AnhSach");

                ImageIcon icon;
                if (path != null && !path.isEmpty()) {
                    ImageIcon imgIcon = new ImageIcon(path);
                    Image img = imgIcon.getImage().getScaledInstance(70, 100, Image.SCALE_SMOOTH);
                    icon = new ImageIcon(img);
                } else {
                    icon = new ImageIcon(new BufferedImage(70, 100, BufferedImage.TYPE_INT_RGB));
                }

                model.addRow(new Object[]{icon, ten, tacGia, soLuong});
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải danh sách sách: " + e.getMessage());
        }
    }
}
