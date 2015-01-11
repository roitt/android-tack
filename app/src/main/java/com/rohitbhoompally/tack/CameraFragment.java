package com.rohitbhoompally.tack;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.rohitbhoompally.tack.adapters.CustomGridViewAdapter;
import com.rohitbhoompally.tack.adapters.FrameImageAdapter;
import com.rohitbhoompally.tack.customviews.BottomButtonsLayout;
import com.rohitbhoompally.tack.customviews.DrawingView;
import com.rohitbhoompally.tack.customviews.GridLayoutItems;
import com.rohitbhoompally.tack.customviews.SquareLayout;
import com.rohitbhoompally.tack.utils.BusProvider;
import com.rohitbhoompally.tack.utils.LayoutChangedEvent;
import com.rohitbhoompally.tack.utils.PictureTakenEvent;
import com.rohitbhoompally.tack.utils.SharedPrefHandler;
import com.squareup.otto.Subscribe;

import java.util.List;

/**
 * Created by Rohit Bhoompally on 12/8/14.
 */
public class CameraFragment extends Fragment {
    private Context mContext;
    private CameraPreview mPreview;
    BottomButtonsLayout bottomLayout;
    FrameLayout cameraLayout;
    ImageButton flashButton;
    ImageButton switchButton;
    DrawingView drawingView;
    SquareLayout squareLayout;
    CustomGridViewAdapter gAdapter;

    List<String> flashModes;
    private int mCameraId = 0;


    private static String activeFlash = Camera.Parameters.FLASH_MODE_OFF;

