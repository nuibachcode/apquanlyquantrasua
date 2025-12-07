package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters; // Cần thiết
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.Set;
import java.util.TreeSet; // Cần thiết
import javax.swing.JPanel;
import java.awt.BorderLayout;

import model.User;
import model.Bill;
import model.Product;
import repository.UserRepositoryImpl;
import repository.IRepository.IBillRepository;
import repository.IRepository.IProductRepository;
import repository.IRepository.IUserRepository;
import repository.BillRepositoryImpl;
import repository.ProductRepositoryImpl;
import view.AdminView;
import view.BillView;
import view.SignUpView;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;

public class AdminController implements ActionListener {

    private AdminView adminView;
    private IUserRepository userRepository;
    private IBillRepository billRepository;
    private IProductRepository productRepository;
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public AdminController(AdminView adminView, User loggedInUser) {
        this.adminView = adminView;
        this.userRepository = new UserRepositoryImpl();
        this.billRepository = new BillRepositoryImpl();
        this.productRepository = new ProductRepositoryImpl();
        
        loadAccounts();
        loadProducts();
        loadBills(); 
    }

    private void loadAccounts() {
        List<User> users = userRepository.findAll();
        adminView.updateUserList(users);
    }

    private void loadProducts() {
        List<Product> products = productRepository.findAll();
        adminView.updateProductList(products);
    }
    
    private void loadBills() {
        List<Bill> bills = billRepository.findAll();
        adminView.updateBillList(bills); 
        calculateRevenue(bills);
    }
    
    private void calculateRevenue(List<Bill> bills) {
        double totalRevenue = 0;
        for (Bill bill : bills) {
            if (bill.getIsCompleted()) { 
                totalRevenue += bill.getTongTien();
            }
        }
        adminView.setTotalRevenue(totalRevenue);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {
            case "USER_VIEW": viewUserDetail(); break;
            case "USER_UPDATE": updateUserRole(); break;
            case "USER_DELETE": deleteUser(); break;
            case "USER_CLEAR": adminView.clearUserFields(); loadAccounts(); break;
                
            case "PRODUCT_ADD": addProduct(); break;
            case "PRODUCT_UPDATE": updateProduct(); break;
            case "PRODUCT_DELETE": deleteProduct(); break;
            case "PRODUCT_CLEAR": adminView.clearProductFields(); loadProducts(); break;
                
            case "BILL_VIEW_ALL": getTotalBills(); break;
            
            case "ANALYTICS_VIEW_REPORT": generateReport(); break;
            
            case "SignUp": new SignUpView().setVisible(true); break;
                
            default:
                JOptionPane.showMessageDialog(adminView, "Lệnh không xác định: " + command, "Error", JOptionPane.ERROR_MESSAGE);
                break;
        }
    }

    // =======================================================
    // # LOGIC THỐNG KÊ VÀ BIỂU ĐỒ (MỚI)
    // =======================================================
    
