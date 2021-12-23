package io.suite.venus.job.admin.controller;


import io.suite.venus.job.admin.common.inout.BaseResponse;
import io.suite.venus.job.admin.common.log.ActionOperateLog;
import io.suite.venus.job.admin.common.util.AssertUtil;
import io.suite.venus.job.admin.domain.dto.BasePageResp;
import io.suite.venus.job.admin.domain.dto.VenusJobUserQueryReq;
import io.suite.venus.job.admin.domain.dto.VenusJobUserReq;
import io.suite.venus.job.admin.domain.dto.VenusJobUserResp;
import io.suite.venus.job.admin.service.VenusJobUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author author
 * @since 2021-12-17
 */
@RestController
@RequestMapping("/job/jobUser")
@Api(tags = "用户管理")
public class VenusJobUserController {

    @Autowired
    private VenusJobUserService venusJobUserService;

    @PostMapping("/addVenusJobUser")
    @ApiOperation(value = "新增用户")
    @ActionOperateLog(eventName = "新增用户[addVenusJobUser]", type = 0)
    public BaseResponse<?> addVenusJobUser(@Validated @RequestBody VenusJobUserReq reqData) {
        AssertUtil.isTrue(reqData.getEmail() != null, "邮箱不能为空");
        AssertUtil.isTrue(reqData.getName() != null, "姓名不能为空");
        AssertUtil.isTrue(reqData.getPassword() != null, "密码不能为空");
        venusJobUserService.addVenusJobUser(reqData);
        return BaseResponse.valueOfSuccess();
    }

    @PostMapping("/updateVenusJobUser")
    @ApiOperation(value = "编辑用户")
    @ActionOperateLog(eventName = "编辑用户[updateVenusJobUser]", type = 0)
    public BaseResponse<?> updateVenusJobUser(@Validated @RequestBody VenusJobUserReq reqData) {
        AssertUtil.isTrue(reqData.getId() > 0, "id不能为空");
        venusJobUserService.updateVenusJobUser(reqData);
        return BaseResponse.valueOfSuccess();
    }

    @PostMapping("/deleteVenusJobUser")
    @ApiOperation(value = "删除用户")
    @ActionOperateLog(eventName = "删除用户[deleteVenusJobUser]", type = 0)
    public BaseResponse<?> deleteVenusJobUser(@Validated @RequestBody VenusJobUserReq reqData) {
        AssertUtil.isTrue(reqData.getId() > 0, "id不能为空");
        venusJobUserService.deleteVenusJobUser(reqData);
        return BaseResponse.valueOfSuccess();
    }

    @PostMapping("/queryVenusJobUserPage")
    @ApiOperation(value = "分页查询用户列表")
    @ActionOperateLog(eventName = "分页查询用户列表[queryVenusJobUserPage]", type = 0)
    public BaseResponse<BasePageResp<VenusJobUserResp>> queryVenusJobUserPage(@Validated @RequestBody VenusJobUserQueryReq reqData) {
        AssertUtil.isTrue(reqData.getCurrent() > 0, "页面必须大于0");
        AssertUtil.isTrue(reqData.getPageSize() > 0, "pageSize必须大于0");

        BasePageResp<VenusJobUserResp> respPage =  venusJobUserService.queryVenusJobUserPage(reqData);
        return BaseResponse.valueOfSuccess(respPage);
    }

}
