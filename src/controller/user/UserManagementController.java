package controller.user;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;

import model.User;
import repository.UserRepositoryImpl;
import repository.IRepository.IUserRepository;
import view.AdminView;

public class UserManagementController implements ActionListener {
    
    private AdminView adminView;
    private IUserRepository userRepository;

    public UserManagementController(AdminView adminView) {
        this.adminView = adminView;
        this.userRepository = new UserRepositoryImpl(); 
    }

    // Tải dữ liệu từ DB và cập nhật View
    public void loadAccounts() {
        List<User> users = userRepository.findAll();
        adminView.updateUserList(users);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        
        switch (command) {
            // Đã loại bỏ case "USER_VIEW"
            case "USER_UPDATE": updateUserRole(); break;
            case "USER_DELETE": deleteUser(); break;
            // Đã loại bỏ case "USER_CLEAR"
        }
    }
    
    // --- HÀM CHI TIẾT ---
    
    // Đã loại bỏ private void viewUserDetail() {}

    private void updateUserRole() {
        String userIdStr = adminView.getUserIdInput();
        String newRole = adminView.getUserRoleInput();
        
        if (userIdStr.isEmpty() || newRole.isEmpty()) {
            JOptionPane.showMessageDialog(adminView, "Vui lòng chọn tài khoản và nhập Role mới.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            // **Quan trọng:** Đảm bảo kiểu dữ liệu ID (MaNV) trong Java (Integer) khớp với CSDL.
            // (Nhắc lại theo thông tin đã lưu: Kiểu dữ liệu ID trong Entity Java và cơ sở dữ liệu phải khớp nhau.
            // Nếu MaNV là Long trong Entity, bạn cần dùng Long.parseLong() và thay đổi kiểu dữ liệu tương ứng.)
            int userId = Integer.parseInt(userIdStr); 
            User existingUser = userRepository.findById(userId);

            if (existingUser != null) {
                existingUser.setRole(newRole); // CHỈ CẬP NHẬT ROLE
                userRepository.update(existingUser);
                JOptionPane.showMessageDialog(adminView, "Cập nhật Role cho tài khoản ID: " + userId + " thành công.", "Success", JOptionPane.INFORMATION_MESSAGE);
                adminView.clearUserFields();
                loadAccounts();
            } else {
                JOptionPane.showMessageDialog(adminView, "Không tìm thấy tài khoản để sửa.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(adminView, "ID không hợp lệ. Vui lòng kiểm tra lại kiểu dữ liệu ID.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(adminView, "Lỗi cập nhật Role: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteUser() {
        String userIdStr = adminView.getUserIdInput();
        if (userIdStr.isEmpty()) {
            JOptionPane.showMessageDialog(adminView, "Vui lòng chọn tài khoản cần xóa.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            int userId = Integer.parseInt(userIdStr);
            User userToDelete = userRepository.findById(userId);
            
            if (userToDelete != null) {
                userRepository.delete(userToDelete);
                JOptionPane.showMessageDialog(adminView, "Xóa tài khoản ID: " + userId + " thành công.", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadAccounts();
                adminView.clearUserFields();
            } else {
                JOptionPane.showMessageDialog(adminView, "Không tìm thấy tài khoản", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
             JOptionPane.showMessageDialog(adminView, "ID không hợp lệ. Vui lòng kiểm tra lại kiểu dữ liệu ID.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
             JOptionPane.showMessageDialog(adminView, "Lỗi xóa tài khoản: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}