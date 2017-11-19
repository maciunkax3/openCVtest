package com.example.maciej.opencvtest;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Vibrator;
import android.support.annotation.RequiresPermission;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    NfcService nfcService;
    Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeServices();
        initializeLayout();
    }

    private void initializeServices() {
        nfcService = new NfcService(this);
        resources = getResources();
    }

    private void initializeLayout() {
        if(nfcService.isNfcAvalaible())
            findViewById(R.id.nfcNotEnabledTextView).setVisibility(View.INVISIBLE);
        initializeSpinners();
        initializeExpandableListView();
        initializeEditTexts();
    }

    private void initializeEditTexts() {
        EditText vibrationIntervalEditText = (EditText) findViewById(R.id.vibrationIntervalEditText);
        vibrationIntervalEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                SettingsSingleton.getInstance().setVibrationInterval(Long.valueOf(editable.toString()));
            }
        });
        EditText noVibrationIntervalEditText = (EditText) findViewById(R.id.noVibrationIntervalEditText);
        noVibrationIntervalEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                SettingsSingleton.getInstance().setNoVibrationInterval(Long.valueOf(editable.toString()));
            }
        });
        EditText endOfBitInterval = (EditText) findViewById(R.id.endOfBitIntervalEditText);
        endOfBitInterval.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                SettingsSingleton.getInstance().setEndOfBitInterval(Long.valueOf(editable.toString()));
            }
        });
    }

    private void initializeExpandableListView() {
        ExpandableListView cardSettings = (ExpandableListView) findViewById(R.id.cardsSettings);
        cardSettings.setAdapter(new CardsSettingsAdapter());
    }

    private void initializeSpinners(){
        Spinner trickModeSpinner = (Spinner)findViewById(R.id.trickModeSpinner);
        TrickModeAdapter trickModeAdapter = new TrickModeAdapter(this, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.trickModes));
        trickModeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        trickModeSpinner.setAdapter(trickModeAdapter);
        trickModeSpinner.setOnItemSelectedListener(new TrickModeListener());

        Spinner readingTagModeSpinner = (Spinner)findViewById(R.id.readingTagModeSpinner);
        ReadingTagModeAdapter readingTagModeAdapter = new ReadingTagModeAdapter(this, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.readingTagModes));
        readingTagModeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        readingTagModeSpinner.setAdapter(readingTagModeAdapter);
        readingTagModeSpinner.setOnItemSelectedListener(new ReadingTagModeListener());
    }

    public void startTrick(View v){
        switch(SettingsSingleton.getInstance().getTrickMode()){
            case "Vibration":
                this.startActivity(new Intent(this, VibrationModeActivity.class));
                break;
            case "Photo":
                this.startActivity(new Intent(this, PhotoModeActivity.class));
                break;
            case "Write":
                this.startActivity(new Intent(this, WriteModeActivity.class));
                break;
            default:
                Toast.makeText(this, "Invalid trick mode selected", Toast.LENGTH_SHORT).show();
        }
    }

    public void saveSetting(View v) throws JSONException, IOException {
        ExpandableListView cardSettings = (ExpandableListView) findViewById(R.id.cardsSettings);
        HashMap<CardSettingsHeader, List<CardSettingsItem>> items = ((CardsSettingsAdapter) cardSettings.getExpandableListAdapter()).getItems();
        try{
            SettingsSingleton.getInstance().setCardSettingsItems(items);
            SettingsSingleton.getInstance().saveSettings();
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();

        }
        catch(JSONException e){
            Toast.makeText(this, "JSON error", Toast.LENGTH_SHORT).show();
        }
        catch(IOException e){
            Toast.makeText(this, "Can't open data file", Toast.LENGTH_SHORT).show();
        }

    }

    public void displayReadingTagModeHelp(View v){

    }

}
