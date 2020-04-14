package com.example.catchphrase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class GameActivity extends AppCompatActivity implements Scoreboard.ScoreboardListener {
    private static final String TAG = "GameActivity";

    WordList words;
    WordList.Difficulty difficulty;
    CountDownTimer timer;
    Scoreboard scoreboard;
    Config config;

    TextView wordView;
    TextView timerView;
    TextView scoreView1;
    TextView scoreView2;
    TextView teamView1;
    TextView teamView2;
    Button startButton;
    Button correctButton;
    Button endButton;
    Button skipButton;
    Button newButton;
    Button exitButton;

    int textColor;
    int accentColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // find all views
        wordView = findViewById(R.id.text_word);
        timerView = findViewById(R.id.text_timer);
        scoreView1 = findViewById(R.id.text_score1);
        scoreView2 = findViewById(R.id.text_score2);
        teamView1 = findViewById(R.id.text_team1);
        teamView2 = findViewById(R.id.text_team2);
        startButton = findViewById(R.id.button_start);
        correctButton = findViewById(R.id.button_correct);
        endButton = findViewById(R.id.button_end);
        skipButton = findViewById(R.id.button_skip);
        newButton = findViewById(R.id.button_new);
        exitButton = findViewById(R.id.button_exit);

        // retrieve resources
        if (android.os.Build.VERSION.SDK_INT < 23) {
            textColor = getResources().getColor(R.color.colorText);
            accentColor = getResources().getColor(R.color.colorAccent);
        }
        else {
            textColor = getResources().getColor(R.color.colorText, null);
            accentColor = getResources().getColor(R.color.colorAccent, null);
        }

        // get intent
        Intent intent = getIntent();

        // get config from intent
        config = intent.getParcelableExtra(MainActivity.CONFIG);

        // get word list from intent and set it up
        words = intent.getParcelableExtra(MainActivity.WORD_LIST);
        difficulty = (WordList.Difficulty) intent.getSerializableExtra(MainActivity.GAME_MODE);
        words.startGame(difficulty);

        // set up scoreboard
        scoreboard = new Scoreboard(this, config.getPointsToWin());

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
                // hide correct and skip buttons; show start button
                showStartScreen();

                // end round in the scoreboard
                scoreboard.endRound();
            }
        };
    }

    public void buttonStart(View view) {
        String word = words.getWord();
        wordView.setText(word);

        // hide start button; show timer, correct button, and skip button
        showGameScreen();

        // highlight active team
        highlightActiveTeam(scoreboard.getActiveTeam());

        // reset and start timer
        timer.start();
    }

    public void buttonCorrect(View view) {
        scoreboard.addPoint();
        String word = words.getWord();
        wordView.setText(word);
    }

    public void buttonEnd(View view) { // TODO: make the end button visibly different (red maybe?)
        onEnd();
    }

    public void buttonSkip(View view) {
        String word = words.getWord();
        wordView.setText(word);
    }

    public void buttonNew(View view) {
        scoreboard.reset();
        words.startGame(difficulty);
        showStartScreen();
    }

    public void buttonExit(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    void showStartScreen() {
        timerView.setVisibility(View.INVISIBLE);
        wordView.setVisibility(View.INVISIBLE);
        correctButton.setVisibility(View.INVISIBLE);
        endButton.setVisibility(View.VISIBLE);
        skipButton.setVisibility(View.INVISIBLE);
        startButton.setVisibility(View.VISIBLE);
        newButton.setVisibility(View.INVISIBLE);
        exitButton.setVisibility(View.INVISIBLE);
    }

    void showGameScreen() {
        timerView.setVisibility(View.VISIBLE);
        wordView.setVisibility(View.VISIBLE);
        correctButton.setVisibility(View.VISIBLE);
        endButton.setVisibility(View.VISIBLE);
        skipButton.setVisibility(View.VISIBLE);
        startButton.setVisibility(View.INVISIBLE);
        newButton.setVisibility(View.INVISIBLE);
        exitButton.setVisibility(View.INVISIBLE);
    }

    void showWinScreen() {
        timerView.setVisibility(View.INVISIBLE);
        wordView.setVisibility(View.INVISIBLE);
        correctButton.setVisibility(View.GONE);
        endButton.setVisibility(View.GONE);
        skipButton.setVisibility(View.GONE);
        startButton.setVisibility(View.GONE);
        newButton.setVisibility(View.VISIBLE);
        exitButton.setVisibility(View.VISIBLE);
    }

    void onEnd() {
        // TODO: show something celebratory
        // TODO: think through this (and the listener callbacks) more in general
        timer.cancel();
        showWinScreen();
    }

    void highlightActiveTeam(int activeTeam) {
        switch(activeTeam) {
            case 1: { // TODO: adapt this to whatever theme you end up using
                scoreView1.setTextColor(accentColor);
                teamView1.setTextColor(accentColor);
                scoreView2.setTextColor(textColor);
                teamView2.setTextColor(textColor);
                break;
            }
            case 2: {
                scoreView2.setTextColor(accentColor);
                teamView2.setTextColor(accentColor);
                scoreView1.setTextColor(textColor);
                teamView1.setTextColor(textColor);
                break;
            }
        }
    }

    @Override
    public void onScoreChange1(int score) {
        scoreView1.setText(String.format(Locale.getDefault(), "%d", score));
    }

    @Override
    public void onScoreChange2(int score) {
        scoreView2.setText(String.format(Locale.getDefault(), "%d", score));
    }

    @Override
    public void onWin1() {
        onEnd();
    }

    @Override
    public void onWin2() {
        onEnd();
    }

    // TODO: when Scoreboard says a team wins, maybe celebrate then exit to MainActivity
    // TODO: on win, ask to start new game or exit to main
}
