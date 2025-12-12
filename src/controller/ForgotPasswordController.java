package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.JOptionPane;
import view.ForgotPasswordView;
import repository.UserRepositoryImpl;
import view.LoginView;

public class ForgotPasswordController implements ActionListener {

    private ForgotPasswordView view;
    private UserRepositoryImpl userRepo;
    private String generatedOTP; // Lưu mã OTP hệ thống tạo ra để so sánh
    private String verifiedEmail; // Lưu email đã được xác thực để đổi pass

    public ForgotPasswordController(ForgotPasswordView view) {
        this.view = view;
        this.userRepo = new UserRepositoryImpl();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.getBtnSendCode()) {
            handleSendCode();
        } else if (e.getSource() == view.getBtnConfirmReset()) {
            handleResetPassword();
        }
    }

    // 1. Xử lý nút "Gửi mã xác nhận"
    private void handleSendCode() {
        String email = view.getTxtEmail().getText().trim();
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập Email!");
            return;
        }
        // Kiểm tra Email có tồn tại trong hệ thống không
        if (userRepo.checkEmailExists(email)) {
            // Tạo mã OTP ngẫu nhiên 6 số
            Random rand = new Random();
            int otpNum = 100000 + rand.nextInt(900000);
            generatedOTP = String.valueOf(otpNum);
            verifiedEmail = email; // Lưu lại email để tí nữa đổi pass
            // GIẢ LẬP GỬI EMAIL: Hiển thị OTP lên Dialog thay vì gửi mail thật
            JOptionPane.showMessageDialog(view, 
                "Hệ thống đã gửi mã xác nhận đến email: " + email + "\n" +
                "Mã OTP của bạn là: " + generatedOTP + "\n" +
                "(Vui lòng ghi nhớ mã này để nhập vào bước sau)",
                "Giả lập Email Server", JOptionPane.INFORMATION_MESSAGE);

            // Hiện panel nhập OTP và Mật khẩu mới
            view.getPnlReset().setVisible(true);
            view.getTxtEmail().setEditable(false); // Khóa ô email lại
            view.getBtnSendCode().setEnabled(false); // Khóa nút gửi lại
        } else {
            JOptionPane.showMessageDialog(view, "Email này chưa được đăng ký trong hệ thống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    // 2. Xử lý nút "Xác nhận đổi mật khẩu"
    private void handleResetPassword() {
        String inputOTP = view.getTxtOTP().getText().trim();
        String newPass = new String(view.getTxtNewPass().getPassword());
        String confirmPass = new String(view.getTxtConfirmPass().getPassword());
        // Validate
        if (inputOTP.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }
        // Kiểm tra OTP
        if (!inputOTP.equals(generatedOTP)) {
            JOptionPane.showMessageDialog(view, "Mã OTP không chính xác!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Kiểm tra mật khẩu khớp nhau
        if (!newPass.equals(confirmPass)) {
            JOptionPane.showMessageDialog(view, "Mật khẩu xác nhận không khớp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (newPass.length() < 6) {
             JOptionPane.showMessageDialog(view, "Mật khẩu phải từ 6 ký tự trở lên!", "Lỗi", JOptionPane.WARNING_MESSAGE);
             return;
        }
        // THÀNH CÔNG -> Gọi Repo lưu mật khẩu mới
        userRepo.updatePassword(verifiedEmail, newPass);
        JOptionPane.showMessageDialog(view, "Đổi mật khẩu thành công!\nVui lòng đăng nhập lại.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
        // Quay về màn hình đăng nhập
        new LoginView().setVisible(true);
        view.dispose();
    }
}