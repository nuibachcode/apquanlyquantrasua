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

    // Định nghĩa các phương thức thanh toán
    private final String[] PAYMENT_OPTIONS = {"Tiền mặt (COD)", "Chuyển khoản QR", "Thẻ Tín Dụng/Visa"};

    public OrderHistoryDialog(JFrame parent, List<Bill> bills) {
        super(parent, "Lịch Sử Đặt Hàng", true);
        this.myBills = bills;
        this.billRepo = new BillRepositoryImpl();
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
                String statusText;
                if (b.getIsCompleted()) {
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

        // Lấy thông tin đơn hàng
        // Chú ý: Cần đảm bảo cột 0 (Mã Đơn) có kiểu dữ liệu khớp với b.getMaHD()
        Object billIdObj = table.getValueAt(selectedRow, 0); 
        
        // Xử lý ID là Long/BigInt nếu cần (theo Saved Information của bạn)
        // Nếu MaHD là Long (BIGINT), thì cần ép kiểu an toàn hơn. 
        // Hiện tại bạn đang dùng (int), tôi giữ lại để phù hợp với mã cũ.
        int billId;
        if (billIdObj instanceof Integer) {
             billId = (int) billIdObj;
        } else {
             // Giả định nó là String hoặc Long và có thể chuyển đổi được
             billId = Integer.parseInt(billIdObj.toString()); 
        }

        String amount = table.getValueAt(selectedRow, 2).toString();

        // -----------------------------------------------------
        // THAY THẾ JOptionPane đơn giản bằng logic chọn phương thức
        // -----------------------------------------------------

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Tổng tiền: " + amount));
        panel.add(new JLabel("Chọn phương thức thanh toán:"));
        
        JComboBox<String> cbPaymentMethod = new JComboBox<>(PAYMENT_OPTIONS);
        panel.add(cbPaymentMethod);
        
        int result = JOptionPane.showConfirmDialog(
            this, 
            panel, 
            "Xác Nhận Thanh Toán Đơn #" + billId, 
            JOptionPane.OK_CANCEL_OPTION, 
            JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String selectedMethod = (String) cbPaymentMethod.getSelectedItem();
            
            // 1. Cập nhật trong file/database
            billRepo.updatePaymentStatus(billId);
            
            // 2. Cập nhật trạng thái trong List bộ nhớ hiện tại
            for(Bill b : myBills) {
                if(b.getMaHD() == billId) {
                    b.setIsCompleted(true);
                    break;
                }
            }

            // 3. Refresh bảng
            refreshTableData();
            
            JOptionPane.showMessageDialog(this, 
                "Thanh toán thành công qua phương thức: " + selectedMethod + "!", 
                "Hoàn tất", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Hủy bỏ thanh toán
            JOptionPane.showMessageDialog(this, "Đã hủy bỏ thanh toán cho đơn hàng #" + billId + ".");
        }
    }
}