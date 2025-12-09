package utils;

import model.Bill;
import model.Product;
import model.SelectedProduct;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class BillDataSeeder {

    private static final String FILE_PATH = "bills.txt";

    // Danh sách sản phẩm mẫu (Dùng chung)
    private static final List<Product> PRODUCTS = Arrays.asList(
            new Product(101, "Trà sữa Trân Châu", 45000.0),
            new Product(102, "Trà sữa Khoai Môn", 50000.0),
            new Product(103, "Hồng trà Macchiato", 40000.0),
            new Product(104, "Sữa tươi Trân châu đường đen", 55000.0),
            new Product(105, "Trà đào cam sả", 42000.0),
            new Product(106, "Matcha Đá xay", 59000.0)
    );

    public static void main(String[] args) {
        List<Bill> allBills = new ArrayList<>();

        System.out.println("1. Tạo 100 hóa đơn ngẫu nhiên (2024 - Hiện tại)...");
        allBills.addAll(generateRandomBills(1, 100)); // ID từ 1 đến 100

        System.out.println("2. Tạo thêm 50 hóa đơn tháng 12/2025...");
        allBills.addAll(generateDecember2025Bills(101, 50)); // ID từ 101, thêm 50 cái

        saveBillsToFile(allBills);
        System.out.println("HOÀN TẤT! Tổng cộng: " + allBills.size() + " hóa đơn đã được lưu.");
    }

    // Hàm 1: Tạo hóa đơn ngẫu nhiên thông thường
    private static List<Bill> generateRandomBills(int startId, int count) {
        // Ngày bắt đầu: 01/01/2024
        LocalDate minDate = LocalDate.of(2024, 1, 1);
        // Ngày kết thúc: Hôm nay
        LocalDate maxDate = LocalDate.now();
        
        return createBills(startId, count, minDate, maxDate);
    }

    // Hàm 2: Tạo hóa đơn chuyên biệt cho tháng 12/2025
    private static List<Bill> generateDecember2025Bills(int startId, int count) {
        // Ngày bắt đầu: 01/12/2025
        LocalDate minDate = LocalDate.of(2025, 12, 1);
        // Ngày kết thúc: 31/12/2025
        LocalDate maxDate = LocalDate.of(2025, 12, 31);

        return createBills(startId, count, minDate, maxDate);
    }

    // Hàm chung để sinh dữ liệu
    private static List<Bill> createBills(int startId, int count, LocalDate minDate, LocalDate maxDate) {
        List<Bill> list = new ArrayList<>();
        Random rand = new Random();

        // Dữ liệu giả
        String[] hos = {"Nguyễn", "Trần", "Lê", "Phạm", "Hoàng", "Huỳnh", "Phan", "Vũ", "Võ", "Đặng"};
        String[] dems = {"Văn", "Thị", "Hữu", "Minh", "Ngọc", "Thanh", "Đức", "Thùy", "Gia", "Bảo"};
        String[] tens = {"An", "Bình", "Cường", "Dũng", "Giang", "Hương", "Khánh", "Lan", "Nam", "Tâm", "Vy", "Tuấn"};
        String[] duongs = {"Nguyễn Huệ", "Lê Lợi", "Pasteur", "Hai Bà Trưng", "Phạm Văn Đồng", "Cầu Giấy", "Láng Hạ"};

        long minDay = minDate.toEpochDay();
        long maxDay = maxDate.toEpochDay();

        for (int i = 0; i < count; i++) {
            int currentId = startId + i;

            // Random thông tin khách
            String name = hos[rand.nextInt(hos.length)] + " " + dems[rand.nextInt(dems.length)] + " " + tens[rand.nextInt(tens.length)];
            String phone = "09" + (rand.nextInt(90000000) + 10000000);
            String email = "user" + currentId + "@gmail.com";
            String address = (rand.nextInt(200) + 1) + " " + duongs[rand.nextInt(duongs.length)];

            // Random ngày trong khoảng quy định
            long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay + 1); // +1 để lấy cả ngày cuối
            String date = LocalDate.ofEpochDay(randomDay).toString();

            // Random giỏ hàng
            List<SelectedProduct> cart = new ArrayList<>();
            int numberOfItems = rand.nextInt(4) + 1; // 1-4 món
            double totalMoney = 0;

            for (int j = 0; j < numberOfItems; j++) {
                Product randomProd = PRODUCTS.get(rand.nextInt(PRODUCTS.size()));
                int quantity = rand.nextInt(3) + 1;
                
                cart.add(new SelectedProduct(randomProd, quantity));
                totalMoney += randomProd.getGia() * quantity;
            }

            // Tạo Bill
            Bill bill = new Bill(currentId, name, phone, email, address, date, totalMoney, cart);
            
            // Random trạng thái (90% hoàn thành để báo cáo đẹp)
            bill.setIsCompleted(rand.nextInt(10) < 9);

            list.add(bill);
        }
        return list;
    }

    private static void saveBillsToFile(List<Bill> bills) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(bills);
            System.out.println("-> Đã ghi file " + FILE_PATH + " thành công.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}