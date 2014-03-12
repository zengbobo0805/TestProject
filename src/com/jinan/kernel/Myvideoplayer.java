package com.jinan.kernel;

import android.app.Activity;

import android.content.BroadcastReceiver;

import android.content.Context;

import android.content.Intent;

import android.content.IntentFilter;

import android.content.pm.ActivityInfo;

import android.graphics.PixelFormat;

import android.media.AudioManager;

import android.media.MediaPlayer;

import android.os.Bundle;
import android.os.PowerManager;

import android.os.Environment;

import android.view.MotionEvent;

import android.view.SurfaceHolder;

import android.view.SurfaceView;

import android.view.Window;

import android.view.WindowManager;

public class Myvideoplayer extends Activity implements SurfaceHolder.Callback {
	// String path =
	// Environment.getExternalStorageDirectory().getPath()+"/movie.mp4";
	private boolean isPause = false;
	private  static String path;
	private SurfaceHolder surfaceHolder;
	private MediaPlayer mediaPlayer;
	private SurfaceView surfaceView;
	private Intent floatingwindow = null;
	private String TAG = "android.intent.action.myvideoplayer";
	private MyBroadcastReceiver mr;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题栏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 设置为竖屏
		setContentView(R.layout.main);
		getWindow().setFormat(PixelFormat.UNKNOWN);
		// 初始化相关类 设置相关属性
		surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mediaPlayer.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK);
		mediaPlayer.setDisplay(surfaceHolder);// 设置Video影片以SurfaceHolder播放
		mediaPlayer
				.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
					@Override
					public void onCompletion(MediaPlayer mp) {
						// mp.stop();
						mediaPlayer.stop();
						Myvideoplayer.this.finish(); // 关闭视频播放器的界面
					}

				});

		floatingwindow = new Intent();
		floatingwindow.setClass(Myvideoplayer.this, FloatingService.class);
		mr = new MyBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(TAG);
		this.registerReceiver(mr, intentFilter);
		Intent intent = getIntent();
		path = intent.getStringExtra("moviename");
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int position = this.mediaPlayer.getCurrentPosition();
		floatingwindow.putExtra("position", position);
		floatingwindow.putExtra("max", mediaPlayer.getDuration());
		floatingwindow.putExtra("visable", true); //
		startService(floatingwindow);
		return super.onTouchEvent(event);
	}

	public void playVideo(String strPath) {// 自定义播放影片函数
		if (mediaPlayer.isPlaying() == true) {
			mediaPlayer.reset();
		}
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mediaPlayer.setDisplay(surfaceHolder);// 设置Video影片以SurfaceHolder播放
		try {
			mediaPlayer.setDataSource(strPath); // 设置MediaPlayer的数据源
			mediaPlayer.prepare(); // 准备
		} catch (Exception e) {
			e.printStackTrace();
		}
		mediaPlayer.start();
	}

	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {

	}

	public void surfaceCreated(SurfaceHolder holder) {
		mediaPlayer.setDisplay(holder);
		try {
			mediaPlayer.setDataSource(path); // 设置MediaPlayer的数据源
			mediaPlayer.prepare(); // 准备
			mediaPlayer.start(); // 播放
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void surfaceDestroyed(SurfaceHolder arg0) {

	}

	@Override
	protected void onDestroy() {
		stopService(floatingwindow);
		this.unregisterReceiver(mr);
		if (this.mediaPlayer != null) {
			this.mediaPlayer.release();
			mediaPlayer = null;
		}
		super.onDestroy();
	}

	public class MyBroadcastReceiver extends BroadcastReceiver { // 自定义广播接受者
		@Override
		public void onReceive(Context arg0, Intent intent) {
			String action = intent.getAction();
			if (action.equals(TAG)) {
				String flag = intent.getStringExtra("flag");
				if (flag.equals("play")) {
					if (!mediaPlayer.isPlaying())
						mediaPlayer.start();
				} else if (flag.equals("pause")) {
					if (mediaPlayer.isPlaying())
						mediaPlayer.pause(); // 暂停
				} else if (flag.equals("change")) {
					int pos = intent.getIntExtra("newpos", 0);
					if (mediaPlayer.isPlaying())
						mediaPlayer.pause(); // 暂停
					mediaPlayer.seekTo(pos);
					mediaPlayer.start();
				} else if (flag.equals("forward")) {
					int pos = intent.getIntExtra("newpos", 0);
					if (mediaPlayer.isPlaying())
						mediaPlayer.pause(); // 暂停
					mediaPlayer.seekTo(pos);
					mediaPlayer.start();
				} else if (flag.equals("backward")) {
					int pos = intent.getIntExtra("newpos", 0);
					if (mediaPlayer.isPlaying())
						mediaPlayer.pause(); // 暂停
					mediaPlayer.seekTo(pos);
					mediaPlayer.start();
				} else {
					Myvideoplayer.this.finish();
				}
			}
		}
	}
}