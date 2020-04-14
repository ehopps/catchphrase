package com.example.catchphrase;

import android.content.res.Resources;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;

class GameSounds {
    private static final String TAG = "GameSounds";
    private SoundPool pool;

    private int buttonPress;
    private int clockTick;
    private int clockTock;
    private int fanfare;

    GameSounds(Resources resources) {
        if (android.os.Build.VERSION.SDK_INT < 21) {
            pool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        }
        else {
            SoundPool.Builder builder = new SoundPool.Builder();
            builder.setMaxStreams(2);
            builder.setAudioAttributes(
                    new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_GAME)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build());
            pool = builder.build();
        }

        buttonPress = pool.load(resources.getResourceName(R.raw.button1), 1);
        fanfare = pool.load(resources.getResourceName(R.raw.success1), 1);
    }

    void button() {
        pool.play(buttonPress, 1f, 1f, 1, 1, 1f);
    }

    void fanfare() {
        pool.play(fanfare, 1f, 1f, 1, 1, 1f);
    }

    void destroy() {
        pool.release();
        pool = null;
    }
}