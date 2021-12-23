import { ButtonProps } from 'antd';
export interface ProActionProps extends ButtonProps {
    /**
     * 确认框的描述
     */
    confirm?: string;
    /**
     * 提示文字
     */
    tooltip?: string;
}
declare const ProAction: ({ style, confirm, tooltip, ...restProps }: ProActionProps) => JSX.Element;
export default ProAction;
