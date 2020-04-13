package com.example.catchphrase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class GameActivity extends AppCompatActivity {
    private static final String TAG = "GameActivity";

    WordList words;
    CountDownTimer timer;
    Config config;

    TextView wordView;
    TextView timerView;
    TextView scoreView1;
    TextView scoreView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        wordView = findViewById(R.id.text_word);
        timerView = findViewById(R.id.text_timer);
        scoreView1 = findViewById(R.id.text_score1);
        scoreView2 = findViewById(R.id.text_score2);

        // get word list and difficulty from MainActivity
        Intent intent = getIntent();
        config = intent.getParcelableExtra(MainActivity.CONFIG);
        words = intent.getParcelableExtra(MainActivity.WORD_LIST);
        WordList.Difficulty difficulty = (WordList.Difficulty) intent.getSerializableExtra(MainActivity.GAME_MODE);
        words.startGame(difficulty);

        // set up timer
        timer = new CountDownTimer(config.getTimerLength(), config.getTimerInterval()) {
            @Override
            public void onTick(long millisUntilFinished) {
                // every tick, update the timer view
                String time = String.format(Locale.getDefault(), "%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished));
                timerView.setText(time);
            }

            @Override
            public void onFinish() {
                // TODO: end round
            }
        };

        timer.start();
    }

    public void buttonStart(View view) {
        // TODO: reset and start timer
        String word = words.getWord();
        wordView.setText(word);
    }

    public void buttonCorrect(View view) {
        // TODO: increment score of current team
        String word = words.getWord();
        wordView.setText(word);
    }

    public void buttonSkip(View view) {
        String word = words.getWord();
        wordView.setText(word);
    }

    // TODO: add start button that begins timer (maybe hide it when timer is running? change it to a pause button? put start button on top of text_word?)
    // TODO: add timer
    // TODO: highlight current team: start with team1 and toggle to team 2 after timer, repeat
    // TODO: when Scoreboard says a team wins, exit to MainActivity
}
