package io.suite.venus.job.admin.domain.dto;

import lombok.Data;

@Data
public class AuditDetailReq<T> {
    private T data;
    private String code;
    private String type;
    private String method;
    private String desc;

}
