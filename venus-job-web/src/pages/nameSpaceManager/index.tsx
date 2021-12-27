/* eslint-disable @typescript-eslint/no-unsafe-assignment */
import { BetaSchemaForm } from '@ant-design/pro-form';
import type { FormSchema } from '@ant-design/pro-form/lib/components/SchemaForm';
import { PageContainer } from '@ant-design/pro-layout';
import type { ProColumns } from '@ant-design/pro-table';
import ProTable from '@ant-design/pro-table';
import ProAction from '@/components/ProAction';
import { Button } from 'antd';
import { useState } from 'react';
import {
  addNamespace,
  addNamespaceToUser,
  delNamespace,
  getNamespaceList,
  removeUserNamespace,
  updateNamespace,
} from '../services/nameSpaceManager';
import type {
  AddNamespaceParams,
  AddNamespaceToUserParams,
  GetNamespaceListParams,
  NamespaceAndUser,
  RemoveUserNamespaceParams,
  UpdateNamespaceParams,
} from '../types/nameSpaceManager';

const formCol: FormSchema<AddNamespaceParams>['columns'] = [
  {
    title: '业务名称',
    dataIndex: 'businessName',
    formItemProps: {
      rules: [{ required: true, message: '请选择执行器' }],
    },
  },
  {
    title: '服务名称',
    dataIndex: 'service',
    formItemProps: {
      rules: [{ required: true, message: '请选择服务名称' }],
    },
  },
  {
    title: '日志通知WebHook(不填写，错误不提醒)',
    dataIndex: 'noticeWebHook',
    formItemProps: {
      rules: [{ required: false, message: '请填写日志通知' }],
    },
  },
  {
    title: '审核通知WebHook(不填写，默认不审核)',
    dataIndex: 'auditWebHook',
    formItemProps: {
      rules: [{ required: false, message: '请填写审核通知' }],
    },
  },
  { title: '备注', dataIndex: 'remark' },
];
const App = () => {
  const user = JSON.parse(localStorage.getItem('jobUser')) || {};
  const [isExpend, setIsExpend] = useState({});
  const expendClick = function (namespaceId) {
    isExpend[namespaceId] = !isExpend[namespaceId];
    setIsExpend({ ...isExpend });
  };
  const columns: ProColumns<NamespaceAndUser>[] = [
    { title: 'id', dataIndex: 'namespaceId', search: false, width: 80 },
    {
      title: '命名空间',
      dataIndex: 'namespace',
      render: (dom, record) => {
        return <div>{record.businessName + ':' + record.service}</div>;
      },
    },
    { title: '业务名称', dataIndex: 'businessName' },
    // { title: '命名空间key', dataIndex: 'appKey' },
    { title: '服务名', dataIndex: 'service' },
    {
      title: '拥有命名空间的用户',
      dataIndex: 'userNamespaceList',
      search: false,
      render: (dom, record) => {
        const { userNamespaceList: list } = record;
        const expend = isExpend[record.namespaceId];
        let renderDom;
        if (list.length > 0) {
          renderDom = (
            <div>
              <Button
                type="link"
                onClick={expendClick.bind(this, record.namespaceId)}
                style={{
                  position: 'absolute',
                  right: 0,
                  top: 0,
                  display: list.length > 3 ? 'block' : 'none',
                }}
              >
                {expend ? '收起' : '展开'}
              </Button>
              <div>
                {list.map((item, index) => (
                  <p
                    key={item.email}
                    style={{
                      display: index < 3 || (index >= 3 && expend) ? 'block' : 'none',
                    }}
                  >
                    {item.email}
                  </p>
                ))}
              </div>
            </div>
          );
        } else {
          renderDom = (
            <div>
              {record.userNamespaceList.map((item) => (
                <p key={item.email}>{item.email}</p>
              ))}
            </div>
          );
        }
        return <div>{renderDom}</div>;
      },
    },
    {
      title: '日志通知',
      dataIndex: 'noticeWebHook',
      search: false,
      ellipsis: true,
    },
    { title: '备注', dataIndex: 'remark', search: false },
    {
      title: '操作',
      valueType: 'option',
      width: 320,
      render: (dom, record, index, action) => [
        <BetaSchemaForm<UpdateNamespaceParams>
          key="update"
          title="编辑"
          trigger={
            <ProAction
              type="link"
              size="small"
              style={{ display: user.admin ? 'inline-block' : 'none' }}
            >
              编辑
            </ProAction>
          }
          layoutType="ModalForm"
          width={800}
          initialValues={record}
          onFinish={async (values) => {
            await updateNamespace({
              ...values,
              namespaceId: record.namespaceId,
            });
            action?.reload();
            return true;
          }}
          columns={formCol}
        />,
        <ProAction
          key="delete"
          type="link"
          size="small"
          danger
          confirm="确定删除这条数据吗？"
          style={{ display: user.admin ? 'inline-block' : 'none' }}
          onClick={async () => {
            await delNamespace({
              namespaceId: record.namespaceId,
            });
            action?.reload();
          }}
        >
          删除
        </ProAction>,
        <BetaSchemaForm<AddNamespaceToUserParams>
          key="add"
          title="授权命名空间"
          trigger={
            <ProAction size="small" type="link">
              新增授权
            </ProAction>
          }
          layoutType="ModalForm"
          width={500}
          initialValues={record}
          onFinish={async (values) => {
            // eslint-disable-next-line @typescript-eslint/no-unsafe-call
            const emailList = values.emailList.split(',');
            await addNamespaceToUser({
              // eslint-disable-next-line @typescript-eslint/no-unsafe-assignment
              emailList,
              needAudit: Number(values.needAudit),
              namespaceIdList: [record.namespaceId],
            });
            action?.reload();
            return true;
          }}
          columns={[
            {
              title: '用户邮箱',
              dataIndex: 'emailList',
              valueType: 'textarea',
              formItemProps: {
                required: true,
              },
              fieldProps: {
                placeholder: '可输入多个邮箱，用英文逗号","分隔',
              },
            },
          ]}
        />,
        <BetaSchemaForm<RemoveUserNamespaceParams>
          key="remove"
          title="删除授权命名空间"
          trigger={
            <ProAction type="link" size="small" danger>
              删除授权
            </ProAction>
          }
          layoutType="ModalForm"
          width={500}
          initialValues={record}
          onFinish={async (values) => {
            await removeUserNamespace({
              ...values,
              namespaceId: record.namespaceId,
            });
            action?.reload();
            return true;
          }}
          columns={[
            {
              title: '用户邮箱',
              dataIndex: 'email',
              formItemProps: {
                required: true,
              },
              valueType: 'select',
              fieldProps: {
                options: record.userNamespaceList.map((item) => ({
                  label: item.email,
                  value: item.email,
                })),
              },
            },
          ]}
        />,
      ],
    },
  ];

  return (
    <PageContainer>
      <ProTable<NamespaceAndUser, GetNamespaceListParams>
        rowKey="namespaceId"
        columns={columns}
        search={{ labelWidth: 'auto' }}
        request={async (params) => {
          const data = await getNamespaceList(params);
          const expendObj = {};
          data?.contentList.forEach((item) => {
            expendObj[item.namespaceId] = false;
          });

          setIsExpend(expendObj);
          return {
            ...data,
            data: data?.contentList,
          };
        }}
        // scroll={{ x: 1000 }}
        toolBarRender={(action) => [
          <BetaSchemaForm<AddNamespaceParams>
            key="create"
            title="新建"
            trigger={
              <ProAction type="primary" style={{ display: user.admin ? 'inline-block' : 'none' }}>
                新建
              </ProAction>
            }
            layoutType="ModalForm"
            width={800}
            labelCol={{ span: 12 }}
            onFinish={async (values) => {
              await addNamespace(values);
              action?.reload();
              return true;
            }}
            columns={formCol}
          />,
        ]}
      />
    </PageContainer>
  );
};

export default App;
