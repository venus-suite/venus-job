package io.suite.venus.job.admin.service;

import io.suite.venus.job.admin.core.model.Namespace;
import io.suite.venus.job.admin.core.model.UserNamespace;

import io.suite.venus.job.admin.domain.dto.*;

import java.util.List;

public interface NamespaceService {
    BasePageResp<NamespaceAndUser> getNamespaceList(NamespaceReq req, UserInfo  userInfo);

    boolean addNamespace(NamespaceReq req);

    boolean delNamespace(NamespaceReq req);

    boolean updateNamespace(NamespaceReq req);

    boolean addNamespaceToUser(UserNamespaceReq req);

    boolean isExits(NamespaceReq req);

    boolean isExitsNotMe(NamespaceReq req, long namespaceId );

    Namespace getNamespaceByGroup(String groupName);

    Namespace getNamespaceById(long namespaceId);

    UserNamespace getUserNamespace(long userId,long namespaceId);

    UserNamespace getUserNamespaceByEmail(String email,long namespaceId);

    Boolean removeUserNamespace(UserNamespaceRightReq req);

    List<JobGroupStat> getStatByJobGroup();
}
