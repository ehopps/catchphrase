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
    Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        decorView = getWindow().getDecorView();
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
        startGameActivity(WordList.Difficulty.EASY);
    }

    public void startMedium(View view) {
        startGameActivity(WordList.Difficulty.MEDIUM);
    }

    public void startHard(View view) {
        startGameActivity(WordList.Difficulty.HARD);
    }

    private void startGameActivity(WordList.Difficulty difficulty) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(CONFIG, config);
        intent.putExtra(WORD_LIST, words);
        intent.putExtra(GAME_MODE, difficulty);
        startActivity(intent);
    }

    public void onResume() {
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        super.onResume();
    }
}



// TODO: add support for different categories of words (sports, movies, etc.)
// TODO: fix toast styling (dark mode?)
