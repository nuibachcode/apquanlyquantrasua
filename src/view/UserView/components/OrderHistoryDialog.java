package view.UserView.components;

import model.Bill;
import repository.BillRepositoryImpl;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class OrderHistoryDialog extends JDialog {
    
    private JTable table;
    private DefaultTableModel tableModel;
    private BillRepositoryImpl billRepo;
    private List<Bill> myBills; // Danh sách đang hiển thị

    public OrderHistoryDialog(JFrame parent, List<Bill> bills) {
        super(parent, "Lịch Sử Đặt Hàng", true);
        this.myBills = bills;
        this.billRepo = new BillRepositoryImpl(); // Để gọi hàm update
        
        setSize(850, 500);
        setLocationRelativeTo(parent);
        
        // --- Title ---
        JLabel lblTitle = new JLabel("DANH SÁCH ĐƠN HÀNG CỦA BẠN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(new Color(0, 102, 0));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15,0,15,0));
        add(lblTitle, BorderLayout.NORTH);

        // --- Table ---
        String[] columnNames = {"Mã Đơn", "Ngày Đặt", "Tổng Tiền", "Trạng Thái", "Địa Chỉ Giao"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        
        // Load data lần đầu
        refreshTableData();

        add(new JScrollPane(table), BorderLayout.CENTER);

        // --- Buttons Panel ---
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        
        // NÚT THANH TOÁN
        JButton btnPay = new JButton("THANH TOÁN NGAY");
        btnPay.setBackground(new Color(255, 140, 0));
        btnPay.setForeground(Color.WHITE);
        btnPay.setFont(new Font("Arial", Font.BOLD, 14));
        
        JButton btnClose = new JButton("Đóng");
        btnClose.setFont(new Font("Arial", Font.BOLD, 14));

        // Logic sự kiện Thanh Toán
        btnPay.addActionListener(e -> handlePayment());
        btnClose.addActionListener(e -> dispose());

        btnPanel.add(btnPay);
        btnPanel.add(btnClose);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void refreshTableData() {
        tableModel.setRowCount(0);
        if (myBills != null) {
            for (Bill b : myBills) {
                // Kiểm tra boolean isCompleted để hiển thị text
                String statusText;
                if (b.getIsCompleted()) { // Giả sử model Bill có hàm này
                    statusText = "Đã thanh toán ";
                } else {
                    statusText = "Chờ thanh toán (COD)";
                }

                Object[] row = {
                    b.getMaHD(), 
                    b.getNgayDat(), 
                    String.format("%,.0f₫", b.getTongTien()),
                    statusText, 
                    b.getDiaChi()
                };
                tableModel.addRow(row);
            }
        }
    }

    private void handlePayment() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đơn hàng cần thanh toán!");
            return;
        }

        // Kiểm tra xem đã thanh toán chưa (Cột 3 là cột Trạng thái)
        String currentStatus = table.getValueAt(selectedRow, 3).toString();
        if (currentStatus.contains("Đã thanh toán")) {
            JOptionPane.showMessageDialog(this, "Đơn hàng này đã được thanh toán rồi!");
            return;
        }

        int billId = (int) table.getValueAt(selectedRow, 0);
        String amount = table.getValueAt(selectedRow, 2).toString();

        // Mở hộp thoại xác nhận thanh toán (Dùng lại PaymentDialog của bạn hoặc confirm đơn giản)
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Xác nhận thanh toán số tiền " + amount + " cho đơn hàng #" + billId + "?",
            "Thanh Toán", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // 1. Cập nhật trong file/database
            billRepo.updatePaymentStatus(billId);
            
            // 2. Cập nhật trạng thái trong List bộ nhớ hiện tại để bảng hiển thị đúng ngay lập tức
            for(Bill b : myBills) {
                if(b.getMaHD() == billId) {
                    b.setIsCompleted(true);
                    break;
                }
            }

            // 3. Refresh bảng
            refreshTableData();
            JOptionPane.showMessageDialog(this, "Thanh toán thành công! ");
        }
    }
}