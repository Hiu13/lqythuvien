package ui;

import javax.swing.*;
import java.awt.*;

public class MainMenuForm extends JFrame {

    public MainMenuForm() {
        initUI();
    }

    private void initUI() {
        setTitle("Hệ thống quản lý thư viện");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Menu bar
        JMenuBar menuBar = new JMenuBar();

        // Đầu sách
        JMenu menuBookTitle = new JMenu("Đầu sách");
        JMenuItem menuBookTitleForm = new JMenuItem("Quản lý đầu sách");
        menuBookTitleForm.addActionListener(e -> new BookTitleForm().setVisible(true));
        menuBookTitle.add(menuBookTitleForm);

        // Sách (bản sao sách)
        JMenu menuBook = new JMenu("Sách");
        JMenuItem menuBookForm = new JMenuItem("Quản lý sách");
        menuBookForm.addActionListener(e -> new BookCopyForm().setVisible(true));
        menuBook.add(menuBookForm);

        // Người mượn
        JMenu menuBorrower = new JMenu("Người mượn");
        JMenuItem menuBorrowerForm = new JMenuItem("Quản lý người mượn");
        menuBorrowerForm.addActionListener(e -> new BorrowerForm().setVisible(true));
        menuBorrower.add(menuBorrowerForm);

        // Phiếu mượn
        JMenu menuLoan = new JMenu("Phiếu mượn");
        JMenuItem menuLoanForm = new JMenuItem("Quản lý phiếu mượn");
        menuLoanForm.addActionListener(e -> new LoanSlipForm().setVisible(true));
        menuLoan.add(menuLoanForm);

        // Thoát
        JMenu menuSystem = new JMenu("Hệ thống");
        JMenuItem menuExit = new JMenuItem("Thoát");
        menuExit.addActionListener(e -> System.exit(0));
        menuSystem.add(menuExit);

        // Thêm vào thanh menu
        menuBar.add(menuBookTitle);
        menuBar.add(menuBook);
        menuBar.add(menuBorrower);
        menuBar.add(menuLoan);
        menuBar.add(menuSystem);

        setJMenuBar(menuBar);

        // Giao diện trung tâm (placeholder)
        JLabel welcomeLabel = new JLabel("Chào mừng đến với hệ thống quản lý thư viện!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 22));
        add(welcomeLabel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenuForm().setVisible(true));
    }
}
