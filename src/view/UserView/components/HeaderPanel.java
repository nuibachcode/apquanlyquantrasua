package view.UserView.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class HeaderPanel extends JPanel {
    private JTextField txtSearch;
    private JButton btnSearch;
    private JButton btnHistory;
    private JButton btnProfile;
    private JButton btnLogout;

    // Định nghĩa màu sắc
    private final Color PRIMARY_COLOR = new Color(51, 153, 255);
    private final Color DANGER_COLOR = new Color(220, 53, 69);
    private final Color BG_COLOR = new Color(240, 248, 255);

    public HeaderPanel() {
        setLayout(new BorderLayout());
        setBackground(BG_COLOR);
        setBorder(new EmptyBorder(15, 20, 15, 20));

        // --- TRÁI: Tìm kiếm ---
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchPanel.setOpaque(false);

        JLabel lblSearch = new JLabel("Tìm sản phẩm: ");
        lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSearch.setForeground(new Color(80, 80, 80));

        txtSearch = new JTextField(20);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // SỬA: Với FlatLaf, để TextField bo tròn đẹp, ta nên dùng putClientProperty
        // thay vì setBorder cứng. Tuy nhiên nếu muốn giữ padding, ta dùng margin.
        txtSearch.putClientProperty("JTextField.placeholderText", "Nhập tên món...");
        txtSearch.putClientProperty("TextComponent.arc", 20); // Bo tròn riêng cho ô tìm kiếm
        // Thêm padding bên trong text field bằng Margin
        txtSearch.setMargin(new Insets(5, 8, 5, 8));

        btnSearch = new JButton("Tìm");
        styleButton(btnSearch, PRIMARY_COLOR);

        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        // --- PHẢI: Menu User ---
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        userPanel.setOpaque(false);
        
        btnHistory = new JButton("Lịch sử đơn hàng");
        btnProfile = new JButton("Thông tin cá nhân");
        btnLogout = new JButton("Đăng xuất");

        styleButton(btnHistory, PRIMARY_COLOR);
        styleButton(btnProfile, PRIMARY_COLOR);
        styleButton(btnLogout, DANGER_COLOR);

        userPanel.add(btnHistory);
        userPanel.add(btnProfile);
        userPanel.add(btnLogout);

        add(searchPanel, BorderLayout.WEST);
        add(userPanel, BorderLayout.EAST);
    }

    /**
     * Hàm hỗ trợ style nút
     * ĐÃ CHỈNH SỬA ĐỂ TƯƠNG THÍCH FLATLAF BO TRÒN
     */
    private void styleButton(JButton btn, Color bgColor) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        
        // 1. QUAN TRỌNG: Không được tắt BorderPainted nếu muốn FlatLaf vẽ bo tròn
        btn.setFocusPainted(false); 
        // btn.setBorderPainted(false); // <-- XÓA DÒNG NÀY
        
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // 2. QUAN TRỌNG: Dùng setMargin thay vì setBorder để giữ viền bo tròn
        // (Padding: Trên, Trái, Dưới, Phải)
        btn.setMargin(new Insets(8, 15, 8, 15)); 
        // btn.setBorder(new EmptyBorder(8, 15, 8, 15)); // <-- XÓA DÒNG NÀY

        // 3. Hiệu ứng Hover (Giữ nguyên logic cũ của bạn)
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor);
            }
        });
    }

    public String getSearchKeyword() { return txtSearch.getText(); }
    public JButton getBtnSearch() { return btnSearch; }
    public JButton getBtnProfile() { return btnProfile; }
    public JButton getBtnLogout() { return btnLogout; }
    public JButton getBtnHistory() { return btnHistory; } 
    public JTextField getTxtSearch() { return txtSearch; }
}