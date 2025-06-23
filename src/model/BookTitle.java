package model;

/**
 * Class ánh xạ bảng tb_dausach trong CSDL.
 */
public class BookTitle {
    private String maDauSach;
    private String tenSach;
    private int soLuong;
    private String theLoai;
    private String tacGia;
    private String nxb;
    private int namXB;

    // Constructor
    public BookTitle() {}

    public BookTitle(String maDauSach, String tenSach, int soLuong, String theLoai, String tacGia, String nxb, int namXB) {
        this.maDauSach = maDauSach;
        this.tenSach = tenSach;
        this.soLuong = soLuong;
        this.theLoai = theLoai;
        this.tacGia = tacGia;
        this.nxb = nxb;
        this.namXB = namXB;
    }

    // Getters và Setters
    public String getMaDauSach() {
        return maDauSach;
    }

    public void setMaDauSach(String maDauSach) {
        this.maDauSach = maDauSach;
    }

    public String getTenSach() {
        return tenSach;
    }

    public void setTenSach(String tenSach) {
        this.tenSach = tenSach;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getTheLoai() {
        return theLoai;
    }

    public void setTheLoai(String theLoai) {
        this.theLoai = theLoai;
    }

    public String getTacGia() {
        return tacGia;
    }

    public void setTacGia(String tacGia) {
        this.tacGia = tacGia;
    }

    public String getNxb() {
        return nxb;
    }

    public void setNxb(String nxb) {
        this.nxb = nxb;
    }

    public int getNamXB() {
        return namXB;
    }

    public void setNamXB(int namXB) {
        this.namXB = namXB;
    }

    @Override
    public String toString() {
        return "BookTitle{" +
                "maDauSach='" + maDauSach + '\'' +
                ", tenSach='" + tenSach + '\'' +
                ", soLuong=" + soLuong +
                ", theLoai='" + theLoai + '\'' +
                ", tacGia='" + tacGia + '\'' +
                ", nxb='" + nxb + '\'' +
                ", namXB=" + namXB +
                '}';
    }
}
