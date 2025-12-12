package view.UserView.components;

import model.User;
import javax.swing.*;
import java.awt.*;
// QUAN TRỌNG: Import UserRepositoryImpl
import repository.UserRepositoryImpl; 

public class ProfileDialog extends JDialog {
    
    private JTextField txtName, txtEmail, txtPhone, txtAddress;
    private User user;
    // THÊM: Repository để thực hiện lưu
    private UserRepositoryImpl userRepo; 
    
    public ProfileDialog(JFrame parent, User user) {
        super(parent, "Thông Tin Cá Nhân", true);
        this.user = user;
        this.userRepo = new UserRepositoryImpl(); // Khởi tạo Repo
        initComponents();
        loadUserData();
    }

    private void initComponents() {
        setSize(450, 450);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());
        
        // --- Panel Form ---
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 20));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // Họ và Tên
        formPanel.add(new JLabel("Họ và Tên:"));
        txtName = new JTextField();
        formPanel.add(txtName);
        
        // Email (KHÔNG ĐƯỢC SỬA)
        formPanel.add(new JLabel("Email (Tài khoản):"));
        txtEmail = new JTextField();
        txtEmail.setEditable(false); 
        txtEmail.setBackground(new Color(230, 230, 230)); 
        txtEmail.setForeground(Color.DARK_GRAY);
        formPanel.add(txtEmail);
        
        // Số điện thoại
        formPanel.add(new JLabel("Số điện thoại:"));
        txtPhone = new JTextField();
        formPanel.add(txtPhone);
        
        // Địa chỉ giao hàng
        formPanel.add(new JLabel("Địa chỉ giao hàng:"));
        txtAddress = new JTextField();
        formPanel.add(txtAddress);
        
        // Mật khẩu
        formPanel.add(new JLabel("Mật khẩu:"));
        JButton btnChangePass = new JButton("Đổi mật khẩu");
        btnChangePass.setBackground(new Color(255, 102, 102));
        btnChangePass.setForeground(Color.WHITE);
        formPanel.add(btnChangePass);
        
        add(formPanel, BorderLayout.CENTER);
        
        // --- Panel Button Footer ---
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnSave = new JButton("Lưu Thay Đổi");
        btnSave.setBackground(new Color(0, 153, 76));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("Arial", Font.BOLD, 14));
        JButton btnCancel = new JButton("Đóng");
        
        // Sự kiện
        btnSave.addActionListener(e -> saveProfile());
        btnCancel.addActionListener(e -> dispose());
        
        // Sự kiện mở Dialog đổi mật khẩu
        btnChangePass.addActionListener(e -> {
            new ChangePasswordDialog(this, user).setVisible(true);
        });
        
        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void loadUserData() {
        if (user != null) {
            txtName.setText(user.getTen());
            txtEmail.setText(user.getEmail());
            txtPhone.setText(user.getSdt());
            txtAddress.setText(user.getDiaChi());
        }
    }

    private void saveProfile() {
        if (txtName.getText().trim().isEmpty() || txtPhone.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên và SĐT không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        user.setTen(txtName.getText());
        user.setSdt(txtPhone.getText());
        user.setDiaChi(txtAddress.getText());

        try {
            // GỌI REPOSITORY ĐỂ LƯU XUỐNG DB/FILE
            userRepo.update(user); 
            JOptionPane.showMessageDialog(this, "Cập nhật thông tin thành công!");
            // Sau khi lưu thành công, thông tin user trong bộ nhớ đã được cập nhật, 
            // có thể đóng dialog.
            dispose(); 
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}