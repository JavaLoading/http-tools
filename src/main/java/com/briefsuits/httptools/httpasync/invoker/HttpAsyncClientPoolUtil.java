package com.briefsuits.httptools.httpasync.invoker;

import java.util.concurrent.Future;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HttpAsyncClientPoolUtil {

	private static final Logger logger = LoggerFactory.getLogger(HttpAsyncClientPoolUtil.class);
	
	private static CloseableHttpAsyncClient httpclient = null;

	static {
		try {
			ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor();
			PoolingNHttpClientConnectionManager cm = new PoolingNHttpClientConnectionManager(ioReactor);
			cm.setMaxTotal(400);
			cm.setDefaultMaxPerRoute(200);
			
			httpclient = HttpAsyncClients.custom()
			        .setConnectionManager(cm)
			        .build();
			
			httpclient.start();
			
		} catch (IOReactorException e) {
			e.printStackTrace();
		}

	}

	public static CloseableHttpAsyncClient getHttpClient() {
		return httpclient;
	}
	
	public static Future<HttpResponse> execute(HttpUriRequest request) {

		try {
			return httpclient.execute(request, new FutureCallback<HttpResponse>() {
				@Override
				public void failed(Exception ex) {
					ex.printStackTrace();
					logger.error("httpclient async eror:" + ex.getMessage());
				}
				
				@Override
				public void completed(HttpResponse result) {
					logger.debug("httpclient async completed:" + result.getStatusLine().getStatusCode());
				}
				
				@Override
				public void cancelled() {
					logger.error("httpclient async cancelled");
				}
			});
		} catch (Exception e) {
			request.abort();
			logger.error("httpclient pool error", e);
		}
		return null;
	}
}
