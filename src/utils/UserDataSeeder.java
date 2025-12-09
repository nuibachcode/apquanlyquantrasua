package utils; 

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
        System.out.println("Đang bắt đầu tạo danh sách User với mật khẩu đã mã hóa...");
        List<User> users = generateUsers(50);
        saveUsersToFile(users);
        System.out.println("Đã tạo xong! File " + FILE_PATH + " đã có dữ liệu mới.");
        System.out.println("LƯU Ý: Mật khẩu của Admin là 'nhungxinhgai' và '123' (Đã được mã hóa trong file).");
    }

    private static List<User> generateUsers(int totalCount) {
        List<User> list = new ArrayList<>();
        Random rand = new Random();

        // 1. Thêm các Admin cố định (MÃ HÓA PASSWORD)
        // Mật khẩu thực: "nhungxinhgai" -> Lưu: Hash("nhungxinhgai")
        list.add(new User(1, "Nguyễn Thị Tuyết Nhung", "nhung@gmail.com", "0987654321", "Hà Nội", 
                SecurityUtils.hashPassword("nhungxinhgai"), "ADMIN"));
        
        list.add(new User(2, "Bạch Sỹ Núi", "nuiadmin@gmail.com", "0819617768", "Hà Nội", 
                SecurityUtils.hashPassword("123"), "ADMIN"));
        
        list.add(new User(3, "Bạch Sỹ Núi", "nuiuser@gmail.com", "0819617768", "Hà Nội", 
                SecurityUtils.hashPassword("123"), "USER"));

        // 2. Dữ liệu mẫu để random
        String[] hos = {"Nguyễn", "Trần", "Lê", "Phạm", "Hoàng", "Huỳnh"};
        String[] tens = {"An", "Bình", "Cường", "Dũng", "Giang", "Hương", "Lan", "Nam", "Tâm"};
        String[] cities = {"Hà Nội", "TP.HCM", "Đà Nẵng", "Cần Thơ"};

        // 3. Tạo random users
        for (int i = 4; i <= totalCount; i++) {
            String name = hos[rand.nextInt(hos.length)] + " " + tens[rand.nextInt(tens.length)];
            String email = "user" + i + "@gmail.com";
            String phone = "09" + (rand.nextInt(90000000) + 10000000);
            String address = "Số " + (rand.nextInt(100) + 1) + ", " + cities[rand.nextInt(cities.length)];
            
            // MÃ HÓA PASSWORD MẶC ĐỊNH LÀ "123"
            String password = SecurityUtils.hashPassword("123");
            
            String role = (rand.nextInt(10) == 0) ? "ADMIN" : "USER";

            User u = new User(i, name, email, phone, address, password, role);
            list.add(u);
        }

        return list;
    }

    private static void saveUsersToFile(List<User> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}