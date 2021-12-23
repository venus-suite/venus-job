package io.suite.venus.job.admin.mapper;

import io.suite.venus.job.admin.core.model.XxlJobGroup;
import io.suite.venus.job.admin.domain.dto.GroupInfoReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by xuxueli on 16/9/30.
 */
@Mapper
public interface XxlJobGroupMapper {

    List<XxlJobGroup> findAll();

    List<XxlJobGroup> findByAddressType(@Param("addressType") int addressType);

    int save(XxlJobGroup xxlJobGroup);

    int update(XxlJobGroup xxlJobGroup);

    int remove(@Param("id") int id);

    XxlJobGroup load(@Param("id") int id);

    List<XxlJobGroup> findByUserId(@Param("userId") Long userId);

    List<XxlJobGroup> findByUserIdAndName(@Param("email") String email,
                                          @Param("groupInfoReq") GroupInfoReq groupInfoReq);
}
