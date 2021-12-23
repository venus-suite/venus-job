package io.suite.venus.job.admin.service.impl;

import io.suite.venus.job.admin.core.model.XxlJobGroup;
import io.suite.venus.job.admin.core.model.XxlJobInfo;
import io.suite.venus.job.admin.core.route.ExecutorRouteStrategyEnum;
import io.suite.venus.job.admin.core.schedule.XxlJobDynamicScheduler;
import io.suite.venus.job.admin.core.thread.JobTriggerPoolHelper;
import io.suite.venus.job.admin.core.trigger.TriggerTypeEnum;
import io.suite.venus.job.admin.core.util.I18nUtil;
import io.suite.venus.job.admin.core.util.LocalCacheUtil;
import io.suite.venus.job.admin.domain.dto.ChartInfoRes;
import io.suite.venus.job.admin.domain.dto.DashInfoRes;
import io.suite.venus.job.admin.common.inout.BaseMngPageResp;
import io.suite.venus.job.admin.mapper.XxlJobGroupMapper;
import io.suite.venus.job.admin.mapper.XxlJobInfoMapper;
import io.suite.venus.job.admin.mapper.XxlJobLogGlueMapper;
import io.suite.venus.job.admin.mapper.XxlJobLogMapper;
import io.suite.venus.job.admin.service.XxlJobService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.enums.ExecutorBlockStrategyEnum;
import com.xxl.job.core.glue.GlueTypeEnum;
import com.xxl.job.core.util.DateUtil;
import org.quartz.CronExpression;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.*;

/**
 * core job action for xxl-job
 * @author xuxueli 2016-5-28 15:30:33
 */
@Service
public class XxlJobServiceImpl implements XxlJobService {
	private static Logger logger = LoggerFactory.getLogger(XxlJobServiceImpl.class);

	@Resource
	private XxlJobGroupMapper xxlJobGroupMapper;
	@Resource
	private XxlJobInfoMapper xxlJobInfoMapper;
	@Resource
	public XxlJobLogMapper xxlJobLogMapper;
	@Resource
	private XxlJobLogGlueMapper xxlJobLogGlueMapper;
	
	@Override
	public BaseMngPageResp<XxlJobInfo> pageList(int start, int length, int jobGroup, String jobDesc,String email,
												String executorHandler, String filterTime,
												String  jobStatus,String author,String jobCron,int jobId ) {

		// page list
		//int list_count = xxlJobInfoMapper.pageListCount(start, length, jobGroup, jobDesc, executorHandler);

		try {
			PageHelper.startPage(start, length);

			List<XxlJobInfo> list = xxlJobInfoMapper.pageList(start, length, jobGroup, jobDesc,email,
					executorHandler,jobStatus,author,jobCron,jobId);
			// fill job info
			if (list != null && list.size() > 0) {
				for (XxlJobInfo jobInfo : list) {
					Trigger.TriggerState triggerState =XxlJobDynamicScheduler.fillJobInfo(jobInfo);
					if (triggerState!=null) {
						String curName = jobInfo.getJobStatus()+"";
						if ( StringUtils.isEmpty(curName) || !curName.equals(triggerState.name())) {
							jobInfo.setJobStatus(triggerState.name());
							xxlJobInfoMapper.updateJobStatus(jobInfo);
						}
						jobInfo.setJobStatus(triggerState.name());
					}
				}
			}
			PageInfo<XxlJobInfo> pageInfo = new PageInfo<>(list);
			BaseMngPageResp<XxlJobInfo> resp = new BaseMngPageResp<>();
			resp.setTotal(pageInfo.getTotal());
			resp.setPages(pageInfo.getPages());
			resp.setContentList(list);
			return resp;
		} finally {
			PageHelper.clearPage();
		}
	}

