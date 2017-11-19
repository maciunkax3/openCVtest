package com.example.maciej.opencvtest;

import android.media.Image;

/**
 * Created by Trollo on 2017-11-10.
 */

public class CardSettingsHeader {
    int headerIconResourceId;
    CardSuit headerName;

    public CardSettingsHeader(int headerIconResourceId, CardSuit headerName){
        this.headerIconResourceId = headerIconResourceId;
        this.headerName = headerName;
    }

    public int getHeaderIconResourceId() {
        return headerIconResourceId;
    }

    public String getHeaderNameText() {
        return headerName.toString();
    }

}
