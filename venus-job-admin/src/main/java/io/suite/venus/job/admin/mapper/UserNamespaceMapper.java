package io.suite.venus.job.admin.mapper;


import io.suite.venus.job.admin.core.model.UserNamespace;
import io.suite.venus.job.admin.domain.dto.UserNamespaceRightReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hcmfys@163.com
 * @since 2021-08-04
 */
@Mapper
public interface UserNamespaceMapper extends BaseMapper<UserNamespace> {

    int deleteByUserId(@Param("email") String email);

    UserNamespace getUserNamespace(@Param("userId") long userId, @Param("namespaceId") long namespaceId);

    UserNamespace getUserNamespaceByEmail(@Param("email") String email, @Param("namespaceId") long namespaceId);

    int removeUserNamespace(@Param("req") UserNamespaceRightReq req);

    List<UserNamespace> getUserNamespaceByIdList(@Param("idList")List<Long> idList);
}

