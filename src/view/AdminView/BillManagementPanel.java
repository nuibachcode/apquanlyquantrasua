package view.AdminView;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.ListSelectionListener; // Import mới
import model.Bill;
import java.awt.event.ActionListener;
import controller.AdminController.BillManagementController;

public class BillManagementPanel extends JPanel {
    private JTable billTable;
    private DefaultTableModel billTableModel;
    // Đã loại bỏ private JButton btnBillViewAll;

    public BillManagementPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        controlPanel.setBorder(BorderFactory.createTitledBorder("Thao tác Hóa đơn"));
        add(controlPanel, BorderLayout.NORTH);
        // 2. Table Panel (CENTER)
        billTableModel = new DefaultTableModel(new Object[] { "Mã HĐ", "Tên Khách Hàng", "Ngày", "Hoàn thành" }, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // Giả định cột "Hoàn thành" là cột 3 (index 3)
                if (columnIndex == 3) return Boolean.class; 
                return super.getColumnClass(columnIndex);
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Chỉ cột "Hoàn thành" được chỉnh sửa
            }
        };
        billTable = new JTable(billTableModel);
        billTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        billTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = billTable.getSelectedRow();
                    if (selectedRow != -1) {
                        try {
                            // Lấy Mã HĐ (ID) từ cột đầu tiên
                            int billId = (int) billTableModel.getValueAt(selectedRow, 0);
                            // Gọi Controller để hiển thị chi tiết
                            // Lưu ý: Cần truyền Controller vào phương thức này
                            if (billTable.getParent().getParent().getParent() instanceof BillManagementPanel) {
 
                            }
                        } catch (Exception ex) {
                            // Không cần thiết phải hiển thị lỗi khi chọn, nhưng giữ lại để debug
                        }
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(billTable);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    // Phương thức gắn Controller và TableModelListener
    public void addController(ActionListener listener, BillManagementController billManagementController) {
        // Đã loại bỏ việc gắn listener cho btnBillViewAll
        
        // <<< THÊM LOGIC GỌI HÀM CHI TIẾT KHI CHỌN HÀNG >>>
        billTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            private int lastSelectedRow = -1;
            @Override
            public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = billTable.getSelectedRow();
                    if (selectedRow != -1 && selectedRow != lastSelectedRow) {
                        try {
                            int billId = (int) billTableModel.getValueAt(selectedRow, 0);
                            // Gọi Controller để hiển thị chi tiết hóa đơn
                            billManagementController.showBillDetails(billId);
                        } catch (Exception ex) {
                             JOptionPane.showMessageDialog(BillManagementPanel.this, "Lỗi khi xem chi tiết: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    lastSelectedRow = selectedRow;
                }
            }
        });

        // Gắn TableModelListener để gọi phương thức cập nhật trạng thái
        billTableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 3) { // Cột 3 là Hoàn thành
                    int row = e.getFirstRow();
                    if (row != -1) {
                        try {
                            int billId = (int) billTableModel.getValueAt(row, 0);
                            boolean isCompleted = (boolean) billTableModel.getValueAt(row, 3);
                            
                            // Gọi trực tiếp phương thức trong BillManagementController
                            billManagementController.updateBillCompletionStatus(billId, isCompleted);
                            
                        } catch (Exception ex) {
                            // Cần hiển thị lỗi một cách an toàn
                             SwingUtilities.invokeLater(() -> {
                                 JOptionPane.showMessageDialog(null, "Lỗi cập nhật trạng thái hóa đơn: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                             });
                        }
                    }
                }
            }
        });
    }

    // Phương thức cập nhật bảng từ Controller
    public void updateBillList(List<Bill> bills) {
        billTableModel.setRowCount(0);
        if (bills != null) {
            for (Bill bill : bills) {
                // Chỉ hiển thị 4 cột theo yêu cầu: Mã HĐ, Tên Khách Hàng, Ngày, Hoàn thành
                billTableModel.addRow(new Object[] {
                    bill.getMaHD(),
                    bill.getTen(), // Giả định getTen() là Tên Khách Hàng
                    bill.getNgayDat(),
                    bill.getIsCompleted()
                });
            }
        }
    }
}