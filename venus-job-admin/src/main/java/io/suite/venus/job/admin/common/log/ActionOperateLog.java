package io.suite.venus.job.admin.common.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ActionOperateLog {
	
	
	String eventName();
	
	int type() default 0;//0=增加，1=删除，2=修改

}
