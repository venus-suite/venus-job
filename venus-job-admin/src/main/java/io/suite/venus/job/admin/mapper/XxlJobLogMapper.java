package io.suite.venus.job.admin.mapper;

import io.suite.venus.job.admin.core.model.XxlJobLog;
import io.suite.venus.job.admin.domain.dto.OperaLogRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * job log
 * @author xuxueli 2016-1-12 18:03:06
 */
@Mapper
public interface XxlJobLogMapper {

	// exist jobId not use jobGroup, not exist use jobGroup
	List<XxlJobLog> pageList(@Param("offset") int offset,
							 @Param("pagesize") int pagesize,
							 @Param("jobGroup") int jobGroup,
							 @Param("jobId") int jobId,
							 @Param("triggerTimeStart") Date triggerTimeStart,
							 @Param("triggerTimeEnd") Date triggerTimeEnd,
							 @Param("logStatus") int logStatus,
							 @Param("email") String email
	);

	int pageListCount(@Param("offset") int offset,
					  @Param("pagesize") int pagesize,
					  @Param("jobGroup") int jobGroup,
					  @Param("jobId") int jobId,
					  @Param("triggerTimeStart") Date triggerTimeStart,
					  @Param("triggerTimeEnd") Date triggerTimeEnd,
					  @Param("logStatus") int logStatus);

	XxlJobLog load(@Param("id") int id);

	int save(@Param("xxlJobLog") XxlJobLog xxlJobLog);

	int updateTriggerInfo(@Param("xxlJobLog") XxlJobLog xxlJobLog);

	int updateHandleInfo(@Param("xxlJobLog") XxlJobLog xxlJobLog);

	int delete(@Param("jobId") int jobId);

	int triggerCountByHandleCode(@Param("handleCode") int handleCode);

	List<Map<String, Object>> triggerCountByDay(@Param("from") Date from,
												@Param("to") Date to);

	int clearLog(@Param("jobGroup") int jobGroup,
				 @Param("jobId") int jobId,
				 @Param("clearBeforeTime") Date clearBeforeTime,
				 @Param("clearBeforeNum") int clearBeforeNum);

	List<Integer> findFailJobLogIds(@Param("pagesize") int pagesize);

	int updateAlarmStatus(@Param("logId") int logId,
						  @Param("oldAlarmStatus") int oldAlarmStatus,
						  @Param("newAlarmStatus") int newAlarmStatus);


	int clearLogByDay(@Param("clearBeforeTime") Date clearBeforeTime);


	int clearServerLogByDay(@Param("clearBeforeTime") Date clearBeforeTime);


    List<OperaLogRes> getOperLogPageList(@Param("offset") int offset,
										 @Param("pagesize") int pagesize);

}
