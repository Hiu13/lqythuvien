package model;

/**
 * Class ánh xạ bảng tb_nguoimuon trong CSDL.
 * Đại diện cho người mượn (sinh viên, giáo viên...).
 */
public class Borrower {
    private String maNguoiMuon;
    private String tenNguoiMuon;
    private String DiaChi;
    private String sdt;
    private String Gmail;

    public Borrower() {}

    public Borrower(String maNguoiMuon, String tenNguoiMuon, String DiaChi, String sdt, String Gmail) {
        this.maNguoiMuon = maNguoiMuon;
        this.tenNguoiMuon = tenNguoiMuon;
        this.DiaChi = DiaChi;
        this.sdt = sdt;
        this.Gmail = Gmail;

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
        return DiaChi;
    }

    public void setDiaChi(String diaChi) {
        this.DiaChi = diaChi;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }
    
    public String getGmail() {
        return Gmail;
    }

    public void setGmail(String Gmail) {
        this.Gmail = Gmail;
    }

    @Override
    public String toString() {
        return "Borrower{" +
                "maNguoiMuon='" + maNguoiMuon + '\'' +
                ", tenNguoiMuon='" + tenNguoiMuon + '\'' +
                ", diaChi='" + DiaChi + '\'' +
                ", sdt='" + sdt + '\'' +
                ", Gmail='" + Gmail + '\'' +
                '}';
    }
}
