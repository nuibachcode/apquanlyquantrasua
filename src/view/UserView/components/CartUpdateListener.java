package view.UserView.components;

/**
 * Interface được dùng để thông báo cho Controller biết khi giỏ hàng (cart)
 * bị thay đổi (thêm, xóa, hoặc sửa số lượng) bên trong CartDetailsDialog.
 */
public interface CartUpdateListener {
    /**
     * Được gọi khi tổng giá trị giỏ hàng cần được cập nhật trên giao diện chính.
     */
    void onCartUpdated();
}