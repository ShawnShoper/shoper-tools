package org.shoper.http.apache;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.shoper.commons.StringUtil;
import org.shoper.http.apache.handle.DefaultResponseHandler;
import org.shoper.http.apache.handle.ResponseHandler;
import org.shoper.http.apache.handle.RetryHandler;
import org.shoper.http.apache.proxy.ProxyServer;
import org.shoper.http.apache.proxy.ProxyServerPool;
import org.shoper.http.exception.HttpClientException;
import org.shoper.http.exception.UnHandleException;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.util.*;
import java.util.concurrent.TimeoutException;

/**
 * Created by ShawnShoper on 16/8/8.
 */
public class HttpClient {
	private AccessBean accessBean;
	//	private Domain domain;
	private List<Header> headers = new ArrayList<>();
	//	private boolean isAutoProxyServer = true;
	private ProxyServer proxyServer;
	private ResponseHandler responseHandle;
	private String cookies;

	public void setResponseHandle (ResponseHandler responseHandle) {
		this.responseHandle = responseHandle;
	}

	public String getCookies () {
		return cookies;
	}

	public void setCookies (String cookies) {
		this.cookies = cookies;
	}

	public Map<String, String> getRequestHeader () {
		if (Objects.isNull(headers)) return null;
		else {
			Map<String, String> requestHeaders = new HashMap<>(headers.size());
			this.headers.forEach(h -> requestHeaders.put(h.getName(), h.getValue()));
			return requestHeaders;
		}
	}

	public void setRequestHeader (Map<String, String> requestHeader) {

		if (Objects.nonNull(requestHeader) && !requestHeader.isEmpty()) {
			List<Header> headers = new ArrayList<>(requestHeader.size());
			for (String key : requestHeader.keySet()) {
				Header header = new BasicHeader(key, requestHeader.get(key));
				headers.add(header);
			}
			this.headers.addAll(headers);


		}
	}

	public AccessBean getAccessBean () {
		return accessBean;
	}

	public void setAccessBean (AccessBean accessBean) {
		this.accessBean = accessBean;
	}

	public HttpClient (AccessBean accessBean) throws MalformedURLException {
		this();
		this.accessBean = accessBean;
//		parseDomain();
	}

	public HttpClient () {
		responseHandle = new DefaultResponseHandler();
	}

//	void parseDomain () throws MalformedURLException {
//		String url = accessBean.getUrl();
//		URL u = new URL(url);
//		this.domain = new Domain(u.getHost(), u.getPort() == -1 ? u.getDefaultPort() : u.getPort(), u.getProtocol());
//	}


	public static final String DEFAULT_CHARSET = "ISO-8859-1";
	private static String DEFAULT_UA = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.89 Safari/537.36";

	public String post () throws HttpClientException, InterruptedException, TimeoutException {
		CloseableHttpClient httpClient = null;//HttpClients.createDefault();
		CloseableHttpResponse response = null;
		int statuCode = 0;
		try {
			httpClient = getHttpClient();
			HttpPost httpPost = new HttpPost(accessBean.getUrl());
			httpPost.setHeaders(injectHeaders());
			if (Objects.nonNull(accessBean.getFormDatas()) && !accessBean.getFormDatas().isEmpty())
				httpPost.setEntity(new UrlEncodedFormEntity(accessBean.getFormDatas()));
			response = httpClient.execute(httpPost);
			responseHandle.handleHttpResponse(response, this);
			return responseHandle.getContent();
		} catch (SocketException e) {
			statuCode = 1;
		} catch (Exception e) {
			statuCode = 1;
			throw new HttpClientException(e);
		} finally {
			destroy(response, httpClient, statuCode);
		}
		return null;
	}

	private void destroy (Closeable closeable1, Closeable closeable2, int statuCode) throws HttpClientException {
		if (accessBean.isProxy())
			ProxyServerPool.returnProxyServer(proxyServer, statuCode == 0);
		try {
			if (!Objects.isNull(closeable1))
				closeable1.close();
			if (!Objects.isNull(closeable2))
				closeable2.close();
		} catch (IOException e) {
			throw new HttpClientException(e);
		}
	}

	/**
	 * Use get method access to websites
	 *
	 * @return
	 * @throws HttpClientException
	 */
	public String doGet () throws InterruptedException, HttpClientException, TimeoutException {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		int statuCode = 0;
		try {
			httpClient = getHttpClient();
			HttpGet httpGet = new HttpGet(accessBean.getUrl());
			httpGet.setHeaders(injectHeaders());
			response = httpClient.execute(httpGet);
			responseHandle.handleHttpResponse(response, this);
			return responseHandle.getContent();
		} catch (IOException e) {
			statuCode = 1;
			throw new HttpClientException(e);
		} catch (UnHandleException e) {
		} finally {
			destroy(response, httpClient, statuCode);
		}
		return null;
	}

