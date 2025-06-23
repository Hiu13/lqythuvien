package ui;

import model.Borrower;
import service.BorrowerService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BorrowerForm extends JFrame {
    private final BorrowerService borrowerService;
    private JTable table;
    private DefaultTableModel model;

    private JTextField txtMaNM, txtTenNM, txtLop, txtSDT, txtEmail;

    public BorrowerForm() {
        borrowerService = new BorrowerService();
        initUI();
        loadData();
    }

    private void initUI() {
        setTitle("Quản lý Người mượn");
        setSize(750, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Table
        model = new DefaultTableModel(new Object[]{"Mã", "Tên", "Lớp", "SĐT", "Email"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Form nhập
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin người mượn"));

        txtMaNM = new JTextField();
        txtTenNM = new JTextField();
        txtLop = new JTextField();
        txtSDT = new JTextField();
        txtEmail = new JTextField();

        formPanel.add(new JLabel("Mã người mượn:"));
        formPanel.add(txtMaNM);
        formPanel.add(new JLabel("Tên người mượn:"));
        formPanel.add(txtTenNM);
        formPanel.add(new JLabel("Lớp:"));
        formPanel.add(txtLop);
        formPanel.add(new JLabel("SĐT:"));
        formPanel.add(txtSDT);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(txtEmail);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnAdd = new JButton("Thêm");
        JButton btnUpdate = new JButton("Sửa");
        JButton btnDelete = new JButton("Xoá");
        JButton btnRefresh = new JButton("Làm mới");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);

        // Sự kiện
        btnAdd.addActionListener(e -> addBorrower());
        btnUpdate.addActionListener(e -> updateBorrower());
        btnDelete.addActionListener(e -> deleteBorrower());
        btnRefresh.addActionListener(e -> loadData());
        table.getSelectionModel().addListSelectionListener(e -> loadSelectedRow());

        // Layout
        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadData() {
        model.setRowCount(0);
        List<Borrower> list = borrowerService.getAllBorrowers();
        for (Borrower b : list) {
            model.addRow(new Object[]{
                    b.getMaNguoiMuon(),
                    b.getTenNguoiMuon(),
                    b.getClass(),
                    b.getSdt(),
                    b.getGmail()
            });
        }
    }

    private void addBorrower() {
        Borrower b = getInputBorrower();
        if (borrowerService.addBorrower(b)) {
            JOptionPane.showMessageDialog(this, "Thêm thành công");
            loadData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại");
        }
    }

    private void updateBorrower() {
        Borrower b = getInputBorrower();
        if (borrowerService.updateBorrower(b)) {
            JOptionPane.showMessageDialog(this, "Cập nhật thành công");
            loadData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại");
        }
    }

    private void deleteBorrower() {
        String maNM = txtMaNM.getText().trim();
        if (borrowerService.deleteBorrower(maNM)) {
            JOptionPane.showMessageDialog(this, "Xoá thành công");
            loadData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Xoá thất bại");
        }
    }

    private void loadSelectedRow() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            txtMaNM.setText(model.getValueAt(row, 0).toString());
            txtTenNM.setText(model.getValueAt(row, 1).toString());
            txtLop.setText(model.getValueAt(row, 2).toString());
            txtSDT.setText(model.getValueAt(row, 3).toString());
            txtEmail.setText(model.getValueAt(row, 4).toString());
        }
    }

    private void clearForm() {
        txtMaNM.setText("");
        txtTenNM.setText("");
        txtLop.setText("");
        txtSDT.setText("");
        txtEmail.setText("");
    }

    private Borrower getInputBorrower() {
        return new Borrower(
                txtMaNM.getText().trim(),
                txtTenNM.getText().trim(),
                txtLop.getText().trim(),
                txtSDT.getText().trim(),
                txtEmail.getText().trim()
        );
    }
}
