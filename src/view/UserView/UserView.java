package view.UserView;

import model.User;
import view.UserView.components.*; 
// Import Controller
import controller.UserController.UserController; 

import javax.swing.*;
import java.awt.*;

public class UserView extends JFrame {
    
    // [QUAN TRỌNG] Các thành phần giao diện public
    public HeaderPanel headerPanel;
    public ProductListPanel productListPanel;
    public CartPanel cartPanel; // Phải là CartPanel (JPanel), KHÔNG ĐƯỢC là Dialog
    
    private User loggedInUser;

    public UserView(User user) {
        this.loggedInUser = user;
        initUI();
    }

    private void initUI() {
        setTitle("Hệ Thống Bán Hàng - Xin chào: " + (loggedInUser != null ? loggedInUser.getTen() : "Khách"));
        setSize(1100, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 1. Khởi tạo
        headerPanel = new HeaderPanel();
        productListPanel = new ProductListPanel();
        
        // [QUAN TRỌNG] Khởi tạo CartPanel (không tham số)
        cartPanel = new CartPanel(); 

        // 2. Add vào Frame
        add(headerPanel, BorderLayout.NORTH);
        add(productListPanel, BorderLayout.CENTER);
        
        // [QUAN TRỌNG] Add CartPanel vào phía Nam
        add(cartPanel, BorderLayout.SOUTH); 
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    // Main Test
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                User mockUser = new User(1, "Test User", "test@mail.com", "pass", "Address", "0123456789", "USER");
                UserView view = new UserView(mockUser);
                new UserController(view); // Kích hoạt controller
                view.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}