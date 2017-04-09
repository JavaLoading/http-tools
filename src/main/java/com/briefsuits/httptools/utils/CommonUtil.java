package com.briefsuits.httptools.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.DeflateDecompressingEntity;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonUtil {

	private static final Logger log = LoggerFactory.getLogger(CommonUtil.class);

	public static String paseResponse(HttpResponse response) {
		try {
			HttpEntity entity = response.getEntity();
			Header ceheader = entity.getContentEncoding();
			if (ceheader != null) {
				for (HeaderElement element : ceheader.getElements()) {
					if (element.getName().equalsIgnoreCase("gzip")) {
						entity = new GzipDecompressingEntity(response.getEntity());
						response.setEntity(entity);
						break;
					}
					if (element.getName().equalsIgnoreCase("deflate")) {
						entity = new DeflateDecompressingEntity(response.getEntity());
						response.setEntity(entity);
						break;
					}
				}
			}

			String body = null;
			String charset = "UTF-8";
			if (null != ContentType.getOrDefault(entity).getCharset()) {
				if (StringUtils.isNotBlank(ContentType.getOrDefault(entity).getCharset().name())) {
					charset = ContentType.getOrDefault(entity).getCharset().name();
				}
			}
			body = EntityUtils.toString(entity, charset);
			EntityUtils.consume(entity);

			return body;
		} catch (Exception ex) {
			log.error("http response parse exception,{}", StackTraceUtil.getStackTrace(ex));
			return null;
		}
	}
}
