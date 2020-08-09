package downloadfile;

import java.io.File;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;

import com.chilkatsoft.CkGlobal;
import com.chilkatsoft.CkScp;
import com.chilkatsoft.CkSsh;

import GuiMail.SendMailTLS;
import LoadDataStaging.ConnectDataConfig;
import LoadDataStaging.InsertData2;

public class Download {
	private ConnectDataConfig connectDataConfig;
	private SendMailTLS sendMail;
	private InsertData2 insert;

	public Download() {
		connectDataConfig = new ConnectDataConfig();
		insert = new InsertData2();
	}

	static {
		try {
			System.loadLibrary("chilkat");
		} catch (UnsatisfiedLinkError e) {
			System.err.println("Native code library failed to load.\n" + e);
			System.exit(1);
		}
	}

	// lay tu csdl len roi dowload
	public void dowloadFile(int id) {
		String fileName = "";
		String typeFile = "";
		String remoteDir = "";
		String localDir = "";
		String host = "";
		Integer port = null;
		String userName = "";
		String password = "";
		String sql = "SELECT * FROM datawarehouse_configuration.`control.data_file_configuration` where data_file_config_id ="
				+ id;
		try {
			ResultSet r = connectDataConfig.selectDatabase(sql);
			while (r.next()) {
				fileName = r.getString("data_file_config_name");
				typeFile = r.getString("data_file_type");
				remoteDir = r.getString("remote_Dir");
				localDir = r.getString("import_dir");
				host = r.getString("hostname");
				port = r.getInt("port");
				userName = r.getString("username");
				password = r.getString("password");
			}
			downloadFileFromNas(id, fileName + "." + typeFile, host, port, userName, password, remoteDir, localDir);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void downloadFileFromNas(int id, String fileFomatDownload, String host, Integer portName, String userName,
			String password, String remoteDirName, String localDirName) {

		String textSendMail = " ";
		CkGlobal glob = new CkGlobal();
		glob.UnlockBundle("Waiting . . .");
		CkSsh ssh = new CkSsh();

		String hostname = host;
		int port = portName;
		boolean success = ssh.Connect(hostname, port);
		if (success != true) {
			System.out.println(ssh.lastErrorText());
			return;
		}
		ssh.put_IdleTimeoutMs(5000);
		success = ssh.AuthenticatePw(userName, password);
		if (success != true) {
			System.out.println(ssh.lastErrorText());
			return;
		}
		CkScp scp = new CkScp();
		success = scp.UseSsh(ssh);
		if (success != true) {
			System.out.println(scp.lastErrorText());
			return;
		}
		scp.put_SyncMustMatch(fileFomatDownload);
		int mode = 3;
		boolean bRecurse = false;
		success = scp.SyncTreeDownload(remoteDirName, localDirName, mode, bRecurse);
		if (success != true) {
			System.out.println(scp.lastErrorText());
			return;
		}

		ssh.Disconnect();
		// check trùng file và insert log
		File file = new File(localDirName);
		File[] listFile = file.listFiles();
		int count = 0;
		for (File file2 : listFile) {
			if (file2.length() == 0) {
				System.out.println("File error :" + file2.getName());
				textSendMail = "File " + file2.getName() + " error";
				sendMail.sendMail("17130256@st.hcmuaf.edu.vn", "File Error", textSendMail);
			} else {
				if (checkDuplicate(file2.getName(), id) == false) {
					insertLog(file2.getName(), id, "DL");
				}
			}
		}

		System.out.println("Download File Success And Insert " + count + " File To Log");
		textSendMail = "Download File Success And Insert " + count + " File To Log";
		sendMail.sendMail("17130024@st.hcmuaf.edu.vn", "File Error", textSendMail);
	}

	public void insertLog(String fileName, int id, String status) {
		String sql = "insert into datawarehouse_configuration.`control.data_file`(file_name,data_config_id,file_status,dt_file_queued) value('"
				+ fileName + "'," + id + ",'" + status + "','" + String.valueOf(LocalDateTime.now()) + "')";
		try {
			connectDataConfig.perform(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// kiem tra trung du lieu khi ghi vao log
	public boolean checkDuplicate(String fileNameNeedCheck, int id) {
		String sql = "SELECT * FROM datawarehouse_configuration.`control.data_file` where data_config_id =" + id;
		ArrayList<String> listFileName = new ArrayList<String>();
		try {
			ResultSet rs = connectDataConfig.selectDatabase(sql);
			while (rs.next()) {
				String fileName = rs.getString("file_name");
				listFileName.add(fileName);
			}
			for (String fileName : listFileName) {
				if (fileNameNeedCheck.equalsIgnoreCase(fileName))
					return true;
			}
		} catch (Exception e) {
			sendMail.sendMail("17130024@st.hcmuaf.edu.vn", "File Error", "ERROR SQL");
		}
		return false;
	}
}
