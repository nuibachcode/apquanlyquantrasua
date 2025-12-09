package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

import view.LoginView;
import view.SignUpView;
import model.User;
import repository.UserRepositoryImpl;
import utils.SecurityUtils;

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
            String rawPassword = new String(signUpView.getPasswordField().getPassword());
            String address = signUpView.getTextFieldAddress().getText();
            String phoneNumber = signUpView.getTextFieldPhoneNumber().getText();
            String role = signUpView.getSelectedRole();

            // 1. Kiểm tra rỗng
            if (name.isEmpty() || email.isEmpty() || rawPassword.isEmpty() || address.isEmpty() || role.isEmpty()) {
                JOptionPane.showMessageDialog(signUpView, "Bạn Phải Điền Đủ Tất Cả Các Ô", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // 2. Validate SĐT (10 số)
            if (!phoneNumber.matches("\\d{10}")) {
                JOptionPane.showMessageDialog(signUpView, "Số điện thoại phải là 10 chữ số!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // 3. Validate Email
            if (!email.contains("@") || !email.contains(".")) {
                JOptionPane.showMessageDialog(signUpView, "Định dạng Email không đúng", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 4. Kiểm tra user tồn tại
            if (userRepository.findByEmail(email) != null) {
                JOptionPane.showMessageDialog(signUpView, "Email Đã Tồn Tại", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 5. MÃ HÓA MẬT KHẨU
            String hashedPassword = SecurityUtils.hashPassword(rawPassword);

            // 6. Lưu User mới
            User newUser = new User(name, email, phoneNumber, address, hashedPassword, role);
            userRepository.save(newUser);
            
            JOptionPane.showMessageDialog(signUpView, "Đăng Ký Thành Công", "Success", JOptionPane.INFORMATION_MESSAGE);
            
            signUpView.xoaForm();
            signUpView.dispose();
            new LoginView().setVisible(true);
            
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(signUpView, "Lỗi Đăng Ký: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}