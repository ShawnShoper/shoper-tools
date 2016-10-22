package org.shoper.mail;

import com.alibaba.fastjson.JSONObject;

import java.util.Set;

public class MailInfo
{

	private String smtp;// = "smtp.exmail.qq.com";
	private String account;
	private String password;
	private String to;
	private String subject;
	private String content;
	private boolean auth;
	private Set<String> fileName;

	public String getSmtp()
	{
		return smtp;
	}
	public void setSmtp(String smtp)
	{
		this.smtp = smtp;
	}
	public String getAccount()
	{
		return account;
	}
	public void setAccount(String account)
	{
		this.account = account;
	}
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
	public String getTo()
	{
		return to;
	}
	public void setTo(String to)
	{
		this.to = to;
	}
	public String getSubject()
	{
		return subject;
	}
	public void setSubject(String subject)
	{
		this.subject = subject;
	}
	public String getContent()
	{
		return content;
	}
	public void setContent(String content)
	{
		this.content = content;
	}
	public boolean isAuth()
	{
		return auth;
	}
	public void setAuth(boolean auth)
	{
		this.auth = auth;
	}
	public Set<String> getFileName()
	{
		return fileName;
	}
	public void setFileName(Set<String> fileName)
	{
		this.fileName = fileName;
	}
	public String toJson()
	{
		return JSONObject.toJSONString(this);
	}
}
