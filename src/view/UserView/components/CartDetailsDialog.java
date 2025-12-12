package view.UserView.components;

import model.SelectedProduct;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;

import java.util.List;

public class CartDetailsDialog extends JDialog {

    private DefaultTableModel model;
    private JTable table;
    private List<SelectedProduct> cart;    // reference GIỎ HÀNG THẬT
    private JLabel lblTotal;
    // THÊM: Listener để thông báo cập nhật giỏ hàng ra bên ngoài
    private CartUpdateListener listener; 

    public CartDetailsDialog(JFrame parent, List<SelectedProduct> cart, CartUpdateListener listener) {
        super(parent, "Chi Tiết Giỏ Hàng", true);

        this.cart = cart;
        this.listener = listener; // LƯU LISTENER

        this.setTitle("Chi Tiết Giỏ Hàng - " + (cart.isEmpty() ? "Trống" : "Tổng: " + String.format("%,.0f₫", calculateTotal())));

        setSize(700, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // Header
        JLabel lblTitle = new JLabel("GIỎ HÀNG CỦA BẠN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(new Color(255, 69, 0));
        add(lblTitle, BorderLayout.NORTH);

        // Table setup
        String[] columns = {"Tên món", "Số Lượng", "Đơn Giá", "Thành Tiền"};

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 1;
            }
        };

        table = new JTable(model);
        table.setRowHeight(40);

        table.getColumnModel().getColumn(1).setCellRenderer(new QuantityButtonRenderer());
        table.getColumnModel().getColumn(1).setCellEditor(new QuantityButtonEditor());

        add(new JScrollPane(table), BorderLayout.CENTER);

        loadTableData();

        // Footer
        JPanel footer = new JPanel(new BorderLayout());

        JButton btnRemove = new JButton("HỦY MÓN");
        btnRemove.addActionListener(e -> handleRemoveItem());

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.add(btnRemove);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblTotal = new JLabel();
        lblTotal.setFont(new Font("Arial", Font.BOLD, 18));

        JButton btnClose = new JButton("Đóng");
        btnClose.addActionListener(e -> {
            if (table.isEditing()) table.getCellEditor().stopCellEditing();
            dispose();
        });

        rightPanel.add(lblTotal);
        rightPanel.add(btnClose);

        footer.add(leftPanel, BorderLayout.WEST);
        footer.add(rightPanel, BorderLayout.EAST);

        add(footer, BorderLayout.SOUTH);

        updateTotalLabel();
    }
    
    // THÊM: Hàm tính tổng
    private double calculateTotal() {
        double total = 0;
        for (SelectedProduct sp : cart)
            total += sp.getQuantity() * sp.getProduct().getGia();
        return total;
    }

    private void loadTableData() {
        model.setRowCount(0);

        for (SelectedProduct sp : cart) {
            model.addRow(new Object[]{
                    sp.getProduct().getTenSP(),
                    sp.getQuantity(),
                    String.format("%,.0f₫", sp.getProduct().getGia()),
                    String.format("%,.0f₫", sp.getQuantity() * sp.getProduct().getGia())
            });
        }
    }

    private void updateTotalLabel() {
        double total = calculateTotal();
        lblTotal.setText("Tổng cộng: " + String.format("%,.0f VNĐ", total));
        // Cập nhật title của dialog
        this.setTitle("Chi Tiết Giỏ Hàng - " + (cart.isEmpty() ? "Trống" : "Tổng: " + String.format("%,.0f₫", total)));
    }

    private void handleRemoveItem() {
        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Chọn món cần hủy.");
            return;
        }

        String name = (String) model.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Hủy món: " + name + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {

            if (table.isEditing()) table.getCellEditor().stopCellEditing();

            // Sửa lỗi: Cần remove khỏi cart trước khi remove khỏi model
            // vì index của cart và model phải khớp nhau
            cart.remove(row); 
            model.removeRow(row);

            updateTotalLabel();
            
