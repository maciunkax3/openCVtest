package com.example.maciej.opencvtest;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.support.annotation.RequiresPermission;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
//        NfcManager manager = (NfcManager) this.getSystemService(Context.NFC_SERVICE);
//
//        NfcAdapter adapter = manager.getDefaultAdapter();
//        if (adapter != null && adapter.isEnabled()) {
//            // adapter exists and is enabled.
//        }
        if(nfcService.isNfcAvalaible())
            findViewById(R.id.nfcNotEnabledTextView).setVisibility(View.INVISIBLE);
        initializeSpinners();
    }

    private void initializeSpinners(){
        Spinner trickModeSpinner = (Spinner)findViewById(R.id.trickModeSpinner);
        trickModeSpinner.setAdapter(new TrickModeAdapter(this, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.trickModes)));
        trickModeSpinner.setOnItemSelectedListener(new TrickModeListener());
        //Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner readingTagModeSpinner = (Spinner)findViewById(R.id.readingTagModeSpinner);
        readingTagModeSpinner.setAdapter(new ReadingTagModeAdapter(this, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.readingTagModes)));
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

    public void saveSetting(View v){
        //TO DO
    }

    public void displayReadingTagModeHelp(View v){

    }

}
