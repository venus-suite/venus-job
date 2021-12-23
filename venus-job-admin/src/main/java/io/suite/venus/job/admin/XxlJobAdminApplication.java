package io.suite.venus.job.admin;

import ch.qos.logback.classic.PatternLayout;
import io.suite.venus.job.admin.common.log.logback.EscapeMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * origin @author xuxueli 2018-10-28 00:38:13
 * 修改 hcmfys@163.com
 */

@SpringBootApplication(scanBasePackages={"io.suite.venus.job"})

 public class XxlJobAdminApplication {

	static {
		PatternLayout.defaultConverterMap.put("emsg", EscapeMessageConverter.class.getName());
	}
	public static void main(String[] args) {
		try {
			SpringApplication.run(XxlJobAdminApplication.class, args);
		}catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}