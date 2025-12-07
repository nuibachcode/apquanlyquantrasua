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

    // Ghi danh sách User vào file
    private void saveUsersToFile() {
        File file = new File(FILE_PATH);

        try (OutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(users);
            System.out.println("Danh sách User đã được ghi vào file thành công.");
        } catch (IOException e) {
            System.err.println("Lỗi khi ghi file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Đọc danh sách User từ file
    private void loadUsersFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println("File không tồn tại. Trả về danh sách rỗng.");
            return; 
        }

        try (InputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            Object obj = ois.readObject();

            if (obj instanceof List<?>) {
                List<?> list = (List<?>) obj;
                if (!list.isEmpty() && list.get(0) instanceof User) {
                    users = new ArrayList<>();
                    for (Object item : list) {
                        users.add((User) item);
                    }
                    System.out.println("Danh sách User đã được đọc từ file thành công.");
                } else {
                    throw new IOException("Dữ liệu trong file không phải là List<User>");
                }
            } else {
                throw new IOException("Dữ liệu trong file không phải là List<?>");
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Lỗi khi đọc file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initializeUsers() {
    	 // Tạo tài khoản quản trị viên
        users.add(new User(1, "Nguyễn Lê Hoài Nam", "nam@gmail.com", "0966618229", "Address1", "123", "ADMIN"));
        users.add(new User(2, "Bạch Sỹ Núi", "n@gmail.com", "0819617768", "Address1", "123", "ADMIN"));

        // Tạo tài khoản người dùng với vai trò "USER"
        users.add(new User(4, "Nguyễn Văn A", "nguyenvana@gmail.com", "987654321", "Address4", "123", "USER"));
        users.add(new User(5, "Nguyễn Văn B", "nguyenvanb@gmail.com", "987654322", "Address5", "123", "USER"));
        users.add(new User(6, "Nguyễn Văn C", "nguyenvanc@gmail.com", "987654323", "Address6", "123", "USER"));
        users.add(new User(7, "Nguyễn Văn D", "nguyenvand@gmail.com", "987654324", "Address7", "123", "USER"));
        users.add(new User(8, "Nguyễn Văn E", "nguyenvane@gmail.com", "987654325", "Address8", "123", "USER"));
        users.add(new User(9, "Nguyễn Văn F", "nguyenvanf@gmail.com", "987654326", "Address9", "123", "USER"));
        users.add(new User(10, "Nguyễn Văn G", "nguyenvang@gmail.com", "987654327", "Address10", "123", "USER"));
        users.add(new User(11, "Nguyễn Văn H", "nguyenvanh@gmail.com", "987654328", "Address11", "123", "USER"));
        users.add(new User(12, "Nguyễn Văn I", "nguyenvani@gmail.com", "987654329", "Address12", "123", "USER"));
    }
    @Override
    public User save(User user) {
        int newId = generateNewId();  
        user.setMaNV(newId);  
        users.add(user);
        saveUsersToFile();
        return user;
    }

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
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public User update(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getEmail().equals(user.getEmail())) {
                users.set(i, user);
                saveUsersToFile();
                return user;
            }
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users);
    }

    @Override
    public void clear() {
        users.clear();
        saveUsersToFile();
    }
    @Override
    public User findById(int id) {
        for (User user : users) {
            if (user.getMaNV() == id) {
                return user;
            }
        }
        return null; // Trả về null nếu không tìm thấy User
    }
    @Override
    public void delete(User user) {
        users.remove(user);
        saveUsersToFile();
    }

    private int generateNewId() {
		int maxId = users.stream().mapToInt(User::getMaNV)
				.max()
				.orElse(0);
		return maxId + 1;
	}
}
