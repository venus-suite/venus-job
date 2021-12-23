export interface GetAudioListParams {
  /** 审核Id */
  auditId: number;
  /** 第几页 */
  current: number;
  isAdmin: number;
  /** 每页大小 */
  pageSize: number;
  /** 审核状态 */
  status: number;
  /** 总页数(导出的时候分页使用) */
  total: number;
  userId: number;
}

export interface AuditRes {
  /** 申请时间 */
  applyTime: number;
  /** 操作编码（每个操作不同的code） */
  auditCode: string;
  /** 操作描述 */
  auditDesc: string;
  /** 主键 */
  auditId: number;
  /** 操作编码（操作编码对应的操作类型id） */
  auditSourceId: number;
  /** 创建时间 */
  createTime: string;
  email: string;
  userName: string;
  /** 提交详情 */
  // eslint-disable-next-line @typescript-eslint/ban-types
  detail: {
    data: {
      addressType?: number;
      appName?: string;
      namespaceId?: number;
      order?: number;
      title?: string;
      addressList: string;
      /** 数据新建时间 */
      addTime?: string;
      /** 报警邮件 */
      alarmEmail?: string;
      /** 负责人 */
      author?: string;
      /** 子任务ID，多个逗号分隔 */
      childJobId?: string;
      /** 阻塞处理策略 */
      executorBlockStrategy?: string;
      /** 失败重试次数 */
      executorFailRetryCount?: number;
      /** 执行器，任务Handler名称 */
      executorHandler?: string;
      /** 执行器，任务参数 */
      executorParam?: string;
      /** 执行器路由策略 */
      executorRouteStrategy?: string;
      /** 任务执行超时时间，单位秒 */
      executorTimeout?: number;
      /** GLUE备注 */
      glueRemark?: string;
      /** GLUE源代码 */
      glueSource?: string;
      /** glueType */
      glueType?: string;
      /** GLUE更新时间 */
      glueUpdatetime?: string;
      /** 任务执行CRON表达式 */
      jobCron?: string;
      /** 任务描述 */
      jobDesc?: string;
      /** 执行器主键ID */
      jobGroup?: number;
      /** 任务状态 【base on quartz】 */
      jobStatus?: string;
      /** 数据更新时间 */
    };

    desc: string;
  };
  /** 是否有效（1,-有效 0-无效） */
  status: number;
}

export interface BasePageRespAuditRes {
  /** 列表 */
  contentList: AuditRes[];
  /** 总页数 */
  pages: number;
  /** 总数 */
  total: number;
}

export interface GetAudioListResponse {
  /** 200表示成功，其他表示错误 */
  code: number;
  data: BasePageRespAuditRes;
  /** 提示信息 */
  msg: string;
  /** 服务器时间 */
  timestamp: number;
}
export interface AuditParams {
  /** auditId */
  auditId: number;
  /** regCode */
  regCode: string;
}

export interface AuditResponse {
  /** 200表示成功，其他表示错误 */
  code: number;
  /** 调用成功的时返回的数据 */
  data: boolean;
  /** 提示信息 */
  msg: string;
  /** 服务器时间 */
  timestamp: number;
}
