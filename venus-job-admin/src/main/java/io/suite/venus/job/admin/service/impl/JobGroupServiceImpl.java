package io.suite.venus.job.admin.service.impl;

import io.suite.venus.job.admin.common.inout.BaseResponse;
import io.suite.venus.job.admin.common.util.AssertUtil;
import io.suite.venus.job.admin.config.XxlJobAdminConfig;
import io.suite.venus.job.admin.core.model.Namespace;
import io.suite.venus.job.admin.core.model.XxlJobGroup;
import io.suite.venus.job.admin.core.model.XxlJobRegistry;
import io.suite.venus.job.admin.core.util.I18nUtil;
import io.suite.venus.job.admin.domain.dto.GroupInfoReq;
import io.suite.venus.job.admin.domain.dto.UserInfo;
import io.suite.venus.job.admin.common.inout.BaseMngPageResp;
import io.suite.venus.job.admin.mapper.XxlJobGroupMapper;
import io.suite.venus.job.admin.mapper.XxlJobInfoMapper;
import io.suite.venus.job.admin.service.JobGroupService;
import io.suite.venus.job.admin.service.NamespaceService;
import io.suite.venus.job.admin.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxl.job.core.enums.RegistryConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Service
public class JobGroupServiceImpl  implements JobGroupService {
    @Autowired
    private UserService userService;

    @Autowired
    private XxlJobGroupMapper jobGroupMapper;

    @Autowired
    private XxlJobGroupMapper xxlJobGroupMapper;

    @Autowired
    private XxlJobInfoMapper xxlJobInfoMapper;
    @Autowired
    private NamespaceService namespaceService;

    @Override
    public BaseMngPageResp<XxlJobGroup> getAllJobGroup(GroupInfoReq groupInfoReq, UserInfo userInfo) {
        try {
            List<XxlJobGroup> list = null;
            String email = userInfo.getEmail();
            if (userService.isSuperAdmin(userInfo.getEmail())) {
                email = null;
            }
            PageHelper.startPage(groupInfoReq.getCurrent(), groupInfoReq.getPageSize());
            list = jobGroupMapper.findByUserIdAndName(email, groupInfoReq);
            PageInfo<XxlJobGroup> pageInfo = new PageInfo<>(list);
            BaseMngPageResp<XxlJobGroup> resp = new BaseMngPageResp<>();
            resp.setTotal(pageInfo.getTotal());
            resp.setPages(pageInfo.getPages());
            resp.setContentList(list);
            return resp;
        } finally {
            PageHelper.clearPage();
        }
    }

    @Override
    public BaseMngPageResp<XxlJobGroup> getAllJobGroup(int start, int length, UserInfo userInfo) {
        GroupInfoReq groupInfoReq = new GroupInfoReq();
        groupInfoReq.setCurrent(start);
        groupInfoReq.setPageSize(length);
        return getAllJobGroup(groupInfoReq, userInfo);

    }

    @Override
    public BaseResponse<String> save(XxlJobGroup xxlJobGroup) {
        Namespace namespace = namespaceService.getNamespaceById(xxlJobGroup.getNamespaceId());
        AssertUtil.isTrue(namespace != null, "命名空间不能为空！");
        xxlJobGroup.setNamespace(namespace.getService() + ":" + namespace.getService());
        // valid
        if (xxlJobGroup.getAppName() == null || xxlJobGroup.getAppName().trim().length() == 0) {
            return BaseResponse.valueOfError(500, (I18nUtil.getString("system_please_input") + "AppName"));
        }
        if (xxlJobGroup.getAppName().length() < 4 || xxlJobGroup.getAppName().length() > 64) {
            return BaseResponse.valueOfError(500, I18nUtil.getString("jobgroup_field_appName_length"));
        }

        if (xxlJobGroup.getNamespace() == null || xxlJobGroup.getNamespace().trim().length() == 0) {
            return BaseResponse.valueOfError(500, "命名空间不能为空！");
        }
        if (xxlJobGroup.getNamespaceId() == null || xxlJobGroup.getNamespaceId() < 0) {
            return BaseResponse.valueOfError(500, "命名空间不能为空！");
        }
        if (xxlJobGroup.getTitle() == null || xxlJobGroup.getTitle().trim().length() == 0) {
            return BaseResponse.valueOfError(500, (I18nUtil.getString("system_please_input") + I18nUtil.getString("jobgroup_field_title")));
        }
        if (xxlJobGroup.getAddressType() != 0) {
            if (xxlJobGroup.getAddressList() == null || xxlJobGroup.getAddressList().trim().length() == 0) {
                return BaseResponse.valueOfError(500, I18nUtil.getString("jobgroup_field_addressType_limit"));
            }
            String[] addresss = xxlJobGroup.getAddressList().split(",");
            for (String item : addresss) {
                if (item == null || item.trim().length() == 0) {
                    return BaseResponse.valueOfError(500, I18nUtil.getString("jobgroup_field_registryList_unvalid"));
                }
            }
        }

        int ret = xxlJobGroupMapper.save(xxlJobGroup);
        return (ret > 0) ? BaseResponse.valueOfSuccess() : BaseResponse.valueOfSysError();
    }

