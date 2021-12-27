
create  database venus_job;
use venus_job;

create table if not exists venus_job_qrtz_user
(
    id              bigint auto_increment comment '主键id'
    primary key,
    email           varchar(100) default ''                not null comment '企业邮箱(帐号)',
    name            varchar(50)  default ''                not null comment '真实姓名',
    password        varchar(100)                           not null comment '用户密码',
    token           varchar(100)                           null comment '登录token',
    token_time      bigint                                 null comment 'token生成时间,方便计算过期时间，存毫秒时间戳',
    operator_id     bigint       default 0                 null comment '最后操作人id',
    operator        varchar(10)  default ''                null comment '最后操作人',
    create_time     datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time     datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted      tinyint      default 0                 not null comment '0:有效，1:删除',
    last_login_time datetime                               null comment '最后的登录时间',
    is_super        tinyint      default 0                 null comment '是否超级管理员，0否，1是',
    constraint uniq_venus_job_qrtz_user
    unique (email),
    constraint uniq_venus_job_qrtz_user_name
    unique (name)
    )
    comment '用户表' collate = utf8mb4_unicode_ci;

create index idx_name
    on venus_job_qrtz_user (name);

create table if not exists xxl_job_qrtz_audit
(
    audit_id          bigint unsigned auto_increment comment '主键',
    audit_code        varchar(255)                               not null comment '操作编码（每个操作不同的code）',
    audit_desc        varchar(255)                               null comment '操作描述',
    audit_source_id   bigint                                     not null comment '操作编码对应的操作类型id',
    status            int(10)                                    not null comment '是否审核（1,-有效  2-无效(已经审核））',
    verification_code varchar(255)                               not null comment '验证码',
    user_id           bigint                                     null comment '发送验证码的用户id',
    user_name         varchar(255)                               null comment '提交人姓名',
    namespace         varchar(255) default ''                    null comment '命名空间',
    namespace_id      bigint       default 0                     null comment '命名空间Id',
    apply_time        bigint                                     not null comment '申请时间',
    create_time       timestamp    default '2000-01-10 01:00:00' not null on update CURRENT_TIMESTAMP comment '创建时间',
    update_time       timestamp    default CURRENT_TIMESTAMP     not null comment '更新时间',
    is_deleted        tinyint                                    not null comment '0=未删除，1=已删除',
    email             varchar(255)                               null comment '提交人邮箱',
    primary key (audit_id, create_time)
    )
    comment '定时任务操作审核' charset = utf8;

create index idx_code
    on xxl_job_qrtz_audit (audit_code, apply_time);

create table if not exists xxl_job_qrtz_audit_detail
(
    audit_detail_id bigint unsigned auto_increment comment '主键'
    primary key,
    audit_id        bigint unsigned                         not null comment '审核id',
    audit_text      mediumtext                              null comment '操作内容',
    create_time     timestamp default '2000-10-10 01:00:00' not null on update CURRENT_TIMESTAMP comment '创建时间',
    update_time     timestamp default CURRENT_TIMESTAMP     not null comment '更新时间',
    is_deleted      tinyint                                 not null comment '0=未删除，1=已删除'
)
    comment '定时任务操作审核' charset = utf8;

create index idx_code
    on xxl_job_qrtz_audit_detail (audit_id);

create table if not exists xxl_job_qrtz_calendars
(
    SCHED_NAME    varchar(120) not null comment 'SCHED_NAME',
    CALENDAR_NAME varchar(200) not null comment 'CALENDAR_NAME',
    CALENDAR      blob         not null comment 'CALENDAR',
    primary key (SCHED_NAME, CALENDAR_NAME)
    )
    comment 'XXL_JOB_QRTZ_CALENDARS' charset = utf8;

