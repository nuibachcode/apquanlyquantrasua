package view.UserView;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.TitledBorder;

import controller.UserController.ProductController;
import model.Product;
import model.SelectedProduct;
import model.User; 
import repository.ProductRepositoryImpl;
import view.BackgroundPanel;
import view.LoginView;

public class ProductView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    
    private JTextField textFieldTotal, textFieldSearch; 
    
    private JButton btnOrder, btnViewBill, btnLogout, btnSearch;
    private List<SelectedProduct> selectedProducts = new ArrayList<>();
    private List<Product> allProducts; 
    private JPanel productsPanel; 
    private JScrollPane scrollPane; 
    
    private User loggedInUser; 
    
    // =======================================================
    // CONSTRUCTOR MỚI
    // =======================================================
    public ProductView(User user) { 
        this.loggedInUser = user;
        initComponents();
    }
    
    // Constructor cũ (giữ lại cho main method test)
    public ProductView() {
        // Tạo User giả để tránh lỗi NullPointer khi chạy main()
        this.loggedInUser = new User(0, "Khách (TEST)", "test@mail.com", "pass", "123 Đường ABC, Quận 1", "0900000000", "USER");
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Đặt Hàng");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1000, 750); 
        setLocationRelativeTo(null);
        
        // --- Setup ContentPane ---
        // Giả định BackgroundPanel và đường dẫn banner.jpg là hợp lệ
        Image bg = new ImageIcon(getClass().getResource("/images/banner.jpg")).getImage();
        contentPane = new BackgroundPanel(bg); 
        contentPane.setLayout(null);
        setContentPane(contentPane);

        Font labelFont = new Font("Arial", Font.BOLD, 16); 
        Font buttonFont = new Font("Arial", Font.BOLD, 18);
        
        allProducts = new ProductRepositoryImpl().findAll();
        
        // =======================================================
        // 2. Thanh Tìm Kiếm
        // =======================================================
        int searchY = 20;
        JLabel lblSearch = createLabel("Tìm Kiếm SP:", labelFont, 10, searchY); 
        
        textFieldSearch = createTextField(170, searchY, 200, 30);
        textFieldSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterProducts(textFieldSearch.getText());
            }
        });

        btnSearch = createButton("Tìm", buttonFont, new Color(153, 204, 255), 380, searchY, 100, 30);
        btnSearch.setActionCommand("Search"); 
        
        // =======================================================
        // 3. Khu vực Menu Sản Phẩm
        // =======================================================
        initMenuPanel(); 
        
        // =======================================================
        // 4. Thông tin Tổng tiền và Hóa đơn
        // =======================================================
        int totalY = 550;
        
        createLabel("Tổng Cộng:", labelFont, 10, totalY);
        
        textFieldTotal = createTextField(170, totalY, 150, 30);
        textFieldTotal.setEditable(false);

        btnViewBill = createButton("Xem Hóa Đơn", buttonFont, new Color(255, 102, 102), 340, totalY, 180, 30);
        btnViewBill.setActionCommand("Show Bill");
        
        // =======================================================
        // 5. Nút Đặt Hàng và Đăng Xuất
        // =======================================================
        int btnY = 620;
        
        btnOrder = createButton("ĐẶT HÀNG", buttonFont, new Color(255, 153, 0), 10, btnY, 250, 50);
        btnOrder.setActionCommand("GENERATE BILL & PRINT");

        btnLogout = createButton("ĐĂNG XUẤT", buttonFont, new Color(255, 153, 0), 700, btnY, 250, 50);
        
        // --- Controller and Events ---
        ProductController controller = new ProductController(this);
        btnOrder.addActionListener(controller);
        btnViewBill.addActionListener(controller);
        btnSearch.addActionListener(controller); 

        btnLogout.addActionListener(e -> {
            dispose();
             
             new LoginView().setVisible(true); 
        });
    }

    // =======================================================
    // GETTER CHÍNH: Cung cấp đối tượng User cho Controller
    // =======================================================
    public User getLoggedInUser() { 
        return loggedInUser; 
    } 
    
    // ===== Getters & Setters khác =====
    public List<SelectedProduct> getSelectedProducts() { return selectedProducts; }
    public String getSearchKeyword() { return textFieldSearch.getText(); }
    public void setTotalPrice(double total) { textFieldTotal.setText(String.format("%,.0f₫", total)); }
    
    public void xoaForm() {
        textFieldTotal.setText("");
        textFieldSearch.setText("");
        selectedProducts.clear();
        filterProducts(""); 
    }

    // =======================================================
    // LOGIC MENU VÀ SẢN PHẨM
    // =======================================================
    public void filterProducts(String keyword) {
        List<Product> filteredList;
        if (keyword == null || keyword.trim().isEmpty()) {
            filteredList = allProducts;
        } else {
            String lowerCaseKeyword = keyword.trim().toLowerCase();
            filteredList = new ArrayList<>();
            for (Product p : allProducts) {
                if (p.getTenSP().toLowerCase().contains(lowerCaseKeyword)) {
                    filteredList.add(p);
                }
            }
        }
        updateMenuPanel(filteredList);
    }
    
    private void updateMenuPanel(List<Product> productsToDisplay) {
        productsPanel.removeAll();
        for (int i = 0; i < productsToDisplay.size(); i++) {
            Product product = productsToDisplay.get(i);
            // Index không còn dùng để tính tên ảnh, chỉ dùng để tạo ProductItem
            productsPanel.add(createProductItem(product, i)); 
        }
        productsPanel.revalidate();
        productsPanel.repaint();
    }
    
    // PHƯƠNG THỨC TẠO SẢN PHẨM ĐÃ SỬA LOGIC ẢNH
