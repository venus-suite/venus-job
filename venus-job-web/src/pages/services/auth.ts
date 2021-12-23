import { defaultDomain } from '@/config/domain';
import request from '@/utils/request';

/** 登录 */
export async function loginIn(params: any) {
  return request({
    baseURL: defaultDomain,
    url: '/login',
    method: 'POST',
    params
  });
}