    // newInstance constructor for creating fragment with arguments
    public static CameraFragment newInstance() {
        CameraFragment cameraFragment = new CameraFragment();
        return cameraFragment;
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
        mCameraId = SharedPrefHandler.getCameraWhich(mContext);
        activeFlash = SharedPrefHandler.getFlashMode(mContext);
        createCameraPreview();
        checkCameras();
        checkFlash();
        setFlashMode(activeFlash);
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPrefHandler.setFlashMode(mContext, activeFlash);
        SharedPrefHandler.setCameraWhich(mContext, mCameraId);
        mPreview.stop();
        cameraLayout.removeView(mPreview); // This is necessary.
        mPreview = null;
        BusProvider.getInstance().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        cameraLayout = (FrameLayout) view.findViewById(R.id.camera_preview);
        bottomLayout = (BottomButtonsLayout) view.findViewById(R.id.bottom_rl);

        flashButton = (ImageButton) view.findViewById(R.id.flash_button);
        switchButton = (ImageButton) view.findViewById(R.id.switch_camera_button);
        drawingView = (DrawingView) view.findViewById(R.id.drawView);

        squareLayout = (SquareLayout) view.findViewById(R.id.root_layout);
        finalizeGridViewLayout(squareLayout);

        squareLayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CustomGridViewAdapter.mViewSelectedPosition = position;
                gAdapter.notifyDataSetInvalidated();
            }
        });

        ImageButton captureButton = (ImageButton) view.findViewById(R.id.capture_button);
        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPreview.takePicture();
                    }
                }
        );

        flashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFlashes();
            }
        });
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleCameras();
            }
        });

        /* Handling navigation to frames fragment */
        ImageButton framesButton = (ImageButton) view.findViewById(R.id.frames_button);
        framesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.setViewPagerItem(0);
            }
        });

        ImageButton photosButton = (ImageButton) view.findViewById(R.id.photos_button);
        photosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.setViewPagerItem(2);
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
        actionBar.setLogo(getResources().getDrawable(R.drawable.ic_tack));
    }

    public static int getActionBarHeight(Context context){
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[] { android.R.attr.actionBarSize });
        int mActionBarSize = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        return mActionBarSize;
    }

    public void setFlashMode(String flashMode) {
        activeFlash = flashMode;
        flashButton.setImageDrawable(getFlashDrawable(flashMode));
        mPreview.changeCameraFlashParameters(flashMode);
    }

    public Drawable getFlashDrawable(String flashMode) {
        switch (flashMode) {
            case Camera.Parameters.FLASH_MODE_OFF:
                return getResources().getDrawable(R.drawable.flash_no_selector);
            case Camera.Parameters.FLASH_MODE_ON:
                return getResources().getDrawable(R.drawable.flash_selector);
            case Camera.Parameters.FLASH_MODE_AUTO:
                return getResources().getDrawable(R.drawable.flash_auto_selector);
            default:
                return getResources().getDrawable(R.drawable.flash_no_selector);
        }
    }

    public void toggleFlashes() {
        checkFlash();
        String nextFlashMode = Camera.Parameters.FLASH_MODE_OFF;
        switch (activeFlash) {
            case Camera.Parameters.FLASH_MODE_OFF:
                if (flashModes.contains(Camera.Parameters.FLASH_MODE_ON)) {
                    activeFlash = Camera.Parameters.FLASH_MODE_ON;
                    // set the focus mode
                    nextFlashMode = Camera.Parameters.FLASH_MODE_ON;
                    flashButton.setImageDrawable(getResources().getDrawable(R.drawable.flash_selector));
                }
                break;
            case Camera.Parameters.FLASH_MODE_ON:
                if (flashModes.contains(Camera.Parameters.FLASH_MODE_AUTO)) {
                    activeFlash = Camera.Parameters.FLASH_MODE_AUTO;
                    // set the focus mode
                    nextFlashMode = Camera.Parameters.FLASH_MODE_AUTO;
                    flashButton.setImageDrawable(getResources().getDrawable(R.drawable.flash_auto_selector));
                } else {
                    activeFlash = Camera.Parameters.FLASH_MODE_OFF;
                    // set the focus mode
                    nextFlashMode = Camera.Parameters.FLASH_MODE_OFF;
                    flashButton.setImageDrawable(getResources().getDrawable(R.drawable.flash_no_selector));
                }
                break;
            case Camera.Parameters.FLASH_MODE_AUTO:
                activeFlash = Camera.Parameters.FLASH_MODE_OFF;
                // set the focus mode
                nextFlashMode = Camera.Parameters.FLASH_MODE_OFF;
                flashButton.setImageDrawable(getResources().getDrawable(R.drawable.flash_no_selector));
                break;
        }
        mPreview.changeCameraFlashParameters(nextFlashMode);
    }

    public void checkCameras() {
        if(mPreview.checkFrontFacingCamera()) {
            switchButton.setClickable(true);
        } else {
            switchButton.setClickable(false);
        }
    }

    public void checkFlash() {
        if(mPreview.checkFlashCapabilities()) {
            flashButton.setClickable(true);
            flashModes = mPreview.getFlashCapabilities();
        } else {
            flashButton.setClickable(false);
        }
    }

    public void toggleCameras() {
        mPreview.stop();
        cameraLayout.removeView(mPreview);
        if(mCameraId == 0) {
            mCameraId = 1;
            flashButton.setImageDrawable(getResources().getDrawable(R.drawable.flash_no_selector));
            flashButton.setClickable(false);
        }
        else if(mCameraId == 1) {
            mCameraId = 0;
            // Update to what is current.
            flashButton.setImageDrawable(getResources().getDrawable(R.drawable.flash_no_selector));
            flashButton.setClickable(true);
        }
        createCameraPreview();
    }

    public void createCameraPreview() {
        mPreview = new CameraPreview(getActivity(), mCameraId, CameraPreview.LayoutMode.FitToParent, drawingView);
        LayoutParams previewLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        cameraLayout.addView(mPreview, 0, previewLayoutParams);
    }

    public void finalizeGridViewLayout(SquareLayout squareLayout) {
        int position = FrameImageAdapter.mSelectedPosition;
        int numberOfItems = 0;
        switch (position) {
            case GridLayoutItems.SQUARE_2_HORIZONTAL:
                // This layout requires 1 column and 2 rows
                squareLayout.setNumColumns(1);
                numberOfItems = 2;
                break;
            case GridLayoutItems.SQUARE_2_VERTICAL:
                // This layout requires 1 column and 1 rows
                squareLayout.setNumColumns(2);
                numberOfItems = 2;
                break;
            case GridLayoutItems.SQUARE_3_HORIZONTAL:
                squareLayout.setNumColumns(1);
                numberOfItems = 3;
                break;
            case GridLayoutItems.SQUARE_3_VERTICAL:
                squareLayout.setNumColumns(3);
                numberOfItems = 3;
                break;
            case GridLayoutItems.SQUARE_4:
                squareLayout.setNumColumns(2);
                numberOfItems = 4;
                break;
        }
        gAdapter = new CustomGridViewAdapter(mContext, numberOfItems, position);
        squareLayout.setAdapter(gAdapter);
    }

    @Subscribe
    public void layoutRefresh(LayoutChangedEvent event) {
        // TODO: React to the event somehow!
        if(gAdapter != null && squareLayout != null) {

            finalizeGridViewLayout(squareLayout);
        }
    }
}
