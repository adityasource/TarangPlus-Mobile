package com.otl.tarangplus.customeUI;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.internal.ViewUtils;
import com.otl.tarangplus.R;
import com.otl.tarangplus.Utils.Helper;

public class MyRecyclerView extends RecyclerView {
    private int maxHeight;
    private int heightMeasureSpec;
    private int widthMeasureSpec;
    private int maxHeightDp;
    public MyRecyclerView(@NonNull Context context) {
        super(context);
    }
    public MyRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        final TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MaxHeightLinearLayout, 0, 0);
        try {
            maxHeightDp = a.getInteger(R.styleable.MaxHeightLinearLayout_maxHeightDp, 0);
        } finally {
            a.recycle();
        }
    }
    public void setMaxHeightDp(int maxHeightDp) {
        this.maxHeightDp = maxHeightDp;
        invalidate();
    }
    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int maxHeightPx = Helper.dpToPx(maxHeightDp);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeightPx, MeasureSpec.AT_MOST);
        super.onMeasure(widthSpec, heightMeasureSpec);
    }
}
