package view.UserView.components;

import model.User;
// Nếu bạn có UserRepository thì import vào để lưu
import repository.UserRepositoryImpl; 
import javax.swing.*;
import java.awt.*;

public class ChangePasswordDialog extends JDialog {

    private JPasswordField txtOldPass, txtNewPass, txtConfirmPass;
    private User user;

    public ChangePasswordDialog(Dialog parent, User user) {
        super(parent, "Đổi Mật Khẩu", true);
        this.user = user;
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
        if (oldPass.isEmpty() || newPass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        // 2. Kiểm tra mật khẩu cũ có đúng không
        // (Lưu ý: user.getMatKhau() hoặc user.getPassword() tùy model của bạn)
        if (!user.getMatKhau().equals(oldPass)) { 
            JOptionPane.showMessageDialog(this, "Mật khẩu cũ không chính xác!", "Lỗi", JOptionPane.ERROR_MESSAGE);
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

        // 5. Cập nhật và Lưu
        user.setMatKhau(newPass);
        
        // QUAN TRỌNG: Gọi Repository để lưu vào file/DB
        new UserRepositoryImpl().update(user); 

        JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công!");
        dispose();
    }
}