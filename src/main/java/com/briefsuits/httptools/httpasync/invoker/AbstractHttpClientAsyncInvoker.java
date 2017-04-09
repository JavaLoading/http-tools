package com.briefsuits.httptools.httpasync.invoker;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;

import com.briefsuits.httptools.http.invoker.IHttpClientInvoker;


public abstract class AbstractHttpClientAsyncInvoker implements IHttpClientInvoker {
	
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
		
		Future<HttpResponse> respFuture = HttpAsyncClientPoolUtil.execute(request);
		
		try {
			HttpResponse resp = respFuture.get();
			return resp;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	protected abstract HttpUriRequest constructRequest(String url, Object paramObj);
}
