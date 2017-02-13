package cn.sjjy.edu.account.common;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class EmailUtils {

    private static final CloseableHttpClient httpClient;
    public static final String CHARSET = "UTF-8";
    private static final String emailServiceHost = "notify.ymtech.info";

    static {
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(30000)
                .setSocketTimeout(15000)
                .build();
        httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(config)
                .build();
    }
    
    public static void main(String[] args) {
		sendDisplayAdsEmail("2894021461@qq.com", "test", "test");
	}
    
    private static void sendDisplayAdsEmail(String to, String subject, String htmlContent){
		final String emailUrl = "smtp.exmail.qq.com";
		final String from = "cuihaichuan@bilibili.com";
		final String key = "7cQKmUb1";
		final String app = "Native_Official";
		final String fromName = "YeahMobi Team";
		
		HashMap<String, String> emailParams = new HashMap<String, String>();
		emailParams.put("from", from);
		emailParams.put("fromName", fromName);
		emailParams.put("to",to);
		emailParams.put("subject", subject);
		emailParams.put("body", htmlContent);
		emailParams.put("app", app);
		long secondes = new Date().getTime()/1000;
		emailParams.put("time", secondes + "");
		emailParams.put("sign", DigestUtils.md5Hex(("Native_Official"+key+secondes).getBytes()).substring(0, 8).toLowerCase());
		
		try {
			String response = EmailUtils.httpPost(emailUrl, emailParams);
			System.out.println(response);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

    public static String httpGet(String url) {
        return httpGet(url, null, CHARSET);
    }

    public static String httpGet(String url, Map<String, String> params) {
        return httpGet(url, params, CHARSET);
    }

    public static String httpPost(String url) {
        return httpPost(url, null, CHARSET);
    }

    public static String httpPost(String url, Map<String, String> params) {
        return httpPost(url, params, CHARSET);
    }


    /**
     * @param url    url地址
     * @param params 参数,编码之前的参数
     * @return
     */
    public static String httpGet(String url, Map<String, String> params, String charset) {
        if (url == "") {
            return null;
        }
        try {
            if (params != null && !params.isEmpty()) {
                List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    String value = entry.getValue();
                    if (value != null) {
                        pairs.add(new BasicNameValuePair(entry.getKey(), value));
                    }
                }
                url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
            }
            HttpGet httpget = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(httpget);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                httpget.abort();
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null) {
                result = EntityUtils.toString(entity, CHARSET);
            }
            EntityUtils.consume(entity);
            response.close();
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param url    url地址
     * @param params 参数,编码之前的参数
     * @return
     */
    public static String httpPost(String url, Map<String, String> params, String charset) {
        if (url == "") {
            return null;
        }
        try {
            List<NameValuePair> pairs = null;
            if (params != null && !params.isEmpty()) {
                pairs = new ArrayList<NameValuePair>(params.size());
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    String value = entry.getValue();
                    if (value != null) {
                        pairs.add(new BasicNameValuePair(entry.getKey(), value));
                    }
                }
            }
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(pairs, charset);
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(urlEncodedFormEntity);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                httpPost.abort();
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null) {
                result = EntityUtils.toString(entity, CHARSET);
            }
            EntityUtils.consume(entity);
            response.close();
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}