    private void generateReport() {
        String filter = adminView.getDateFilterSelection();
        String chartTitle = "";
        String xAxisLabel = "";
        CategoryDataset dataset = new DefaultCategoryDataset();
        
        List<Bill> allBills = billRepository.findAll();
        List<Bill> completedBills = allBills.stream()
            .filter(Bill::getIsCompleted)
            .collect(Collectors.toList()); 

        if (completedBills.isEmpty()) {
            JOptionPane.showMessageDialog(adminView, "Không có dữ liệu doanh thu để tạo báo cáo.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // 1. LỌC VÀ CHỌN CHẾ ĐỘ HIỂN THỊ
        if (filter.equals("Tháng hiện tại")) {
            // Xem theo NGÀY (của tháng hiện tại)
            LocalDate now = LocalDate.now();
            List<Bill> currentMonthBills = filterBillsByMonth(completedBills, now.getYear(), now.getMonthValue());
            
            dataset = createDailyDataset(currentMonthBills, now.getYear(), now.getMonthValue());
            chartTitle = String.format("Doanh thu theo NGÀY (Tháng %02d/%d)", now.getMonthValue(), now.getYear());
            xAxisLabel = "Ngày trong tháng";

        } else if (filter.equals("Năm hiện tại")) {
            // Xem theo THÁNG (của năm hiện tại)
            LocalDate now = LocalDate.now();
            List<Bill> currentYearBills = filterBillsByYear(completedBills, now.getYear());
            
            dataset = createMonthlyDataset(currentYearBills, now.getYear());
            chartTitle = String.format("Doanh thu theo THÁNG (Năm %d)", now.getYear());
            xAxisLabel = "Tháng";
            
        } else if (filter.equals("Xem theo năm")) {
            // Xem theo NĂM (Tổng hợp các năm)
            dataset = createYearlyDataset(completedBills);
            chartTitle = "Tổng Doanh thu theo NĂM";
            xAxisLabel = "Năm";
            
        } else {
            // Fallback
            dataset = createYearlyDataset(completedBills);
            chartTitle = "Tổng Doanh thu theo NĂM";
            xAxisLabel = "Năm";
        }
        
        // 2. TẠO VÀ HIỂN THỊ BIỂU ĐỒ
        JFreeChart chart = ChartFactory.createBarChart(
            chartTitle, 
            xAxisLabel, 
            "Doanh thu (VNĐ)", 
            dataset, 
            PlotOrientation.VERTICAL, 
            true, true, false
        );
        
        ChartPanel chartPanel = new ChartPanel(chart);
        
        // Cập nhật giao diện
        JPanel displayPanel = adminView.getChartDisplayPanel();
        displayPanel.removeAll();
        displayPanel.add(chartPanel, BorderLayout.CENTER);
        displayPanel.revalidate();
        displayPanel.repaint();
    }
    
    // =======================================================
    // # HÀM HỖ TRỢ LỌC VÀ TẠO DATASET
    // =======================================================
    
    /** Lọc hóa đơn theo Năm và Tháng cụ thể */
    private List<Bill> filterBillsByMonth(List<Bill> bills, int year, int month) {
        return bills.stream()
            .filter(bill -> {
                try {
                    LocalDate date = LocalDate.parse(bill.getNgayDat(), DATE_FORMATTER);
                    return date.getYear() == year && date.getMonthValue() == month;
                } catch (Exception e) {
                    return false;
                }
            })
            .collect(Collectors.toList());
    }
    
    /** Lọc hóa đơn theo Năm cụ thể */
    private List<Bill> filterBillsByYear(List<Bill> bills, int year) {
        return bills.stream()
            .filter(bill -> {
                try {
                    LocalDate date = LocalDate.parse(bill.getNgayDat(), DATE_FORMATTER);
                    return date.getYear() == year;
                } catch (Exception e) {
                    return false;
                }
            })
            .collect(Collectors.toList());
    }

    /** Tạo Dataset: Trục hoành là NGÀY trong tháng (ĐIỀN 0) */
    private CategoryDataset createDailyDataset(List<Bill> bills, int year, int month) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        // 1. Tổng hợp dữ liệu thực tế theo NGÀY
        Map<Integer, Double> dailyRevenueMap = bills.stream()
            .collect(Collectors.groupingBy(
                bill -> LocalDate.parse(bill.getNgayDat(), DATE_FORMATTER).getDayOfMonth(),
                Collectors.summingDouble(Bill::getTongTien)
            ));
        
        // 2. Điền dữ liệu 0 cho các ngày thiếu
        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        int daysInMonth = startOfMonth.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
        
        for (int day = 1; day <= daysInMonth; day++) {
            double revenue = dailyRevenueMap.getOrDefault(day, 0.0);
            dataset.addValue(revenue, "Doanh thu", String.valueOf(day));
        }
        
        return dataset;
    }

    /** Tạo Dataset: Trục hoành là THÁNG (ĐIỀN 0) */
    private CategoryDataset createMonthlyDataset(List<Bill> bills, int year) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        // 1. Tổng hợp dữ liệu thực tế theo THÁNG
        Map<Integer, Double> monthlyRevenueMap = bills.stream()
            .collect(Collectors.groupingBy(
                bill -> LocalDate.parse(bill.getNgayDat(), DATE_FORMATTER).getMonthValue(),
                Collectors.summingDouble(Bill::getTongTien)
            ));
        
        // 2. Điền dữ liệu 0 cho các tháng thiếu (từ 1 đến 12)
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MM/yyyy");
        
        for (int month = 1; month <= 12; month++) {
            double revenue = monthlyRevenueMap.getOrDefault(month, 0.0);
            // Tạo key hiển thị: MM/YYYY
            String displayKey = LocalDate.of(year, month, 1).format(monthFormatter);
            dataset.addValue(revenue, "Doanh thu", displayKey);
        }
        
        return dataset;
    }
    
