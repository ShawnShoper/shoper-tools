import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;

import java.io.IOException;

public class HttpClientTest {

    public static void main(String args[]) {

        try {

            HttpClient httpclient = new DefaultHttpClient();
            //Secure Protocol implementation.
//			SSLContext ctx = SSLContext.getInstance("SSL");
//                        //Implementation of a trust manager for X509 certificates
//			X509TrustManager tm = new X509TrustManager() {
//
//				public void checkClientTrusted(X509Certificate[] xcs,
//						String string) throws CertificateException {
//
//				}
//
//				public void checkServerTrusted(X509Certificate[] xcs,
//						String string) throws CertificateException {
//				}
//
//				public X509Certificate[] getAcceptedIssuers() {
//					return null;
//				}
//			};
//			ctx.init(null, new TrustManager[] { tm }, null);
//			SSLSocketFactory ssf = new SSLSocketFactory(ctx);
//			ClientConnectionManager ccm = httpclient.getConnectionManager();
//                        //register https protocol in httpclient's scheme registry
//			SchemeRegistry sr = ccm.getSchemeRegistry();
//			sr.register(new Scheme("https", 443, ssf));
            HttpGet httpget = new HttpGet("https://login.weibo.cn/login/?");
//			HttpParams params = httpclient.getParams();
//			params.setParameter("param1", "paramValue1");
//			httpget.setParams(params);
//			System.out.println("REQUEST:" + httpget.getURI());
            ResponseHandler responseHandler = new BasicResponseHandler();
            String responseBody;
            responseBody = (String) httpclient.execute(httpget, responseHandler);
            org.jsoup.nodes.Document parse = Jsoup.parse(responseBody);
            String action = parse.getElementsByTag("form").get(0).attr("action");
            System.out.println(action);
            // Create a response handler
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}


