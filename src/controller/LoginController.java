package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

import model.User;
import repository.UserRepositoryImpl;
import utils.SecurityUtils;
import view.AdminView.AdminView; // Đảm bảo đúng package của bạn
import view.LoginView;
import view.SignUpView;
import view.ForgotPasswordView; // View Quên mật khẩu xịn của bạn
import view.UserView.UserView;  // <--- IMPORT MỚI
import controller.UserController.UserController; // <--- IMPORT MỚI

public class LoginController implements ActionListener {

    private LoginView loginView;
    private UserRepositoryImpl userRepository;

    public LoginController(LoginView loginView) {
        this.loginView = loginView;
        this.userRepository = new UserRepositoryImpl();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {
            case "Login":
                handleLogin();
                break;
                
            case "Forget Password ?":
                // Mở màn hình Quên Mật Khẩu riêng (ForgotPasswordView)
                new ForgotPasswordView().setVisible(true);
                loginView.dispose();
                break;
                
            case "SignUp":
                new SignUpView().setVisible(true);
                loginView.dispose();
                break;
                
            default:
                break;
        }
    }

    private void handleLogin() {
        String email = loginView.getTextFieldEmail().getText();
        String rawPassword = new String(loginView.getPasswordField().getPassword());

        // 1. Mã hóa mật khẩu người dùng vừa nhập
        String hashedPassword = SecurityUtils.hashPassword(rawPassword);

        // 2. Tìm kiếm trong CSDL (so sánh chuỗi mã hóa)
        User user = userRepository.findByEmailAndPassword(email, hashedPassword);

        if (user != null) {
            JOptionPane.showMessageDialog(loginView, "Đăng Nhập Thành Công!", "Success", JOptionPane.INFORMATION_MESSAGE);
            
            if ("ADMIN".equals(user.getRole())) {
                // Chuyển sang màn hình Admin
                new AdminView(user).setVisible(true);
            } else {
                // Chuyển sang màn hình User (Đổi từ ProductView -> UserView)
                UserView userView = new UserView(user);
                new UserController(userView); // Gắn Controller
                userView.setVisible(true);
            }
            
            loginView.dispose();
        } else {
            JOptionPane.showMessageDialog(loginView, "Sai Email Hoặc Mật Khẩu!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}