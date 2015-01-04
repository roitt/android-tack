package com.rohitbhoompally.tack;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

/**
 * Created by Rohit Bhoompally on 12/8/14.
 */
public class LayoutFragment extends Fragment {

    private Context mContext;
    private FrameImageAdapter mFrameImageAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        mContext = getActivity();
    }

    public static LayoutFragment newInstance() {
        LayoutFragment layoutFragment = new LayoutFragment();
        return layoutFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout, container, false);

        final GridView gridview = (GridView) view.findViewById(R.id.gridview);
        mFrameImageAdapter = new FrameImageAdapter(mContext);
        gridview.setAdapter(mFrameImageAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                FrameImageAdapter.mSelectedPosition = position;
                mFrameImageAdapter.notifyDataSetChanged();
            }
        });
        return view;
    }
}
