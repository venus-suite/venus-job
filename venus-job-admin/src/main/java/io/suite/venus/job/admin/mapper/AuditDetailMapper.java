package io.suite.venus.job.admin.mapper;


import io.suite.venus.job.admin.core.model.AuditDetail;
import org.apache.ibatis.annotations.Mapper;
import tk.mybatis.mapper.common.BaseMapper;

/**
 * <p>
 * 定时任务操作审核 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2021-08-04
 */
@Mapper
public interface AuditDetailMapper extends BaseMapper<AuditDetail> {


}
