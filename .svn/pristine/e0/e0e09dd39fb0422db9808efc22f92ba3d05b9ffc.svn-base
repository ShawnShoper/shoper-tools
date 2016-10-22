package org.shoper.http.apache;

import org.shoper.http.apache.message.BasicNameValuePair;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by ShawnShoper on 16/8/7.
 * This util is for Apache HttpCLient...
 * It's different from org.shoper.http.httpClient.HttpClient
 */
public class HttpClientBuilder {
	private String charset;
	private int timeout = 20;
	private int retry = 3;
	private TimeUnit timeoutUnit = TimeUnit.SECONDS;
	private String url;
	private List<Map<String,String>> formDatas;
	private boolean proxy;

	public HttpClientBuilder setProxy (boolean proxy) {
		this.proxy = proxy;
		return this;
	}

	public HttpClientBuilder setCharset (String charset) {
		this.charset = charset;
		return this;
	}

	public HttpClientBuilder setTimeout (int timeout) {
		this.timeout = timeout;
		return this;
	}

	public HttpClientBuilder setRetry (int retry) {
		this.retry = retry;
		return this;
	}

	public HttpClientBuilder setTimeoutUnit (TimeUnit timeoutUnit) {
		this.timeoutUnit = timeoutUnit;
		return this;
	}

	public HttpClientBuilder setUrl (String url) {
		this.url = url;
		return this;
	}

	public HttpClientBuilder setFormDatas (List<Map<String,String>> formDatas) {
		this.formDatas = formDatas;
		return this;
	}
	public HttpClientBuilder setFormDatas (Map<String,String> formDatas) {
		List<Map<String,String>> formData = new ArrayList<>();
		formData.add(formDatas);
		setFormDatas(formData);
		return this;
	}
	public static HttpClientBuilder custom () {
		return new HttpClientBuilder();
	}

	public HttpClient build () throws MalformedURLException {
		AccessBean accessBean = new AccessBean(charset, timeout, retry, timeoutUnit, url, proxy,formDatas);
		return new HttpClient(accessBean);
	}
}
