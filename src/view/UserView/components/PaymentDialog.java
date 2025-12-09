package view.UserView.components;

import javax.swing.*;
import java.awt.*;

public class PaymentDialog extends JDialog {
    
    private JComboBox<String> cbPaymentMethod;
    private boolean isConfirmed = false; // Biến cờ để kiểm tra user có bấm Thanh toán không
    
    public PaymentDialog(JFrame parent, double totalAmount) {
        super(parent, "Xác Nhận Thanh Toán", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // --- Header Title ---
        JLabel lblTitle = new JLabel("THÔNG TIN THANH TOÁN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(new Color(0, 102, 204));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);

        // --- Center Info ---
        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        // Dòng 1: Hiển thị tổng tiền
        JLabel lblTotal = new JLabel("Tổng tiền: " + String.format("%,.0f VNĐ", totalAmount));
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
        lblTotal.setForeground(Color.RED);
        lblTotal.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Dòng 2: Label chọn phương thức
        JLabel lblMethod = new JLabel("Chọn phương thức thanh toán:");
        
        // Dòng 3: ComboBox
        String[] methods = {"Tiền mặt (COD)", "Chuyển khoản QR", "Thẻ Tín Dụng/Visa"};
        cbPaymentMethod = new JComboBox<>(methods);

        infoPanel.add(lblTotal);
        infoPanel.add(lblMethod);
        infoPanel.add(cbPaymentMethod);
        add(infoPanel, BorderLayout.CENTER);

        // --- Button Footer ---
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        
        JButton btnConfirm = new JButton("XÁC NHẬN THANH TOÁN");
        btnConfirm.setBackground(new Color(255, 153, 0));
        btnConfirm.setForeground(Color.WHITE);
        btnConfirm.setFont(new Font("Arial", Font.BOLD, 13));
        
        JButton btnCancel = new JButton("Hủy Bỏ");

        // Sự kiện nút Xác nhận
        btnConfirm.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this, 
                "Bạn chắc chắn muốn thanh toán qua " + cbPaymentMethod.getSelectedItem() + "?",
                "Xác nhận", JOptionPane.YES_NO_OPTION);
            
            if (choice == JOptionPane.YES_OPTION) {
                isConfirmed = true; // Đánh dấu là đã đồng ý mua
                dispose(); // Đóng dialog để quay về Controller xử lý tiếp
            }
        });

        // Sự kiện nút Hủy
        btnCancel.addActionListener(e -> dispose());

        btnPanel.add(btnConfirm);
        btnPanel.add(btnCancel);
        add(btnPanel, BorderLayout.SOUTH);
    }

    // Hàm để Controller kiểm tra xem user đã thanh toán thành công chưa
    public boolean isPaymentConfirmed() {
        return isConfirmed;
    }

    // Hàm để lấy phương thức thanh toán user đã chọn
    public String getPaymentMethod() {
        return (String) cbPaymentMethod.getSelectedItem();
    }
}