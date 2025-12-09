package view.UserView.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// [QUAN TRỌNG] Phải extends JPanel, KHÔNG ĐƯỢC extends JDialog
public class CartPanel extends JPanel {
    
    private JTextField txtTotal;
    private JButton btnOrder;
    private JButton btnViewCart;

    // Màu sắc đồng bộ với HeaderPanel
    private final Color BG_COLOR = new Color(240, 248, 255); 
    private final Color PRIMARY_ORANGE = new Color(255, 120, 0); // Cam đậm hơn chút cho sang

    public CartPanel() {
        // Tăng khoảng cách giữa các phần tử (gap) lên 20px
        setLayout(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        setBackground(BG_COLOR);
        
        // Viền trên mảnh màu xám để ngăn cách với danh sách sản phẩm
        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)));

        // --- 1. LABEL & TOTAL ---
        JLabel lblTitle = new JLabel("Tạm tính:");
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblTitle.setForeground(Color.DARK_GRAY);

        txtTotal = new JTextField(12); // Độ rộng vừa phải
        txtTotal.setEditable(false);
        txtTotal.setFont(new Font("Segoe UI", Font.BOLD, 18)); // Số tiền to, rõ
        txtTotal.setForeground(new Color(220, 53, 69)); // Màu đỏ chuẩn
        txtTotal.setBackground(Color.WHITE);
        txtTotal.setHorizontalAlignment(JTextField.RIGHT); // Số tiền căn phải
        
        // Style cho ô tổng tiền: Viền mỏng + Padding bên trong
        txtTotal.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(5, 10, 5, 10)
        ));
        txtTotal.setText("0₫");

        add(lblTitle);
        add(txtTotal);

        // --- 2. BUTTONS ---
        btnViewCart = new JButton("Xem giỏ hàng");
        btnOrder = new JButton("TIẾN HÀNH ĐẶT HÀNG");

        // Style nút phụ (Xem giỏ hàng): Nền trắng, chữ đen
        styleButton(btnViewCart, Color.WHITE, Color.BLACK);
        
        // Style nút chính (Đặt hàng): Nền cam, chữ trắng
        styleButton(btnOrder, PRIMARY_ORANGE, Color.WHITE);
        btnOrder.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Font to hơn nút thường
        btnOrder.setPreferredSize(new Dimension(220, 40)); // Nút đặt hàng dài hơn để dễ bấm

        add(btnViewCart);
        add(btnOrder);
    }

    /**
     * Hàm style nút bấm dùng chung (Giống HeaderPanel)
     */
    private void styleButton(JButton btn, Color bgColor, Color fgColor) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bgColor);
        btn.setForeground(fgColor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Padding cho nút thoáng hơn
        btn.setBorder(new EmptyBorder(8, 15, 8, 15));

        // Nếu là nút trắng (Xem giỏ hàng) thì thêm viền mỏng cho dễ nhìn
        if (bgColor.equals(Color.WHITE)) {
            btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                new EmptyBorder(8, 15, 8, 15)
            ));
        }

        // Hiệu ứng Hover
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                if (!bgColor.equals(Color.WHITE)) {
                    btn.setBackground(bgColor.darker());
                } else {
                    btn.setBackground(new Color(245, 245, 245)); // Xám nhẹ khi hover nút trắng
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