package com.fsmer.csip.util.http;

import com.alibaba.fastjson.JSON;
import com.fsmer.csip.entity.ai.RtspInfo;
import com.fsmer.csip.entity.ai.RtspRoot;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class HttpUtil {
	private static Logger  logger = LoggerFactory.getLogger(HttpUtil.class);

	public static String doPost(String url, Map<String, Object> param, String jsonParams)  {
		logger.info("请求用户中心的Url:{}",url);
		/*String jsonParams = "";
		if(param !=null ) {
			 jsonParams = JSONObject.toJSONString(param);

		}*/
		//创建HttpClient对象
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String resultString = "";
		CloseableHttpResponse httpResponse = null;
		try {
			// 创建HttpPost对象
			HttpPost httpPost = new HttpPost(url);
			 httpPost.addHeader("Content-Type", "application/json;charset=utf-8");
			 logger.info("head:"+httpPost.getAllHeaders());
			 Header[] headers = httpPost.getAllHeaders();
			 for (int i = 0; i < headers.length; i++) {
				logger.info("===============请求header:"+headers[i]);
			}
			 if(param!=null && !param.isEmpty()){
				List<BasicNameValuePair> params = new ArrayList<>();
				for(String key :param.keySet()){
					params.add(new BasicNameValuePair(key, param.get(key)==null?"":param.get(key).toString()));
				}
			}
			if(StringUtils.isNotBlank(jsonParams)) {
				httpPost.setEntity(new StringEntity(jsonParams,"UTF-8"));
			}
			//设置请求和传输超时时间
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(120000).setConnectTimeout(120000).build();
			httpPost.setConfig(requestConfig);

			// 开始执行http请求
			long startTime = System.currentTimeMillis();
			httpResponse = httpclient.execute(httpPost);
			long endTime = System.currentTimeMillis();

			// 获得响应状态码
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			logger.info("调用API花费时间(单位：毫秒)：" + (endTime - startTime));

			// 取出应答字符串
			HttpEntity httpEntity = httpResponse.getEntity();
			resultString = EntityUtils.toString(httpEntity, StandardCharsets.UTF_8);

			// 判断返回状态是否为200
			if (statusCode != HttpStatus.SC_OK) {
				throw new RuntimeException(String.format("\n\tStatus:%s\n\tError Message:%s", statusCode,resultString));
			}
			//对返回结果进行非空校验
			if (StringUtils.isBlank(resultString)|| StringUtils.equals(StringUtils.trim(resultString), "null")) {
				logger.error("被调用系统响应参数：{}",resultString);
				resultString = "{'code':100005,'msg':'响应不合法！'}";
			}
		} catch (ClientProtocolException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e){
			logger.error(e.getMessage(), e);
		}finally{
			try {
				if(httpResponse != null){
					httpResponse.close();
				}
				httpclient.close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return resultString;
	}

	public static String doGet(String url, Map<String, String> param){
		//创建HttpClient对象
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String resultString = "";
		CloseableHttpResponse httpResponse = null;
		try {
			//创建uri
			URIBuilder builder = new URIBuilder(url);
			if(param!=null && !param.isEmpty()){
				for(String key :param.keySet()){
					builder.addParameter(key, param.get(key));
				}
			}
			URI uri = builder.build();
			// 创建httpGet请求
			HttpGet httpGet = new HttpGet(uri);

			// 开始执行http请求
			long startTime = System.currentTimeMillis();
			httpResponse = httpclient.execute(httpGet);
			long endTime = System.currentTimeMillis();

			// 获得响应状态码
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			logger.info("调用API花费时间(单位：毫秒)：" + (endTime - startTime));

			// 取出应答字符串
			HttpEntity httpEntity = httpResponse.getEntity();
			resultString = EntityUtils.toString(httpEntity, StandardCharsets.UTF_8);
			// 去掉返回结果中的"\r"字符，否则会在结果字符串后面显示一个小方格
			resultString.replaceAll("\r", "");

			// 判断返回状态是否为200
			if (statusCode != HttpStatus.SC_OK) {
				throw new RuntimeException(String.format("\n\tStatus:%s\n\tError Message:%s", statusCode,resultString));
			}
		} catch (ClientProtocolException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} catch (URISyntaxException e) {
			logger.error(e.getMessage(), e);
		} finally{
			try {
				if(httpResponse != null){
					httpResponse.close();
				}
				httpclient.close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return resultString;
	}
	public static CloseableHttpResponse get(String url, Map<String, String> param){
		//创建HttpClient对象
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String resultString = "";
		CloseableHttpResponse httpResponse = null;
		try {
			//创建uri
			URIBuilder builder = new URIBuilder(url);
			if(param!=null && !param.isEmpty()){
				for(String key :param.keySet()){
					builder.addParameter(key, param.get(key));
				}
			}
			URI uri = builder.build();
			// 创建httpGet请求
			HttpGet httpGet = new HttpGet(uri);

			// 开始执行http请求
			long startTime = System.currentTimeMillis();
			httpResponse = httpclient.execute(httpGet);
			long endTime = System.currentTimeMillis();

			// 获得响应状态码
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			logger.info("调用API花费时间(单位：毫秒)：" + (endTime - startTime));

			// 取出应答字符串
			HttpEntity httpEntity = httpResponse.getEntity();
			resultString = EntityUtils.toString(httpEntity, StandardCharsets.UTF_8);
			// 去掉返回结果中的"\r"字符，否则会在结果字符串后面显示一个小方格
			resultString.replaceAll("\r", "");

			// 判断返回状态是否为200
			if (statusCode != HttpStatus.SC_OK) {
				throw new RuntimeException(String.format("\n\tStatus:%s\n\tError Message:%s", statusCode,resultString));
			}
		} catch (ClientProtocolException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} catch (URISyntaxException e) {
			logger.error(e.getMessage(), e);
		} finally{
			try {
				if(httpResponse != null){
					httpResponse.close();
				}
				httpclient.close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return httpResponse;
	}
}
