package model;

import java.io.Serializable;

public class Product implements Serializable {
    
	private static final long serialVersionUID = 1L;
    
	private int maSP;
	private String tenSP;
	private double gia;
    private String imageName; // Thuộc tính đã được khai báo

	public Product() {
	}
    
    // Constructor đầy đủ tham số (đã thêm imageName)
	public Product(int maSP, String tenSP, double gia, String imageName) {
		this.maSP = maSP;
		this.tenSP = tenSP;
		this.gia = gia;
        this.imageName = imageName; // Khởi tạo trường imageName
	}
    
    // Constructor 3 tham số (giữ lại nếu cần)
	public Product(int maSP, String tenSP, double gia) {
		this.maSP = maSP;
		this.tenSP = tenSP;
		this.gia = gia;
        // imageName sẽ là null hoặc chuỗi rỗng
	}
    
    // Constructor chỉ có tên (giữ lại nếu cần)
	public Product(String tenSP) {
		this.tenSP = tenSP;
	}

    // --- Getters ---
	public int getMaSP() {
		return maSP;
	}
	public String getTenSP() {
		return tenSP;
	}
	
	public double getGia() {
		return gia;
	}

    // Getter chuẩn cho imageName
	public String getImageName() {
		return imageName;
	}
    
    // Loại bỏ getter không cần thiết (getAnh)
	
    // --- Setters ---
	public void setMaSP(int maSP) {
		this.maSP = maSP;
	}
	public void setTenSP(String tenSP) {
		this.tenSP = tenSP;
	}

	public void setGia(double gia) {
		this.gia = gia;
	}
    
    // Setter cho imageName
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

	@Override
	public String toString() {
		return "SanPham [maSP=" + maSP + ", tenSP=" + tenSP + ", gia=" + gia + ", imageName=" + imageName + "]";
	}
}