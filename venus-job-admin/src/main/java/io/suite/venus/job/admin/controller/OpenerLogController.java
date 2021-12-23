package io.suite.venus.job.admin.controller;

import io.suite.venus.job.admin.common.inout.BaseMngPageResp;
import io.suite.venus.job.admin.common.inout.BaseResponse;
import io.suite.venus.job.admin.domain.dto.*;
import io.suite.venus.job.admin.service.LogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "操作日志管理")
public class OpenerLogController {

    @Autowired
    private LogService logService;
    @RequestMapping(value = "/job/getOperaLogPageList", method = RequestMethod.POST)
    @ApiOperation(value = "操作日志分页", notes = "操作日志分页")
    public BaseResponse<BaseMngPageResp<OperaLogRes>> getOperaLogPageList(@RequestBody BaseReq req) {
        BaseMngPageResp<OperaLogRes> list = logService.getOperLogPageList(req);
        return BaseResponse.valueOfSuccess(list);// 分页列表

    }
}
