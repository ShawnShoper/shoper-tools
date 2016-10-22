package org.shoper.http;

import com.alibaba.fastjson.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.shoper.commons.StringUtil;
import org.shoper.http.apache.HttpClient;
import org.shoper.http.apache.HttpClientBuilder;
import org.shoper.http.exception.HttpClientException;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * Created by ShawnShoper on 16/9/27.
 */
public class Bigdata_palmyou {
	private String url = "http://bigdata.palmyou.com/ntsms/homeSearchTeam.action?coptype=%ct%&head=%head%&carrivestate=&bgndate=%bd%&enddate=%ed%&currentPage=%page%";
	private LinkedBlockingQueue<List<Map<String, String>>> listData = new LinkedBlockingQueue<>(
			10);
	private String rootUrl = "http://bigdata.palmyou.com/";
	private AtomicBoolean list_over = new AtomicBoolean(false);
	private String cookie;
	private String category;
	private String type;
	private String fileName;

	public void run () throws InterruptedException {
		cookie = "JSESSIONID=ADD0B06C94C8816EED915B731B7461A1; usertype=0; typename=\"\"; ccode=lxsdyc; remember=true; DWRSESSIONID=78ZBXkgv8atG1HwdjcYQIIriHtl; aliyungf_tc=AQAAAAIZE2GEEAAAi1dVRXL2HKGJmjgD; JSESSIONID=15B8DC43F8762A9BEDA2EB73CC1CD98C; Hm_lvt_1b1f059ea2c708661a7c62b98791ca71=1475028930; Hm_lpvt_1b1f059ea2c708661a7c62b98791ca71=1475028930; Hm_lvt_8fed1432e6c3c3c8f80a8016890c19bf=1474973638; Hm_lpvt_8fed1432e6c3c3c8f80a8016890c19bf=1475111123";
		{
			category = "国内接待";
			type = "国内游接待团查询";
			fileName = "domesticTeamRevView";
		}
//		{
//			category = "出境";
//			type = "出境游组团查询";
//			fileName = "outboundTeamView";
//		}
//		{
//			category = "入境";
//			type = "入境游接待查询";
//			fileName = "inboundTeamView";
//		}

//		{
//			category = "国内组团";
//			type = "国内游组团查询";
//			fileName = "domesticTeamOrgView";
//		}
		CountDownLatch cdl = new CountDownLatch(2);
		Thread fetchList = new Thread(() -> {
			try {
				fetchList();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				list_over.set(true);
				cdl.countDown();
			}
		});
		fetchList.start();
		Thread fetchDetail = new Thread(() -> {
			try {
				fetchDetail();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				cdl.countDown();
			}
		});
		fetchDetail.start();
		cdl.await();
	}

	public static void main (String[] args) throws InterruptedException, FileNotFoundException {
		new Bigdata_palmyou().run();
	}

	private void fetchDetail () throws InterruptedException, IOException {
		FileOutputStream fos = new FileOutputStream("/Users/ShawnShoper/Desktop/palmyou/" + fileName);
		for (; ; ) {
			if (listData.isEmpty() && list_over.get()) break;
			List<Map<String, String>> datas;
			while (Objects.isNull(datas = listData.poll(1, TimeUnit.SECONDS))) ;
			datas.stream().map(this::parseTableData).forEach(e -> {
																			try {
																				fos.write(e.toJSONString().getBytes());
																			} catch (FileNotFoundException e1) {
																				e1.printStackTrace();
																			} catch (IOException e1) {
																				e1.printStackTrace();
																			}
																		}
			);
		}
		fos.flush();
		fos.close();
	}


