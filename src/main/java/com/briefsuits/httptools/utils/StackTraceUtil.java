package com.briefsuits.httptools.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class StackTraceUtil {

	/**
	 * 取出exception中的信息
	 * 
	 * @param exception
	 * @return
	 */
	public static String getStackTrace(Throwable exception) {
		StringWriter sw = null;
		PrintWriter pw = null;
		try {
			sw = new StringWriter();
			pw = new PrintWriter(sw);
			exception.printStackTrace(pw);
			return sw.toString();
		} finally {
			if (pw != null) {
				pw.close();
			}
		}
	}

	public static Throwable findException(Throwable throwable, Class<?>... exClassArray) {
		Throwable t = throwable;
		int i = 0;

		while (t != null && (++i) < 10) {
			for (Class<?> exClass : exClassArray) {
				if (exClass.isInstance(t)) {
					return t;
				}
				t = t.getCause();
			}
		}

		return null;
	}

	/**
	 * 打印当前线程的堆栈信息
	 */
	public static String printStackTrace() {
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		StringBuffer buffer = new StringBuffer();
		for (StackTraceElement el : stackTraceElements) {
			buffer.append(el.toString()).append("\n");
		}
		return buffer.toString();
	}
}
