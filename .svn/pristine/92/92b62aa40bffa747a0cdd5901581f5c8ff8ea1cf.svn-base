package org.shoper.http;

import org.shoper.http.httpClient.HttpClient;
import org.shoper.http.httpClient.HttpClient.HttpClientBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class HttpUtil_testHXK
{
	public static void main(String[] args) throws IOException,
			InterruptedException, ExecutionException, TimeoutException
	{
		HttpClient hc = HttpClientBuilder.getInstances(
				"http://192.168.2.55:8888/PFVA/login", "UTF-8", 20,
				TimeUnit.SECONDS, 3);
		Map<String, String> header = new HashMap<>();
		header.put("Content-Type", "text/plain");
		hc.setReqHeadersMap(header);
		Map<String, String> datas = new HashMap<>();
		datas.put("account", "admin");
		datas.put("password", "daqsoft");
		// JSONObject jsonObject = new JSONObject();
		// jsonObject.put("account", "admin");
		// jsonObject.put("password", "daqsoft");
		// System.out.println(jsonObject.toJSONString());
		System.out
				.println(hc.doPost(HttpClient.PostDataMapToStr(datas), false));
	}
}
