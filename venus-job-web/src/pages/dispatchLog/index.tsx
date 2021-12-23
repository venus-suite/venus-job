import ProAction from '@/components/ProAction';
import { ModalForm } from '@ant-design/pro-form';
// import { BetaSchemaForm } from '@ant-design/pro-form';s
import { PageContainer } from '@ant-design/pro-layout';
import type { ActionType, ProColumns } from '@ant-design/pro-table';
import ProTable from '@ant-design/pro-table';
import { FormInstance } from 'antd/es';
import React, { MutableRefObject, useRef, useState } from 'react';
import { logDetailCat, pageList } from '../services/dispatchLog';
import { getJobInfo } from '../services/jobManager';
import type { LogResult, PageListParams, XxlJobLog } from '../types/dispatchLog';

async function getInfo() {
  return getJobInfo({});
}
const infoRes = ((await getInfo()) ?? {}) as {
  JobGroupList: [];
  ExecutorRouteStrategyEnum: [];
  ExecutorBlockStrategyEnum: [];
  GlueTypeEnum: [];
};
const newJobGroupList = infoRes.JobGroupList.map(
  (item: { appName: string; title: string; id: number }) => ({
    label: item.title + '(' + item.appName + ')',
    value: item.id,
  }),
);

const App = (props: { location: { query: { jobId: string } } }) => {
  const [logDetailres, setLogDetailres] = useState<LogResult>({
    end: true,
    fromLineNum: 1,
    logContent: '暂无记录',
    toLineNum: 1,
  });
  let myItemRef = useRef<ActionType>();
  const formRef = useRef<FormInstance>() as MutableRefObject<FormInstance>;
  const getLogDetail = async function (logItem: {
    id: number;
    triggerTime: string;
    executorAddress?: '';
  }) {
    const { id: logId, triggerTime, executorAddress = '172.19.2.170.9999' } = logItem;
    const res = (await logDetailCat({
      logId,
      triggerTime: new Date(triggerTime).getTime(),
      executorAddress,
      fromLineNum: '1',
    })) ?? { end: true, fromLineNum: 1, logContent: '暂无记录', toLineNum: 1 };
    setLogDetailres(res);

    return <div />;
  };

  // eslint-disable-next-line @typescript-eslint/no-unsafe-assignment
  // eslint-disable-next-line react/destructuring-assignment
  let { jobId } = props.location.query;
  // @ts-ignore
  const columns: ProColumns<XxlJobLog>[] = [
    { title: 'id', dataIndex: 'id', search: false, width: 30 },

    {
      title: '执行器',
      dataIndex: 'jobGroup',
      valueType: 'select',
      fieldProps: {
        options: newJobGroupList,
        placeholder: '请选择执行器',
      },
      hideInTable: true,
    },
    {
      title: '任务ID',
      dataIndex: 'jobId',
      initialValue: jobId,
      width: 30,
      render: (dom, record) => (
        <a
          href={'#'}
          onClick={async () => {
            let frm = formRef.current;
            frm.setFieldsValue({
              jobId: record.jobId,
            });
            frm.submit();
          }}
        >
          {record.jobId}
        </a>
      ),
    },
    {
      title: '任务描述',
      dataIndex: 'jobName',
      width: 100,
    },
    {
      title: '触发时间',
      dataIndex: 'triggerTime',
      width: 120,
      valueType: 'dateTimeRange',
      render: (dom, record) => <span>{record.triggerTime}</span>,
    },
    {
      title: '调度结果',
      dataIndex: 'triggerCode',
      width: 80,
      search: false,
      renderText: (text) => <span>{text === 200 ? '成功' : '失败'}</span>,
    },
    {
      title: '调度备注',
      dataIndex: 'triggerMsg',
      width: 80,
      search: false,
      render: (dom, record) => {
        if (record.triggerMsg !== null) {
          return (
            <ModalForm
              key="detail"
              title="调度备注"
              trigger={
                <ProAction type="link" size="small">
                  查看
                </ProAction>
              }
              width="80%"
              onFinish={async () => true}
            >
              <div
                // eslint-disable-next-line react/no-danger
                dangerouslySetInnerHTML={{
                  __html: record.triggerMsg,
                }}
              />
            </ModalForm>
          );
        }
        return dom;
      },
    },
    { title: '执行时间', dataIndex: 'handleTime', width: 120, search: false },
    {
      title: '执行结果',
      dataIndex: 'handleCode',
      width: 80,
      search: false,
      valueEnum: {
        200: { text: '成功' },
        0: { text: '-' },
        500: { text: '失败' },
        502: { text: '失败(超时)' },
      },
    },
    {
      title: '执行结果',
      dataIndex: 'logStatus',
      // width: 80,
      // search: false,
      hideInTable: true,
      valueType: 'select',
      fieldProps: {
        options: [
          {
            label: '全部',
            value: '-1',
          },
          {
            label: '成功',
            value: '1',
          },
          {
            label: '失败',
            value: '2',
          },
          {
            label: '进行中',
            value: '3',
          },
        ],
      },
    },
    {
      title: '执行备注',
      dataIndex: 'handleMsg',
      width: 80,
      search: false,
      renderText: (dom, record) =>
        (record.handleMsg || '').length > 30 ? (
          <ModalForm
            key="detail"
            title="执行备注"
            trigger={
              <div color={'red'} style={{ color: 'red', cursor: 'pointer' }}>
                {record.handleMsg.trim().substring(0, 30).trim().replaceAll('\n', '')}
                ...
              </div>
            }
            width="80%"
            onFinish={async () => true}
          >
            <div
              // eslint-disable-next-line react/no-danger
              dangerouslySetInnerHTML={{
                __html: record.handleMsg.replace('\n', '<br/>'),
              }}
            />
          </ModalForm>
        ) : (
          <>{record.handleMsg}</>
        ),
    },
    {
      title: '操作',
      valueType: 'option',
      width: 20,
      render: (dom, record) => (
        <ModalForm
          key="detail"
          title="日志详情"
          trigger={
            <ProAction type="link" size="small" onClick={getLogDetail.bind(this, record)}>
              日志详情
            </ProAction>
          }
          width="80%"
          onFinish={async () => true}
        >
          <div
            // eslint-disable-next-line react/no-danger
            dangerouslySetInnerHTML={{
              // eslint-disable-next-line @typescript-eslint/naming-convention
              __html: logDetailres.logContent,
            }}
          />
        </ModalForm>
      ),
    },
  ];

  return (
    <PageContainer>
      <ProTable<XxlJobLog, PageListParams>
        rowKey="id"
        columns={columns}
        actionRef={myItemRef}
        formRef={formRef}
        search={{ labelWidth: 'auto' }}
        request={async (params) => {
          const data = await pageList(params);
          return {
            ...data,
            data: data?.contentList,
          };
        }}
        scroll={{ x: 1000 }}
      />
    </PageContainer>
  );
};

export default App;
