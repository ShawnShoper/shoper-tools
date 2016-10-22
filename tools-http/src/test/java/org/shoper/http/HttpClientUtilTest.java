package org.shoper.http;

import org.junit.Before;
import org.junit.Test;
import org.shoper.commons.DateUtil;
import org.shoper.commons.ImageInfo;
import org.shoper.commons.ImageUtil;
import org.shoper.http.apache.AccessBean;
import org.shoper.http.apache.HttpClient;
import org.shoper.http.apache.HttpClientBuilder;
import org.shoper.http.apache.handle.AbuyunProxyResponseHandler;
import org.shoper.http.apache.proxy.ProxyServerPool;
import org.shoper.http.exception.HttpClientException;
import org.shoper.http.exception.TimeoutException;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Created by ShawnShoper on 16/8/7.
 */
public class HttpClientUtilTest {
	@Before
	public void initProxyPool () throws FileNotFoundException {
		ProxyServerPool.importProxyServer(new File("src/main/resources/proxyip.ls"), Charset.forName("utf-8"));
	}

	@Test
	public void get () throws HttpClientException, IOException, InterruptedException, TimeoutException, java.util.concurrent.TimeoutException {
		HttpClient httpClient = HttpClientBuilder.custom().setUrl("http://weibo.com/p/aj/v6/mblog/mbloglist?ajwvr=6&domain=100505&profile_ftype=1&is_all=1&pagebar=0&pl_name=Pl_Official_MyProfileFeed__24&id=1005053627755783&script_uri=/u/3627755783&feed_type=0&page=1&pre_page=1&domain_op=100505&__rnd=1470627772452").build();
//		httpClient.setCookies("SINAGLOBAL=4466064745352.929.1475033718231; wvr=6; YF-Ugrow-G0=1eba44dbebf62c27ae66e16d40e02964; SCF=Ah9Z2M30c4B9_6Thfkz--Vh0Lr5sJgOXdV44r8enK_f2lKYdoKOhVPwhLWEx82WTwP83FbhmvFWGfNL-SljN_Yw.; SUB=_2A2566YyeDeTxGedG6VYS9i3LzT6IHXVZnvlWrDV8PUNbmtBeLXPHkW-O44Qf_9hQgtQUtkULL_-K8kpF5A..; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WhdPz3lCecbA_hp6RKPxGfu5JpX5KMhUgL.Fo2ReoB0SoeNSoz2dJLoIEqLxK-LBK2L1--LxKML12qLB-B_TCH8SCHFxbHWSEH8Sb-RSF-ReBtt; SUHB=010XX2a6jXFhTf; ALF=1506750542; SSOLoginState=1475214542; YF-V5-G0=8a3c37d39afd53b5f9eb3c8fb1874eec; _s_tentry=login.sina.com.cn; Apache=9010673075615.451.1475214562259; ULV=1475214562281:3:3:3:9010673075615.451.1475214562259:1475159910047; YF-Page-G0=b9004652c3bb1711215bacc0d9b6f2b5; UOR=,,login.sina.com.cn");
		System.out.println(httpClient.doGet());
	}

	@Test
	public void proxyGet () throws HttpClientException, MalformedURLException, InterruptedException {
		CountDownLatch countDownLatch = new CountDownLatch(10);
		for (int i = 0; i < 10; i++) {
			Function<Runnable, Thread> runnable = Thread::new;
			runnable.apply(() -> {
				try {
					long d = System.currentTimeMillis();
					HttpClient httpClient = HttpClientBuilder.custom().setUrl("http://1212.ip138.com/ic.asp").setProxy(true).build();
					Map<String, String> requestHeader = new HashMap<>();
					requestHeader.put("Proxy-Switch-Ip", "yes");
					httpClient.setRequestHeader(requestHeader);
					httpClient.setResponseHandle(new AbuyunProxyResponseHandler());
					System.out.println(httpClient.getDocument().text());
					System.out.println(DateUtil.TimeToStr(System.currentTimeMillis() - d));
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					countDownLatch.countDown();
				}
			}).start();
//			Thread thread = new Thread(() -> {
//				try {
//					long d = System.currentTimeMillis();
//					HttpClient httpClient = HttpClientBuilder.custom().setUrl("http://1212.ip138.com/ic.asp").setProxy(true).build();
//					Map<String, String> requestHeader = new HashMap<>();
//					requestHeader.put("Proxy-Switch-Ip", "yes");
//					httpClient.setRequestHeader(requestHeader);
//					httpClient.setResponseHandle(new AbuyunProxyResponseHandler());
//					System.out.println(httpClient.getDocument().text());
//					System.out.println(DateUtil.TimeToStr(System.currentTimeMillis() - d));
//				} catch (Exception e) {
//					e.printStackTrace();
//				} finally {
//					countDownLatch.countDown();
//				}
//			});
//			thread.start();
		}
		countDownLatch.await();
	}

