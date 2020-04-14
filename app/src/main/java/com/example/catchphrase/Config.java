package com.example.catchphrase;

import android.os.Parcel;
import android.os.Parcelable;

class Config implements Parcelable {
    private int timerLength; // TODO: set this up as a list of key-value pairs for easier attribute adding and parceling
    private int timerInterval;
    private int pointsToWin;

    Config() {
        timerLength = 60000;
        timerInterval = 1000;
        pointsToWin = 10;
    }

    void setTimerLength(int newLength) { // TODO: ensure all arguments are valid ints?
        timerLength = newLength;
    }

    void setPointsToWin(int newPoints) {
        pointsToWin = newPoints;
    }

    void setTimerInterval(int newInterval) {
        timerInterval = newInterval;
    }

    int getTimerLength() {
        return timerLength;
    }

    int getTimerInterval() { // TODO: change number format for shorter intervals? e.g. show milliseconds
        return timerInterval;
    }

    int getPointsToWin() {
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
