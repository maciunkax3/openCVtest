package com.example.maciej.opencvtest;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

/**
 * Created by Trollo on 2017-11-02.
 */

public class ReadingTagModeAdapter extends ArrayAdapter {

    public ReadingTagModeAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull Object[] objects) {
        super(context, resource, objects);
    }
}
