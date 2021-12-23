package io.suite.venus.job.admin.mapper;

import io.suite.venus.job.admin.core.model.XxlJobServerInfoHis;
import io.suite.venus.job.admin.common.inout.ServerInfoReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;

/***
 * 服务监控
 * hcmfys@163.com
 */

@Mapper
public interface XxlJobServerInfoHisMapper extends BaseMapper<XxlJobServerInfoHis> {

    @Select("<script>"+
            "select *  from  xxl_job_server_info_his where  server_name =#{req.serverName} " +
            "  <if test=\"req.startDate != null\">\n" +
            "AND create_time <![CDATA[ >= ]]> #{req.startDate}\n" +
            "</if>\n" +
            "<if test=\"req.endDate != null\">\n" +
            "AND create_time <![CDATA[ <= ]]> #{req.endDate}\n" +
            "</if> " +
            " order by id limit 2000 "
            + "</script>"
    )
    List<XxlJobServerInfoHis> getServerHisList(@Param("req") ServerInfoReq req );

}