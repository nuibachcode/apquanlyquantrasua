package controller.product;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;

import model.Product;
import repository.ProductRepositoryImpl;
import repository.IRepository.IProductRepository;
import view.AdminView;

public class ProductManagementController implements ActionListener {
    
    private AdminView adminView;
    private IProductRepository productRepository;

    public ProductManagementController(AdminView adminView) {
        this.adminView = adminView;
        this.productRepository = new ProductRepositoryImpl();
    }

    // Tải dữ liệu từ DB và cập nhật View
    public void loadProducts() {
        List<Product> products = productRepository.findAll();
        adminView.updateProductList(products);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        
        switch (command) {
            case "PRODUCT_ADD": addProduct(); break;
            case "PRODUCT_UPDATE": updateProduct(); break;
            case "PRODUCT_DELETE": deleteProduct(); break;
            case "PRODUCT_CLEAR": adminView.clearProductFields(); loadProducts(); break;
        }
    }

    // --- HÀM CHI TIẾT ---
    
    private void addProduct() {
        String name = adminView.getProductNameInput();
        String imageName = adminView.getProductImageInput(); // Lấy đường dẫn ảnh
        double price = 0;
        
        try {
             price = Double.parseDouble(adminView.getProductPriceInput());
        } catch (NumberFormatException e) {
             JOptionPane.showMessageDialog(adminView, "Giá tiền không hợp lệ.", "Error", JOptionPane.ERROR_MESSAGE);
             return;
        }
        
        if (name.isEmpty() || price <= 0) {
            JOptionPane.showMessageDialog(adminView, "Tên sản phẩm hoặc Giá tiền không hợp lệ.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Tạo sản phẩm mới với ID=0 (sẽ được Repository gán ID)
        // và thêm trường ảnh
        Product newProduct = new Product(0, name, price, imageName); 
        
        try {
            productRepository.save(newProduct);
            JOptionPane.showMessageDialog(adminView, "Thêm sản phẩm thành công", "Success", JOptionPane.INFORMATION_MESSAGE);
            adminView.clearProductFields();
            loadProducts();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(adminView, "Lỗi khi lưu sản phẩm: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateProduct() {
        String productIdStr = adminView.getProductCodeInput();
        String name = adminView.getProductNameInput();
        String imageName = adminView.getProductImageInput(); // Lấy đường dẫn ảnh
        
        if (productIdStr.isEmpty()) {
            JOptionPane.showMessageDialog(adminView, "Vui lòng chọn sản phẩm cần sửa.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int productId = Integer.parseInt(productIdStr);
            Product existingProduct = productRepository.findById(productId);
            
            if (existingProduct != null) {
                double price = Double.parseDouble(adminView.getProductPriceInput());
                
                // Cập nhật các thuộc tính của sản phẩm đã tồn tại
                existingProduct.setTenSP(name);
                existingProduct.setGia(price);
                existingProduct.setImageName(imageName); // Cập nhật đường dẫn ảnh
                
                // **SỬA LỖI QUAN TRỌNG:** PHẢI GỌI update() thay vì save()
                // Giả định IProductRepository có phương thức update(Product)
                productRepository.update(existingProduct); 
                
                JOptionPane.showMessageDialog(adminView, "Sửa sản phẩm ID: " + productId + " thành công.", "Success", JOptionPane.INFORMATION_MESSAGE);
                adminView.clearProductFields();
                loadProducts();
            } else {
                JOptionPane.showMessageDialog(adminView, "Không tìm thấy sản phẩm để sửa.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
             JOptionPane.showMessageDialog(adminView, "ID hoặc Giá tiền không hợp lệ.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
             JOptionPane.showMessageDialog(adminView, "Lỗi cập nhật sản phẩm: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
}