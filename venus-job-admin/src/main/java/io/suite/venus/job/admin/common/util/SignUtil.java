package io.suite.venus.job.admin.common.util;

import org.springframework.util.DigestUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLEncoder;

public class SignUtil {

	public static String getSign(Object obj,String appKey) {
		String param=getParam(obj)+":"+appKey;
		return DigestUtils.md5DigestAsHex(param.getBytes());

	}

	public static String getParam(Object obj) {
		StringBuffer params = new StringBuffer();
		try {

			BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			int num = 0;
			for (PropertyDescriptor pd : propertyDescriptors) {
				String proName = pd.getName();
				if (!"class".equals(proName) && !"sign".equals(proName)) {
					Method method = pd.getReadMethod();
					Object value = method.invoke(obj);
					if (value != null) {
						if (num != 0) {
							params.append("&");
						}
						if (value instanceof Double) {
							String fee = getPrettyNumber((Double) value);
							params.append(proName).append("=").append(fee);
						} else {
							params.append(proName).append("=").append(URLEncoder.encode(value.toString(), "UTF-8"));
						}
						num++;
					}
				}
			}
		} catch (Exception e) {
			return null;
		}
		return params.toString();
	}

	private static String getPrettyNumber(double number) {
		return BigDecimal.valueOf(number).stripTrailingZeros().toPlainString();
	}
}
