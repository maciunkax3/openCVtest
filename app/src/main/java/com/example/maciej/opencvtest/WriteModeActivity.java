package com.example.maciej.opencvtest;

import android.content.Intent;
import android.support.annotation.StringRes;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class WriteModeActivity extends AppCompatActivity {

    NfcService nfcService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_mode);
        initializeServices();
        initilizeLayout();
    }

    private void initilizeLayout() {
        Button writeButton = (Button) findViewById(R.id.writeModeButton);
        if(SettingsSingleton.getInstance().isReadingTagModeAutomatic()){
            writeButton.setVisibility(View.INVISIBLE);
        }
        else{
            writeButton.setVisibility(View.VISIBLE);
        }
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
        //super.onNewIntent(intent);
        boolean result = nfcService.setNfcTag(intent);
        if(SettingsSingleton.getInstance().isReadingTagModeAutomatic()){
            writeToNfcTag(null);
        }
        if(!result) super.onNewIntent(intent);
    }

    private void initializeServices() {
        nfcService = new NfcService(this);
    }

    public void writeToNfcTag(View v){
        EditText writeModeEditText = (EditText) findViewById(R.id.writeModeEditText);
        String content = writeModeEditText.getText().toString();
        if(!content.isEmpty()){
            writeModeEditText.setError(null);
            boolean success = nfcService.writeToNfcTag(content);
            if(success){
                Toast.makeText(this, "Tag written!", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "There is no writable tag nearby!", Toast.LENGTH_SHORT).show();
            }
        }
        else {
//            Toast.makeText(this, "This field cannot be empty", Toast.LENGTH_SHORT).show();
            writeModeEditText.setError("This field cannot be empty!");
        }
    }

}
