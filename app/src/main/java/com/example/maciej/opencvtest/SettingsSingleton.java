package com.example.maciej.opencvtest;

import java.util.Dictionary;

/**
 * Created by Trollo on 2017-10-31.
 */

public class SettingsSingleton {
    private static SettingsSingleton ourInstance = new SettingsSingleton();

    private String trickMode;
    private String readingTagMode;



    private int endOfBitSignalInterval;
    private int nonVibrationInterval;
    private Dictionary<Card, VibrationPattern> VibrationPatternsDictionary;
    private int vibrationInterval;


    private SettingsSingleton(){
        //load default values from file
    }

    public static SettingsSingleton getInstance(){
        return ourInstance;
    }

    public String getTrickMode(){
        return trickMode;
    }

    public void setTrickMode(String trickMode){
        this.trickMode = trickMode;
    }

    public boolean isReadingTagModeAutomatic() {
        return readingTagMode.equals("Automatic");
    }

    public void setReadingTagMode(String readingTagMode) {
        this.readingTagMode = readingTagMode;
    }

    public int getEndOfBitSignalInterval() {
        return endOfBitSignalInterval;
    }

    public void setEndOfBitSignalInterval(int endOfBitSignalInterval) {
        this.endOfBitSignalInterval = endOfBitSignalInterval;
    }

    public int getNonVibrationInterval() {
        return nonVibrationInterval;
    }

    public void setNonVibrationInterval(int nonVibrationInterval) {
        this.nonVibrationInterval = nonVibrationInterval;
    }

    public int getVibrationInterval() {
        return vibrationInterval;
    }

    public void setVibrationInterval(int vibrationInterval) {
        this.vibrationInterval = vibrationInterval;
    }

    public Dictionary<Card, VibrationPattern> getVibrationPatternsDictionary() {
        return VibrationPatternsDictionary;
    }

    public void setVibrationPatternsDictionary(Dictionary<Card, VibrationPattern> vibrationPatternsDictionary) {
        VibrationPatternsDictionary = vibrationPatternsDictionary;
    }
}
