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

    private static int button;
    private static int correct;
    private static int tick;
    private static int tock;
    private static int timeUp;
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
                AssetFileDescriptor buttonFile = context.getAssets().openFd("sounds/data/button3.mp3");
                AssetFileDescriptor correctFile = context.getAssets().openFd("sounds/data/correct2.mp3");
                AssetFileDescriptor tickFile = context.getAssets().openFd("sounds/data/tick1.wav");
                AssetFileDescriptor tockFile = context.getAssets().openFd("sounds/data/tock1.wav");
                AssetFileDescriptor timeUpFile = context.getAssets().openFd("sounds/data/timer1.mp3");
                AssetFileDescriptor fanfareFile = context.getAssets().openFd("sounds/data/success1.mp3");
                )
        {
            button = pool.load(buttonFile, 1);
            correct = pool.load(correctFile, 1);
            tick = pool.load(tickFile, 1);
            tock = pool.load(tockFile, 1);
            timeUp = pool.load(timeUpFile, 1);
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
            pool.play(button, 0.75f, 0.75f, 1, 0, 1.25f);
        }
        catch (NullPointerException e) {
            Log.e(TAG, "No SoundPool exists to play from.", e);
        }
    }

    static void correct() {
        try {
            pool.play(correct, 1f, 1f, 1, 0, 1.25f);
        }
        catch (NullPointerException e) {
            Log.e(TAG, "No SoundPool exists to play from.", e);
        }
    }

    static void tick() {
        try {
            pool.play(tick, 1f, 1f, 1, 0, 1.1f);
        }
        catch (NullPointerException e) {
            Log.e(TAG, "No SoundPool exists to play from.", e);
        }
    }

    static void tock() {
        try {
            pool.play(tock, 1f, 1f, 1, 0, 1f);
        }
        catch (NullPointerException e) {
            Log.e(TAG, "No SoundPool exists to play from.", e);
        }
    }

    static void timeUp() {
        try {
            pool.play(timeUp, 1f, 1f, 1, 0, 1f);
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