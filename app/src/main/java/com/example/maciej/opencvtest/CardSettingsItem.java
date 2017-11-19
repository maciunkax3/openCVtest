package com.example.maciej.opencvtest;

import android.media.Image;
import android.widget.ImageView;

/**
 * Created by Trollo on 2017-11-10.
 */

public class CardSettingsItem {
    private int itemIconResourceId;
    private String itemName;
    private boolean[] vibrationPattern;
    private Card id;

    public CardSettingsItem(int itemIconResourceId, String itemName, boolean[] vibrationPattern, Card id){
        this.itemIconResourceId = itemIconResourceId;
        this.itemName = itemName;
        this.vibrationPattern = vibrationPattern;
        this.id = id;
    }

    public int getItemIconResourceId() {
        return itemIconResourceId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public boolean[] getVibrationPattern() {
        return vibrationPattern;
    }

    public void setVibrationPatternAtIndex(boolean value, int index) {
        this.vibrationPattern[index] = value;
    }

    public Card getId() { return id; }

}
