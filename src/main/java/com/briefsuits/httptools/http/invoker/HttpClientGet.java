package com.briefsuits.httptools.http.invoker;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

public class HttpClientGet extends AbstractHttpClientInvoker {

	@SuppressWarnings("unchecked")
	@Override
	protected HttpUriRequest constructRequest(String url, Object param) {
		StringBuilder urlBuilder = new StringBuilder(url);
		
		// 构造 get url
		Map<String, String> paramMap = (Map<String, String>) param;
		if (paramMap != null && !paramMap.isEmpty()) {
			Set<String> keySet = paramMap.keySet();
			Iterator<String> it = keySet.iterator();
			
			String firstKey = it.next();
			
			urlBuilder.append("?").append(firstKey).append("=").append(paramMap.get(firstKey));
			
			while (it.hasNext()) {
				String key = it.next();
				urlBuilder.append("&").append(key).append("=").append(paramMap.get(key));
			}
		}
		
		HttpGet get = new HttpGet(urlBuilder.toString());
		
		return get;
	}

}
