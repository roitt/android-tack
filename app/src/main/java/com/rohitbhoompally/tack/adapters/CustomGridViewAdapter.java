package com.rohitbhoompally.tack.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.rohitbhoompally.tack.R;
import com.rohitbhoompally.tack.customviews.GridLayoutItems;
import com.rohitbhoompally.tack.customviews.OverlayLayout;
import com.rohitbhoompally.tack.customviews.RectagularHLayout;
import com.rohitbhoompally.tack.customviews.RectagularVLayout;
import com.rohitbhoompally.tack.customviews.RectangularHLayout3;
import com.rohitbhoompally.tack.customviews.RectangularLayout4;
import com.rohitbhoompally.tack.customviews.RectangularVLayout3;

/**
 * Created by Rohit on 1/10/15.
 */
public class CustomGridViewAdapter extends BaseAdapter {

    private Context mContext;
    private int mNumberOfItems;
    private final int mFramePosition;
    public static int mViewSelectedPosition;
    OverlayLayout imageView;

    public CustomGridViewAdapter(Context c, int numberOfItems, int position) {
        mContext = c;
        this.mFramePosition = position;
        this.imageView = null;
        this.mViewSelectedPosition = 0;
        this.mNumberOfItems = numberOfItems;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mNumberOfItems;
    }
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        grid = inflater.inflate(R.layout.layout_grid_item, null);
        ImageView refresh = (ImageView) grid.findViewById(R.id.retake);
        imageView = getRequiredLayout(grid);
        refresh.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_retake));
        if (mViewSelectedPosition == position)
            imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.transparent));
        else
            imageView.setBackground(mContext.getResources().getDrawable(R.drawable.whitish));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        ViewGroup gridImageView = (RelativeLayout) grid.findViewById(R.id.grid_image_view);
        gridImageView.removeAllViewsInLayout();
        gridImageView.addView(imageView);
        return grid;
    }

    public OverlayLayout getRequiredLayout(View grid) {
        OverlayLayout imageView;
        switch (mFramePosition) {
            case GridLayoutItems.SQUARE_2_HORIZONTAL:
                imageView = new RectagularHLayout(mContext);
                return imageView;
            case GridLayoutItems.SQUARE_2_VERTICAL:
                imageView = new RectagularVLayout(mContext);
                return imageView;
            case GridLayoutItems.SQUARE_3_HORIZONTAL:
                imageView = new RectangularHLayout3(mContext);
                return imageView;
            case GridLayoutItems.SQUARE_3_VERTICAL:
                imageView = new RectangularVLayout3(mContext);
                return imageView;
            case GridLayoutItems.SQUARE_4:
                imageView = new RectangularLayout4(mContext);
                return imageView;
            default:
                return null;
        }
    }
}
