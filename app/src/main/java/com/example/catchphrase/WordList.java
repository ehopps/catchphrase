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

class WordList {
    private static final String TAG = "WordList";

    static private WordList instance;

    static private List<String> easy;
    static private List<String> medium;
    static private List<String> hard;
    static private List<String> adventist;
    static private List<String> current;
    static private List<String> current_used;

    public enum Difficulty {
        EASY,
        MEDIUM,
        HARD,
        ADVENTIST
    }

    private WordList(InputStream filestream) { // TODO: error handle filename input any more than you already are?
        easy = new ArrayList<>();
        medium = new ArrayList<>();
        hard = new ArrayList<>();
        adventist = new ArrayList<>();
        current = easy; // default to easy mode

        try (BufferedReader br = new BufferedReader(new InputStreamReader(filestream))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
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
                    case "a": {
                        adventist.add(tokens[0]);
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

    static void init(InputStream filestream) {
        if (instance == null) instance = new WordList(filestream);
    }

    static void startGame(Difficulty difficulty) {
        switch(difficulty) {
            case EASY: {
                current = new ArrayList<>(easy);
                current_used = new ArrayList<>(easy.size());
                break;
            }
            case MEDIUM: {
                current = new ArrayList<>(medium);
                current_used = new ArrayList<>(medium.size());
                break;
            }
            case HARD: {
                current = new ArrayList<>(hard);
                current_used = new ArrayList<>(hard.size());
                break;
            }
            case ADVENTIST: {
                current = new ArrayList<>(adventist);
                current_used = new ArrayList<>(adventist.size());
                break;
            }
        }

        Collections.shuffle(current);
    }

    static String getWord() {
        String word;

        // if out of words, refill and reshuffle
        if (current.size() == 0) {
            Log.d(TAG, "Ran out of words! Starting list over.");
            List<String> current_temp = current;
            current = current_used;
            current_used = current_temp;
            Collections.shuffle(current);
        }

        // pull word from list
        try {
            word = current.remove(0);
            current_used.add(word);
        }
        catch (IndexOutOfBoundsException e) {
            Log.e(TAG, "Tried to access word that was out of bounds.", e);
            word = " ";
        }

        return word;
    }
}
