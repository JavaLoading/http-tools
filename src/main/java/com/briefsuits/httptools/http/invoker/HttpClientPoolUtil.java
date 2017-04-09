package com.briefsuits.httptools.http.invoker;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.briefsuits.httptools.constants.HttpPoolConfig;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;

public class HttpClientPoolUtil {

	private static final Logger logger = LoggerFactory.getLogger(HttpClientPoolUtil.class);

	/**
	 * 连接池里的最大连接数
	 */
	private static int MAX_TOTAL_CONNECTIONS = 800;

	/**
	 * 每个路由的默认最大连接数
	 */
	private static int MAX_ROUTE_CONNECTIONS = MAX_TOTAL_CONNECTIONS >> 1;

	/**
	 * 连接超时时间
	 */
	private static int CONNECT_TIMEOUT = 6000;

	/**
	 * 套接字超时时间
	 */
	private static int SOCKET_TIMEOUT = 8000;

	/**
	 * 连接池中 连接请求执行被阻塞的超时时间
	 */
	private static long CONN_MANAGER_TIMEOUT = 10000;

	/**
	 * http连接相关参数
	 */
	private static HttpParams parentParams;

	/**
	 * http线程池管理器
	 */
	private static PoolingClientConnectionManager cm;

	/**
	 * http客户端
	 */
	private static HttpClient httpClient;

	/**
	 * 初始化http连接池，设置参数、http头等等信息
	 */
	static {
		// init http pool params
		if (HttpPoolConfig.MAX_TOTAL_CONNECTIONS > 0) {
			MAX_TOTAL_CONNECTIONS = HttpPoolConfig.MAX_TOTAL_CONNECTIONS;
		}
		if (HttpPoolConfig.MAX_ROUTE_CONNECTIONS > 0) {
			MAX_ROUTE_CONNECTIONS = HttpPoolConfig.MAX_ROUTE_CONNECTIONS;
		}
		if (HttpPoolConfig.CONNECT_TIMEOUT > 0) {
			CONNECT_TIMEOUT = HttpPoolConfig.CONNECT_TIMEOUT;
		}
		if (HttpPoolConfig.SOCKET_TIMEOUT > 0) {
			SOCKET_TIMEOUT = HttpPoolConfig.SOCKET_TIMEOUT;
		}
		if (HttpPoolConfig.CONN_MANAGER_TIMEOUT > 0) {
			CONN_MANAGER_TIMEOUT = HttpPoolConfig.CONN_MANAGER_TIMEOUT;
		}
		
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
		schemeRegistry.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));
		cm = new PoolingClientConnectionManager(schemeRegistry);
		cm.setMaxTotal(MAX_TOTAL_CONNECTIONS);
		cm.setDefaultMaxPerRoute(MAX_ROUTE_CONNECTIONS);
		parentParams = new BasicHttpParams();
		parentParams.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		parentParams.setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
		parentParams.setParameter(ClientPNames.CONN_MANAGER_TIMEOUT, CONN_MANAGER_TIMEOUT);
		parentParams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECT_TIMEOUT);
		parentParams.setParameter(CoreConnectionPNames.SO_TIMEOUT, SOCKET_TIMEOUT);
		parentParams.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		parentParams.setParameter(ClientPNames.HANDLE_REDIRECTS, true);
		// 设置头信息,模拟浏览器
		Collection<Header> collection = new ArrayList<Header>();
		collection.add(new BasicHeader("User-Agent", "httptools/1.0.0"));
		collection.add(new BasicHeader("Accept-Language", "zh-cn,zh,en-US,en;q=0.5"));
		collection.add(new BasicHeader("Accept-Charset", "ISO-8859-1,utf-8,gbk,gb2312;q=0.7,*;q=0.7"));
		collection.add(new BasicHeader("Accept-Encoding", "gzip, deflate"));
		parentParams.setParameter(ClientPNames.DEFAULT_HEADERS, collection);
		httpClient = new DefaultHttpClient(cm, parentParams);
	}

	public static HttpClient getHttpClient() {
		return httpClient;
	}
	
	public static HttpResponse execute(HttpUriRequest request) {

		try {
			HttpResponse response = httpClient.execute(request);
			return response;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			request.abort();
			logger.error("httpclient pool error", e);
		}
		
		return null;
	}
}
