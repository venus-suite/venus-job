package io.suite.venus.job.admin.common.inout;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

@Data
public class ServerInfoRes {

    @ApiModelProperty(value = "主键")

    private Long id;


    /**
     * CPU系统使用率
     */
    @Column(name = "cpu_sys")
    private Double cpuSys;

    /**
     * CPU用户使用率
     */
    @Column(name = "cpu_used")
    private Double cpuUsed;

    /**
     * CPU当前等待率
     */
    @Column(name = "cpu_wait")
    private Double cpuWait;

    /**
     * CPU当前空闲率
     */
    @Column(name = "cpu_free")
    private Double cpuFree;

    /**
     * 当前JVM占用的内存总数(M)
     */
    @Column(name = "jvm_total")
    private Double jvmTotal;

    /**
     * JVM最大可用内存总数(M)
     */
    @Column(name = "jvm_max")
    private Double jvmMax;

    /**
     * JVM空闲内存(M)
     */
    @Column(name = "jvm_free")
    private Double jvmFree;


    /**
     * jvm使用率
     */
    @Column(name = "jvm_Usage")
    private  double jvmUsage;
    /**
     * JDK运行时间
     */
    @Column(name = "jvm_RunTime")
    private  String jvmRunTime;

    /**
     * 内存总量
     */

    @Column(name = "mem_total")
    private Double memTotal;

    /**
     * 已用内存
     */
    @Column(name = "mem_used")
    private Double memUsed;

    /**
     * 内存使用率
     */
    @Column(name = "mem_usage")
    private Double memUsage;

    /**
     * 剩余内存
     */
    @Column(name = "mem_free")
    private Double memFree;

    /**
     * 总大小
     */
    @Column(name = "file_total")
    private String fileTotal;

    /**
     * 剩余大小
     */
    @Column(name = "file_free")
    private String fileFree;

    /**
     * 已经使用量
     */
    @Column(name = "file_used")
    private String fileUsed;

    /**
     * 资源的使用率
     */
    @Column(name = "file_usage")
    private Double fileUsage;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
}