create table if not exists xxl_job_qrtz_fired_triggers
(
    SCHED_NAME        varchar(120) not null comment 'SCHED_NAME',
    ENTRY_ID          varchar(95)  not null comment 'ENTRY_ID',
    TRIGGER_NAME      varchar(200) not null comment 'TRIGGER_NAME',
    TRIGGER_GROUP     varchar(200) not null comment 'TRIGGER_GROUP',
    INSTANCE_NAME     varchar(200) not null comment 'INSTANCE_NAME',
    FIRED_TIME        bigint(13)   not null comment 'FIRED_TIME',
    SCHED_TIME        bigint(13)   not null comment 'SCHED_TIME',
    PRIORITY          int          not null comment 'PRIORITY',
    STATE             varchar(16)  not null comment 'STATE',
    JOB_NAME          varchar(200) null comment 'JOB_NAME',
    JOB_GROUP         varchar(200) null comment 'JOB_GROUP',
    IS_NONCONCURRENT  varchar(1)   null comment 'IS_NONCONCURRENT',
    REQUESTS_RECOVERY varchar(1)   null comment 'REQUESTS_RECOVERY',
    primary key (SCHED_NAME, ENTRY_ID)
    )
    comment 'XXL_JOB_QRTZ_FIRED_TRIGGERS' charset = utf8;

create table if not exists xxl_job_qrtz_job_details
(
    SCHED_NAME        varchar(120) not null comment 'SCHED_NAME',
    JOB_NAME          varchar(200) not null comment 'JOB_NAME',
    JOB_GROUP         varchar(200) not null comment 'JOB_GROUP',
    DESCRIPTION       varchar(250) null comment 'DESCRIPTION',
    JOB_CLASS_NAME    varchar(250) not null comment 'JOB_CLASS_NAME',
    IS_DURABLE        varchar(1)   not null comment 'IS_DURABLE',
    IS_NONCONCURRENT  varchar(1)   not null comment 'IS_NONCONCURRENT',
    IS_UPDATE_DATA    varchar(1)   not null comment 'IS_UPDATE_DATA',
    REQUESTS_RECOVERY varchar(1)   not null comment 'REQUESTS_RECOVERY',
    JOB_DATA          blob         null comment 'JOB_DATA',
    primary key (SCHED_NAME, JOB_NAME, JOB_GROUP)
    )
    comment 'XXL_JOB_QRTZ_JOB_DETAILS' charset = utf8;

create table if not exists xxl_job_qrtz_locks
(
    SCHED_NAME varchar(120) not null comment 'SCHED_NAME',
    LOCK_NAME  varchar(40)  not null comment 'LOCK_NAME',
    primary key (SCHED_NAME, LOCK_NAME)
    )
    comment 'XXL_JOB_QRTZ_LOCKS' charset = utf8;

create table if not exists xxl_job_qrtz_namespace
(
    namespace_id    int auto_increment comment '主键'
    primary key,
    business_name       varchar(50)                                 not null comment '业务空间',
    service         varchar(255)                                not null comment '服务名',
    app_key         varchar(255)                                null comment '命名空间对应的key',
    remark          varchar(255)                                null comment '空间描述',
    create_time     timestamp     default '2010-10-01 10:00:00' not null on update CURRENT_TIMESTAMP comment '创建时间',
    update_time     timestamp     default CURRENT_TIMESTAMP     null comment '更新时间',
    is_deleted      tinyint                                     not null comment '0=未删除，1=已删除',
    admin_user      mediumtext                                  null comment '命名空间管理员，用来@对应的用户',
    notice_web_hook varchar(1024) default ''                    null comment '错误信息通知webhook',
    audit_web_hook varchar(1024) default ''                    null comment '审核信息通知webhook',
    constraint uniq_service
    unique (namespace, service)
    )
    comment 'job对应的命名空间' charset = utf8;

