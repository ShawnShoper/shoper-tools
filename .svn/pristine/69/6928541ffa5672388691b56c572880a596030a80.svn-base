package org.shoper.http.httpClient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.shoper.http.exception.HttpClientException;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;


/**
 * Http client 不是线程安全的，一个httpClient同时只能处理一个请求.... 请自行处理每个请求返回的结果，以及inputStream.
 * 目前支持connection超时机制，设置timeout即可...</br>
 * Eg:
 * <p>
 * <pre>
 * HttpClient httpClient = HttpClient.HttpClientBuilder.getInstances(
 * 		&quot;http://www.qq.com&quot;, &quot;utf-8&quot;, TimeUnit.SECONDS.toMillis(20));
 * httpClient.doGet();
 * </pre>
 * <p>
 * That mean's timeout is 20 seconds...
 *
 * @author ShawnShoper
 */
public class HttpClient {
	/**
	 * 访问类型...
	 *
	 * @author ShawnShoper
	 */
	public enum Method {
		POST, GET;
	}

	// 请求投
	private Map<String, Object> header = new HashMap<String, Object>();
	// UA
	private String User_Agent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.81 Safari/537.36";
	// private String User_Agent =
	// "{'User-agent': 'spider'} # Mozilla/5.0 (compatible; Googlebot/2.1 yield
	// scrapy.Request(\"http://weibo.com/aragakiyui0611\",callback=self.after_parse,headers=user_agent)";
	// Connection
	private final String Connection = "keep-alive";
	// AL
	private final String Accept_Language = "zh-CN,zh;q=0.8";
	// encoding
	public static final String UTF_8 = "UTF-8";
	public static final String GBK = "GBK";
	public static final String GB2312 = "gb2312";
	// default encoding
	private String charset = UTF_8;
	// default accept capacity
	private final int cap = 2 << 10;
	// cookies
	private String Cookie = null;
	// access url
	private String url;
	// retry count,default 3 times;
	private int retry = 3;
	// time out. unit is the default unit.see timeUnit field
	private long timeout = 20;
	// timeout unit;
	private TimeUnit timeUnit = TimeUnit.SECONDS;

	public int getRetry () {
		return retry;
	}

	public void setRetry (int retry) {
		this.retry = retry;
	}

	public TimeUnit getTimeUnit () {
		return timeUnit;
	}

	public void setTimeUnit (TimeUnit timeUnit) {
		this.timeUnit = timeUnit;
	}

	// 是否输出
	private boolean isOutput;
	// 访问类型
	private Method method;

	public long getTimeout () {
		return timeout;
	}

	public void setTimeout (long timeout) {
		this.timeout = timeout;
	}

	/**
	 * HttpClient Builder
	 *
	 * @author ShawnShoper
	 */
	public static class HttpClientBuilder {
		public static HttpClient getInstances () {
			return getInstances(null);
		}

		public static HttpClient getInstances (String url) {
			return getInstances(url, null);
		}

		public static HttpClient getInstances (String url, String charset) {
			return getInstances(url, charset, 0);
		}

		public static HttpClient getInstances (String url, String charset,
		                                       long timeout) {
			return getInstances(url, charset, timeout, TimeUnit.SECONDS, 3);
		}

		public static HttpClient getInstances (String url, String charset,
		                                       long timeout, TimeUnit timeUnit, int retry) {
			HttpClient httpClient = new HttpClient(url);
			httpClient.setCharset(charset);
			httpClient.setTimeout(timeout);
			httpClient.setTimeUnit(timeUnit);
			httpClient.setRetry(retry);
			return httpClient;
		}
	}

	public Method getMethod () {
		return method;
	}

	public void setMethod (Method method) {
		this.method = method;
	}

	public Map<String, Object> getHeader () {
		return header;
	}

	public void setHeader (Map<String, Object> header) {
		this.header = header;
	}

	public void setIsOutput (boolean isOutput) {
		this.isOutput = isOutput;
	}

	public HttpClient (String url, boolean useCookie) {
		if (useCookie)
			CookieHandler.setDefault(
					new CookieManager(null, CookiePolicy.ACCEPT_ALL));
		else
			CookieHandler.setDefault(
					new CookieManager(null, CookiePolicy.ACCEPT_NONE));
		this.url = url;
	}

	public HttpClient (String url) {
		this(url, true);
	}

	public HttpClient () {
		this(null, false);
	}

	public String getCookie () {
		return Cookie;
	}

	public void setCookie (String cookie) {
		Cookie = cookie;
	}

	/**
	 * Get method request,for get the website content
	 *
	 * @return
	 * @throws IOException
	 * @throws TimeoutException
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	@Deprecated
	public String getContent () {
		return doGet();
	}

	/**
	 * generate a URLConnection
	 *
	 * @return URLConnection
	 * @throws IOException
	 */
	public HttpURLConnection getConnection () throws IOException {
		URL u = new URL(this.url);
		HttpURLConnection conn = null;
		conn = (HttpURLConnection) u.openConnection();
		conn.setUseCaches(true);
		setDefaultHeader(conn);
		return conn;
	}

