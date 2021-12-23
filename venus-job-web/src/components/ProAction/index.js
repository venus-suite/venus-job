import _objectSpread from "@babel/runtime/helpers/esm/objectSpread2";
import _objectWithoutProperties from "@babel/runtime/helpers/esm/objectWithoutProperties";
import { Button, Popconfirm, Tooltip } from 'antd';
import React, { cloneElement } from 'react';

var genTooltip = function genTooltip(element, tooltip) {
  if (tooltip) {
    return /*#__PURE__*/React.createElement(Tooltip, {
      title: tooltip
    }, element);
  }

  return element;
};

var genConfirm = function genConfirm(element, _ref, confirm) {
  var onClick = _ref.onClick,
      restProps = _objectWithoutProperties(_ref, ["onClick"]);

  if (confirm && !restProps.disabled) {
    return (
      /*#__PURE__*/
      // @ts-ignore
      React.createElement(Popconfirm, {
        title: confirm,
        onConfirm: onClick
      }, /*#__PURE__*/cloneElement(element, restProps))
    );
  }

  return /*#__PURE__*/cloneElement(element, _objectSpread(_objectSpread({}, restProps), {}, {
    onClick: onClick
  }));
};

var ProAction = function ProAction(_ref2) {
  var style = _ref2.style,
      confirm = _ref2.confirm,
      tooltip = _ref2.tooltip,
      restProps = _objectWithoutProperties(_ref2, ["style", "confirm", "tooltip"]);

  var buttonStyles = restProps.type === 'link' ? _objectSpread({
    padding: 0
  }, style) : style;
  var buttonElement = /*#__PURE__*/React.createElement(Button, {
    style: buttonStyles
  });
  return genTooltip(genConfirm(buttonElement, restProps, confirm), tooltip);
};

export default ProAction;