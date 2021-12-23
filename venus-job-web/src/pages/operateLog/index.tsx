import { defaultDomain } from '@/config/domain';
import request from '@/utils/request';
import { PageContainer } from '@ant-design/pro-layout';
import ProTable from '@ant-design/pro-table';
import React from 'react';

const App = () => {
  const queryParams = [
    { title: '用户名称', dataIndex: 'userName', search: false },
    { title: '用户邮箱', dataIndex: 'userEmail', search: false },
    { title: '访问ip', dataIndex: 'ip', search: false },
    { title: '访问事件', dataIndex: 'eventName', search: false },
    {
      title: '访问地址URL',
      dataIndex: 'url',
      search: false,
      render: (dom, record) => <b>{record.url.substr(record.url.indexOf('/job/') + 1)}</b>,
    },
    { title: '访问时间', dataIndex: 'createTime', search: false },
  ];

  return (
    <PageContainer>
      <ProTable
        rowKey="id"
        search={{ labelWidth: 'auto' }}
        scroll={{ x: 800 }}
        pagination={{ pageSize: 10 }}
        columns={queryParams}
        request={async (params) => {
          const data = await request({
            baseURL: defaultDomain,
            url: '/job/getOperaLogPageList',
            method: 'POST',
            params: params,
          });
          return { ...data, data: data?.contentList };
        }}
      />
    </PageContainer>
  );
};

export default App;
