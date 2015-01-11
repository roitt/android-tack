package com.rohitbhoompally.tack.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by Rohit on 1/11/15.
 */
public class CamButtonsLayout extends LinearLayout {
    public CamButtonsLayout(Context context) {
        super(context);
    }

    public CamButtonsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        super.onMeasure(
                MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec((int) (height * 0.4), MeasureSpec.EXACTLY)
        );
    }
}
