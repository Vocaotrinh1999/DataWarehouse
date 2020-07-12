package DateDim;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import LoadDataStaging.ConnectDataConfig;

public class Date_Dim {

	ConnectDataConfig config;
	
	public Date_Dim() {
		config = new ConnectDataConfig();
	}
	
	public void insertDate_Dim() {
		String readDate =config.readFileFromFolder("data\\date_dim_without_quarter.txt");
		Connection connection = config.connectDataStaging();
		String sql = "insert into datawarehouse_staging.date_dim_without_quarter "
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			PreparedStatement pre = connection.prepareStatement(sql);
			String[] data = readDate.split("\n");
			for (String d : data) {
				String[] date = d.split(",");
				pre.setInt(1, Integer.parseInt(date[0]));
				pre.setString(2, date[1]);
				pre.setInt(3, Integer.parseInt(date[2]));
				pre.setInt(4, Integer.parseInt(date[3]));
				pre.setString(5, date[4]);
				pre.setString(6, date[5]);
				pre.setString(7, date[6]);
				pre.setString(8, date[7]);
				pre.setInt(9, Integer.parseInt(date[8]));
				pre.setInt(10, Integer.parseInt(date[9]));
				pre.setInt(11, Integer.parseInt(date[10]));
				pre.setString(12, date[11]);
				pre.setString(13, date[12]);
				pre.setInt(14, Integer.parseInt(date[13]));
				pre.setString(15, date[14]);
				pre.setString(16, date[15]);
				pre.setString(17, date[16]);
				pre.setString(18, date[17]);
				pre.execute();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Insert Date dim sucess");
	}
	public void insertMonth_Dim() {
		String readDate =config.readFileFromFolder("data\\month_dim.txt");
		Connection connection = config.connectDataStaging();
		String sql = "insert into datawarehouse_staging.month_dim "
				+ "values(?,?,?,?,?)";
		try {
			PreparedStatement pre = connection.prepareStatement(sql);
			String[] data = readDate.split("\n");
			for (String d : data) {
				String[] month = d.split(",");
				pre.setInt(1, Integer.parseInt(month[0]));
				pre.setString(2, month[1]);
				pre.setInt(3, Integer.parseInt(month[2]));
				pre.setInt(4, Integer.parseInt(month[3]));
				pre.setInt(5, Integer.parseInt(month[4]));
				pre.execute();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Insert month dim sucess");
	}
	public static void main(String[] args) {
		Date_Dim d = new Date_Dim();
		//d.insertDate_Dim();
		d.insertMonth_Dim();
	}

}
