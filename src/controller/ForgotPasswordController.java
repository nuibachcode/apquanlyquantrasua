package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import model.User;
import view.ForgotPasswordView;
import repository.UserRepositoryImpl; // Đảm bảo import đúng

public class ForgotPasswordController implements ActionListener {

    private ForgotPasswordView forgotPasswordView;
    private UserRepositoryImpl userRepository;

    public ForgotPasswordController(ForgotPasswordView forgotPasswordView) {
        this.forgotPasswordView = forgotPasswordView;
        this.userRepository = new UserRepositoryImpl();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.equals("Find")) { // Action Command từ nút "Lấy lại mật khẩu"
            handleFindUser();
        }
    }

    private void handleFindUser() {
        String email = forgotPasswordView.getTextFieldEmail().getText().trim();
        String phoneNumberStr = forgotPasswordView.getTextFieldPhoneNumber().getText().trim();
        
        if (email.isEmpty() || phoneNumberStr.isEmpty()) {
            JOptionPane.showMessageDialog(forgotPasswordView, 
                                          "Vui lòng điền đầy đủ Email và Số Điện Thoại.", 
                                          "Lỗi Nhập Liệu", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Sử dụng phương thức tối ưu để tìm kiếm User theo cả Email và SĐT
            // *Bạn cần đảm bảo phương thức này tồn tại trong UserRepositoryImpl*
            User userFound = userRepository.findByEmailAndPassword(email, phoneNumberStr);

            if (userFound != null) {
                // Hiển thị MẬT KHẨU
                String message = String.format(
                    "Tìm Thấy Người Dùng: %s\nEmail: %s\nMật Khẩu Của Bạn Là: %s", 
                    userFound.getTen(), userFound.getEmail(), userFound.getMatKhau());
                    
                JOptionPane.showMessageDialog(forgotPasswordView, 
                                              message, 
                                              "Tìm Thấy Người Dùng", 
                                              JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(forgotPasswordView, 
                                              "Không tìm thấy người dùng. Vui lòng kiểm tra lại Email hoặc Số Điện Thoại.", 
                                              "Lỗi", 
                                              JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(forgotPasswordView, 
                                          "Lỗi truy cập dữ liệu: " + ex.getMessage(), 
                                          "Lỗi Hệ Thống", 
                                          JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}