create table if not exists xxl_job_qrtz_operate_log
(
    id            int auto_increment comment '主键'
    primary key,
    username      varchar(255)            null comment '用户名',
    event_name    varchar(255)            null comment '栏位名称',
    user_email    varchar(255) default '' null comment 'user_email',
    ip            varchar(255)            null comment 'ip',
    url           varchar(255)            null comment 'url',
    use_time      int                     null comment 'use_time',
    headers       text                    null comment 'ip地址',
    params        text                    null comment '参数',
    response_body text                    null comment '返回值',
    create_time   datetime                not null comment 'create_time'
    )
    comment 'xxl_job日志';

create table if not exists xxl_job_qrtz_paused_trigger_grps
(
    SCHED_NAME    varchar(120) not null comment 'SCHED_NAME',
    TRIGGER_GROUP varchar(200) not null comment 'TRIGGER_GROUP',
    primary key (SCHED_NAME, TRIGGER_GROUP)
    )
    comment 'XXL_JOB_QRTZ_PAUSED_TRIGGER_GRPS' charset = utf8;

create table if not exists xxl_job_qrtz_scheduler_state
(
    SCHED_NAME        varchar(120) not null comment 'SCHED_NAME',
    INSTANCE_NAME     varchar(200) not null comment 'INSTANCE_NAME',
    LAST_CHECKIN_TIME bigint(13)   not null comment 'LAST_CHECKIN_TIME',
    CHECKIN_INTERVAL  bigint(13)   not null comment 'CHECKIN_INTERVAL',
    primary key (SCHED_NAME, INSTANCE_NAME)
    )
    comment 'XXL_JOB_QRTZ_SCHEDULER_STATE' charset = utf8;

create table if not exists xxl_job_qrtz_trigger_group
(
    id           int auto_increment comment 'id'
    primary key,
    app_name     varchar(64)             not null comment '执行器AppName',
    namespace    varchar(255) default '' null comment '命名空间',
    namespace_id bigint       default 0  null comment '命名空间ID',
    title        varchar(12)             not null comment '执行器名称',
    `order`      tinyint      default 0  not null comment '排序',
    address_type tinyint      default 0  not null comment '执行器地址类型：0=自动注册、1=手动录入',
    address_list varchar(512)            null comment '执行器地址列表，多地址逗号分隔'
    )
    comment 'XXL_JOB_QRTZ_TRIGGER_GROUP' charset = utf8;

create table if not exists xxl_job_qrtz_trigger_info
(
    id                        int auto_increment comment 'id'
    primary key,
    job_group                 int           not null comment '执行器主键ID',
    job_cron                  varchar(128)  not null comment '任务执行CRON',
    job_desc                  varchar(255)  not null comment 'job_desc',
    add_time                  datetime      null comment 'add_time',
    update_time               datetime      null comment 'update_time',
    author                    varchar(64)   null comment '作者',
    alarm_email               varchar(255)  null comment '报警邮件',
    executor_route_strategy   varchar(50)   null comment '执行器路由策略',
    executor_handler          varchar(255)  null comment '执行器任务handler',
    executor_param            varchar(512)  null comment '执行器任务参数',
    executor_block_strategy   varchar(50)   null comment '阻塞处理策略',
    executor_timeout          int default 0 not null comment '任务执行超时时间，单位秒',
    executor_fail_retry_count int default 0 not null comment '失败重试次数',
    glue_type                 varchar(50)   not null comment 'GLUE类型',
    glue_source               mediumtext    null comment 'GLUE源代码',
    glue_remark               varchar(128)  null comment 'GLUE备注',
    glue_updatetime           datetime      null comment 'GLUE更新时间',
    child_jobid               varchar(255)  null comment '子任务ID，多个逗号分隔',
    job_status                varchar(50)   null
    )
    comment 'XXL_JOB_QRTZ_TRIGGER_INFO' charset = utf8;