    @Override
    public BaseResponse<String> update(XxlJobGroup xxlJobGroup) {
        // valid
        if (xxlJobGroup.getAppName() == null || xxlJobGroup.getAppName().trim().length() == 0) {
            return BaseResponse.valueOfError(500, (I18nUtil.getString("system_please_input") + "AppName"));
        }
        if (xxlJobGroup.getAppName().length() < 4 || xxlJobGroup.getAppName().length() > 64) {
            return BaseResponse.valueOfError(500, I18nUtil.getString("jobgroup_field_appName_length"));
        }
        if (xxlJobGroup.getTitle() == null || xxlJobGroup.getTitle().trim().length() == 0) {
            return BaseResponse.valueOfError(500, (I18nUtil.getString("system_please_input") + I18nUtil.getString("jobgroup_field_title")));
        }
        if (xxlJobGroup.getAddressType() == 0) {
            // 0=自动注册
            List<String> registryList = findRegistryByAppName(xxlJobGroup.getAppName());
            String addressListStr = null;
            if (registryList != null && !registryList.isEmpty()) {
                Collections.sort(registryList);
                addressListStr = "";
                for (String item : registryList) {
                    addressListStr += item + ",";
                }
                addressListStr = addressListStr.substring(0, addressListStr.length() - 1);
            }
            xxlJobGroup.setAddressList(addressListStr);
        } else {
            // 1=手动录入
            if (xxlJobGroup.getAddressList() == null || xxlJobGroup.getAddressList().trim().length() == 0) {
                return BaseResponse.valueOfError(500, I18nUtil.getString("jobgroup_field_addressType_limit"));
            }
            String[] addresss = xxlJobGroup.getAddressList().split(",");
            for (String item : addresss) {
                if (item == null || item.trim().length() == 0) {
                    return BaseResponse.valueOfError(500, I18nUtil.getString("jobgroup_field_registryList_unvalid"));
                }
            }
        }

        int ret = xxlJobGroupMapper.update(xxlJobGroup);
        return (ret > 0) ? BaseResponse.valueOfSuccess() : BaseResponse.valueOfSysError();
    }

    @Override
    public BaseResponse<String> remove(XxlJobGroup xxlJobGroup) {
        int id = xxlJobGroup.getId();
        // valid
        int count = xxlJobInfoMapper.pageListCount(0, 10, id, null, null);
        if (count > 0) {
            return BaseResponse.valueOfError(500, I18nUtil.getString("jobgroup_del_limit_0"));
        }

        List<XxlJobGroup> allList = xxlJobGroupMapper.findAll();
        if (allList.size() == 1) {
            return BaseResponse.valueOfError(500, I18nUtil.getString("jobgroup_del_limit_1"));
        }

        int ret = xxlJobGroupMapper.remove(id);
        return (ret > 0) ? BaseResponse.valueOfSuccess() : BaseResponse.valueOfSysError();
    }

    @Override
    public XxlJobGroup getGroupById(int jobGroupId) {
        return jobGroupMapper.load(jobGroupId);
    }


    private List<String> findRegistryByAppName(String appNameParam) {
        HashMap<String, List<String>> appAddressMap = new HashMap<String, List<String>>();
        List<XxlJobRegistry> list = XxlJobAdminConfig.getAdminConfig().getXxlJobRegistryDao().findAll(RegistryConfig.DEAD_TIMEOUT);
        if (list != null) {
            for (XxlJobRegistry item : list) {
                if (RegistryConfig.RegistType.EXECUTOR.name().equals(item.getRegistryGroup())) {
                    String appName = item.getRegistryKey();
                    List<String> registryList = appAddressMap.get(appName);
                    if (registryList == null) {
                        registryList = new ArrayList<String>();
                    }

                    if (!registryList.contains(item.getRegistryValue())) {
                        registryList.add(item.getRegistryValue());
                    }
                    appAddressMap.put(appName, registryList);
                }
            }
        }
        return appAddressMap.get(appNameParam);
    }

}
