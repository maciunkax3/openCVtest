package com.example.maciej.opencvtest;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by Trollo on 2017-11-02.
 */

public class TrickModeAdapter extends ArrayAdapter {
    public TrickModeAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull Object[] objects) {
        super(context, resource, objects);
    }
}
