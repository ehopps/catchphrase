package com.example.catchphrase;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import java.io.IOException;

class GameSounds {
    private static final String TAG = "GameSounds";

    private static GameSounds instance;
    private static SoundPool pool;

    private static int buttonPress;
    private static int clockTick;
    private static int clockTock;
    private static int fanfare;

    private GameSounds(Context context) {
        // create a SoundPool
        if (android.os.Build.VERSION.SDK_INT < 21) {
            pool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        }
        else {
            SoundPool.Builder builder = new SoundPool.Builder();
            builder.setMaxStreams(3);
            builder.setAudioAttributes(
                    new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_GAME)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build());
            pool = builder.build();
        }

        // load all sounds into SoundPool
        try (
                AssetFileDescriptor button1File = context.getAssets().openFd("sounds/data/button1.mp3");
                AssetFileDescriptor fanfareFile = context.getAssets().openFd("sounds/data/success1.mp3");
                )
        {
            buttonPress = pool.load(button1File, 1);
            fanfare = pool.load(fanfareFile, 1);
        }
        catch (IOException e) {
            Log.e(TAG, "Couldn't find sounds.", e);
        }
    }

    static void init(Context context) {
        if (instance == null) instance = new GameSounds(context);
    }

    static void button() {
        try {
            pool.play(buttonPress, 1f, 1f, 1, 0, 1f);
        }
        catch (NullPointerException e) {
            Log.e(TAG, "No SoundPool exists to play from.", e);
        }
    }

    static void fanfare() {
        try {
            pool.play(fanfare, 1f, 1f, 1, 0, 1f);
        }
        catch (NullPointerException e) {
            Log.e(TAG, "No SoundPool exists to play from.", e);
        }
    }

    static void deInit() {
        if (pool != null) {
            pool.release();
            pool = null;
        }
    }
}