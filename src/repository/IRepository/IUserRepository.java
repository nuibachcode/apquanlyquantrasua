package repository.IRepository;
import model.User;
import java.util.List;

public interface IUserRepository {
    public User save(User user);
    public User findByEmailAndPassword(String email, String password);
    public User findByEmail(String email);
    public User update(User user);
    public List<User> findAll();
    public void clear();
    public void delete(User user);
    public User findById(int id); // <--- THÊM DÒNG NÀY
}