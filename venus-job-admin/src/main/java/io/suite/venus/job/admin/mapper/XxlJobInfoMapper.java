package io.suite.venus.job.admin.mapper;

import io.suite.venus.job.admin.core.model.XxlJobInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * job info
 *  origin @author xuxueli 2018-10-28 00:38:13
 *  * 修改 hcmfys@163.com
 */
@Mapper
public interface XxlJobInfoMapper {

	List<XxlJobInfo> pageList(@Param("offset") int offset,
							  @Param("pagesize") int pagesize,
							  @Param("jobGroup") int jobGroup,
							  @Param("jobDesc") String jobDesc,
							  @Param("email") String email,
							  @Param("executorHandler") String executorHandler,
							  @Param("jobStatus") String jobStatus,
							  @Param("author") String author,
							  @Param("jobCron") String jobCron,
							  @Param("jobId") int jobId
	);

	int pageListCount(@Param("offset") int offset,
					  @Param("pagesize") int pagesize,
					  @Param("jobGroup") int jobGroup,
					  @Param("jobDesc") String jobDesc,
					  @Param("executorHandler") String executorHandler);

	int save(XxlJobInfo info);

	XxlJobInfo loadById(@Param("id") int id);

	int update(XxlJobInfo item);

	int updateJobStatus(XxlJobInfo item);

	int delete(@Param("id") int id);

	List<XxlJobInfo> getJobsByGroup(@Param("jobGroup") int jobGroup);

	int findAllCount();

	List<XxlJobInfo> getJobsByIdList(@Param("idList") List<Integer> jobIdList);
}
