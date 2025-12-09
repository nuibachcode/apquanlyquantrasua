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

    // Định nghĩa màu sắc chủ đạo (Bạn có thể thay đổi mã màu tại đây)
    private final Color PRIMARY_COLOR = new Color(51, 153, 255); // Xanh dương tươi
    private final Color DANGER_COLOR = new Color(220, 53, 69);   // Đỏ dịu
    private final Color BG_COLOR = new Color(240, 248, 255);     // Nền xanh nhạt (AliceBlue)

    public HeaderPanel() {
        setLayout(new BorderLayout());
        setBackground(BG_COLOR);
        // Tăng padding cho toàn bộ header tạo cảm giác thoáng
        setBorder(new EmptyBorder(15, 20, 15, 20));

        // --- TRÁI: Tìm kiếm ---
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchPanel.setOpaque(false);

        JLabel lblSearch = new JLabel("Tìm sản phẩm: ");
        lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSearch.setForeground(new Color(80, 80, 80));

        txtSearch = new JTextField(20);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        // Tạo padding bên trong ô text để chữ không dính viền
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)), 
            new EmptyBorder(5, 8, 5, 8)
        ));

        btnSearch = new JButton("Tìm");
        styleButton(btnSearch, PRIMARY_COLOR); // Áp dụng style

        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        // --- PHẢI: Menu User ---
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        userPanel.setOpaque(false);
        
        // Khởi tạo nút
        btnHistory = new JButton("Lịch sử đơn hàng");
        btnProfile = new JButton("Thông tin cá nhân");
        btnLogout = new JButton("Đăng xuất");

        // Áp dụng style đồng bộ
        styleButton(btnHistory, PRIMARY_COLOR);
        styleButton(btnProfile, PRIMARY_COLOR); // Cùng màu với Lịch sử cho đồng bộ
        styleButton(btnLogout, DANGER_COLOR);   // Màu đỏ riêng biệt

        userPanel.add(btnHistory);
        userPanel.add(btnProfile);
        userPanel.add(btnLogout);

        add(searchPanel, BorderLayout.WEST);
        add(userPanel, BorderLayout.EAST);
    }

    /**
     * Hàm hỗ trợ style nút cho đồng bộ
     */
    private void styleButton(JButton btn, Color bgColor) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false); // Bỏ viền nét đứt khi click
        btn.setBorderPainted(false); // Bỏ viền lồi lõm mặc định
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Đổi con trỏ chuột thành bàn tay
        
        // Tạo độ dày cho nút (Padding: Trên, Trái, Dưới, Phải)
        btn.setBorder(new EmptyBorder(8, 15, 8, 15));

        // Hiệu ứng hover (Di chuột vào đổi màu nhẹ) - Tùy chọn nâng cao
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor.darker()); // Tối hơn chút khi hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor); // Trả về màu cũ
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