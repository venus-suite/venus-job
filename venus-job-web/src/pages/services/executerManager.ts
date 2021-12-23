import { defaultDomain } from '@/config/domain';
import request from '@/utils/request';
import type {
  JobGroupPageListResponse,
  QueryExecutorPageParams,
  RemoveParams,
  RemoveResponse,
  SaveParams,
  SaveResponse,
  UpdateParams,
  UpdateResponse,
  UserNamespaceListParams,
  UserNamespaceListResponse,
} from '../types/executerManager';
import { ServerNameParams } from '../types/executerManager';

/** 分页查询应用表 */
export async function queryExecuterPage(params: QueryExecutorPageParams) {
  return request<JobGroupPageListResponse['data']>({
    baseURL: defaultDomain,
    url: '/job/jobgroup/',
    method: 'POST',
    params,
  });
}

/** 删除应用 */
export async function deleteExecuter(params: RemoveParams) {
  return request<RemoveResponse['data']>({
    baseURL: defaultDomain,
    url: '/job/jobgroup/remove',
    method: 'POST',
    params,
  });
}

/** 新增应用 */
export async function addExecuter(params: SaveParams) {
  return request<SaveResponse['data']>({
    baseURL: defaultDomain,
    url: '/job/jobgroup/save',
    method: 'POST',
    params,
  });
}

/** 编辑应用 */
export async function updateExecuter(params: UpdateParams) {
  return request<UpdateResponse['data']>({
    baseURL: defaultDomain,
    url: '/job/jobgroup/update',
    method: 'POST',
    params,
  });
}

/** 执行器对应的命名空间 */
export async function userNamespaceList(params: UserNamespaceListParams) {
  return request<UserNamespaceListResponse['data']>({
    baseURL: defaultDomain,
    url: '/job/jobgroup/userNamespaceList',
    method: 'POST',
    params,
  });
}

/** getHisByName */
export async function getHisByName(params: ServerNameParams) {
  return request<RemoveResponse['data']>({
    baseURL: defaultDomain,
    url: '/job/stat/getHisByName',
    method: 'POST',
    params,
  });
}
