package io.suite.venus.job.admin.domain.dto;

import lombok.Data;

@Data
public class DashInfoRes {
    int jobInfoCount  ;
    int jobLogCount  ;
    int jobLogSuccessCount  ;
    int executorCount;
}
