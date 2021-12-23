package io.suite.venus.job.admin.enums;

/**
 * 操作类型
 */
public enum AuditTypeEnum {
    TASK_TYPE("task","任务类型"),
    GROUP_TYPE("group","执行器类型"),

    ;

    private  String code;
    private  String desc;
    AuditTypeEnum(String code, String desc ){
        this.code=code;
        this.desc=desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
