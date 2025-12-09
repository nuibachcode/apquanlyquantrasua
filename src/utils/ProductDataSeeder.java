package utils; // Hoặc package repository tùy cấu trúc của bạn

import model.Product;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProductDataSeeder {

    // Tên file phải khớp với biến FILE_PATH trong ProductRepositoryImpl
    private static final String FILE_PATH = "products.dat";

    public static void main(String[] args) {
        System.out.println("Đang bắt đầu tạo 50 sản phẩm mẫu...");
        List<Product> products = generateProducts(50);
        saveProductsToFile(products);
        System.out.println("Đã tạo xong! File " + FILE_PATH + " đã được cập nhật.");
        System.out.println("Vui lòng khởi động lại ứng dụng để thấy dữ liệu mới.");
    }

    private static List<Product> generateProducts(int count) {
        List<Product> list = new ArrayList<>();
        Random rand = new Random();

        // 1. Dữ liệu mẫu để ghép tên sản phẩm
        String[] loais = {"Trà sữa", "Hồng trà", "Lục trà", "Sữa tươi", "Trà trái cây", "Sinh tố", "Đá xay", "Macchiato"};
        String[] huongVis = {"Truyền thống", "Khoai môn", "Matcha", "Socola", "Dâu tây", "Xoài", "Đào", "Việt quất", 
                             "Bạc hà", "Hạt dẻ", "Sầu riêng", "Oreo", "Caramel", "Đường đen"};
        String[] toppings = {"", "Trân châu trắng", "Trân châu đen", "Full topping", "Kem Cheese", "Thạch dừa", "Pudding trứng"};

        for (int i = 1; i <= count; i++) {
            // Tạo tên ngẫu nhiên: Loại + Hương vị + Topping (nếu có)
            String tenSp = loais[rand.nextInt(loais.length)] + " " + huongVis[rand.nextInt(huongVis.length)];
            String topping = toppings[rand.nextInt(toppings.length)];
            
            if (!topping.isEmpty()) {
                tenSp += " " + topping;
            }

            // Tạo giá ngẫu nhiên: Từ 20.000 đến 70.000 (làm tròn hàng nghìn)
            // 20 + random(0-50) -> 20 -> 70. Nhân 1000.
            double gia = (20 + rand.nextInt(51)) * 1000.0;

            // Tạo ảnh ngẫu nhiên từ picture1.png đến picture15.png
            int picIndex = rand.nextInt(15) + 1;
            String imagePath = "picture" + picIndex + ".png";

            // Tạo đối tượng Product
            // Constructor: (int id, String tenSP, double donGia, String hinhAnh)
            Product product = new Product(i, tenSp, gia, imagePath);
            list.add(product);
        }

        return list;
    }

    private static void saveProductsToFile(List<Product> products) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(products);
            System.out.println("Ghi file thành công: " + products.size() + " sản phẩm.");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Lỗi khi ghi file: " + e.getMessage());
        }
    }
}