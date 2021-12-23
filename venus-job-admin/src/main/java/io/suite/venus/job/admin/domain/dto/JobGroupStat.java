package io.suite.venus.job.admin.domain.dto;

import lombok.Data;

@Data
public class JobGroupStat {
    private int jobGroup;
    private String jobStatus;
    private int groupCount;
}