	@Override
	public ReturnT<String> add(XxlJobInfo jobInfo) {
		// valid
		XxlJobGroup group = xxlJobGroupMapper.load(jobInfo.getJobGroup());
		if (group == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("system_please_choose")+I18nUtil.getString("jobinfo_field_jobgroup")) );
		}
		if (!CronExpression.isValidExpression(jobInfo.getJobCron())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, I18nUtil.getString("jobinfo_field_cron_unvalid") );
		}
		if (jobInfo.getJobDesc()==null || jobInfo.getJobDesc().trim().length()==0) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("system_please_input")+I18nUtil.getString("jobinfo_field_jobdesc")) );
		}
		if (jobInfo.getAuthor()==null || jobInfo.getAuthor().trim().length()==0) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("system_please_input")+I18nUtil.getString("jobinfo_field_author")) );
		}
		if (ExecutorRouteStrategyEnum.match(jobInfo.getExecutorRouteStrategy(), null) == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("jobinfo_field_executorRouteStrategy")+I18nUtil.getString("system_unvalid")) );
		}
		if (ExecutorBlockStrategyEnum.match(jobInfo.getExecutorBlockStrategy(), null) == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("jobinfo_field_executorBlockStrategy")+I18nUtil.getString("system_unvalid")) );
		}
		if (GlueTypeEnum.match(jobInfo.getGlueType()) == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("jobinfo_field_gluetype")+I18nUtil.getString("system_unvalid")) );
		}
		if (GlueTypeEnum.BEAN==GlueTypeEnum.match(jobInfo.getGlueType()) && (jobInfo.getExecutorHandler()==null || jobInfo.getExecutorHandler().trim().length()==0) ) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("system_please_input")+"JobHandler") );
		}

		// fix "\r" in shell
		if (GlueTypeEnum.GLUE_SHELL==GlueTypeEnum.match(jobInfo.getGlueType()) && jobInfo.getGlueSource()!=null) {
			jobInfo.setGlueSource(jobInfo.getGlueSource().replaceAll("\r", ""));
		}

		// ChildJobId valid
		if (jobInfo.getChildJobId()!=null && jobInfo.getChildJobId().trim().length()>0) {
			String[] childJobIds = jobInfo.getChildJobId().split(",");
			for (String childJobIdItem: childJobIds) {
				if (childJobIdItem!=null && childJobIdItem.trim().length()>0 && isNumeric(childJobIdItem)) {
					XxlJobInfo childJobInfo = xxlJobInfoMapper.loadById(Integer.valueOf(childJobIdItem));
					if (childJobInfo==null) {
						return new ReturnT<String>(ReturnT.FAIL_CODE,
								MessageFormat.format((I18nUtil.getString("jobinfo_field_childJobId")+"({0})"+I18nUtil.getString("system_not_found")), childJobIdItem));
					}
				} else {
					return new ReturnT<String>(ReturnT.FAIL_CODE,
							MessageFormat.format((I18nUtil.getString("jobinfo_field_childJobId")+"({0})"+I18nUtil.getString("system_unvalid")), childJobIdItem));
				}
			}

			String temp = "";	// join ,
			for (String item:childJobIds) {
				temp += item + ",";
			}
			temp = temp.substring(0, temp.length()-1);

			jobInfo.setChildJobId(temp);
		}

		// add in db
		xxlJobInfoMapper.save(jobInfo);
		if (jobInfo.getId() < 1) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("jobinfo_field_add")+I18nUtil.getString("system_fail")) );
		}

		return new ReturnT<String>(String.valueOf(jobInfo.getId()));
	}

	private boolean isNumeric(String str){
		try {
			int result = Integer.valueOf(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	@Override
	public ReturnT<String> update(XxlJobInfo jobInfo) {

		// valid
		if (!CronExpression.isValidExpression(jobInfo.getJobCron())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, I18nUtil.getString("jobinfo_field_cron_unvalid") );
		}
		if (jobInfo.getJobDesc()==null || jobInfo.getJobDesc().trim().length()==0) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("system_please_input")+I18nUtil.getString("jobinfo_field_jobdesc")) );
		}
		if (jobInfo.getAuthor()==null || jobInfo.getAuthor().trim().length()==0) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("system_please_input")+I18nUtil.getString("jobinfo_field_author")) );
		}
		if (ExecutorRouteStrategyEnum.match(jobInfo.getExecutorRouteStrategy(), null) == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("jobinfo_field_executorRouteStrategy")+I18nUtil.getString("system_unvalid")) );
		}
		if (ExecutorBlockStrategyEnum.match(jobInfo.getExecutorBlockStrategy(), null) == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("jobinfo_field_executorBlockStrategy")+I18nUtil.getString("system_unvalid")) );
		}

		// ChildJobId valid
		if (jobInfo.getChildJobId()!=null && jobInfo.getChildJobId().trim().length()>0) {
			String[] childJobIds = jobInfo.getChildJobId().split(",");
			for (String childJobIdItem: childJobIds) {
				if (childJobIdItem!=null && childJobIdItem.trim().length()>0 && isNumeric(childJobIdItem)) {
					XxlJobInfo childJobInfo = xxlJobInfoMapper.loadById(Integer.valueOf(childJobIdItem));
					if (childJobInfo==null) {
						return new ReturnT<String>(ReturnT.FAIL_CODE,
								MessageFormat.format((I18nUtil.getString("jobinfo_field_childJobId")+"({0})"+I18nUtil.getString("system_not_found")), childJobIdItem));
					}
				} else {
					return new ReturnT<String>(ReturnT.FAIL_CODE,
							MessageFormat.format((I18nUtil.getString("jobinfo_field_childJobId")+"({0})"+I18nUtil.getString("system_unvalid")), childJobIdItem));
				}
			}

			String temp = "";	// join ,
			for (String item:childJobIds) {
				temp += item + ",";
			}
			temp = temp.substring(0, temp.length()-1);

			jobInfo.setChildJobId(temp);
		}

		// group valid
		XxlJobGroup jobGroup = xxlJobGroupMapper.load(jobInfo.getJobGroup());
		if (jobGroup == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("jobinfo_field_jobgroup")+I18nUtil.getString("system_unvalid")) );
		}

		// stage job info
		XxlJobInfo exists_jobInfo = xxlJobInfoMapper.loadById(jobInfo.getId());
		if (exists_jobInfo == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("jobinfo_field_id")+I18nUtil.getString("system_not_found")) );
		}

		exists_jobInfo.setJobGroup(jobInfo.getJobGroup());
		exists_jobInfo.setJobCron(jobInfo.getJobCron());
		exists_jobInfo.setJobDesc(jobInfo.getJobDesc());
		exists_jobInfo.setAuthor(jobInfo.getAuthor());
		exists_jobInfo.setAlarmEmail(jobInfo.getAlarmEmail());
		exists_jobInfo.setExecutorRouteStrategy(jobInfo.getExecutorRouteStrategy());
		exists_jobInfo.setExecutorHandler(jobInfo.getExecutorHandler());
		exists_jobInfo.setExecutorParam(jobInfo.getExecutorParam());
		exists_jobInfo.setExecutorBlockStrategy(jobInfo.getExecutorBlockStrategy());
		exists_jobInfo.setExecutorTimeout(jobInfo.getExecutorTimeout());
		exists_jobInfo.setExecutorFailRetryCount(jobInfo.getExecutorFailRetryCount());
		exists_jobInfo.setChildJobId(jobInfo.getChildJobId());
        xxlJobInfoMapper.update(exists_jobInfo);


		// update quartz-cron if started
        try {
			String qz_name = String.valueOf(exists_jobInfo.getId());
            XxlJobDynamicScheduler.updateJobCron(qz_name, exists_jobInfo.getJobCron());
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
			return ReturnT.FAIL;
        }

		return ReturnT.SUCCESS;
	}

	@Override
	public ReturnT<String> remove(XxlJobInfo jobInfo) {
		int id= jobInfo.getId();
		XxlJobInfo xxlJobInfo = xxlJobInfoMapper.loadById(id);
		if (xxlJobInfo == null) {
			return ReturnT.SUCCESS;
		}
		String name = String.valueOf(xxlJobInfo.getId());

		try {
			// unbind quartz
			XxlJobDynamicScheduler.removeJob(name);

			xxlJobInfoMapper.delete(id);
			xxlJobLogMapper.delete(id);
			xxlJobLogGlueMapper.deleteByJobId(id);
			return ReturnT.SUCCESS;
		} catch (SchedulerException e) {
			logger.error(e.getMessage(), e);
			return ReturnT.FAIL;
		}

	}

	@Override
	public ReturnT<String> start(XxlJobInfo jobInfo) {
		XxlJobInfo xxlJobInfo = xxlJobInfoMapper.loadById(jobInfo.getId());
		String name = String.valueOf(xxlJobInfo.getId());
		String cronExpression = xxlJobInfo.getJobCron();

		try {
			boolean ret = XxlJobDynamicScheduler.addJob(name, cronExpression);
			return ret?ReturnT.SUCCESS:ReturnT.FAIL;
		} catch (SchedulerException e) {
			logger.error(e.getMessage(), e);
			return ReturnT.FAIL;
		}
	}

	@Override
	public ReturnT<String> stop(XxlJobInfo jobInfo) {
        XxlJobInfo xxlJobInfo = xxlJobInfoMapper.loadById(jobInfo.getId());
        String name = String.valueOf(xxlJobInfo.getId());

		try {
			// bind quartz
            boolean ret = XxlJobDynamicScheduler.removeJob(name);
            return ret?ReturnT.SUCCESS:ReturnT.FAIL;
		} catch (SchedulerException e) {
			logger.error(e.getMessage(), e);
			return ReturnT.FAIL;
		}
	}

	public ReturnT<String> pause(XxlJobInfo jobInfo) {
		return stop(jobInfo);
	}


	@Override
	public DashInfoRes dashboardInfo() {

		int jobInfoCount = xxlJobInfoMapper.findAllCount();
		int jobLogCount = xxlJobLogMapper.triggerCountByHandleCode(-1);
		int jobLogSuccessCount = xxlJobLogMapper.triggerCountByHandleCode(ReturnT.SUCCESS_CODE);

		// executor count
		Set<String> executerAddressSet = new HashSet<String>();
		List<XxlJobGroup> groupList = xxlJobGroupMapper.findAll();

		if (groupList != null && !groupList.isEmpty()) {
			for (XxlJobGroup group : groupList) {
				if (group.getRegistryList() != null && !group.getRegistryList().isEmpty()) {
					executerAddressSet.addAll(group.getRegistryList());
				}
			}
		}

		int executorCount = executerAddressSet.size();
		DashInfoRes res = new DashInfoRes();

		//Map<String, Object> dashboardMap = new HashMap<String, Object>();
		//dashboardMap.put("jobInfoCount", jobInfoCount);
		res.setJobInfoCount(jobInfoCount);
		//dashboardMap.put("jobLogCount", jobLogCount);
		res.setJobLogCount(jobLogCount);
		////dashboardMap.put("jobLogSuccessCount", jobLogSuccessCount);
		res.setJobLogSuccessCount(jobLogSuccessCount);
		//dashboardMap.put("executorCount", executorCount);
		res.setExecutorCount(executorCount);
		return res;
	}

	private static final String TRIGGER_CHART_DATA_CACHE = "p_trigger_chart_data_cache";
	@Override
	public  ChartInfoRes chartInfo(Date startDate, Date endDate) {
		// get cache
		String cacheKey = TRIGGER_CHART_DATA_CACHE;
		ChartInfoRes chartInfo = (ChartInfoRes) LocalCacheUtil.get(cacheKey);
		if (chartInfo != null) {
			//return chartInfo;
		}

		// process
		List<String> triggerDayList = new ArrayList<String>();
		List<Integer> triggerDayCountRunningList = new ArrayList<Integer>();
		List<Integer> triggerDayCountSucList = new ArrayList<Integer>();
		List<Integer> triggerDayCountFailList = new ArrayList<Integer>();
		int triggerCountRunningTotal = 0;
		int triggerCountSucTotal = 0;
		int triggerCountFailTotal = 0;

		List<Map<String, Object>> triggerCountMapAll = xxlJobLogMapper.triggerCountByDay(startDate, endDate);
		if (triggerCountMapAll != null && triggerCountMapAll.size() > 0) {
			for (Map<String, Object> item : triggerCountMapAll) {
				String day = String.valueOf(item.get("triggerDay"));
				int triggerDayCount = Integer.valueOf(String.valueOf(item.get("triggerDayCount")));
				int triggerDayCountRunning = Integer.valueOf(String.valueOf(item.get("triggerDayCountRunning")));
				int triggerDayCountSuc = Integer.valueOf(String.valueOf(item.get("triggerDayCountSuc")));
				int triggerDayCountFail = triggerDayCount - triggerDayCountRunning - triggerDayCountSuc;

				triggerDayList.add(day);
				triggerDayCountRunningList.add(triggerDayCountRunning);
				triggerDayCountSucList.add(triggerDayCountSuc);
				triggerDayCountFailList.add(triggerDayCountFail);

				triggerCountRunningTotal += triggerDayCountRunning;
				triggerCountSucTotal += triggerDayCountSuc;
				triggerCountFailTotal += triggerDayCountFail;
			}
		} else {
			for (int i = 4; i > -1; i--) {
				triggerDayList.add(DateUtil.formatDate(DateUtil.addDays(new Date(), -i)));
				triggerDayCountRunningList.add(0);
				triggerDayCountSucList.add(0);
				triggerDayCountFailList.add(0);
			}
		}

		ChartInfoRes res = new ChartInfoRes();
		//Map<String, Object> result = new HashMap<String, Object>();
		//result.put("triggerDayList", triggerDayList);
		res.setTriggerDayList(triggerDayList);
		//result.put("triggerDayCountRunningList", triggerDayCountRunningList);
		res.setTriggerDayCountRunningList(triggerDayCountRunningList);
		//result.put("triggerDayCountSucList", triggerDayCountSucList);
		res.setTriggerDayCountSucList(triggerDayCountSucList);
		//result.put("triggerDayCountFailList", triggerDayCountFailList);
		res.setTriggerDayCountFailList(triggerDayCountFailList);

		//result.put("triggerCountRunningTotal", triggerCountRunningTotal);
		res.setTriggerCountFailTotal(triggerCountRunningTotal);
		//result.put("triggerCountSucTotal", triggerCountSucTotal);
		res.setTriggerCountSucTotal(triggerCountSucTotal);
		//result.put("triggerCountFailTotal", triggerCountFailTotal);
		res.setTriggerCountFailTotal(triggerCountFailTotal);
		// set cache
		//LocalCacheUtil.set(cacheKey, res, 60 *60  * 1000);     // cache 3600s*/
		return res;
	}

	@Override
	public ReturnT<String> triggerJob(XxlJobInfo jobInfo) {

		String executorParam=jobInfo.getExecutorParam();
		int id=jobInfo.getId();
		// force cover job param
		if (StringUtils.isEmpty(executorParam)) {
			XxlJobInfo job=	xxlJobInfoMapper.loadById(id);
			executorParam = "";
			if(job!=null){
				executorParam=job.getExecutorParam();
			}
		}
		JobTriggerPoolHelper.trigger(id, TriggerTypeEnum.MANUAL, -1, null, executorParam);
		return ReturnT.SUCCESS;

	}



	@Override
	public Integer clearLogByDate(Date expireCalendar) {
		return xxlJobLogMapper.clearLogByDay(expireCalendar);
	}


	@Override
	public Integer clearServerLogByDay(Date expireCalendar) {
		return xxlJobLogMapper.clearServerLogByDay(expireCalendar);
	}

}
