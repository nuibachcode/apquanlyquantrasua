package controller.UserController;

import model.Bill;
import model.Product;
import model.SelectedProduct;
import model.User;
import repository.BillRepositoryImpl;
import repository.ProductRepositoryImpl;

import view.UserView.UserView; 
import view.UserView.components.OrderHistoryDialog;
import view.UserView.components.ProfileDialog;
import view.UserView.components.CartDetailsDialog; // [QUAN TRỌNG] Import cái này
import view.LoginView;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class UserController {

    private UserView view;
    private ProductRepositoryImpl productRepo;
    private BillRepositoryImpl billRepo;
    
    private List<Product> allProducts;
    private List<SelectedProduct> cart;

    public UserController(UserView view) {
        this.view = view;
        this.productRepo = new ProductRepositoryImpl();
        this.billRepo = new BillRepositoryImpl();
        this.cart = new ArrayList<>();

        loadData();
        initController();
    }

    private void loadData() {
        allProducts = productRepo.findAll();
        // [SỬA LỖI] Bây giờ ProductListPanel đã extends JPanel nên gọi được hàm này
        view.productListPanel.renderProducts(allProducts, new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JPanel item = (JPanel) e.getSource();
                Product p = (Product) item.getClientProperty("product_data");
                if (p != null) addToCart(p);
            }
        });
    }

    private void initController() {
        view.headerPanel.getBtnSearch().addActionListener(e -> filterProducts());
        view.headerPanel.getTxtSearch().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) { filterProducts(); }
        });

        view.cartPanel.getBtnOrder().addActionListener(e -> processOrderWithoutPayment());
        
        // [SỬA LỖI] Nút xem giỏ hàng gọi hàm showCartDetails
        view.cartPanel.getBtnViewCart().addActionListener(e -> showCartDetails());

        view.headerPanel.getBtnHistory().addActionListener(e -> showOrderHistory());

        view.headerPanel.getBtnProfile().addActionListener(e -> {
            new ProfileDialog(view, view.getLoggedInUser()).setVisible(true);
        });

        view.headerPanel.getBtnLogout().addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(view, "Đăng xuất?", "Logout", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                view.dispose();
                new LoginView().setVisible(true);
            }
        });
    }

    // --- HÀM HIỂN THỊ GIỎ HÀNG ĐẸP ---
    private void showCartDetails() {
         if (cart.isEmpty()) {
             JOptionPane.showMessageDialog(view, "Giỏ hàng đang trống!", "Thông báo", JOptionPane.WARNING_MESSAGE);
             return;
         }
         // [SỬA LỖI] Gọi CartDetailsDialog (Popup), KHÔNG PHẢI CartPanel
         new CartDetailsDialog(view, cart).setVisible(true);
    }

    private void processOrderWithoutPayment() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Giỏ hàng trống!");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(view, "Xác nhận đặt đơn hàng này?\n(Thanh toán khi nhận hàng)", "Đặt hàng", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        double total = 0;
        for (SelectedProduct sp : cart) total += sp.getQuantity() * sp.getProduct().getGia();

        User user = view.getLoggedInUser();
        try {
            Bill bill = new Bill(0, user.getTen(), user.getSdt(), user.getEmail(), user.getDiaChi(), java.time.LocalDate.now().toString(), total, new ArrayList<>(cart));
            billRepo.save(bill);
            JOptionPane.showMessageDialog(view, "✅ ĐẶT HÀNG THÀNH CÔNG!\nVui lòng vào Lịch sử đơn hàng để thanh toán.");
            cart.clear();
            updateTotalUI();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi lưu đơn: " + e.getMessage());
        }
    }

    private void showOrderHistory() {
        User currentUser = view.getLoggedInUser();
        List<Bill> allBills = billRepo.findAll();
        List<Bill> myBills = new ArrayList<>();
        if (allBills != null) {
            for (Bill b : allBills) {
                if (b.getEmail().equals(currentUser.getEmail())) {
                    myBills.add(b);
                }
            }
        }
        new OrderHistoryDialog(view, myBills).setVisible(true);
    }

    private void addToCart(Product p) {
        String qtyStr = JOptionPane.showInputDialog(view, "Nhập số lượng " + p.getTenSP() + ":", "1");
        if (qtyStr == null) return;
        try {
            int qty = Integer.parseInt(qtyStr);
            if (qty <= 0) return;
            boolean exists = false;
            for (SelectedProduct sp : cart) {
                if (sp.getProduct().getMaSP() == p.getMaSP()) {
                    sp.setQuantity(sp.getQuantity() + qty);
                    exists = true; break;
                }
            }
            if (!exists) cart.add(new SelectedProduct(p, qty));
            updateTotalUI();
            JOptionPane.showMessageDialog(view, "Đã thêm vào giỏ!");
        } catch (Exception e) {}
    }

    private void updateTotalUI() {
        double total = 0;
        for (SelectedProduct sp : cart) total += sp.getQuantity() * sp.getProduct().getGia();
        view.cartPanel.setTotal(total);
    }

    private void filterProducts() {
        String keyword = view.headerPanel.getSearchKeyword().toLowerCase();
        List<Product> filtered = new ArrayList<>();
        if (allProducts != null) {
            for (Product p : allProducts) {
                if (p.getTenSP().toLowerCase().contains(keyword)) filtered.add(p);
            }
        }
        view.productListPanel.renderProducts(filtered, new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JPanel item = (JPanel) e.getSource();
                Product p = (Product) item.getClientProperty("product_data");
                if (p != null) addToCart(p);
            }
        });
    }
}