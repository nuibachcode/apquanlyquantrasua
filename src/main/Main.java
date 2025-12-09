package main;

import javax.swing.UIManager;
import com.formdev.flatlaf.FlatLightLaf; // Import FlatLaf
import view.LoginView;
import java.awt.EventQueue;

public class Main {
    public static void main(String[] args) {
        try {
        // 1. Khởi tạo FlatLaf
        UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());

        // 2. THÊM DÒNG NÀY ĐỂ BO GÓC (Số càng to càng tròn)
        UIManager.put("Button.arc", 20); // Bo góc 20 pixel (nhìn vừa đẹp)
        // UIManager.put("Button.arc", 999); // Nếu muốn bo tròn xoe như viên thuốc
        
        // Tùy chỉnh thêm: Bo góc cho ô nhập liệu (Text Field) luôn cho đồng bộ
        UIManager.put("TextComponent.arc", 20); 

    } catch (Exception ex) {
        System.err.println("Lỗi giao diện");
    }
        // 2. Chạy ứng dụng
        EventQueue.invokeLater(() -> {
            try {
                LoginView loginView = new LoginView();
                loginView.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}