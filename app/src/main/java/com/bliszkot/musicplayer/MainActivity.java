package com.bliszkot.musicplayer;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    MediaPlayer music;
    int[] songs;
    String[] songNames;
    int song = 0;
    CountDownTimer timer;
    TextView playingTime;
    SeekBar seekBar;
    String FORMAT = "%2d:%02d";
    TextView musicLabel;
    Boolean isPlaying = false;
    ImageButton next, previous;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playingTime = findViewById(R.id.playingTime);
        musicLabel = findViewById(R.id.music_label);
        next = findViewById(R.id.next);
        previous = findViewById(R.id.previous);

        songsList();
        music = MediaPlayer.create(this, songs[song]);

        ImageButton play = findViewById(R.id.playpause);
        play.setOnClickListener(view -> {
            if (music.isPlaying()) {
                play.setImageDrawable(getDrawable(android.R.drawable.ic_media_play));
                music.pause();
                isPlaying = true;
            } else {
                play.setImageDrawable(getDrawable(android.R.drawable.ic_media_pause));
                playSong();
                music.start();
            }
        });

        buttonNext();
        buttonPrevious();


        seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (music != null && fromUser) {
                    music.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void songsList() {
        songs = new int[]{
                R.raw.daang_daang,
                R.raw.the_wakhra_song,
                R.raw.pileche,
                R.raw.kala_chashma,
                R.raw.i_am_an_albatraoz,
                R.raw.achcham_telugandham,
                R.raw.zumba,
        };

        songNames = new String[]{
                "Daang Daang", "The wakhra song", "Pileche", "Kala chashma", "I am an albatroaz", "Achcham telugandham", "Zumba"
        };
    }

    private void buttonNext() {
        next.setOnClickListener(view -> {
            isPlaying = false;
            music.release();
            song++;
            Log.e("Song", "" + song);
            if (song > 6) song = 0;

            playSong();
            music.start();
        });
    }

    private void buttonPrevious() {
        previous.setOnClickListener(view -> {
            isPlaying = false;
            music.release();
            song--;
            Log.i("Num", "" + song);
            if (song < 0) song = songs.length - 1;

            playSong();
            music.start();
        });
    }

    private void setTotalDuration() {
        TextView totalTime = findViewById(R.id.totalTime);
        int timer = music.getDuration();
        totalTime.setText(String.format(FORMAT,
                TimeUnit.MILLISECONDS.toMinutes(timer),
                TimeUnit.MILLISECONDS.toSeconds(timer) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
                                .toMinutes(timer)))
        );
    }

    private void playSong() {
        String songName = songNames[song] + ".mp3";
        musicLabel.setText(songName);

        if (!isPlaying) {
            music = MediaPlayer.create(this, songs[song]);
            setTotalDuration();
        }

        seekBar.setMax(music.getDuration() / 1000);
        Handler mHandler = new Handler();

        MainActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (music != null) {
                    int timer = music.getCurrentPosition();
                    int mCurrentPosition = timer / 1000;
                    seekBar.setProgress(mCurrentPosition);
                    playingTime.setText(String.format(FORMAT,
                            TimeUnit.MILLISECONDS.toMinutes(timer),
                            TimeUnit.MILLISECONDS.toSeconds(timer) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
                                            .toMinutes(timer)))
                    );
                }
                mHandler.postDelayed(this, 1000);
            }
        });
    }
}