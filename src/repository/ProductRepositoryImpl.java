package repository;

import model.Product;
import repository.IRepository.IProductRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProductRepositoryImpl implements IProductRepository {
    private static final String FILE_PATH = "products.dat"; // đổi tên file để tránh nhầm text
    private List<Product> products = new ArrayList<>();

    public ProductRepositoryImpl() {
        loadProductsFromFile();
        if (products.isEmpty()) {
            initializeProducts();
            saveProductsToFile();
        }
    }

    // ================== Load sản phẩm từ file ==================
    private void loadProductsFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println("Chưa có sản phẩm nào.");
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            if (obj instanceof List<?>) {
                List<?> list = (List<?>) obj;
                products.clear(); // xóa list cũ trước khi thêm
                for (Object item : list) {
                    if (item instanceof Product) {
                        products.add((Product) item);
                    }
                }
                System.out.println("Danh sách sản phẩm đã được đọc từ file thành công.");
            } else {
                System.out.println("Dữ liệu trong file không đúng định dạng.");
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // ================== Lưu sản phẩm vào file ==================
    private void saveProductsToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(products);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ================== Khởi tạo danh sách mặc định ==================
    public void initializeProducts() {
        products.add(new Product(1, "Trà sữa trân châu trắng", 45000));
        products.add(new Product(2, "Trà sữa Oreo Cake Cream", 50000));
        products.add(new Product(3, "Trà sữa trân châu đường đen", 40000));
        products.add(new Product(4, "Trà sữa kem cheese", 45000));
        products.add(new Product(5, "Trà sữa khoai môn", 50000));
        products.add(new Product(6, "Trà sữa matcha đậu đỏ", 55000));
        products.add(new Product(7, "Trà sữa Oreo Chocolate Cream", 60000));
        products.add(new Product(8, "Trà sữa Pudding đậu đỏ", 70000));
        products.add(new Product(9, "Trà sữa sương sáo", 35000));
        products.add(new Product(10, "Trà sữa Earl Grey", 40000));
        products.add(new Product(11, "Trà sữa Caramel", 35000));
        products.add(new Product(12, "Trà sữa Cà phê", 30000));
        products.add(new Product(13, "Lục trà sữa", 35000));
        products.add(new Product(14, "Trà Sữa Socola", 40000));
        products.add(new Product(15, "Trà sữa cheese milk foam", 40000));
    }

    // ================== Các phương thức Repository ==================
    @Override
    public Product save(Product product) {
        int newId = generateNewId();
        product.setMaSP(newId);
        products.add(product);
        saveProductsToFile();
        return product;
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(products);
    }

    @Override
    public Product findByName(String name) {
        for (Product product : products) {
            if (product.getTenSP().equals(name)) {
                return product;
            }
        }
        return null;
    }
    @Override
    public Product findById(int id) {
        for (Product product : products) {
            if (product.getMaSP() == id) {
                return product;
            }
        }
        return null; // Trả về null nếu không tìm thấy sản phẩm
    }
    @Override
    public void delete(Product product) {
        products.remove(product);
        saveProductsToFile();
    }

    @Override
    public void clear() {
        products.clear();
        saveProductsToFile();
    }

    private int generateNewId() {
        int maxId = products.stream().mapToInt(Product::getMaSP).max().orElse(0);
        return maxId + 1;
    }
}
