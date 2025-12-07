package repository;

import model.Product;
import repository.IRepository.IProductRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductRepositoryImpl implements IProductRepository {
    private static final String FILE_PATH = "products.dat";
    private List<Product> products = new ArrayList<>();

    public ProductRepositoryImpl() {
        loadProductsFromFile();
        if (products.isEmpty()) {
            initializeProducts();
            saveProductsToFile();
        }
    }

    // ================== Load & Save ==================
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
                products.clear();
                for (Object item : list) {
                    if (item instanceof Product) {
                        products.add((Product) item);
                    }
                }
                System.out.println("Danh sach san pham da duoc doc tu file thanh cong.");
            } else {
                System.out.println("du lieu trong file khong dung dinh dang.");
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveProductsToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(products);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ================== Khởi tạo danh sách mặc định ==================
    public void initializeProducts() {
        products.add(new Product(1, "Trà sữa trân châu trắng", 45000, "picture1.png"));
        products.add(new Product(2, "Trà sữa Oreo Cake Cream", 50000, "picture2.png"));
        products.add(new Product(3, "Trà sữa trân châu đường đen", 40000, "picture3.png"));
        products.add(new Product(4, "Trà sữa kem cheese", 45000, "picture4.png"));
        products.add(new Product(5, "Trà sữa khoai môn", 50000, "picture5.png"));
        products.add(new Product(6, "Trà sữa matcha đậu đỏ", 55000, "picture6.png"));
        products.add(new Product(7, "Trà sữa Oreo Chocolate Cream", 60000, "picture7.png"));
        products.add(new Product(8, "Trà sữa Pudding đậu đỏ", 70000, "picture8.png"));
        products.add(new Product(9, "Trà sữa sương sáo", 35000, "picture9.png"));
        products.add(new Product(10, "Trà sữa Earl Grey", 40000, "picture10.png"));
        products.add(new Product(11, "Trà sữa Caramel", 35000, "picture11.png"));
        products.add(new Product(12, "Trà sữa Cà phê", 30000, "picture12.png"));
        products.add(new Product(13, "Lục trà sữa", 35000, "picture13.png"));
        products.add(new Product(14, "Trà Sữa Socola", 40000, "picture14.png"));
        products.add(new Product(15, "Trà sữa cheese milk foam", 40000, "picture15.png"));
    }

    // ================== Các phương thức Repository (CRUD) ==================
    
    @Override // Dùng cho Thêm mới (luôn tạo ID mới)
    public Product save(Product product) {
        int newId = generateNewId();
        product.setMaSP(newId);
        products.add(product);
        saveProductsToFile();
        return product;
    }
    
    // PHƯƠNG THỨC MỚI: Dùng cho Sửa (Cập nhật)
    public void update(Product updatedProduct) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getMaSP() == updatedProduct.getMaSP()) {
                // Thay thế sản phẩm cũ bằng sản phẩm mới đã cập nhật
                products.set(i, updatedProduct);
                saveProductsToFile(); // Lưu lại toàn bộ danh sách sau khi sửa
                return;
            }
        }
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(products);
    }
    
    @Override
    public Product findById(int id) {
        for (Product product : products) {
            if (product.getMaSP() == id) {
                return product;
            }
        }
        return null;
    }
    
    // ... (findByName, delete, clear giữ nguyên)
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