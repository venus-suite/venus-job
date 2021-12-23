import ProAction from '@/components/ProAction';
import { color } from '@/config/color';
import { Line } from '@ant-design/charts';
import { LoadingOutlined, ReloadOutlined } from '@ant-design/icons';
import TabPane from '@ant-design/pro-card/lib/components/TabPane';

import { BetaSchemaForm } from '@ant-design/pro-form';
import type { FormSchema } from '@ant-design/pro-form/lib/components/SchemaForm';
import { PageContainer } from '@ant-design/pro-layout';
import type { ProColumns } from '@ant-design/pro-table';
import ProTable from '@ant-design/pro-table';
import { Button, Col, DatePicker, Modal, Progress, Row, Spin, Tabs, Tag, Tooltip } from 'antd';
import moment from 'moment';
import { useEffect, useState } from 'react';
import {
  addExecuter,
  deleteExecuter,
  getHisByName,
  queryExecuterPage,
  updateExecuter,
  userNamespaceList,
} from '../services/executerManager';
import type {
  JobGroupPageListParams,
  SaveParams,
  UpdateParams,
  执行器,
} from '../types/executerManager';

const App = () => {
  const [nameSpaceList, setNameSpaceList] = useState<{ label: string; value: number }[]>([]);

  async function getNameSpaceList() {
    const res: { label: string; value: number }[] = ((await userNamespaceList({})) ?? []).map(
      (item) => ({
        label: item.namespace,
        value: item.namespaceId,
      }),
    );
    setNameSpaceList(res);
  }

  useEffect(() => {
    getNameSpaceList();
  }, []);
  const formCol: FormSchema<SaveParams>['columns'] = [
    {
      title: 'appName',
      dataIndex: 'appName',
      formItemProps: {
        rules: [{ required: true, message: '请填写APP名称' }],
      },
    },
    {
      title: '命名空间',
      dataIndex: 'namespaceId',
      valueType: 'select',
      fieldProps: {
        options: nameSpaceList,
      },
      formItemProps: {
        rules: [{ required: true, message: '请选择命名空间' }],
      },
    },
    {
      title: '标题',
      dataIndex: 'title',
      formItemProps: {
        rules: [{ required: true, message: '请填写标题' }],
      },
    },
    {
      title: '排序',
      dataIndex: 'order',
      formItemProps: {
        rules: [{ required: true, message: '请填写排序' }],
      },
    },
    {
      title: '地址类型',
      dataIndex: 'addressType',
      valueType: 'radio',
      initialValue: 0,
      // valueEnum: new Map({
      //   0: { text: '自动注册' },
      //   1: { text: '手动录入' },
      // }),
      fieldProps: {
        options: [
          { label: '自动注册', value: 0 },
          { label: '手动注册', value: 1 },
        ],
      },
      // {
      //   0: { text: '自动注册' },
      //   1: { text: '手动录入' },
      // },
      formItemProps: {
        rules: [{ required: true, message: '请选择执行器' }],
      },
    },
    {
      title: '地址列表',
      dataIndex: 'addressList',
      valueType: 'textarea',
      dependencies: ['addressType'],
      fieldProps: (form) => ({
        disabled: form.getFieldValue('addressType') === 0,
      }),
      formItemProps: (form) => ({
        rules: [
          {
            required: form.getFieldValue('addressType') === 1,
            message: '请填写地址',
          },
        ],
      }),
    },
  ];
  const pollingInterval = 1000 * 6;
  const [polling, setPolling] = useState(pollingInterval);
  const [visible, setVisible] = useState(false);
  const [cpuData, setCpuData] = useState([]);

  const [jvmData, setJvmData] = useState([]);
  const [memData, setMemData] = useState([]);
  const [loading, setLoading] = useState(false);
  const [searchServerName, setSearchServerName] = useState('');
  const [startDate, setStartDate] = useState(moment().add(-3600, 'm'));
  const [endDate, setEndDate] = useState(moment());

  const columns: ProColumns<执行器>[] = [
    { title: 'id', dataIndex: 'id', search: false },
    { title: 'appName', dataIndex: 'appName' },
    { title: '排序', dataIndex: 'order', search: false },
    {
      title: '命名空间',
      dataIndex: 'namespace',
      valueType: 'select',
      fieldProps: {
        options: nameSpaceList,
      },
    },
    { title: '名称', dataIndex: 'title' },
    {
      title: '注册方式',
      dataIndex: 'addressType',
      // valueEnum: {
      //   0: { text: '自动注册' },
      //   1: { text: '手动录入' },
      // },
      valueType: 'select',
      fieldProps: {
        options: [
          { label: '自动注册', value: 0 },
          { label: '手动注册', value: 1 },
        ],
      },
    },
    {
      title: '执行器地址',
      dataIndex: 'addressList',
      search: false,
      render: (dom, record) => {
        if (record.addressList) {
          const list = record.addressList.split(',');
          return (
            <div>
              {list.map((item) => (
                <p
                  key={item}
                  style={{
                    background: color.success,
                    color: '#fff',
                    padding: '2px 10px',
                    borderRadius: '15px',
                    width: 'fit-content',
                  }}
                >
                  {item}
                </p>
              ))}
            </div>
          );
        }
      },
    },
    {
      title: '操作',
      valueType: 'option',
      width: 100,
      render: (dom, record, index, action) => [
        <BetaSchemaForm<UpdateParams>
          key="update"
          title="编辑"
          trigger={
            <ProAction type="link" size="small">
              编辑
            </ProAction>
          }
          layoutType="ModalForm"
          width={500}
          initialValues={record}
          onFinish={async (values) => {
            await updateExecuter({
              ...values,
              id: record.id,
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
          onClick={async () => {
            await deleteExecuter({
              id: record.id,
              namespaceId: record.namespaceId,
            });
            action?.reload();
          }}
        >
          删除
        </ProAction>,
      ],
    },
  ];

  async function searchStatData(serverName) {
    setLoading(true);
    setVisible(true);
    if (!endDate) {
      return;
    }

    let fmt = 'YYYY-MM-DD HH:mm:ss';
    let start = moment(endDate).add(-180, 'm').format(fmt);
    let end = endDate.format(fmt);

    let rs = await getHisByName({
      serverName: serverName,
      startDate: start,
      endDate: end,
    });
    //cpuFree: 76.91
    // cpuSys: 8.82
    // cpuUsed: 12.45
    // cpuWait: 1.3
    // createTime: "2021-12-09 18:40:35"
    let len = rs.length;
    let curData = [];
    let curJvmData = [];
    let memData = [];
    for (let i = 0; i < len; i++) {
      let createTime = rs[i].createTime.split(' ')[1].trim();
      curData.push({
        year: createTime,
        category: '系统使用率',
        value: rs[i].cpuSys,
      });
      curData.push({
        year: createTime,
        category: '用户使用率',
        value: rs[i].cpuUsed,
      });

      //jvm
      curJvmData.push({
        year: createTime,
        category: 'JVM使用率',
        value: rs[i].jvmUsage,
      });
      //mem
      memData.push({
        year: createTime,
        category: '内存使用率',
        value: rs[i].memUsage,
      });
    }
    console.log('curData=', curData);
    setLoading(false);
    setCpuData(curData);
    setJvmData(curJvmData);
    setMemData(memData);
  }

  const expandedRowRender = (record) => {
    const columns = [
      {
        title: '服务地址',
        dataIndex: 'serverName',
        key: 'serverName',
        search: false,
        render: (dom, record) => [<span key="serverName">{record.serverName}</span>],
      },
      {
        title: 'CPU',
        dataIndex: 'cpuTotal',
        key: 'cpuTotal',
        search: false,
        render: (text, record, index) => {
          return (
            <Tooltip
              key={'cpu'}
              title={
                <span>
                  {' '}
                  CPU核心数:{record.cpuNum} <br />
                  CPU系统使用率:{record.cpuSys}
                  <br />
                  CPU用户使用率:{record.cpuUsed} <br />
                  CPU当前等待率:{record.cpuWait}
                  <br />
                  CPU当前空闲率:{record.cpuFree}
                </span>
              }
            >
              <Progress
                trailColor="#64b53b"
                strokeLinecap="square"
                strokeColor="#ff4d4f"
                width={80}
                type="circle"
                percent={record.cpuUsed || 0}
              >
                {' '}
              </Progress>
            </Tooltip>
          );
        },
      },
      {
        title: 'JVM内存',
        dataIndex: 'jvmTotal',
        key: 'jvmTotal',
        search: false,
        render: (text, record, index) => {
          return (
            <Tooltip
              key={'jvm'}
              title={
                <span>
                  {' '}
                  JVM占用的内存(M):{record.jvmTotal} <br />
                  JVM最大可用内存(M):{record.jvmMax} <br />
                  JVM空闲内存(M):{record.jvmFree}
                  <br />
                  jvm使用率:{record.jvmUsage}
                  <br />
                  JDK运行时间:{record.jvmRunTime}
                  <br />
                </span>
              }
            >
              <Progress
                width={80}
                strokeLinecap="square"
                trailColor="#64b53b"
                strokeColor="#ff4d4f"
                type="circle"
                percent={record.jvmUsage || 0}
              >
                {' '}
              </Progress>
            </Tooltip>
          );
        },
      },
      {
        title: '物理机内存',
        dataIndex: 'memUsage',
        key: 'memUsage',
        search: false,
        render: (text, record, index) => {
          return (
            <Tooltip
              key={'mem'}
              title={
                <span>
                  内存总量:{record.memTotal}G <br />
                  已用内存:{record.memUsed}G <br />
                  剩余内存:{record.memFree}G<br />
                  内存使用率:{record.memUsage}
                  <br />
                </span>
              }
            >
              <Progress
                width={80}
                strokeLinecap="square"
                trailColor="#64b53b"
                strokeColor="#ff4d4f"
                type="circle"
                percent={record.memUsage || 0}
              >
                {' '}
              </Progress>
            </Tooltip>
          );
        },
      },
      {
        title: '磁盘',
        dataIndex: 'fileTotal',
        key: 'fileTotal',
        search: false,
        render: (text, record, index) => {
          return (
            <Tooltip
              title={
                <span>
                  磁盘总量:{record.fileTotal}
                  <br />
                  磁盘使用量:{record.fileUsed}
                  <br />
                  磁盘剩余:{record.fileFree}
                  <br />
                  资源的使用率:{record.fileUsage}
                  <br />
                </span>
              }
            >
              <Progress
                width={80}
                strokeLinecap="square"
                trailColor="#64b53b"
                strokeColor="#ff4d4f"
                type="circle"
                percent={record.fileUsage || 0}
              >
                {' '}
              </Progress>
            </Tooltip>
          );
        },
      },
      {
        title: '操作',
        render: (dom, record) => [
          <Tooltip title={'点击查看历史图表'}>
            <ProAction
              size="small"
              type="link"
              title={'查看更多'}
              key="serverBt"
              onClick={async () => {
                setSearchServerName(record.serverName);
                searchStatData(record.serverName);
              }}
            >
              查看更多
            </ProAction>
          </Tooltip>,
        ],
      },
    ];

    return (
      <ProTable
        key={'jmtable'}
        columns={columns}
        locale={{
          emptyText: <Tag color="#FAA219" key={'emp'}></Tag>,
        }}
        dataSource={record.server}
        pagination={false}
        search={false}
        options={false}
        style={{ margin: 0, paddingTop: 16 }}
      />
    );
  };

  const cpuConf = {
    cpuData,
    xField: 'year',
    yField: 'value',
    seriesField: 'category',
    smooth: true,
    animation: {
      // 配置图表第一次加载时的入场动画
      appear: {
        animation: 'fade-in', // 动画效果
        duration: 1000, // 动画执行时间
      },
    },
    slider: {
      start: 0.1,
      end: 0.3,
    },
  };
  const jvmConf = {
    jvmData,
    xField: 'year',
    yField: 'value',
    seriesField: 'category',
    smooth: true,
    animation: {
      // 配置图表第一次加载时的入场动画
      appear: {
        animation: 'wave-in', // 动画效果
        duration: 1000, // 动画执行时间
      },
    },
    slider: {
      start: 0.1,
      end: 0.3,
    },
  };
  const memConf = {
    memData,
    xField: 'year',
    yField: 'value',
    seriesField: 'category',
    smooth: true,
    animation: {
      // 配置图表第一次加载时的入场动画
      appear: {
        animation: 'fade-in', // 动画效果
        duration: 1000, // 动画执行时间
      },
    },
    slider: {
      start: 0.1,
      end: 0.3,
    },
  };

  //console.log("startday=",startDate.toString(),"end-",endDate.toString());
  function doTimeChange(date, dateString) {
    //console.log(">>> YYYY-MM-DD HH:mm:ss=", date,date.format("YYYY-MM-DD HH:mm:ss"));
    setEndDate(date);
  }

  return (
    <PageContainer key={'mainContainer'}>
      <ProTable<执行器, JobGroupPageListParams>
        rowKey="id"
        columns={columns}
        search={{ labelWidth: 'auto' }}
        request={async (params) => {
          const data = await queryExecuterPage(params);
          return {
            ...data,
            data: data?.contentList,
          };
        }}
        scroll={{ x: 1000 }}
        toolBarRender={(action) => [
          <BetaSchemaForm<SaveParams>
            key="create"
            title="新建"
            trigger={<ProAction type="primary">新建</ProAction>}
            layoutType="ModalForm"
            width={500}
            layout="horizontal"
            labelCol={{ span: 6 }}
            onFinish={async (values) => {
              await addExecuter(values);
              action?.reload();
              return true;
            }}
            columns={formCol}
          />,

          <ProAction
            key="3"
            type="primary"
            onClick={() => {
              if (polling) {
                setPolling(undefined);
                return;
              }
              setPolling(pollingInterval);
            }}
          >
            {polling ? <LoadingOutlined /> : <ReloadOutlined />}
            {polling ? '停止轮询' : '开始轮询'}
          </ProAction>,
        ]}
        polling={polling}
        expandable={{ expandedRowRender }}
      />

      <Modal
        title={'显示3小时内历史数据'}
        key={'modelfrm'}
        visible={visible}
        confirmLoading={true}
        footer={null}
        onOk={() => {
          setVisible(false);
        }}
        onCancel={() => {
          setVisible(false);
        }}
        width={800}
      >
        <Spin spinning={loading} key={'spin'}>
          <Row gutter={[16, 16]}>
            <Col span={6}>
              <DatePicker
                onChange={doTimeChange}
                defaultValue={endDate}
                renderExtraFooter={() => '当前时间'}
                showTime
              />
            </Col>
            <Col span={4}>
              <Button
                type="primary"
                value="查询"
                onClick={() => {
                  console.log('>>> click');
                  searchStatData(searchServerName);
                }}
              >
                查询
              </Button>
            </Col>
          </Row>
          <Row gutter={[16, 16]} style={{ marginTop: '10px' }}>
            <Col span={24}>
              <Tabs defaultActiveKey="1" style={{ height: '420px' }}>
                <TabPane tab="CPU" key="1">
                  <Line data={cpuData} {...cpuConf} style={{ height: '300px' }} />
                </TabPane>
                <TabPane tab="JVM内存" key="2">
                  <Line data={jvmData} {...jvmConf} style={{ height: '300px' }} />
                </TabPane>
                <TabPane tab="物理机内存" key="3">
                  <Line data={memData} {...memConf} style={{ height: '300px' }} />
                </TabPane>
              </Tabs>
            </Col>
          </Row>
        </Spin>
      </Modal>
    </PageContainer>
  );
};

export default App;
