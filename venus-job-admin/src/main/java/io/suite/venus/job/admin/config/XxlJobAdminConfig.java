package io.suite.venus.job.admin.config;


import io.suite.venus.job.admin.service.RobotService;
import com.xxl.job.core.biz.AdminBiz;
import io.suite.venus.job.admin.mapper.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * xxl-job config
 *
 * @author xuxueli 2017-04-28
 */
@Configuration
public class XxlJobAdminConfig implements InitializingBean{
    private static XxlJobAdminConfig adminConfig = null;
    public static XxlJobAdminConfig getAdminConfig() {
        return adminConfig;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        adminConfig = this;
    }

    // conf

    @Value("${xxl.job.login.username:}")
    private String loginUsername;

    @Value("${xxl.job.login.password:}")
    private String loginPassword;

    @Value("${xxl.job.i18n:}")
    private String i18n;

    @Value("${xxl.job.accessToken:}")
    private String accessToken;

    @Value("${spring.mail.username:}")
    private String emailUserName;

    // dao, service

    @Resource
    private XxlJobLogMapper xxlJobLogMapper;
    @Resource
    private XxlJobInfoMapper xxlJobInfoMapper;
    @Resource
    private XxlJobRegistryMapper xxlJobRegistryMapper;
    @Resource
    private XxlJobGroupMapper xxlJobGroupMapper;
    @Resource
    private AdminBiz adminBiz;

    @Resource
    private NamespaceMapper namespaceMapper;
    @Resource
    private RobotService robotService;


    public String getLoginUsername() {
        return loginUsername;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public String getI18n() {
        return i18n;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getEmailUserName() {
        return emailUserName;
    }

    public XxlJobLogMapper getXxlJobLogDao() {
        return xxlJobLogMapper;
    }

    public XxlJobInfoMapper getXxlJobInfoDao() {
        return xxlJobInfoMapper;
    }

    public XxlJobRegistryMapper getXxlJobRegistryDao() {
        return xxlJobRegistryMapper;
    }

    public XxlJobGroupMapper getXxlJobGroupDao() {
        return xxlJobGroupMapper;
    }

    public AdminBiz getAdminBiz() {
        return adminBiz;
    }

    public NamespaceMapper  getNamespaceMapper(){
        return  namespaceMapper;
    }

    public RobotService getRobotService(){
        return  robotService;
    }

}
