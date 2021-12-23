package io.suite.venus.job.admin.common.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

public class MethodParameterDiscovererUtil {
	
	private static Cache<String, Object> manualCache = Caffeine.newBuilder().expireAfterWrite(30, TimeUnit.SECONDS).maximumSize(10000)
			.build();
	
	
	private static final LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
	
	public static String getElString(String expression,Method method,Object[] params){
		if(expression==null){
			return null;
		}
		if(!expression.contains("#")){
			return expression;
		}
		String[] paramNames =(String[]) manualCache.getIfPresent(method.getName());
		if(paramNames==null){
			paramNames = parameterNameDiscoverer.getParameterNames(method);
		}
		ExpressionParser parser = new SpelExpressionParser();
		StandardEvaluationContext context = new StandardEvaluationContext();
		int i=0;
		for (String paramName : paramNames) {
			context.setVariable(paramName,params[i]);
			i++;
		}
		String st= parser.parseExpression(expression).getValue(context,String.class);
		return st;
		
	}

	
	
	
}
