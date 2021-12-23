package io.suite.venus.job.admin.service;


import io.suite.venus.job.admin.common.inout.BaseResponse;
import io.suite.venus.job.admin.domain.dto.LoginReq;
import io.suite.venus.job.admin.domain.dto.LoginTokenReq;
import io.suite.venus.job.admin.domain.dto.LoginUserInfo;

import javax.servlet.http.HttpServletRequest;

public interface LoginService {
    BaseResponse<String> loginIn(LoginReq reqData, HttpServletRequest request);
    BaseResponse<?> loginOut(String userToken, HttpServletRequest request);
    BaseResponse<LoginUserInfo> getConcurrentUserInfo(LoginTokenReq reqData, HttpServletRequest request);
}
