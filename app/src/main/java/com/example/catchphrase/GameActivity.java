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

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class GameActivity extends AppCompatActivity implements Scoreboard.ScoreboardListener {
    private static final String TAG = "GameActivity";

    View decorView;

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
    Button endRoundButton;
    Button endGameButton;
    Button skipButton;
    Button newButton;
    Button exitButton;

    int textColor;
    int primaryColor;
    int highlightColor;
    int accentColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

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

        // find all views
        wordView = findViewById(R.id.text_word);
        timerView = findViewById(R.id.text_timer);
        scoreView1 = findViewById(R.id.text_score1);
        scoreView2 = findViewById(R.id.text_score2);
        teamView1 = findViewById(R.id.text_team1);
        teamView2 = findViewById(R.id.text_team2);
        startButton = findViewById(R.id.button_start);
        correctButton = findViewById(R.id.button_correct);
        endRoundButton = findViewById(R.id.button_endround);
        endGameButton = findViewById(R.id.button_endgame);
        skipButton = findViewById(R.id.button_skip);
        newButton = findViewById(R.id.button_new);
        exitButton = findViewById(R.id.button_exit);

        // retrieve resources
        if (android.os.Build.VERSION.SDK_INT < 23) {
            textColor = getResources().getColor(R.color.colorText);
            primaryColor = getResources().getColor(R.color.colorPrimary);
            highlightColor = getResources().getColor(R.color.colorHighlight);
            accentColor = getResources().getColor(R.color.colorAccent);
        }
        else {
            textColor = getResources().getColor(R.color.colorText, null);
            primaryColor = getResources().getColor(R.color.colorPrimary, null);
            highlightColor = getResources().getColor(R.color.colorHighlight, null);
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
                showBreakScreen();

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

        // reset and start timer
        timer.start();
    }

    public void buttonCorrect(View view) {
        scoreboard.addPoint();
        String word = words.getWord();
        wordView.setText(word);
    }

    public void buttonEndRound(View view) { // TODO: need separate End Round and End Game buttons
        showBreakScreen();
        scoreboard.endRound();
        timer.cancel();
    }

    public void buttonEndGame(View view) {
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
        endRoundButton.setVisibility(View.INVISIBLE);
        endGameButton.setVisibility(View.INVISIBLE);
        skipButton.setVisibility(View.INVISIBLE);
        startButton.setVisibility(View.VISIBLE);
        newButton.setVisibility(View.INVISIBLE);
        exitButton.setVisibility(View.INVISIBLE);
        scoreView1.setVisibility(View.INVISIBLE);
        scoreView2.setVisibility(View.INVISIBLE);
        teamView1.setVisibility(View.INVISIBLE);
        teamView2.setVisibility(View.INVISIBLE);

        // unhighlight teams
        unhighlightActiveTeam();
    }

    void showGameScreen() {
        timerView.setVisibility(View.VISIBLE);
        wordView.setVisibility(View.VISIBLE);
        correctButton.setVisibility(View.VISIBLE);
        endRoundButton.setVisibility(View.VISIBLE);
        endGameButton.setVisibility(View.INVISIBLE);
        skipButton.setVisibility(View.VISIBLE);
        startButton.setVisibility(View.INVISIBLE);
        newButton.setVisibility(View.INVISIBLE);
        exitButton.setVisibility(View.INVISIBLE);
        scoreView1.setVisibility(View.VISIBLE);
        scoreView2.setVisibility(View.VISIBLE);
        teamView1.setVisibility(View.VISIBLE);
        teamView2.setVisibility(View.VISIBLE);

        // highlight active team
        highlightActiveTeam(scoreboard.getActiveTeam());
    }

    void showBreakScreen() {
        timerView.setVisibility(View.INVISIBLE);
        wordView.setVisibility(View.INVISIBLE);
        correctButton.setVisibility(View.INVISIBLE);
        endRoundButton.setVisibility(View.INVISIBLE);
        endGameButton.setVisibility(View.VISIBLE);
        skipButton.setVisibility(View.INVISIBLE);
        startButton.setVisibility(View.VISIBLE);
        newButton.setVisibility(View.INVISIBLE);
        exitButton.setVisibility(View.INVISIBLE);
        scoreView1.setVisibility(View.VISIBLE);
        scoreView2.setVisibility(View.VISIBLE);
        teamView1.setVisibility(View.VISIBLE);
        teamView2.setVisibility(View.VISIBLE);

        // unhighlight teams
        unhighlightActiveTeam();
    }

    void showWinScreen() {
        timerView.setVisibility(View.INVISIBLE);
        wordView.setVisibility(View.INVISIBLE);
        correctButton.setVisibility(View.GONE);
        endRoundButton.setVisibility(View.GONE);
        endGameButton.setVisibility(View.GONE);
        skipButton.setVisibility(View.GONE);
        startButton.setVisibility(View.GONE);
        newButton.setVisibility(View.VISIBLE);
        exitButton.setVisibility(View.VISIBLE);
        scoreView1.setVisibility(View.VISIBLE);
        scoreView2.setVisibility(View.VISIBLE);
        teamView1.setVisibility(View.VISIBLE);
        teamView2.setVisibility(View.VISIBLE);
    }

    void onEnd() {
        // TODO: think through this (and the listener callbacks) more in general
        timer.cancel();
        showWinScreen();
    }

    void highlightActiveTeam(int activeTeam) {
        switch(activeTeam) {
            case 1: {
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

    void unhighlightActiveTeam() {
        scoreView1.setTextColor(textColor);
        teamView1.setTextColor(textColor);
        scoreView2.setTextColor(textColor);
        teamView2.setTextColor(textColor);
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
    public void onWin() {
        KonfettiView konfettiSpout = findViewById(R.id.konfetti_spout);

        int winningTeam = scoreboard.getWinningTeam();
        highlightActiveTeam(winningTeam); // TODO: highlight differently here?

        double directionMin, directionMax;
        float position;

        switch(winningTeam) {
            case 2: {
                directionMin = 190.0;
                directionMax = 240.0;
                position = konfettiSpout.getWidth() + 100;
                break;
            }
            case 1:
            default: {
                directionMin = 300.0;
                directionMax = 350.0;
                position = -100f;
                break;
            }
        }

        konfettiSpout.build()
                .addColors(primaryColor, highlightColor, accentColor)
                .setDirection(directionMin, directionMax)
                .setSpeed(1f, 35f)
                .setFadeOutEnabled(true)
                .setTimeToLive(5000L)
                .addShapes(Shape.Square.INSTANCE, Shape.Circle.INSTANCE)
                .addSizes(new Size(12, 5))
                .setPosition(position, position, 450f, 480f)
                .streamFor(5000, 10L);

        onEnd();
    }

    public void onResume() {
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        super.onResume();
    }
}

// TODO: add sounds (ticking, buzzer, congratulations)