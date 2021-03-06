package com.briefsuits.httptools;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.briefsuits.httptools.constants.HttpRequestType;
import com.briefsuits.httptools.http.invoker.IHttpClientInvoker;
import com.briefsuits.httptools.httpasync.invoker.HttpClientAsyncGet;
import com.briefsuits.httptools.httpasync.invoker.HttpClientAsyncPost;
import com.briefsuits.httptools.utils.CommonUtil;
import com.briefsuits.httptools.utils.StackTraceUtil;

public class HttpAsyncUtils {

	private static final Logger log = LoggerFactory.getLogger(HttpAsyncUtils.class);

	private static final ObjectMapper MAPPER = new ObjectMapper();

	private static final int MAX_TRY = 1;
	
	public static String sendRequest(HttpRequestType type, String url, Object param) {
		return HttpAsyncUtils.execute(type, url, param, null); 
	}
	
	public static <T> T sendRequest(HttpRequestType type, String url, Object param, Class<T> retClz) {
		return HttpAsyncUtils.sendRequest(type, url, param, retClz, null);
	}
	
	public static <T> T sendRequest(HttpRequestType type, String url, Object param, Class<T> retClz, Map<String, String> headerMap) {
		
		String result = HttpAsyncUtils.execute(type, url, param, headerMap); 
		
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
				invoker = new HttpClientAsyncPost();
			} else if (HttpRequestType.GET == type) {
				invoker = new HttpClientAsyncGet();
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