	// request headers
	private Map<String, String> reqHeaders;

	/**
	 * set reqHeaders map
	 *
	 * @param reqHeaders
	 */
	public void setReqHeadersMap (Map<String, String> reqHeaders) {
		if (reqHeaders != null)
			this.reqHeaders = reqHeaders;
	}

	/**
	 * set customer request headers
	 *
	 * @param conn
	 */
	private void setCustomerReqHeaders (HttpURLConnection conn) {
		if (reqHeaders != null)
			for (String key : this.reqHeaders.keySet()) {
				conn.setRequestProperty(key, reqHeaders.get(key));
			}
	}

	/**
	 * get method request header
	 *
	 * @return result
	 * @throws IOException
	 * @throws TimeoutException
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public String doGet () {
		String content = null;
		try {
			// get new connection
			HttpURLConnection conn = getConnection();
			conn.setReadTimeout((int) timeUnit.toMillis(timeout));
			// set default request headers
			setDefaultHeader(conn);
			// set customer request headers
			setCustomerReqHeaders(conn);
			// fetch content charset
			// String contentType = conn.getContentType();
			// if (contentType != null && contentType.contains("charset="))
			// charset = contentType.substring(contentType.indexOf("charset", 0)
			// + "charset=".length(), contentType.length());
			content = read(conn);
		} catch (Exception e) {

			// TODO: handle exception
		}
		return content;
	}

	/**
	 * 获取inputStream...
	 *
	 * @return
	 * @throws IOException
	 * @throws TimeoutException
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public InputStream getInputStream () throws IOException {
		InputStream is = null;
		// get new connection
		HttpURLConnection conn = getConnection();
		// set default request headers
		setDefaultHeader(conn);
		// set customer request headers
		setCustomerReqHeaders(conn);
		// fetch content charset
		// String contentType = conn.getContentType();
		// if (contentType != null && contentType.contains("charset="))
		// charset = contentType.substring(contentType.indexOf("charset", 0)
		// + "charset=".length(), contentType.length());
		is = getInputStream(conn);
		return is;
	}

	/**
	 * set default request header
	 *
	 * @param conn
	 */
	private void setDefaultHeader (HttpURLConnection conn) {
		// set default charset to utf-8
		conn.setRequestProperty("charset", "UTF-8");
		// set default UA
		conn.setRequestProperty("User-Agent", User_Agent);
		conn.setRequestProperty("Connection", Connection);
		if (Cookie != null)
			conn.setRequestProperty("Cookie", Cookie);
		conn.setRequestProperty("Accept_Language", Accept_Language);
	}

