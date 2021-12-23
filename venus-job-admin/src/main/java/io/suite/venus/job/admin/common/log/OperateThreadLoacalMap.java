package io.suite.venus.job.admin.common.log;


import com.alibaba.fastjson.JSON;
import io.suite.venus.job.admin.core.model.OperateLog;

import java.util.HashMap;
import java.util.Map;

public class OperateThreadLoacalMap {

	private static final String EVENT_NAME = "eventName";

	private static final String DATA_OLD = "dataOld";

	private static final String DATA_NEW = "dataNew";

	private static final String MSG = "msg";
	private static ThreadLocal<Map<String, Object>> operateThreadLocalMap =
			new ThreadLocal<Map<String, Object>>();

	private static void put(String key, Object value) {
		Map<String, Object> map = operateThreadLocalMap.get();
		if (map == null) {
			map = new HashMap<String, Object>();

		}
		map.put(key, value);
		operateThreadLocalMap.set(map);
	}

	private static Object get(String key) {
		Map<String, Object> map = operateThreadLocalMap.get();
		if (map != null) {
			return map.get(key);
		}
		return null;
	}

	public static void putEventName(String obj) {
		if (obj != null) {
			String value = JSON.toJSONString(obj);
			put(EVENT_NAME, value);
		}

	}
	public static void putMsg(OperateLog obj) {
		if (obj != null) {
			put(MSG, obj);
		}

	}

	public static void putDataNew(Object obj) {
		if (obj != null) {
			String value = JSON.toJSONString(obj);
			put(DATA_NEW, value);
		}
	}

	public static void putDataOldAndNew(Object objOld,Object objNew) {
		putDataOld(objOld);
		putDataNew(objNew);
	}

	public static void putDataOld(Object obj) {
		if (obj != null) {
			String value = JSON.toJSONString(obj);
			put(DATA_OLD, value);
		}
	}

	public static String getEventName() {
		return String.valueOf(get(EVENT_NAME));
	}

	public static String getDataNew() {
		return String.valueOf(get(DATA_NEW));
	}

	public static String getDataOld() {
		return String.valueOf(get(DATA_OLD));
	}
	public static OperateLog getMsg() {
		return (OperateLog)get(MSG);
	}

	public static void clear() {
		Map<String, Object> map = operateThreadLocalMap.get();
		if (map != null) {
			map.remove(EVENT_NAME);
			map.remove(DATA_OLD);
			map.remove(DATA_NEW);
			map.remove(MSG);
		}
	}


	public static OperateLogInfo get() {
		OperateLogInfo operateLogInfo=new OperateLogInfo();
		operateLogInfo.eventName=String.valueOf(get(EVENT_NAME));
		operateLogInfo.dataOld=String.valueOf(get(DATA_OLD));
		operateLogInfo.dataNew=String.valueOf(get(DATA_NEW));
		return operateLogInfo;
	}
	public static class OperateLogInfo {

		public String eventName;

		public String dataOld;

		public String dataNew;

	}
}
