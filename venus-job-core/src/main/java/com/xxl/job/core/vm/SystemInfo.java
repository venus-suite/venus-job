package com.xxl.job.core.vm;

import lombok.Data;

@Data
public class SystemInfo {
    /**
     * CPU相关信息
     */
    private Cpu cpu;

    /**
     * 內存相关信息
     */
    private Mem mem  ;

    /**
     * JVM相关信息
     */
    private Jvm jvm ;

    /**
     * 服务器相关信息
     */
    private Sys sys ;

    private  TotalFile file;

    private  String appName;

    private String server;
}
