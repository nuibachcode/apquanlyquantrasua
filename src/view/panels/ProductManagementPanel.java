package view.panels;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Product;
import java.awt.event.ActionListener;
import javax.swing.event.ListSelectionListener; // Cần thiết

public class ProductManagementPanel extends JPanel {
    private JTextField tfProductId, tfProductName, tfProductPrice, tfProductImage; // Thêm tfProductImage
    private JTable productTable;
    private DefaultTableModel productTableModel;
    private JButton btnProductAdd, btnProductUpdate, btnProductDelete, btnProductClear;

    public ProductManagementPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. Form và Button Panel (NORTH)
        // Cần 4 cột cho Label và 4 cột cho Input => GridLayout(2, 4)
        JPanel formPanel = new JPanel(new GridLayout(2, 4, 10, 10)); 
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin Sản phẩm"));

        tfProductId = new JTextField(10);
        tfProductId.setEditable(false);
        tfProductName = new JTextField(10);
        tfProductPrice = new JTextField(10);
        tfProductImage = new JTextField(10); // Trường input mới

        // Hàng 1: Label
        formPanel.add(new JLabel("Mã SP:"));
        formPanel.add(new JLabel("Tên SP:"));
        formPanel.add(new JLabel("Giá:"));
        formPanel.add(new JLabel("Đường dẫn ảnh:")); // Label mới

        // Hàng 2: Input Fields
        formPanel.add(tfProductId);
        formPanel.add(tfProductName);
        formPanel.add(tfProductPrice);
        formPanel.add(tfProductImage); // Input mới

        // Nút Thao tác (SOUTH của NORTH Panel)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnProductAdd = new JButton("Thêm");
        btnProductUpdate = new JButton("Sửa");
        btnProductDelete = new JButton("Xóa");
        btnProductClear = new JButton("Làm mới");

        buttonPanel.add(btnProductAdd);
        buttonPanel.add(btnProductUpdate);
        buttonPanel.add(btnProductDelete);
        buttonPanel.add(btnProductClear);

        // Đưa formPanel và buttonPanel vào một Wrapper để đặt vị trí tốt hơn
        JPanel northWrapper = new JPanel(new BorderLayout());
        northWrapper.add(formPanel, BorderLayout.NORTH);
        northWrapper.add(buttonPanel, BorderLayout.CENTER); 
        
        add(northWrapper, BorderLayout.NORTH);

        // 2. Table Panel (CENTER)
        // Thêm cột "Đường dẫn ảnh"
        productTableModel = new DefaultTableModel(new Object[] { "Mã SP", "Tên SP", "Giá", "Đường dẫn ảnh" }, 0) {
            // Vô hiệu hóa chỉnh sửa trực tiếp trên bảng
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        
        productTable = new JTable(productTableModel);
        productTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        // Gắn listener để điền dữ liệu khi chọn hàng
        productTable.getSelectionModel().addListSelectionListener(e -> fillFields()); 

        JScrollPane scrollPane = new JScrollPane(productTable);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    // Phương thức gắn Controller
    public void addController(ActionListener listener) {
        btnProductAdd.addActionListener(listener);
        btnProductUpdate.addActionListener(listener);
        btnProductDelete.addActionListener(listener);
        btnProductClear.addActionListener(listener);

        btnProductAdd.setActionCommand("PRODUCT_ADD");
        btnProductUpdate.setActionCommand("PRODUCT_UPDATE");
        btnProductDelete.setActionCommand("PRODUCT_DELETE");
        btnProductClear.setActionCommand("PRODUCT_CLEAR");
    }

    // Phương thức hỗ trợ fill dữ liệu lên form
    private void fillFields() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow != -1 ) {
            // Cột 0: Mã SP, Cột 1: Tên SP, Cột 2: Giá, Cột 3: Đường dẫn ảnh
            tfProductId.setText(productTableModel.getValueAt(selectedRow, 0).toString());
            tfProductName.setText(productTableModel.getValueAt(selectedRow, 1).toString());
            tfProductPrice.setText(productTableModel.getValueAt(selectedRow, 2).toString());
            tfProductImage.setText(productTableModel.getValueAt(selectedRow, 3).toString()); // Cập nhật trường ảnh
        }
    }

    // Phương thức cập nhật bảng từ Controller
    public void updateProductList(List<Product> products) {
        productTableModel.setRowCount(0);
        for (Product product : products) {
            // Cần Product Model có getMaSP(), getTenSP(), getGia(), getImageName()
            productTableModel.addRow(new Object[] { 
                product.getMaSP(), 
                product.getTenSP(), 
                product.getGia(), 
                product.getImageName() // Thêm dữ liệu ảnh
            });
        }
    }

    // Getters cho Controller (thêm Getters cho ảnh)
    public String getProductCodeInput() { return tfProductId.getText(); }
    public String getProductNameInput() { return tfProductName.getText(); }
    public String getProductPriceInput() { return tfProductPrice.getText(); }
    public String getProductImageInput() { return tfProductImage.getText(); } // Getter mới
    
    public void clearFields() {
        tfProductId.setText("");
        tfProductName.setText("");
        tfProductPrice.setText("");
        tfProductImage.setText(""); // Xóa trường ảnh
    }
}