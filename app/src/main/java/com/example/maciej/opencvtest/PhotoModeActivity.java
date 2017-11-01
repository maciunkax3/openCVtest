package com.example.maciej.opencvtest;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.opencv.android.OpenCVLoader;

import java.io.IOException;

public class PhotoModeActivity extends AppCompatActivity {

    private PictureService pictureService;
    private NfcService nfcService;
    private ImageView mainImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_mode);
        initializeLayout();
        initializeServices();
        //recognizerService.updateImage(new File(pathPic));
    }

    private void initializeServices() {
        pictureService = new PictureService(this);
        nfcService = new NfcService(this);
    }

    private void initializeLayout() {
        Button bt = ((Button)findViewById(R.id.button));
        Button bt2 = ((Button)findViewById(R.id.button2));
        Button bt3 = ((Button)findViewById(R.id.button3));
        mainImage = ((ImageView)findViewById(R.id.imageView2));
        if(!OpenCVLoader.initDebug()){
            ((TextView)findViewById(R.id.sample_text)).setText("Nie udało sie uruchomic OpenCV");
        }
        else{
            //initializeOpenCVDependencies();
            ((TextView)findViewById(R.id.sample_text)).setText("OpenCV załadowane");
        }
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pictureService.takePic();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    pictureService.markFace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    pictureService.markCard();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( requestCode == pictureService.REQUEST_IMAGE && resultCode == Activity.RESULT_OK ){
            pictureService.updateImage();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        nfcService.onNewIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        nfcService.enableForegroundDispatchSystem();
    }

    @Override
    protected void onPause() {
        super.onPause();
        nfcService.disableForegroundDispatchSystem();
    }

    public ImageView getMainImage(){
        return mainImage;
    }
}
