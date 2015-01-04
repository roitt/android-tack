package com.rohitbhoompally.tack;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Rohit Bhoompally on 12/8/14.
 */
public class GalleryFragment extends Fragment {
    private Context mContext;

    // newInstance constructor for creating fragment with arguments
    public static GalleryFragment newInstance() {
        GalleryFragment galleryFragment = new GalleryFragment();
        return galleryFragment;
    }

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
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
        actionBar.setLogo(getResources().getDrawable(R.drawable.ic_photos));
    }
}