create table if not exists xxl_job_qrtz_trigger_log
(
    id                        int auto_increment comment 'id'
    primary key,
    job_group                 int               not null comment '执行器主键ID',
    job_id                    int               not null comment '任务，主键ID',
    executor_address          varchar(255)      null comment '执行器地址，本次执行的地址',
    executor_handler          varchar(255)      null comment '执行器任务handler',
    executor_param            varchar(512)      null comment '执行器任务参数',
    executor_sharding_param   varchar(20)       null comment '执行器任务分片参数，格式如 1/2',
    executor_fail_retry_count int     default 0 not null comment '失败重试次数',
    trigger_time              datetime          null comment '调度-时间',
    trigger_code              int               not null comment '调度-结果',
    trigger_msg               text              null comment '调度-日志',
    handle_time               datetime          null comment '执行-时间',
    handle_code               int               not null comment '执行-状态',
    handle_msg                text              null comment '执行-日志',
    alarm_status              tinyint default 0 not null comment '告警状态：0-默认、1-无需告警、2-告警成功、3-告警失败',
    trigger_day               varchar(20)       null comment '触发日期'
    )
    comment 'XXL_JOB_QRTZ_TRIGGER_LOG' charset = utf8;

create index idx_handle_code
    on xxl_job_qrtz_trigger_log (handle_code);

create index idx_job_group
    on xxl_job_qrtz_trigger_log (job_group);

create index idx_job_id
    on xxl_job_qrtz_trigger_log (job_id);

create index idx_trigger_day
    on xxl_job_qrtz_trigger_log (trigger_day);

create index idx_trigger_time
    on xxl_job_qrtz_trigger_log (trigger_time);

create table if not exists xxl_job_qrtz_trigger_logglue
(
    id          int auto_increment comment 'id'
    primary key,
    job_id      int          not null comment '任务，主键ID',
    glue_type   varchar(50)  null comment 'GLUE类型',
    glue_source mediumtext   null comment 'GLUE源代码',
    glue_remark varchar(128) not null comment 'GLUE备注',
    add_time    timestamp    null comment 'add_time',
    update_time timestamp    null on update CURRENT_TIMESTAMP comment 'update_time'
    )
    comment 'XXL_JOB_QRTZ_TRIGGER_LOGGLUE' charset = utf8;

create table if not exists xxl_job_qrtz_trigger_registry
(
    id             int auto_increment comment 'id'
    primary key,
    registry_group varchar(255)                           not null comment 'registry_group',
    registry_key   varchar(255)                           not null comment 'registry_key',
    registry_value varchar(255)                           not null comment 'registry_value',
    namespace      varchar(255) default ''                null comment 'namespace',
    update_time    timestamp    default CURRENT_TIMESTAMP not null comment 'update_time'
    )
    comment 'XXL_JOB_QRTZ_TRIGGER_REGISTRY' charset = utf8;

create table if not exists xxl_job_qrtz_triggers
(
    SCHED_NAME     varchar(120) not null comment 'SCHED_NAME',
    TRIGGER_NAME   varchar(200) not null comment 'TRIGGER_NAME',
    TRIGGER_GROUP  varchar(200) not null comment 'TRIGGER_GROUP',
    JOB_NAME       varchar(200) not null comment 'JOB_NAME',
    JOB_GROUP      varchar(200) not null comment 'JOB_GROUP',
    DESCRIPTION    varchar(250) null comment 'DESCRIPTION',
    NEXT_FIRE_TIME bigint(13)   null comment 'NEXT_FIRE_TIME',
    PREV_FIRE_TIME bigint(13)   null comment 'PREV_FIRE_TIME',
    PRIORITY       int          null comment 'PRIORITY',
    TRIGGER_STATE  varchar(16)  not null comment 'TRIGGER_STATE',
    TRIGGER_TYPE   varchar(8)   not null comment 'TRIGGER_TYPE',
    START_TIME     bigint(13)   not null comment 'START_TIME',
    END_TIME       bigint(13)   null comment 'END_TIME',
    CALENDAR_NAME  varchar(200) null comment 'CALENDAR_NAME',
    MISFIRE_INSTR  smallint(2)  null comment 'MISFIRE_INSTR',
    JOB_DATA       blob         null comment 'JOB_DATA',
    primary key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
    constraint xxl_job_qrtz_triggers_ibfk_1
    foreign key (SCHED_NAME, JOB_NAME, JOB_GROUP) references xxl_job_qrtz_job_details (SCHED_NAME, JOB_NAME, JOB_GROUP)
    )
    comment 'XXL_JOB_QRTZ_TRIGGERS' charset = utf8;

