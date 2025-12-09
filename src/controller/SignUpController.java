package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

import view.AdminView;
import view.LoginView;
import view.SignUpView;
import model.User;
import repository.UserRepositoryImpl;

public class SignUpController implements ActionListener {

    private SignUpView signUpView;
    private UserRepositoryImpl userRepository;

    public SignUpController(SignUpView signUpView) {
        this.signUpView = signUpView;
        this.userRepository = new UserRepositoryImpl();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {
            case "Save":
                handleSignUp();
                break;
            case "Back":
                new LoginView().setVisible(true);
                signUpView.dispose();
                break;
            default:
                break;
        }
    }

    private void handleSignUp() {
        try {
            String name = signUpView.getTextFieldName().getText();
            String email = signUpView.getTextFieldEmail().getText();
            String password = new String(signUpView.getPasswordField().getPassword());
            String address = signUpView.getTextFieldAddress().getText();
            String phoneNumber = signUpView.getTextFieldPhoneNumber().getText();
            String role = signUpView.getSelectedRole();

            // 1. Kiểm tra rỗng
            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || address.isEmpty() || role.isEmpty()) {
                JOptionPane.showMessageDialog(signUpView, "Bạn Phải Điền Đủ Tất Cả Các Ô", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 2. Validate Số điện thoại (Mới thêm vào)
            // \\d đại diện cho ký tự số, {10} nghĩa là chính xác 10 lần
            if (!phoneNumber.matches("\\d{10}")) {
                JOptionPane.showMessageDialog(signUpView, "Số điện thoại phải là 10 chữ số!", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 3. Validate Email
            if (!email.contains("@") || !email.contains(".")) {
                JOptionPane.showMessageDialog(signUpView, "Định dạng Email không đúng", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 4. Kiểm tra user tồn tại
            // Lưu ý: Thường logic này chỉ nên check Email, không cần check password
            User existingUser = userRepository.findByEmailAndPassword(email, password);
            if (existingUser != null) {
                JOptionPane.showMessageDialog(signUpView, "Email Đã Tồn Tại", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 5. Tạo và lưu user mới
            User newUser = new User(name, email, phoneNumber, address, password, role);
            userRepository.save(newUser);
            
            JOptionPane.showMessageDialog(signUpView, "Đăng Ký Thành Công", "Success", JOptionPane.INFORMATION_MESSAGE);
            
            // Xóa form và chuyển màn hình
            signUpView.xoaForm();
            signUpView.dispose();
            new LoginView().setVisible(true);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(signUpView, "Đã Xảy Ra Lỗi Trong Quá Trình Đăng Kí: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}