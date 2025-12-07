package view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import model.Bill; // Cần import model.Bill
// Cần import model.SelectedProduct và model.Product nếu không lỗi sẽ xảy ra trong BillView
// Giả định bạn có thể resolve các import này

public class BillView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable billTable;
    private DefaultTableModel tableModel;
    private List<Bill> billList;

    private final int FRAME_WIDTH = 1200;
    private final int FRAME_HEIGHT = 700;

    /**
     * Constructor nhận danh sách hóa đơn để hiển thị.
     * Đây là constructor bị thiếu mà AdminController đang gọi.
     */
    public BillView(List<Bill> bills) {
        this.billList = bills;
        setTitle("Quản Lý Hóa Đơn");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setBounds(100, 100, FRAME_WIDTH, FRAME_HEIGHT);
        setLocationRelativeTo(null);
        
        contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);

        // ===== 1. Thiết lập Table Model =====
        String[] columnNames = {"Mã HĐ", "Tên Khách Hàng", "SĐT", "Địa Chỉ", "Ngày Đặt", "Tổng Tiền", "Hoàn Thành"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 6) return Boolean.class; 
                return String.class;
            }
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; 
            }
        };

        // ===== 2. Tạo JTable và ScrollPane =====
        billTable = new JTable(tableModel);
        billTable.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(billTable);
        contentPane.add(scrollPane, BorderLayout.CENTER);
        
        // ===== 3. Nạp dữ liệu vào bảng =====
        loadBillData();

        // ===== 4. Thêm chức năng xem chi tiết =====
        addDetailViewListener();
    }

    private void loadBillData() {
        tableModel.setRowCount(0); 
        if (billList != null) {
            for (Bill bill : billList) {
                Object[] row = new Object[]{
                    bill.getMaHD(),
                    bill.getTen(),
                    bill.getSdt(),
                    // Giả định getDiaChi() và getIsCompleted() đã tồn tại trong model.Bill
                    bill.getDiaChi(), 
                    bill.getNgayDat(),
                    String.format("%,.0f₫", bill.getTongTien()),
                    bill.getIsCompleted()
                };
                tableModel.addRow(row);
            }
        }
    }
    
    // Phương thức để xem chi tiết hóa đơn (khi double click)
    private void addDetailViewListener() {
        billTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { 
                    int selectedRow = billTable.getSelectedRow();
                    if (selectedRow != -1) {
                        int billId = (int) billTable.getValueAt(selectedRow, 0);
                        
                        Bill selectedBill = null;
                        for (Bill bill : billList) {
                            if (bill.getMaHD() == billId) {
                                selectedBill = bill;
                                break;
                            }
                        }
                        
                        if (selectedBill != null) {
                            showBillDetailsPopup(selectedBill);
                        }
                    }
                }
            }
        });
    }
    
    // Logic hiển thị chi tiết hóa đơn trong pop-up
    private void showBillDetailsPopup(Bill bill) {
        StringBuilder details = new StringBuilder();
        details.append("--- THÔNG TIN KHÁCH HÀNG ---\n");
        details.append(String.format("Tên: %s\n", bill.getTen()));
        details.append(String.format("SĐT: %s\n", bill.getSdt()));
        details.append(String.format("Email: %s\n", bill.getEmail()));
        details.append(String.format("Địa Chỉ: %s\n", bill.getDiaChi()));
        details.append("-------------------------------------------\n");
        details.append("--- CHI TIẾT SẢN PHẨM ---\n");
        
        // Giả định getSelectedProducts() đã tồn tại trong model.Bill
        if (bill.getSelectedProducts() != null) { 
            for (model.SelectedProduct sp : bill.getSelectedProducts()) {
                // Giả định getProduct(), getTenSP(), getQuantity(), getGia() tồn tại
                details.append(String.format("%s x %d (Giá: %,.0f₫)\n", 
                    sp.getProduct().getTenSP(), 
                    sp.getQuantity(), 
                    sp.getProduct().getGia()));
            }
        } else {
             details.append("Không có chi tiết sản phẩm.\n");
        }
        
        details.append("-------------------------------------------\n");
        details.append(String.format("TỔNG CỘNG: %,.0f₫", bill.getTongTien()));

        JTextArea textArea = new JTextArea(details.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        
        JOptionPane.showMessageDialog(this, new JScrollPane(textArea), 
            "Chi Tiết Đơn Hàng #" + bill.getMaHD() + " (" + bill.getNgayDat() + ")", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
   
}