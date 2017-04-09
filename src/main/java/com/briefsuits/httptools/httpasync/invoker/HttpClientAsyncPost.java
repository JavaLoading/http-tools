package com.briefsuits.httptools.httpasync.invoker;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.briefsuits.httptools.utils.StackTraceUtil;

public class HttpClientAsyncPost extends AbstractHttpClientAsyncInvoker {
	private static final Logger log = LoggerFactory.getLogger(HttpClientAsyncPost.class);
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	protected HttpUriRequest constructRequest(String url, Object param) {
		HttpPost post = new HttpPost(url);
		post.setHeader("Content-type", "application/json");

		if (param != null) {

			StringEntity entity = null;
			try {
				String jsonParam = objectMapper.writeValueAsString(param);

				entity = new StringEntity(jsonParam, "UTF-8");
			} catch (Exception e) {
				log.error("httpclient post exception,{}",StackTraceUtil.getStackTrace(e));
			}

			post.setEntity(entity);
		}

		return post;
	}

}
