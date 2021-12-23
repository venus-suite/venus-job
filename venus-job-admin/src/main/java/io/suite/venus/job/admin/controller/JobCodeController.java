package io.suite.venus.job.admin.controller;

import com.xxl.job.core.glue.GlueTypeEnum;
import io.suite.venus.job.admin.common.inout.BaseResponse;
import io.suite.venus.job.admin.common.log.ActionOperateLog;
import io.suite.venus.job.admin.core.model.XxlJobInfo;
import io.suite.venus.job.admin.core.model.XxlJobLogGlue;
import io.suite.venus.job.admin.core.util.I18nUtil;
import io.suite.venus.job.admin.domain.dto.JobCodeReq;
import io.suite.venus.job.admin.mapper.XxlJobInfoMapper;
import io.suite.venus.job.admin.mapper.XxlJobLogGlueMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**

 */
@RestController
@RequestMapping("/job/jobcode")
@Api(tags = "jobCode接口")
public class JobCodeController {
	
	@Resource
	private XxlJobInfoMapper xxlJobInfoMapper;
	@Resource
	private XxlJobLogGlueMapper xxlJobLogGlueMapper;

	@RequestMapping(value = "/",method = RequestMethod.POST)
	@ApiOperation(value = "jobCode接口枚举相关", notes = "jobCode接口")
	public BaseResponse<Map<String,Object>> index(@RequestBody JobCodeReq codeReq) {

		Map<String,Object> model=new HashMap<>();
		int jobId=codeReq.getId();
		XxlJobInfo jobInfo = xxlJobInfoMapper.loadById(jobId);
		List<XxlJobLogGlue> jobLogGlues = xxlJobLogGlueMapper.findByJobId(jobId);

		if (jobInfo == null) {
			return BaseResponse.valueOfError(500, I18nUtil.getString("jobinfo_glue_jobid_unvalid"));
		}
		if (GlueTypeEnum.BEAN == GlueTypeEnum.match(jobInfo.getGlueType())) {
			return BaseResponse.valueOfError(500,I18nUtil.getString("jobinfo_glue_gluetype_unvalid"));
		}

		// Glue类型-字典
		model.put("GlueTypeEnum", GlueTypeEnum.values());

		model.put("jobInfo", jobInfo);
		model.put("jobLogGlues", jobLogGlues);
		return  BaseResponse.valueOfSuccess(model) ;//"jobcode/jobcode.index";
	}
	
	@RequestMapping(value = "/save",method = RequestMethod.POST)
	@ApiOperation(value = "jobcode保存", notes = "jobCode接口")
	@ActionOperateLog(eventName = "jobcode保存[save]",type = 0)
	@ResponseBody
	public BaseResponse<String> save(@RequestBody JobCodeReq codeReq ) {
		// valid
		 int id=codeReq.getId();
		 String glueSource= codeReq.getGlueSource();
		 String glueRemark= codeReq.getGlueRemark();
		if (glueRemark==null) {
			return BaseResponse.valueOfError(500, (I18nUtil.getString("system_please_input") + I18nUtil.getString("jobinfo_glue_remark")) );
		}
		if (glueRemark.length()<4 || glueRemark.length()>100) {
			return BaseResponse.valueOfError(500, I18nUtil.getString("jobinfo_glue_remark_limit"));
		}
		XxlJobInfo exists_jobInfo = xxlJobInfoMapper.loadById(id);
		if (exists_jobInfo == null) {
			return BaseResponse.valueOfError(500, I18nUtil.getString("jobinfo_glue_jobid_unvalid"));
		}
		
		// update new code
		exists_jobInfo.setGlueSource(glueSource);
		exists_jobInfo.setGlueRemark(glueRemark);
		exists_jobInfo.setGlueUpdatetime(new Date());
		xxlJobInfoMapper.update(exists_jobInfo);

		// log old code
		XxlJobLogGlue xxlJobLogGlue = new XxlJobLogGlue();
		xxlJobLogGlue.setJobId(exists_jobInfo.getId());
		xxlJobLogGlue.setGlueType(exists_jobInfo.getGlueType());
		xxlJobLogGlue.setGlueSource(glueSource);
		xxlJobLogGlue.setGlueRemark(glueRemark);
		xxlJobLogGlueMapper.save(xxlJobLogGlue);

		// remove code backup more than 30
		xxlJobLogGlueMapper.removeOld(exists_jobInfo.getId(), 30);

		return BaseResponse.valueOfSuccess();
	}
	
}
