package io.suite.venus.job.admin.enums;

/**
 * 操作类型
 */
public enum OperaCodeEnum {
    TASK_STOP("task_stop","停止任务"),
    TASK_START("task_start","启动任务"),
    TASK_DELETE("task_delete","删除任务"),
    TASK_EDIT("task_edit","编辑任务"),
    TASK_ADD("task_add","新增任务"),
    TASK_EXECUTE("task_execute","执行任务"),

    GROUP_ADD("group_add","新增执行器"),
    GROUP_EDIT("group_edit","编辑执行器"),
    GROUP_DELETE("group_delete","删除执行器"),
    ;

    private  String code;
    private  String desc;
    OperaCodeEnum(String code, String desc ){
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
