package com.rohitbhoompally.tack.customviews;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by bearcatmobile on 1/11/15.
 */
public class RectangularVLayout3 extends OverlayLayout {

    public RectangularVLayout3(Context context) {
        super(context);
    }

    public RectangularVLayout3(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);

        super.onMeasure(
                MeasureSpec.makeMeasureSpec(width / 3, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY)
        );
    }
}
