package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

import model.User;
import repository.UserRepositoryImpl;
import view.AdminView.AdminView;
import view.ForgotPasswordView;
import view.LoginView;
import view.UserView.UserView;
import view.SignUpView;

// [QUAN TRỌNG 1] Thêm dòng import này
import controller.UserController.UserController; 
// Nếu có AdminController thì import luôn: import controller.AdminController.AdminController;
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
        String password = new String(loginView.getPasswordField().getPassword());

        User user = userRepository.findByEmailAndPassword(email, password);
        
        if (user != null) {
            JOptionPane.showMessageDialog(loginView, "Đăng Nhập Thành Công!", "Success", JOptionPane.INFORMATION_MESSAGE);
            
            if (user.getRole().equals("ADMIN")) {
                // Nếu bên Admin cũng bị trắng, bạn cần sửa tương tự như dưới:
                // AdminView adminView = new AdminView(user);
                // new AdminController(adminView);
                // adminView.setVisible(true);
                new AdminView(user).setVisible(true); 
                
            } else {
                // [QUAN TRỌNG 2] SỬA ĐOẠN NÀY
                // 1. Tạo View
                UserView productView = new UserView(user);
                
                // 2. KẾT NỐI CONTROLLER (Đây là bước nạp dữ liệu)
                new UserController(productView); 
                
                // 3. Hiển thị View
                productView.setVisible(true);
            }
            
            loginView.dispose();
        } else {
            JOptionPane.showMessageDialog(loginView, "Sai Email Hoặc Mật Khẩu!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}