package model;

/**
 * Class ánh xạ bảng tb_nguoimuon trong CSDL.
 * Đại diện cho người mượn (sinh viên, giáo viên...).
 */
public class Borrower {
    private String maNguoiMuon;
    private String tenNguoiMuon;
    private String diaChi;
    private String gmail;
    private String sdt;

    public Borrower() {}

    public Borrower(String maNguoiMuon, String tenNguoiMuon, String diaChi, String gmail, String sdt) {
        this.maNguoiMuon = maNguoiMuon;
        this.tenNguoiMuon = tenNguoiMuon;
        this.diaChi = diaChi;
        this.gmail = gmail;
        this.sdt = sdt;
    }

    public String getMaNguoiMuon() {
        return maNguoiMuon;
    }

    public void setMaNguoiMuon(String maNguoiMuon) {
        this.maNguoiMuon = maNguoiMuon;
    }

    public String getTenNguoiMuon() {
        return tenNguoiMuon;
    }

    public void setTenNguoiMuon(String tenNguoiMuon) {
        this.tenNguoiMuon = tenNguoiMuon;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    @Override
    public String toString() {
        return "Borrower{" +
                "maNguoiMuon='" + maNguoiMuon + '\'' +
                ", tenNguoiMuon='" + tenNguoiMuon + '\'' +
                ", diaChi='" + diaChi + '\'' +
                ", gmail='" + gmail + '\'' +
                ", sdt='" + sdt + '\'' +
                '}';
    }
}
