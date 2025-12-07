package view;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelListener;
import javax.swing.event.TableModelEvent;

import controller.AdminController;
import model.User;
import model.Product;
import model.Bill; 

public class AdminView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTabbedPane tabbedPane;
    
    // TRƯỜNG NHẬP LIỆU CHUNG
    private JTextField tfUserId;
    private JComboBox<String> cmbUserRole; 
    private JTextField tfProductId, tfProductName, tfProductPrice;
    
    // THỐNG KÊ
    private JLabel lblTotalRevenue; 
    private JComboBox<String> cmbDateFilter;
    private JButton btnViewReport;           
    private JPanel chartDisplayPanel;       
    
    // Bảng dữ liệu
    private JTable userTable;
    private JTable productTable;
    private JTable billTable;
    private DefaultTableModel userTableModel;
    private DefaultTableModel productTableModel;
    private DefaultTableModel billTableModel;

    // Các nút cần kết nối Controller
    private JButton btnUserView, btnUserRoleChange, btnUserDelete, btnUserClear;
    private JButton btnProductAdd, btnProductUpdate, btnProductDelete, btnProductClear;
    private JButton btnBillViewAll;
    private JButton btnLogout;

    public AdminView(User loggedInUser) {
        setTitle("Admin Panel - Quản Lý Cửa Hàng");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Tăng kích thước cửa sổ để chứa biểu đồ
        setBounds(100, 100, 1200, 750); 
        
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        // Tiêu đề và Nút Đăng Xuất
        JLabel lblAdminPanel = new JLabel("BẢNG ĐIỀU KHIỂN QUẢN TRỊ (ADMIN)");
        lblAdminPanel.setFont(new Font("Tahoma", Font.BOLD, 26));
        lblAdminPanel.setHorizontalAlignment(SwingConstants.CENTER);
        lblAdminPanel.setForeground(new Color(0, 102, 153));
        
        btnLogout = new JButton("Đăng Xuất");
        btnLogout.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnLogout.setBackground(new Color(255, 102, 102));
        btnLogout.addActionListener(e -> {
            dispose();
            // new LoginView().setVisible(true); 
        });
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(lblAdminPanel, BorderLayout.CENTER);
        headerPanel.add(btnLogout, BorderLayout.EAST);
        contentPane.add(headerPanel, BorderLayout.NORTH);


        // Bộ chứa TabbedPane
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setFont(new Font("Tahoma", Font.PLAIN, 16));
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        // Khởi tạo các tab
        tabbedPane.addTab("Quản lý Tài khoản", null, createAccountManagementPanel(), null);
        tabbedPane.addTab("Quản lý Sản phẩm", null, createProductManagementPanel(), null);
        tabbedPane.addTab("Quản lý Hóa đơn", null, createBillManagementPanel(), null);
        tabbedPane.addTab("Thống kê & Báo cáo", null, createAnalyticsPanel(), null); 

        // Khởi tạo Controller sau khi tất cả các thành phần UI đã được tạo
        AdminController adminController = new AdminController(this, loggedInUser);
        
        // Thiết lập ActionCommand cho Controller
        // User Actions
        btnUserView.setActionCommand("USER_VIEW");
        btnUserRoleChange.setActionCommand("USER_UPDATE"); 
        btnUserDelete.setActionCommand("USER_DELETE");
        btnUserClear.setActionCommand("USER_CLEAR");

        // Product Actions
        btnProductAdd.setActionCommand("PRODUCT_ADD");
        btnProductUpdate.setActionCommand("PRODUCT_UPDATE");
        btnProductDelete.setActionCommand("PRODUCT_DELETE");
        btnProductClear.setActionCommand("PRODUCT_CLEAR");
        
        // Bill Actions
        btnBillViewAll.setActionCommand("BILL_VIEW_ALL");
        
        // Thống kê Actions
        btnViewReport.setActionCommand("ANALYTICS_VIEW_REPORT"); 
        
        // Gắn Listener cho Controller
        btnUserView.addActionListener(adminController);
        btnUserRoleChange.addActionListener(adminController);
        btnUserDelete.addActionListener(adminController);
        btnUserClear.addActionListener(adminController);
        
        btnProductAdd.addActionListener(adminController);
        btnProductUpdate.addActionListener(adminController);
        btnProductDelete.addActionListener(adminController);
        btnProductClear.addActionListener(adminController);
        
        btnBillViewAll.addActionListener(adminController);
        btnViewReport.addActionListener(adminController); 

        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    public AdminView() {
        this(null);
    }

    // =======================================================
    // # 1. QUẢN LÝ TÀI KHOẢN
    // =======================================================
    private JPanel createAccountManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel formPanel = new JPanel(new GridLayout(2, 3, 10, 10)); 
        formPanel.setBorder(BorderFactory.createTitledBorder("Thao tác Tài khoản"));
        
        tfUserId = new JTextField(10);
        tfUserId.setEditable(false); 
        cmbUserRole = new JComboBox<>(new String[]{"ADMIN", "USER"});
        
        // Hàng 1: Label
        formPanel.add(new JLabel("ID tài khoản (Chọn từ bảng):"));
        formPanel.add(new JLabel("Role mới:"));
        formPanel.add(new JLabel(""));
        
        // Hàng 2: Input Fields
        formPanel.add(tfUserId);
        formPanel.add(cmbUserRole);

        // Nút Thao tác
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        btnUserView = new JButton("Xem Chi Tiết");
        btnUserRoleChange = new JButton("Đổi Role");
        btnUserDelete = new JButton("Xóa");
        btnUserClear = new JButton("Làm mới");
        
        buttonPanel.add(btnUserView);
        buttonPanel.add(btnUserRoleChange);
        buttonPanel.add(btnUserDelete);
        buttonPanel.add(btnUserClear);
        
        formPanel.add(buttonPanel);
        panel.add(formPanel, BorderLayout.NORTH);
        
        // --- Table Panel (Center) ---
        userTableModel = new DefaultTableModel(new Object[] { "ID", "Tên", "Email", "SĐT", "Địa chỉ", "Role" }, 0);
        userTable = new JTable(userTableModel);
        userTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        userTable.getSelectionModel().addListSelectionListener(e -> fillUserFields()); 
        
        JScrollPane scrollPane = new JScrollPane(userTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    // =======================================================
    // # 2. QUẢN LÝ SẢN PHẨM
    // =======================================================
    private JPanel createProductManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel formPanel = new JPanel(new GridLayout(2, 5, 10, 10)); 
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin Sản phẩm"));
        
        tfProductId = new JTextField(10);
        tfProductId.setEditable(false); 
        tfProductName = new JTextField(10);
        tfProductPrice = new JTextField(10);
        
        // Hàng 1: Label
        formPanel.add(new JLabel("Mã SP:"));
        formPanel.add(new JLabel("Tên SP:"));
        formPanel.add(new JLabel("Giá:"));
        formPanel.add(new JLabel("")); 
        formPanel.add(new JLabel(""));
        
        // Hàng 2: Input Fields
        formPanel.add(tfProductId);
        formPanel.add(tfProductName);
        formPanel.add(tfProductPrice);
        
        // Nút Thao tác
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnProductAdd = new JButton("Thêm");
        btnProductUpdate = new JButton("Sửa");
        btnProductDelete = new JButton("Xóa");
        btnProductClear = new JButton("Làm mới");
        
        buttonPanel.add(btnProductAdd);
        buttonPanel.add(btnProductUpdate);
        buttonPanel.add(btnProductDelete);
        buttonPanel.add(btnProductClear);
        
        formPanel.add(buttonPanel);
        panel.add(formPanel, BorderLayout.NORTH);
        
        // --- Table Panel (Center) ---
        productTableModel = new DefaultTableModel(new Object[] { "Mã SP", "Tên SP", "Giá" }, 0);
        productTable = new JTable(productTableModel);
        productTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        productTable.getSelectionModel().addListSelectionListener(e -> fillProductFields()); 
        
        JScrollPane scrollPane = new JScrollPane(productTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    // =======================================================
    // # 3. QUẢN LÝ HÓA ĐƠN
    // =======================================================
    private JPanel createBillManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        controlPanel.setBorder(BorderFactory.createTitledBorder("Thao tác Hóa đơn"));
        
        btnBillViewAll = new JButton("Xem Tất Cả Hóa Đơn");
        controlPanel.add(btnBillViewAll);
        
        panel.add(controlPanel, BorderLayout.NORTH);
        
        // Cập nhật billTableModel để hỗ trợ Checkbox và không cho sửa cột khác
        billTableModel = new DefaultTableModel(new Object[] { "Mã HĐ", "Ngày", "Tổng Tiền", "Tên Khách Hàng", "Hoàn thành" }, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 4) return Boolean.class; 
                return super.getColumnClass(columnIndex);
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Chỉ cột "Hoàn thành" được chỉnh sửa
            }
        };

        billTable = new JTable(billTableModel);
        billTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        // THÊM LISTENER ĐỂ GỬI LỆNH CẬP NHẬT TRẠNG THÁI
        billTable.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 4) {
                    int row = e.getFirstRow();
                    if (row != -1 ) { 
                        try {
                            int billId = (int) billTableModel.getValueAt(row, 0);
                            boolean isCompleted = (boolean) billTableModel.getValueAt(row, 4);
                            
                            // Lấy Controller đang chạy
                            ActionListener[] listeners = btnUserView.getActionListeners();
                            if (listeners.length > 0 && listeners[0] instanceof AdminController) {
                                ((AdminController) listeners[0]).updateBillCompletionStatus(billId, isCompleted);
                            }
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Lỗi cập nhật trạng thái hóa đơn: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(billTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    // =======================================================
    // # 4. THỐNG KÊ & BÁO CÁO (NÂNG CẤP)
    // =======================================================
    private JPanel createAnalyticsPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // =======================================================
        // # 1. CONTROL PANEL (Bộ lọc) - Đặt ở phía TÂY (West)
        // =======================================================
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS)); 
        controlPanel.setBorder(BorderFactory.createTitledBorder("Bộ lọc & Điều khiển"));
        controlPanel.setPreferredSize(new Dimension(250, 600)); 

        // 1.1. Bộ lọc Thời gian (Theo Ngày, Tháng, Năm)
        JLabel lblFilter = new JLabel("Chọn thời gian thống kê:");
        lblFilter.setAlignmentX(Component.LEFT_ALIGNMENT);
        controlPanel.add(lblFilter);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 5))); 

        // Cập nhật các lựa chọn lọc để phù hợp với logic Daily/Monthly/Yearly
        cmbDateFilter = new JComboBox<>(new String[] {
        "Tháng hiện tại", // -> Xem theo NGÀY (của tháng hiện tại)
        "Năm hiện tại",   // -> Xem theo THÁNG (của năm hiện tại)
        "Xem theo năm"    // -> Xem theo NĂM (Doanh thu tổng hợp các năm)
        });
        cmbDateFilter.setMaximumSize(new Dimension(200, 30));
        cmbDateFilter.setAlignmentX(Component.LEFT_ALIGNMENT);
        controlPanel.add(cmbDateFilter);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // 1.2. Nút Xem Báo cáo
        btnViewReport = new JButton("Xem Báo Cáo");
        btnViewReport.setAlignmentX(Component.LEFT_ALIGNMENT);
        controlPanel.add(btnViewReport);
        controlPanel.add(Box.createVerticalGlue()); 

        panel.add(controlPanel, BorderLayout.WEST);
        
        // =======================================================
        // # 2. STATS PANEL (Tổng quan tài chính) - Đặt ở phía BẮC (North)
        // =======================================================
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Tổng quan Doanh thu"));
        
        lblTotalRevenue = new JLabel("Tổng Doanh thu Đã hoàn thành: Đang tính...");
        lblTotalRevenue.setFont(new Font("Tahoma", Font.BOLD, 18));
        
        statsPanel.add(lblTotalRevenue);
        panel.add(statsPanel, BorderLayout.NORTH);


        // =======================================================
        // # 3. CHART DISPLAY PANEL (Khu vực Biểu đồ) - Đặt ở TRUNG TÂM (Center)
        // =======================================================
        chartDisplayPanel = new JPanel(new BorderLayout());
        chartDisplayPanel.setBorder(BorderFactory.createTitledBorder("Biểu đồ Doanh thu theo thời gian"));
        chartDisplayPanel.setBackground(Color.WHITE); 
        
        JLabel chartPlaceholder = new JLabel("Chọn bộ lọc và nhấn 'Xem Báo Cáo' để tải biểu đồ.", SwingConstants.CENTER);
        chartDisplayPanel.add(chartPlaceholder, BorderLayout.CENTER);

        panel.add(chartDisplayPanel, BorderLayout.CENTER);
        
        return panel;
    }


    // =======================================================
    // # GETTERS VÀ CẬP NHẬT BẢNG
    // =======================================================

    private void fillUserFields() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow != -1 ) { 
            tfUserId.setText(userTableModel.getValueAt(selectedRow, 0).toString());
            String currentRole = userTableModel.getValueAt(selectedRow, 5).toString();
            cmbUserRole.setSelectedItem(currentRole);
        }
    }

    private void fillProductFields() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow != -1 ) {
            tfProductId.setText(productTableModel.getValueAt(selectedRow, 0).toString());
            tfProductName.setText(productTableModel.getValueAt(selectedRow, 1).toString());
            tfProductPrice.setText(productTableModel.getValueAt(selectedRow, 2).toString());
        }
    }

    public void updateUserList(List<User> users) {
        userTableModel.setRowCount(0);
        for (User user : users) {
            userTableModel.addRow(
                new Object[] { 
                    user.getMaNV(), user.getTen(), user.getEmail(), user.getSdt(), 
                    user.getDiaChi(), user.getRole()
                });
        }
    }

    public void updateProductList(List<Product> products) {
        productTableModel.setRowCount(0);
        for (Product product : products) {
            productTableModel.addRow(new Object[] { product.getMaSP(), product.getTenSP(), product.getGia() });
        }
    }
    
    public void updateBillList(List<Bill> bills) { 
        billTableModel.setRowCount(0);
        if (bills != null) {
            for (Bill bill : bills) {
                // Giả định Bill có getMaHD, getNgayDat, getTongTien, getTen, getIsCompleted
                billTableModel.addRow(new Object[] { 
                    bill.getMaHD(), 
                    bill.getNgayDat(), 
                    bill.getTongTien(), 
                    bill.getTen(), 
                    bill.getIsCompleted() // Trạng thái Hoàn thành (boolean)
                });
            }
        }
    }
    
    public void setTotalRevenue(double revenue) {
        lblTotalRevenue.setText(String.format("Tổng Doanh thu Đã hoàn thành: %,.0f VNĐ", revenue));
    }
    
    // =======================================================
    // # GETTERS BẮT BUỘC CHO CONTROLLER
    // =======================================================
    
    public String getUserIdInput() { return tfUserId.getText(); }
    public String getUserRoleInput() { return cmbUserRole.getSelectedItem().toString(); }

    public String getProductCodeInput() { return tfProductId.getText(); }
    public String getProductNameInput() { return tfProductName.getText(); }
    public String getProductPriceInput() { return tfProductPrice.getText(); }
    public String getDateFilterSelection() { return cmbDateFilter.getSelectedItem().toString(); }

    public JPanel getChartDisplayPanel() { return chartDisplayPanel; }
    
    // Phương thức xóa dữ liệu form (Clear)
    public void clearUserFields() {
        tfUserId.setText("");
        cmbUserRole.setSelectedIndex(0);
    }
    
    public void clearProductFields() {
        tfProductId.setText("");
        tfProductName.setText("");
        tfProductPrice.setText("");
    }
}