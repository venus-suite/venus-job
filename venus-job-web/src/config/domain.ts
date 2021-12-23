import { env } from './common';

const domainPrefix = env === 'prod' ? '' : `${env}-`;

/**
 * 默认域名
 */
export const defaultDomain = `//localhost:8901`;
