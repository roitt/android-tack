package com.rohitbhoompally.tack.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

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
    private final int[] Imageid;
    private final int mFramePosition;

    public CustomGridViewAdapter(Context c, int[] Imageid, int position) {
        mContext = c;
        this.Imageid = Imageid;
        this.mFramePosition = position;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return Imageid.length;
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
        if (convertView == null) {
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.layout_grid_item, null);
            ImageView refresh = (ImageView) grid.findViewById(R.id.retake);
            OverlayLayout imageView = getRequiredLayout(grid);
            refresh.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_retake));
            imageView.setImageResource(Imageid[position]);
        } else {
            grid = convertView;
        }
        return grid;
    }

    public OverlayLayout getRequiredLayout(View grid) {
        OverlayLayout imageView;
        switch (mFramePosition) {
            case GridLayoutItems.SQUARE_2_HORIZONTAL:
                imageView = (RectagularHLayout)grid.findViewById(R.id.grid_image_view);
                return imageView;
            case GridLayoutItems.SQUARE_2_VERTICAL:
                imageView = (RectagularVLayout)grid.findViewById(R.id.grid_image_view);
                return imageView;
            case GridLayoutItems.SQUARE_3_HORIZONTAL:
                imageView = (RectangularHLayout3)grid.findViewById(R.id.grid_image_view);
                return imageView;
            case GridLayoutItems.SQUARE_3_VERTICAL:
                imageView = (RectangularVLayout3)grid.findViewById(R.id.grid_image_view);
                return imageView;
            case GridLayoutItems.SQUARE_4:
                imageView = (RectangularLayout4)grid.findViewById(R.id.grid_image_view);
                return imageView;
            default:
                return null;
        }
    }
}
