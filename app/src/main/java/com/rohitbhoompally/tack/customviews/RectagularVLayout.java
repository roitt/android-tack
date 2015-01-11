package com.rohitbhoompally.tack.customviews;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by bearcatmobile on 1/10/15.
 */
public class RectagularVLayout extends OverlayLayout {
    public RectagularVLayout(Context context) {
        super(context);
    }

    public RectagularVLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);

        super.onMeasure(
                MeasureSpec.makeMeasureSpec(width / 2, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY)
        );
    }
}
