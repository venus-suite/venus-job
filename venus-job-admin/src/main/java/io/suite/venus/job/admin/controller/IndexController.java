package io.suite.venus.job.admin.controller;

import io.suite.venus.job.admin.common.inout.BaseResponse;
import io.suite.venus.job.admin.common.util.AssertUtil;
import io.suite.venus.job.admin.core.model.XxlJobServerInfoHis;
import io.suite.venus.job.admin.core.util.LocalCacheUtil;
import io.suite.venus.job.admin.domain.dto.DashboardInfo;
import io.suite.venus.job.admin.domain.dto.UserInfo;
import io.suite.venus.job.admin.domain.dto.UserInfoContext;
import io.suite.venus.job.admin.common.inout.ServerInfoReq;
import io.suite.venus.job.admin.common.inout.ServerInfoRes;
import io.suite.venus.job.admin.service.LogService;
import io.suite.venus.job.admin.service.StatLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**

 */
@RestController
@Api(tags = "index统计主页接口")
@RequestMapping("/job/")
public class IndexController {

	@Autowired
	private LogService logService;

	@Autowired
	private StatLogService statLogService;


	@RequestMapping(value = "/stat/dashboard", method = RequestMethod.POST)
	@ApiOperation(value = "chartInfo图表接口", notes = "index主页接口")
	public BaseResponse<DashboardInfo> dashboard() {
		DashboardInfo ds = statLogService.getDashInfo();
		return BaseResponse.valueOfSuccess(ds);
	}


	@RequestMapping(value = "/stat/getHisByName", method = RequestMethod.POST)
	@ApiOperation(value = "获取历史记录", notes = "index主页接口")
	public BaseResponse<List<ServerInfoRes>> getNamespaceList(@RequestBody ServerInfoReq req) {
		UserInfo userInfo = UserInfoContext.getUserInfo();
		AssertUtil.isTrue(!StringUtils.isEmpty(req.getServerName()), "severName不能为空！");
		List<ServerInfoRes> resList=new ArrayList<ServerInfoRes>();
		List<XxlJobServerInfoHis> hisList=logService.getServerHisPage(req, userInfo);
		if(!CollectionUtils.isEmpty(hisList)) {
			for(XxlJobServerInfoHis his:hisList ) {
				ServerInfoRes res=new ServerInfoRes();
				BeanUtils.copyProperties( his,res);
				resList.add(res);
			}
		}
		return BaseResponse.valueOfSuccess(resList);
	}


	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

}
