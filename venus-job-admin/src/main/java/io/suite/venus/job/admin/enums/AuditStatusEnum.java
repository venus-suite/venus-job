package io.suite.venus.job.admin.enums;

/**
 * 审核类型
 */
public enum AuditStatusEnum {
    YES(1,"未审核（有效）"),
    NO(2,"已审核（无效）"),

    ;

    private  int code;
    private  String desc;
    AuditStatusEnum(int code, String desc ){
        this.code=code;
        this.desc=desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
