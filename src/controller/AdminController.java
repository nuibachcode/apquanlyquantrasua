package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import view.AdminView;
import model.User;

// Import các Sub-Controller mới
import controller.user.UserManagementController;
import controller.product.ProductManagementController;
import controller.bill.BillManagementController;
import controller.analytics.AnalyticsController;

public class AdminController implements ActionListener {

    private AdminView adminView;
    private UserManagementController userController;
    private ProductManagementController productController;
    private BillManagementController billController;
    private AnalyticsController analyticsController;
    
    public AdminController(AdminView adminView, User loggedInUser) {
        this.adminView = adminView;
        
        // Khởi tạo các Controller chức năng
        this.userController = new UserManagementController(adminView);
        this.productController = new ProductManagementController(adminView);
        this.billController = new BillManagementController(adminView);
        this.analyticsController = new AnalyticsController(adminView);
        
        // Tải dữ liệu ban đầu
        userController.loadAccounts();
        productController.loadProducts();
        billController.loadBills();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        // PHÂN TÍCH LỆNH VÀ CHUYỂN TIẾP CHO CONTROLLER PHÙ HỢP
        
        // 1. Lệnh Quản lý Người dùng (USER)
        if (command.startsWith("USER_")) {
            userController.actionPerformed(e);
            return;
        }

        // 2. Lệnh Quản lý Sản phẩm (PRODUCT)
        if (command.startsWith("PRODUCT_")) {
            productController.actionPerformed(e);
            return;
        }

        // 3. Lệnh Quản lý Hóa đơn (BILL)
        if (command.startsWith("BILL_")) {
            billController.actionPerformed(e);
            return;
        }
        
        // 4. Lệnh Thống kê (ANALYTICS)
        if (command.startsWith("ANALYTICS_")) {
            analyticsController.actionPerformed(e);
            return;
        }
        
        // 5. Lệnh Chung (Global)
        switch (command) {
            case "SignUp": 
                // new SignUpView().setVisible(true); // Cần lớp SignUpView
                JOptionPane.showMessageDialog(adminView, "Mở màn hình đăng ký.", "Info", JOptionPane.INFORMATION_MESSAGE);
                break;
            default:
                JOptionPane.showMessageDialog(adminView, "Lệnh không xác định: " + command, "Error", JOptionPane.ERROR_MESSAGE);
                break;
        }
    }
    
    // Phương thức public để Controller khác gọi cập nhật (ví dụ BillController gọi)
    public BillManagementController getBillController() {
        return billController;
    }
    // ... cần các Getters khác nếu logic bị liên kết.
}