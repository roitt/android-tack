package com.rohitbhoompally.tack.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.rohitbhoompally.tack.R;

/**
 * Created by Rohit on 1/3/15.
 */
public class FrameImageAdapter extends BaseAdapter {
    private Context mContext;
    public static int mSelectedPosition;

    public FrameImageAdapter(Context c) {
        mContext = c;
        mSelectedPosition = 0;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(275, 275));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setPadding(5, 5, 5, 5);
        } else {
            imageView = (ImageView) convertView;
        }

        if(position == mSelectedPosition)
            imageView.setImageResource(mThumbIdsSelected[position]);
        else
            imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.frame_2_h, R.drawable.frame_2_v, R.drawable.frame_3_h, R.drawable.frame_3_v, R.drawable.frame_4_vh
    };

    // references to selected images
    private Integer[] mThumbIdsSelected = {
            R.drawable.frame_2_h_selected, R.drawable.frame_2_v_selected, R.drawable.frame_3_v_selected, R.drawable.frame_3_h_selected, R.drawable.frame_4_vh_selected
    };
}
