package com.briefsuits.httptools.http.invoker;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;


public abstract class AbstractHttpClientInvoker implements IHttpClientInvoker {
	
	@Override
	public HttpResponse sendRequest(String url, Object paramObj, Map<String, String> headerMap) {
		
		HttpUriRequest request = constructRequest(url, paramObj);
		
		if (headerMap != null && headerMap.size() > 0) {
			Iterator<String> it = headerMap.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				String value = headerMap.get(key);
				if (StringUtils.isNotBlank(value)) {
					request.setHeader(key, value);
				}
			}
		}
		
		return HttpClientPoolUtil.execute(request);
	}
	
	protected abstract HttpUriRequest constructRequest(String url, Object paramObj);
}
