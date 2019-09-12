package com.kevin.base.common.utils;

import com.kevin.base.common.consts.Consts;
import com.kevin.base.common.exception.RemoteCallException;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description: 处理系统对外HTTP调用的工具类.
 * @Author: kevin
 * @Date: 2019/9/12 10:18
 */
public class HttpUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtils.class);

    private static final RequestConfig config = RequestConfig.custom().setConnectTimeout(60000).setSocketTimeout(15000)
            .build();

    /**
     * 发送HTTP GET请求到指定URL
     * @param url 请求的URL
     * @throws RemoteCallException
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static void doGet(String url) throws RemoteCallException {
        doGet(url, null, null);
    }

    /**
     * 发送HTTP GET请求到指定URL，传入的参数会以URL参数对的方式追加在url之后
     * @param url    请求的URL
     * @param params 参数信息
     * @throws RemoteCallException
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static void doGet(String url, Map<String, String> params) throws RemoteCallException {
        doGet(url, params, null);
    }

    /**
     * 发送HTTP GET请求到指定URL，传入的参数会以URL参数对的方式追加在url之后，需要传入身份token,key:Authorization,value:bearer +token,注意bearer后面需要有空格
     * @param url    请求的URL
     * @param params 参数信息
     * @throws RemoteCallException
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static void doGet(String url, Map<String, String> params, Map<String, String> headerMap)
            throws RemoteCallException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
        HttpGet httpGet = null;
        try {
            if (params != null && !params.isEmpty()) {
                List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    Object value = entry.getValue();
                    if (value != null) {
                        pairs.add(new BasicNameValuePair(entry.getKey(), value.toString()));
                    }
                }
                String seperator = (url.indexOf("?") == -1) ? "?" : "&";
                url += seperator + EntityUtils.toString(new UrlEncodedFormEntity(pairs, Consts.UTF8));
            }
            LOGGER.info("doGet url:" + url);

            httpGet = new HttpGet(url);
            httpGet.addHeader("content-type", "application/json;charset=UTF-8");
            if (headerMap != null && !headerMap.isEmpty()) {
                for (String key : headerMap.keySet()) {
                    httpGet.addHeader(key, headerMap.get(key));
                }
            }
            CloseableHttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                LOGGER.error("do get exception,the url is:" + url);
                httpGet.abort();
                throw new RemoteCallException("HttpClient get error status code :" + statusCode);
            }
            LOGGER.info("do get call success, the url is:" + url);
        } catch (Exception e) {
            LOGGER.error("do post exception,the url is:" + url, e);
            throw new RemoteCallException(e.getMessage(), e);
        } finally {
            if (httpGet != null) {
                httpGet.releaseConnection();
            }
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                LOGGER.error("close client exception", e);
            }
        }
    }

    /**
     * 通过HTTP POST方法发送body内容至指定URL
     * @param url  目标URL
     * @param body 发送的内容
     * @throws RemoteCallException
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static void doPost(String url, String body) throws RemoteCallException {
        doPost(url, body, null);
    }

    /**
     * 通过HTTP POST方法发送body内容至指定URL
     * @param url       目标URL
     * @param body      发送的内容
     * @param headerMap 头部参数
     * @throws RemoteCallException
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static void doPost(String url, String body, Map<String, String> headerMap) throws RemoteCallException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();

        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("content-type", "application/json;charset=UTF-8");
        if (headerMap != null && !headerMap.isEmpty()) {
            for (String key : headerMap.keySet()) {
                httpPost.addHeader(key, headerMap.get(key));
            }
        }
        try {
            HttpEntity entity = new StringEntity(body, Consts.UTF8);
            httpPost.setEntity(entity);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                LOGGER.error("do post exception,the url is:" + url);
                httpPost.abort();
                throw new RemoteCallException("Http post error status code :" + statusCode);
            }
        } catch (Exception e) {
            LOGGER.error("do post exception,the url is:" + url, e);
            throw new RemoteCallException(e.getMessage(), e);
        } finally {
            if (httpPost != null) {
                httpPost.releaseConnection();
            }
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                LOGGER.error("close client exception", e);
            }
        }
    }

    /**
     * Client发送HTTP GET请求到指定URL
     * @param url 请求的URL
     * @throws RemoteCallException
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String doGetClient(String url) throws RemoteCallException {
        return doGetClient(url, null, null);
    }

    /**
     * Client发送HTTP GET请求到指定URL，传入的参数会以URL参数对的方式追加在url之后
     * @param url    请求的URL
     * @param params 参数信息
     * @throws RemoteCallException
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String doGetClient(String url, Map<String, String> params) throws RemoteCallException {
        return doGetClient(url, params, null);
    }

    /**
     * Client发送HTTP GET请求到指定URL，传入的参数会以URL参数对的方式追加在url之后,需要传入身份token,key:Authorization,value:bearer +token,注意bearer后面需要有空格
     * @param url       请求的URL
     * @param params    参数信息
     * @param headerMap 头部参数
     * @return
     * @throws RemoteCallException
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String doGetClient(String url, Map<String, String> params, Map<String, String> headerMap)
            throws RemoteCallException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
        HttpGet httpGet = null;
        InputStream inputStream = null;
        try {
            if (params != null && !params.isEmpty()) {
                List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    String value = entry.getValue();
                    if (value != null) {
                        pairs.add(new BasicNameValuePair(entry.getKey(), value));
                    }
                }
                String seperator = (url.indexOf("?") == -1) ? "?" : "&";
                url += seperator + EntityUtils.toString(new UrlEncodedFormEntity(pairs, Consts.UTF8));
            }
            LOGGER.info(" client http doGet url:" + url);

            httpGet = new HttpGet(url);
            httpGet.addHeader("content-type", "application/json;charset=UTF-8");
            if (headerMap != null && !headerMap.isEmpty()) {
                for (String key : headerMap.keySet()) {
                    httpGet.addHeader(key, headerMap.get(key));
                }
            }
            CloseableHttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                LOGGER.error(" Client http do get exception,the url is:" + url);
                httpGet.abort();
                throw new RemoteCallException("HttpClient get error status code :" + statusCode);
            }
            LOGGER.debug(" Client do get call success, the url is:{}", url);
            inputStream = response.getEntity().getContent();
            String respStr = IOUtils.toString(inputStream, "UTF-8");
            LOGGER.debug("receive response content:{}", respStr);
            return respStr;
        } catch (Exception e) {
            LOGGER.error(" Client do get exception,the url is:" + url, e);
            throw new RemoteCallException(e.getMessage(), e);
        } finally {
            closeConnection(inputStream, httpClient, httpGet);
        }
    }

    /**
     * Client通过HTTP POST方法发送body内容至指定URL
     * @param url  目标URL
     * @param body 发送的内容
     * @throws RemoteCallException
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String doPostClient(String url, String body) throws RemoteCallException {
        return doPostClient(url, body, null);
    }

    /**
     * Client通过HTTP POST方法发送body内容至指定URL，需要传入身份token,key:Authorization,value:bearer +token,注意bearer后面需要有空格
     * @param url       目标URL
     * @param body      发送的内容
     * @param headerMap 头部信息
     * @throws RemoteCallException
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String doPostClient(String url, String body, Map<String, String> headerMap)
            throws RemoteCallException {
        InputStream inputStream = null;
        CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();

        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("content-type", "application/json;charset=UTF-8");
        if (headerMap != null && !headerMap.isEmpty()) {
            for (String key : headerMap.keySet()) {
                httpPost.addHeader(key, headerMap.get(key));
            }
        }
        try {
            HttpEntity entity = new StringEntity(body, Consts.UTF8);
            httpPost.setEntity(entity);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                LOGGER.error(" Client do post exception,the url is:" + url);
                httpPost.abort();
                throw new RemoteCallException("Http post error status code :" + statusCode);
            }
            inputStream = response.getEntity().getContent();
            String respStr = IOUtils.toString(inputStream, "UTF-8");
            LOGGER.info("receive response content:{}", respStr);
            return respStr;
        } catch (Exception e) {
            LOGGER.error(" Client do post exception,the url is:" + url, e);
            throw new RemoteCallException(e.getMessage(), e);
        } finally {
            closeConnection(inputStream, httpClient, httpPost);
        }
    }

    /**
     * 下载文件
     * @param url          文件路径
     * @param outputStream 输出流
     */
    public static void downloadFile(String url, OutputStream outputStream) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
        HttpGet httpGet = null;
        InputStream inputStream = null;
        try {
            httpGet = new HttpGet(url);
            httpGet.addHeader("content-type", "application/json;charset=UTF-8");
            CloseableHttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                LOGGER.error(" Client http download file exception,the url is:" + url);
                httpGet.abort();
                throw new RemoteCallException("HttpClient get error status code :" + statusCode);
            }
            LOGGER.debug(" Client download file call success, the url is:{}", url);
            inputStream = response.getEntity().getContent();
            IOUtils.copy(inputStream, outputStream);
        } catch (Exception e) {
            LOGGER.error(" Client download file exception,the url is:" + url, e);
            throw new RemoteCallException(e.getMessage(), e);
        } finally {
            closeConnection(inputStream, httpClient, httpGet);
        }
    }

    private static void closeConnection(InputStream inputStream, CloseableHttpClient httpClient, HttpRequestBase httpRequest) {
        try {
            if (inputStream != null) {
                IOUtils.closeQuietly(inputStream);
            }
            if (httpRequest != null) {
                httpRequest.releaseConnection();
            }
            if (httpClient != null) {
                httpClient.close();
            }
        } catch (IOException e) {
            LOGGER.error("close client exception", e);
        }
    }
}
