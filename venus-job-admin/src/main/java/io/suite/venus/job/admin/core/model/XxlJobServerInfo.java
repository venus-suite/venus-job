package io.suite.venus.job.admin.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * xxl_job_server_info
 * @author 
 */

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "xxl_job_server_info", description = "定时任务服务信息")
@Entity
@Table(name = "xxl_job_server_info")
public class XxlJobServerInfo implements Serializable {

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "server_name")
    private String serverName;

    @Column(name = "app_name")
    private String appName;

    /**
     * CPU核心数
     */
    @Column(name = "cpu_num")
    private Integer cpuNum;

    /**
     * CPU总的使用率
     */
    @Column(name = "cpu_total")
    private Double cpuTotal;

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
     * JDK版本
     */
    @Column(name = "jdk_version")
    private String jdkVersion;

    /**
     * JDK路径
     */
    @Column(name = "jdk_home")
    private String jdkHome;
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
     * 服务器名称
     */
    @Column(name = "sys_computerName")
    private String sysComputername;

    /**
     * 服务器Ip
     */
    @Column(name = "sys_computerIp")
    private String sysComputerip;

    /**
     * 项目路径
     */
    @Column(name = "sys_userDir")
    private String sysUserdir;

    /**
     * 操作系统
     */
    @Column(name = "sys_osName")
    private String sysOsname;

    /**
     * 系统架构
     */
    @Column(name = "sys_osArch")
    private String sysOsarch;

    /**
     * 盘符路径
     */
    @Column(name = "file_dirName")
    private String fileDirname;

    /**
     * 盘符类型
     */
    @Column(name = "file_sysTypeName")
    private String fileSystypename;

    /**
     * 文件类型
     */
    @Column(name = "file_typeName")
    private String fileTypename;

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
    private Date createTime;



    /**
     * 0=未删除，1=已删除
     */
    @Column(name = "is_deleted")

    private Integer isDeleted;

}