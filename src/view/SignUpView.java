package view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import controller.SignUpController; // Import Controller logic

public class SignUpView extends JFrame {

    private static final long serialVersionUID = 1L;
    
    // Tên biến theo code logic thứ hai (SignUpView)
    private JTextField textField_name;
    private JPasswordField passwordField; 
    private JTextField textField_address;
    private JTextField textField_phonenumber;
    private JTextField textField_email; 
    
    // Các hằng số vị trí & kích thước theo giao diện thứ nhất (950x550)
    private final int START_Y = 130;
    private final int SPACING = 50;
    private final int INPUT_WIDTH = 250;
    private final int LABEL_X = 450; 
    private final int FIELD_X = 550; 
    private final Color TITLE_COLOR = new Color(255, 102, 204); // Màu hồng nhạt
    private final Color BTN_COLOR = Color.YELLOW;

    private JPanel contentPane;
    
    public SignUpView() {
        setTitle("Đăng Ký");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Kích thước từ code giao diện thứ nhất
        setBounds(100, 100, 950, 550);
        setLocationRelativeTo(null);
        setResizable(false);

        // ===== 1. Tải và Thiết lập Ảnh nền =====
        Image bgImage = null;
        try {
            // Sử dụng tên ảnh từ code giao diện thứ nhất (đã sửa đường dẫn)
            bgImage = new ImageIcon(getClass().getResource("/images/SignUpView.jpg")).getImage();
        } catch (Exception e) {
            System.err.println("Warning: Background image not found.");
        }
        
        // Sử dụng lớp BackgroundPanel riêng biệt của bạn
        contentPane = new BackgroundPanel(bgImage);
        setContentPane(contentPane);

        Font titleFont = new Font("Arial", Font.BOLD, 36);
        
        // ===== 2. Tiêu đề "Đăng Kí" =====
        JLabel lblTitle = new JLabel("Đăng Kí");
        lblTitle.setFont(titleFont);
        lblTitle.setForeground(TITLE_COLOR); 
        lblTitle.setBounds(550, 50, 200, 40); // Vị trí theo code thứ nhất
        contentPane.add(lblTitle);
        
        // ===== 3. Các trường nhập liệu (Sử dụng hàm tiện ích từ code logic thứ hai) =====
        
        // 1. Họ Tên (startY)
        textField_name = addLabelAndField("Họ Tên:", FIELD_X, START_Y);

        // 2. Mật Khẩu (startY + spacing)
        passwordField = (JPasswordField) addLabelAndField("Mật Khẩu:", FIELD_X, START_Y + SPACING);

        // 3. Địa Chỉ (startY + spacing * 2)
        textField_address = addLabelAndField("Địa Chỉ:", FIELD_X, START_Y + SPACING * 2);

        // 4. SĐT (startY + spacing * 3)
        textField_phonenumber = addLabelAndField("SĐT:", FIELD_X, START_Y + SPACING * 3);

        // 5. Email (startY + spacing * 4)
        textField_email = addLabelAndField("Email:", FIELD_X, START_Y + SPACING * 4);

        // ===== 4. Nút Đăng Kí và Quay lại Đăng nhập =====
        
        // Nút Đăng kí (Vị trí và kích thước theo code giao diện thứ nhất)
        JButton btnRegister = createButton("Đăng kí", 400, START_Y + SPACING * 5 + 20, BTN_COLOR);
        btnRegister.setActionCommand("Save"); // ActionCommand theo logic SignUpController

        // Nút Quay lại Đăng nhập (Vị trí và kích thước theo code giao diện thứ nhất)
        JButton btnBackToLogin = createButton("Quay lại đăng nhập", 580, START_Y + SPACING * 5 + 20, BTN_COLOR);
        btnBackToLogin.setBounds(580, START_Y + SPACING * 5 + 20, 230, 40); // Điều chỉnh kích thước
        
        // ===== 5. Gán Controller và Xử lý Sự kiện (Theo code logic thứ hai) =====
        SignUpController controller = new SignUpController(this);
        btnRegister.addActionListener(controller);
        
        btnBackToLogin.setActionCommand("Back");
        btnBackToLogin.addActionListener(controller);
        
    }
    
    // Phương thức chung để thêm Label và Field (Lấy từ logic thứ hai, điều chỉnh vị trí)
    private JTextField addLabelAndField(String text, int fieldX, int y) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Tahoma", Font.BOLD, 18));
        label.setForeground(Color.BLACK); // Màu label (chọn màu đen cho rõ ràng)
        label.setBounds(LABEL_X, y, 100, 30); // Vị trí LABEL_X = 450
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        contentPane.add(label);

        JTextField field;
        if (text.contains("Mật Khẩu")) {
            field = new JPasswordField();
        } else {
            field = new JTextField();
        }
        
        field.setFont(new Font("Tahoma", Font.PLAIN, 18));
        field.setBounds(fieldX, y, INPUT_WIDTH, 30); // Vị trí FIELD_X = 550, INPUT_WIDTH = 250
        contentPane.add(field);
        return field;
    }

    // Phương thức tạo Button (Lấy từ logic thứ hai)
    private JButton createButton(String text, int x, int y, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Tahoma", Font.BOLD, 16)); // Font size lớn hơn cho nút chính
        btn.setBackground(color);
        btn.setForeground(Color.BLACK);
        btn.setBounds(x, y, 150, 40); 
        contentPane.add(btn);
        return btn;
    }

    // ===== Getters (Theo code logic thứ hai) =====
    public JTextField getTextFieldName() { return textField_name; }
    public JTextField getTextFieldEmail() { return textField_email; }
    public JTextField getTextFieldAddress() { return textField_address; }
    public JTextField getTextFieldPhoneNumber() { return textField_phonenumber; }
    public JPasswordField getPasswordField() { return passwordField; }
    
    // Role cố định là "USER"
    public String getSelectedRole() { return "USER"; } 

    // Phương thức xóa form (Theo code logic thứ hai)
    public void xoaForm() {
        textField_name.setText("");
        textField_email.setText("");
        textField_address.setText("");
        textField_phonenumber.setText("");
        passwordField.setText("");
    }
    
    // Main method
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                SignUpView frame = new SignUpView();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}