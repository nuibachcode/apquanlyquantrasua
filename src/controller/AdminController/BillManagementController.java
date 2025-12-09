package controller.AdminController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea; // Cần import JTextArea
import model.Bill;
import model.SelectedProduct; // Cần import SelectedProduct
import repository.BillRepositoryImpl;
import repository.IRepository.IBillRepository;
import view.AdminView.AdminView;


public class BillManagementController implements ActionListener {
    
    private AdminView adminView;
    private IBillRepository billRepository;

    public BillManagementController(AdminView adminView) {
        this.adminView = adminView;
        this.billRepository = new BillRepositoryImpl();
    }

    // Tải dữ liệu từ DB và cập nhật View
    public void loadBills() {
        List<Bill> bills = billRepository.findAll();
        adminView.updateBillList(bills);
        calculateRevenue(bills);
    }

    // Tính tổng doanh thu
    private void calculateRevenue(List<Bill> bills) {
        double totalRevenue = 0;
        for (Bill bill : bills) {
            if (bill.getIsCompleted()) {
                totalRevenue += bill.getTongTien();
            }
        }
        adminView.setTotalRevenue(totalRevenue);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        // Không có nút bấm, chỉ có logic TableModelEvent, nên hàm này có thể trống
    }

    // Phương thức MỚI: Hiển thị chi tiết hóa đơn khi người dùng click vào bảng
    public void showBillDetails(int billId) {
        Bill bill = billRepository.findById(billId);

        if (bill != null) {
            StringBuilder detail = new StringBuilder();
            
            // --- 1. Thông tin Khách hàng ---
            detail.append("--- THÔNG TIN KHÁCH HÀNG ---\n");
            detail.append(String.format("Tên Khách Hàng: %s\n", bill.getTen()));
            detail.append(String.format("SĐT: %s\n", bill.getSdt()));
            detail.append(String.format("Email: %s\n", bill.getEmail()));
            detail.append(String.format("Địa Chỉ: %s\n", bill.getDiaChi()));
            detail.append("------------------------------\n");

            // --- 2. Thông tin Đơn hàng ---
            detail.append("--- CHI TIẾT ĐƠN HÀNG (Mã HĐ: ").append(bill.getMaHD()).append(") ---\n");
            detail.append(String.format("Ngày Đặt: %s\n", bill.getNgayDat()));
            detail.append(String.format("Trạng thái: %s\n", bill.getIsCompleted() ? "Đã Hoàn Thành" : "Chưa Hoàn Thành"));
            detail.append("\nSản phẩm:\n");
            
            // --- 3. Chi tiết Sản phẩm ---
            double total = 0;
            for (SelectedProduct sp : bill.getSelectedProducts()) {
                double subTotal = sp.getTotalPrice();
                total += subTotal;
                detail.append(String.format("  - %s (x%d) @ %,.0f₫ = %,.0f₫\n", 
                    sp.getProduct().getTenSP(), 
                    sp.getQuantity(), 
                    sp.getProduct().getGia(),
                    subTotal
                ));
            }
            detail.append("------------------------------\n");
            detail.append(String.format("TỔNG TIỀN: %,.0f₫\n", bill.getTongTien()));
            
            JTextArea textArea = new JTextArea(detail.toString());
            textArea.setEditable(false);
            
            JOptionPane.showMessageDialog(
                adminView, 
                new JScrollPane(textArea), 
                "Chi Tiết Hóa Đơn #" + billId, 
                JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            JOptionPane.showMessageDialog(adminView, "Không tìm thấy hóa đơn có ID: " + billId, "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Phương thức này được gọi bởi TableModelListener trong BillManagementPanel
    public void updateBillCompletionStatus(int billId, boolean isCompleted) {
        try {
            Bill billToUpdate = billRepository.findById(billId);
            
            if (billToUpdate != null) {
                billToUpdate.setIsCompleted(isCompleted);
                billRepository.update(billToUpdate);
                JOptionPane.showMessageDialog(adminView, "Cập nhật trạng thái Hóa đơn ID " + billId + " thành công.");
            } else {
                JOptionPane.showMessageDialog(adminView, "Không tìm thấy Hóa đơn ID " + billId + " để cập nhật.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(adminView, "Lỗi cập nhật trạng thái hóa đơn: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        loadBills(); // Tải lại bills để cập nhật bảng và tổng doanh thu
    }
}