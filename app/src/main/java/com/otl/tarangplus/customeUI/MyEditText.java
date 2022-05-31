package com.otl.tarangplus.customeUI;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.otl.tarangplus.R;


/**
 * Created by Kiran on 10/14/2015.
 */
public class MyEditText extends androidx.appcompat.widget.AppCompatEditText {
    public MyEditText(Context context) {
        super(context);
//        if(!isInEditMode())
        init(null);
    }

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
//        if(!isInEditMode())
        init(attrs);
    }

    public MyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
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
