package com.reactnativeviewvisibility;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.view.ViewTreeObserver;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.facebook.react.views.scroll.ReactHorizontalScrollView;
import com.facebook.react.views.scroll.ReactScrollView;
import com.facebook.react.views.view.ReactViewGroup;

@SuppressLint("ViewConstructor")
public class ViewVisibilityView extends ReactViewGroup {
    private ReactScrollView scrollView;
    private ReactHorizontalScrollView scrollHView;
    private final RCTEventEmitter mEventEmitter;

    private Boolean bannerExists = false;
    private String idBanner = "";
    private Boolean horizontal = false;
    private Boolean detach = false;
    private int percentVisibility = 50;

    private static final int BANNER_NOT_VISIBLE = 0;
    private static final int BANNER_PARTIALLY_VISIBLE = 1;
    private static final int BANNER_PERCENT_VISIBLE = 2;
    private static final int BANNER_FULLY_VISIBLE = 3;

    private int isVisible = BANNER_NOT_VISIBLE;
    private boolean isSendNotification = false;

    public ViewVisibilityView(ThemedReactContext themedReactContext) {
        super(themedReactContext);

        mEventEmitter = themedReactContext.getJSModule(RCTEventEmitter.class);
        setLayerType(LAYER_TYPE_HARDWARE, null);
    }

    public void setIdBanner(String id) {
        this.idBanner = id;
    }

    public void setHorizontal(Boolean h) {
        this.horizontal = h;
    }

    public void setPercentVisibility(int p) {
        this.percentVisibility = p;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (horizontal) {
            if (scrollHView == null) {
                scrollHView = ViewVisibilityView.fintSpecifyParent(ReactHorizontalScrollView.class, this.getParent());

                if (scrollHView != null) {
                    setHandleHScroll();
                }
            } else {
                setHandleHScroll();
            }
        } else {
            if (scrollView == null) {
                scrollView = ViewVisibilityView.fintSpecifyParent(ReactScrollView.class, this.getParent());

                if (scrollView != null) {
                    setHandleScroll();
                }
            } else {
                setHandleScroll();
            }
        }
    }

