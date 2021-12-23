import { defaultDomain } from '@/config/domain';
import request from '@/utils/request';
import type {
  AddJobParams,
  AddJobResponse,
  DeleteJobParams,
  DeleteJobResponse,
  JobInfoParams,
  JobInfoResponse,
  QueryJobPageParams,
  QueryJobPageResponse,
  StartParams,
  StartResponse,
  UpdateJobParams,
  UpdateJobResponse,
} from '../types/jobManager';

/** 启动 */
export async function start(params: StartParams) {
  return request<StartResponse['data']>({
    baseURL: defaultDomain,
    url: '/job/jobinfo/start',
    method: 'POST',
    params,
    successMessage: true,
  });
}
/** 停止 */
export async function pause(params: StartParams) {
  return request<StartResponse['data']>({
    baseURL: defaultDomain,
    url: '/job/jobinfo/stop',
    method: 'POST',
    params,
    successMessage: true,
  });
}
/** 执行 */
export async function triggerJob(params: StartParams) {
  return request<StartResponse['data']>({
    baseURL: defaultDomain,
    url: '/job/jobinfo/trigger',
    method: 'POST',
    params,
    successMessage: true,
  });
}
/** 枚举查询 */
export async function getJobInfo(params: JobInfoParams) {
  return request<JobInfoResponse['data']>({
    baseURL: defaultDomain,
    url: '/job/jobinfo/',
    method: 'POST',
    params,
  });
}

/** 分页查询应用表 */
export async function queryJobPage(params: QueryJobPageParams) {
  return request<QueryJobPageResponse['data']>({
    baseURL: defaultDomain,
    url: '/job/jobinfo/pageList',
    method: 'POST',
    params,
  });
}

/** 删除应用 */
export async function deleteJob(params: DeleteJobParams) {
  return request<DeleteJobResponse['data']>({
    baseURL: defaultDomain,
    url: '/job/jobinfo/remove',
    method: 'POST',
    params,
    successMessage: true,
  });
}

/** 新增应用 */
export async function addJob(params: AddJobParams) {
  return request<AddJobResponse['data']>({
    baseURL: defaultDomain,
    url: '/job/jobinfo/add',
    method: 'POST',
    params,
    successMessage: true,
  });
}

/** 编辑应用 */
export async function updateJob(params: UpdateJobParams) {
  return request<UpdateJobResponse['data']>({
    baseURL: defaultDomain,
    url: '/job/jobinfo/update',
    method: 'POST',
    params,
    successMessage: true,
  });
}

/** 保持code */
export async function saveCode(params: UpdateJobParams) {
  return request<UpdateJobResponse['data']>({
    baseURL: defaultDomain,
    url: '/job/jobcode/save',
    method: 'POST',
    params,
    successMessage: true,
  });
}

/** 查询下次执行时间 */
export async function getNextExecuteTime(params: UpdateJobParams) {
  return request<UpdateJobResponse>({
    baseURL: defaultDomain,
    url: '/job/jobinfo/getNextExecuteTime',
    method: 'POST',
    params,
  });
}
