package io.suite.venus.job.admin.service;


import io.suite.venus.job.admin.core.model.Namespace;
import io.suite.venus.job.admin.core.model.VenusJobQrtzUser;
import io.suite.venus.job.admin.domain.dto.UserInfo;

import java.util.List;

public interface UserService {

	VenusJobQrtzUser getUserInfoForLogin(String userAccount, String password);

	void addUserInfo(VenusJobQrtzUser userInfo);

	void refreshLoginCache(VenusJobQrtzUser userInfo);

	VenusJobQrtzUser getUserInfo(String userToken);

	UserInfo getUserInfoById(String userToken, boolean isThrow);

	UserInfo getUserInfoById(String userToken);

	VenusJobQrtzUser getCurrentUserInfo();

	VenusJobQrtzUser getUserInfoByToken(String userToken);

	Long getCurrentUserId();

	String getCurrentUsername();

	boolean isSuperAdmin(String userId);

	List<Namespace> getUserNamespaceList(UserInfo userInfo);


	public void loginOut(UserInfo userInfo);

}
