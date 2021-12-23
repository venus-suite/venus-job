package io.suite.venus.job.admin.controller;


import io.suite.venus.job.admin.common.inout.BaseResponse;
import io.suite.venus.job.admin.common.log.ActionOperateLog;
import io.suite.venus.job.admin.domain.dto.LoginReq;
import io.suite.venus.job.admin.domain.dto.LoginTokenReq;
import io.suite.venus.job.admin.domain.dto.LoginUserInfo;
import io.suite.venus.job.admin.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Api(tags = "登录模块")
public class LoginController {

    @Autowired
    private LoginService loginService;

    /**
     * 个人登录
     *
     * @param reqData 用户名密码
     * @return
     */
    @ApiOperation(value = "登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ActionOperateLog(eventName = "登录[login]",type = 0)
    public BaseResponse<String> loginIn(@RequestBody LoginReq reqData, HttpServletRequest request) {
        return this.loginService.loginIn(reqData, request);
    }

    /**
     * token获取用户基本信息
     *
     * @param reqData 用户名密码
     * @return
     */
    @ApiOperation(value = "token获取用户基本信息")
    @RequestMapping(value = "/loginToken", method = RequestMethod.POST)
    @ActionOperateLog(eventName = "token获取用户基本信息[loginToken]",type = 0)
    public BaseResponse<LoginUserInfo> getConcurrentUserInfo(@RequestBody LoginTokenReq reqData, HttpServletRequest request) {
        return this.loginService.getConcurrentUserInfo(reqData, request);
    }

    /**
     * 注销登录
     *
     * @param userToken 用户token
     * @return
     */
    @ApiOperation(value = "注销")
    @RequestMapping(value = "/job/loginOut", method = RequestMethod.POST)
    @ActionOperateLog(eventName = "注销[loginOut]",type = 0)
    public BaseResponse<?> loginOut(@RequestHeader(name = "token") String userToken, HttpServletRequest request) {
        return loginService.loginOut(userToken, request);
    }
}

