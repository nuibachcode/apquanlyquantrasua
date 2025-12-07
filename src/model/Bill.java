package model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

// Cần import List và SelectedProduct
// Giả định model.SelectedProduct tồn tại
// Nếu bạn chưa có SelectedProduct, bạn cần tạo nó.

public class Bill implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
    
	private int maHD;
	private String ten;
    // Đã đổi kiểu dữ liệu từ int sang String
	private String sdt; 
	private String email;
    // Thuộc tính mới
        private String diaChi; 
	private String ngayDat;
	private double tongTien;
    private boolean isCompleted;
    // Thuộc tính mới để lưu chi tiết đơn hàng
    private List<SelectedProduct> selectedProducts; 

	public Bill() {
	}

    // =======================================================
    // CONSTRUCTOR MỚI: DÙNG TRONG ProductController.generateBill()
    // =======================================================
    public Bill(int maHD, String ten, String sdt, String email, String diaChi, String ngayDat, double tongTien, List<SelectedProduct> selectedProducts) {
        this.maHD = maHD;
        this.ten = ten;
        this.sdt = sdt; // Sử dụng String
        this.email = email;
        this.diaChi = diaChi; // Địa chỉ mới
        this.ngayDat = ngayDat;
        this.tongTien = tongTien;
        this.selectedProducts = selectedProducts; // Chi tiết đơn hàng mới
        this.isCompleted = false; // Mặc định là chưa hoàn thành
    }


    // =======================================================
    // Constructor cũ (Tùy chọn: Có thể giữ lại hoặc xóa)
    // =======================================================
	public Bill(int maHD, String ten, int sDT, String email, String ngayDat, double tongTien) {
		this.maHD = maHD;
		this.ten = ten;
        // Chú ý: Constructor cũ này vẫn dùng int sDT, nên nếu dùng
        // phải đảm bảo code không gây NumberFormatException.
		// Tốt nhất nên chuyển constructor này sang dùng String sdt
        // Hoặc xóa đi nếu không dùng.
        // Để tránh lỗi, tôi giữ nguyên tên biến trong constructor cũ
        // Nhưng khuyến khích dùng constructor mới bên trên.
		this.email = email;
		this.ngayDat = ngayDat;
		this.tongTien = tongTien;
	}
	
    // =======================================================
    // GETTERS
    // =======================================================
	public int getMaHD() { return maHD; }
	public String getTen() { return ten; }
    // Getters đã đổi sang String
	public String getSdt() { return sdt; } 
	public String getEmail() { return email; }
    // Getter mới
    public String getDiaChi() { return diaChi; }
	public String getNgayDat() { return ngayDat; }
	public double getTongTien() { return tongTien; }
 	public boolean getIsCompleted() { return isCompleted; }
    // Getter mới
    public List<SelectedProduct> getSelectedProducts() { return selectedProducts; }


    // =======================================================
    // SETTERS
    // =======================================================
	public void setMaHD(int maHD) { this.maHD = maHD; }
	public void setTen(String ten) { this.ten = ten; }
    // Setter đã đổi sang String
	public void setSdt(String sdt) { this.sdt = sdt; }
	public void setEmail(String email) { this.email = email; }
    // Setter mới
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }
	public void setNgayDat(String ngayDat) { this.ngayDat = ngayDat; }
	public void setTongTien(double tongTien) { this.tongTien = tongTien; }
 	public void setIsCompleted(boolean isCompleted) { this.isCompleted = isCompleted; }
    // Setter mới
    public void setSelectedProducts(List<SelectedProduct> selectedProducts) { this.selectedProducts = selectedProducts; }


    // =======================================================
    // TOSTRING, HASHCODE, EQUALS
    // =======================================================
	@Override
	public String toString() {
		return "Bill [maHD=" + maHD + ", ten=" + ten + ", SDT=" + sdt + ", email=" + email + ", diaChi=" + diaChi 
				+ ", ngayDat=" + ngayDat + ", tongTien=" + tongTien + "]";
	}

	@Override
	public int hashCode() {
		// Cập nhật hashCode để bao gồm các trường mới (diaChi, selectedProducts)
		return Objects.hash(sdt, email, maHD, ngayDat, ten, tongTien, diaChi, selectedProducts);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Bill other = (Bill) obj;
		return Objects.equals(sdt, other.sdt) && Objects.equals(email, other.email) && maHD == other.maHD
				&& Objects.equals(diaChi, other.diaChi)
                && Objects.equals(ngayDat, other.ngayDat) && Objects.equals(ten, other.ten)
				&& Double.doubleToLongBits(tongTien) == Double.doubleToLongBits(other.tongTien)
                && Objects.equals(selectedProducts, other.selectedProducts);
	}
}