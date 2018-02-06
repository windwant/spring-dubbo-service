package org.windwant.spring.util;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.Map.Entry;

/**
 * Created by  on 2016/7/18.
 */
public class HttpUtils {
    public static final String CONTENT_TYPE_JSON = "application/json";

    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    private static PoolingHttpClientConnectionManager pconnMgr = null;

    static {
        pconnMgr = new PoolingHttpClientConnectionManager();
        pconnMgr.setMaxTotal(200);
        pconnMgr.setDefaultMaxPerRoute(100);
        SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(10 * 1000).build();//设置读取超时时间
        pconnMgr.setDefaultSocketConfig(socketConfig);

    }

    private HttpUtils(){}

    public static CloseableHttpClient getCloseableHttpClient(int timeout) throws ConfigurationException {
        HttpClientBuilder httpClientBuilder = HttpClients.custom();
//        timeout = timeout == 0?5:timeout;
//        httpClientBuilder.setConnectionTimeToLive(timeout, TimeUnit.SECONDS).
        httpClientBuilder.setConnectionManager(pconnMgr).
                setConnectionManagerShared(true);
        httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler());

        return httpClientBuilder.build();
    }

    private static void closeHttp(CloseableHttpClient client, HttpPost httpPost, HttpGet httpGet, CloseableHttpResponse response){
        try {
            if(client != null) {
                client.close();
            }
            if(httpPost != null) {
                httpPost.releaseConnection();
            }
            if(httpGet != null) {
                httpGet.releaseConnection();
            }
            if(response != null) {
                response.close();
            }
        } catch (IOException e) {
            logger.error("post error: {}", e);
        }
    }

    /**
     * 通过流 http post
     * @param url 请求url
     * @param param 请求参数
     */
    public static String getResponseByHttpStream(String url,String param){
        HttpURLConnection http;
        BufferedWriter out = null;
        try{
            //参数转码
            URL ur = new URL(url);
            http = (HttpURLConnection)ur.openConnection();
            http.setConnectTimeout(10000);
            http.setRequestProperty("content-type", "text/html");
            http.setRequestMethod("POST");// 设定请求的方法为"POST"
            http.setDoInput(true);// 设置是否从httpUrlConnection读入
            // 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在 http正文内，因此需要设为true, 默认情况下是false;
            http.setDoOutput(true);
            http.setUseCaches(false);
            http.connect();
            out = new BufferedWriter(new OutputStreamWriter(http.getOutputStream(), "UTF-8"));
            out.write(param);
            out.flush();
            //接收返回数据
            InputStream is = http.getInputStream();
            byte [] b = new byte[1024];
            int c;
            StringBuilder ret = new StringBuilder();
            while((c=is.read(b))!= -1){
                ret.append(new String(b,0,c,"UTF-8"));
            }
            return ret.toString();
        }catch(Exception e){
            logger.error("getResponseByHttpStream ex",e);
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
                logger.error("resource close error:{}", e);
            }
        }
        return null;
    }

    public static String urlParamGet(String url, int timeout){
        CloseableHttpClient closeableHttpClient = null;
        HttpGet hp = null;
        CloseableHttpResponse closeableHttpResponse = null;
        try {
            closeableHttpClient = getCloseableHttpClient(timeout);
            hp = new HttpGet(url);

            logger.info("get request: {}", hp.toString());
            closeableHttpResponse = closeableHttpClient.execute(hp);
            if(closeableHttpResponse != null && closeableHttpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                String result = EntityUtils.toString(closeableHttpResponse.getEntity());
                logger.info("response entity: {}", result);
                return result;
            }
        } catch (Exception e) {
            logger.error("get error: {}", e);
        } finally {
            closeHttp(closeableHttpClient, null, hp, closeableHttpResponse);
        }
        return null;
    }

	/**
     * <pre>
     * 发送带参数的POST的HTTP请求
     * </pre>
     *
     * @param reqUrl     HTTP请求URL
     * @param parameters 参数映射表
     * @return HTTP响应的字符串
     */
    public static String doPost(String reqUrl, Map parameters, String recvEncoding) {
        HttpURLConnection url_con = null;
        String responseContent = null;
        try {
            StringBuffer params = new StringBuffer();
            for (Iterator iter = parameters.entrySet().iterator(); iter.hasNext(); ) {
                Entry element = (Entry) iter.next();
                params.append(element.getKey().toString());
                params.append("=");
                params.append(URLEncoder.encode(element.getValue().toString(),
                		"UTF-8"));
                params.append("&");
            }

            if (params.length() > 0) {
                params = params.deleteCharAt(params.length() - 1);
            }

            URL url = new URL(reqUrl);
            url_con = (HttpURLConnection) url.openConnection();
            url_con.setRequestMethod("POST");
             url_con.setConnectTimeout(5000);//（单位：毫秒）jdk 1.5换成这个,连接超时
             url_con.setReadTimeout(5000);//（单位：毫秒）jdk 1.5换成这个,读操作超时
            url_con.setDoOutput(true);
            byte[] b = params.toString().getBytes();
            url_con.getOutputStream().write(b, 0, b.length);
            url_con.getOutputStream().flush();
            url_con.getOutputStream().close();

            InputStream in = url_con.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(in,
                    recvEncoding));
            String tempLine = rd.readLine();
            StringBuffer tempStr = new StringBuffer();
            String crlf = System.getProperty("line.separator");
            while (tempLine != null) {
                tempStr.append(tempLine);
                tempStr.append(crlf);
                tempLine = rd.readLine();
            }
            responseContent = tempStr.toString();
            rd.close();
            in.close();
        } catch (IOException e) {
        	logger.info("网络故障" + e);
        } finally {
            if (url_con != null) {
                url_con.disconnect();
            }
        }
        return responseContent;
    }

}