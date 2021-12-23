import { dashboard } from '../services/nameSpaceManager';
import { Area, Pie } from '@ant-design/charts';
import { PageContainer } from '@ant-design/pro-layout';
import { Card, Col, Row, Spin, Statistic } from 'antd';
import React, { useEffect, useState } from 'react';
import '../../assets/jsMind.css';

const App = () => {
  const [loading, setLoading] = useState(true);
  const [data, setData] = useState([]);
  const [pieData, setPieData] = useState([]);
  const [executorCount, setExecutorCount] = useState(0);
  const [jobInfoCount, setJobInfoCount] = useState(0);
  const [jobLogCount, setJobLogCount] = useState(0);
  const [jobLogSuccessCount, setJobLogSuccessCount] = useState(0);
  const [jobErrorCount, setJobErrorCount] = useState(0);

  async function asyncFetch() {
    let json = await dashboard({});
    setLoading(false);
    // @ts-ignore
    let data = json.dayChartInfoList;
    // @ts-ignore
    let dash = json.dashInfo;
    //executorCount: 11
    setExecutorCount(dash?.executorCount);
    // jobInfoCount: 458
    setJobInfoCount(dash?.jobInfoCount);
    setJobLogCount(dash?.jobLogCount);
    // jobLogCount: 458
    setJobLogSuccessCount(dash?.jobLogSuccessCount);
    // jobLogSuccessCount: 1208154
    // @ts-ignore
    setJobErrorCount(json.triggerCountFailTotal);

    let pieData = [
      {
        type: '成功数',
        // @ts-ignore
        value: json.triggerCountSucTotal,
      },
      {
        type: '失败数',
        // @ts-ignore
        value: json.triggerCountFailTotal,
      },
      {
        type: '运行中',
        // @ts-ignore
        value: json.triggerCountRunningTotal,
      },
    ];
    setPieData(pieData);
    setData(data);
  }

  useEffect(() => {
    asyncFetch();
  }, []);

  const config = {
    data,
    xField: 'label',
    yField: 'value',
    seriesField: 'category',
    xAxis: {
      type: 'time',
    },
    yAxis: {
      label: {
        // 数值格式化为千分位
        formatter: (v) => `${v}`.replace(/\d{1,3}(?=(\d{3})+$)/g, (s) => `${s},`),
      },
    },
    areaStyle: {
      fillOpacity: 0.4,
    },
    color: ['#1979C9', '#D62A0D', '#FAA219'],
  };

  const pieConfig = {
    appendPadding: 10,
    pieData,
    angleField: 'value',
    colorField: 'type',
    radius: 0.9,
    areaStyle: {
      fillOpacity: 0.4,
    },
    label: {
      type: 'inner',
      offset: '-30%',
      content: ({ percent }) => `${(percent * 100).toFixed(0)}%`,
      style: {
        fontSize: 14,
        textAlign: 'center',
      },
    },
    interactions: [
      {
        type: 'element-active',
      },
    ],
    color: ['#1979C9', '#D62A0D', '#FAA219'],
  };

  return (
    <PageContainer>
      <Spin spinning={loading}>
        <div className="site-card-wrapper">
          <Row gutter={16}>
            <Col span={4}>
              <Card title="" bordered={false}>
                <Statistic title="执行器数量" value={executorCount} suffix="" />
              </Card>
            </Col>
            <Col span={4}>
              <Card title="" bordered={false}>
                <Statistic title="任务数量" value={jobInfoCount} />
              </Card>
            </Col>
            <Col span={6}>
              <Card title="" bordered={false}>
                <Statistic title="调度次数" value={jobLogCount} suffix="" />
              </Card>
            </Col>
            <Col span={6}>
              <Card title="" bordered={false}>
                <Statistic title="执行成功数量" value={jobLogSuccessCount} suffix="" />
              </Card>
            </Col>
            <Col span={4}>
              <Card title="" bordered={false}>
                <Statistic title="执行失败数目" value={jobErrorCount} suffix="" />
              </Card>
            </Col>
          </Row>

          <Row style={{ marginTop: '10px' }}>
            <Col span={16}>
              <Card title="调度报表">
                <Area {...config} data={data} />
              </Card>
            </Col>
            <Col span={8}>
              <Card title="成功比例图">
                <Pie {...pieConfig} data={pieData} />
              </Card>
            </Col>
          </Row>
        </div>
      </Spin>
    </PageContainer>
  );
};

export default App;
