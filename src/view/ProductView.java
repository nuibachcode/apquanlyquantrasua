package view;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.TitledBorder;

import controller.ProductController;
import model.Product;
import model.SelectedProduct;
import model.User; // Cần import User
import repository.ProductRepositoryImpl; 

public class ProductView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    
    // Giữ lại các trường liên quan đến hóa đơn và tìm kiếm
    private JTextField textFieldTotal, textFieldSearch; 
    
    private JButton btnOrder, btnViewBill, btnLogout, btnSearch;
    private List<SelectedProduct> selectedProducts = new ArrayList<>();
    private List<Product> allProducts; 
    private JPanel productsPanel; 
    private JScrollPane scrollPane; 
    
    // Khai báo User để lưu trữ thông tin người dùng đã đăng nhập
    private User loggedInUser; 
    
    // =======================================================
    // CONSTRUCTOR MỚI: Nhận User, gọi initComponents (Sử dụng cho LoginController)
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
        Image bg = new ImageIcon(getClass().getResource("/images/banner.jpg")).getImage();
        contentPane = new BackgroundPanel(bg); // Giả định BackgroundPanel đã có
        contentPane.setLayout(null);
        setContentPane(contentPane);

        Font labelFont = new Font("Arial", Font.BOLD, 16); 
        Font buttonFont = new Font("Arial", Font.BOLD, 18);
        
        allProducts = new ProductRepositoryImpl().findAll();
        
        // =======================================================
        // 1. (ĐÃ XÓA) Thông tin Khách hàng - Không hiển thị trên giao diện
        // =======================================================
        
        // =======================================================
        // 2. Thanh Tìm Kiếm (Đẩy lên đầu)
        // =======================================================
        int searchY = 20;
        JLabel lblSearch = createLabel("Tìm Kiếm SP:", labelFont, 10, searchY); 
        
        textFieldSearch = createTextField(170, searchY, 200, 30);
        // Gắn Listener cho việc lọc tức thời
        textFieldSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterProducts(textFieldSearch.getText());
            }
        });

        btnSearch = createButton("Tìm", buttonFont, new Color(153, 204, 255), 380, searchY, 100, 30);
        btnSearch.setActionCommand("Search"); 
        
        // =======================================================
        // 3. Khu vực Menu Sản Phẩm (Bắt đầu từ Y=80)
        // =======================================================
        initMenuPanel(); 
        
        // =======================================================
        // 4. Thông tin Tổng tiền và Hóa đơn (Y=550)
        // =======================================================
        int totalY = 550;
        
        createLabel("Tổng Cộng:", labelFont, 10, totalY);
        
        textFieldTotal = createTextField(170, totalY, 150, 30);
        textFieldTotal.setEditable(false);

        btnViewBill = createButton("Xem Hóa Đơn", buttonFont, new Color(255, 102, 102), 340, totalY, 180, 30);
        btnViewBill.setActionCommand("Show Bill");
        
        // =======================================================
        // 5. Nút Đặt Hàng và Đăng Xuất (Y=620)
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
    
    // XoaForm không xóa các trường của khách hàng
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
            productsPanel.add(createProductItem(product, i));
        }
        productsPanel.revalidate();
        productsPanel.repaint();
    }
    
    private JPanel createProductItem(Product product, int index) {
        JPanel productItem = new JPanel(new BorderLayout());
        productItem.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        productItem.setBackground(Color.WHITE);
        String imageName = "/images/picture" + (index % 3 + 1) + ".png"; 
        ImageIcon icon = new ImageIcon(getClass().getResource(imageName));
        Image img = icon.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(img));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        productItem.add(imageLabel, BorderLayout.CENTER);
        JLabel nameLabel = new JLabel(
            "<html><center>" + product.getTenSP() + "<br>Giá: " + String.format("%,.0f₫", product.getGia()) + "</center></html>",
            SwingConstants.CENTER
        );
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        productItem.add(nameLabel, BorderLayout.SOUTH);
        productItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String qtyStr = JOptionPane.showInputDialog(ProductView.this, "Nhập số lượng cho " + product.getTenSP() + ":", "1");
                try {
                    if (qtyStr == null) return; 
                    int qty = Integer.parseInt(qtyStr);
                    if (qty <= 0) qty = 1;
                    addSelectedProduct(product, qty);
                    JOptionPane.showMessageDialog(ProductView.this, product.getTenSP() + " x" + qty + " đã được thêm!", "Đã Thêm", JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(ProductView.this, "Vui lòng nhập số lượng hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
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
    
    // =======================================================
    // PHƯƠNG THỨC HỖ TRỢ GIAO DIỆN (Giữ nguyên)
    // =======================================================
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
                // Tạo User giả cho mục đích kiểm tra
                User testUser = new User(1, "Nguyễn Văn A", "vana@mail.com", "pass", "123 Đường ABC, Quận 1", "0901234567", "USER");
                ProductView frame = new ProductView(testUser);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}