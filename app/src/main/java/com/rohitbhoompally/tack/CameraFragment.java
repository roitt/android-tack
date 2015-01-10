package com.rohitbhoompally.tack;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

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
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPrefHandler.setFlashMode(mContext, activeFlash);
        SharedPrefHandler.setCameraWhich(mContext, mCameraId);
        mPreview.stop();
        cameraLayout.removeView(mPreview); // This is necessary.
        mPreview = null;
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

        SquareLayout squareLayout = (SquareLayout) view.findViewById(R.id.root_layout);
        View square2View = inflater.inflate(R.layout.square_2_vertical, null);

        squareLayout.addView(square2View);

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
}
