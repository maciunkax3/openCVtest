package com.example.maciej.opencvtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class VibrationModeActivity extends AppCompatActivity {

    NfcService nfcService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vibration_mode);
        initializeLayout();
        initializeServices();
    }

    @Override
    protected void onPause() {
        super.onPause();
        nfcService.disableForegroundDispatchSystem();
    }

    @Override
    protected void onResume() {
        super.onResume();
        nfcService.enableForegroundDispatchSystem();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        nfcService.setNfcTag(intent);
        if(SettingsSingleton.getInstance().isReadingTagModeAutomatic()){
            readFromNfcTag(null);
        }
    }

    private void initializeServices() {
        nfcService = new NfcService(this);
    }

    private void initializeLayout() {
        Button readButton = (Button) findViewById(R.id.vibrationModeReadButton);
        if(SettingsSingleton.getInstance().isReadingTagModeAutomatic()){
            readButton.setVisibility(View.INVISIBLE);
        }
        else{
            readButton.setVisibility(View.VISIBLE);
        }
    }

    public void readFromNfcTag(View v){
        Toast.makeText(this, nfcService.readFromNfcTag(), Toast.LENGTH_SHORT).show();
    }
}
