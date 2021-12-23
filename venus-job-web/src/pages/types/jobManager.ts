export interface StartParams {
  /** id */
  id: number;
  executorParam?: string;
  /** 执行器主键ID */
  jobGroup: number;
}

export interface StartResponse {
  code: number;
  content: string;
  data: string;
  msg: string;
}
export interface JobInfoParams {
  /** jobGroup */
  jobGroup?: number;
}

export interface JobInfoResponse {
  /** 200表示成功，其他表示错误 */
  code: number;
  /** 调用成功的时返回的数据 */
  data: {
    JobGroupList: [];
    ExecutorRouteStrategyEnum: [];
    ExecutorBlockStrategyEnum: [];
    GlueTypeEnum: [];
  };
  /** 提示信息 */
  msg: string;
  /** 服务器时间 */
  timestamp: number;
  JobGroupList: [];
}
export interface QueryJobPageParams {
  /** 第几页 */
  executorHandler: string;
  /** 第几页 */
  filterTime: string;
  /** 第几页 */
  jobDesc: string;
  /** 第几页 */
  jobGroup: number;
  /** 第几页 */
  length: number;
  /** 第几页 */
  start: number;
}

export interface 应用表 {
  /** 数据新建时间 */
  addTime: string;
  /** 报警邮件 */
  alarmEmail: string;
  /** 负责人 */
  author: string;
  /** 子任务ID，多个逗号分隔 */
  childJobId: string;
  /** 阻塞处理策略 */
  executorBlockStrategy: string;
  /** 失败重试次数 */
  executorFailRetryCount: number;
  /** 执行器，任务Handler名称 */
  executorHandler: string;
  /** 执行器，任务参数 */
  executorParam: string;
  /** 执行器路由策略 */
  executorRouteStrategy: string;
  /** 任务执行超时时间，单位秒 */
  executorTimeout: number;
  /** GLUE备注 */
  glueRemark: string;
  /** GLUE源代码 */
  glueSource: string;
  /** glueType */
  glueType: string;
  /** GLUE更新时间 */
  glueUpdatetime: string;
  /** id */
  id: number;
  /** 任务执行CRON表达式 */
  jobCron: string;
  /** 任务描述 */
  jobDesc: string;
  /** 执行器主键ID */
  jobGroup: number;
  /** 任务状态 【base on quartz】 */
  jobStatus: string;
  /** 数据更新时间 */
  updateTime: string;
}

export interface BasePageInfo应用表 {
  /** 内容 */
  contentList: 应用表[];
  /** 总页数 */
  pages: number;
  /** 总数 */
  total: number;
}

export interface QueryJobPageResponse {
  /** 200表示成功，其他表示错误 */
  code: number;
  data: BasePageInfo应用表;
  /** 提示信息 */
  msg: string;
  /** 服务器时间 */
  timestamp: number;
}

export interface DeleteJobParams {
  /** 主键 */
  id: number;
  jobGroup: number;
}

export interface DeleteJobResponse {
  /** 200表示成功，其他表示错误 */
  code: number;
  /** 调用成功的时返回的数据 */
  data: null;
  /** 提示信息 */
  msg: string;
  /** 服务器时间 */
  timestamp: number;
}

export interface AddJobParams {
  /** 数据新建时间 */
  addTime: string;
  /** 报警邮件 */
  alarmEmail: string;
  /** 负责人 */
  author: string;
  /** 子任务ID，多个逗号分隔 */
  childJobId: string;
  /** 阻塞处理策略 */
  executorBlockStrategy: string;
  /** 失败重试次数 */
  executorFailRetryCount: number;
  /** 执行器，任务Handler名称 */
  executorHandler: string;
  /** 执行器，任务参数 */
  executorParam: string;
  /** 执行器路由策略 */
  executorRouteStrategy: string;
  /** 任务执行超时时间，单位秒 */
  executorTimeout: number;
  /** GLUE备注 */
  glueRemark: string;
  /** GLUE源代码 */
  glueSource: string;
  /** glueType */
  glueType: string;
  /** GLUE更新时间 */
  glueUpdatetime: string;
  /** id */
  id: number;
  /** 任务执行CRON表达式 */
  jobCron: string;
  /** 任务描述 */
  jobDesc: string;
  /** 执行器主键ID */
  jobGroup: number;
  /** 任务状态 【base on quartz】 */
  jobStatus: string;
  /** 数据更新时间 */
  updateTime: string;
}
export interface AddJobResponse {
  /** 200表示成功，其他表示错误 */
  code: number;
  /** 调用成功的时返回的数据 */
  data: null;
  /** 提示信息 */
  msg: string;
  /** 服务器时间 */
  timestamp: number;
}

export interface UpdateJobResponse {
  length: any;
  /** 200表示成功，其他表示错误 */
  code: number;
  /** 调用成功的时返回的数据 */
  data: null;
  /** 提示信息 */
  msg: string;
  /** 服务器时间 */
  timestamp: number;
}

export interface UpdateJobParams {
  /** 数据新建时间 */
  addTime: string;
  /** 报警邮件 */
  alarmEmail: string;
  /** 负责人 */
  author: string;
  /** 子任务ID，多个逗号分隔 */
  childJobId: string;
  /** 阻塞处理策略 */
  executorBlockStrategy: string;
  /** 失败重试次数 */
  executorFailRetryCount: number;
  /** 执行器，任务Handler名称 */
  executorHandler: string;
  /** 执行器，任务参数 */
  executorParam: string;
  /** 执行器路由策略 */
  executorRouteStrategy: string;
  /** 任务执行超时时间，单位秒 */
  executorTimeout: number;
  /** GLUE备注 */
  glueRemark: string;
  /** GLUE源代码 */
  glueSource: string;
  /** glueType */
  glueType: string;
  /** GLUE更新时间 */
  glueUpdatetime: string;
  /** id */
  id: number;
  /** 任务执行CRON表达式 */
  jobCron: string;
  /** 任务描述 */
  jobDesc: string;
  /** 执行器主键ID */
  jobGroup: number;
  /** 任务状态 【base on quartz】 */
  jobStatus: string;
  /** 数据更新时间 */
  updateTime: string;
}
