package com.ityang.qtmusic.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;

import com.ityang.qtmusic.bean.MusicInfo;
import com.ityang.qtmusic.util.MediaUtils;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlayService extends Service implements MediaPlayer.OnPreparedListener {

    private MediaPlayer mediaPlayer;
    private List<MusicInfo> musicInfos;
    public int currentPosition;
    public int currentProgress;
    private int duration;
    private ExecutorService executorService;
    public static final int STATE_PAUSE = 2;
    public static final int STATE_DEFAULT = 0;
    public static final int STATE_PLAYING = 1;
    public static final int PLAYMODE_NORMOL = 0;
    public static final int PLAYMODE_RANDOM = 1;
    public static final int PLAYMODE_SINGLE = 2;

    private int playState = 0;
    private int playMode;

    public List<MusicInfo> getMusicInfos() {
        return musicInfos;
    }

    public int getPlayState() {
        return playState;
    }

    public int getDuration() {
        return duration;
    }

    public void setPlayMode(int playMode) {
        this.playMode = playMode;
    }

    public int getPlayMode() {
        return playMode;
    }

    /**
     * 自定义binder类，用于获取PlayService
     */
    public class PalyServiceBinder extends Binder {
        public PlayService getPlayService() {
            return PlayService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //获取歌曲信息
        musicInfos = MediaUtils.getMusicInfos(this);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (mediaPlayer.isPlaying()) {
                        playServiceListener.onProgressUpdate(currentProgress = mediaPlayer.getCurrentPosition());
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //播放结束后自动播放下一首
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                next();
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new PalyServiceBinder();
    }

    /**
     * 播放音乐
     *
     * @param position
     */
    public void play(int position) {
        //System.out.println("执行播放");
        if (mediaPlayer != null) {
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(this, Uri.parse(musicInfos.get(position).getUrl()));
                mediaPlayer.prepareAsync();
                // mediaPlayer.prepare();
                // mediaPlayer.start();
                //改变当前播放音乐的索引
                currentPosition = position;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 暂停播放音乐
     */
    public void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            playState = STATE_PAUSE;
        }
    }

    /**
     * 继续播放音乐
     */
    public void keepOn() {
        if (mediaPlayer != null && (!mediaPlayer.isPlaying())) {

            if (playState == STATE_DEFAULT) { //当刚进入程序时点击播放按钮从第一首开始播
                play(0);
            } else {
                mediaPlayer.start();
                playState = STATE_PLAYING;
            }
        }
    }

    /**
     * 播放下一首音乐
     */
    public void next() {
        playState = STATE_PLAYING;
        switch (playMode) {
            case PLAYMODE_NORMOL:
                if (currentPosition == musicInfos.size() - 1) {
                    currentPosition = 0;
                } else {
                    currentPosition++;
                }
                break;
            case PLAYMODE_RANDOM:
                Random random = new Random();
                currentPosition = random.nextInt(musicInfos.size());
                break;
            default:
                break;
        }

        play(currentPosition);
    }

    /**
     * 播放上一首音乐
     */
    public void pre() {
        playState = STATE_PLAYING;
        switch (playMode) {
            case PLAYMODE_NORMOL:
                if (currentPosition == 0) {
                    currentPosition = musicInfos.size() - 1;
                } else {
                    currentPosition--;
                }
                break;
            case PLAYMODE_RANDOM:
                Random random = new Random();
                currentPosition = random.nextInt(musicInfos.size());
                break;
            default:
                break;
        }
        play(currentPosition);
    }

    /**
     * 异步准备完成就播放
     *
     * @param mp
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.start();
        playState = STATE_PLAYING;
        //获取总长度
        duration = mediaPlayer.getDuration();
        playServiceListener.publish(currentPosition);
    }

    /**
     * 改变进度
     */
    public void seekTo(int progress) {

        if (playState == STATE_DEFAULT) {
            play(0);
        } else if (playState == STATE_PAUSE) {
            mediaPlayer.start();
        }
        mediaPlayer.seekTo(progress);
        playState = STATE_PLAYING;
    }

    /**
     * 服务销毁时释放资源
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!executorService.isShutdown()) {
            executorService.shutdown();
        }
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
        executorService = null;
    }

    private PlayServiceListener playServiceListener;

    public void setPlayServiceListener(PlayServiceListener playServiceListener) {
        this.playServiceListener = playServiceListener;
    }

    /**
     * 回调接口，设置播放进度更新与ui更新
     */
    public interface PlayServiceListener {
        //position代表播放歌曲索引
        public void publish(int position);

        public void onProgressUpdate(int progress);
    }

}
