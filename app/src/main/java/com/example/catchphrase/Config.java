package com.example.catchphrase;

import android.os.Parcel;
import android.os.Parcelable;

class Config implements Parcelable { // TODO: get rid of Parcelable?
    private static Config instance;

    private static int timerLength; // TODO: set this up as a list of key-value pairs for easier attribute adding and parceling
    private static int timerInterval;
    private static int timerWarning;
    private static int pointsToWin;

    private Config() {
        timerLength = 60000;
        timerInterval = 500;
        timerWarning = 10000;
        pointsToWin = 10;
    }

    static void init() {
        if (instance == null) instance = new Config();
    }

    static void setTimerLength(int newLength) { // TODO: ensure all arguments are valid ints?
        timerLength = newLength;
    }

    static void setTimerInterval(int newInterval) {
        timerInterval = newInterval;
    }

    static void setTimerWarning(int newWarning) {
        timerWarning = newWarning;
    }

    static void setPointsToWin(int newPoints) {
        pointsToWin = newPoints;
    }

    static int getTimerLength() {
        return timerLength;
    }

    static int getTimerInterval() { // TODO: change number format for shorter intervals? e.g. show milliseconds
        return timerInterval;
    }

    static int getTimerWarning() { // TODO: change number format for shorter intervals? e.g. show milliseconds
        return timerWarning;
    }

    static int getPointsToWin() {
        return pointsToWin;
    }

    private Config(Parcel in) {
        timerLength = in.readInt();
        timerInterval = in.readInt();
        pointsToWin = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(timerLength);
        parcel.writeInt(timerInterval);
        parcel.writeInt(pointsToWin);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Config createFromParcel(Parcel in) {
            return new Config(in);
        }

        public Config[] newArray(int size) {
            return new Config[size];
        }
    };
}
