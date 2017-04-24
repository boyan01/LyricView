package com.yangbin.lyricviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.SeekBar;

import com.yangbin.lyricview.widght.LyricView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private LyricView lyricView;
    private SeekBar sbPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lyricView = (LyricView) findViewById(R.id.view_lyric);
        sbPlay = (SeekBar) findViewById(R.id.sb_play_progress);
        lyricView.setLyric(getResources().openRawResource(R.raw.call_me_maybe), null);
        sbPlay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                lyricView.scrollLyricTo(progress * 1000);
                Log.i(TAG, "scrollLyricTo: " + progress * 1000);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ((SeekBar) findViewById(R.id.sb_text_size)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                lyricView.setLyricTextSize(progress + 10);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
