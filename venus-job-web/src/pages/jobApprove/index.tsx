import ProAction from '@/components/ProAction';
import { BetaSchemaForm, ModalForm } from '@ant-design/pro-form';
import { PageContainer } from '@ant-design/pro-layout';
import type { ProColumns } from '@ant-design/pro-table';
import ProTable from '@ant-design/pro-table';
import { audit, getAudioList } from '../services/jobApprove';
import type { AuditParams, AuditRes, GetAudioListParams } from '../types/jobApprove';

const App = () => {
  // eslint-disable-next-line react/destructuring-assignment
  const { href } = window.location;
  const auditId = href.split('=')[1];

  const columns: ProColumns<AuditRes>[] = [
    { title: 'id', dataIndex: 'auditId', initialValue: auditId },
    { title: '操作编码', dataIndex: 'auditCode', search: false },
    { title: '操作描述', dataIndex: 'auditDesc' },
    // {
    //   title: '操作编码',
    //   dataIndex: 'auditSourceId',
    // },
    // { title: '申请时间', dataIndex: 'applyTime' },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      valueType: 'dateTimeRange',
      render: (dom, record) => <span>{record.createTime}</span>,
    },
    {
      title: '提交人',
      dataIndex: 'email',
      search: false,
      render: (dom, record) => <span>{record.userName + '(' + record.email + ')'}</span>,
    },
    {
      title: '提交详情',
      dataIndex: 'detail',
      search: false,
      render: (dom, record) => {
        const type = record.auditCode.split('_')[0];
        const { desc } = record.detail;

        function groupDom(record: AuditRes) {
          const { addressType, appName, namespaceId, order, title, addressList } =
            record.detail.data;

          return (
            <ul>
              <li>appName:{appName}</li>
              <li>标题:{title}</li>
              <li>地址类型:{addressType}</li>
              <li>地址列表:{addressList}</li>
              <li>命名空间:{namespaceId}</li>
              <li>排序:{order}</li>
            </ul>
          );
        }
        function groupTask(record: AuditRes) {
          const {
            alarmEmail,
            author,
            childJobId,
            executorBlockStrategy,
            executorFailRetryCount,
            executorHandler,
            executorParam,
            executorRouteStrategy,
            executorTimeout,
            glueType,
            jobCron,
            jobDesc,
            jobGroup,
          } = record.detail.data;

          return (
            <ul>
              <li>执行器:{jobGroup}</li>
              <li>任务描述:{jobDesc}</li>
              <li>路由策略:{executorRouteStrategy}</li>
              <li>Cron:{jobCron}</li>
              <li>运行模式:{glueType}</li>
              <li>JobHandler:{executorHandler}</li>
              <li>阻塞处理策略:{executorBlockStrategy}</li>
              <li>子任务id:{childJobId}</li>
              <li>任务超时时间:{executorTimeout}</li>
              <li>失败重试次数:{executorFailRetryCount}</li>
              <li>负责人:{author}</li>
              <li>报警邮件:{alarmEmail}</li>
              <li>任务参数:{executorParam}</li>
            </ul>
          );
        }
        let returnDom;
        switch (type) {
          case 'task':
            returnDom = groupTask(record);
            break;
          case 'group':
          default:
            returnDom = groupDom(record);
            break;
        }
        return (
          <div>
            <ModalForm
              key="detail"
              title="提交详情"
              trigger={
                <ProAction type="link" size="small">
                  查看
                </ProAction>
              }
              width={500}
              onFinish={async () => true}
            >
              <div style={{ lineHeight: '30px' }}>
                <h3>提交描述：{desc}</h3>
                {returnDom}
              </div>
            </ModalForm>
          </div>
        );
      },
    },

    {
      title: '是否审核',
      dataIndex: 'status',
      valueEnum: {
        1: { text: '未审核' },
        2: { text: '已审核' },
      },
    },
    {
      title: '操作',
      valueType: 'option',
      width: 100,
      render: (dom, record, index, action) => {
        if (record.status === 2) {
          return '';
        }
        return (
          <div>
            <BetaSchemaForm<AuditParams>
              key="update"
              title="审核"
              trigger={
                <ProAction type="link" size="small">
                  审核
                </ProAction>
              }
              layoutType="ModalForm"
              width={500}
              initialValues={{ auditId: record.auditId }}
              onFinish={async (values) => {
                await audit({
                  ...values,
                  auditId: record.auditId,
                });
                action?.reload();
                return true;
              }}
              columns={[
                {
                  title: '校验码',
                  dataIndex: 'regCode',
                  formItemProps: {
                    rules: [{ required: true, message: '请填写校验码' }],
                  },
                },
              ]}
            />
          </div>
        );
      },
    },
  ];

  return (
    <PageContainer>
      <ProTable<AuditRes, GetAudioListParams>
        rowKey="auditId"
        columns={columns}
        search={{ labelWidth: 'auto' }}
        request={async (params) => {
          const data = await getAudioList(params);
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
