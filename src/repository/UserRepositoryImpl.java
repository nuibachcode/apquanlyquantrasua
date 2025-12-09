package repository;

import model.User;
import repository.IRepository.IUserRepository;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements IUserRepository {
    private static final String FILE_PATH = "users.txt";
    private List<User> users = new ArrayList<>();
    
    public UserRepositoryImpl() {
        loadUsersFromFile();
        if (users.isEmpty()) {
            initializeUsers();
            saveUsersToFile();
        }
    }

    // --- CÁC HÀM FILE I/O (GIỮ NGUYÊN) ---
    private void saveUsersToFile() {
        File file = new File(FILE_PATH);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadUsersFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            if (obj instanceof List<?>) {
                users = (List<User>) obj;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            users = new ArrayList<>();
        }
    }

    private void initializeUsers() {
        // Tạo dữ liệu mẫu
        users.add(new User(1, "Nguyễn Thị Tuyết Nhung", "nhung@gmail.com", "0987654321", "HN", "admin123", "ADMIN"));
        users.add(new User(2, "Bạch Sỹ Núi", "n@gmail.com", "0819617768", "HN", "123", "ADMIN"));
        users.add(new User(4, "Nguyễn Văn A", "user@gmail.com", "0900000000", "HN", "123", "USER"));
        // ... (Bạn có thể thêm user mẫu khác tùy ý)
    }

    // --- CÁC HÀM CRUD CƠ BẢN ---

    @Override
    public User save(User user) {
        int newId = generateNewId();  
        user.setMaNV(newId);  
        users.add(user);
        saveUsersToFile();
        return user;
    }

    @Override
    public User update(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getMaNV() == user.getMaNV()) { // Nên so sánh theo ID (MaNV) chuẩn hơn Email
                users.set(i, user);
                saveUsersToFile();
                return user;
            }
        }
        return null;
    }

    @Override
    public void delete(User user) {
        // Xóa user dựa trên ID để chính xác nhất
        users.removeIf(u -> u.getMaNV() == user.getMaNV());
        saveUsersToFile();
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users);
    }

    @Override
    public User findById(int id) {
        for (User user : users) {
            if (user.getMaNV() == id) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void clear() {
        users.clear();
        saveUsersToFile();
    }

    // --- CÁC HÀM NGHIỆP VỤ (LOGIN, FORGOT PASSWORD) ---

    @Override
    public User findByEmailAndPassword(String email, String password) {
        for (User user : users) {
            if (user.getEmail().equals(email) && user.getMatKhau().equals(password)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public User findByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }
        return null;
    }

    // [MỚI] Hàm hỗ trợ chức năng Quên mật khẩu: Kiểm tra email có tồn tại không
    public boolean checkEmailExists(String email) {
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    // [MỚI] Hàm hỗ trợ chức năng Quên mật khẩu: Cập nhật mật khẩu mới theo Email
    public void updatePassword(String email, String newPassword) {
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                user.setMatKhau(newPassword);
                saveUsersToFile(); // Lưu ngay lập tức xuống file
                System.out.println("Đã cập nhật mật khẩu cho user: " + email);
                return;
            }
        }
    }

    // Helper: Tạo ID tự động tăng
    private int generateNewId() {
        int maxId = users.stream().mapToInt(User::getMaNV).max().orElse(0);
        return maxId + 1;
    }
}