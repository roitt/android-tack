package com.rohitbhoompally.tack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * Created by Rohit Bhoompally on 12/8/14.
 */
public class CameraFragment extends Fragment {
    private Context mContext;
    private CameraPreview mPreview;
    RelativeLayout bottomLayout;
    FrameLayout cameraLayout;
    ImageButton flashButton;
    ImageButton switchButton;

    List<String> flashModes;
    private int mCameraId = 0;

    private static String TAG = "Camera Fragment";
    public static final int MEDIA_TYPE_IMAGE = 1;
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
        createCameraPreview();
        checkCameras();
        checkFlash();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPreview.stop();
        cameraLayout.removeView(mPreview); // This is necessary.
        mPreview = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        cameraLayout = (FrameLayout) view.findViewById(R.id.camera_preview);
        bottomLayout = (RelativeLayout) view.findViewById(R.id.bottom_rl);

        flashButton = (ImageButton) view.findViewById(R.id.flash_button);
        switchButton = (ImageButton) view.findViewById(R.id.switch_camera_button);

        // Resize the layout
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int actionBarHeight = getActionBarHeight(mContext);
        int screenHeight = size.y - size.x - actionBarHeight;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, screenHeight);
        params.gravity = Gravity.BOTTOM;
        bottomLayout.setLayoutParams(params);

        ImageButton captureButton = (ImageButton) view.findViewById(R.id.capture_button);
        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera
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

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null){
                Log.d(TAG, "Error creating media file, check storage permissions");
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            }
        }
    };

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
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

    public void toggleFlashes() {
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
    ShutterCallback shutterCallback = new ShutterCallback() {
        public void onShutter() {
            //			 Log.d(TAG, "onShutter'd");
        }
    };

    PictureCallback rawCallback = new PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            //			 Log.d(TAG, "onPictureTaken - raw");
        }
    };

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
        if(mCameraId == 0)
            mCameraId = 1;
        else if(mCameraId == 1)
            mCameraId = 0;
        createCameraPreview();
    }

    public void createCameraPreview() {
        mPreview = new CameraPreview(getActivity(), mCameraId, CameraPreview.LayoutMode.FitToParent);
        LayoutParams previewLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        cameraLayout.addView(mPreview, 0, previewLayoutParams);
    }
}
