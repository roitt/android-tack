package com.rohitbhoompally.tack.customviews;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.rohitbhoompally.tack.R;
import com.rohitbhoompally.tack.adapters.FrameImageAdapter;

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
        setHasOptionsMenu(true);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        restoreActionBar();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // Was true initially.
        actionBar.setDisplayUseLogoEnabled(true); // To show the logo.
        actionBar.setLogo(getResources().getDrawable(R.drawable.ic_frames));
    }
}
