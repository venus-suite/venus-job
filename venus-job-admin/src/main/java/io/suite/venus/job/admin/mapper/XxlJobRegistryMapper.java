package io.suite.venus.job.admin.mapper;

import io.suite.venus.job.admin.core.model.XxlJobRegistry;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by xuxueli on 16/9/30.
 */
@Mapper
 public  interface XxlJobRegistryMapper {

     int removeDead(@Param("timeout") int timeout);

     List<XxlJobRegistry> findAll(@Param("timeout") int timeout);

     int registryUpdate(@Param("registryGroup") String registryGroup,
                              @Param("namespace") String namespace,
                              @Param("registryKey") String registryKey,
                              @Param("registryValue") String registryValue);

     int registrySave(@Param("registryGroup") String registryGroup,
                            @Param("namespace") String namespace,
                            @Param("registryKey") String registryKey,
                            @Param("registryValue") String registryValue);

     int registryDelete(@Param("registryGroup") String registGroup,
                              @Param("namespace") String namespace,
                          @Param("registryKey") String registryKey,
                          @Param("registryValue") String registryValue);

}
