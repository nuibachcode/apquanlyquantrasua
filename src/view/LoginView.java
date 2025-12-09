package view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import controller.LoginController;

public class LoginView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField textFieldEmail;
    private JPasswordField passwordField;
    private JPanel contentPane;
    private JButton btnLogin; // Khai báo biến ở đây để dùng chung

    public LoginView() {
        setTitle("Đăng Nhập");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 950, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        // ===== 1. Tải và Thiết lập Ảnh nền =====
        Image bgImage = null;
        try {
            java.net.URL imgUrl = getClass().getResource("/images/signUpView.jpg");
            if (imgUrl != null) {
                bgImage = new ImageIcon(imgUrl).getImage();
            }
        } catch (Exception e) {
            System.err.println("Warning: Background image not found.");
        }

        // Sử dụng lớp BackgroundPanel (Giả định bạn đã có class này từ trước)
        contentPane = new BackgroundPanel(bgImage);
        setContentPane(contentPane);
        
        // Lưu ý: BackgroundPanel của bạn cần có layout là null để setBounds hoạt động
        contentPane.setLayout(null); 

        // Định nghĩa Fonts và Colors
        Font titleFont = new Font("Arial", Font.BOLD, 36);
        Font labelFont = new Font("Arial", Font.BOLD, 18);
        Color titleColor = new Color(255, 102, 204);
        Color btnColor = Color.YELLOW;

        // Vị trí và kích thước
        int startY = 130;
        int inputWidth = 250;
        int inputHeight = 30;
        int labelX = 400;
        int fieldX = 500;

        // ===== 2. Tiêu đề =====
        JLabel lblTitle = new JLabel("Đăng Nhập");
        lblTitle.setFont(titleFont);
        lblTitle.setForeground(titleColor);
        lblTitle.setBounds(480, 50, 200, 40);
        contentPane.add(lblTitle);

        // ===== 3. Email =====
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(labelFont);
        lblEmail.setBounds(labelX, startY, 100, inputHeight);
        contentPane.add(lblEmail);

        textFieldEmail = new JTextField();
        textFieldEmail.setFont(new Font("Tahoma", Font.PLAIN, 16));
        textFieldEmail.setBounds(fieldX, startY, inputWidth, inputHeight);
        contentPane.add(textFieldEmail);

        // ===== 4. Mật Khẩu (KHỞI TẠO TRƯỚC) =====
        JLabel lblPassword = new JLabel("Mật Khẩu:");
        lblPassword.setFont(labelFont);
        lblPassword.setBounds(labelX, startY + 60, 100, inputHeight);
        contentPane.add(lblPassword);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Tahoma", Font.PLAIN, 16));
        passwordField.setBounds(fieldX, startY + 60, inputWidth, inputHeight);
        contentPane.add(passwordField);

        // ===== 5. Quên Mật Khẩu =====
        JButton btnForgotPassword = new JButton("Quên mật khẩu");
        btnForgotPassword.setFont(new Font("Arial", Font.PLAIN, 12));
        btnForgotPassword.setBorderPainted(false);
        btnForgotPassword.setContentAreaFilled(false);
        btnForgotPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnForgotPassword.setBounds(630, startY + 95, 120, 20);
        btnForgotPassword.setActionCommand("Forget Password ?");
        contentPane.add(btnForgotPassword);

        // ===== 6. Nút Đăng Nhập và Đăng Kí (KHỞI TẠO NÚT LOGIN) =====
        btnLogin = new JButton("Đăng Nhập");
        btnLogin.setFont(labelFont);
        btnLogin.setBackground(btnColor);
        btnLogin.setBounds(400, startY + 150, 150, 40);
        btnLogin.setActionCommand("Login");
        contentPane.add(btnLogin);

        JButton btnRegister = new JButton("Đăng Kí");
        btnRegister.setFont(labelFont);
        btnRegister.setBackground(btnColor);
        btnRegister.setBounds(600, startY + 150, 150, 40);
        btnRegister.setActionCommand("SignUp");
        contentPane.add(btnRegister);

        // ===== 7. Xử lý sự kiện Enter (QUAN TRỌNG: Đặt ở đây mới đúng) =====
        // Lúc này passwordField và btnLogin đã được new rồi, không bị NullPointer
        passwordField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnLogin.doClick(); // Tự động click nút Đăng Nhập
            }
        });
        
        // Thêm tính năng nhấn Enter ở ô Email cũng đăng nhập luôn cho tiện
        textFieldEmail.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnLogin.doClick();
            }
        });

        // ===== 8. Gán Controller =====
        LoginController controller = new LoginController(this);
        btnLogin.addActionListener(controller);
        btnRegister.addActionListener(controller);
        btnForgotPassword.addActionListener(controller);
    }

    // ===== Getters và Clear =====
    public JTextField getTextFieldEmail() {
        return textFieldEmail;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public void xoaForm() {
        textFieldEmail.setText("");
        passwordField.setText("");
    }
}