package com.bytedance.videoplayer;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class VedioPlayActivity extends AppCompatActivity {

    private String url="http://jzvd.nathen.cn/video/1137e480-170bac9c523-0007-1823-c86-de200.mp4";
    private String url2="http://pic.ibaotu.com/00/20/08/96e888piCHck.mp4";
    private String url3="http://jzvd.nathen.cn/video/2a101070-170bad88892-0007-1823-c86-de200.mp4";
    private Button playbtn;
    private Button pausebtn;
    private VideoView videoView;
    private SeekBar seekBar;
    private TextView totaltimetv;
    private TextView currenttimetv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vedio_play);

        //隐藏ActionBar
        getSupportActionBar().hide();
        //设置页面全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        videoView=findViewById(R.id.videoView);
        playbtn=findViewById(R.id.playbutton);
        pausebtn=findViewById(R.id.pausebutton);
        seekBar=findViewById(R.id.seekBar);
        totaltimetv=findViewById(R.id.totaltime_textview);
        currenttimetv=findViewById(R.id.currenttime_textview);

        totaltimetv.bringToFront();


//        videoView.setVideoPath(getVideoPath(R.raw.bytedance));

        videoView.setVideoURI(Uri.parse(url));


        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        videoView.setLayoutParams(params);

//        videoView.start();
//        seekBar.setMax(videoView.getDuration());
//        int total=videoView.getDuration();
//        int minute=total/60000;
//        int second=(total-minute*6000)/1000;
//        totaltimetv.setText(" "+minute+":"+second);


//        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
//                    @Override
//                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
//                        int surfaceWidth = videoView.getWidth();
//                        int surfaceHeight = videoView.getHeight();
//                        // 获取视频资源的宽度
//                        int videoWidth = mp.getVideoWidth();
//                        // 获取视频资源的高度
//                        int videoHeight = mp.getVideoHeight();
//
//                        //根据视频尺寸去计算->视频可以在sufaceView中放大的最大倍数。
//                        float max;
//                        if (getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
//                            //竖屏模式下按视频宽度计算放大倍数值
//                            max = Math.max((float) videoWidth / (float) surfaceWidth, (float) videoHeight / (float) surfaceHeight);
//                        } else {
//                            //横屏模式下按视频高度计算放大倍数值
//                            max = Math.max(((float) videoWidth / (float) surfaceHeight), (float) videoHeight / (float) surfaceWidth);
//                        }
//
//                        //视频宽高分别/最大倍数值 计算出放大后的视频尺寸
//                        videoWidth = (int) Math.ceil((float) videoWidth / max);
//                        videoHeight = (int) Math.ceil((float) videoHeight / max);
//                        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(videoWidth, videoHeight);
//                        params.addRule(RelativeLayout.CENTER_IN_PARENT);
//                        videoView.setLayoutParams(params);
//                    }
//                });
//
//            }
//        });



        VideoThread videoThread=new VideoThread();
        videoThread.start();

//        videoView.setMediaController(new MediaController(this));
        videoView.requestFocus();
        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.start();
                seekBar.setMax(videoView.getDuration());//必须在这里获取，因为getduration是获取当前播放的视频的总长度，所以如果不在视频开始播放后获取，就会把seekbar的max设为0
                int total=videoView.getDuration();
                int minute=total/60000;
                int second=(total-minute*6000)/1000;
                totaltimetv.setText(" "+minute+":"+second);
            }
        });

        pausebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.pause();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                videoView.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                videoView.seekTo(seekBar.getProgress());//不能在onprogresschanged里面设置，因为handler改变seekbar的progress也会触发onprogresschanged，导致在一秒内重复播放
                int total= seekBar.getProgress();
                int minute=total/60000;
                int second=(total-minute*6000)/1000;
                currenttimetv.setText(" "+minute+":"+second);
            }
        });



    }

    private String getVideoPath(int resId) {
        return "android.resource://" + this.getPackageName() + "/" + resId;
    }

    Handler videohandler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 888:
                    seekBar.setProgress(videoView.getCurrentPosition());
                    int total=videoView.getCurrentPosition();
                    int minute=total/60000;
                    int second=(total-minute*6000)/1000;
                    currenttimetv.setText(" "+minute+":"+second);
                    break;
            }
        }
    };


    class VideoThread extends Thread{
        @Override
        public void run() {
            super.run();
            while(!Thread.currentThread().isInterrupted())
            {

                Message message=new Message();
                message.what=888;
                videohandler.sendMessage(message);
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,dip2px(this,235f));
//            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//            videoView.setLayoutParams(params);
//        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.FILL_PARENT,
//                RelativeLayout.LayoutParams.FILL_PARENT);
//            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//            videoView.setLayoutParams(layoutParams);
//        }
//    }

//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//            params.addRule(RelativeLayout.CENTER_IN_PARENT);
//            videoView.setLayoutParams(params);
//        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//            RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//            params.addRule(RelativeLayout.CENTER_IN_PARENT);
//            videoView.setLayoutParams(params);
//        }
//    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
        public static int dip2px(Context context, float dpValue) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
        }


//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//
//        // Checks the orientation of the screen
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.FILL_PARENT,
//                RelativeLayout.LayoutParams.FILL_PARENT);
//            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//            videoView.setLayoutParams(layoutParams);
//            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
//
//        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
//
//
//            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
//
//        }
//    }

}
