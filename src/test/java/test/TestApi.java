package test;

import com.briefsuits.httptools.HttpAsyncUtils;
import com.briefsuits.httptools.constants.HttpRequestType;

public class TestApi {
	public static void main(String[] args){
		String url = "http://www.baidu.com";
		String result = HttpAsyncUtils.sendRequest(HttpRequestType.GET, url, null);
		System.out.println(result);
	}
}
