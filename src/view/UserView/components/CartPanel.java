package view.UserView.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// [QUAN TRỌNG] Phải extends JPanel
public class CartPanel extends JPanel {
    
    private JTextField txtTotal;
    private JButton btnOrder;
    private JButton btnViewCart;

    // Màu sắc
    private final Color BG_COLOR = new Color(240, 248, 255); 
    private final Color PRIMARY_ORANGE = new Color(255, 120, 0);

    public CartPanel() {
        setLayout(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        setBackground(BG_COLOR);
        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)));
        // --- 1. LABEL & TOTAL ---
        JLabel lblTitle = new JLabel("Tạm tính:");
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblTitle.setForeground(Color.DARK_GRAY);

        txtTotal = new JTextField(12);
        txtTotal.setEditable(false);
        txtTotal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        txtTotal.setForeground(new Color(220, 53, 69));
        txtTotal.setBackground(Color.WHITE);
        txtTotal.setHorizontalAlignment(JTextField.RIGHT);

        txtTotal.putClientProperty("TextComponent.arc", 20);
        txtTotal.putClientProperty("Component.borderColor", new Color(200, 200, 200));
        txtTotal.setMargin(new Insets(5, 10, 5, 10));
        txtTotal.setText("0₫");
        add(lblTitle);
        add(txtTotal);
        // --- 2. BUTTONS ---
        btnViewCart = new JButton("Xem giỏ hàng");
        btnOrder = new JButton("TIẾN HÀNH ĐẶT HÀNG");
        styleButton(btnViewCart, Color.WHITE, Color.BLACK);
        styleButton(btnOrder, PRIMARY_ORANGE, Color.WHITE);
        btnOrder.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnOrder.setPreferredSize(new Dimension(220, 40)); 
        add(btnViewCart);
        add(btnOrder);
    }

    /**
     * Hàm style nút bấm (Đã sửa đổi để hỗ trợ Bo Tròn)
     */
    private void styleButton(JButton btn, Color bgColor, Color fgColor) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bgColor);
        btn.setForeground(fgColor);
        
        btn.setFocusPainted(false);
        // btn.setBorderPainted(false); // <-- XÓA DÒNG NÀY để FlatLaf vẽ viền cong
        
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Thay setBorder bằng setMargin để giữ độ bo tròn
        btn.setMargin(new Insets(8, 15, 8, 15));

        // Xử lý riêng cho nút Trắng (Xem giỏ hàng)
        if (bgColor.equals(Color.WHITE)) {
            // Thay vì tạo LineBorder thủ công, ta bảo FlatLaf đổi màu viền của nút này
            btn.putClientProperty("Component.borderColor", new Color(200, 200, 200));
            // Đảm bảo viền được vẽ
            btn.setBorderPainted(true); 
        } else {
            // Nút màu (Cam): FlatLaf tự xử lý viền rất đẹp, không cần can thiệp
        }

        // Hiệu ứng Hover
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                if (!bgColor.equals(Color.WHITE)) {
                    btn.setBackground(bgColor.darker());
                } else {
                    btn.setBackground(new Color(245, 245, 245));
                }
            }
            public void mouseExited(MouseEvent evt) {
                btn.setBackground(bgColor);
            }
        });
    }

    public void setTotal(double total) {
        txtTotal.setText(String.format("%,.0f₫", total));
    }

    public JButton getBtnOrder() { return btnOrder; }
    public JButton getBtnViewCart() { return btnViewCart; }
}