	@Test
	public void post () throws HttpClientException, MalformedURLException, InterruptedException, TimeoutException, java.util.concurrent.TimeoutException {
		Map<String, String> formData = new HashMap<>();
		formData.put("cityId", "1097");
		formData.put("cityPY", "panzhihua");
		formData.put("cityCode", "0812");
		formData.put("page", 1 + "");
		formData.put("StartTime", "2016-09-21");
		formData.put("DepTime", "2016-09-22");
		String listUrl = "http://hotels.ctrip.com/Domestic/Tool/AjaxHotelList.aspx";
		System.out.println(HttpClientBuilder.custom().setUrl(listUrl).setTimeout(20).setTimeoutUnit(TimeUnit.SECONDS).setFormDatas(formData).build().post());
	}

	@Test
	public void postProxy () throws HttpClientException, MalformedURLException, InterruptedException, TimeoutException, java.util.concurrent.TimeoutException {
		AccessBean accessBean = new AccessBean("http://192.168.0.36:8081/login");
		Map<String, String> formData = new HashMap<>();
		formData.put("username", "admin");
		formData.put("pwd", "daqsoft");
		accessBean.setFormDatas(formData);
		System.out.println(HttpClientBuilder.custom().setUrl("http://1921.168.0.36:8081/login").setFormDatas(formData).build().post());
	}

	@Test
	public void parseCookies () {
		String cookiestr = "_s_tentry=www.thebigdata.cn; UOR=www.speedycloud.cn,widget.weibo.com,sound.zol.com.cn; login_sid_t=33593e4cbb938da8de2051e0aa3c824b; Apache=691294291289.7146.1470588507205; SINAGLOBAL=691294291289.7146.1470588507205; ULV=1470588507223:1:1:1:691294291289.7146.1470588507205:; SCF=AueW_lkMz4IIxOUh4rd7-xa3NTg4XrbMRjM6nQ4MvuiC_Qp-srmdRh6ykwrxWvyBteRSVP-S_l7j3OR4kOSmjH8.; SUB=_2A256oxbBDeTxGedG6VYS9i3LzT6IHXVZ2Q8JrDV8PUNbmtBeLWvekW-TvkdF6oFJn8p-5-xxHio4uDHvFQ..; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WhdPz3lCecbA_hp6RKPxGfu5JpX5K2hUgL.Fo2ReoB0SoeNSoz2dJLoIEqLxK-LBK2L1--LxKML12qLB-B_TCH8SCHFxbHWSEH8Sb-RSF-ReBtt; SUHB=0G0_bcVxn483Fz; ALF=1502124561; SSOLoginState=1470588562; un=xiehao3692@vip.qq.com; wvr=6";
		cookiestr = cookiestr.replaceAll(" ", "");
		String[] cookies = cookiestr.split(";");
		for (String cookie : cookies) {
			String[] singleCookie = cookie.split("=");
			System.out.println(singleCookie[0] + "=" + singleCookie[1]);
		}
	}

	@Test
	public void allTest () throws IOException, HttpClientException, InterruptedException, java.util.concurrent.TimeoutException {
		String url = "http://192.168.2.65:8000/";
		InputStream inputStream = new FileInputStream(new File("src/test/resources/2.jpg"));
		String base64 = ImageUtil.ImageToBase64(inputStream);
		inputStream.close();
		inputStream = new FileInputStream(new File("src/test/resources/2.jpg"));
		ImageInfo imageInfo = ImageUtil.ReadImageToInfo(inputStream);
		System.out.println(base64);
		System.out.println(imageInfo);
		Map<String, String> datas = new HashMap<>();
		datas.put("pic", base64);
		datas.put("width", imageInfo.getWidth() + "");
		datas.put("high", imageInfo.getHeigt() + "");
		HttpClient httpClient = HttpClientBuilder.custom().setUrl(url).setFormDatas(datas).build();
		System.out.println(httpClient.post());
	}
}
