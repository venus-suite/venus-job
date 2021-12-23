package io.suite.venus.job.admin.common.exception;

import org.apache.commons.text.StringEscapeUtils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


public class ExceptionUtils {

	
	public static String getExceptionToString(Exception ex,boolean isEscape) {
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintStream pout = new PrintStream(out);
		ex.printStackTrace(pout);
		String ret = new String(out.toByteArray());
		if(isEscape){
			ret=StringEscapeUtils.escapeJson(ret);
		}
		pout.close();
		try {
			out.close();
		} catch (Exception e) {
		}
		return ret;
	}
}
