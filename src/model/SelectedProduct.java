package model;

import java.io.Serializable;

// THÊM implements Serializable Ở ĐÂY
public class SelectedProduct implements Serializable {
    private static final long serialVersionUID = 1L; // Đánh dấu phiên bản
    private Product product;
    private int quantity;

    public SelectedProduct(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public double getTotalPrice() { return product.getGia() * quantity; }

    public void setQuantity(int quantity) { this.quantity = quantity; }
}