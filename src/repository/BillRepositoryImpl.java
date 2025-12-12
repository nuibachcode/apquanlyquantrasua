package repository;

import model.Bill;
import repository.IRepository.IBillRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BillRepositoryImpl implements IBillRepository {
    private static final String FILE_PATH = "bills.txt";
    private List<Bill> bills = new ArrayList<>();

    public BillRepositoryImpl() {
        loadBillsFromFile();
        // initializeSampleBills(); // Chỉ bật dòng này 1 lần đầu để tạo dữ liệu mẫu, sau đó comment lại để tránh duplicate
    }

    private void loadBillsFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists() || file.length() == 0) {
            bills = new ArrayList<>();
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            Object obj = ois.readObject();
            if (obj instanceof List<?>) {
                bills = (List<Bill>) obj;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            bills = new ArrayList<>();
        }
    }

    private void saveBillsToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(bills);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
    
    // --- PHƯƠNG THỨC MỚI: CẬP NHẬT TRẠNG THÁI THANH TOÁN ---
    public void updatePaymentStatus(int billId) {
        for (Bill b : bills) {
            if (b.getMaHD() == billId) {
                b.setIsCompleted(true); // Đánh dấu đã thanh toán
                saveBillsToFile();      // Lưu ngay xuống file để Admin thấy
                System.out.println("Đã cập nhật trạng thái thanh toán cho đơn: " + billId);
                return;
            }
        }
    }

    @Override
    public void clear() {
        bills.clear();
        saveBillsToFile();
    }

    @Override
    public Bill findById(int id) {
        return bills.stream().filter(b -> b.getMaHD() == id).findFirst().orElse(null);
    }

    @Override
    public Bill update(Bill updatedBill) {
        for (int i = 0; i < bills.size(); i++) {
            if (bills.get(i).getMaHD() == updatedBill.getMaHD()) {
                bills.set(i, updatedBill);
                saveBillsToFile();
                return updatedBill;
            }
        }
        return null;
    }
    @Override
    public void delete(int id) {
        // Tìm và xóa hóa đơn dựa trên MaHD
        boolean removed = bills.removeIf(bill -> bill.getMaHD() == id);

        if (removed) {
            // Chỉ lưu lại vào file nếu có hóa đơn được xóa
            saveBillsToFile();
            System.out.println("DEBUG: Đã xóa Hóa đơn có ID: " + id + " và lưu lại vào file.");
        } else {
            System.out.println("DEBUG: Không tìm thấy Hóa đơn có ID: " + id + " để xóa.");
            // Tùy chọn: Bạn có thể ném một ngoại lệ ở đây nếu muốn thông báo lỗi cụ thể
        }
    }
    private int generateNewId() {
        return bills.stream().mapToInt(Bill::getMaHD).max().orElse(0) + 1;
    }
}