create table if not exists xxl_job_qrtz_blob_triggers
(
    SCHED_NAME    varchar(120) not null comment 'SCHED_NAME',
    TRIGGER_NAME  varchar(200) not null comment 'TRIGGER_NAME',
    TRIGGER_GROUP varchar(200) not null comment 'TRIGGER_GROUP',
    BLOB_DATA     blob         null comment 'BLOB_DATA',
    primary key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
    constraint xxl_job_qrtz_blob_triggers_ibfk_1
    foreign key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP) references xxl_job_qrtz_triggers (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
    )
    comment 'XXL_JOB_QRTZ_BLOB_TRIGGERS' charset = utf8;

create table if not exists xxl_job_qrtz_cron_triggers
(
    SCHED_NAME      varchar(120) not null comment 'SCHED_NAME',
    TRIGGER_NAME    varchar(200) not null comment 'TRIGGER_NAME',
    TRIGGER_GROUP   varchar(200) not null comment 'TRIGGER_GROUP',
    CRON_EXPRESSION varchar(200) not null comment 'CRON_EXPRESSION',
    TIME_ZONE_ID    varchar(80)  null comment 'TIME_ZONE_ID',
    primary key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
    constraint xxl_job_qrtz_cron_triggers_ibfk_1
    foreign key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP) references xxl_job_qrtz_triggers (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
    )
    comment 'XXL_JOB_QRTZ_CRON_TRIGGERS' charset = utf8;

create table if not exists xxl_job_qrtz_simple_triggers
(
    SCHED_NAME      varchar(120) not null comment 'SCHED_NAME',
    TRIGGER_NAME    varchar(200) not null comment 'TRIGGER_NAME',
    TRIGGER_GROUP   varchar(200) not null comment 'TRIGGER_GROUP',
    REPEAT_COUNT    bigint(7)    not null comment 'REPEAT_COUNT',
    REPEAT_INTERVAL bigint(12)   not null comment 'REPEAT_INTERVAL',
    TIMES_TRIGGERED bigint(10)   not null comment 'TIMES_TRIGGERED',
    primary key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
    constraint xxl_job_qrtz_simple_triggers_ibfk_1
    foreign key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP) references xxl_job_qrtz_triggers (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
    )
    comment 'XXL_JOB_QRTZ_SIMPLE_TRIGGERS' charset = utf8;

create table if not exists xxl_job_qrtz_simprop_triggers
(
    SCHED_NAME    varchar(120)   not null comment 'SCHED_NAME',
    TRIGGER_NAME  varchar(200)   not null comment 'TRIGGER_NAME',
    TRIGGER_GROUP varchar(200)   not null comment 'TRIGGER_GROUP',
    STR_PROP_1    varchar(512)   null comment 'STR_PROP_1',
    STR_PROP_2    varchar(512)   null comment 'STR_PROP_2',
    STR_PROP_3    varchar(512)   null comment 'STR_PROP_3',
    INT_PROP_1    int            null comment 'INT_PROP_1',
    INT_PROP_2    int            null comment 'INT_PROP_2',
    LONG_PROP_1   bigint         null comment 'LONG_PROP_1',
    LONG_PROP_2   bigint         null comment 'LONG_PROP_2',
    DEC_PROP_1    decimal(13, 4) null comment 'DEC_PROP_1',
    DEC_PROP_2    decimal(13, 4) null comment 'DEC_PROP_2',
    BOOL_PROP_1   varchar(1)     null comment 'BOOL_PROP_1',
    BOOL_PROP_2   varchar(1)     null comment 'BOOL_PROP_2',
    primary key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
    constraint xxl_job_qrtz_simprop_triggers_ibfk_1
    foreign key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP) references xxl_job_qrtz_triggers (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
    )
    comment 'XXL_JOB_QRTZ_SIMPROP_TRIGGERS' charset = utf8;

