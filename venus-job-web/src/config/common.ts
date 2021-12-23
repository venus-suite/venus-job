const { hostname } = window.location;

const segments = /[0-9a-zA-Z-]+/.exec(hostname)![0].split('-');

/**
 * 后端接口环境，本地开发使用的默认环境需要在 `.env.local` 中通过 `UMI_APP_ENV` 环境变量设置，不要直接修改这里
 */
export const env =
  process.env.UMI_APP_ENV ??
  (segments[0] === 'test' || segments[0] === 'localhost' ? 'test' : 'prod');
