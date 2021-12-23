package io.suite.venus.job.admin.mapper;


import io.suite.venus.job.admin.domain.dto.JobGroupStat;
import io.suite.venus.job.admin.domain.dto.NamespaceReq;
import io.suite.venus.job.admin.core.model.Namespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 
 * @since 2021-08-04
 */
@Mapper
public interface  NamespaceMapper extends BaseMapper<Namespace> {

    /**
     * 获取用户拥有的命名空间
     * @param userId
     * @return
     */
    List<Namespace> getUserNamespaceList(@Param("userId") long userId);

    /**
     * 获取命名空间列表
     * @return
     */
    List<Namespace> getNamespaceList(@Param("req")  NamespaceReq req);


    String getNamespaceByGroup(@Param("regGroup")  String regGroup);

    Namespace getNamespace(@Param("name")  String name,  @Param("server")  String server);

    List<Namespace> getNamespaceListByEmail(@Param("email") String email);

    List<JobGroupStat> getStatByJobGroup();

}

