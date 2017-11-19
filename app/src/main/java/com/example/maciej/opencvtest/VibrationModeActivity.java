package com.example.maciej.opencvtest;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

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
        String cardName = nfcService.readFromNfcTag();
        Toast.makeText(this, cardName, Toast.LENGTH_SHORT).show();
        ((TextView)findViewById(R.id.valueTextView)).setText(cardName);
        vibrate(cardName);
    }

    private void vibrate(String cardName) {
        boolean[] vibrationPattern = SettingsSingleton.getInstance().getVibrationPatternOfCardName(cardName);
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long vibrationInterval = SettingsSingleton.getInstance().getVibrationInterval();
        long noVibrationInterval=SettingsSingleton.getInstance().getNoVibrationInterval();
        long endOfBitInterval = SettingsSingleton.getInstance().getEndOfBitInterval();
        List<Long> list = new ArrayList<>();
        list.add(endOfBitInterval);
        for(boolean val: vibrationPattern){
            if(val){//do nadania jest 1
                list.add(vibrationInterval);
            }
            else{//do nadania jest 0
                list.add(vibrationInterval);
                list.add(noVibrationInterval);
                list.add(vibrationInterval);
            }
            list.add(endOfBitInterval);
        }
        Long[] l = list.toArray(new Long[list.size()]);
        long[] pattern = new long[l.length];
        for( int i=0; i< l.length; i++){
            pattern[i] = l[i].longValue();
        }
        vibrator.vibrate(pattern, -1);
    }
}
