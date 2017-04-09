package com.briefsuits.httptools.constants;

public enum HttpRequestType {

	POST(1),
	GET(2),
	PUT(3),
	DELETE(4);

	private final int type;

	private HttpRequestType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}
}
