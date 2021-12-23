package com.xxl.job.core.vm;

import cn.hutool.core.util.NumberUtil;
import lombok.Data;

import java.io.Serializable;

@Data
public class Mem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 内存总量
     */
    private double memTotal;

    /**
     * 已用内存
     */
    private double memUsed;

    /**
     * 剩余内存
     */
    private double memFree;

    public double getTotal() {
        return NumberUtil.div(memTotal, (1024 * 1024 * 1024), 2);
    }

    public double getUsed() {
        return NumberUtil.div(memUsed, (1024 * 1024 * 1024), 2);
    }


    public double getFree() {
        return NumberUtil.div(memFree, (1024 * 1024 * 1024), 2);
    }

    public double getUsage() {
        return NumberUtil.mul(NumberUtil.div(memUsed, memTotal, 4), 100);
    }
}