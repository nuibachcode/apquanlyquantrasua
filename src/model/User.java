package model;

import java.io.Serializable;

public class User implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int maNV;
    private String ten;
    private String email;
    private String sdt;
    private String diaChi;
    private String matKhau;
    private String role; 

    public static int n = 1;
    
    public User() {
    }

    public User(int maNV, String ten, String email, String sdt, String diaChi, String matKhau, String role) {
        this.maNV = maNV;
        this.ten = ten;
        this.email = email;
        this.sdt = sdt;
        this.diaChi = diaChi;
        this.matKhau = matKhau;
        this.role = role;
    }

    public User(String ten, String email, String phoneNumber, String diaChi, String matKhau, String role) {
        this.ten = ten;
        this.email = email;
        this.sdt = phoneNumber;
        this.diaChi = diaChi;
        this.matKhau = matKhau;
        this.role = role;
    }

  

    public int getMaNV() {
        return maNV;
    }

    public void setMaNV(int maNV) {
        this.maNV = maNV;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "NguoiSuDung [maNV=" + maNV + ", ten=" + ten + ", email=" + email + ", sdt=" + sdt + ", diaChi=" + diaChi + ", matKhau=" + matKhau + ", role=" + role + "]";
    }
}
