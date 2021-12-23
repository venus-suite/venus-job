package io.suite.venus.job.admin.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class LogInfoReq  extends  BaseReq {
    private int jobGroup;
    private int jobId;
    private int logStatus;
    private String filterTime;
    private List<String> triggerTime;
    private  String email;
}
