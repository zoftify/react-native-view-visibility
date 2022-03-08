import * as React from 'react';
import { requireNativeComponent, ViewProps } from 'react-native';

const ComponentName = 'ViewVisibilityView';

const ViewVisibility = requireNativeComponent(ComponentName);

const BANNER_STATE_TYPE = {
  BANNER_NOT_VISIBLE: 0,
  BANNER_PARTIALLY_VISIBLE: 1,
  BANNER_PERCENT_VISIBLE: 2,
  BANNER_FULLY_VISIBLE: 3,
};

interface INativeEvent {
  nativeEvent: {
    visible?: number;
    id?: string;
  };
}

interface IViewVisibility extends ViewProps {
  idBanner: string;
  percentVisibility?: number;
  onAdVisibleChange?: (visible: number, idBanner: string) => void;
  horizontal?: boolean;
}

export default class ViewVisibilityView extends React.Component<IViewVisibility> {
  static defaultProps = {
    idBanner: '',
    percentVisibility: 50,
    onAdVisibleChange: null,
    horizontal: false,
  };

  constructor(props?: any) {
    super(props);
  }

  /**
   * Banner visibility handler
   * @param event
   */
  onAdVisibleChangeHandler = (event: INativeEvent) => {
    const { onAdVisibleChange } = this.props;
    const visible: number = event.nativeEvent.visible
      ? event.nativeEvent.visible
      : BANNER_STATE_TYPE.BANNER_NOT_VISIBLE;

    const idBanner: string = event.nativeEvent.id ? event.nativeEvent.id : '';

    onAdVisibleChange && onAdVisibleChange(visible, idBanner);
  };

  private getProps() {
    return {
      ...this.props,
      onAdVisibleChangeReceived: this.onAdVisibleChangeHandler,
    };
  }

  render() {
    return <ViewVisibility {...this.getProps()} />;
  }
}
