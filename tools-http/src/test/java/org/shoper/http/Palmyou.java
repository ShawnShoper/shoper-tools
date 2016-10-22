package org.shoper.http;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Collector;
import org.jsoup.select.Elements;
import org.jsoup.select.Evaluator;
import org.shoper.commons.StringUtil;
import org.shoper.http.apache.HttpClient;
import org.shoper.http.apache.HttpClientBuilder;
import org.shoper.http.apache.proxy.ProxyServerPool;
import org.shoper.http.exception.HttpClientException;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.jayway.jsonpath.JsonPath.parse;

/**
 * Created by ShawnShoper on 16/9/23.
 */
public class Palmyou {

	public static void main (String[] args) throws MalformedURLException, HttpClientException, InterruptedException, TimeoutException, FileNotFoundException {
		ProxyServerPool.importProxyServer(new File("/Users/ShawnShoper/Documents/IDEAWorkspace/shoper-tools/tools-http/src/main/resources/proxyip.ls"), Charset.forName("utf-8"));
//		{
//			Map<String, String> postData = new HashMap<>();
//			postData.put("callCount", "1");
//			postData.put("c0-scriptName", "Guest");
//			postData.put("windowName", "");
//			postData.put("c0-methodName", "guestEvaluationList");
//			postData.put("c0-id", "0");
//			postData.put("batchId", "1");
//			postData.put("instanceId", "0");
//			postData.put("page", "%2Fntsms%2FpageForward.action%3FpageName%3Dlargescreen%2FmonitorEvaluation");
//			postData.put("scriptSessionId", "vRHMhJFBf6mZxgAJVzK*XsNOktl/uiXSktl-8U5oMjsT3");
//			HttpClient httpClient = HttpClientBuilder.custom().setUrl("http://bigdata.palmyou.com/ntsms/dwr/call/plaincall/Guest.guestEvaluationList.dwr").setFormDatas(postData).build();
//			System.out.println(StringUtil.unicodeToStr(httpClient.post()));
//		}
//		{
//			HttpClient httpClient = HttpClientBuilder.custom().setUrl("http://data.palmyou.com/data/json/getGuidePriceData?callback=jQuery17206720149363396601_1474598646302&ctype=1&orderColumn=ddate%2Cnprice&orderSequence=%2B%2C%2B&_=1474598646855").build();
//			System.out.println(StringUtil.unicodeToStr(httpClient.post()));
//		}
//		{
//
//			Map<String, String> postData = new HashMap<>();
//			postData.put("overseas", "Outbound");
//			postData.put("sessionKey", "");
//			Map<String, String> headers = new HashMap<>();
//			headers.put("cookie", "JSESSIONID=CE304E492B83522733C0C5F04A910C5A; DWRSESSIONID=vRHMhJFBf6mZxgAJVzK*XsNOktl; aliyungf_tc=AQAAADwq6HKPagsAOdaXtp6MhMsmqj6p; JSESSIONID=599E8905F144E61EC2DF4DC346C0389F; Hm_lvt_1b1f059ea2c708661a7c62b98791ca71=1474596123; Hm_lpvt_1b1f059ea2c708661a7c62b98791ca71=1474596123; Hm_lvt_8fed1432e6c3c3c8f80a8016890c19bf=1474595731; Hm_lpvt_8fed1432e6c3c3c8f80a8016890c19bf=1474601198");
//			headers.put("Origin", "http://bigdata.palmyou.com");
//			headers.put("Host", "bigdata.palmyou.com");
//			headers.put("Referer", "http://bigdata.palmyou.com/ntsms/main/pageForward.action?pageName=largescreen/index&leaflets=1");
//			headers.put("Accept","*/*");
//			HttpClient httpClient = HttpClientBuilder.custom().setUrl("http://bigdata.palmyou.com/ntsms/homeGetOverseasData.action").setFormDatas(postData).setProxy(true).build();
//			httpClient.setRequestHeader(headers);
//			System.out.println(StringUtil.unicodeToStr(httpClient.post()));
//		}
		{
			Map<String,String> headers = new HashMap<>();
			headers.put("Cookie","JSESSIONID=ADD0B06C94C8816EED915B731B7461A1; usertype=0; typename=\"\"; ccode=lxsdyc; remember=true; DWRSESSIONID=78ZBXkgv8atG1HwdjcYQIIriHtl; aliyungf_tc=AQAAAAIZE2GEEAAAi1dVRXL2HKGJmjgD; JSESSIONID=15B8DC43F8762A9BEDA2EB73CC1CD98C; Hm_lvt_1b1f059ea2c708661a7c62b98791ca71=1475028930; Hm_lpvt_1b1f059ea2c708661a7c62b98791ca71=1475028930; Hm_lvt_8fed1432e6c3c3c8f80a8016890c19bf=1474973638; Hm_lpvt_8fed1432e6c3c3c8f80a8016890c19bf=1475111123");
			HttpClient httpClient = HttpClientBuilder.custom().setUrl("http://bigdata.palmyou.com/ntsms/homeGetScrollDestinationRankInfo.action").setCharset("utf-8").build();
			httpClient.setRequestHeader(headers);
			Document document = httpClient.getDocument();
			Elements divs = document.getElementsByTag("div");
			JSONObject all = new JSONObject();
			int i =0 ;
			for (Element div : divs){
				String text = div.getElementsByTag("span").get(0).text();
				Pattern pattern = Pattern.compile("\\d+ 、([\u2e80-\u9FFF]+)\\s(\\d+)\\s团\\s(\\d+)\\s人");
				Matcher matcher = pattern.matcher(text);
				JSONArray data = new JSONArray();
				while(matcher.find()){
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("city",matcher.group(1));
					jsonObject.put("group",matcher.group(2));
					jsonObject.put("people_count",matcher.group(3));
					data.add(jsonObject);
				}
				if(i==0)
					all.put("outsea",data);
				else
					all.put("china",data);
				i++;
			}
			System.out.println(all);
		}
	}
}
