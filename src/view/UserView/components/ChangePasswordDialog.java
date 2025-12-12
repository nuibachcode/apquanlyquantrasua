package view.UserView.components;

import model.User;
import repository.UserRepositoryImpl; 
import javax.swing.*;
import java.awt.*;
import utils.SecurityUtils;

public class ChangePasswordDialog extends JDialog {

    private JPasswordField txtOldPass, txtNewPass, txtConfirmPass;
    private User user;
    private UserRepositoryImpl userRepo;

    public ChangePasswordDialog(Dialog parent, User user) {
        super(parent, "Đổi Mật Khẩu", true);
        this.user = user;
        this.userRepo = new UserRepositoryImpl(); // Khởi tạo Repo
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Form
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Mật khẩu cũ:"));
        txtOldPass = new JPasswordField();
        panel.add(txtOldPass);

        panel.add(new JLabel("Mật khẩu mới:"));
        txtNewPass = new JPasswordField();
        panel.add(txtNewPass);

        panel.add(new JLabel("Nhập lại MK mới:"));
        txtConfirmPass = new JPasswordField();
        panel.add(txtConfirmPass);

        add(panel, BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel();
        JButton btnSave = new JButton("Xác nhận");
        btnSave.setBackground(new Color(0, 153, 76));
        btnSave.setForeground(Color.WHITE);
        
        JButton btnCancel = new JButton("Hủy");

        btnSave.addActionListener(e -> handleChangePassword());
        btnCancel.addActionListener(e -> dispose());

        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void handleChangePassword() {
        String oldPass = new String(txtOldPass.getPassword());
        String newPass = new String(txtNewPass.getPassword());
        String confirmPass = new String(txtConfirmPass.getPassword());
        
        // 1. Kiểm tra rỗng
        if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        // --- LƯU Ý SỬA LỖI QUAN TRỌNG NHẤT ---
        // Giả định: Đăng nhập thành công nghĩa là user.getMatKhau() là mật khẩu đã lưu/hash.
        // Nếu Đăng nhập đang dùng HASHING, bạn cần dùng hàm kiểm tra HASHING ở đây.
        // VÌ CODE CỦA BẠN CHỈ SO SÁNH CHUỖI, TÔI GIẢ ĐỊNH BẠN ĐANG LƯU PLAIN TEXT
        String hashedPassword = SecurityUtils.hashPassword(oldPass);
        // 2. Kiểm tra mật khẩu cũ có đúng không
        if (!user.getMatKhau().equals(hashedPassword)) { 
            JOptionPane.showMessageDialog(this, "Mật khẩu cũ không chính xác! (Lưu ý: Mật khẩu có thể đã được mã hóa)", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 3. Kiểm tra mật khẩu mới có trùng khớp không
        if (!newPass.equals(confirmPass)) {
            JOptionPane.showMessageDialog(this, "Mật khẩu xác nhận không khớp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // 4. Kiểm tra độ dài (tùy chọn)
        if (newPass.length() < 6) {
            JOptionPane.showMessageDialog(this, "Mật khẩu mới phải từ 6 ký tự trở lên!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String hashedPasswordNew = SecurityUtils.hashPassword(newPass);
        // 5. Cập nhật và Lưu
        user.setMatKhau(hashedPasswordNew); // Cập nhật mật khẩu Plain Text mới
        
        try {
            // QUAN TRỌNG: Gọi Repository để lưu vào file/DB
            userRepo.update(user); 
            JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công!");
            dispose();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu mật khẩu mới: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}