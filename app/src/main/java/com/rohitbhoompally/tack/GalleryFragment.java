package com.rohitbhoompally.tack;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.RejectedExecutionException;

/**
 * Created by Rohit Bhoompally on 12/8/14.
 */
public class GalleryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {
    private Context mContext;
    private Display display;
    private GridView mGridView;
    GridViewAdapter mAdapter;

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
        display = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    }

    @Override
    public void onResume() {
        super.onResume();
        mContext = getActivity();
        BusProvider.getInstance().register(this);
    }

    /*
     * This callback is invoked when the framework is starting or re-starting the Loader. It
     * returns a CursorLoader object containing the desired query
     */
    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle)
    {
        String[] projection = { MediaStore.Images.Media.DATA,
                MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATE_ADDED};
        String select = MediaStore.Images.Media.DISPLAY_NAME + " like 'TACK_%'";
        String sort = MediaStore.Images.Media.DATE_ADDED;
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        return new CursorLoader(mContext, uri, projection, select, null, sort);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        // Sets the View's data adapter to be a new GridViewAdapter
        mAdapter = new GridViewAdapter(getActivity());
        // Gets a handle to the GridView in the layout
        mGridView = ((GridView) view.findViewById(R.id.images_gridview));

        // Instantiates a DisplayMetrics object
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();

        // Gets the current display metrics from the current Window
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);

        /*
         * Gets the dp value from the thumbSize resource as an integer in dps. The value can
         * be adjusted for specific display sizes, etc. in the dimens.xml file for a particular
         * values-<qualifier> directory
         */
        int pixelSize = getResources().getDimensionPixelSize(R.dimen.thumbSize);

        /*
         * Calculates a width scale factor from the pixel width of the current display and the
         * desired pixel size
         */
        int widthScale = localDisplayMetrics.widthPixels / pixelSize;

        // Calculates the grid column width
        int mColumnWidth = (localDisplayMetrics.widthPixels / widthScale);

        // Sets the GridView's column width
        mGridView.setColumnWidth(mColumnWidth);

        // Starts by setting the GridView to have no columns
        mGridView.setNumColumns(-1);

        // Sets the GridView's data adapter
        mGridView.setAdapter(mAdapter);

        /*
         * Sets the GridView's click listener to be this class. As a result, when users click the
         * GridView, PhotoThumbnailFragment.onClick() is invoked.
         */
        mGridView.setOnItemClickListener(this);

        /*
         * Sets the "empty" View for the layout. If there's nothing to show, a ProgressBar
         * is displayed.
         */
        mGridView.setEmptyView(view.findViewById(R.id.progressRoot));

        getLoaderManager().initLoader(0, null, this);

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

    /*
     * Implements OnItemClickListener.onItemClick() for this View's listener.
     * This implementation detects the View that was clicked and retrieves its picture URL.
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int viewId, long rowId) {


    }

    /*
     * Invoked when the CursorLoader finishes the query. A reference to the Loader and the
     * returned Cursor are passed in as arguments
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor returnCursor) {

        /*
         *  Changes the adapter's Cursor to be the results of the load. This forces the View to
         *  redraw.
         */

        mAdapter.changeCursor(returnCursor);
    }
    /*
     * Invoked when the CursorLoader is being reset. For example, this is called if the
     * data in the provider changes and the Cursor becomes stale.
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        // Sets the Adapter's backing data to null. This prevents memory leaks.
        mAdapter.changeCursor(null);
    }

    /**
     * Defines a custom View adapter that extends CursorAdapter. The main reason to do this is to
     * display images based on the backing Cursor, rather than just displaying the URLs that the
     * Cursor contains.
     */
    private class GridViewAdapter extends CursorAdapter {

        /**
         * Simplified constructor that calls the super constructor with the input Context,
         * a null value for Cursor, and no flags
         * @param context A Context for this object
         */
        public GridViewAdapter(Context context) {
            super(context, null, false);
        }

        /**
         *
         * Binds a View and a Cursor
         *
         * @param view An existing View object
         * @param context A Context for the View and Cursor
         * @param cursor The Cursor to bind to the View, representing one row of the returned query.
         */
        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            // Gets a handle to the View
            ImageView localImageDownloaderView = (ImageView) view.getTag();
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
            // Converts the URL string to a URL and tries to retrieve the picture
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), "Tack");
            File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    cursor.getString(columnIndex));
            Picasso.with(context)
                    .load(mediaFile)
                    .resize(200, 200)
                    .centerCrop().placeholder(R.drawable.black)
                    .error(R.drawable.black)
                    .into(localImageDownloaderView);
        }
        /**
         * Creates a new View that shows the contents of the Cursor
         *
         *
         * @param context A Context for the View and Cursor
         * @param cursor The Cursor to display. This is a single row of the returned query
         * @param viewGroup The viewGroup that's the parent of the new View
         * @return the newly-created View
         */
        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            // Gets a new layout inflater instance
            LayoutInflater inflater = LayoutInflater.from(context);

            /*
             * Creates a new View by inflating the specified layout file. The root ViewGroup is
             * the root of the layout file. This View is a FrameLayout
             */
            View layoutView = inflater.inflate(R.layout.layout_iv_gv, null);

            /*
             * Creates a second View to hold the thumbnail image.
             */
            View thumbView = layoutView.findViewById(R.id.gallery_image);

            // Sets the layoutView's tag to be the same as the thumbnail image tag.
            layoutView.setTag(thumbView);
            return layoutView;
        }
    }

    public void restartLoader(){
        getLoaderManager().restartLoader(0, null, this);
    }

    @Subscribe
    public void pictureRefresh(PictureTakenEvent event) {
        // TODO: React to the event somehow!
        restartLoader();
    }
}
