package com.briefsuits.httptools.http.invoker;

import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.briefsuits.httptools.utils.StackTraceUtil;

public class HttpClientPut extends AbstractHttpClientInvoker {

	private static final Logger log = LoggerFactory.getLogger(HttpClientPut.class);

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	protected HttpUriRequest constructRequest(String url, Object param) {
		HttpPut put = new HttpPut(url);
		if (param != null) {
			StringEntity entity = null;
			try {
				String jsonParam = objectMapper.writeValueAsString(param);
				entity = new StringEntity(jsonParam, "UTF-8");
			} catch (Exception e) {
				log.error("httpclient put exception,{}", StackTraceUtil.getStackTrace(e));
			}
			put.setEntity(entity);
		}
		put.setHeader("Content-type", "application/json");
		return put;
	}

}
