package com.healthcoco.healthcocopad.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.DoctorVideos;
import com.healthcoco.healthcocopad.custom.DownloadFileFromUrlAsyncTask;
import com.healthcoco.healthcocopad.enums.HealthCocoFileType;
import com.healthcoco.healthcocopad.listeners.DownloadFileFromUrlListener;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;

/**
 * Created by Shreshtha on 01-08-2017.
 */

public class VideoFragment extends HealthCocoFragment implements MediaPlayer.OnPreparedListener, DownloadFileFromUrlListener {

    private VideoView videoView;
    private int position = 0;
    private MediaController mediaController;
    private DoctorVideos doctorVideos;
    private ProgressBar progressLoadingHorizontal;
    private ProgressBar progressLoadingCircular;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_video, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void init() {
        Intent intent = mActivity.getIntent();
        doctorVideos = Parcels.unwrap(intent.getParcelableExtra(HealthCocoConstants.TAG_DOCTOR_VIDEO_DATA));
        initViews();
        initListeners();
        setMediaController();
    }

    private void setMediaController() {
        // Set the media controller buttons
        if (mediaController == null) {
            mediaController = new MediaController(mActivity);

            // St the videoView that acts as the anchor for the MediaController.
            mediaController.setAnchorView(videoView);
            new DownloadFileFromUrlAsyncTask(mActivity, this, HealthCocoFileType.DOCTOR_VIDEO, Util.getFileNameFromUrl(doctorVideos.getVideoUrl()), progressLoadingCircular, progressLoadingHorizontal).execute(doctorVideos.getVideoUrl());
        }
    }

    @Override
    public void initViews() {
        videoView = (VideoView) view.findViewById(R.id.video_view);
        progressLoadingHorizontal = (ProgressBar) view.findViewById(R.id.progress_loading_horizontal);
        progressLoadingCircular = (ProgressBar) view.findViewById(R.id.progress_loading_circular);
    }

    @Override
    public void initListeners() {
        videoView.setOnPreparedListener(this);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        videoView.seekTo(position);
        if (position == 0) {
            videoView.start();
        }
        // When video Screen change size.
        mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                // Re-Set the videoView that acts as the anchor for the MediaController
                mediaController.setAnchorView(videoView);
            }
        });
    }

    // When you change direction of phone, this method will be called.
    // It store the state of video (Current position)
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        // Store current position.
        savedInstanceState.putInt("CurrentPosition", videoView.getCurrentPosition());
        videoView.pause();
    }

    @Override
    public void onPostExecute(String filePath) {
        if (!Util.isNullOrBlank(filePath)) {
            videoView.setVideoPath(filePath);
            videoView.setMediaController(mediaController);
            videoView.requestFocus();
            videoView.start();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    @Override
    public void onPreExecute() {

    }

    //    // After rotating the phone. This method is called.
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Get saved position.
        position = savedInstanceState.getInt("CurrentPosition");
        videoView.seekTo(position);
    }
}
