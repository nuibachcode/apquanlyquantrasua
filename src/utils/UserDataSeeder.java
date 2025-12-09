package utils; // Hoặc để chung package repository nếu bạn muốn

import model.User;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UserDataSeeder {

    private static final String FILE_PATH = "users.txt";

    public static void main(String[] args) {
        System.out.println("Đang bắt đầu tạo danh sách 50 người dùng...");
        List<User> users = generateUsers(50);
        saveUsersToFile(users);
        System.out.println("Đã tạo xong! File " + FILE_PATH + " đã có dữ liệu.");
        System.out.println("Tài khoản Admin mặc định: nhung@gmail.com / nhungxinhgai");
    }

    private static List<User> generateUsers(int totalCount) {
        List<User> list = new ArrayList<>();
        Random rand = new Random();

        // 1. Thêm các Admin cố định (Để bạn dễ test)
        // Constructor: (id, name, email, phone, address, password, role)
        list.add(new User(1, "Nguyễn Thị Tuyết Nhung", "nhung@gmail.com", "0987654321", "Hà Nội", "nhungxinhgai", "ADMIN"));
        list.add(new User(2, "Bạch Sỹ Núi", "nuiadmin@gmail.com", "0819617768", "Hà Nội", "123", "ADMIN"));
        list.add(new User(3, "Bạch Sỹ Núi", "nuiuser@gmail.com", "0819617768", "Hà Nội", "123", "USER"));

        // 2. Dữ liệu mẫu để random cho các User còn lại
        String[] hos = {"Nguyễn", "Trần", "Lê", "Phạm", "Hoàng", "Huỳnh", "Phan", "Vũ", "Võ", "Đặng", "Bùi", "Đỗ"};
        String[] dems = {"Văn", "Thị", "Hữu", "Minh", "Ngọc", "Thanh", "Đức", "Thùy", "Gia", "Bảo", "Quang", "Mạnh"};
        String[] tens = {"An", "Bình", "Cường", "Dũng", "Giang", "Hương", "Khánh", "Lan", "Nam", "Tâm", "Vy", "Tuấn", "Hùng", "Hải"};
        String[] cities = {"Hà Nội", "TP.HCM", "Đà Nẵng", "Cần Thơ", "Hải Phòng", "Bình Dương", "Đồng Nai"};

        // 3. Tạo random users (bắt đầu từ ID 3 vì 1,2 đã dùng)
        for (int i = 4; i <= totalCount; i++) {
            // Tạo tên
            String name = hos[rand.nextInt(hos.length)] + " " + dems[rand.nextInt(dems.length)] + " " + tens[rand.nextInt(tens.length)];
            
            // Tạo email (ví dụ: user3@gmail.com, user4@gmail.com...)
            String email = "user" + i + "@gmail.com";
            
            // Tạo số điện thoại (bảo đảm 10 số, bắt đầu bằng 09)
            // 10000000 -> 99999999 (8 chữ số)
            String phone = "09" + (rand.nextInt(90000000) + 10000000);
            
            // Tạo địa chỉ
            String address = "Số " + (rand.nextInt(100) + 1) + ", " + cities[rand.nextInt(cities.length)];
            
            // Mật khẩu mặc định là 123
            String password = "123";
            
            // Vai trò (90% là USER, 10% là ADMIN thêm)
            String role = (rand.nextInt(10) == 0) ? "ADMIN" : "USER";

            User u = new User(i, name, email, phone, address, password, role);
            list.add(u);
        }

        return list;
    }

    private static void saveUsersToFile(List<User> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(users);
            System.out.println("Ghi file thành công với " + users.size() + " tài khoản.");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Lỗi khi ghi file: " + e.getMessage());
        }
    }
}