package io.suite.venus.job.admin.domain.dto;

public class UserInfoContext {

    private static ThreadLocal<UserInfo> threadLocal = new ThreadLocal<>();

    public static UserInfo getUserInfo() {
        return threadLocal.get();
    }

    public static void setUserInfo(UserInfo userInfo) {
        threadLocal.set(userInfo);
    }

    public  static  void clear(){
        threadLocal.remove();
    }
}