private JPanel createProductItem(Product product, int index) {
        JPanel productItem = new JPanel(new BorderLayout());
        productItem.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        productItem.setBackground(Color.WHITE);
        
        // --- 1. XỬ LÝ ẢNH ---
        // Lấy tên ảnh. Giả định tên ảnh là "picture1.png" (không có tiền tố "images/")
        String imageName = product.getImageName();
        // Đường dẫn tuyệt đối trong Classpath: /images/picture1.png
        String imagePath = "/images/" + (imageName != null && !imageName.isEmpty() ? imageName : "default.png");
        
        JLabel imageLabel;
        
        try {
            // Sử dụng ClassLoader để tải resource an toàn hơn
            java.net.URL imageUrl = getClass().getResource(imagePath);
            
            if (imageUrl != null) {
                // Tải ảnh thành công
                ImageIcon icon = new ImageIcon(imageUrl);
                Image img = icon.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH);
                imageLabel = new JLabel(new ImageIcon(img));
                imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            } else {
                // Không tìm thấy file ảnh (URL == null)
                System.err.println("LỖI TẢI ẢNH: Không tìm thấy file tại " + imagePath);
                imageLabel = new JLabel("<html><center>Không có ảnh</center></html>", SwingConstants.CENTER);
                imageLabel.setPreferredSize(new Dimension(200, 150));
            }
        } catch (Exception e) {
            // Xử lý lỗi ngoại lệ trong quá trình tải
            System.err.println("LỖI XỬ LÝ ẢNH: " + e.getMessage());
            imageLabel = new JLabel("<html><center>Lỗi ảnh</center></html>", SwingConstants.CENTER);
            imageLabel.setPreferredSize(new Dimension(200, 150));
        }

        productItem.add(imageLabel, BorderLayout.CENTER);
        
        // --- 2. THÔNG TIN SẢN PHẨM ---
        
        JLabel nameLabel = new JLabel(
            "<html><center>" + product.getTenSP() + "<br>Giá: " + String.format("%,.0f₫", product.getGia()) + "</center></html>",
            SwingConstants.CENTER
        );
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        productItem.add(nameLabel, BorderLayout.SOUTH);
        
        // --- 3. LOGIC XỬ LÝ SỰ KIỆN CLICK ---
        
        productItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String qtyStr = JOptionPane.showInputDialog(
                    ProductView.this, 
                    "Nhập số lượng cho " + product.getTenSP() + ":", 
                    "1"
                );
                
                try {
                    if (qtyStr == null) return; 
                    int qty = Integer.parseInt(qtyStr);
                    if (qty <= 0) qty = 1;
                    
                    addSelectedProduct(product, qty);
                    JOptionPane.showMessageDialog(
                        ProductView.this, 
                        product.getTenSP() + " x" + qty + " đã được thêm!", 
                        "Đã Thêm", 
                        JOptionPane.INFORMATION_MESSAGE
                    );
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(
                        ProductView.this, 
                        "Vui lòng nhập số lượng hợp lệ!", 
                        "Lỗi", 
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
        
        return productItem;
    }
    
    public void addSelectedProduct(Product product, int quantity) {
        for (SelectedProduct sp : selectedProducts) {
            if (sp.getProduct().getTenSP().equals(product.getTenSP())) {
                sp.setQuantity(sp.getQuantity() + quantity); 
                return;
            }
        }
        selectedProducts.add(new SelectedProduct(product, quantity));
    }
    
    private void initMenuPanel() {
        productsPanel = new JPanel();
        productsPanel.setLayout(new GridLayout(0, 3, 20, 20)); 
        productsPanel.setBackground(Color.WHITE); 
        updateMenuPanel(allProducts);
        scrollPane = new JScrollPane(productsPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        scrollPane.setBounds(10, 80, 960, 450); 
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.BLACK), 
            "MENU SẢN PHẨM", 
            TitledBorder.LEFT, 
            TitledBorder.TOP, 
            new Font("Arial", Font.BOLD, 20), 
            new Color(0, 102, 0)
        ));
        contentPane.add(scrollPane);
    }
    
    // ... (Các phương thức hỗ trợ giao diện giữ nguyên) ...

    private JLabel createLabel(String text, Font font, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(font);
        lbl.setBounds(x, y, 160, 30);
        contentPane.add(lbl);
        return lbl;
    }

    private JTextField createTextField(int x, int y, int width, int height) {
        JTextField field = new JTextField();
        field.setBounds(x, y, width, height);
        contentPane.add(field);
        return field;
    }

    private JButton createButton(String text, Font font, Color color, int x, int y, int width, int height) {
        JButton btn = new JButton(text);
        btn.setFont(font);
        btn.setBackground(color);
        btn.setBounds(x, y, width, height);
        contentPane.add(btn);
        return btn;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                User testUser = new User(1, "Nguyễn Văn A", "vana@mail.com", "pass", "123 Đường ABC, Quận 1", "0901234567", "USER");
                ProductView frame = new ProductView(testUser);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}