package model;

import java.util.Date;

/**
 * Class ánh xạ bảng tb_phieumuon trong CSDL.
 * Đại diện cho một phiếu mượn sách.
 */
public class LoanSlip {
    private String maPhieuMuon;
    private Date ngayMuon;
    private Date hanTra;
    private String maSach;        // Khóa ngoại đến tb_sach
    private String maNguoiMuon;   // Khóa ngoại đến tb_nguoimuon
    private Date ngayTra;

    public LoanSlip() {}

    public LoanSlip(String maPhieuMuon, Date ngayMuon, Date hanTra, String maSach, String maNguoiMuon, Date ngayTra) {
        this.maPhieuMuon = maPhieuMuon;
        this.ngayMuon = ngayMuon;
        this.hanTra = hanTra;
        this.maSach = maSach;
        this.maNguoiMuon = maNguoiMuon;
        this.ngayTra = ngayTra;
    }

    public String getMaPhieuMuon() {
        return maPhieuMuon;
    }

    public void setMaPhieuMuon(String maPhieuMuon) {
        this.maPhieuMuon = maPhieuMuon;
    }

    public Date getNgayMuon() {
        return ngayMuon;
    }

    public void setNgayMuon(Date ngayMuon) {
        this.ngayMuon = ngayMuon;
    }

    public Date getHanTra() {
        return hanTra;
    }

    public void setHanTra(Date hanTra) {
        this.hanTra = hanTra;
    }

    public String getMaSach() {
        return maSach;
    }

    public void setMaSach(String maSach) {
        this.maSach = maSach;
    }

    public String getMaNguoiMuon() {
        return maNguoiMuon;
    }

    public void setMaNguoiMuon(String maNguoiMuon) {
        this.maNguoiMuon = maNguoiMuon;
    }

    public Date getNgayTra() {
        return ngayTra;
    }

    public void setNgayTra(Date ngayTra) {
        this.ngayTra = ngayTra;
    }

    @Override
    public String toString() {
        return "LoanSlip{" +
                "maPhieuMuon='" + maPhieuMuon + '\'' +
                ", ngayMuon=" + ngayMuon +
                ", hanTra=" + hanTra +
                ", maSach='" + maSach + '\'' +
                ", maNguoiMuon='" + maNguoiMuon + '\'' +
                ", ngayTra=" + ngayTra +
                '}';
    }
}