    private void setHandleScroll() {
        bannerExists = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            scrollView.setOnScrollChangeListener(new OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                    ViewVisibilityView.this.handleScroll();
                }
            });
        } else {
            scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {
                    ViewVisibilityView.this.handleScroll();
                }
            });
        }
    }

    private void setHandleHScroll() {
        bannerExists = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            scrollHView.setOnScrollChangeListener(new OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                    ViewVisibilityView.this.handleScroll();
                }
            });
        } else {
            scrollHView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {
                    ViewVisibilityView.this.handleScroll();
                }
            });
        }
    }

    private void handleScroll() {
        if (bannerExists) {
            Rect scrollBounds = new Rect();
            if (horizontal) {
                scrollHView.getHitRect(scrollBounds);
            } else {
                scrollView.getHitRect(scrollBounds);
            }

            if (ViewVisibilityView.this.getLocalVisibleRect(scrollBounds)) {
                if (getVisibilityPercents(this) > 0 && getVisibilityPercents(this) < 100 && this.isVisible != BANNER_PARTIALLY_VISIBLE) {
                    this.isVisible = BANNER_PARTIALLY_VISIBLE;
                    this.onAdVisibleChangeReceived(BANNER_PARTIALLY_VISIBLE);
                } else if (getVisibilityPercents(this) > this.percentVisibility && !this.isSendNotification && this.isVisible == BANNER_PARTIALLY_VISIBLE) {
                    this.isSendNotification = true;
                    this.onAdVisibleChangeReceived(BANNER_PERCENT_VISIBLE);
                } else if (getVisibilityPercents(this) > 99 && this.isVisible == BANNER_PARTIALLY_VISIBLE) {
                    this.isVisible = BANNER_FULLY_VISIBLE;
                    this.onAdVisibleChangeReceived(BANNER_FULLY_VISIBLE);
                }
            } else {
                this.isVisible = BANNER_NOT_VISIBLE;
                this.isSendNotification = false;
                this.onAdVisibleChangeReceived(BANNER_NOT_VISIBLE);
            }
        }
    }

    public void onAdVisibleChangeReceived (int visible) {
        WritableMap event = Arguments.createMap();
        event.putInt("visible",visible);
        event.putString("id",this.idBanner);

        mEventEmitter.receiveEvent(this.getId(), "onAdVisibleChangeReceived", event);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        bannerExists = false;
        this.detach = true;
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);

        if (visibility == View.VISIBLE && !this.detach) {
            if (this.isVisible != BANNER_FULLY_VISIBLE && getVisibilityPercents(this) > 0) {
                this.isVisible = BANNER_FULLY_VISIBLE;

                if (!this.isSendNotification) {
                    this.isSendNotification = true;
                    this.onAdVisibleChangeReceived(BANNER_PERCENT_VISIBLE);
                }

                this.onAdVisibleChangeReceived(BANNER_FULLY_VISIBLE);
            } else if (this.isVisible != BANNER_FULLY_VISIBLE && scrollView == null && scrollHView == null) {
                scrollView = ViewVisibilityView.fintSpecifyParent(ReactScrollView.class, this.getParent());
                scrollHView = ViewVisibilityView.fintSpecifyParent(ReactHorizontalScrollView.class, this.getParent());

                if (scrollView == null && scrollHView == null) {
                    this.isVisible = BANNER_FULLY_VISIBLE;

                    if (!this.isSendNotification) {
                        this.isSendNotification = true;
                        this.onAdVisibleChangeReceived(BANNER_PERCENT_VISIBLE);
                    }

                    this.onAdVisibleChangeReceived(BANNER_FULLY_VISIBLE);
                }
            }
        }
    }

    public static <T> T fintSpecifyParent(Class<T> tClass, ViewParent parent) {
        if (parent == null) {
            return null;
        } else {
            if (tClass.isInstance(parent)) {
                return (T) parent;
            } else {
                return fintSpecifyParent(tClass, parent.getParent());
            }
        }
    }

    public int getVisibilityPercents(View view) {
        final Rect currentViewRect = new Rect();

        int percents = 100;

        if (horizontal) {
            int width = (view == null || view.getVisibility() != View.VISIBLE) ? 0 : view.getWidth();

            if (width == 0) {
                return 0;
            }
            view.getLocalVisibleRect(currentViewRect);

            if (viewIsPartiallyHiddenLeft(currentViewRect)) {
                percents = (width - currentViewRect.left) * 100 / width;
            } else if (viewIsPartiallyHiddenRight(currentViewRect, width)) {
                percents = currentViewRect.right * 100 / width;
            }

        } else {
            int height = (view == null || view.getVisibility() != View.VISIBLE) ? 0 : view.getHeight();

            if (height == 0) {
                return 0;
            }

            view.getLocalVisibleRect(currentViewRect);

            if (viewIsPartiallyHiddenTop(currentViewRect)) {
                percents = (height - currentViewRect.top) * 100 / height;
            } else if (viewIsPartiallyHiddenBottom(currentViewRect, height)) {
                percents = currentViewRect.bottom * 100 / height;
            }
        }

        return percents;
    }

    private static boolean viewIsPartiallyHiddenBottom(Rect currentViewRect, int height) {
        return currentViewRect.bottom > 0 && currentViewRect.bottom < height;
    }

    private static boolean viewIsPartiallyHiddenTop(Rect currentViewRect) {
        return currentViewRect.top > 0;
    }

    private static boolean viewIsPartiallyHiddenRight(Rect currentViewRect, int width) {
        return currentViewRect.right > 0 && currentViewRect.right < width;
    }

    private static boolean viewIsPartiallyHiddenLeft(Rect currentViewRect) {
        return currentViewRect.left > 0;
    }
}
