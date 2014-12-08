package com.rohitbhoompally.tack;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Rohit Bhoompally on 12/8/14.
 */
public class GalleryFragment extends Fragment {

    // newInstance constructor for creating fragment with arguments
    public static GalleryFragment newInstance() {
        GalleryFragment galleryFragment = new GalleryFragment();
        return galleryFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        return view;
    }
}