	/**
	 * Post method request
	 *
	 * @param msg
	 * 		message
	 * @param ignore
	 * 		ignore response
	 * @return
	 * @throws IOException
	 * @throws TimeoutException
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public String doPost (String msg, boolean ignore) throws IOException,
			InterruptedException, ExecutionException, TimeoutException {
		URL u = new URL(this.url);
		HttpURLConnection conn = null;
		conn = (HttpURLConnection) u.openConnection();
		conn.setRequestMethod("POST");// 设置提交方法
		// conn.setRequestProperty("content-type", "text/plain");
		setDefaultHeader(conn);
		setCustomerReqHeaders(conn);
		conn.setDoOutput(true);// 打开写入属性
		conn.setDoInput(true);// 打开读取属性
		conn.setConnectTimeout((int) timeUnit.toMillis(timeout));// 连接超时时间
		conn.setReadTimeout((int) timeUnit.toMillis(timeout));
		conn.setRequestProperty("content-length", msg.length() + "");
		conn.connect();
		DataOutputStream out = new DataOutputStream(conn.getOutputStream());
		out.write(msg.toString().getBytes("utf-8"));// 要post的数据，多个以&符号分割
		out.flush();
		out.close();
		// 是否要忽略返回的数据
		if (!ignore)
			return read(conn);
		return null;
	}

	public void setCharset (String charset) {
		this.charset = charset;
	}

	public String getUrl () {
		return url;
	}

	public HttpClient setUrl (String url) {
		this.url = url;
		return this;
	}

	public String getUser_Agent () {
		return User_Agent;
	}

	public void setUser_Agent (String user_Agent) {
		User_Agent = user_Agent;
	}

	/**
	 * 读取内容
	 *
	 * @param conn
	 * @return
	 * @throws TimeoutException
	 * @throws ExecutionException
	 * @throws InterruptedException
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	public String read (URLConnection conn)
			throws InterruptedException, ExecutionException, TimeoutException, IOException {
		ExecutorService service = Executors.newFixedThreadPool(1);
		String content = null;
		try {
			content = service.submit(new Callable<String>() {
				@Override
				public String call () throws Exception {
					StringBuilder content = new StringBuilder();
					InputStream inputStream = getInputStream(conn);
					if (inputStream != null) {
						OutputStream outputStream = null;
						if (isOutput)
							outputStream = new FileOutputStream(
									"/Users/ShawnShoper/Desktop/current.html");
						// byte[] b = new byte[cap];
						// int len = 0;
						BufferedReader br = new BufferedReader(
								new InputStreamReader(
										inputStream,
										Charset.forName(charset)
								));
						br.lines().forEach(line ->

								                   content.append(line + "\n")
						);
						// while ((len = inputStream.read(b)) > 0)
						// {
						// content.append(new String(b, 0, len, charset));
						// if (isOutput)
						// outputStream.write(b, 0, len);
						// }
						closeStream(inputStream);
						if (isOutput) {
							outputStream.flush();
							closeStream(outputStream);
						}
					}
					return content.toString();
				}
			}).get(timeout, timeUnit);
		} finally {
			service.shutdownNow();
		}
		return content;
	}

	public Document getHtml () throws HttpClientException {
		ExecutorService service = Executors.newFixedThreadPool(1);
		Document document = null;
		try {
			// retry 'retry' times
			Exception oe = null;
retry:
			for (int i = 0; i < retry; i++) {
				try {
					document = service.submit(new Callable<Document>() {
						@Override
						public Document call () throws Exception {

							return Jsoup.parse(getInputStream(), charset, "");
						}
					}).get(timeout, timeUnit);
				} catch (InterruptedException e) {
					// e.printStackTrace();
					// Thread.currentThread().interrupt();
					break retry;
				} catch (ExecutionException e) {
					oe = e;
					// retry
				} catch (TimeoutException e) {
					oe = e;
					// retry
				}
			}
			if (oe != null) {
				throw new HttpClientException(oe);
			}
		} finally {
			service.shutdownNow();
		}
		return document;
	}

	/**
	 * 获取inputStream;
	 *
	 * @param conn
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 */
	private InputStream getInputStream (URLConnection conn) throws IOException {
		/*
		 * // #TODO timeout handle ExecutorService service =
		 * Executors.newFixedThreadPool(1); Future<InputStream> future =
		 * service.submit(new ReadConnection(conn)); InputStream inputStream =
		 * null; try { inputStream = this.timeout > 0 ? future.get(this.timeout,
		 * timeUnit) : future.get(); } catch (Exception e) {
		 * closeInputStream(inputStream); }finally{ service.shutdownNow(); }
		 */
		String contentType = conn.getContentType();
		if (contentType != null && contentType.contains("charset="))
			charset = contentType.substring(
					contentType.indexOf("charset", 0) + "charset=".length(),
					contentType.length()
			);
		// 获取inputStream
		return conn.getInputStream();
	}

	/**
	 * 关闭inputStream
	 */
	private void closeStream (Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/*	*//**
	 * 去读内容核心类.....
	 *
	 * @author ShawnShoper
	 *
	 *//*
			 * class ReadConnection implements Callable<InputStream> { private
			 * URLConnection conn;
			 * 
			 * public ReadConnection(URLConnection conn) { this.conn = conn; }
			 * 
			 * @Override public InputStream call() throws Exception {
			 * //获取网页返回的页面编码 String contentType = conn.getContentType(); if
			 * (contentType != null && contentType.contains("charset=")) charset
			 * = contentType.substring(contentType.indexOf("charset", 0) +
			 * "charset=".length(), contentType.length()); //获取inputStream
			 * return conn.getInputStream(); } }
			 */

	/**
	 * Post data，map to string
	 *
	 * @param postData
	 * @return
	 */
	public static String PostDataMapToStr (Map<String, String> postData) {
		if (postData == null)
			return null;
		StringBuilder dataSB = new StringBuilder();
		for (String key : postData.keySet()) {
			dataSB.append(key + "=" + postData.get(key) + "&");
		}
		if (dataSB.length() > 0)
			dataSB.setLength(dataSB.length() - 1);
		return dataSB.toString();
	}

	/**
	 * Post data , map to string (Duplex key)
	 *
	 * @param postData
	 * @return
	 */
	public static String PostDataMapToStr (List<Map<String, String>> postData) {
		if (postData == null)
			return null;
		StringBuilder dataSB = new StringBuilder();
		for (Map<String, String> map : postData) {
			dataSB.append(PostDataMapToStr(map) + "&");
		}

		if (dataSB.length() > 0)
			dataSB.setLength(dataSB.length() - 1);
		return dataSB.toString();
	}

	/**
	 * 获取HTML文档对象
	 *
	 * @param charset
	 * 		编码
	 * @param baseUri
	 * 		基础页
	 * @return
	 */
	@Deprecated
	public Document getDocument (String charset, String baseUri) {
		Document document = null;
		try {
			document = Jsoup.parse(getInputStream(), charset, baseUri);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return document;
	}
}
