package io.suite.venus.job.admin.core.thread;

import io.suite.venus.job.admin.config.XxlJobAdminConfig;
import io.suite.venus.job.admin.core.model.Namespace;
import io.suite.venus.job.admin.core.model.XxlJobGroup;
import io.suite.venus.job.admin.core.model.XxlJobInfo;
import io.suite.venus.job.admin.core.model.XxlJobLog;
import io.suite.venus.job.admin.core.trigger.TriggerTypeEnum;
import io.suite.venus.job.admin.core.util.I18nUtil;
import io.suite.venus.job.admin.mapper.NamespaceMapper;
import io.suite.venus.job.admin.service.RobotService;
import com.xxl.job.core.biz.model.ReturnT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * job monitor instance
 *
 * @author xuxueli 2015-9-1 18:05:56
 */

public class JobFailMonitorHelper {
	private static Logger logger = LoggerFactory.getLogger(JobFailMonitorHelper.class);

	private static JobFailMonitorHelper instance = new JobFailMonitorHelper();

	public static JobFailMonitorHelper getInstance() {
		return instance;
	}

	// ---------------------- monitor ----------------------

	private Thread monitorThread;
	private volatile boolean toStop = false;

	public void start() {
		monitorThread = new Thread(new Runnable() {

			@Override
			public void run() {

				// monitor
				while (!toStop) {
					try {

						List<Integer> failLogIds = XxlJobAdminConfig.getAdminConfig().getXxlJobLogDao().findFailJobLogIds(1000);
						if (failLogIds != null && !failLogIds.isEmpty()) {
							for (int failLogId : failLogIds) {

								// lock log
								int lockRet = XxlJobAdminConfig.getAdminConfig().getXxlJobLogDao().updateAlarmStatus(failLogId, 0, -1);
								if (lockRet < 1) {
									continue;
								}
								XxlJobLog log = XxlJobAdminConfig.getAdminConfig().getXxlJobLogDao().load(failLogId);
								XxlJobInfo info = XxlJobAdminConfig.getAdminConfig().getXxlJobInfoDao().loadById(log.getJobId());

								// 1、fail retry monitor
								if (log.getExecutorFailRetryCount() > 0) {
									JobTriggerPoolHelper.trigger(log.getJobId(), TriggerTypeEnum.RETRY, (log.getExecutorFailRetryCount() - 1), log.getExecutorShardingParam(), null);
									String retryMsg = "<br><br><span style=\"color:#F39C12;\" > >>>>>>>>>>>" + I18nUtil.getString("jobconf_trigger_type_retry") + "<<<<<<<<<<< </span><br>";
									log.setTriggerMsg(log.getTriggerMsg() + retryMsg);
									XxlJobAdminConfig.getAdminConfig().getXxlJobLogDao().updateTriggerInfo(log);
								}

								// 2、fail alarm monitor
								int newAlarmStatus = 0;        // 告警状态：0-默认、-1=锁定状态、1-无需告警、2-告警成功、3-告警失败
								if (info != null ) {
									boolean alarmResult = true;
									try {
										alarmResult = failAlarm(info, log);
									} catch (Exception e) {
										alarmResult = false;
										logger.error(e.getMessage(), e);
									}
									newAlarmStatus = alarmResult ? 2 : 3;
								} else {
									newAlarmStatus = 1;
								}

								XxlJobAdminConfig.getAdminConfig().getXxlJobLogDao().updateAlarmStatus(failLogId, -1, newAlarmStatus);
							}
						}

						TimeUnit.SECONDS.sleep(10);
					} catch (Exception e) {
						if (!toStop) {
							logger.error(">>>>>>>>>>> xxl-job, job fail monitor thread error:{}", e);
						}
					}
				}

				logger.info(">>>>>>>>>>> xxl-job, job fail monitor thread stop");

			}
		});
		monitorThread.setDaemon(true);
		monitorThread.setName("xxl-job, admin JobFailMonitorHelper");
		monitorThread.start();
	}

	public void toStop() {
		toStop = true;
		// interrupt and wait
		monitorThread.interrupt();
		try {
			monitorThread.join();
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		}
	}


	// ---------------------- alarm ----------------------

	// email alarm template
	private static final String mailBodyTemplate =
			"" + I18nUtil.getString("jobconf_monitor_detail") + "\r\n" +
					"" + I18nUtil.getString("jobinfo_field_jobgroup") + ": {0}\r\n" +
					"" + I18nUtil.getString("jobinfo_field_id") + ": {1}\r\n" +
					"" + I18nUtil.getString("jobinfo_field_jobdesc") + ": {2}\r\n" +
					"" + I18nUtil.getString("jobconf_monitor_alarm_title") + ": " + I18nUtil.getString("jobconf_monitor_alarm_type") +
					"" + I18nUtil.getString("jobconf_monitor_alarm_content") + ": \n{3}\r\n";


	/**
	 * fail alarm
	 *
	 * @param jobLog
	 */
	private boolean failAlarm(XxlJobInfo info, XxlJobLog jobLog) {
		boolean alarmResult = true;
		// send monitor email
		if (info != null) {

			// alarmContent
			String alarmContent = "Alarm Job LogId=" + jobLog.getId();
			if (jobLog.getTriggerCode() != ReturnT.SUCCESS_CODE) {
				alarmContent += "\r\nTriggerMsg=<br>" + jobLog.getTriggerMsg();
			}
			if (jobLog.getHandleCode() > 0 && jobLog.getHandleCode() != ReturnT.SUCCESS_CODE) {
				alarmContent += "\nHandleCode=" + jobLog.getHandleMsg();
			}

			// email info
			XxlJobGroup group = XxlJobAdminConfig.getAdminConfig().getXxlJobGroupDao().load(
					Integer.valueOf(info.getJobGroup()));
			String personal = I18nUtil.getString("admin_name_full");
			String title = I18nUtil.getString("jobconf_monitor");
			String content = MessageFormat.format(mailBodyTemplate,
					group != null ? group.getTitle() : "null",
					info.getId(),
					info.getJobDesc(),
					alarmContent);

			// make webhook
			try {
				NamespaceMapper namespaceMapper = XxlJobAdminConfig.getAdminConfig().getNamespaceMapper();
				Long namespaceId = group.getNamespaceId();
				RobotService robotService = XxlJobAdminConfig.getAdminConfig().getRobotService();
				if (namespaceId != null) {
					Namespace namespace = namespaceMapper.selectByPrimaryKey(namespaceId);
					if (namespaceMapper != null) {
						String webHook = namespace.getNoticeWebHook();
						if(webHook!=null) {
							content = content.replace("<br>", "\r\n");
							content = content.replace("<span style=\"color:#00c0ef;\" >", "");
							content = content.replace("</span>", "");
							robotService.sendRobot(webHook, content, null);
						}
					}
				}
				//XxlJobAdminConfig.getAdminConfig().getMailSender().send(mimeMessage);
			} catch (Exception e) {
				logger.error(">>>>>>>>>>> xxl-job, job fail alarm  send error, JobLogId:{}", jobLog.getId(), e);
				alarmResult = false;
			}
		}

		// TODO, custom alarm strategy, such as sms

		return alarmResult;
	}

}
