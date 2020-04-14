package com.example.catchphrase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import java.io.InputStream;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final String CONFIG = "com.example.catchphrase.CONFIG";
    public static final String WORD_LIST = "com.example.catchphrase.WORD_LIST";
    public static final String GAME_MODE = "com.example.catchphrase.GAME_MODE";

    WordList words;
    Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        config = new Config();

        try {
            InputStream wordStream = this.getResources().openRawResource(R.raw.biblewords);
            words = new WordList(wordStream); // TODO: find a better way to do this?
        }
        catch (Resources.NotFoundException e) {
            Log.e(TAG, "Error finding word list resource.", e);
        }
    }

    public void startEasy(View view) {
        Log.d(TAG, "Starting in easy mode.");
        startGameActivity(WordList.Difficulty.EASY);
    }

    public void startMedium(View view) {
        Log.d(TAG, "Starting in medium mode.");
        startGameActivity(WordList.Difficulty.MEDIUM);
    }

    public void startHard(View view) {
        Log.d(TAG, "Starting in hard mode.");
        startGameActivity(WordList.Difficulty.HARD);
    }

    private void startGameActivity(WordList.Difficulty difficulty) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(CONFIG, config);
        intent.putExtra(WORD_LIST, words);
        intent.putExtra(GAME_MODE, difficulty);
        startActivity(intent);
    }
}

// TODO: add support for different categories