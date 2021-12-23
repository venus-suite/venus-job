package io.suite.venus.job.admin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xxl.job.core.vm.Sys;
import io.suite.venus.job.admin.common.constant.CommonContant;
import io.suite.venus.job.admin.common.exception.ExceptionType;
import io.suite.venus.job.admin.common.inout.BaseResponse;
import io.suite.venus.job.admin.common.util.AESUtil;
import io.suite.venus.job.admin.common.util.AssertUtil;
import io.suite.venus.job.admin.core.model.Namespace;
import io.suite.venus.job.admin.core.model.UserAdmin;
import io.suite.venus.job.admin.core.model.VenusJobQrtzUser;
import io.suite.venus.job.admin.core.util.LocalCacheUtil;
import io.suite.venus.job.admin.domain.dto.UserInfo;
import io.suite.venus.job.admin.mapper.AdminMapper;
import io.suite.venus.job.admin.mapper.NamespaceMapper;
import io.suite.venus.job.admin.mapper.VenusJobQrtzUserMapper;
import io.suite.venus.job.admin.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {


	private static final String USER_ID_REDIS_FORMAT = "app-job-user:%s";

	@Autowired
	private AdminMapper adminMapper;

	@Autowired
	private NamespaceMapper namespaceMapper;

	@Autowired
	private VenusJobQrtzUserMapper venusJobQrtzUserMapper;

	@Override
	public VenusJobQrtzUser getUserInfoForLogin(String userAccount, String password) {
		String encryptPass = AESUtil.encrypt(password, CommonContant.PASSWORD_SALT);
		VenusJobQrtzUser userInfo = venusJobQrtzUserMapper.getUserInfoForLogin(userAccount,encryptPass);
		if (userInfo == null){
			return null;
		}
		return userInfo;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void addUserInfo(VenusJobQrtzUser userInfo) {
		//更新缓存
		String key = String.format(USER_ID_REDIS_FORMAT, userInfo.getToken());
		LocalCacheUtil.set(key, userInfo, CommonContant.LOGIN_CACHE_EXPIRE_TIME);
		//更新数据库
		VenusJobQrtzUser updateUser = new VenusJobQrtzUser();
		updateUser.setId(userInfo.getId());
		updateUser.setToken(userInfo.getToken());
		updateUser.setTokenTime(userInfo.getTokenTime());
		updateUser.setLastLoginTime(userInfo.getLastLoginTime());
		venusJobQrtzUserMapper.updateByPrimaryKeySelective(updateUser);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void refreshLoginCache(VenusJobQrtzUser userInfo) {
		//计算剩余的登录时间
		Long endTokenTime = userInfo.getTokenTime() + CommonContant.LOGIN_CACHE_EXPIRE_TIME;
		Long leftTokenTime = endTokenTime - System.currentTimeMillis();
		//更新缓存
		String key = String.format(USER_ID_REDIS_FORMAT, userInfo.getToken());
		LocalCacheUtil.set(key, userInfo, leftTokenTime);
	}

	@Override
	public void loginOut(UserInfo userInfo) {
		String key = String.format(USER_ID_REDIS_FORMAT, userInfo.getToken());
		LocalCacheUtil.set(key, null, 1);
	}

	@Override
	public VenusJobQrtzUser getUserInfo(String userToken) {
		String key = String.format(USER_ID_REDIS_FORMAT, userToken);
		VenusJobQrtzUser user = (VenusJobQrtzUser) LocalCacheUtil.get(key);
		return user;
	}

	@Override
	public UserInfo getUserInfoById(String userToken) {
		return getUserInfoById(userToken, true);
	}

	@Override
	public UserInfo getUserInfoById(String userToken, boolean isThrow) {
		String key = String.format(USER_ID_REDIS_FORMAT, userToken);
		VenusJobQrtzUser user = (VenusJobQrtzUser) LocalCacheUtil.get(key);
		//在数据库中再查一次
		if (user == null){
			VenusJobQrtzUser dbCurrentUser = this.getUserInfoByToken(userToken);
			if (dbCurrentUser != null){
				Long tokenTime = dbCurrentUser.getTokenTime();
				if (System.currentTimeMillis() > (tokenTime + CommonContant.LOGIN_CACHE_EXPIRE_TIME)){
					log.info("用户：{}的登陆token:{}已过期", dbCurrentUser.getName(),userToken);
					return null;
				}
				//重新刷入缓存
				this.refreshLoginCache(dbCurrentUser);
				user = dbCurrentUser;
			}
		}

		if (isThrow) {
			AssertUtil.isNotNull(user != null, "用户信息获取失败！");
		}
		if (user != null) {
			UserInfo userInfo = new UserInfo();
			userInfo.setEmail(user.getEmail());
			userInfo.setUserId(user.getId());
			userInfo.setUserName(user.getName());
			userInfo.setToken(user.getToken());
			return userInfo;
		} else {
			return null;
		}
	}

	/**
	 * 根据token查询用户基本信息
	 * @param userToken
	 * @return
	 */
	@Override
	public VenusJobQrtzUser getUserInfoByToken(String userToken){
		VenusJobQrtzUser loginUser = venusJobQrtzUserMapper.getUserInfoByToken(userToken);
		if (loginUser == null){
			log.info("根据token：{}查询不到用户信息",userToken);
			return null;
		}
		return loginUser;
	}

	@Override
	public VenusJobQrtzUser getCurrentUserInfo() {
		HttpServletRequest httpServletRequest = this.getHttpServletRequest();
		if (httpServletRequest != null) {
			String userToken = httpServletRequest.getHeader("token");
			log.info("当前用户token：{}", userToken);
			if (userToken != null) {
				HttpSession session = httpServletRequest.getSession(false);
				if (session == null) {
					log.warn("当前用户token：{}的session为空", userToken);
				} else {
					if (session.getAttribute(userToken) != null) {
						try {
							VenusJobQrtzUser userInfo = this.getUserInfo(userToken);
							log.info("当前用户信息：{}", JSONObject.toJSON(userInfo));
							return userInfo;
						} catch (Exception e) {
							log.info("当前用户获取信息失败：{}", userToken);
						}
					} else {
						log.info("当前用户获取session失败：{}", userToken);
						try {
							VenusJobQrtzUser userInfo = this.getUserInfo(userToken);
							log.info("当前用户信息：{}", JSONObject.toJSON(userInfo));
							return userInfo;
						} catch (Exception e) {
							log.info("当前用户获取信息失败：{}", userToken);
						}
					}
				}
			}
		}
		return null;
	}

	private HttpServletRequest getHttpServletRequest() {
		ServletRequestAttributes servletRequestAttributes = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes());
		if (servletRequestAttributes != null) {
			HttpServletRequest httpServletRequest = servletRequestAttributes.getRequest();
			return httpServletRequest;
		}
		return null;
	}

	@Override
	public Long getCurrentUserId() {
		VenusJobQrtzUser userInfo = this.getCurrentUserInfo();
		if (userInfo != null) {
			return userInfo.getId();
		}
		return 0L;
	}

	@Override
	public String getCurrentUsername() {
		VenusJobQrtzUser userInfo = this.getCurrentUserInfo();
		if (userInfo != null) {
			return userInfo.getName();
		}
		return "localUser";
	}

	@Override
	public boolean isSuperAdmin(String email) {
		UserAdmin userAdmin = adminMapper.getByEmail(email);
		return userAdmin != null;
	}

	@Override
	public List<Namespace> getUserNamespaceList(UserInfo userInfo) {
		if (isSuperAdmin(userInfo.getEmail())) {
			return namespaceMapper.selectAll();
		} else {
			return namespaceMapper.getNamespaceListByEmail(userInfo.getEmail());
		}
	}
}