	public JSONObject parseTableData (Map<String, String> map) {
		System.out.println(map.get("link"));
		JSONObject data = new JSONObject();
		data.put("id", map.get("id"));
		data.put("url", map.get("link"));
		String link = map.get("link");
		try {
			Map<String, String> headers = new HashMap<>();
			headers.put("Cookie", cookie);
			HttpClient httpClient = HttpClientBuilder.custom().setUrl(link).build();
			httpClient.setRequestHeader(headers);
			Document document = null;
			try {
				document = httpClient.getDocument();
			} catch (HttpClientException e) {
				e.printStackTrace();
			} catch (TimeoutException e) {
				e.printStackTrace();
			}
			if (Objects.isNull(document)) return data;
			Elements baseinfo2s = document.getElementsByClass("baseInfor2");
			//团队基本信息
			if (Objects.nonNull(baseinfo2s) && !baseinfo2s.isEmpty()) {
				Element ele = baseinfo2s.get(0);
				Elements trs = ele.getElementsByTag("tr");
				trs.stream().forEach(e -> {
					Elements child = e.children();
					for (int i = 0; i < child.size(); i += 2) {
						String key = child.get(i).text();
						if (StringUtil.isEmpty(key)) continue;
						String value = child.get(i + 1).text();
						data.put(key, value);
					}
				});
			}
			//导游信息
			Elements baseinfos = document.getElementsByClass("baseInfor");
			if (Objects.nonNull(baseinfos) && !baseinfos.isEmpty()) {
				Element ele = baseinfos.get(0);
				Elements trs = ele.getElementsByTag("tr");
				trs.stream().forEach(e -> {
					Elements child = e.children();
					for (int i = 0; i < child.size(); i += 2) {
						String key = child.get(i).text();
						if (StringUtil.isEmpty(key.trim())) continue;
						if (i + 1 >= child.size()) break;
						String value = child.get(i + 1).text();
						data.put(key, value);
					}
				});
			}
			List<Map<String, Object>> routes = new ArrayList<>();
			Element routeList = document.getElementById("routeList");
			if (Objects.nonNull(routeList)) {
				Elements trs = routeList.getElementsByTag("tr");
				if (Objects.nonNull(trs) && !trs.isEmpty()) {
					for (Element t : trs) {
						Map<String, Object> route = new HashMap<>();
						Elements tds = t.getElementsByTag("td");
						route.put("day", tds.get(0).text());
						route.put("station", tds.get(1).text());
						route.put("destCity", tds.get(2).text());
						route.put("destPcity", tds.get(3).text());
						if (tds.size()==7) {
							route.put("receptionTravelAgency",tds.get(4).text());
							route.put("scenicSpot", tds.get(5).text());
							Element emt = tds.get(6).child(0);
							if ("disabled".equals(emt.attr("disabled")))
								route.put("scenicSpot", false);
							else
								route.put("scenicSpot", true);
						}else{
							route.put("scenicSpot", tds.get(4).text());
							Element emt = tds.get(5).child(0);
							if ("disabled".equals(emt.attr("disabled")))
								route.put("scenicSpot", false);
							else
								route.put("scenicSpot", true);
						}
						routes.add(route);
					}
				}
			}
			data.put("routes", routes);
			//游客信息
			Element guestList = document.getElementById("guestList");
			List<Map<String, Object>> guests = new ArrayList<>();
			if (Objects.nonNull(guestList)) {
				Elements trs = guestList.getElementsByTag("tr");
				if (Objects.nonNull(trs) && !trs.isEmpty()) {
					for (Element t : trs) {
						Map<String, Object> guest = new HashMap<>();
						Elements tds = t.getElementsByTag("td");
						//common

						if ("inboundTeamView".equals(fileName)) {
							guest.put("name", tds.get(1).text());
							guest.put("gender", tds.get(2).text());
							guest.put("birthday", tds.get(3).text());
							guest.put("IDType", tds.get(4).text());
							guest.put("IDCode", tds.get(5).text());
						} else {
							guest.put("name", tds.get(1).text());
							guest.put("pinyin_familyname", tds.get(2).text());
							guest.put("pinyin_lastname", tds.get(3).text());
							if ("domesticTeamOrgView".equals(fileName) || "domesticTeamRevView".equals(fileName)) {
								guest.put("IDType", tds.get(4).text());
								guest.put("IDCode", tds.get(5).text());
								guest.put("gender", tds.get(6).text());
								guest.put("birthday", tds.get(6).text());
								guest.put("tel", tds.get(6).text());
							} else if ("outboundTeamView".equals(fileName)) {
								guest.put("genger", tds.get(4).text());
								guest.put("birthday", tds.get(5).text());
								guest.put("birthplace", tds.get(6).text());
								guest.put("tel", tds.get(7).text());
								guest.put("IDType", tds.get(8).text());
								guest.put("IDCode", tds.get(9).text());
								guest.put("issuePlace", tds.get(10).text());
								guest.put("issueDate", tds.get(11).text());
							}
						}
						guests.add(guest);
					}
				}
			}
			data.put("guests", guests);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return data;
	}

	public void fetchList () throws InterruptedException {
		int page = 0;

		LocalDate now = LocalDate.now();
		int yy = now.getYear();
		int mm = now.getMonth().getValue();
		int dd = now.getDayOfMonth();
		int total = 0;
		String cd = yy + "-" + mm + "-" + dd;
		String query = url.replace("%ct%", category).replace("%head%", type).replace("%bd%", cd).replace("%ed%", cd);
		for (; ; ) {
			++page;
			String queryUrl = query.replace("%page%", page + "");
			System.out.println("access url :" + queryUrl);
			Map<String, String> headers = new HashMap<>();
			headers.put("Cookie", cookie);
			Document document = null;
			try {
				HttpClient
						httpClient = HttpClientBuilder.custom().setUrl(queryUrl).build();
				httpClient.setRequestHeader(headers);
				document = httpClient.getDocument();
				if(page ==1){
					total = Integer.parseInt(document.getElementsByClass("STYLE1").get(0).text().replaceAll("/",""));
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			} catch (HttpClientException e1) {
				e1.printStackTrace();
			} catch (TimeoutException e) {
				e.printStackTrace();
			}
			if (Objects.isNull(document)) continue;
			Element list_table = document.getElementById("listTable");
			if (Objects.isNull(list_table)) continue;
			Elements trs = list_table.getElementsByTag("tr");
			List<Map<String, String>> list = trs.stream().filter(e -> e.hasAttr("align")).map(e -> {
				Map<String, String> map = new HashMap<>();
				Element a = e.child(0).child(0);
				String link = a.attr("href");
				String odd = a.text();
				map.put("link", rootUrl + link);
				map.put("id", odd);
				return map;
			}).collect(Collectors.toList());
			while (!listData.offer(list, 1, TimeUnit.SECONDS)) ;
			if(page>=total)return;
		}
	}


}
