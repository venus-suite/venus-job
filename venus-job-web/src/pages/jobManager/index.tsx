import ProAction from '@/components/ProAction';
import { color } from '@/config/color';
import { SearchOutlined } from '@ant-design/icons';
import { BetaSchemaForm } from '@ant-design/pro-form';
import type { FormSchema } from '@ant-design/pro-form/lib/components/SchemaForm';
import { PageContainer } from '@ant-design/pro-layout';
import type { ProColumns } from '@ant-design/pro-table';
import ProTable from '@ant-design/pro-table';
import { Button, Input, Modal, Tooltip } from 'antd';
import { useEffect, useRef, useState } from 'react';
import Cron from 'react-cron-antd';
import { history } from 'umi';
import {
  addJob,
  deleteJob,
  getJobInfo,
  getNextExecuteTime,
  pause,
  queryJobPage,
  saveCode,
  start,
  triggerJob,
  updateJob,
} from '../services/jobManager';
import type {
  AddJobParams,
  QueryJobPageParams,
  UpdateJobParams,
  应用表,
} from '../types/jobManager';

const App = () => {
  const [cronValue, setCronValue] = useState('');
  const [visible, setVisible] = useState(false);
  const [infoRes, setInfoRes] = useState<{
    JobGroupList: [];
    ExecutorRouteStrategyEnum: [];
    ExecutorBlockStrategyEnum: [];
    GlueTypeEnum: [];
  }>({
    JobGroupList: [],
    ExecutorRouteStrategyEnum: [],
    ExecutorBlockStrategyEnum: [],
    GlueTypeEnum: [],
  });
  const [newJobGroupList, setNewJobGroupList] = useState<{ label: string; value: number }[]>([]);
  async function setEnum() {
    const res = (await getJobInfo({})) ?? {
      JobGroupList: [],
      ExecutorRouteStrategyEnum: [],
      ExecutorBlockStrategyEnum: [],
      GlueTypeEnum: [],
    };
    setInfoRes(res);
    setNewJobGroupList(
      res.JobGroupList.map((item: { appName: string; id: number; title: string }) => ({
        label: item.title + '(' + item.appName + ')',
        value: item.id,
      })),
    );
  }
  let myref = useRef();
  const [executeVisible, setExecuteVisible] = useState(false);
  const [execTime, setExcTime] = useState('');
  useEffect(() => {
    setEnum();
  }, []);
  const setFormCol = () => {
    const formCol: FormSchema<AddJobParams>['columns'] = [
      {
        valueType: 'group',
        columns: [
          {
            title: '执行器',
            dataIndex: 'jobGroup',
            valueType: 'select',
            fieldProps: {
              options: newJobGroupList,
              placeholder: '请选择执行器',
            },
            formItemProps: {
              rules: [{ required: true, message: '请选择执行器' }],
            },
            width: 's',
          },
          {
            title: '任务描述',
            dataIndex: 'jobDesc',
            fieldProps: {
              placeholder: '请输入任务描述',
            },
            formItemProps: {
              rules: [{ required: true }],
            },
            width: 's',
          },
        ],
      },
      {
        valueType: 'group',
        columns: [
          {
            title: '路由策略',
            dataIndex: 'executorRouteStrategy',
            valueType: 'select',
            width: 's',
            fieldProps: {
              options: infoRes.ExecutorRouteStrategyEnum,
              placeholder: '请选择路由策略',
            },
            formItemProps: {
              rules: [{ required: true, message: '请选择路由策略' }],
            },
          },
          {
            title: 'Cron',
            dataIndex: 'jobCron',
            width: 's',
            fieldProps: {
              placeholder: '请输入Cron',
            },
            formItemProps: {
              rules: [
                {
                  required: true,
                  validator: () => {
                    if (cronValue !== '') {
                      return Promise.resolve();
                    }
                    return Promise.reject(new Error('请输入Cron表达式'));
                  },
                },
              ],
            },
            renderFormItem: () => {
              const change = (val: string) => {
                setCronValue(val);
                setVisible(false);
              };
              const input = (e) => {
                setCronValue(e.target?.value ?? '');
                setVisible(false);
              };
              return (
                <div>
                  <Input.Search
                    value={cronValue}
                    onSearch={() => {
                      setVisible(true);
                    }}
                    onInput={input}
                  />
                  <Modal
                    key="detail"
                    title="Cron表达式生成器"
                    visible={visible}
                    width={650}
                    footer={null}
                    onCancel={() => {
                      setVisible(false);
                    }}
                  >
                    <Cron value={cronValue} onOk={change} />
                  </Modal>
                </div>
                // <Dropdown
                //   trigger={['click']}
                //   placement="bottomLeft"
                //   visible={visible}
                //   overlay={<Cron value={cronValue} onOk={change} />}
                // >
                //   <Input.Search
                //     value={cronValue}
                //     onClick={() => {
                //       setVisible(!visible);
                //     }}
                //     onInput={input}
                //   />
                // </Dropdown>
              );
            },
          },
        ],
      },
      {
        valueType: 'group',
        columns: [
          {
            title: '运行模式',
            dataIndex: 'glueType',
            valueType: 'select',
            width: 's',
            fieldProps: {
              options: infoRes.GlueTypeEnum,
              placeholder: '请选择运行模式',
            },
            formItemProps: {
              // rules: [{ required: true, message: '请选择运行模式' }],
            },
          },
          {
            title: 'JobHandler',
            dataIndex: 'executorHandler',
            width: 's',
            fieldProps: {
              placeholder: '请输入JobHandler',
            },
            formItemProps: {
              // rules: [{ required: true }],
            },
          },
        ],
      },
      {
        valueType: 'group',
        columns: [
          {
            title: '阻塞处理策略',
            dataIndex: 'executorBlockStrategy',
            valueType: 'select',
            width: 's',
            fieldProps: {
              options: infoRes.ExecutorBlockStrategyEnum,
              placeholder: '请选择阻塞处理策略',
            },
            formItemProps: {
              rules: [{ required: true, message: '请选择阻塞处理策略' }],
            },
          },
          {
            title: '子任务Id',
            dataIndex: 'childJobId',
            width: 's',
            fieldProps: {
              placeholder: '请输入子任务的任务ID，如存在多个则逗号分隔',
            },
            formItemProps: {
              // rules: [{ required: true }],
            },
          },
        ],
      },
      {
        valueType: 'group',
        columns: [
          {
            title: '任务超时时间',
            dataIndex: 'executorTimeout',
            valueType: 'digit',
            width: 's',
            fieldProps: {
              placeholder: '单位秒，大于零时生效',
            },
            formItemProps: {
              // rules: [{ required: true }],
            },
          },
          {
            title: '失败重试次数',
            dataIndex: 'executorFailRetryCount',
            valueType: 'digit',
            width: 's',
            fieldProps: {
              placeholder: '大于零时生效',
            },
            formItemProps: {
              // rules: [{ required: true }],
            },
          },
        ],
      },
      {
        valueType: 'group',
        columns: [
          {
            title: '负责人',
            dataIndex: 'author',
            width: 450,
            fieldProps: {
              placeholder: '请输入负责人',
            },
            formItemProps: {
              rules: [{ required: true }],
            },
          },
        ],
      },
      // {
      //   valueType:'group',
      //   columns:[

      //   ]
      // },

      {
        title: '任务参数',
        dataIndex: 'executorParam',
        valueType: 'textarea',
        fieldProps: {
          placeholder: '请输入任务参数',
        },
        formItemProps: {
          // rules: [{ required: true }],
        },
      },
    ];
    return formCol;
  };

  const setGLue = () => {
    const formCol: FormSchema<AddJobParams>['columns'] = [
      {
        valueType: 'group',
        columns: [
          {
            title: '编辑SHELL',
            dataIndex: 'glueSource',
            valueType: 'textarea',
            width: 860,
            fieldProps: {
              options: newJobGroupList,
              placeholder: '请输入SHELL',
              autoSize: { minRows: 20 },
            },
            formItemProps: {
              rules: [{ required: true, message: '请输入SHELL' }],
            },
          },
        ],
      },
    ];
    return formCol;
  };

  const columns: ProColumns<应用表>[] = [
    { title: '任务ID', dataIndex: 'id', width: 100 },
    {
      title: '执行器',
      dataIndex: 'jobGroup',
      valueType: 'select',
      fieldProps: {
        options: newJobGroupList,
      },
      hideInTable: true,
    },
    { title: '任务描述', dataIndex: 'jobDesc', width: 100 },
    {
      title: 'JobHandler',
      dataIndex: 'executorHandler',
      hideInTable: true,
    },
    {
      title: '运行模式',
      dataIndex: 'glueType',
      width: 160,
      search: false,
      renderText: (text: string, record) => `${text}：${record.executorHandler}`,
    },
    {
      title: 'cron',
      dataIndex: 'jobCron',
      width: 200,
      render: (dom, record) => [
        <span key="showCron" style={{ width: 180 }}>
          {record.jobCron}
          <Tooltip title="点击查询预计下次执行时间">
            <Button
              size="small"
              icon={<SearchOutlined />}
              onClick={async () => {
                setExecuteVisible(true);
                setExcTime(record.jobCron);
                let p = {} as UpdateJobParams;
                p.jobCron = record.jobCron;
                console.log('>>>>', p.jobCron);
                getNextExecuteTime(p)
                  .then((res) => {
                    console.log(res);
                    let n = res.length;
                    // @ts-ignore
                    let result = 'cron：' + record.jobCron + '\n\n';
                    for (let i = 0; i < n; i++) {
                      result += res[i] + '\n';
                    }

                    setExcTime(result);
                  })
                  .catch((e) => {});
              }}
            />
          </Tooltip>
        </span>,
      ],
    },
    { title: '负责人', dataIndex: 'author', width: 160 },
    {
      title: '状态',
      dataIndex: 'jobStatus',
      valueType: 'select',
      width: 60,
      valueEnum: {
        NORMAL: { text: '运行(RUNNING)' },
        NONE: { text: '停止(STOP)' },
      },
      render: (dom, record) => {
        const isnormal = record.jobStatus === 'NORMAL';
        return (
          <span
            style={{
              padding: '4px',
              background: isnormal ? color.elementblue : color.danger,
              color: '#fff',
              borderRadius: '4px',
            }}
          >
            {isnormal ? 'RUNNING' : 'STOP'}
          </span>
        );
      },
    },
    {
      title: '操作',
      valueType: 'option',
      width: 1,
      render: (dom, record, index, action) => [
        <BetaSchemaForm<UpdateJobParams>
          key="update"
          title="编辑"
          trigger={
            <ProAction
              type="link"
              size="small"
              onClick={() => {
                setCronValue(record.jobCron);
              }}
            >
              编辑
            </ProAction>
          }
          layoutType="ModalForm"
          width={550}
          initialValues={record}
          onFinish={async (values) => {
            await updateJob({
              ...values,
              id: record.id,
              jobCron: cronValue,
            });
            action?.reload();
            return true;
          }}
          columns={setFormCol()}
        />,
        record.glueType === 'BEAN' ? (
          <></>
        ) : (
          <BetaSchemaForm<UpdateJobParams>
            key="glueSource"
            title=""
            trigger={
              <ProAction type="link" size="small" onClick={() => {}}>
                GLUE
              </ProAction>
            }
            layoutType="ModalForm"
            width={860}
            initialValues={record}
            onFinish={async (values) => {
              let p = {
                ...values,
                id: record.id,
                glueRemark: 'update',
              };
              p.glueSource = values.glueSource.replaceAll('\r\n', '\n');
              await saveCode(p);
              action?.reload();
              return true;
            }}
            columns={setGLue()}
          />
        ),
        <ProAction
          key="start"
          confirm="确定启动吗？"
          // style={{ background: color.elementblue, color: '#fff' }}
          type="link"
          size="small"
          // disabled={}
          onClick={async () => {
            await start({ id: record.id, jobGroup: record.jobGroup });
            action?.reload();
          }}
        >
          启动
        </ProAction>,
        <ProAction
          key="pause"
          danger
          type="link"
          confirm="确定停止吗？"
          size="small"
          onClick={async () => {
            await pause({ id: record.id, jobGroup: record.jobGroup });
            action?.reload();
          }}
        >
          停止
        </ProAction>,
        <ProAction
          key="trigger"
          type="link"
          size="small"
          confirm="确定执行吗？"
          onClick={async () => {
            await triggerJob({
              id: record.id,
              executorParam: record.executorParam,
              jobGroup: record.jobGroup,
            });
            action?.reload();
          }}
        >
          执行
        </ProAction>,
        <ProAction
          key="delete"
          danger
          type="link"
          size="small"
          confirm="确定删除这条数据吗？"
          onClick={async () => {
            await deleteJob({ id: record.id, jobGroup: record.jobGroup });
            action?.reload();
          }}
        >
          删除
        </ProAction>,
        <ProAction
          key="log"
          size="small"
          type="link"
          onClick={async () => {
            // eslint-disable-next-line @typescript-eslint/no-unsafe-member-access
            history.push({
              pathname: '/jobAdmin/log',
              query: { jobId: `${record.id}` },
            });
          }}
        >
          日志
        </ProAction>,
      ],
    },
  ];

  const executeCol: FormSchema<AddJobParams>['columns'] = [
    {
      valueType: 'group',
      columns: [
        {
          title: '下次执行时间',
          dataIndex: 'glueSource',
          valueType: 'textarea',
          width: 360,
          editable: false,
          fieldProps: {
            placeholder: '',
            autoSize: { minRows: 6 },
          },
          formItemProps: {
            rules: [{ required: false }],
          },
        },
      ],
    },
  ];

  return (
    <PageContainer>
      <ProTable<应用表, QueryJobPageParams>
        rowKey="id"
        columns={columns}
        search={{ labelWidth: 'auto' }}
        request={async (params) => {
          const data = await queryJobPage(params);
          return {
            ...data,
            data: data?.contentList,
          };
        }}
        scroll={{ x: 1000 }}
        toolBarRender={(action) => [
          <BetaSchemaForm<AddJobParams>
            key="create"
            title="新建"
            trigger={<ProAction type="primary">新建</ProAction>}
            layoutType="ModalForm"
            // wrapperCol={{ span: 18 }}
            width={550}
            onFinish={async (values) => {
              await addJob({ ...values, jobCron: cronValue });
              action?.reload();
              return true;
            }}
            columns={setFormCol()}
          />,
        ]}
      />
      <Modal
        visible={executeVisible}
        title={'预计下次执行时间'}
        width={360}
        key="exe"
        footer={null}
        onOk={() => {
          setExecuteVisible(false);
        }}
        onCancel={() => {
          setExecuteVisible(false);
        }}
      >
        <textarea
          key="exeArea"
          value={execTime}
          readOnly={true}
          style={{
            width: 300,
            height: 180,
            margin: 0,
            border: 'none',
          }}
        >
          {execTime}
        </textarea>
      </Modal>
    </PageContainer>
  );
};

export default App;
