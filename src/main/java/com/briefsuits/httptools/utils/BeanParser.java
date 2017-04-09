package com.briefsuits.httptools.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import java.lang.reflect.Field;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;

public class BeanParser {
	private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	@SuppressWarnings("rawtypes")
	public static Map<String, String> parseBeanToMap(Object obj) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			Class clazz = obj.getClass();
			Field[] fields = clazz.getDeclaredFields();

			for (Field field : fields) {
				field.setAccessible(true);
				Object object = field.get(obj);

				String value = "";

				String fieldType = field.getType().getSimpleName();
				if ("Date".equals(fieldType)) {
					value = DateFormatUtils.format((Date) object, DATE_FORMAT);
				} else {
					value = String.valueOf(object);
				}

				if (StringUtils.isNotBlank(value) && !"null".equals(value)) {
					map.put(field.getName(), value);
				}
			}
		} catch (SecurityException se) {
			se.printStackTrace();
		} catch (IllegalArgumentException iare) {
			iare.printStackTrace();
		} catch (IllegalAccessException iace) {
			iace.printStackTrace();
		}

		return map;
	}

}
