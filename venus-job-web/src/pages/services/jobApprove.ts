import { defaultDomain } from '@/config/domain';
import type {
  AuditParams,
  AuditResponse,
  GetAudioListParams,
  GetAudioListResponse,
} from '../types/jobApprove';
import request from '@/utils/request';

/** 定时任务审核列表 */
export async function getAudioList(params: GetAudioListParams) {
  return request<GetAudioListResponse['data']>({
    baseURL: defaultDomain,
    url: '/job/audit/getAudioList',
    method: 'POST',
    params,
  });
}
/** 审核 */
export async function audit(params: AuditParams) {
  return request<AuditResponse['data']>({
    baseURL: defaultDomain,
    url: '/job/audit/doAudit',
    method: 'POST',
    params,
    successMessage: true,
  });
}
