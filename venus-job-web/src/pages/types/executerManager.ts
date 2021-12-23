export interface QueryExecutorPageParams {
  /** 第几页 */
  executorHandler?: string;
  /** 第几页 */
  filterTime?: string;
  /** 第几页 */
  jobDesc?: string;
  /** 第几页 */
  jobGroup?: number;
  /** 第几页 */
  pageSize?: number;
  /** 第几页 */
  current?: number;
}

export interface JobGroupPageListParams {
  /** AppName */
  appName: string;
  /** 第几页 */
  current: number;
  /** 每页大小 */
  pageSize: number;
  /** 总页数(导出的时候分页使用) */
  total: number;
}

export interface 执行器 {
  /** 执行器地址列表，多地址逗号分隔(手动录入) */
  addressList: string;
  /** 执行器地址类型：0=自动注册、1=手动录入 */
  addressType: number;
  /** appName */
  appName: string;
  /** 主键id */
  id: number;
  /** 命名空间 */
  namespace: string;
  /** 命名空间 */
  namespaceId: number;
  /** 排序 */
  order: number;
  /** 执行器地址列表(系统注册) */
  registryList: string[];
  /** 标题 */
  title: string;
}

export interface BaseMngPageResp执行器 {
  /** 列表 */
  contentList: 执行器[];
  /** 总页数 */
  pages: number;
  /** 总数 */
  total: number;
}

export interface JobGroupPageListResponse {
  /** 200表示成功，其他表示错误 */
  code: number;
  data: BaseMngPageResp执行器;
  /** 提示信息 */
  msg: string;
  /** 服务器时间 */
  timestamp: number;
}
export interface SaveParams {
  /** 执行器地址列表，多地址逗号分隔(手动录入) */
  addressList: string;
  /** 执行器地址类型：0=自动注册、1=手动录入 */
  addressType: number;
  /** appName */
  appName: string;
  /** 主键id */
  id: number;
  /** 命名空间 */
  namespace: string;
  /** 命名空间 */
  namespaceId: number;
  /** 排序 */
  order: number;
  /** 执行器地址列表(系统注册) */
  registryList: string[];
  /** 标题 */
  title: string;
}

export interface SaveResponse {
  /** 200表示成功，其他表示错误 */
  code: number;
  /** 调用成功的时返回的数据 */
  data: string;
  /** 提示信息 */
  msg: string;
  /** 服务器时间 */
  timestamp: number;
}
export interface UpdateParams {
  /** 执行器地址列表，多地址逗号分隔(手动录入) */
  addressList: string;
  /** 执行器地址类型：0=自动注册、1=手动录入 */
  addressType: number;
  /** appName */
  appName: string;
  /** 主键id */
  id: number;
  /** 命名空间 */
  namespace: string;
  /** 命名空间 */
  namespaceId: number;
  /** 排序 */
  order: number;
  /** 执行器地址列表(系统注册) */
  registryList: string[];
  /** 标题 */
  title: string;
}

export interface UpdateResponse {
  /** 200表示成功，其他表示错误 */
  code: number;
  /** 调用成功的时返回的数据 */
  data: string;
  /** 提示信息 */
  msg: string;
  /** 服务器时间 */
  timestamp: number;
}
export interface RemoveParams {
  /** 主键id */
  id: number;
  namespaceId: number;
}

export interface RemoveResponse {
  /** 200表示成功，其他表示错误 */
  code: number;
  /** 调用成功的时返回的数据 */
  data: string;
  /** 提示信息 */
  msg: string;
  /** 服务器时间 */
  timestamp: number;
}

export interface UserNamespaceListParams {
  /** 主键id */
  id?: number;
}

export interface NamespaceResp {
  /** 命名空间 */
  namespace: string;
  /** 主键 */
  namespaceId: number;
}

export interface UserNamespaceListResponse {
  /** 200表示成功，其他表示错误 */
  code: number;
  /** 调用成功的时返回的数据 */
  data: NamespaceResp[];
  /** 提示信息 */
  msg: string;
  /** 服务器时间 */
  timestamp: number;
}

export interface ServerNameParams {
  /** 主键id */
  serverName: string;
  startDate?: string;
  endDate?: string;
}
