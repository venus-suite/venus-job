package io.suite.venus.job.admin.mapper;


import io.suite.venus.job.admin.core.model.UserAdmin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.BaseMapper;

/**
 * <p>
 * job管理超级管理员 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2021-08-04
 */
@Mapper
public interface AdminMapper extends BaseMapper<UserAdmin> {
      UserAdmin  getByEmail(@Param("email") String email);
}
