package Model;

public class LopHoc {

	private String maLH;
	private int maMH;
	private int namhoc;
	
	public LopHoc(String maLH, int maMH, int namhoc) {
		super();
		this.maLH = maLH;
		this.maMH = maMH;
		this.namhoc = namhoc;
	}

	public String getMaLH() {
		return maLH;
	}

	public void setMaLH(String maLH) {
		this.maLH = maLH;
	}

	public int getMaMH() {
		return maMH;
	}

	public void setMaMH(int maMH) {
		this.maMH = maMH;
	}

	public int getNamhoc() {
		return namhoc;
	}

	public void setNamhoc(int namhoc) {
		this.namhoc = namhoc;
	}

	@Override
	public String toString() {
		return "LopHoc [maLH=" + maLH + ", maMH=" + maMH + ", namhoc=" + namhoc + "]";
	}
	
}
