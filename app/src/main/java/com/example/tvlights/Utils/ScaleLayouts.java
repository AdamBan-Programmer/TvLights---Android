package com.example.tvlights.Utils;

import android.app.Activity;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import java.io.Serializable;

public class ScaleLayouts implements Serializable {

    // Gets Values and sets parameters
    public void setScallingParams(float percentOfWidth, float percentOfHeight, float percentOfControlHeight, float percentOfTopMargin, float percentOfLeftMargin, View control)
    {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) control.getLayoutParams();
        setScaledSize(percentOfWidth,percentOfHeight,percentOfControlHeight,control,params);
        setScaledMargins(percentOfTopMargin,percentOfLeftMargin,control,params);
    }

    // Setting Place on View
    private void setScaledMargins(float percentOfTopMargin, float percentOfLeftMargin, View control, RelativeLayout.LayoutParams params)
    {
        int width = getScreenWidth();
        int height = getScreenHeight();
        params.leftMargin = (int)(width - width * ((float) (100 - percentOfLeftMargin)/100));
        params.topMargin = (int)(height - height * ((float) (100 - percentOfTopMargin)/100));
        control.setLayoutParams(params);
    }

    // Setting Size
    private void setScaledSize(float percentOfWidth, float percentOfHeight, float percentOfControlHeight, View control, RelativeLayout.LayoutParams params)
    {
        if(percentOfWidth != 0 && percentOfHeight != 0) {
            params.width = getScaledWidth(percentOfWidth);
            params.height = getScaledHeight(percentOfHeight);
            setTextSizeOfControl(params.height,percentOfControlHeight,control);
            control.setLayoutParams(params);
        }
    }

    // returns scaled width by %
    public int getScaledWidth(float percentOfWidth)
    {
        int width = getScreenWidth();
        return  (int)(width - width * ((float) (100 - percentOfWidth)/100));
    }
    // returns scaled height by %
    public int getScaledHeight(float percentOfHeight)
    {
        int height = getScreenHeight();
        return (int)(height - height * ((float) (100 - percentOfHeight)/100));
    }

    // sets margins Should be used in TextView to format the text inside
    public void setTextMargins(int percentFromLeft, int percentFromRight, int percentFromTop, int percentFromBottom, TextView textView)
    {
        int width = getScreenWidth();
        int height = getScreenHeight();
        int paddingLeft = (int)(width - width * ((float) (100 - percentFromLeft)/100));
        int paddingRight = (int)(width - width * ((float) (100 - percentFromRight)/100));
        int paddingTop = (int)(height -height * ((float) (100 - percentFromTop)/100));
        int paddingBottom = (int)(height - height * ((float) (100 - percentFromBottom)/100));

        textView.setPadding(paddingLeft,paddingTop,paddingRight,paddingBottom);
    }

    // Gets width
    public int getScreenWidth()
    {
        return  Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    // Gets height
    public int getScreenHeight()
    {
        return  Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    //Scalling text on controlls
    private void setTextSizeOfControl(int controlHeight, float percentOfControlHeight, View control)
    {
        if(control instanceof TextView) {
            ((TextView) control).setTextSize(TypedValue.COMPLEX_UNIT_PX,controlHeight * percentOfControlHeight/100);
        }
    }

    // Converts pixels on side to the percantage value
    public float pixelsAsPercent(int px, int fullPx)
    {
        double finalPercent = (((double) px/(double) fullPx)*100);
        return (float)finalPercent;
    }
}
