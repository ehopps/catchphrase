package com.example.catchphrase;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class WordList implements Parcelable {
    private static final String TAG = "WordList";

    private List<String> easy;
    private List<String> medium;
    private List<String> hard;
    private List<String> current;

    public enum Difficulty {
        EASY,
        MEDIUM,
        HARD
    }

    WordList(InputStream filestream) { // TODO: error handle filename input any more than you already are?
        easy = new ArrayList<>();
        medium = new ArrayList<>();
        hard = new ArrayList<>();
        current = easy; // default to easy mode

        try (BufferedReader br = new BufferedReader(new InputStreamReader(filestream))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split("\t");
                switch(tokens[1]) {
                    case "e": {
                        easy.add(tokens[0]);
                        break;
                    }
                    case "m": {
                        medium.add(tokens[0]);
                        break;
                    }
                    case "h": {
                        hard.add(tokens[0]);
                        break;
                    }
                }
            }
        }
        catch (IOException e) {
            Log.e(TAG, "IOException: Couldn't construct WordList from given input stream.");
            // TODO: handle this further: game can't proceed if this happens
        }
    }

    private WordList(Parcel in) {
        // get sizes of word lists
        int easySize = in.readInt();
        int mediumSize = in.readInt();
        int hardSize = in.readInt();

        // initialize word lists
        easy = new ArrayList<>(easySize);
        medium = new ArrayList<>(mediumSize);
        hard = new ArrayList<>(hardSize);

        // fill word lists
        in.readStringList(easy);
        in.readStringList(medium);
        in.readStringList(hard);
    }

    void startGame(Difficulty difficulty) {
        switch(difficulty) {
            case EASY: {
                current = new ArrayList<>(easy);
                break;
            }
            case MEDIUM: {
                current = new ArrayList<>(medium);
                break;
            }
            case HARD: {
                current = new ArrayList<>(hard);
                break;
            }
        }

        Collections.shuffle(current);
    }

    String getWord() {
        String word;
        try {
            word = current.remove(0);
        }
        catch (IndexOutOfBoundsException e) {
            Log.e(TAG, "Ran out of words!", e);
            // TODO: handle this. what happens when we run out of words?
            word = "NO MORE WORDS";
        }

        return word;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        try {
            parcel.writeInt(easy.size());
            parcel.writeInt(medium.size());
            parcel.writeInt(hard.size());
            parcel.writeStringList(easy);
            parcel.writeStringList(medium);
            parcel.writeStringList(hard);
        }
        catch (NullPointerException e) {
            Log.d(TAG, "Tried to parcel an object with null list.", e);
        }
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public WordList createFromParcel(Parcel in) {
            return new WordList(in);
        }

        public WordList[] newArray(int size) {
            return new WordList[size];
        }
    };
}
