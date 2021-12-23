export interface RemoveUserNamespaceParams {
  /** 用户邮箱 */
  email: string;
  /** 命名空间id */
  namespaceId: number;
}

export interface TaskNode {}

export interface RemoveUserNamespaceResponse {
  /** 200表示成功，其他表示错误 */
  code: number;
  /** 调用成功的时返回的数据 */
  data: boolean;
  /** 提示信息 */
  msg: string;
  /** 服务器时间 */
  timestamp: number;
}
export interface AddNamespaceToUserParams {
  /** 用户邮箱 */
  emailList: string[] | string;
  /** 命名空间ID */
  namespaceIdList: number[];
  /** 是否需要审核操作（0:否 1 是）  默认都是需要审核 */
  needAudit: number;
}

export interface AddNamespaceToUserResponse {
  /** 200表示成功，其他表示错误 */
  code: number;
  /** 调用成功的时返回的数据 */
  data: boolean;
  /** 提示信息 */
  msg: string;
  /** 服务器时间 */
  timestamp: number;
}
export interface AddNamespaceParams {
  /** 命名空间对应的key */
  appKey: string;
  /** 第几页 */
  current: number;
  /** 命名空间 */
  namespace: string;
  /** 主键 */
  namespaceId: number;
  /** 每页大小 */
  pageSize: number;
  noticeWebHook: string;
  remark: string;
  /** 服务名 */
  service: string;
  /** 总页数(导出的时候分页使用) */
  total: number;
}
export interface DelNamespaceParams {
  namespaceId: number;
}

export interface DelNamespaceResponse {
  /** 200表示成功，其他表示错误 */
  code: number;
  /** 调用成功的时返回的数据 */
  data: boolean;
  /** 提示信息 */
  msg: string;
  /** 服务器时间 */
  timestamp: number;
}

export interface UpdateNamespaceParams {
  /** 主键id */
  id: number;
  /** 命名空间对应的key */
  appKey: string;
  /** 第几页 */
  current: number;
  /** 命名空间 */
  namespace: string;
  /** 主键 */
  namespaceId: number;
  noticeWebHook: string;
  /** 每页大小 */
  pageSize: number;
  remark: string;
  /** 服务名 */
  service: string;
  /** 总页数(导出的时候分页使用) */
  total: number;
}

export interface UpdateNamespaceResponse {
  /** 200表示成功，其他表示错误 */
  code: number;
  /** 调用成功的时返回的数据 */
  data: boolean;
  /** 提示信息 */
  msg: string;
  /** 服务器时间 */
  timestamp: number;
}
export interface AddNamespaceResponse {
  /** 200表示成功，其他表示错误 */
  code: number;
  /** 调用成功的时返回的数据 */
  data: boolean;
  /** 提示信息 */
  msg: string;
  /** 服务器时间 */
  timestamp: number;
}

export interface GetNamespaceListParams {
  /** 命名空间对应的key */
  appKey: string;
  /** 第几页 */
  current: number;
  /** 命名空间 */
  namespace: string;
  /** 主键 */
  namespaceId: number;
  /** 每页大小 */
  pageSize: number;
  remark: string;
  /** 服务名 */
  service: string;
  /** 总页数(导出的时候分页使用) */
  total: number;
}

export interface UserNamespace {
  /** 命名空间 */
  email: string;
  /** 命名空间 */
  namespace: string;
  /** 主键 */
  namespaceId: number;
  /** 是否需要审核操作（0:否 1 是）  默认都是需要审核 */
  needAudit: number;
  noticeWebHook: string;
}

export interface NamespaceAndUser {
  /** 命名空间 */
  namespace: string;
  /** 主键 */
  namespaceId: number;
  /** 服务名 */
  service: string;
  /** 拥有这个命名空间的用户 */
  userNamespaceList: UserNamespace[];
  admin: boolean;
  noticeWebHook: string;
}

export interface BasePageRespNamespaceAndUser {
  /** 列表 */
  contentList: NamespaceAndUser[];
  /** 总页数 */
  pages: number;
  /** 总数 */
  total: number;
}

export interface GetNamespaceListResponse {
  /** 200表示成功，其他表示错误 */
  code: number;
  data: BasePageRespNamespaceAndUser;
  /** 提示信息 */
  msg: string;
  /** 服务器时间 */
  timestamp: number;
}