	/**
	 * inject request headers
	 *
	 * @return
	 */
	private Header[] injectHeaders () {
		if (this.headers.stream().noneMatch(e -> "User-Agent".equals(e.getName())))
			this.headers.add(new BasicHeader("User-Agent", DEFAULT_UA));
		if (!StringUtil.isEmpty(this.cookies))
			this.headers.add(new BasicHeader("Cookie", this.cookies));
		return this.headers.toArray(new Header[]{});
	}

	public Document getDocument () throws HttpClientException, InterruptedException, TimeoutException {
		return Jsoup.parse(doGet());
	}

	/**
	 * Get a proxy server
	 *
	 * @return
	 */
	private ProxyServer pullProxyServer () {
		ProxyServer proxyServer = ProxyServerPool.getProxyServer();
		this.proxyServer = proxyServer;
		return proxyServer;
	}

	public ProxyServer getProxyServer () {
		return proxyServer;
	}

	public void setProxyServer (ProxyServer proxyServer) {
		this.proxyServer = proxyServer;

	}

	/**
	 * init HttpClient
	 *
	 * @return CloseableHttpClient
	 */
	private CloseableHttpClient getHttpClient () throws InterruptedException, HttpClientException {
		HttpClientBuilder httpClientBuilder = HttpClients.custom().setRetryHandler(new RetryHandler(accessBean.getRetry(), true));//new RetryHandler(accessBean.getRetry()));
		RequestConfig.Builder builder = RequestConfig.custom();
		if (accessBean.getTimeout() > 0 && Objects.nonNull(accessBean.getTimeoutUnit()))
			builder.setConnectTimeout((int) accessBean.getTimeoutUnit().toMillis(accessBean.getTimeout()));
		builder.setCookieSpec(CookieSpecs.STANDARD);

		//check proxy profile
		if (accessBean.isProxy()) {
			ProxyServer proxyServer = pullProxyServer();
			if (Objects.nonNull(proxyServer)) {
				BasicCredentialsProvider credsProvider = new BasicCredentialsProvider();
				credsProvider.setCredentials(
						new AuthScope(proxyServer.getHost(), proxyServer.getPort()),
						new UsernamePasswordCredentials(proxyServer.getUsername(), proxyServer.getPassword())
				);
				httpClientBuilder.setDefaultCredentialsProvider(credsProvider);
				HttpHost proxy = new HttpHost(proxyServer.getHost(), proxyServer.getPort());
				builder.setProxy(proxy);
			}
		}
		httpClientBuilder.setDefaultRequestConfig(builder.build());
        CloseableHttpClient httpClient = httpClientBuilder.build();
//        if(getAccessBean().getUrl().startsWith("https")){
//                try {
//                    injectHTTPS(httpClient);
//                } catch (Exception e) {
//                    throw  new HttpClientException(e);
//                }
//        }
		return httpClient;
	}

	//	private CookieStore parseCookies () {
//		CookieStore cookieStore = new BasicCookieStore();
//		if (!StringUtil.isEmpty(headers.)) {
//			String[] cookies = cookie.split(";");
//			cookie = cookie.replaceAll(" ", "");
//			for (int i = 0; i < cookies.length; i++) {
//				String[] singleCookie = cookies[i].split("=");
//				cookieStore.addCookie(new BasicClientCookie(singleCookie[0], singleCookie[1]));
//			}
//		}
//		return cookieStore;
//	}
	@Deprecated
	public InputStream getInputSteam () throws HttpClientException, InterruptedException, IOException, TimeoutException {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		int statuCode = 0;
		try {
			httpClient = getHttpClient();
			HttpGet httpGet = new HttpGet(accessBean.getUrl());
			httpGet.setHeaders(injectHeaders());
			response = httpClient.execute(httpGet);
			responseHandle.handleHttpResponse(response, this);
			return responseHandle.getInputSteam();
		} catch (IOException e) {
			statuCode = 1;
			throw new HttpClientException(e);
		} finally {
			destroy(response, httpClient, statuCode);
		}
	}
//	public void injectHTTPS(CloseableHttpClient httpclient) throws KeyManagementException, NoSuchAlgorithmException {
//		//Secure Protocol implementation.
//		SSLContext ctx = SSLContext.getInstance("SSL");
//		//Implementation of a trust manager for X509 certificates
//		X509TrustManager tm = new X509TrustManager() {
//
//			public void checkClientTrusted(X509Certificate[] xcs,
//										   String string) throws CertificateException {
//
//			}
//
//			public void checkServerTrusted(X509Certificate[] xcs,
//										   String string) throws CertificateException {
//			}
//
//			public X509Certificate[] getAcceptedIssuers() {
//				return null;
//			}
//		};
//		ctx.init(null, new TrustManager[] { tm }, null);
//		SSLSocketFactory ssf = new SSLSocketFactory(ctx);
//		ClientConnectionManager ccm = httpclient.getConnectionManager();
//		//register https protocol in httpclient's scheme registry
//		SchemeRegistry sr = ccm.getSchemeRegistry();
//		sr.register(new Scheme("https", 443, ssf));
//	}
}
