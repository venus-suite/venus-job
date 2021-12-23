import { defaultDomain } from '@/config/domain';
import request from '@/utils/request';
import type {
  AddNamespaceParams,
  AddNamespaceResponse,
  AddNamespaceToUserParams,
  AddNamespaceToUserResponse,
  DelNamespaceParams,
  DelNamespaceResponse,
  GetNamespaceListParams,
  GetNamespaceListResponse,
  RemoveUserNamespaceParams,
  RemoveUserNamespaceResponse,
  UpdateNamespaceParams,
  UpdateNamespaceResponse,
} from '../types/nameSpaceManager';
import { TaskNode } from '../types/nameSpaceManager';

/** 命名空间列表 */
export async function getNamespaceList(params: GetNamespaceListParams) {
  return request<GetNamespaceListResponse['data']>({
    baseURL: defaultDomain,
    url: '/job/namespace/getNamespaceList',
    method: 'POST',
    params,
  });
}
/** 添加命名空间 */
export async function addNamespace(params: AddNamespaceParams) {
  return request<AddNamespaceResponse['data']>({
    baseURL: defaultDomain,
    url: '/job/namespace/addNamespace',
    method: 'POST',
    params,
  });
}

/** 删除命名空间 */
export async function delNamespace(params: DelNamespaceParams) {
  return request<DelNamespaceResponse['data']>({
    baseURL: defaultDomain,
    url: '/job/namespace/delNamespace',
    method: 'POST',
    params,
  });
}

/** 编辑命名空间 */
export async function updateNamespace(params: UpdateNamespaceParams) {
  return request<UpdateNamespaceResponse['data']>({
    baseURL: defaultDomain,
    url: '/job/namespace/updateNamespace',
    method: 'POST',
    params,
  });
}
/** 授权用户命名空间 */
export async function addNamespaceToUser(params: AddNamespaceToUserParams) {
  return request<AddNamespaceToUserResponse['data']>({
    baseURL: defaultDomain,
    url: '/job/namespace/addNamespaceToUser',
    method: 'POST',
    params,
    successMessage: true,
  });
}

/** 删除用户命名空间权限 */
export async function removeUserNamespace(params: RemoveUserNamespaceParams) {
  return request<RemoveUserNamespaceResponse['data']>({
    baseURL: defaultDomain,
    url: '/job/namespace/removeUserNamespace',
    method: 'POST',
    params,
    successMessage: true,
  });
}

/** getTaskTreeNode */
export async function getTaskTreeNode(params: TaskNode) {
  return request<RemoveUserNamespaceResponse['data']>({
    baseURL: defaultDomain,
    url: '/job/namespace/getTaskTreeNode',
    method: 'POST',
    params,
    successMessage: false,
  });
}

export async function dashboard(params: TaskNode) {
  return request<RemoveUserNamespaceResponse['data']>({
    baseURL: defaultDomain,
    url: '/job/stat/dashboard',
    method: 'POST',
    params,
    successMessage: false,
  });
}
