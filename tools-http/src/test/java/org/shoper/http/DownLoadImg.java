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
					String url = "http://weibo.cn/interface/f/ttt/captcha/show.php?cpt=2_b3c4341d0a2ea990";
					//http://s.weibo.com/ajax/pincode/pin?type=sass&ts=1475140503 新浪微博
					//http://bigdata.palmyou.com/ntsms/captcha.jsp?d 金棕榈
					//http://weibo.cn/interface/f/ttt/captcha/show.php?cpt=2_b0274b847fd2f538 新浪微博手机端
					org.shoper.http.httpClient.HttpClient httpClient1 = new org.shoper.http.httpClient.HttpClient(url);
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
