package io.suite.venus.job.admin.mapper;

import io.suite.venus.job.admin.core.model.VenusJobQrtzUser;
import io.suite.venus.job.admin.domain.dto.VenusJobUserQueryReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2021-12-17
 */
@Mapper
public interface VenusJobQrtzUserMapper extends BaseMapper<VenusJobQrtzUser> {

    VenusJobQrtzUser getUserInfoForLogin(@Param("userAccount")String userAccount, @Param("password")String password);

    VenusJobQrtzUser getUserInfoByToken(@Param("userToken")String userToken);

    List<VenusJobQrtzUser> getUserInfoPageList(@Param("req") VenusJobUserQueryReq req);
}
