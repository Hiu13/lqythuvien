package model;

public class Book {
    private String maSach;
    private String tenSach;
    private String trangThai;
    private String anhSach;
    private String tacGia;
    private int soLuong;

    public Book() {
    }

    public Book(String maSach, String tenSach, String trangThai, String anhSach, String tacGia, int soLuong) {
        this.maSach = maSach;
        this.tenSach = tenSach;
        this.trangThai = trangThai;
        this.anhSach = anhSach;
        this.tacGia = tacGia;
        this.soLuong = soLuong;
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

    public String getAnhSach() {
        return anhSach;
    }

    public void setAnhSach(String anhSach) {
        this.anhSach = anhSach;
    }

    public String getTacGia() {
        return tacGia;
    }

    public void setTacGia(String tacGia) {
        this.tacGia = tacGia;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
}
