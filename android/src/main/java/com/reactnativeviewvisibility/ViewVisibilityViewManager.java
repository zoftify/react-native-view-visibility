package com.reactnativeviewvisibility;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.Map;

public class ViewVisibilityViewManager extends ViewGroupManager {
    public static final String REACT_CLASS = "ViewVisibilityView";

    ViewVisibilityViewManager(){}

    @Override
    @NonNull
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    @NonNull
    public View createViewInstance(@NonNull ThemedReactContext reactContext) {
        return new ViewVisibilityView(reactContext);
    }

    @Nullable
    @Override
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.<String, Object>builder()
                .put("onAdVisibleChangeReceived",
                        MapBuilder.of("registrationName", "onAdVisibleChangeReceived"))
                .build();
    }

    private ViewVisibilityView castToBannerView(View view) {
        return (ViewVisibilityView) view;
    }

    // props
    @ReactProp(name = "idBanner")
    public void setIdBanner(View view, String idBanner) {
        if (idBanner != null) {
            castToBannerView(view).setIdBanner(idBanner);
        }
    }

    @ReactProp(name = "horizontal")
    public void setHorizontal(View view, Boolean horizontal) {
        castToBannerView(view).setHorizontal(horizontal);
    }

    @ReactProp(name = "percentVisibility")
    public void setPercentVisibility(View view, int percentVisibility) {
        castToBannerView(view).setPercentVisibility(percentVisibility);
    }

}
