package com.xxl.job.core.vm;

import cn.hutool.core.util.NumberUtil;
import lombok.Data;

import java.io.Serializable;

@Data
public class Cpu implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 核心数
     */
    private int cpuNum;

    /**
     * CPU总的使用率
     */
    private double cpuTotal;

    /**
     * CPU系统使用率
     */
    private double cpuSys;

    /**
     * CPU用户使用率
     */
    private double cpuUsed;

    /**
     * CPU当前等待率
     */
    private double cpuWait;

    /**
     * CPU当前空闲率
     */
    private double cpuFree;


    public double getTotal() {
        return NumberUtil.round(NumberUtil.mul(cpuTotal, 100), 2).doubleValue();
    }

    public double getSys() {
        return NumberUtil.round(NumberUtil.mul(cpuSys / cpuTotal, 100), 2).doubleValue();
    }

    public double getUsed() {
        return NumberUtil.round(NumberUtil.mul(cpuUsed / cpuTotal, 100), 2).doubleValue();
    }

    public double getWait() {
        return NumberUtil.round(NumberUtil.mul(cpuWait / cpuTotal, 100), 2).doubleValue();
    }

    public double getFree() {
        return NumberUtil.round(NumberUtil.mul(cpuFree / cpuTotal, 100), 2).doubleValue();
    }
}