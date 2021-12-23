package io.suite.venus.job.admin.service.impl;


import com.alibaba.fastjson.JSONObject;
import io.suite.venus.job.admin.common.constant.CommonContant;
import io.suite.venus.job.admin.common.exception.ExceptionType;
import io.suite.venus.job.admin.common.inout.BaseResponse;
import io.suite.venus.job.admin.common.util.AssertUtil;
import io.suite.venus.job.admin.core.model.VenusJobQrtzUser;
import io.suite.venus.job.admin.domain.dto.*;
import io.suite.venus.job.admin.service.LoginService;
import io.suite.venus.job.admin.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

    private static final Integer SESSION_TIMEOUT = 6 * 60 * 60;

    @Autowired
    private UserService userService;

    @Override
    public BaseResponse<String> loginIn(LoginReq reqData, HttpServletRequest request) {
        log.info("User login with info :"+ JSONObject.toJSONString(reqData));
        //根据用户名密码查询用户信息
        VenusJobQrtzUser userInfo = userService.getUserInfoForLogin(reqData.getUserAccount(),reqData.getPassword());
        if (userInfo == null){
            log.error("/admin/login invalid info:" + JSONObject.toJSONString(reqData));
            return BaseResponse.valueOfError(ExceptionType.USER_LOGIN_ERROR);
        }
        //生成token，更新表信息和缓存
        String genStr = String.format("%s#%s#%s", userInfo.getEmail(), userInfo.getName(), System.currentTimeMillis());
        String loginToken = DigestUtils.sha1Hex(genStr);

        userInfo.setToken(loginToken);
        userInfo.setTokenTime(System.currentTimeMillis());
        userInfo.setLastLoginTime(new Date());
        log.info("User login have new token:"+ loginToken);
        userService.addUserInfo(userInfo);

        String sId = request.getSession().getId();
        String userToken = userInfo.getToken();
        request.getSession().setAttribute(userToken, sId);
        log.info("Set userToken :" + userToken + "into session: " + sId);
        // 会话时间，单位秒
        request.getSession().setMaxInactiveInterval(SESSION_TIMEOUT);

        // 返回结果
        return BaseResponse.valueOfSuccess(userToken);
    }

    @Override
    public BaseResponse<?> loginOut(String userToken, HttpServletRequest request) {
        UserInfo userInfo = UserInfoContext.getUserInfo();
        AssertUtil.isTrue(userInfo != null, "注销异常,用户没有登录");
        userService.loginOut(userInfo);
        return BaseResponse.valueOfSuccess("注销成功");
    }

    /**
     * 根据token获取用户信息
     * @param reqData
     * @param request
     * @return
     */
    @Override
    public BaseResponse<LoginUserInfo> getConcurrentUserInfo(LoginTokenReq reqData, HttpServletRequest request){
        String userToken = reqData.getToken();
        LoginUserInfo loginUserInfo = new LoginUserInfo();
        //从本地缓存中取
        VenusJobQrtzUser currentUser = userService.getUserInfo(userToken);
        if (currentUser != null){
            BeanUtils.copyProperties(currentUser, loginUserInfo);
            loginUserInfo.setUserId(currentUser.getId());
            loginUserInfo.setUserName(currentUser.getName());
            return BaseResponse.valueOfSuccess(loginUserInfo);
        }
        //缓存中没有，数据库查询，比较tokenTime是否过期
        VenusJobQrtzUser dbCurrentUser = userService.getUserInfoByToken(userToken);
        if (dbCurrentUser != null){
            Long tokenTime = dbCurrentUser.getTokenTime();
            if (System.currentTimeMillis() > (tokenTime + CommonContant.LOGIN_CACHE_EXPIRE_TIME)){
                log.info("用户：{}的登陆token:{}已过期", dbCurrentUser.getName(),userToken);
                return BaseResponse.valueOfError(ExceptionType.TOKEN_TIMEOUT_ERROR);
            }
            //重新刷入缓存
            userService.refreshLoginCache(dbCurrentUser);

            BeanUtils.copyProperties(dbCurrentUser, loginUserInfo);
            loginUserInfo.setUserId(dbCurrentUser.getId());
            loginUserInfo.setUserName(dbCurrentUser.getName());
            return BaseResponse.valueOfSuccess(loginUserInfo);
        }
        //都没有记录，提示登录
        //注：这里可能会被刷
        log.error("token:{}的记录不存在", userToken);
        return BaseResponse.valueOfError(ExceptionType.TOKEN_TIMEOUT_ERROR);


    }
}
