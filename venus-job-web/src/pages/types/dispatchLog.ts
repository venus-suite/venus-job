export interface LogDetailCatParams {
  /** executorAddress */
  executorAddress: string;
  // /** fromLineNum */
  fromLineNum: string;
  /** logId */
  logId: number;
  /** triggerTime */
  triggerTime: number;
}

export interface LogResult {
  end: boolean;
  fromLineNum: number;
  logContent: string;
  toLineNum: number;
}

export interface LogDetailCatResponse {
  code: number;
  content: LogResult;
  data: LogResult;
  msg: string;
}
export interface PageListParams {
  /** 第几页 */
  current: number;
  filterTime: string;
  jobGroup: number;
  jobId: number;
  logStatus: number;
  /** 每页大小 */
  pageSize: number;
  /** 总页数(导出的时候分页使用) */
  total: number;
}

export interface XxlJobLog {
  alarmStatus: number;
  executorAddress: string;
  executorFailRetryCount: number;
  executorHandler: string;
  executorParam: string;
  executorShardingParam: string;
  handleCode: number;
  handleMsg: string;
  handleTime: string;
  id: number;
  jobGroup: number;
  jobId: number;
  triggerCode: number;
  triggerMsg: string | null;
  triggerTime: string;
}

export interface BaseMngPageRespXxlJobLog {
  /** 列表 */
  contentList: XxlJobLog[];
  /** 总页数 */
  pages: number;
  /** 总数 */
  total: number;
}

export interface PageListResponse {
  /** 200表示成功，其他表示错误 */
  code: number;
  data: BaseMngPageRespXxlJobLog;
  /** 提示信息 */
  msg: string;
  /** 服务器时间 */
  timestamp: number;
}
