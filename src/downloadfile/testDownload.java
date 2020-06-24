package downloadfile;

public class testDownload {
	public static void main(String[] args) {
		Download download = new Download();
		Account account = new Account("guest_access", "123456");
		FileProperties fileProperties = new FileProperties("sinhvien*.*", "/volume1/ECEP/song.nguyen/DW_2020/data",
<<<<<<< HEAD
				"F:\\anh\\datawarehouse\\data");
=======
				"D:\\data");
>>>>>>> branch 'master' of https://github.com/Vocaotrinh1999/DataWarehouse
		download.downloadFileFromNas(account, fileProperties);
	}
}
