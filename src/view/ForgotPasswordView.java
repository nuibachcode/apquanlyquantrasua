package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import controller.ForgotPasswordController;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

// Lưu ý: Giả định bạn đã có file BackgroundPanel.java trong package view
// và nó chứa logic vẽ ảnh nền: g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);

public class ForgotPasswordView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textFieldEmail; 
    private JTextField textFieldPhoneNumber; 
    
    // Loại bỏ JLabel backgroundLabel vì ta sẽ dùng BackgroundPanel

    // Các hằng số vị trí
    private final int INPUT_WIDTH = 300;
    private final int INPUT_HEIGHT = 30;
    private final int START_Y = 200;
    private final int LABEL_X = 450;
    private final int FIELD_X = 580;
    
    public ForgotPasswordView() {
        setTitle("Quên Mật Khẩu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100,1000, 650); 
        setLocationRelativeTo(null);
        setResizable(false);
        
        // --- Tải ảnh gốc (KHÔNG resize ở đây) ---
        Image bgImage = null;
        try {
            // Tên ảnh trong code gốc của bạn là signUpView.jpg, tôi giữ nguyên theo bạn gửi
            // Nếu bạn muốn dùng ảnh riêng cho ForgotPassword, hãy đảm bảo đường dẫn đúng
            bgImage = new ImageIcon(getClass().getResource("/images/signUpView.jpg")).getImage(); 
        } catch (Exception e) {
             System.err.println("Warning: Background image not found.");
        }
        
        // ***************************************************************
        // *** TỐI ƯU: Chỉ truyền ảnh GỐC vào BackgroundPanel để load nhanh ***
        // ***************************************************************
        contentPane = new BackgroundPanel(bgImage); 
        contentPane.setLayout(null);
        setContentPane(contentPane);
        
        // ===== Tiêu đề =====
        JLabel lblTitle = new JLabel("QUÊN MẬT KHẨU");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 40)); 
        lblTitle.setForeground(new Color(0, 0, 153)); 
        lblTitle.setBounds(550, 70, 350, 50); 
        contentPane.add(lblTitle);

        // ===== Email =====
        JLabel lbl_email = new JLabel("Email:"); 
        lbl_email.setFont(new Font("Arial", Font.BOLD, 20)); 
        lbl_email.setBounds(LABEL_X, START_Y, 120, INPUT_HEIGHT);
        contentPane.add(lbl_email);

        textFieldEmail = new JTextField();
        textFieldEmail.setFont(new Font("Tahoma", Font.PLAIN, 18));
        textFieldEmail.setBounds(FIELD_X, START_Y, INPUT_WIDTH, INPUT_HEIGHT);
        contentPane.add(textFieldEmail);

        // ===== Số Điện Thoại =====
        JLabel lbl_phonenumber = new JLabel("SĐT:"); 
        lbl_phonenumber.setFont(new Font("Arial", Font.BOLD, 20)); 
        lbl_phonenumber.setBounds(LABEL_X, START_Y + 50, 150, INPUT_HEIGHT);
        contentPane.add(lbl_phonenumber);

        textFieldPhoneNumber = new JTextField();
        textFieldPhoneNumber.setFont(new Font("Tahoma", Font.PLAIN, 18));
        textFieldPhoneNumber.setBounds(FIELD_X, START_Y + 50, INPUT_WIDTH, INPUT_HEIGHT);
        contentPane.add(textFieldPhoneNumber);

        // ===== Nút "Lấy lại mật khẩu" (Find) =====
        JButton btnFind = new JButton("Lấy lại mật khẩu"); 
        btnFind.setBounds(500, START_Y + 130, 200, 40);
        btnFind.setBackground(Color.CYAN);
        btnFind.setFont(new Font("Arial", Font.BOLD, 18));
        btnFind.setActionCommand("Find"); 
        contentPane.add(btnFind);

        // ===== Nút "Quay lại đăng nhập" =====
        JButton btnBackToLogin = new JButton("Quay lại đăng nhập");
        btnBackToLogin.setBounds(720, START_Y + 130, 230, 40);
        btnBackToLogin.setBackground(Color.CYAN);
        btnBackToLogin.setFont(new Font("Arial ", Font.BOLD, 18));
        
        btnBackToLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new LoginView().setVisible(true); 
                dispose();
            }
        });
        contentPane.add(btnBackToLogin);

        // ===== Gán Controller =====
        ForgotPasswordController controller = new ForgotPasswordController(this);
        btnFind.addActionListener(controller);
        
        this.setVisible(true);
    }
    
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                new ForgotPasswordView();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // Getters
    public JTextField getTextFieldEmail() { return textFieldEmail; }
    public JTextField getTextFieldPhoneNumber() { return textFieldPhoneNumber; }
}