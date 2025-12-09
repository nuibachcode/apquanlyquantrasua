package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityUtils {

    // Hàm băm mật khẩu bằng thuật toán MD5
    public static String hashPassword(String originalPassword) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(originalPassword.getBytes());
            byte[] bytes = md.digest();
            
            // Chuyển mảng byte sang chuỗi Hex (ký tự đọc được)
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // Test thử
    public static void main(String[] args) {
        System.out.println(hashPassword("123")); 
        // Kết quả sẽ ra chuỗi dài ngoằng: 202cb962ac59075b964b07152d234b70
    }
}