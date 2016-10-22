package org.shoper.http;

import org.shoper.http.apache.HttpClient;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ShawnShoper on 16/9/14.
 */
public class DownLoadImg {
	public static void main (String[] args) throws InterruptedException {
		ExecutorService executorService = Executors.newFixedThreadPool(100);
		for (int i = 0; i < 500; i++) {
			executorService.submit(() -> {
				try {
					HttpClient httpClient = null;
					//http://s.weibo.com/ajax/pincode/pin?type=sass&ts=1475140503 新浪微博
					//http://bigdata.palmyou.com/ntsms/captcha.jsp?d 金棕榈
					org.shoper.http.httpClient.HttpClient httpClient1 = new org.shoper.http.httpClient.HttpClient("http://s.weibo.com/ajax/pincode/pin?type=sass&ts=" + System.currentTimeMillis());
					InputStream inputStream = httpClient1.getInputStream();
					FileOutputStream out = new FileOutputStream("/Users/ShawnShoper/Desktop/img/" + System.currentTimeMillis() + ".jpg");
					byte[] a = new byte[1024];
					int len = 0;
					while ((len = inputStream.read(a)) > 0) {
						out.write(a, 0, len);
						System.out.println(len);
					}
					out.close();
					inputStream.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			});

		}

	}
}
