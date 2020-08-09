package Model;

public class Subject {

	private int stt;
	private int msmh;
	private String tenmh;
	private int stc;
	private String khoaQly;
	private String khoaSuDung;
	private String ghichu;

	public Subject() {

	}

	public Subject(int stt, int msmh, String tenmh, int stc, String khoaQly, String khoaSuDung, String ghichu) {
		super();
		this.stt = stt;
		this.msmh = msmh;
		this.tenmh = tenmh;
		this.stc = stc;
		this.khoaQly = khoaQly;
		this.khoaSuDung = khoaSuDung;
		this.ghichu = ghichu;
	}

	public int getStt() {
		return stt;
	}

	public void setStt(int stt) {
		this.stt = stt;
	}

	public int getMsmh() {
		return msmh;
	}

	public void setMsmh(int msmh) {
		this.msmh = msmh;
	}

	public String getTenmh() {
		return tenmh;
	}

	public void setTenmh(String tenmh) {
		this.tenmh = tenmh;
	}

	public int getStc() {
		return stc;
	}

	public void setStc(int stc) {
		this.stc = stc;
	}

	public String getKhoaQly() {
		return khoaQly;
	}

	public void setKhoaQly(String khoaQly) {
		this.khoaQly = khoaQly;
	}

	public String getKhoaSuDung() {
		return khoaSuDung;
	}

	public void setKhoaSuDung(String khoaSuDung) {
		this.khoaSuDung = khoaSuDung;
	}

	public String getGhichu() {
		return ghichu;
	}

	public void setGhichu(String ghichu) {
		this.ghichu = ghichu;
	}

	@Override
	public String toString() {
		return "Subject [stt=" + stt + ", msmh=" + msmh + ", tenmh=" + tenmh + ", stc=" + stc + ", khoaQly=" + khoaQly
				+ ", khoaSuDung=" + khoaSuDung + ", ghichu=" + ghichu + "]";
	}

}