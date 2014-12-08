package com.rohitbhoompally.tack;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Rohit Bhoompally on 12/8/14.
 */
public class LayoutFragment extends Fragment {

    public static LayoutFragment newInstance() {
        LayoutFragment layoutFragment = new LayoutFragment();
        return layoutFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout, container, false);
        return view;
    }
}
