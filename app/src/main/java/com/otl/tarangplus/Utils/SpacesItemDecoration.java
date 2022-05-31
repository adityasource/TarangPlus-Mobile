package com.otl.tarangplus.Utils;

/**
 * Created by ankith on 25/11/15.
 */

import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int top;
    private int bottom;
    private int left;
    private int right;

    public SpacesItemDecoration(int space, int right) {
        this.left = space;
        this.bottom = space;
        this.right = right;
    }

    public SpacesItemDecoration(int top, int bottom, int left, int right) {
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = left;
        outRect.bottom = bottom;
        outRect.top = top;
        outRect.right = right;
    }

    public static LinearLayout.LayoutParams addLeftMargin(int x) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        lp.setMargins(x, 0, 0, 0);
        return lp;
    }

    public static LinearLayout.LayoutParams addRightMargin(int x) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        lp.setMargins(0, 0, x, 0);
        return lp;
    }

    public int getRight() {
        return right;
    }
}