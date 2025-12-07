package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import model.Bill;
import model.SelectedProduct;
import model.User; 
import repository.BillRepositoryImpl;
import view.ProductView;

public class ProductController implements ActionListener {

    private ProductView productView;
    private BillRepositoryImpl billRepository;

    public ProductController(ProductView productView) {
        this.productView = productView;
        this.billRepository = new BillRepositoryImpl();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        switch (cmd) {
            case "GENERATE BILL & PRINT": 
                generateBill();
                break;
            case "Show Bill": 
                showBillDetails(); // SỬA ĐỔI CHỨC NĂNG NÀY
                break;
            case "Search": 
                productView.filterProducts(productView.getSearchKeyword());
                break;
            default:
                JOptionPane.showMessageDialog(productView, "Sai thao tác!");
        }
    }

    private void generateBill() {
        // ... (Logic generateBill giữ nguyên)
        // Phần này đã đúng, chỉ cần Bill model được cập nhật
        List<SelectedProduct> selectedProducts = productView.getSelectedProducts();
        User user = productView.getLoggedInUser(); 

        if (selectedProducts.isEmpty()) {
            JOptionPane.showMessageDialog(productView, "Vui lòng chọn ít nhất một sản phẩm để đặt hàng!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String name = user.getTen();
        String email = user.getEmail();
        String phone = user.getSdt(); 
        String address = user.getDiaChi(); 

        double total = calculateTotal(selectedProducts);
        StringBuilder productsStr = buildProductsString(selectedProducts);

        try {
            Bill bill = new Bill(
                0, 
                name, 
                phone.trim(), 
                email, 
                address, 
                java.time.LocalDate.now().toString(), 
                total,
                selectedProducts 
            );
            
            billRepository.save(bill); 

            String finalMessage = String.format(
                "✅ ĐẶT HÀNG THÀNH CÔNG! (Hóa đơn đã lưu)\n" +
                "--------------------------------------------------\n" +
                "KHÁCH HÀNG: %s\n" +
                "SĐT: %s\n" +
                "Email: %s\n" +
                "Địa chỉ giao: %s\n" +
                "--------------------------------------------------\n" +
                "SẢN PHẨM:\n%s" +
                "--------------------------------------------------\n" +
                "🌟 TỔNG TIỀN: %s",
                name, phone, email, address, productsStr.toString(), String.format("%,.0f₫", total)
            );

            JOptionPane.showMessageDialog(productView, finalMessage, "Đặt Hàng Thành Công", JOptionPane.INFORMATION_MESSAGE);

            productView.xoaForm();
            
        } catch (Exception ex) {
             JOptionPane.showMessageDialog(productView, "LỖI LƯU HÓA ĐƠN: Lỗi Constructor Bill hoặc Lỗi kết nối DB. Chi tiết: " + ex.getMessage(), "Lỗi Hệ Thống", JOptionPane.ERROR_MESSAGE);
             ex.printStackTrace();
        }
    }

    // PHƯƠNG THỨC ĐÃ SỬA: Hiển thị thông tin khách hàng
    private void showBillDetails() {
        List<SelectedProduct> selectedProducts = productView.getSelectedProducts();
        User user = productView.getLoggedInUser(); // LẤY THÔNG TIN USER TỪ ĐÂY
        
        if (selectedProducts.isEmpty()) {
            productView.setTotalPrice(0);
            JOptionPane.showMessageDialog(productView, "Chưa có sản phẩm nào được chọn!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        double total = calculateTotal(selectedProducts);
        productView.setTotalPrice(total);
        
        StringBuilder productsStr = buildProductsString(selectedProducts);
        
        // --- XÂY DỰNG NỘI DUNG HIỂN THỊ ĐẦY ĐỦ ---
        StringBuilder detailMessage = new StringBuilder();
        detailMessage.append("--- THÔNG TIN KHÁCH HÀNG ---\n");
        detailMessage.append(String.format("Tên: %s\n", user.getTen()));
        detailMessage.append(String.format("SĐT: %s\n", user.getSdt()));
        detailMessage.append(String.format("Email: %s\n", user.getEmail()));
        detailMessage.append(String.format("Địa Chỉ: %s\n", user.getDiaChi())); // Địa chỉ
        detailMessage.append("-----------------------------------\n");
        detailMessage.append("--- CHI TIẾT SẢN PHẨM ---\n");
        detailMessage.append(productsStr.toString());
        detailMessage.append("-----------------------------------\n");
        detailMessage.append(String.format("TỔNG CỘNG: %,.0f₫", total));
        
        JTextArea textArea = new JTextArea(detailMessage.toString());
        textArea.setEditable(false);
        
        // Sử dụng JScrollPane để hiển thị tốt hơn
        JOptionPane.showMessageDialog(productView, new JScrollPane(textArea), 
            "Xem Hóa Đơn", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private double calculateTotal(List<SelectedProduct> selectedProducts) {
        double total = 0;
        for (SelectedProduct sp : selectedProducts) {
            total += sp.getTotalPrice();
        }
        return total;
    }

    private StringBuilder buildProductsString(List<SelectedProduct> selectedProducts) {
         StringBuilder productsStr = new StringBuilder();
         for (SelectedProduct sp : selectedProducts) {
            productsStr.append(sp.getProduct().getTenSP())
                        .append(" x").append(sp.getQuantity())
                        .append(" (").append(String.format("%,.0f₫", sp.getProduct().getGia())).append("/SP)\n");
        }
        return productsStr;
    }
}