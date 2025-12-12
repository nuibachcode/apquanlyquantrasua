package repository.IRepository;


import model.Bill;
import java.util.List;

public interface IBillRepository {
    public Bill save(Bill bill);
    public List<Bill> findAll();
    public void clear();
    public Bill findById(int id);
    public Bill update(Bill bill);
    public void delete(int id);
}
