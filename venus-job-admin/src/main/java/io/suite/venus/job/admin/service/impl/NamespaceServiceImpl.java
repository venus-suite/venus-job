package io.suite.venus.job.admin.service.impl;


import io.suite.venus.job.admin.core.model.Namespace;
import io.suite.venus.job.admin.core.model.UserNamespace;

import io.suite.venus.job.admin.mapper.NamespaceMapper;
import io.suite.venus.job.admin.mapper.UserNamespaceMapper;
import io.suite.venus.job.admin.service.NamespaceService;
import io.suite.venus.job.admin.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.suite.venus.job.admin.domain.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class NamespaceServiceImpl  implements NamespaceService {

    @Autowired
    private NamespaceMapper namespaceMapper;

    @Autowired
    private UserNamespaceMapper userNamespaceMapper;
    @Autowired
    private UserService userService;

    @Override
    public BasePageResp<NamespaceAndUser> getNamespaceList(NamespaceReq req, UserInfo userInfo) {
        try {
            boolean isAdmin = userService.isSuperAdmin(userInfo.getEmail());
            if(!isAdmin){
                req.setEmail(userInfo.getEmail());
            }
            PageHelper.startPage(req.getCurrent(), req.getPageSize());
            List<Namespace> namespaceList = namespaceMapper.getNamespaceList(req);
            PageInfo<Namespace> pageInfo = new PageInfo<>(namespaceList);
            BasePageResp<NamespaceAndUser> basePageResp = new BasePageResp();
            basePageResp.setTotal(pageInfo.getTotal());
            basePageResp.setPages(pageInfo.getPages());
            List<NamespaceAndUser> userNamespaceList = new ArrayList<>();
            List<Long> namespaceIdList = new ArrayList<>();
            for (Namespace namespace : namespaceList) {
                namespaceIdList.add(namespace.getNamespaceId());
            }
            List<UserNamespace> userNamespaceIdList = null;
            if (!CollectionUtils.isEmpty(namespaceIdList)) {
                userNamespaceIdList = userNamespaceMapper.getUserNamespaceByIdList(namespaceIdList);
            }

            for (Namespace namespace : namespaceList) {
                NamespaceAndUser namespaceAndUser = new NamespaceAndUser();
                BeanUtils.copyProperties(namespace, namespaceAndUser);
                namespaceAndUser.setAdmin(isAdmin);
                namespaceAndUser.setUserNamespaceList(new ArrayList<>());
                if (userNamespaceIdList != null) {
                    for (UserNamespace userNamespace : userNamespaceIdList) {
                        if (Objects.equals(userNamespace.getNamespaceId(), namespace.getNamespaceId())) {
                            io.suite.venus.job.admin.domain.dto.UserNamespace up =
                                    new io.suite.venus.job.admin.domain.dto.UserNamespace();
                            up.setNamespaceId(userNamespace.getNamespaceId());
                            up.setEmail(userNamespace.getEmail());
                            up.setNamespace(namespace.getNamespace() + ":" + namespace.getService());
                            up.setNeedAudit(userNamespace.getNeedAudit());
                            namespaceAndUser.getUserNamespaceList().add(up);
                        }
                    }
                }
                userNamespaceList.add(namespaceAndUser);
            }
            basePageResp.setContentList(userNamespaceList);

            return basePageResp;
        } finally {
            PageHelper.clearPage();
        }
    }

    @Override
    public boolean addNamespace(NamespaceReq req) {

        Namespace namespace = new Namespace();
        BeanUtils.copyProperties(req, namespace);
        namespace.setCreateTime(LocalDateTime.now());
        namespace.setUpdateTime(LocalDateTime.now());
        namespace.setIsDeleted(0);
        return namespaceMapper.insertSelective(namespace) > 0;
    }

    @Override
    public boolean delNamespace(NamespaceReq req) {
        Namespace namespace = new Namespace();
        namespace.setNamespaceId(req.getNamespaceId());
        return namespaceMapper.deleteByPrimaryKey(namespace) > 0;
    }

    @Override
    public boolean updateNamespace(NamespaceReq req) {
        Namespace namespace = new Namespace();
        BeanUtils.copyProperties(req, namespace);
        namespace.setRemark(req.getRemark());
        namespace.setNoticeWebHook(req.getNoticeWebHook());
        namespace.setUpdateTime(LocalDateTime.now());
        return namespaceMapper.updateByPrimaryKeySelective(namespace) > 0;
    }

    @Override
    public boolean addNamespaceToUser(UserNamespaceReq req) {
        List<Long> namespaceList = req.getNamespaceIdList();
        List<String> emailList = req.getEmailList();
        for (String email : emailList) {
            namespaceList.stream().forEach(item->{
                UserNamespaceRightReq userReq=new UserNamespaceRightReq();
                userReq.setNamespaceId(item);
                userReq.setEmail(email);
                userNamespaceMapper.removeUserNamespace(userReq);
            });

            namespaceList.stream().forEach(item -> {
                UserNamespace namespace = new UserNamespace();
                namespace.setCreateTime(LocalDateTime.now());
                namespace.setEmail(email);
                namespace.setNamespaceId(item);
                namespace.setIsDeleted(0);
                namespace.setNeedAudit(req.getNeedAudit());
                namespace.setUserId(0L);
                userNamespaceMapper.insertSelective(namespace);

            });
        }
        return true;
    }

    @Override
    public boolean isExits(NamespaceReq req) {
        List<Namespace> lst = namespaceMapper.getNamespaceList(req);
        return !CollectionUtils.isEmpty(lst);
    }

    @Override
    public boolean isExitsNotMe(NamespaceReq req, long namespaceId) {
        List<Namespace> lst = namespaceMapper.getNamespaceList(req);
        if (!CollectionUtils.isEmpty(lst)) {
            for (Namespace namespace : lst) {
                if (namespace.getNamespaceId() != namespaceId) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Namespace getNamespaceByGroup(String groupName) {
        String namespace = namespaceMapper.getNamespaceByGroup(groupName);
        String arr[] = namespace.split("\\:");
        String name = "";
        String server = "";
        if (arr.length == 2) {
            name = arr[0];
            server = arr[1];
            return namespaceMapper.getNamespace(name, server);
        }
        return null;
    }

    @Override
    public Namespace getNamespaceById(long namespaceId) {
        return namespaceMapper.selectByPrimaryKey(namespaceId);
    }

    @Override
    public UserNamespace getUserNamespace(long userId, long namespaceId) {
        return userNamespaceMapper.getUserNamespace(userId, namespaceId);
    }

    @Override
    public UserNamespace getUserNamespaceByEmail(String email, long namespaceId) {

        UserNamespace userNamespace = userNamespaceMapper.getUserNamespaceByEmail(email, namespaceId);
        if (userNamespace == null) {
            boolean isAdmin = userService.isSuperAdmin(email);
            if (isAdmin) {
                userNamespace = new UserNamespace();
                userNamespace.setEmail(email);
                userNamespace.setNamespaceId(namespaceId);
                userNamespace.setNeedAudit(1);
                return userNamespace;
            }
        }
        return userNamespace;
    }

    @Override
    public Boolean removeUserNamespace(UserNamespaceRightReq req) {
        return userNamespaceMapper.removeUserNamespace(req) > 0;
    }

    @Override
    public List<JobGroupStat> getStatByJobGroup() {
        return namespaceMapper.getStatByJobGroup();
    }

}
