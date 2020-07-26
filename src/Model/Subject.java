package Model;

public class Subject {

	private int stt;
	private int msmh2013;
	private String tenmh2013;
	private int stc2013;
	private String khoaQly2013;
	private String khoaSuDung;
	private int msmh2014;
	private String tenmh2014;
	private int stc2014;
	private String khoaQly2014;
	private String ghichu;

	public Subject() {

	}

	public Subject(int stt, int msmh2013, String tenmh2013, int stc2013, String khoaQly2013, String khoaSuDung,
			int msmh2014, String tenmh2014, int stc2014, String khoaQly2014, String ghichu) {
		super();
		this.stt = stt;
		this.msmh2013 = msmh2013;
		this.tenmh2013 = tenmh2013;
		this.stc2013 = stc2013;
		this.khoaQly2013 = khoaQly2013;
		this.khoaSuDung = khoaSuDung;
		this.msmh2014 = msmh2014;
		this.tenmh2014 = tenmh2014;
		this.stc2014 = stc2014;
		this.khoaQly2014 = khoaQly2014;
		this.ghichu = ghichu;
	}

	public int getStt() {
		return stt;
	}

	public int getMsmh2013() {
		return msmh2013;
	}

	public String getTenmh2013() {
		return tenmh2013;
	}

	public int getStc2013() {
		return stc2013;
	}

	public String getKhoaQly2013() {
		return khoaQly2013;
	}

	public String getKhoaSuDung() {
		return khoaSuDung;
	}

	public int getMsmh2014() {
		return msmh2014;
	}

	public String getTenmh2014() {
		return tenmh2014;
	}

	public int getStc2014() {
		return stc2014;
	}

	public String getKhoaQly2014() {
		return khoaQly2014;
	}

	public String getGhichu() {
		return ghichu;
	}

	public void setStt(int stt) {
		this.stt = stt;
	}

	public void setMsmh2013(int msmh2013) {
		this.msmh2013 = msmh2013;
	}

	public void setTenmh2013(String tenmh2013) {
		this.tenmh2013 = tenmh2013;
	}

	public void setStc2013(int stc2013) {
		this.stc2013 = stc2013;
	}

	public void setKhoaQly2013(String khoaQly2013) {
		this.khoaQly2013 = khoaQly2013;
	}

	public void setKhoaSuDung(String khoaSuDung) {
		this.khoaSuDung = khoaSuDung;
	}

	public void setMsmh2014(int msmh2014) {
		this.msmh2014 = msmh2014;
	}

	public void setTenmh2014(String tenmh2014) {
		this.tenmh2014 = tenmh2014;
	}

	public void setStc2014(int stc2014) {
		this.stc2014 = stc2014;
	}

	public void setKhoaQly2014(String khoaQly2014) {
		this.khoaQly2014 = khoaQly2014;
	}

	public void setGhichu(String ghichu) {
		this.ghichu = ghichu;
	}

	@Override
	public String toString() {
		return "Subject [stt=" + stt + ", msmh2013=" + msmh2013 + ", tenmh2013=" + tenmh2013 + ", stc2013=" + stc2013
				+ ", khoaQly2013=" + khoaQly2013 + ", khoaSuDung=" + khoaSuDung + ", msmh2014=" + msmh2014
				+ ", tenmh2014=" + tenmh2014 + ", stc2014=" + stc2014 + ", khoaQly2014=" + khoaQly2014 + ", ghichu="
				+ ghichu + "]";
	}
}
