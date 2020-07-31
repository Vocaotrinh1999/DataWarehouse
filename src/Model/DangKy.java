package Model;

public class DangKy {

	private String maDK;
	private int mssv;
	private String maLH;
	private String ngayDK;

	public DangKy(String maDK, int mssv, String maLH, String ngayDK) {
		super();
		this.maDK = maDK;
		this.mssv = mssv;
		this.maLH = maLH;
		this.ngayDK = ngayDK;
	}

	public DangKy() {

	}

	public String getMaDK() {
		return maDK;
	}

	public void setMaDK(String maDK) {
		this.maDK = maDK;
	}

	public int getMssv() {
		return mssv;
	}

	public void setMssv(int mssv) {
		this.mssv = mssv;
	}

	public String getMaLH() {
		return maLH;
	}

	public void setMaLH(String maLH) {
		this.maLH = maLH;
	}

	public String getNgayDK() {
		return ngayDK;
	}

	public void setNgayDK(String ngayDK) {
		this.ngayDK = ngayDK;
	}

	@Override
	public String toString() {
		return "DangKy [maDK=" + maDK + ", mssv=" + mssv + ", maLH=" + maLH + ", ngayDK=" + ngayDK + "]";
	}

}