            // GỌI CALLBACK ĐỂ CẬP NHẬT TỔNG Ở MÀN HÌNH CHÍNH
            if (listener != null) listener.onCartUpdated();
        }
    }

    public List<SelectedProduct> getUpdatedCart() {
        return cart;
    }

    // ==========================================
    //    RENDERER + EDITOR CHO NÚT + / -
    // ==========================================
    private class QuantityButtonRenderer extends JPanel implements TableCellRenderer {

        JLabel lbl;
        JButton btnMinus, btnPlus;

        public QuantityButtonRenderer() {
            setLayout(new BorderLayout());
            btnMinus = new JButton("-");
            btnPlus = new JButton("+");
            lbl = new JLabel("0", SwingConstants.CENTER);
            lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 14f));
            
            // Giảm kích thước nút
            btnMinus.setPreferredSize(new Dimension(30, 25));
            btnPlus.setPreferredSize(new Dimension(30, 25));

            add(btnMinus, BorderLayout.WEST);
            add(lbl, BorderLayout.CENTER);
            add(btnPlus, BorderLayout.EAST);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                      boolean isSelected, boolean hasFocus,
                                                      int row, int column) {
            lbl.setText(value.toString());
            return this;
        }
    }

    private class QuantityButtonEditor extends AbstractCellEditor implements TableCellEditor {

        JPanel panel;
        JLabel lbl;
        JButton btnMinus, btnPlus;
        int value;
        int rowIndex;

        public QuantityButtonEditor() {
            panel = new JPanel(new BorderLayout());
            btnMinus = new JButton("-");
            btnPlus = new JButton("+");
            lbl = new JLabel("0", SwingConstants.CENTER);
            lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 14f));
            
            // Giảm kích thước nút
            btnMinus.setPreferredSize(new Dimension(30, 25));
            btnPlus.setPreferredSize(new Dimension(30, 25));

            panel.add(btnMinus, BorderLayout.WEST);
            panel.add(lbl, BorderLayout.CENTER);
            panel.add(btnPlus, BorderLayout.EAST);

            btnMinus.addActionListener(e -> {
                if (value > 1) {
                    value--;
                    lbl.setText(value + "");
                    updateQuantity(rowIndex, value);
                } else if (value == 1) {
                    // Hỏi xác nhận xóa khi giảm từ 1 về 0
                     int confirm = JOptionPane.showConfirmDialog(panel,
                        "Giảm số lượng món này về 0 sẽ xóa món khỏi giỏ hàng. Tiếp tục?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
                     if (confirm == JOptionPane.YES_OPTION) {
                         // Thực hiện xóa món (giống hàm handleRemoveItem, nhưng dùng rowIndex)
                         stopCellEditing(); // Dừng edit trước khi xóa
                         cart.remove(rowIndex);
                         model.removeRow(rowIndex);
                         
                         updateTotalLabel();
                         if (listener != null) listener.onCartUpdated();
                     }
                }
            });

            btnPlus.addActionListener(e -> {
                value++;
                lbl.setText(value + "");
                updateQuantity(rowIndex, value);
            });
        }
        
        // Cần dừng việc chỉnh sửa để đảm bảo giá trị mới được lưu
        // Nếu không có, bảng sẽ bị lỗi hiển thị.
       

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                    boolean isSelected, int row, int col) {

            this.rowIndex = row;
            // Ép kiểu Object thành Integer cho chắc chắn
            this.value = (Integer) value; 
            lbl.setText(value.toString());

            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return value;
        }
    }

    // ============================
    // CẬP NHẬT SỐ LƯỢNG THẬT
    // ============================
    private void updateQuantity(int row, int newQuantity) {
        
        // Đảm bảo chỉ cập nhật khi row còn hợp lệ (chưa bị xóa)
        if (row < 0 || row >= cart.size()) return;

        SelectedProduct sp = cart.get(row);
        sp.setQuantity(newQuantity);

        // Cập nhật trên JTable
        model.setValueAt(newQuantity, row, 1);
        model.setValueAt(
                String.format("%,.0f₫", newQuantity * sp.getProduct().getGia()),
                row, 3
        );

        updateTotalLabel();
        
        // GỌI CALLBACK ĐỂ CẬP NHẬT TỔNG Ở MÀN HÌNH CHÍNH
        if (listener != null) listener.onCartUpdated();
    }
}