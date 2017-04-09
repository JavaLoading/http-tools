package com.briefsuits.httptools.http.invoker;

import java.util.Map;

import org.apache.http.HttpResponse;

public interface IHttpClientInvoker {
	public HttpResponse sendRequest(String url, Object paramObj, Map<String, String> headerMap);
}
