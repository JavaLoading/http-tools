package com.briefsuits.httptools.constants;

public enum ResultCodeEnum {
	SUCCESS("0", "成功"), // 成功
	NORMAL_ERROR("100", "通用错误"), // 通用错误
	PARAM_ERROR("301", "参数错误"); // 参数错误(会有具体信息)

	private String value;

	private String message;

	ResultCodeEnum(String value, String message) {
		this.value = value;
		this.message = message;
	}

	public String val() {
		return value;
	}

	public String msg() {
		return message;
	}
}
