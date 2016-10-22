package org.shoper.http.apache.handle;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.shoper.commons.StringUtil;
import org.shoper.http.apache.HttpClient;
import org.shoper.http.exception.UnHandleException;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

/**
 * Created by ShawnShoper on 16/9/12.
 */
public class DefaultResponseHandler extends ResponseHandler {
}