create index SCHED_NAME
    on xxl_job_qrtz_triggers (SCHED_NAME, JOB_NAME, JOB_GROUP);

create table if not exists xxl_job_qrtz_user_admin
(
    admin_id    bigint(11) auto_increment comment '主键'
    primary key,
    email       varchar(50)                             not null comment '管理员邮箱',
    create_time timestamp default '2000-10-10 10:00:00' not null on update CURRENT_TIMESTAMP comment '创建时间',
    update_time timestamp default CURRENT_TIMESTAMP     not null comment '更新时间',
    is_deleted  int(4)                                  not null comment '0=未删除，1=已删除',
    constraint `idx_namespace-service`
    unique (email)
    )
    comment 'job管理超级管理员' charset = utf8;

create table if not exists xxl_job_qrtz_user_namespace
(
    id           bigint(11) auto_increment comment '主键'
    primary key,
    user_id      bigint    default 0                     null comment '用户id',
    namespace_id bigint(11)                              not null comment '命名空间',
    email        varchar(255)                            not null comment '用户邮箱',
    create_time  timestamp default '2000-10-10 10:00:00' not null on update CURRENT_TIMESTAMP comment '创建时间',
    update_time  timestamp default CURRENT_TIMESTAMP     not null comment '更新时间',
    is_deleted   int(2)                                  not null comment '0=未删除，1=已删除',
    need_audit   int(10)   default 0                     null comment '是否需要审核(发消息）（1-需要 0 -不需要)'
    )
    comment '用户命名空间' charset = utf8;

create index idx_email
    on xxl_job_qrtz_user_namespace (email);

create table if not exists xxl_job_server_info
(
    id               bigint auto_increment
    primary key,
    server_name      varchar(100)                            null,
    app_name         varchar(25)                             null,
    cpu_num          int                                     null comment 'CPU核心数',
    cpu_total        double                                  null comment 'CPU总的使用率',
    cpu_sys          double                                  null comment 'CPU系统使用率',
    cpu_used         double                                  null comment 'CPU用户使用率',
    cpu_wait         double                                  null comment 'CPU当前等待率',
    cpu_free         double                                  null comment 'CPU当前空闲率',
    jvm_total        double                                  null comment '当前JVM占用的内存总数(M)',
    jvm_max          double                                  null comment 'JVM最大可用内存总数(M)',
    jvm_free         double                                  null comment 'JVM空闲内存(M)',
    jdk_version      varchar(50)                             null comment 'JDK版本',
    jdk_home         varchar(2048)                           null comment 'JDK路径',
    jvm_Usage        double                                  null comment 'jvm使用率',
    jvm_RunTime      varchar(200)                            null comment 'JDK运行时间',
    mem_total        double                                  null comment '内存总量',
    mem_used         double                                  null comment '已用内存',
    mem_free         double                                  null comment '剩余内存',
    mem_usage        double                                  null comment '内存使用率',
    sys_computerName varchar(200)                            null comment '服务器名称',
    sys_computerIp   varchar(20)                             null comment '服务器Ip',
    sys_userDir      varchar(200)                            null comment '项目路径',
    sys_osName       varchar(50)                             null comment '操作系统',
    sys_osArch       varchar(20)                             null comment '系统架构',
    file_dirName     varchar(1024)                           null comment '盘符路径',
    file_sysTypeName varchar(20)                             null comment '盘符类型',
    file_typeName    varchar(20)                             null comment '文件类型',
    file_total       varchar(20)                             null comment '总大小',
    file_free        varchar(20)                             null comment '剩余大小',
    file_used        varchar(20)                             null comment '已经使用量',
    file_usage       double                                  null comment '资源的使用率',
    create_time      timestamp default '2000-10-10 10:00:00' not null on update CURRENT_TIMESTAMP comment '创建时间',
    update_time      timestamp default CURRENT_TIMESTAMP     not null comment '更新时间',
    is_deleted       int(2)                                  not null comment '0=未删除，1=已删除'
    )
    comment '定时任务服务信息' charset = utf8;

