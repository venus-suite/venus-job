package io.suite.venus.job.admin.controller;

import io.suite.venus.job.admin.common.inout.BaseResponse;
import io.suite.venus.job.admin.common.util.AssertUtil;
import io.suite.venus.job.admin.core.model.XxlJobServerInfo;
import io.suite.venus.job.admin.domain.dto.BasePageResp;
import io.suite.venus.job.admin.domain.dto.BaseReq;
import io.suite.venus.job.admin.domain.dto.UserInfo;
import io.suite.venus.job.admin.domain.dto.UserInfoContext;
import io.suite.venus.job.admin.service.VmService;
import com.xxl.job.core.vm.SystemInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "job cpu推送到服务")
public class VmController {
    @Autowired
    private VmService vmService;

    @ApiOperation(value = "推送到服务")
    @RequestMapping(value = "/vm/add", method = RequestMethod.POST)

    public BaseResponse<Boolean> add(@RequestBody SystemInfo systemInfo) {
        return  BaseResponse.valueOfSuccess(vmService.add(systemInfo));
    }



    @RequestMapping(value = "/job/getServerList", method = RequestMethod.POST)
    @ApiOperation(value = "server列表", notes = "server列表")

    public BaseResponse<BasePageResp<XxlJobServerInfo>> getServerList(@RequestBody  BaseReq req) {
        UserInfo userInfo = UserInfoContext.getUserInfo();
        AssertUtil.isTrue(req.getCurrent() > 0, "页面必须大于0");
        AssertUtil.isTrue(req.getPageSize() > 0, "pageSize必须大于0");
        return BaseResponse.valueOfSuccess(vmService.getServerList(req, userInfo));
    }

}
