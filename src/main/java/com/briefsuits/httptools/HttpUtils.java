package com.briefsuits.httptools;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.briefsuits.httptools.constants.HttpRequestType;
import com.briefsuits.httptools.http.invoker.HttpClientDelete;
import com.briefsuits.httptools.http.invoker.HttpClientGet;
import com.briefsuits.httptools.http.invoker.HttpClientPost;
import com.briefsuits.httptools.http.invoker.HttpClientPut;
import com.briefsuits.httptools.http.invoker.IHttpClientInvoker;
import com.briefsuits.httptools.utils.CommonUtil;
import com.briefsuits.httptools.utils.StackTraceUtil;

public class HttpUtils {

	private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);

	private static final ObjectMapper MAPPER = new ObjectMapper();

	private static final int MAX_TRY = 1;
	
	public static String sendRequest(HttpRequestType type, String url, Object param) {
		return HttpUtils.execute(type, url, param, null); 
	}
	
	public static <T> T sendRequest(HttpRequestType type, String url, Object param, Class<T> retClz) {
		return HttpUtils.sendRequest(type, url, param, retClz, null);
	}
	
	public static <T> T sendRequest(HttpRequestType type, String url, Object param, Class<T> retClz, Map<String, String> headerMap) {
		
		String result = HttpUtils.execute(type, url, param, headerMap); 
		
		if (StringUtils.isNotBlank(result)) {
			try {
				return MAPPER.readValue(result, retClz);
			} catch (IOException e) {
				e.printStackTrace();
				log.error("result parse error");
			}
		}
		
		return null;
	}

	private static String execute(HttpRequestType type, String url, Object param, Map<String, String> headerMap) {

		int redo = 0;

		HttpResponse resp = null;

		while (redo < MAX_TRY) {

			IHttpClientInvoker invoker = null;
			if (HttpRequestType.POST == type) {
				invoker = new HttpClientPost();
			} else if (HttpRequestType.GET == type) {
				invoker = new HttpClientGet();
			} else if (HttpRequestType.PUT == type) {
				invoker = new HttpClientPut();
			} else if (HttpRequestType.DELETE == type) {
				invoker = new HttpClientDelete();
			} else {
				log.error("error http request type");
				return null;
			}

			if (invoker != null) {
				try {
					String jsonParam = MAPPER.writeValueAsString(param);
					log.info("Req:" + "|" + url + "|" + jsonParam);
				} catch (Exception e) {
					log.error("HttpUtil.sendRequest param parse exception,{}", StackTraceUtil.getStackTrace(e));
				}

				resp = invoker.sendRequest(url, param, headerMap);
			}
			
			if (resp != null) {
				break;
			} else {
				redo++;
			}
		}

		if (resp != null) {
			String result = CommonUtil.paseResponse(resp);
			log.info("Res:" + result);

			return result;
		} else {
			log.error("http response is incorrect");
			return null;
		}
	}
}
