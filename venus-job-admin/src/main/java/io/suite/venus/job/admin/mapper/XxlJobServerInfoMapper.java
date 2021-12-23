package io.suite.venus.job.admin.mapper;

import io.suite.venus.job.admin.core.model.XxlJobServerInfo;
import io.suite.venus.job.admin.domain.dto.BaseReq;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;


@Mapper
public interface XxlJobServerInfoMapper extends BaseMapper<XxlJobServerInfo> {

    @Delete("delete from  xxl_job_server_info where server_name=#{server}")
    int deleteByServerName(@Param("server") String server);

    @Select("select *  from  xxl_job_server_info where 1=1 ")
    List<XxlJobServerInfo> getServerList(@Param("req") BaseReq req);

    @Select("<script>" +
            "select *  from  xxl_job_server_info where  server_name  in " +
            " <foreach collection=\"idList\" index=\"index\" item=\"item\" open=\"(\" separator=\",\" close=\")\">\n" +
            "            #{item}\n" +
            "        </foreach>" +
            "</script> "
    )
    List<XxlJobServerInfo>  getServerListByAddressList(@Param("idList")  List<String> addressList);

}