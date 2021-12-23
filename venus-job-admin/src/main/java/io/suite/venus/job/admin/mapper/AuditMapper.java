package io.suite.venus.job.admin.mapper;


import io.suite.venus.job.admin.core.model.Audit;
import io.suite.venus.job.admin.core.model.AuditDetail;
import io.suite.venus.job.admin.domain.dto.AuditReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;

/**
 * <p>
 * 定时任务操作审核 Mapper 接口
 * </p>
 * @author hcmfys@163.com
 * @since 2021-08-04
 */
@Mapper
public interface AuditMapper extends BaseMapper<Audit> {

    List<Audit> getAuditPageList(@Param("req") AuditReq req);

    List<AuditDetail> getAudioDetailByIdList(@Param("idList") List<Long> idList);

    void updateAudioStatus(@Param("auditId") Long auditId,@Param("status")  int status);
}
