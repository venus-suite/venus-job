package io.suite.venus.job.admin.controller;

import io.suite.venus.job.admin.common.inout.BaseResponse;
import io.suite.venus.job.admin.common.log.ActionOperateLog;
import io.suite.venus.job.admin.controller.annotation.AuditLimit;
import io.suite.venus.job.admin.core.model.XxlJobGroup;
import io.suite.venus.job.admin.core.model.XxlJobInfo;
import io.suite.venus.job.admin.core.route.ExecutorRouteStrategyEnum;
import io.suite.venus.job.admin.domain.dto.JobInfoReq;
import io.suite.venus.job.admin.domain.dto.UserInfo;
import io.suite.venus.job.admin.domain.dto.UserInfoContext;
import io.suite.venus.job.admin.enums.OperaCodeEnum;
import io.suite.venus.job.admin.common.inout.BaseMngPageResp;
import io.suite.venus.job.admin.service.JobGroupService;
import io.suite.venus.job.admin.service.UserService;
import io.suite.venus.job.admin.service.XxlJobService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.enums.ExecutorBlockStrategyEnum;
import com.xxl.job.core.glue.GlueTypeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.quartz.CronExpression;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * index controller
 */
@RestController
@RequestMapping("/job/jobinfo")
@Api(tags = "任务管理")
public class JobInfoController {


	@Resource
	private XxlJobService xxlJobService;
	@Resource
	private UserService userService;

	@Resource
	private JobGroupService jobGroupService;

	@RequestMapping(value = "/", method = RequestMethod.POST)
	@ApiOperation(value = "枚举详情", notes = "任务接口")
	public BaseResponse<Map<String, Object>> index(@RequestHeader("token") String userIdToken, @RequestParam(required = false, defaultValue = "-1") int jobGroup) {
		Map<String, Object> model = new HashMap<>();
		UserInfo userInfo = userService.getUserInfoById(userIdToken);
		// 枚举-字典
		//model.put("ExecutorRouteStrategyEnum", ExecutorRouteStrategyEnum.values());    // 路由策略-列表

		List<Map<String, String>> exeNameList = new ArrayList<>();
		for (ExecutorRouteStrategyEnum u : ExecutorRouteStrategyEnum.values()) {
			Map<String, String> map = new HashMap<>();
			map.put("value", u.name());
			map.put("label", u.getTitle());
			exeNameList.add(map);
		}
		model.put("ExecutorRouteStrategyEnum", exeNameList);

		exeNameList = new ArrayList<>();
		for (GlueTypeEnum u : GlueTypeEnum.values()) {
			Map<String, String> map = new HashMap<>();
			map.put("value", u.name());
			map.put("label", u.getDesc());
			exeNameList.add(map);

		}
		//model.put("GlueTypeEnum", GlueTypeEnum.values());
		model.put("GlueTypeEnum", exeNameList);

		// Glue类型-字典
		//model.put("ExecutorBlockStrategyEnum", ExecutorBlockStrategyEnum.values());    // 阻塞处理策略-字典

		exeNameList = new ArrayList<>();
		for (ExecutorBlockStrategyEnum u : ExecutorBlockStrategyEnum.values()) {
			Map<String, String> map = new HashMap<>();
			map.put("value", u.name());
			map.put("label", u.getTitle());
			exeNameList.add(map);
		}
		model.put("ExecutorBlockStrategyEnum", exeNameList);
		// 任务组
		BaseMngPageResp<XxlJobGroup> jobGroupList = jobGroupService.getAllJobGroup(1, 5000, userInfo);
		model.put("JobGroupList", jobGroupList.getContentList());
		model.put("jobGroup", jobGroup);

		return BaseResponse.valueOfSuccess(model);
	}

