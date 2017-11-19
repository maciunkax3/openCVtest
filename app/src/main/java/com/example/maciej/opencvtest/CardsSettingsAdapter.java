package com.example.maciej.opencvtest;

import android.content.Context;
import android.content.res.Resources;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.maciej.opencvtest.CardSuit.*;

/**
 * Created by Trollo on 2017-11-10.
 */

public class CardsSettingsAdapter extends BaseExpandableListAdapter {

    List<CardSettingsHeader> headers;
    HashMap<CardSettingsHeader, List<CardSettingsItem>> items;

    public HashMap<CardSettingsHeader, List<CardSettingsItem>> getItems() {
        return items;
    }

    public CardsSettingsAdapter(){
        headers = SettingsSingleton.getInstance().getCardSettingsHeaders();
        items = SettingsSingleton.getInstance().getCardSettingsItems();
    }

    @Override
    public int getGroupCount() {
        return headers.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return items.get(headers.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return headers.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return items.get(headers.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) { return i1; }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        CardSettingsHeader header = (CardSettingsHeader) getGroup(i);
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) Technologic.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.cards_settings_header, null);
        }
        TextView title = (TextView) view.findViewById(R.id.headerTitle);
        title.setText(header.getHeaderNameText());

        ImageView icon = (ImageView) view.findViewById(R.id.headerIcon);
        icon.setImageResource(header.getHeaderIconResourceId());
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        final CardSettingsItem item = (CardSettingsItem) getChild(i, i1);
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) Technologic.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.cards_settings_item, null);
        }
        ImageView icon = (ImageView) view.findViewById(R.id.itemIcon);
        icon.setImageResource(item.getItemIconResourceId());

        final EditText title = (EditText) view.findViewById(R.id.itemTitle);
        title.setText(item.getItemName());
        title.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                for (Map.Entry<CardSettingsHeader, List<CardSettingsItem>> entry: items.entrySet()) {
                    for(CardSettingsItem card: entry.getValue()) {
                        if(item.getId().toString().equalsIgnoreCase(card.getId().toString())){
                            card.setItemName(title.getText().toString());
                        }
                    }
                }
            }
        });

        boolean[] vibrationPattern = item.getVibrationPattern();
        View.OnClickListener vibrationPatternOnClickListener = (new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Map.Entry<CardSettingsHeader, List<CardSettingsItem>> entry: items.entrySet()) {
                    for(CardSettingsItem card: entry.getValue()) {
                        if(item.getId().toString().equalsIgnoreCase(card.getId().toString())){
                            int index = Integer.valueOf(view.getTag().toString());
                            card.setVibrationPatternAtIndex(((CheckBox)view).isChecked(), index);
                        }
                    }
                }
            }
        });

        CheckBox vibrationPattern_0 = (CheckBox) view.findViewById(R.id.vibrationPattern0);
        vibrationPattern_0.setChecked(vibrationPattern[0]);
        vibrationPattern_0.setOnClickListener(vibrationPatternOnClickListener);
        CheckBox vibrationPattern_1 = (CheckBox) view.findViewById(R.id.vibrationPattern1);
        vibrationPattern_1.setChecked(vibrationPattern[1]);
        vibrationPattern_1.setOnClickListener(vibrationPatternOnClickListener);
        CheckBox vibrationPattern_2 = (CheckBox) view.findViewById(R.id.vibrationPattern2);
        vibrationPattern_2.setChecked(vibrationPattern[2]);
        vibrationPattern_2.setOnClickListener(vibrationPatternOnClickListener);
        CheckBox vibrationPattern_3 = (CheckBox) view.findViewById(R.id.vibrationPattern3);
        vibrationPattern_3.setChecked(vibrationPattern[3]);
        vibrationPattern_3.setOnClickListener(vibrationPatternOnClickListener);
        CheckBox vibrationPattern_4 = (CheckBox) view.findViewById(R.id.vibrationPattern4);
        vibrationPattern_4.setChecked(vibrationPattern[4]);
        vibrationPattern_4.setOnClickListener(vibrationPatternOnClickListener);
        CheckBox vibrationPattern_5 = (CheckBox) view.findViewById(R.id.vibrationPattern5);
        vibrationPattern_5.setChecked(vibrationPattern[5]);
        vibrationPattern_5.setOnClickListener(vibrationPatternOnClickListener);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
