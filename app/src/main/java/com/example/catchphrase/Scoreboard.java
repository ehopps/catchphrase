package com.example.catchphrase;

class Scoreboard {
    private ScoreboardListener listener;

    interface ScoreboardListener {
        void onScoreChange1(int score);
        void onScoreChange2(int score);
        void onWin1();
        void onWin2();
    }

    private int team1;
    private int team2;
    private int activeTeam;
    private int pointsToWin;

    Scoreboard(ScoreboardListener listener, int pointsToWin) {
        this.listener = listener;
        this.pointsToWin = pointsToWin;
        reset();
    }

    void addPoint() {
        switch(activeTeam) {
            case 1: {
                addPoint1();
                break;
            }
            case 2: {
                addPoint2();
                break;
            }
        }
    }

    void endRound() {
        switch(activeTeam) {
            case 1: {
                activeTeam = 2;
                break;
            }
            case 2: {
                activeTeam = 1;
                break;
            }
        }
    }

    void addPoint1() {
        // add point to scoreboard
        team1++;
        listener.onScoreChange1(team1);

        // check if team has won
        if (team1 >= pointsToWin) {
            listener.onWin1();
        }
    }

    void addPoint2() {
        // add point to scoreboard
        team2++;
        listener.onScoreChange2(team2);

        // check if team has won
        if (team2 >= pointsToWin) {
            listener.onWin2();
        }
    }

    void reset() {
        activeTeam = 1;
        team1 = 0;
        team2 = 0;

        if (listener != null) {
            listener.onScoreChange1(team1);
            listener.onScoreChange2(team2);
        }
    }

    int getActiveTeam() {
        return activeTeam;
    }

    // TODO: add setters for: winning threshold, team1++, team2++, reset score
    // TODO: create a scoreboard interface and send message to GameActivity when a team wins
    // TODO: track which team's turn it is
}
