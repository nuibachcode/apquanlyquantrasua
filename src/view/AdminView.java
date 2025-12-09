package view;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import controller.AdminController;
import model.User;
import model.Product;
import model.Bill;
// Import các Panel mới
import view.panels.AccountManagementPanel;
import view.panels.ProductManagementPanel;
import view.panels.BillManagementPanel;
import view.panels.AnalyticsPanel;
import controller.bill.BillManagementController;
import controller.user.UserManagementController;
import controller.product.ProductManagementController;
import controller.analytics.AnalyticsController;
import view.LoginView;

public class AdminView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTabbedPane tabbedPane;
    private JButton btnLogout;
    
    // Khai báo các Panel chuyên biệt
    private AccountManagementPanel userPanel;
    private ProductManagementPanel productPanel;
    private BillManagementPanel billPanel;
    private AnalyticsPanel analyticsPanel;

    public AdminView(User loggedInUser) {
        setTitle("Admin Panel - Quản Lý Cửa Hàng");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1200, 750);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        // --- Header ---
        JLabel lblAdminPanel = new JLabel("BẢNG ĐIỀU KHIỂN QUẢN TRỊ (ADMIN)");
        lblAdminPanel.setFont(new Font("Tahoma", Font.BOLD, 26));
        lblAdminPanel.setHorizontalAlignment(SwingConstants.CENTER);
        lblAdminPanel.setForeground(new Color(0, 102, 153));

        btnLogout = new JButton("Đăng Xuất");
        btnLogout.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnLogout.setBackground(new Color(255, 102, 102));
        btnLogout.addActionListener(e ->{
            dispose();
             new LoginView().setVisible(true); 
        });
    

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(lblAdminPanel, BorderLayout.CENTER);
        headerPanel.add(btnLogout, BorderLayout.EAST);
        contentPane.add(headerPanel, BorderLayout.NORTH);

        // --- TabbedPane ---
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setFont(new Font("Tahoma", Font.PLAIN, 16));
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        // Khởi tạo các Panel chức năng
        userPanel = new AccountManagementPanel();
        productPanel = new ProductManagementPanel();
        billPanel = new BillManagementPanel();
        analyticsPanel = new AnalyticsPanel();
        
        // Thêm Panel vào TabbedPane
        tabbedPane.addTab("Quản lý Tài khoản", null, userPanel, null);
        tabbedPane.addTab("Quản lý Sản phẩm", null, productPanel, null);
        tabbedPane.addTab("Quản lý Hóa đơn", null, billPanel, null);
        tabbedPane.addTab("Thống kê & Báo cáo", null, analyticsPanel, null);

        // Khởi tạo Controller chính (điều phối)
        AdminController adminController = new AdminController(this, loggedInUser);
        
        // Lấy các Controller chuyên biệt để gắn vào các Panel tương ứng
        UserManagementController userController = new UserManagementController(this);
        ProductManagementController productController = new ProductManagementController(this);
        BillManagementController billController = new BillManagementController(this);
        AnalyticsController analyticsController = new AnalyticsController(this);
        
        // GẮN LISTENER (SỬ DỤNG CONTROLLER CHUYÊN BIỆT)
        userPanel.addController(userController);
        productPanel.addController(productController);
        analyticsPanel.addController(analyticsController);
        
        // Bill Panel cần controller của nó để xử lý TableModelEvent
        billPanel.addController(billController, billController); 
        
        // Tải dữ liệu ban đầu
        userController.loadAccounts();
        productController.loadProducts();
        billController.loadBills();


        setLocationRelativeTo(null);
        setVisible(true);
    }

    // --- GETTERS & SETTERS (Chuyển tiếp cho các Panel) ---
    
    // User Panel Getters
    public String getUserIdInput() { return userPanel.getUserIdInput(); }
    public String getUserRoleInput() { return userPanel.getUserRoleInput(); }
    public void updateUserList(List<User> users) { userPanel.updateUserList(users); }
    public void clearUserFields() { userPanel.clearFields(); }
    
    // Product Panel Getters
    public String getProductCodeInput() { return productPanel.getProductCodeInput(); }
    public String getProductNameInput() { return productPanel.getProductNameInput(); }
    public String getProductPriceInput() { return productPanel.getProductPriceInput(); }
    
    // <<< THÊM GETTER MỚI CHO ẢNH TỪ PRODUCT PANEL >>>
    public String getProductImageInput() { return productPanel.getProductImageInput(); }
    
    public void updateProductList(List<Product> products) { productPanel.updateProductList(products); }
    public void clearProductFields() { productPanel.clearFields(); }
    
    // Bill Panel Getters
    public void updateBillList(List<Bill> bills) { billPanel.updateBillList(bills); }
    
    // Analytics Panel Getters
    public String getDateFilterSelection() { return analyticsPanel.getDateFilterSelection(); }
    public JPanel getChartDisplayPanel() { return analyticsPanel.getChartDisplayPanel(); }
    public void setTotalRevenue(double revenue) { analyticsPanel.setTotalRevenue(revenue); }
}