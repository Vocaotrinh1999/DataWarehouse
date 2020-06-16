package LoadData;

import java.io.IOException;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class DowloadFile {

	public void dowloadFile(String username,String host,int port) {
      
      try {
    	  java.util.Properties config = new java.util.Properties();
          config.put("StrictHostKeyChecking", "no");
   
          JSch ssh = new JSch();
          Session session = ssh.getSession(username, host, port);
          session.setConfig(config);
          session.setPassword("123456");
          session.connect();
   
          ChannelSftp sftp = (ChannelSftp) session.openChannel("sftp");
          sftp.connect();
          System.out.println("ket noi thanh cong");
          System.err.println(sftp.pwd());
   
          sftp.disconnect();
          session.disconnect();
	} catch (Exception e) {
		e.printStackTrace();
	}
    }
	
	public static void main(String[] args) {
		DowloadFile dowload = new DowloadFile();
		dowload.dowloadFile("guest_access", "115.78.8.83", 5000);
	}
}
