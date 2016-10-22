package org.shoper.mail;

import org.junit.Test;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

public class Mail_Test {
	@Test
	public void mail_test() throws AddressException, MessagingException{
		String account = "xieh@daqsoft.com";
		String pwd = "shawn930825";
		String sub = "test";
		String to = "xiehao3692@vip.qq.com";
		String content = "hello,this is a test..";
		MailBuilder.getInstances().setAccount(account, pwd)
		.setSMTPServer(SMTPServer.STMP_QQ).setSubject(sub)
		.setAuth(true)
		.setTo(to).setContent(content).build().send();
	}
}