    /** Tạo Dataset: Trục hoành là NĂM */
    private CategoryDataset createYearlyDataset(List<Bill> bills) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        // 1. Tổng hợp dữ liệu thực tế theo NĂM
        Map<Integer, Double> yearlyRevenueMap = bills.stream()
            .collect(Collectors.groupingBy(
                bill -> LocalDate.parse(bill.getNgayDat(), DATE_FORMATTER).getYear(),
                TreeMap::new, 
                Collectors.summingDouble(Bill::getTongTien)
            ));
        
        // 2. Điền dữ liệu vào Dataset (Sắp xếp theo TreeMap đã đảm bảo sắp xếp)
        yearlyRevenueMap.forEach((year, revenue) -> {
            dataset.addValue(revenue, "Doanh thu", year.toString());
        });
        
        // Thêm trường hợp không có dữ liệu để tránh biểu đồ trống
        if (yearlyRevenueMap.isEmpty()) {
             dataset.addValue(0.0, "Doanh thu", String.valueOf(LocalDate.now().getYear()));
        }
        
        return dataset;
    }


    // =======================================================
    // # CÁC HÀM CRUD KHÁC (Giữ nguyên)
    // =======================================================
    
    private void viewUserDetail() {
        String userIdStr = adminView.getUserIdInput();
        
        if (userIdStr.isEmpty()) {
            JOptionPane.showMessageDialog(adminView, "Vui lòng chọn tài khoản cần xem.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            int userId = Integer.parseInt(userIdStr);
            User foundUser = userRepository.findById(userId);

            if (foundUser != null) {
                String detail = String.format(
                    "Mã NV: %d\nTên: %s\nEmail: %s\nSĐT: %s\nĐịa chỉ: %s\nMật khẩu: ********\nRole hiện tại: %s",
                    foundUser.getMaNV(), foundUser.getTen(), foundUser.getEmail(), 
                    foundUser.getSdt(), foundUser.getDiaChi(), foundUser.getRole()
                );
                JOptionPane.showMessageDialog(adminView, detail, "Thông tin chi tiết Người dùng", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(adminView, "Không tìm thấy tài khoản có ID: " + userId, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(adminView, "ID không hợp lệ.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateUserRole() {
        String userIdStr = adminView.getUserIdInput();
        String newRole = adminView.getUserRoleInput();
        
        if (userIdStr.isEmpty() || newRole.isEmpty()) {
            JOptionPane.showMessageDialog(adminView, "Vui lòng chọn tài khoản và nhập Role mới.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            int userId = Integer.parseInt(userIdStr);
            User existingUser = userRepository.findById(userId);

            if (existingUser != null) {
                existingUser.setRole(newRole); 
                
                userRepository.update(existingUser);
                JOptionPane.showMessageDialog(adminView, "Cập nhật Role cho tài khoản ID: " + userId + " thành công.", "Success", JOptionPane.INFORMATION_MESSAGE);
                adminView.clearUserFields();
                loadAccounts();
            } else {
                JOptionPane.showMessageDialog(adminView, "Không tìm thấy tài khoản để sửa.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(adminView, "ID không hợp lệ.", "Error", JOptionPane.ERROR_MESSAGE);
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
             JOptionPane.showMessageDialog(adminView, "ID không hợp lệ.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
             JOptionPane.showMessageDialog(adminView, "Lỗi xóa tài khoản: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void addProduct() {
        String name = adminView.getProductNameInput();
        double price = 0;
        try {
             price = Double.parseDouble(adminView.getProductPriceInput()); 
        } catch (NumberFormatException e) {
             JOptionPane.showMessageDialog(adminView, "Giá tiền không hợp lệ.", "Error", JOptionPane.ERROR_MESSAGE);
             return;
        }
        
        if (name.isEmpty() || price <= 0) {
            JOptionPane.showMessageDialog(adminView, "Sản phẩm không hợp lệ", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Product newProduct = new Product(0, name, price);
        productRepository.save(newProduct);
        JOptionPane.showMessageDialog(adminView, "Thêm sản phẩm thành công", "Success", JOptionPane.INFORMATION_MESSAGE);
        adminView.clearProductFields();
        loadProducts();
    }
    
    private void updateProduct() {
        String productIdStr = adminView.getProductCodeInput();
        String name = adminView.getProductNameInput();
        
        if (productIdStr.isEmpty()) {
            JOptionPane.showMessageDialog(adminView, "Vui lòng chọn sản phẩm cần sửa.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int productId = Integer.parseInt(productIdStr);
            Product existingProduct = productRepository.findById(productId); 
            
            if (existingProduct != null) {
                double price = Double.parseDouble(adminView.getProductPriceInput()); 
                
                existingProduct.setTenSP(name);
                existingProduct.setGia(price);
                
                productRepository.save(existingProduct); 
                JOptionPane.showMessageDialog(adminView, "Sửa sản phẩm ID: " + productId + " thành công.", "Success", JOptionPane.INFORMATION_MESSAGE);
                adminView.clearProductFields();
                loadProducts();
            } else {
                JOptionPane.showMessageDialog(adminView, "Không tìm thấy sản phẩm để sửa.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
             JOptionPane.showMessageDialog(adminView, "ID hoặc Giá tiền không hợp lệ.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteProduct() {
        String productIdStr = adminView.getProductCodeInput();
        if (productIdStr.isEmpty()) {
            JOptionPane.showMessageDialog(adminView, "Vui lòng chọn sản phẩm cần xóa.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            int productId = Integer.parseInt(productIdStr);
            Product productToDelete = productRepository.findById(productId); 
            
            if (productToDelete != null) {
                productRepository.delete(productToDelete);
                JOptionPane.showMessageDialog(adminView, "Xóa sản phẩm ID: " + productId + " thành công.", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadProducts();
                adminView.clearProductFields();
            } else {
                JOptionPane.showMessageDialog(adminView, "Không tìm thấy sản phẩm", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
             JOptionPane.showMessageDialog(adminView, "ID không hợp lệ.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
             JOptionPane.showMessageDialog(adminView, "Lỗi xóa sản phẩm: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void getTotalBills() {
        List<Bill> bills = billRepository.findAll();
        new BillView(bills).setVisible(true);
    }
    
    public void updateBillCompletionStatus(int billId, boolean isCompleted) {
        try {
            Bill billToUpdate = billRepository.findById(billId); 
            
            if (billToUpdate != null) {
                billToUpdate.setIsCompleted(isCompleted); 
                billRepository.update(billToUpdate);
            } else {
                 JOptionPane.showMessageDialog(adminView, "Không tìm thấy Hóa đơn ID " + billId + " để cập nhật.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(adminView, "Lỗi cập nhật trạng thái hóa đơn: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        loadBills();
    }
}