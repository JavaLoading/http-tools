package com.briefsuits.httptools.constants;

public class HttpPoolConfig {
	/**
	 * 连接池里的最大连接数, 默认值: 800
	 */
	public static int MAX_TOTAL_CONNECTIONS = 800;

	/**
	 * 每个路由的默认最大连接数, 默认值: 400
	 */
	public static int MAX_ROUTE_CONNECTIONS = MAX_TOTAL_CONNECTIONS >> 1;

	/**
	 * 连接超时时间, 默认值: 6000
	 */
	public static int CONNECT_TIMEOUT = 6000;

	/**
	 * 套接字超时时间, 默认值: 8000
	 */
	public static int SOCKET_TIMEOUT = 8000;

	/**
	 * 连接池中 连接请求执行被阻塞的超时时间, 默认值: 10000
	 */
	public static long CONN_MANAGER_TIMEOUT = 10000;
}
