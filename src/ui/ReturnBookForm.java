package ui;

import service.LoanSlipService;

import javax.swing.*;
import java.awt.*;

public class ReturnBookForm extends JFrame {
    private final LoanSlipService loanSlipService;
    private JTextField txtMaPhieuMuon;

    public ReturnBookForm() {
        loanSlipService = new LoanSlipService();
        initUI();
    }

    private void initUI() {
        setTitle("Trả sách");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Form nhập
        JPanel formPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Nhập mã phiếu mượn"));

        txtMaPhieuMuon = new JTextField();
        formPanel.add(new JLabel("Mã phiếu mượn:"));
        formPanel.add(txtMaPhieuMuon);

        // Nút trả sách
        JPanel buttonPanel = new JPanel();
        JButton btnReturn = new JButton("Trả sách");
        buttonPanel.add(btnReturn);

        btnReturn.addActionListener(e -> traSach());

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void traSach() {
        String maPhieuMuon = txtMaPhieuMuon.getText().trim();
        if (maPhieuMuon.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã phiếu mượn.");
            return;
        }

        boolean success = loanSlipService.traSach(maPhieuMuon);
        if (success) {
            JOptionPane.showMessageDialog(this, "Trả sách thành công!");
        } else {
            JOptionPane.showMessageDialog(this, "Trả sách thất bại!");
        }
    }
}
