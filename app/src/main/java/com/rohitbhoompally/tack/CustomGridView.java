package com.rohitbhoompally.tack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * Created by Rohit on 1/10/15.
 */
public class CustomGridView extends BaseAdapter {

    private Context mContext;
    private final int[] Imageid;

    public CustomGridView(Context c, int[] Imageid) {
        mContext = c;
        this.Imageid = Imageid;
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
            RectagularHLayout imageView = (RectagularHLayout)grid.findViewById(R.id.grid_image_view);
            refresh.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_retake));
            imageView.setImageResource(Imageid[position]);
        } else {
            grid = convertView;
        }
        return grid;
    }
}
