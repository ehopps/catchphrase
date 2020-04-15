package com.example.catchphrase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final String CONFIG = "com.example.catchphrase.CONFIG";
    public static final String WORD_LIST = "com.example.catchphrase.WORD_LIST";
    public static final String GAME_MODE = "com.example.catchphrase.GAME_MODE";

    View decorView;
    WordList words;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        decorView = getWindow().getDecorView();
        hideStatusBar();

        // initialize app settings and sounds
        GameSounds.init(this);
        Config.init();

        // load word list
        try {
            InputStream wordStream = this.getResources().openRawResource(R.raw.biblewords);
            words = new WordList(wordStream); // TODO: find a better way to do this?
        }
        catch (Resources.NotFoundException e) {
            Log.e(TAG, "Error finding word list resource.", e);
        }
    }

    private void hideStatusBar() {
        decorView.setOnSystemUiVisibilityChangeListener
                (new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                            decorView.setSystemUiVisibility(uiOptions);
                        }
                    }
                });
    }

    public void about(View view) {
        // TODO: add functionality
        Log.d(TAG, "Clicked About button.");
    }

    public void settings(View view) {
        // TODO: add functionality
        Log.d(TAG, "Clicked Settings button.");
    }

    public void startEasy(View view) {
        GameSounds.button();
        startGameActivity(WordList.Difficulty.EASY);
    }

    public void startMedium(View view) {
        GameSounds.button();
        startGameActivity(WordList.Difficulty.MEDIUM);
    }

    public void startHard(View view) {
        GameSounds.button();
        startGameActivity(WordList.Difficulty.HARD);
    }

    private void startGameActivity(WordList.Difficulty difficulty) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(WORD_LIST, words);
        intent.putExtra(GAME_MODE, difficulty);
        startActivity(intent);
    }

    public void onResume() {
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        super.onResume();
    }

    public void onDestroy() {
        GameSounds.deInit();
        super.onDestroy();
    }
}

// TODO: add support for different lists of words (sports, movies, etc.)
// TODO: fix toast styling (dark mode?)
// TODO: add Settings icon in one top corner and About icon in the other
// TODO: when About is pressed, display info including attribution of sounds, etc.
