import type {
  LogDetailCatParams,
  LogDetailCatResponse,
  PageListParams,
  PageListResponse,
} from '../types/dispatchLog';
import request from '@/utils/request';
import { defaultDomain } from '@/config/domain';

/** 日志列表分页 */
export async function pageList(params: PageListParams) {
  return request<PageListResponse['data']>({
    baseURL: defaultDomain,
    url: '/job/joblog/pageList',
    method: 'POST',
    params,
  });
}
/** 日志详情 */
export async function logDetailCat(params: LogDetailCatParams) {
  return request<LogDetailCatResponse['data']>({
    baseURL: defaultDomain,
    url: '/job/joblog/logDetailCat',
    method: 'POST',
    params,
  });
}
