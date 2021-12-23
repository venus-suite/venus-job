package io.suite.venus.job.admin.domain.dto;

import lombok.Data;

@Data
public class JobCodeReq {
    private int id;
    private String glueSource;
    private String glueRemark;
}
