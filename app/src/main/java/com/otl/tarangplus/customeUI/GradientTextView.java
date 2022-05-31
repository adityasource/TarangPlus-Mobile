package com.otl.tarangplus.customeUI;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.otl.tarangplus.R;

public class GradientTextView extends androidx.appcompat.widget.AppCompatTextView {


    int startColor;
    int endColor;

    public GradientTextView(Context context) {
        super(context, null, -1);
        init(null);
    }

    public GradientTextView(Context context,
                            AttributeSet attrs) {
        super(context, attrs, -1);
        init(attrs);
    }

    public GradientTextView(Context context,
                            AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.GradientTextView);
            String fontName = a.getString(R.styleable.GradientTextView_fontName);
            if(TextUtils.isEmpty(fontName))
                 fontName = a.getString(R.styleable.GradientTextView_fontsName);
            startColor = a.getInt(R.styleable.GradientTextView_startcolor, 0);
            endColor = a.getInt(R.styleable.GradientTextView_endcolor, 0);
            if (fontName != null) {
                Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + fontName);
                setTypeface(myTypeface);
            }
            a.recycle();
        }
    }

    @Override
    protected void onLayout(boolean changed,
                            int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            if (startColor != 0 && endColor != 0) {
                getPaint().setShader(new LinearGradient(
                        0, 0, getHeight(), getHeight(),
                        startColor, endColor,
                        Shader.TileMode.CLAMP));
            }
        }
    }

    public void setStartAndEndColor(int start, int end) {
        startColor = start;
        endColor = end;
        if (startColor != 0 && endColor != 0) {
            getPaint().setShader(new LinearGradient(0, 0, getHeight(), getHeight(), startColor, endColor, Shader.TileMode.CLAMP));
            requestLayout();
        }
    }

    public void setStartColor(int startColor) {
        this.startColor = startColor;
    }

    public void setEndColor(int endColor) {
        this.endColor = endColor;
    }
}