package io.suite.venus.job.admin.common.exception;

import lombok.Data;

@Data
public class BizBaseException  extends  RuntimeException{
    private  int  code;
    private  String message;
    public BizBaseException(int code, String message) {
        this.code=code;
        this.message=message;
    }
}
