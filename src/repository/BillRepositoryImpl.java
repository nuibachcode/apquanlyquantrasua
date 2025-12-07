package repository;

import model.Bill;
import model.Product; // Cần thiết
import model.SelectedProduct; // Cần thiết
import repository.IRepository.IBillRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional; 
import java.time.LocalDate; // Cần thiết cho ngày

public class BillRepositoryImpl implements IBillRepository {
    private static final String FILE_PATH = "bills.txt";
    private List<Bill> bills = new ArrayList<>();

    public BillRepositoryImpl() {
        loadBillsFromFile();
        initializeSampleBills();
    }

    // (loadBillsFromFile và saveBillsToFile giữ nguyên)
    // ...
    private void loadBillsFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            Object obj = ois.readObject();

            if (obj instanceof List<?>) {
                List<?> list = (List<?>) obj;
                if (!list.isEmpty() && list.get(0) instanceof Bill) {
                    bills = new ArrayList<>();
                    for (Object item : list) {
                        bills.add((Bill) item);
                    }
                    System.out.println("danh sach hoa don da duoc doc tu file thanh cong.");
                } else {
                    throw new IOException("du lieu trong file khong phai List<Bill>");
                }
            } else {
                throw new IOException("du lieu trong file khong phai la List<?>");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Chưa Có Hóa Đơn Nào");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveBillsToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(bills);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // ...
    

    private void initializeSampleBills() {
        if (bills.isEmpty()) {
            // Khởi tạo các Sản phẩm mẫu để dùng trong chi tiết hóa đơn
            Product productA = new Product(101, "Trà sữa Trân Châu", 45000.0);
            Product productB = new Product(102, "Trà sữa Khoai Môn", 50000.0);

            // Chi tiết đơn hàng
            List<SelectedProduct> details1 = Arrays.asList(
                new SelectedProduct(productA, 2),
                new SelectedProduct(productB, 1)
            );
            List<SelectedProduct> details2 = Arrays.asList(
                new SelectedProduct(productB, 3)
            );
            
            // Lấy ngày hiện tại
            String today = LocalDate.now().toString();

            // SỬ DỤNG CONSTRUCTOR MỚI (8 tham số)
            // (int id, String ten, String sdt, String email, String diaChi, String ngayDat, double tongTien, List<SelectedProduct> selectedProducts)
            
            // Bill 1: Hoàn thành
            Bill bill1 = new Bill(1, "Nguyễn Văn A", "0901234567", "a.nguyen@gmail.com", 
                                  "123 Đường Nguyễn Huệ", "2024-12-01", 140000.0, details1); // (45k*2 + 50k*1 = 140k)
            bill1.setIsCompleted(true); 
            bills.add(bill1);
            
            // Bill 2: Chưa hoàn thành
            Bill bill2 = new Bill(2, "Trần Thị B", "0918765432", "b.tran@gmail.com", 
                                  "45 Lê Lợi, Quận 1", "2024-12-02", 150000.0, details2); // (50k * 3 = 150k)
            bill2.setIsCompleted(false); 
            bills.add(bill2);
            
            // Bill 3: Mẫu đơn giản
            bills.add(new Bill(3, "Lê Văn C", "0900112233", "c.le@gmail.com", "78 Phạm Văn Đồng", 
                               "2024-12-03", 300000.0, new ArrayList<>())); 

            // Cập nhật các Bill mẫu khác nếu cần thiết...
            
            saveBillsToFile();
        }
    }

    // (Các phương thức khác giữ nguyên)
    
    @Override
    public Bill save(Bill bill) {
    	int newId = generateNewId();
    	bill.setMaHD(newId);
        bills.add(bill);
        saveBillsToFile();
        return bill;
    }

    @Override
    public List<Bill> findAll() {
        return new ArrayList<>(bills);
    }

    @Override
    public void clear() {
        bills.clear();
        saveBillsToFile();
    }
    
    @Override
    public Bill findById(int id) {
        Optional<Bill> foundBill = bills.stream()
                .filter(bill -> bill.getMaHD() == id)
                .findFirst();
        return foundBill.orElse(null);
    }

    @Override
    public Bill update(Bill updatedBill) {
        for (int i = 0; i < bills.size(); i++) {
            Bill existingBill = bills.get(i);
            if (existingBill.getMaHD() == updatedBill.getMaHD()) {
                bills.set(i, updatedBill);
                saveBillsToFile(); 
                return updatedBill;
            }
        }
        return null; 
    }
    
    private int generateNewId() {
		int maxId = bills.stream().mapToInt(Bill::getMaHD)
				.max()
				.orElse(0);
		return maxId + 1;
	}
}