package com.example.maciej.opencvtest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import static com.example.maciej.opencvtest.R.raw.lbpcascade_frontalcatface;

public class MainActivity extends AppCompatActivity {


    private static final int REQUEST_IMAGE = 100;
    String pathPic="/mnt/sdcard/abc0.jpg";
    int counter;
    File dest;
    ImageView imV;
    private CascadeClassifier cascadeClassifier;
    private Mat grayscaleImage;
    private int absoluteFaceSize;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button bt=((Button)findViewById(R.id.button));
        Button bt2=((Button)findViewById(R.id.button2));
        Button bt3=((Button)findViewById(R.id.button3));
        imV=((ImageView)findViewById(R.id.imageView2));
        counter=0;
        if(!OpenCVLoader.initDebug()){
            ((TextView)findViewById(R.id.sample_text)).setText("Nie udało sie uruchomic OpenCV");
        }
        else{
            //initializeOpenCVDependencies();
            ((TextView)findViewById(R.id.sample_text)).setText("OK");
        }
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePic();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markFace(1);
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markFace(2);
            }
        });
        updateImage(new File(pathPic));

    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    void takePic(){
        String name = "abc"+Integer.toBinaryString(counter++);
        dest=new File(Environment.getExternalStorageDirectory(), name+".jpg");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(dest));
        pathPic=dest.getPath();
        startActivityForResult(intent, REQUEST_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( requestCode == REQUEST_IMAGE && resultCode == Activity.RESULT_OK ){
            updateImage(new File(pathPic));
        }
    }
    void markFace(int type){
        ((TextView)findViewById(R.id.sample_text)).setText("Start");
        disableButtons();
        if(grayscaleImage==null)grayscaleImage=new Mat();
        String name = Environment.getExternalStorageDirectory().toString();
        File destt=new File(Environment.getExternalStorageDirectory().toString());
        File file=new File(destt, "cba.jpg");
        Mat pictureToMark= Imgcodecs.imread(pathPic);
        Mat marked;
        if(type == 1) {
            marked = picToMark(pictureToMark);
        }
        else {
            marked = markCard(pictureToMark);
        }
        String nazwa=file.toString();
        Imgcodecs.imwrite(nazwa, marked);
        updateImage(new File(nazwa));
        enableButtons();
        ((TextView)findViewById(R.id.sample_text)).setText("Stop");
    }



    void updateImage(File a){
        imV.setImageURI(Uri.fromFile(a));
    }

    public void disableButtons(){
        Button bt=((Button)findViewById(R.id.button));
        Button bt2=((Button)findViewById(R.id.button2));
        Button bt3=((Button)findViewById(R.id.button3));
        bt.setEnabled(false);
        bt2.setEnabled(false);
        bt3.setEnabled(false);
    }
    public void enableButtons(){
        Button bt=((Button)findViewById(R.id.button));
        Button bt2=((Button)findViewById(R.id.button2));
        Button bt3=((Button)findViewById(R.id.button3));
        bt.setEnabled(true);
        bt2.setEnabled(true);
        bt3.setEnabled(true);
    }

    public Mat markCard(Mat aInputFrame){
        Imgproc.cvtColor(aInputFrame, grayscaleImage, Imgproc.COLOR_RGBA2RGB);
        Imgproc.resize(aInputFrame, aInputFrame, new Size(1900, 1200));
        ObjectReconizer cR=new ObjectReconizer(this, R.raw.cascade_card_back,"cascade_card_back.xml");
        MatOfRect cards=cR.getObjects(aInputFrame);
        Rect[] cardsArray = cards.toArray();
        if(cardsArray.length > 0){
            for(int i=0;i<cardsArray.length;i++){
                Rect card=cardsArray[i];
                Imgproc.rectangle(aInputFrame, card.tl(), card.br(), new Scalar(0, 128, 0, 128), 5);
            }
        }
        return aInputFrame;
    }

    public Mat picToMark(Mat aInputFrame){
        Imgproc.cvtColor(aInputFrame, grayscaleImage, Imgproc.COLOR_RGBA2RGB);
        ObjectReconizer fR=new ObjectReconizer(this, R.raw.lbpcascade_frontalface,"lbpcascade_frontalface.xml");
        MatOfRect faces=fR.getObjects(aInputFrame);
        Rect[] facesArray = faces.toArray();
        if(facesArray.length>0) {
            ObjectReconizer eR=new ObjectReconizer(this, R.raw.haarcascade_lefteye_2splits, "haarcascade_lefteye_2splits.xml");
            for (int i = 0; i < facesArray.length; i++) {
                Rect face=facesArray[i];
                Rect abcd =new Rect(face.x, face.y, face.width, face.height);
                Mat roi_color= new Mat(aInputFrame, abcd);
                MatOfRect eyes=eR.getObjects(roi_color);
                Rect[] eyesArray=eyes.toArray();
                Imgproc.rectangle(aInputFrame, face.tl(), face.br(), new Scalar(0, 255, 0, 255), 3);
                for (Rect eye : eyesArray){
                    Imgproc.rectangle(roi_color, eye.tl(), eye.br(), new Scalar(255, 255, 0, 255), 3);
                }
                if(eyesArray.length==2){
                    int x=(eyesArray[0].x<eyesArray[1].x) ? eyesArray[0].x:eyesArray[1].x;
                    int x2=(eyesArray[0].x>eyesArray[1].x) ? eyesArray[0].x+eyesArray[0].width:eyesArray[1].x+eyesArray[1].width;
                    int y = eyesArray[0].y;
                    int w=x2-x;
                    int h=w/2;
                    Imgproc.rectangle(roi_color, new Point(x, y-h), new Point(x+w, y), new Scalar(0, 0, 255, 255), 3);
                }
            }
        }
        return aInputFrame;
    }
}
