package com.rohitbhoompally.tack.customviews;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by Rohit on 1/9/15.
 */
public class RectagularHLayout extends OverlayLayout {
    public RectagularHLayout(Context context) {
        super(context);
    }

    public RectagularHLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);

        super.onMeasure(
                MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(width / 2, MeasureSpec.EXACTLY)
        );
    }
}
