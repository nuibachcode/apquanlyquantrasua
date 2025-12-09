package repository;

import model.User;
import repository.IRepository.IUserRepository;
import utils.SecurityUtils; // Import tiện ích bảo mật

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements IUserRepository {
    private static final String FILE_PATH = "users.txt";
    private List<User> users = new ArrayList<>();
    
    public UserRepositoryImpl() {
        loadUsersFromFile();
        // Nếu file rỗng thì tạo mẫu (thường Seeder đã làm việc này rồi)
        if (users.isEmpty()) {
            // initializeUsers(); 
            // saveUsersToFile();
        }
    }

    // --- FILE I/O ---
    private void saveUsersToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
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

    // --- CRUD ---
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
            if (users.get(i).getMaNV() == user.getMaNV()) {
                users.set(i, user);
                saveUsersToFile();
                return user;
            }
        }
        return null;
    }

    @Override
    public void delete(User user) {
        users.removeIf(u -> u.getMaNV() == user.getMaNV());
        saveUsersToFile();
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users);
    }

    @Override
    public User findById(int id) {
        return users.stream().filter(u -> u.getMaNV() == id).findFirst().orElse(null);
    }
    
    @Override
    public void clear() {
        users.clear();
        saveUsersToFile();
    }

    // --- LOGIC ĐĂNG NHẬP & BẢO MẬT ---

    @Override
    public User findByEmailAndPassword(String email, String password) {
        // Lưu ý: password truyền vào ở đây phải là chuỗi ĐÃ MÃ HÓA từ Controller
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

    // [MỚI] Kiểm tra email tồn tại (cho ForgotPasswordController)
    public boolean checkEmailExists(String email) {
        return findByEmail(email) != null;
    }

    // [MỚI] Cập nhật mật khẩu mới (Tự động mã hóa trước khi lưu)
    public void updatePassword(String email, String newRawPassword) {
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                // Mã hóa
                String hashedPassword = SecurityUtils.hashPassword(newRawPassword);
                user.setMatKhau(hashedPassword);
                saveUsersToFile();
                return;
            }
        }
    }

    private int generateNewId() {
        int maxId = users.stream().mapToInt(User::getMaNV).max().orElse(0);
        return maxId + 1;
    }
}