	@RequestMapping(value = "/pageList", method = RequestMethod.POST)
	@ApiOperation(value = "任务管理列表分页", notes = "任务接口")
	@ActionOperateLog(eventName = "任务管理列表分页[pageList]", type = 0)
	public BaseResponse<BaseMngPageResp<XxlJobInfo>> pageList(@RequestBody JobInfoReq req) {
		int start = req.getCurrent();
		int length = req.getPageSize();
		int jobGroup = req.getJobGroup();
		String jobDesc = req.getJobDesc();
		String executorHandler = req.getExecutorHandler();
		String filterTime = req.getFilterTime();
		UserInfo userInfo = UserInfoContext.getUserInfo();
		String email = userInfo.getEmail();
		if (userService.isSuperAdmin(userInfo.getEmail())) {
			email = "";
		}
		String jobStatus=req.getJobStatus();
		String author=req.getAuthor();
		String jobCron=req.getJobCron();
		int jobId=req.getId();
		return BaseResponse.valueOfSuccess(xxlJobService.pageList(start, length, jobGroup,
				jobDesc, email, executorHandler, filterTime,jobStatus,author,jobCron,jobId));
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ApiOperation(value = "新增任务", notes = "任务接口")
	@ActionOperateLog(eventName = "新增任务[add]", type = 0)
	@AuditLimit(code = OperaCodeEnum.TASK_ADD)
	public BaseResponse<String> add(@RequestBody XxlJobInfo jobInfo) {
		return wrapResponse(xxlJobService.add(jobInfo));
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ApiOperation(value = "编辑", notes = "任务接口")
	@ActionOperateLog(eventName = "任务接口编辑[update]", type = 0)
	@AuditLimit(code = OperaCodeEnum.TASK_EDIT)
	public BaseResponse<String> update(@RequestBody XxlJobInfo jobInfo) {
		return wrapResponse(xxlJobService.update(jobInfo));
	}

	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	@ApiOperation(value = "删除", notes = "任务接口")
	@ActionOperateLog(eventName = "任务接口删除[remove]", type = 0)
	@AuditLimit(code = OperaCodeEnum.TASK_DELETE)
	public BaseResponse<String> remove(@RequestBody XxlJobInfo jobInfo) {
		return wrapResponse(xxlJobService.remove(jobInfo));
	}

	@RequestMapping(value = "/stop", method = RequestMethod.POST)
	@ApiOperation(value = "停止", notes = "任务接口")
	@ActionOperateLog(eventName = "任务接口停止[pause]", type = 0)
	@AuditLimit(code = OperaCodeEnum.TASK_STOP)
	public BaseResponse<String> pause(@RequestBody XxlJobInfo jobInfo) {
		return wrapResponse(xxlJobService.stop(jobInfo));
	}

	@RequestMapping(value = "/start", method = RequestMethod.POST)
	@ApiOperation(value = "启动", notes = "任务接口")
	@ActionOperateLog(eventName = "任务接口启动[start]", type = 0)
	@AuditLimit(code = OperaCodeEnum.TASK_START)
	public BaseResponse<String> start(@RequestBody XxlJobInfo jobInfo) {
		return wrapResponse(xxlJobService.start(jobInfo));
	}

	@RequestMapping(value = "/trigger", method = RequestMethod.POST)
	@ApiOperation(value = "执行", notes = "任务接口")
	@ActionOperateLog(eventName = "任务接口执行[triggerJob]", type = 0)
	@AuditLimit(code = OperaCodeEnum.TASK_EXECUTE)
	public BaseResponse<String> triggerJob(@RequestBody XxlJobInfo jobInfo) {
		return wrapResponse(xxlJobService.triggerJob(jobInfo));

	}

	private BaseResponse<String> wrapResponse(ReturnT<String> ret) {
		if (ret == ReturnT.FAIL || ret.getCode() == ReturnT.FAIL_CODE) {
			return BaseResponse.valueOfError(500, ret.getMsg());
		} else {
			BaseResponse<String> msg = BaseResponse.valueOfSuccess(ret.getData());
			return msg;
		}
	}

	@RequestMapping(value = "/getNextExecuteTime", method = RequestMethod.POST)
	@ApiOperation(value = "获取执行下次执行时间", notes = "任务接口")
	public BaseResponse<List<String>> getNextExecuteTime(@RequestBody XxlJobInfo jobInfo) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<String> list = new ArrayList<>(10);
		Date nextTimePoint = new Date();
		for (int i = 0; i < 3; i++) {
			CronExpression expression=new CronExpression(jobInfo.getJobCron());
			nextTimePoint =expression.getTimeAfter(nextTimePoint);
			list.add(sdf.format(nextTimePoint));
		}
		return BaseResponse.valueOfSuccess(list);
	}
}
