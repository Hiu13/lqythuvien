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

        JMenuBar menuBar = new JMenuBar();

        JMenu menuBookTitle = new JMenu("Đầu sách");
        JMenuItem menuBookForm = new JMenuItem("Quản lý sách");
        menuBookForm.addActionListener(e -> new BookForm().setVisible(true));
        menuBookTitle.add(menuBookForm);

        JMenu menuBorrower = new JMenu("Người mượn");
        JMenuItem menuBorrowerForm = new JMenuItem("Quản lý người mượn");
        menuBorrowerForm.addActionListener(e -> new BorrowerForm().setVisible(true));
        menuBorrower.add(menuBorrowerForm);

        JMenu menuLoan = new JMenu("Mượn/Trả sách");
        JMenuItem menuLoanForm = new JMenuItem("Quản lý phiếu mượn");
        menuLoanForm.addActionListener(e -> new LoanSlipForm().setVisible(true));
        menuLoan.add(menuLoanForm);
        JMenuItem menuReturnBook = new JMenuItem("Trả sách");
        menuReturnBook.addActionListener(e -> new ReturnBookForm().setVisible(true));
        menuLoan.add(menuReturnBook);

        JMenu menuSystem = new JMenu("Tài khoản");
        JMenuItem menuExit = new JMenuItem("Đăng xuất");
        menuExit.addActionListener(e -> System.exit(0));
        menuSystem.add(menuExit);

        menuBar.add(menuBookTitle);
        menuBar.add(menuBorrower);
        menuBar.add(menuLoan);
        menuBar.add(menuSystem);

        setJMenuBar(menuBar);

        JLabel welcomeLabel = new JLabel("Chào mừng đến với hệ thống quản lý thư viện!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 22));
        add(welcomeLabel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenuForm().setVisible(true));
    }
}
