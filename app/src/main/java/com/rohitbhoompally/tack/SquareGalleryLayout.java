package com.rohitbhoompally.tack;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by bearcatmobile on 1/8/15.
 */
public class SquareGalleryLayout extends ImageView {
    public SquareGalleryLayout(Context context) {
        super(context);
    }

    public SquareGalleryLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(
                widthMeasureSpec, widthMeasureSpec
        );
    }
}
