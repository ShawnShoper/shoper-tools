package org.shoper.http;

import org.junit.Test;
import org.shoper.http.exception.HttpClientException;
import org.shoper.http.httpClient.HttpClient;

import java.util.concurrent.TimeUnit;

import static org.shoper.http.httpClient.HttpClient.HttpClientBuilder.getInstances;

public class HttpClientTest
{
	@Test
	public void HttpClient() throws HttpClientException
	{
		HttpClient httpClient = HttpClient.HttpClientBuilder
				.getInstances("http://httpbin.org/", "utf-8",
						TimeUnit.MILLISECONDS.toMillis(1000));
		System.out.println(httpClient.getHtml());
	}

}
