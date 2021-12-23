import ProAction from '@/components/ProAction';
import { PageContainer } from '@ant-design/pro-layout';
import ProTable from '@ant-design/pro-table';
import { BetaSchemaForm } from '@ant-design/pro-form';
import request from '@/utils/request';
import { defaultDomain } from '@/config/domain';

const App = () => {
  const addParams = [
    {
      title: '邮箱账号',
      dataIndex: 'email',
      formItemProps: {
        rules: [
          {
            pattern: '^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(.[a-zA-Z0-9_-]+)+$',
            required: true,
            message: '请填写合法邮箱地址',
          },
        ],
      },
    },
    {
      title: '是否超级管理员',
      dataIndex: 'isSuper',
      valueType: 'radio',
      initialValue: 0,
      formItemProps: {
        rules: [
          {
            required: true,
            message: '请选择',
          },
        ],
      },
      fieldProps: {
        options: [
          { label: '否', value: 0 },
          { label: '是', value: 1 },
        ],
      },
    },
    {
      title: '用户名',
      dataIndex: 'name',
      formItemProps: {
        rules: [
          {
            required: true,
            message: '请填写用户名',
          },
        ],
      },
    },
    {
      title: '密码',
      dataIndex: 'password',
      formItemProps: {
        rules: [
          {
            required: true,
            message: '请填写密码',
          },
        ],
      },
    },
  ];

  const typeEnum = {
    1: {
      text: '超级管理员',
      render: (
        <svg
          t="1640067701515"
          class="icon"
          viewBox="0 0 1024 1024"
          version="1.1"
          xmlns="http://www.w3.org/2000/svg"
          p-id="2121"
          width="20"
          height="20"
        >
          <path
            d="M876.0576 769.024c5.12 34.816 34.82624 29.696 39.94624 45.056 0 9.71776-5.12 45.056-19.968 49.664-14.84288 0-34.816-5.12-49.664 0-9.71776 5.12-19.95776 9.72288-25.07776 19.96288-9.728 25.08288 9.71776 45.05088 0 54.77888-14.848 9.728-45.06112 25.088-54.784 14.848-14.84288-14.848-14.84288-34.82112-45.056-34.82112-39.94112 0-39.94112 39.94112-59.904 39.94112s-54.784-14.848-54.784-29.70112c5.12-19.96288 14.848-34.81088-5.12-54.77888-25.088-19.968-54.77888 0-65.01888-9.728-14.84288-9.728-19.96288-34.816-14.84288-49.65888s39.936-14.84288 39.936-54.78912c0-29.69088-34.816-34.81088-39.936-54.77376-5.12-14.848 5.12-39.94624 19.96288-45.06624 25.08288-9.71776 39.93088 9.728 65.01888-9.71776 14.848-14.83776 9.728-49.664 9.728-59.904 5.12-14.84288 25.07776-19.96288 39.936-19.96288 29.696 0 19.96288 34.82112 59.904 39.94112 39.936 0 34.816-14.848 54.77888-39.94112 25.08288 0 45.06112 9.72288 49.65888 19.96288 9.728 19.968-9.71776 49.664 19.968 69.63712 25.07776 14.84288 49.664-5.12 65.024 0 9.72288 5.12 14.84288 19.96288 19.96288 39.94112-9.72288 29.16352-54.78912 24.04352-49.66912 69.10976z m-179.70176-99.84c-119.80288 29.696-74.752 204.28288 39.936 184.832 115.2-25.09824 99.84-209.92-39.936-184.832z m-204.288-174.58688c-119.808 0-214.528-94.72-214.528-214.528s94.72-214.528 214.528-214.528c119.80288 0 214.52288 94.72 214.52288 214.528 0.00512 114.688-94.71488 214.528-214.52288 214.528z m174.592 49.65888s-79.88224 0-74.76224 54.784c5.12 59.904-74.752 14.83776-89.59488 65.024-19.968 59.89376 9.72288 49.65376 25.08288 89.6 9.728 25.08288-39.936 39.936-34.816 84.992 9.728 59.904 59.904 29.70624 74.75712 65.024 9.72288 29.696-25.08288 49.664-104.96 49.664-65.01888 0-189.44 0-234.496-5.12 0 0-129.536-5.12-129.536-89.6 0 0-5.12-124.928 74.752-214.528 0 0 84.992-109.56288 229.38112-109.56288l264.192 9.72288z"
            fill="#2165AB"
            p-id="2122"
          ></path>
        </svg>
      ),
    },
    0: {
      text: '普通用户',
      render: (
        <svg
          t="1640067969962"
          class="icon"
          viewBox="0 0 1024 1024"
          version="1.1"
          xmlns="http://www.w3.org/2000/svg"
          p-id="3629"
          width="20"
          height="20"
        >
          <path
            d="M513.183454 532.442588c-62.243623 0-120.763434-24.225751-164.778904-68.215638-44.020586-43.992957-68.26271-102.486162-68.26271-164.703179 0-62.215994 24.242124-120.708176 68.26271-164.702156 44.01547-43.989887 102.535281-68.216661 164.778904-68.216661 62.243623 0 120.764458 24.226774 164.778904 68.216661 44.018539 43.992957 68.260663 102.485139 68.260663 164.702156 0 62.217017-24.242124 120.710222-68.260663 164.703179C633.947911 508.215814 575.4281 532.442588 513.183454 532.442588zM513.183454 117.828592c-100.254332 0-181.817975 81.507362-181.817975 181.694155 0 100.186793 81.563644 181.695179 181.817975 181.695179 100.254332 0 181.816952-81.508385 181.816952-181.695179C695.000406 199.336978 613.437785 117.828592 513.183454 117.828592z"
            p-id="3630"
          ></path>
          <path
            d="M879.044072 956.940698 146.070308 956.940698l-1.243317-24.303522c-0.317225-6.202259-0.477884-12.514012-0.477884-18.762319 0-49.262984 9.751085-97.06264 28.983101-142.071739 18.562774-43.444465 45.127803-82.45085 78.95626-115.937522 33.81106-33.468253 73.183789-59.745732 117.02632-78.100776 45.3867-19.002796 93.581352-28.637224 143.245472-28.637224 49.665143 0 97.861842 9.634428 143.247518 28.637224 43.842531 18.356067 83.21526 44.632523 117.02632 78.100776 33.828456 33.486672 60.393485 72.493058 78.955236 115.937522 19.230993 45.0091 28.982078 92.808756 28.982078 142.071739 0 6.246261-0.161682 12.56006-0.480954 18.768459L879.044072 956.940698zM195.678146 905.71706l633.765251 0c-4.380774-169.130002-144.846946-305.366849-316.883137-305.366849C340.525092 600.350211 200.05892 736.587058 195.678146 905.71706z"
            p-id="3631"
          ></path>
        </svg>
      ),
    },
  };

  const queryParams = [
    { title: '用户名', dataIndex: 'name' },
    { title: '邮箱账号', dataIndex: 'email' },
    {
      title: '权限',
      dataIndex: 'isSuper',
      valueType: 'select',
      valueEnum: {
        0: { text: '普通用户' },
        1: { text: '超级管理员' },
      },
      render: (_, record) => (
        <div style={{ display: 'flex', alignItems: 'center' }}>
          {typeEnum[record.isSuper].render}
          <div>{typeEnum[record.isSuper].text}</div>
        </div>
      ),
    },
    { title: '最后操作人', dataIndex: 'operator', search: false },
    { title: '创建时间', dataIndex: 'createTime', search: false },
    { title: '更新时间', dataIndex: 'updateTime', search: false },
    { title: '最后登陆时间', dataIndex: 'lastLoginTime', search: false },
    {
      title: '操作',
      valueType: 'option',
      fixed: 'right',
      width: 100,
      render: (dom, record, index, action) => [
        <BetaSchemaForm
          key="update"
          title="编辑"
          trigger={<ProAction type="link">编辑</ProAction>}
          layoutType="ModalForm"
          width={500}
          initialValues={record}
          onFinish={async (values) => {
            const res = await request({
              url: '/job/jobUser/updateVenusJobUser',
              method: 'POST',
              params: { ...values, id: record.id },
            });
            action?.reload();
            return true;
          }}
          columns={addParams}
        />,
        <ProAction
          key="delete"
          type="link"
          danger
          confirm="确定删除这条数据吗？"
          onClick={async () => {
            await request({
              baseURL: defaultDomain,
              method: 'POST',
              url: '/job/jobUser/deleteVenusJobUser',
              successMessage: true,
              params: { id: record.id },
            });
            action?.reload();
          }}
        >
          删除
        </ProAction>,
      ],
    },
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
            url: '/job/jobUser/queryVenusJobUserPage',
            method: 'POST',
            params: params,
          });
          return { ...data, data: data?.contentList };
        }}
        toolBarRender={(action) => [
          <BetaSchemaForm
            key="create"
            title="新增db配置信息"
            trigger={<ProAction type="primary">新增</ProAction>}
            layoutType="ModalForm"
            width={500}
            onFinish={async (values) => {
              await request({
                baseURL: defaultDomain,
                url: '/job/jobUser/addVenusJobUser',
                method: 'POST',
                params: values,
              });
              action?.reload();
              return true;
            }}
            columns={addParams}
          />,
        ]}
      />
    </PageContainer>
  );
};

export default App;
