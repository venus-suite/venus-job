import { getTaskTreeNode } from '../services/nameSpaceManager';
import { PageContainer } from '@ant-design/pro-layout';
import { Row } from 'antd';
import 'jsmind/style/jsmind.css';
import React, { useEffect, useState } from 'react';
import '../../assets/jsMind.css';

const jsMind = require('jsmind');

const App = () => {
  const [nameSpaceList, setNameSpaceList] = useState([]);

  async function loadData() {
    let res = await getTaskTreeNode({});
    console.log('>>>>', res);
    // @ts-ignore
    let nameSpaceList = res;
    // @ts-ignore
    setNameSpaceList(nameSpaceList);
    let mind = {
      /* 元数据，定义思维导图的名称、作者、版本等信息 */
      meta: {
        name: 'example',
        author: 'hizzgdev@163.com',
        version: '0.2',
      },
      /* 数据格式声明 */
      format: 'node_array',
      /* 数据内容 */
      data: nameSpaceList,
    };
    let options = {
      container: 'picData',
      editable: false,
      theme: 'belizehole',
      view: {
        line_width: 1, // 思维导图线条的粗细
        line_color: '#5fb7f1', // 思维导图线条的颜色
      },
    };

    let w = document.body.offsetWidth;
    let h = document.body.offsetHeight;
    let obj = document.getElementById('picData');
    obj.style.width = 2 * w + 'px';
    obj.style.height = h - 200 + 'px';
    let jm = new jsMind(options);
    jm.show(mind);
    jm.expand_all();

  }

  useEffect(() => {
    loadData();
  }, []);

  return (
    <PageContainer style={{ width: '100%' }} title={''}>
      <Row style={{ marginTop: '10px' }}>
        <div id="picData" style={{ width: 1000, height: 550, overflow: 'hidden' }} />
      </Row>
    </PageContainer>
  );
};

export default App;
