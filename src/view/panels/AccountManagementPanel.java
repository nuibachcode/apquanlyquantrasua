package view.panels;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.User;
import controller.user.UserManagementController;
import java.awt.event.ActionListener;
import javax.swing.event.ListSelectionListener;
import java.util.List;


public class AccountManagementPanel extends JPanel {
    private JTextField tfUserId;
    private JComboBox<String> cmbUserRole;
    private JTable userTable;
    private DefaultTableModel userTableModel;
    // Đã loại bỏ btnUserView và btnUserClear
    private JButton btnUserRoleChange, btnUserDelete; 

    public AccountManagementPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. Form và Button Panel (NORTH)
        JPanel formPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Thao tác Tài khoản"));

        tfUserId = new JTextField(10);
        tfUserId.setEditable(false);
        cmbUserRole = new JComboBox<>(new String[]{"USER","ADMIN"});

        // Hàng 1: Label
        formPanel.add(new JLabel("ID tài khoản:"));
        formPanel.add(new JLabel("Role:"));
        formPanel.add(new JLabel("")); // Giữ lại ô trống cho GridLayout

        // Hàng 2: Input Fields
        formPanel.add(tfUserId);
        formPanel.add(cmbUserRole);

        // Nút Thao tác (Chỉ còn Đổi Role và Xóa)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        btnUserRoleChange = new JButton("Đổi Role");
        btnUserDelete = new JButton("Xóa");
        
        buttonPanel.add(btnUserRoleChange);
        buttonPanel.add(btnUserDelete);

        formPanel.add(buttonPanel);
        add(formPanel, BorderLayout.NORTH);

        // 2. Table Panel (CENTER)
        userTableModel = new DefaultTableModel(new Object[] { "ID", "Tên", "Email", "SĐT", "Địa chỉ", "Role" }, 0) {
    @Override
    public boolean isCellEditable(int row, int column) {
        return false; // Trả về false để không cho phép chỉnh sửa bất kỳ ô nào
        }
        };
        userTable = new JTable(userTableModel);
        userTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        userTable.getSelectionModel().addListSelectionListener(e -> fillFields());
        
        JScrollPane scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    // Phương thức gắn Controller (Chỉ còn Đổi Role và Xóa)
    public void addController(ActionListener listener) {
        btnUserRoleChange.addActionListener(listener);
        btnUserDelete.addActionListener(listener);
        
        btnUserRoleChange.setActionCommand("USER_UPDATE");
        btnUserDelete.setActionCommand("USER_DELETE");
    }

    // Phương thức hỗ trợ fill dữ liệu lên form
    private void fillFields() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow != -1 ) {
            tfUserId.setText(userTableModel.getValueAt(selectedRow, 0).toString());
            String currentRole = userTableModel.getValueAt(selectedRow, 5).toString();
            cmbUserRole.setSelectedItem(currentRole);
        }
    }

    // Phương thức cập nhật bảng từ Controller
    public void updateUserList(List<User> users) {
        userTableModel.setRowCount(0);
        for (User user : users) {
            userTableModel.addRow(
                new Object[] {
                    user.getMaNV(), user.getTen(), user.getEmail(), user.getSdt(),
                    user.getDiaChi(), user.getRole()
                });
        }
    }

    // Getters cho Controller
    public String getUserIdInput() { return tfUserId.getText(); }
    public String getUserRoleInput() { return cmbUserRole.getSelectedItem().toString(); }
    
    // Giữ lại hàm clearFields để gọi sau khi update/delete thành công
    public void clearFields() {
        tfUserId.setText("");
        cmbUserRole.setSelectedIndex(0);
    }
}