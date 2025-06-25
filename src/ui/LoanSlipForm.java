// LoanSlipForm.java - Đã cập nhật để kiểm tra và trừ số lượng sách khi mượn

package ui;

import model.LoanSlip;
import model.BookCopy;
import model.Borrower;
import service.BookCopyService;
import service.BorrowerService;
import service.LoanSlipService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class LoanSlipForm extends JFrame {
    private final LoanSlipService loanSlipService;
    private final BorrowerService borrowerService;
    private final BookCopyService bookCopyService;

    private JTable table;
    private DefaultTableModel model;

    private JTextField txtMaPM;
    private JComboBox<String> cbMaNM;
    private JComboBox<String> cbMaSach;
    private JSpinner spNgayMuon;
    private JSpinner spHanTra;
    private JSpinner spNgayTra;

    public LoanSlipForm() {
        loanSlipService = new LoanSlipService();
        borrowerService = new BorrowerService();
        bookCopyService = new BookCopyService();

        initUI();
        loadData();
        loadComboboxes();
    }

    private void initUI() {
        setTitle("Quản lý Phiếu mượn");
        setSize(800, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new Object[]{"Mã PM", "Mã Người mượn", "Mã Sách", "Ngày mượn", "Hạn trả", "Ngày trả"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin phiếu mượn"));

        txtMaPM = new JTextField();
        cbMaNM = new JComboBox<>();
        cbMaSach = new JComboBox<>();

        spNgayMuon = new JSpinner(new SpinnerDateModel());
        spNgayMuon.setEditor(new JSpinner.DateEditor(spNgayMuon, "yyyy-MM-dd"));

        spHanTra = new JSpinner(new SpinnerDateModel());
        spHanTra.setEditor(new JSpinner.DateEditor(spHanTra, "yyyy-MM-dd"));

        spNgayTra = new JSpinner(new SpinnerDateModel());
        spNgayTra.setEditor(new JSpinner.DateEditor(spNgayTra, "yyyy-MM-dd"));

        formPanel.add(new JLabel("Mã phiếu mượn:"));
        formPanel.add(txtMaPM);
        formPanel.add(new JLabel("Mã người mượn:"));
        formPanel.add(cbMaNM);
        formPanel.add(new JLabel("Mã sách:"));
        formPanel.add(cbMaSach);
        formPanel.add(new JLabel("Ngày mượn:"));
        formPanel.add(spNgayMuon);
        formPanel.add(new JLabel("Hạn trả:"));
        formPanel.add(spHanTra);
        formPanel.add(new JLabel("Ngày trả:"));
        formPanel.add(spNgayTra);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnAdd = new JButton("Thêm");
        JButton btnUpdate = new JButton("Sửa");
        JButton btnDelete = new JButton("Xoá");
        JButton btnRefresh = new JButton("Làm mới");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);

        btnAdd.addActionListener(e -> addLoan());
        btnUpdate.addActionListener(e -> updateLoan());
        btnDelete.addActionListener(e -> deleteLoan());
        btnRefresh.addActionListener(e -> loadData());
        table.getSelectionModel().addListSelectionListener(e -> loadSelectedRow());

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadComboboxes() {
        cbMaNM.removeAllItems();
        for (Borrower b : borrowerService.getAllBorrowers()) {
            cbMaNM.addItem(b.getMaNguoiMuon());
        }

        cbMaSach.removeAllItems();
        for (BookCopy b : bookCopyService.getAllBookCopies()) {
            cbMaSach.addItem(b.getMaSach());
        }
    }

    private void loadData() {
        model.setRowCount(0);
        List<LoanSlip> list = loanSlipService.getAllLoanSlips();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (LoanSlip l : list) {
            model.addRow(new Object[]{
                    l.getMaPhieuMuon(),
                    l.getMaNguoiMuon(),
                    l.getMaSach(),
                    sdf.format(l.getNgayMuon()),
                    sdf.format(l.getHanTra()),
                    sdf.format(l.getNgayTra())
            });
        }
    }

    private void addLoan() {
        String maSach = (String) cbMaSach.getSelectedItem();
        if (maSach == null || bookCopyService.getSoLuongSach(maSach) <= 0) {
            JOptionPane.showMessageDialog(this, "Không thể mượn. Sách đã hết!");
            return;
        }

        LoanSlip loan = getInputLoan();
        if (loanSlipService.addLoanSlip(loan)) {
            bookCopyService.giamSoLuong(maSach);
            JOptionPane.showMessageDialog(this, "Thêm thành công");
            loadData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại");
        }
    }

    private void updateLoan() {
        LoanSlip loan = getInputLoan();
        if (loanSlipService.updateLoanSlip(loan)) {
            JOptionPane.showMessageDialog(this, "Cập nhật thành công");
            loadData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại");
        }
    }

    private void deleteLoan() {
        String maPM = txtMaPM.getText().trim();
        if (loanSlipService.deleteLoanSlip(maPM)) {
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
            txtMaPM.setText(model.getValueAt(row, 0).toString());
            cbMaNM.setSelectedItem(model.getValueAt(row, 1).toString());
            cbMaSach.setSelectedItem(model.getValueAt(row, 2).toString());

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date muon = sdf.parse(model.getValueAt(row, 3).toString());
                Date hanTra = sdf.parse(model.getValueAt(row, 4).toString());
                Date tra = sdf.parse(model.getValueAt(row, 5).toString());
                spNgayMuon.setValue(muon);
                spHanTra.setValue(hanTra);
                spNgayTra.setValue(tra);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void clearForm() {
        txtMaPM.setText("");
        spNgayMuon.setValue(new Date());
        spHanTra.setValue(new Date());
        spNgayTra.setValue(new Date());
        cbMaNM.setSelectedIndex(0);
        cbMaSach.setSelectedIndex(0);
    }

    private LoanSlip getInputLoan() {
        return new LoanSlip(
                txtMaPM.getText().trim(),
                (Date) spNgayMuon.getValue(),
                (Date) spHanTra.getValue(),
                (String) cbMaSach.getSelectedItem(),
                (String) cbMaNM.getSelectedItem(),
                (Date) spNgayTra.getValue()
        );
    }
}