create index idx_serverName
    on xxl_job_server_info (server_name);

create table if not exists xxl_job_server_info_his
(
    id               bigint auto_increment
    primary key,
    server_name      varchar(100)                            null,
    app_name         varchar(25)                             null,
    cpu_num          int                                     null comment 'CPU核心数',
    cpu_total        double                                  null comment 'CPU总的使用率',
    cpu_sys          double                                  null comment 'CPU系统使用率',
    cpu_used         double                                  null comment 'CPU用户使用率',
    cpu_wait         double                                  null comment 'CPU当前等待率',
    cpu_free         double                                  null comment 'CPU当前空闲率',
    jvm_total        double                                  null comment '当前JVM占用的内存总数(M)',
    jvm_max          double                                  null comment 'JVM最大可用内存总数(M)',
    jvm_free         double                                  null comment 'JVM空闲内存(M)',
    jdk_version      varchar(50)                             null comment 'JDK版本',
    jdk_home         varchar(2048)                           null comment 'JDK路径',
    jvm_Usage        double                                  null comment 'jvm使用率',
    jvm_RunTime      varchar(200)                            null comment 'JDK运行时间',
    mem_total        double                                  null comment '内存总量',
    mem_used         double                                  null comment '已用内存',
    mem_free         double                                  null comment '剩余内存',
    mem_usage        double                                  null comment '内存使用率',
    sys_computerName varchar(200)                            null comment '服务器名称',
    sys_computerIp   varchar(20)                             null comment '服务器Ip',
    sys_userDir      varchar(200)                            null comment '项目路径',
    sys_osName       varchar(50)                             null comment '操作系统',
    sys_osArch       varchar(20)                             null comment '系统架构',
    file_dirName     varchar(1024)                           null comment '盘符路径',
    file_sysTypeName varchar(20)                             null comment '盘符类型',
    file_typeName    varchar(20)                             null comment '文件类型',
    file_total       varchar(20)                             null comment '总大小',
    file_free        varchar(20)                             null comment '剩余大小',
    file_used        varchar(20)                             null comment '已经使用量',
    file_usage       double                                  null comment '资源的使用率',
    create_time      timestamp default '2000-10-10 10:00:00' not null on update CURRENT_TIMESTAMP comment '创建时间',
    update_time      timestamp default CURRENT_TIMESTAMP     not null comment '更新时间',
    is_deleted       int(2)                                  not null comment '0=未删除，1=已删除'
    )
    comment '定时任务服务信息历史记录' charset = utf8;

create index idx_create_time_index
    on xxl_job_server_info_his (create_time);

create index idx_serverName
    on xxl_job_server_info_his (server_name);


#初始化数据

insert into venus_job_qrtz_user (id, email, name, password, token, token_time, operator_id, operator, create_time, update_time, is_deleted, last_login_time, is_super) values (20, '4564@qq.com', 'admin', 'SYvX2uJEJQk/iQXs1edvbw==', 'c7bab4f5369130b88e307e5bad9421c2655dc181', 1640246298146, 0, 'localUser', '2021-12-20 18:31:02', '2021-12-23 15:58:18', 0, '2021-12-23 15:58:18', 1);


