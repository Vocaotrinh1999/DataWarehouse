<<<<<<< HEAD
package GuiMail;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMailTLS {

	public boolean sendMail(String to, String subject, String text) {
		try {
			Properties props = new Properties();
			InputStream input = new FileInputStream("config/mailconfig.properties");
			props.load(input);// doc vao load cac properties cua mail tu file properties
			final String username = props.getProperty("username");
			final String password = props.getProperty("password");
			Session session = Session.getInstance(props, new javax.mail.Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});
			try {
				Message message = new MimeMessage(session);
				message.setHeader("Content-Type", "text/plain; charset=UTF-8");
				message.setFrom(new InternetAddress(username));
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
				message.setSubject(subject);
				message.setText(text);
				Transport.send(message);
			} catch (MessagingException e) {
				System.err.println(e);
				return false;
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		return true;
	}

	public static void main(String[] args) {
		SendMailTLS ss = new SendMailTLS();
		System.out.println(ss.sendMail("17130256@st.hcmuaf.edu.vn", "test send mail", "i am trinh"));
	}
}
=======
package GuiMail;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMailTLS {

	public boolean sendMail(String to, String subject, String text) {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.ssl.trust", "smtp.gmail.com");//very important
		props.put("mail.smtp.port", "587");
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("Vocaotrinh1999@gmail.com", "15062015");
			}
		});
		try {
			Message message = new MimeMessage(session);
			message.setHeader("Content-Type", "text/plain; charset=UTF-8");
			message.setFrom(new InternetAddress("Vocaotrinh1999@gmail.com"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			message.setSubject(subject);
			message.setText(text);
			Transport.send(message);
		} catch (MessagingException e) {
			System.err.println(e);
			return false;
		}
		return true;
	}
	public static void main(String[] args) {
        SendMailTLS ss = new SendMailTLS();
        System.out.println(ss.sendMail("17130256@st.hcmuaf.edu.vn", "test send mail", "i am trinh"));
    }
}
>>>>>>> branch 'master' of https://github.com/Vocaotrinh1999/DataWarehouse
