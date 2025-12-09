package view.UserView.components;

import model.SelectedProduct;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

// [QUAN TRỌNG] File này mới là JDialog
public class CartDetailsDialog extends JDialog {

    public CartDetailsDialog(JFrame parent, List<SelectedProduct> cart) {
        super(parent, "Chi Tiết Giỏ Hàng", true);
        setSize(600, 450);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // 1. Header
        JLabel lblTitle = new JLabel("GIỎ HÀNG CỦA BẠN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(new Color(0, 102, 204));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(lblTitle, BorderLayout.NORTH);

        // 2. Table
        String[] columns = {"Sản Phẩm", "Số Lượng", "Đơn Giá", "Thành Tiền"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        
        double total = 0;
        for (SelectedProduct sp : cart) {
            double lineTotal = sp.getQuantity() * sp.getProduct().getGia();
            total += lineTotal;
            model.addRow(new Object[]{
                sp.getProduct().getTenSP(),
                sp.getQuantity(),
                String.format("%,.0f₫", sp.getProduct().getGia()),
                String.format("%,.0f₫", lineTotal)
            });
        }

        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // 3. Footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        
        JLabel lblTotal = new JLabel("Tổng cộng: " + String.format("%,.0f VNĐ", total));
        lblTotal.setFont(new Font("Arial", Font.BOLD, 18));
        lblTotal.setForeground(Color.RED);
        
        JButton btnClose = new JButton("Đóng");
        btnClose.setFont(new Font("Arial", Font.BOLD, 14));
        btnClose.addActionListener(e -> dispose());

        footer.add(lblTotal);
        footer.add(btnClose);
        add(footer, BorderLayout.SOUTH);
    }
}   