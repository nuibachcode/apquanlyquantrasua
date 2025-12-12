package view.UserView.components;

import model.Product;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;
import java.awt.event.MouseListener;
import java.net.URL; // Import thêm để xử lý đường dẫn ảnh

public class ProductListPanel extends JPanel {
    
    private JPanel gridPanel;

    public ProductListPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), "DANH SÁCH SẢN PHẨM", 
            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, 
            new Font("Arial", Font.BOLD, 14), Color.BLUE));

        // Grid 3 cột, khoảng cách các ô là 15px
        gridPanel = new JPanel(new GridLayout(0, 3, 15, 15)); 
        gridPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Tăng tốc độ cuộn chuột
        add(scrollPane, BorderLayout.CENTER);
    }

    // Hàm hiển thị danh sách
    public void renderProducts(List<Product> products, MouseListener itemListener) {
        gridPanel.removeAll();
        
        if (products != null) {
            for (Product p : products) {
                JPanel item = createProductItem(p);
                item.putClientProperty("product_data", p); // Lưu object Product vào panel
                item.addMouseListener(itemListener);       // Gắn sự kiện click
                gridPanel.add(item);
            }
        }
        
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private JPanel createProductItem(Product p) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(180, 220)); 
        // --- 1. XỬ LÝ ẢNH ---
        JLabel lblImage = new JLabel();
        lblImage.setPreferredSize(new Dimension(200, 150));
        lblImage.setHorizontalAlignment(SwingConstants.CENTER);
        String imageName = p.getImageName(); 
        String imagePath = "/images/" + (imageName != null && !imageName.isEmpty() ? imageName : "default.png");
        try {
            URL url = getClass().getResource(imagePath);
            if (url != null) {
                ImageIcon icon = new ImageIcon(url);
                Image img = icon.getImage().getScaledInstance(200, 160, Image.SCALE_SMOOTH); 
                lblImage.setIcon(new ImageIcon(img));
            } else {
                lblImage.setText("No Image");
                lblImage.setForeground(Color.GRAY);
                lblImage.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY)); // Viền báo thiếu ảnh
            }
        } catch (Exception e) {
            lblImage.setText("Error");
        }
        // --- 2. XỬ LÝ TÊN VÀ GIÁ ---
        JLabel lblName = new JLabel("<html><center>" 
                + p.getTenSP() + "<br>" 
                + "<font color='red' size='4'><b>" + String.format("%,.0f₫", p.getGia()) + "</b></font>" 
                + "</center></html>", SwingConstants.CENTER);
        lblName.setVerticalAlignment(SwingConstants.TOP);
        lblName.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5)); // Padding
        panel.add(lblImage, BorderLayout.CENTER);
        panel.add(lblName, BorderLayout.SOUTH);
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return panel;
    }
}