package view;

import controller.ForgotPasswordController;
import java.awt.*;
import javax.swing.*;

public class ForgotPasswordView extends JFrame {

    private JPanel contentPane;
    private JTextField txtEmail, txtOTP;
    private JPasswordField txtNewPass, txtConfirmPass;
    private JButton btnSendCode, btnConfirmReset, btnBack;
    
    // Các panel chứa form để ẩn/hiện
    public JPanel pnlVerify, pnlReset; 

    public ForgotPasswordView() {
        setTitle("Quên Mật Khẩu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        // Background
        Image bgImage = new ImageIcon(getClass().getResource("/images/signUpView.jpg")).getImage();
        contentPane = new BackgroundPanel(bgImage);
        contentPane.setLayout(null);
        setContentPane(contentPane);

        // Title
        JLabel lblTitle = new JLabel("ĐẶT LẠI MẬT KHẨU");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 30));
        lblTitle.setForeground(new Color(0, 0, 153));
        lblTitle.setBounds(250, 30, 400, 40);
        contentPane.add(lblTitle);

        // --- BƯỚC 1: NHẬP EMAIL ---
        JLabel lblEmail = new JLabel("Nhập Email đăng ký:");
        lblEmail.setFont(new Font("Arial", Font.BOLD, 16));
        lblEmail.setBounds(150, 100, 200, 30);
        contentPane.add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setBounds(350, 100, 250, 30);
        contentPane.add(txtEmail);

        btnSendCode = new JButton("Gửi mã xác nhận");
        btnSendCode.setBounds(620, 100, 140, 30);
        btnSendCode.setBackground(Color.ORANGE);
        contentPane.add(btnSendCode);

        // --- BƯỚC 2: NHẬP OTP & MẬT KHẨU MỚI (Mặc định ẩn hoặc Disable) ---
        pnlReset = new JPanel();
        pnlReset.setLayout(null);
        pnlReset.setOpaque(false); // Để nhìn thấy ảnh nền
        pnlReset.setBounds(100, 150, 600, 250);
        pnlReset.setVisible(false); // Ẩn đi lúc đầu
        contentPane.add(pnlReset);

        // Mã OTP
        JLabel lblOTP = new JLabel("Mã xác nhận (OTP):");
        lblOTP.setFont(new Font("Arial", Font.BOLD, 14));
        lblOTP.setBounds(50, 10, 150, 30);
        pnlReset.add(lblOTP);

        txtOTP = new JTextField();
        txtOTP.setBounds(250, 10, 150, 30);
        pnlReset.add(txtOTP);

        // Mật khẩu mới
        JLabel lblNewPass = new JLabel("Mật khẩu mới:");
        lblNewPass.setFont(new Font("Arial", Font.BOLD, 14));
        lblNewPass.setBounds(50, 60, 150, 30);
        pnlReset.add(lblNewPass);

        txtNewPass = new JPasswordField();
        txtNewPass.setBounds(250, 60, 250, 30);
        pnlReset.add(txtNewPass);
        
        // Nhập lại mật khẩu
        JLabel lblConfirm = new JLabel("Nhập lại mật khẩu:");
        lblConfirm.setFont(new Font("Arial", Font.BOLD, 14));
        lblConfirm.setBounds(50, 110, 150, 30);
        pnlReset.add(lblConfirm);

        txtConfirmPass = new JPasswordField();
        txtConfirmPass.setBounds(250, 110, 250, 30);
        pnlReset.add(txtConfirmPass);

        // Nút Xác nhận đổi
        btnConfirmReset = new JButton("Xác nhận đổi mật khẩu");
        btnConfirmReset.setBackground(Color.GREEN);
        btnConfirmReset.setFont(new Font("Arial", Font.BOLD, 16));
        btnConfirmReset.setBounds(180, 170, 250, 40);
        pnlReset.add(btnConfirmReset);

        // Nút quay lại
        btnBack = new JButton("Quay lại đăng nhập");
        btnBack.setBounds(300, 400, 200, 30);
        btnBack.addActionListener(e -> {
            new LoginView().setVisible(true);
            dispose();
        });
        contentPane.add(btnBack);

        // Gắn Controller
        ForgotPasswordController controller = new ForgotPasswordController(this);
        btnSendCode.addActionListener(controller);
        btnConfirmReset.addActionListener(controller);
    }

    // Getters
    public JTextField getTxtEmail() { return txtEmail; }
    public JTextField getTxtOTP() { return txtOTP; }
    public JPasswordField getTxtNewPass() { return txtNewPass; }
    public JPasswordField getTxtConfirmPass() { return txtConfirmPass; }
    public JButton getBtnSendCode() { return btnSendCode; }
    public JButton getBtnConfirmReset() { return btnConfirmReset; }
    public JPanel getPnlReset() { return pnlReset; }
}