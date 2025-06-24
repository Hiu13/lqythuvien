package model;

/**
 * Class ánh xạ bảng tb_sach trong CSDL.
 * Đại diện cho từng cuốn sách vật lý (bản sao của đầu sách).
 */
public class BookCopy {
    private String maSach;
    private String tenSach;
    private String trangThai;
    private String maDauSach;
    private String anhSach; // Thêm thuộc tính mới

    public BookCopy() {
    }

    public BookCopy(String maSach, String tenSach, String trangThai, String maDauSach, String anhSach) {
        this.maSach = maSach;
        this.tenSach = tenSach;
        this.trangThai = trangThai;
        this.maDauSach = maDauSach;
        this.anhSach = anhSach;
    }

    public String getMaSach() {
        return maSach;
    }

    public void setMaSach(String maSach) {
        this.maSach = maSach;
    }

    public String getTenSach() {
        return tenSach;
    }

    public void setTenSach(String tenSach) {
        this.tenSach = tenSach;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getMaDauSach() {
        return maDauSach;
    }

    public void setMaDauSach(String maDauSach) {
        this.maDauSach = maDauSach;
    }

    public String getAnhSach() {
        return anhSach;
    }

    public void setAnhSach(String anhSach) {
        this.anhSach = anhSach;
    }

    @Override
    public String toString() {
        return "BookCopy{" +
                "maSach='" + maSach + '\'' +
                ", tenSach='" + tenSach + '\'' +
                ", trangThai='" + trangThai + '\'' +
                ", maDauSach='" + maDauSach + '\'' +
                ", anhSach='" + anhSach + '\'' +
                '}';
    }
}
