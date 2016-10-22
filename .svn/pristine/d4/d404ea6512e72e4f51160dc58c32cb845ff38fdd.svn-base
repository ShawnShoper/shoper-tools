package org.shoper.mail;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;
import java.util.Set;

/**
 * 邮箱客户端构建..
 * @author ShawnShoper
 *
 */
public class MailBuilder {
	private String charset = "UTF-8";
	
	public String getCharset() {
		return charset;
	}
	public MailBuilder setCharset(String charset) {
		this.charset = charset;
		return this;
	}
	private MailInfo mailInfo = new MailInfo();
	public static MailBuilder getInstances(){
		return new MailBuilder();
	}
	public MailBuilder setAuth(boolean auth){
		mailInfo.setAuth(auth);
		return this;
	}
	public MailBuilder setAccount(String acc, String pwd){
		mailInfo.setAccount(acc);
		mailInfo.setPassword(pwd);
		return this;
	}
	public MailBuilder setSMTPServer(String smtp){
		mailInfo.setSmtp(smtp);
		return this;
	}
	public MailBuilder setFiles(Set<String> file){
		mailInfo.setFileName(file);
		return this;
	}
	public MailBuilder setTo(String to){
		mailInfo.setTo(to);
		return this;
	}
	public MailBuilder setSubject(String sub){
		mailInfo.setSubject(sub);
		return this;
	}
	public MailBuilder setContent(String content){
		mailInfo.setContent(content);
		return this;
	}
	public Mail build(){
		return new Mail(mailInfo);
	}
	public class Mail{
		private MailInfo mi;
		public Mail(MailInfo mi){
			this.mi = mi;
		}
		public void send() throws AddressException, MessagingException {
			Properties props = new Properties();
			props.put("mail.smtp.host", mi.getSmtp()); // 指定SMTP服务器
			props.put("mail.smtp.auth", mi.isAuth()); // 指定是否需要SMTP验证
			Session mailSession = Session.getDefaultInstance(props);
			Message message = new MimeMessage(mailSession);
			message.setFrom(new InternetAddress(mi.getAccount())); // 发件人
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					mi.getTo())); // 收件人
			message.setSubject(mi.getSubject()); // 邮件主题
			// 指定邮箱内容及ContentType和编码方式
			message.setContent(mi.getContent(), "text/html;charset="+charset);
			// 指定邮件发送日期
			message.setSentDate(new Date());
			message.saveChanges();
			Transport transport = mailSession.getTransport("smtp");
			transport.connect(mi.getSmtp(), mi.getAccount(), mi.getPassword());
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
		}
	}
}
