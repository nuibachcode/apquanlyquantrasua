package controller.AdminController;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import model.Bill;
import model.SelectedProduct;
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
        // Hàm này có thể trống nếu bạn xử lý sự kiện trực tiếp trên TableModel
    }

    public void deleteBill(int billId) {
        // Hỏi lại người dùng trước khi xóa
        int confirm = JOptionPane.showConfirmDialog(
            adminView, 
            "Bạn có chắc chắn muốn xóa Hóa đơn #" + billId + " không? Hành động này không thể hoàn tác.", 
            "Xác nhận Xóa", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                billRepository.delete(billId);
                JOptionPane.showMessageDialog(adminView, "Đã xóa thành công Hóa đơn #" + billId, "Thành công", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(adminView, "Lỗi khi xóa hóa đơn: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
            loadBills(); // Tải lại danh sách để cập nhật giao diện
        }
    }


    // --- [CẬP NHẬT] HIỂN THỊ CHI TIẾT & IN HÓA ĐƠN ---
    public void showBillDetails(int billId) {
        Bill bill = billRepository.findById(billId);

        if (bill != null) {
            // ... (Phần tạo nội dung detail giữ nguyên)
            
            StringBuilder detail = new StringBuilder();
            // ... (Nội dung chi tiết hóa đơn giữ nguyên)
            detail.append("--- THÔNG TIN KHÁCH HÀNG ---\n");
            detail.append(String.format("Tên Khách Hàng: %s\n", bill.getTen())); 
            detail.append(String.format("SĐT: %s\n", bill.getSdt()));
            detail.append(String.format("Email: %s\n", bill.getEmail()));
            detail.append(String.format("Địa Chỉ: %s\n", bill.getDiaChi()));
            detail.append("------------------------------\n");

            detail.append("--- CHI TIẾT ĐƠN HÀNG (Mã HĐ: ").append(bill.getMaHD()).append(") ---\n");
            detail.append(String.format("Ngày Đặt: %s\n", bill.getNgayDat()));
            detail.append(String.format("Trạng thái: %s\n", bill.getIsCompleted() ? "Đã Hoàn Thành" : "Chưa Hoàn Thành"));
            detail.append("\nSản phẩm:\n");
            
            for (SelectedProduct sp : bill.getSelectedProducts()) {
                double subTotal = sp.getTotalPrice(); 
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
            textArea.setRows(15);
            textArea.setColumns(40);
            
            // --- CẬP NHẬT: Thêm nút XÓA ---
            Object[] options = {"In Hóa Đơn (PDF)", "Xóa Đơn Hàng", "Đóng"};
            
            int choice = JOptionPane.showOptionDialog(
                adminView, 
                new JScrollPane(textArea), 
                "Chi Tiết Hóa Đơn #" + billId, 
                JOptionPane.YES_NO_CANCEL_OPTION, // Thay đổi thành 3 tùy chọn
                JOptionPane.INFORMATION_MESSAGE, 
                null, 
                options, 
                options[2] // Mặc định chọn nút Đóng
            );

            // Xử lý lựa chọn
            if (choice == 0) { // Index 0: In Hóa Đơn
                exportBillToPDF(bill);
            } else if (choice == 1) { // Index 1: Xóa Đơn Hàng
                deleteBill(billId); // Gọi hàm xóa mới
            }

        } else {
            JOptionPane.showMessageDialog(adminView, "Không tìm thấy hóa đơn có ID: " + billId, "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // --- [MỚI] HÀM XUẤT FILE PDF ---
    private void exportBillToPDF(Bill bill) {
        try {
            String fileName = "HoaDon_" + bill.getMaHD() + ".pdf";
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            
            document.open();
            
            // Cấu hình Font chữ tiếng Việt (Arial trên Windows)
            // Nếu bạn dùng Mac/Linux, đường dẫn này cần thay đổi
            BaseFont bf = BaseFont.createFont("c:\\windows\\fonts\\arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font fontTitle = new Font(bf, 18, Font.BOLD, BaseColor.BLUE);
            Font fontBold = new Font(bf, 12, Font.BOLD, BaseColor.BLACK);
            Font fontNormal = new Font(bf, 12, Font.NORMAL, BaseColor.BLACK);

            // 1. Tiêu đề
            Paragraph title = new Paragraph("HÓA ĐƠN THANH TOÁN", fontTitle);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" ")); // Dòng trống

            // 2. Thông tin chung
            document.add(new Paragraph("Mã hóa đơn: #" + bill.getMaHD(), fontBold));
            document.add(new Paragraph("Ngày đặt: " + bill.getNgayDat(), fontNormal));
            document.add(new Paragraph("Khách hàng: " + bill.getTen(), fontNormal));
            document.add(new Paragraph("SĐT: " + bill.getSdt(), fontNormal));
            document.add(new Paragraph("Địa chỉ: " + bill.getDiaChi(), fontNormal));
            document.add(new Paragraph("----------------------------------------------------------"));

            // 3. Bảng sản phẩm (PdfPTable)
            PdfPTable table = new PdfPTable(4); // 4 Cột
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);
            
            // Header bảng
            table.addCell(new PdfPCell(new Phrase("Tên món", fontBold)));
            table.addCell(new PdfPCell(new Phrase("SL", fontBold)));
            table.addCell(new PdfPCell(new Phrase("Đơn giá", fontBold)));
            table.addCell(new PdfPCell(new Phrase("Thành tiền", fontBold)));

            // Dữ liệu bảng
            for (SelectedProduct sp : bill.getSelectedProducts()) {
                double subTotal = sp.getProduct().getGia() * sp.getQuantity();
                
                table.addCell(new Phrase(sp.getProduct().getTenSP(), fontNormal));
                table.addCell(new Phrase(String.valueOf(sp.getQuantity()), fontNormal));
                table.addCell(new Phrase(String.format("%,.0f", sp.getProduct().getGia()), fontNormal));
                table.addCell(new Phrase(String.format("%,.0f", subTotal), fontNormal));
            }
            document.add(table);

            // 4. Tổng tiền
            Paragraph total = new Paragraph("TỔNG TIỀN: " + String.format("%,.0f VNĐ", bill.getTongTien()), fontTitle);
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);
            
            document.add(new Paragraph("\nCảm ơn quý khách!", fontNormal));

            document.close();
            
            // Tự động mở file sau khi in
            if (Desktop.isDesktopSupported()) {
                File myFile = new File(fileName);
                Desktop.getDesktop().open(myFile);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(adminView, "Lỗi khi in hóa đơn: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Cập nhật trạng thái hoàn thành
    public void updateBillCompletionStatus(int billId, boolean isCompleted) {
        try {
            Bill billToUpdate = billRepository.findById(billId);
            
            if (billToUpdate != null) {
                billToUpdate.setIsCompleted(isCompleted);
                billRepository.update(billToUpdate);
                // JOptionPane.showMessageDialog(adminView, "Đã cập nhật trạng thái."); // Có thể bỏ comment nếu muốn thông báo mỗi lần tick
            } else {
                JOptionPane.showMessageDialog(adminView, "Không tìm thấy Hóa đơn ID " + billId + " để cập nhật.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(adminView, "Lỗi cập nhật trạng thái: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        loadBills(); // Refresh lại giao diện
    }
}