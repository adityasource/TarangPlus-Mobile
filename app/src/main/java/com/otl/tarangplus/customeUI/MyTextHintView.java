package com.otl.tarangplus.customeUI;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.google.android.material.textfield.TextInputLayout;
import com.otl.tarangplus.R;


/**
 * Created by Kiran on 10/12/2015.
 */
public class MyTextHintView extends TextInputLayout {


    public MyTextHintView(Context context) {
        super(context);
//        if(!isInEditMode())
        init(null);
    }

    public MyTextHintView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        if(!isInEditMode())
        init(attrs);
    }

    public MyTextHintView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        if(!isInEditMode())
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MyTextView);
            String fontName = a.getString(R.styleable.MyTextView_fontName);
            if (fontName != null) {
                Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + fontName);
                setTypeface(myTypeface);
            }
            a.recycle();
        }
    }
}
