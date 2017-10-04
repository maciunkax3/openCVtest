package com.example.maciej.opencvtest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Trollo on 2017-09-10.
 */

public class PictureService {
    private Context mainContext;
    private Mat grayscaleImage;
    public File pictureFile;
    public final int REQUEST_IMAGE = 100;

    public PictureService(Context context){
        this.mainContext = context;
        this.grayscaleImage = new Mat();
    }

    public void takePic(){
        String name = mainContext.getString(R.string.app_name) + getCurrentTimeString();
        pictureFile = new File(Environment.getExternalStorageDirectory(), name+".jpg");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(pictureFile));
        //pathPic = dest.getPath();
        ((Activity)mainContext).startActivityForResult(intent, REQUEST_IMAGE);
    }

    private String getCurrentTimeString(){
        Date currentTime = Calendar.getInstance().getTime();
        return currentTime.toString();//simpleDateFormat.format(Calendar.getInstance().getTime());
    }

    public void markFace(){
        if(grayscaleImage==null)grayscaleImage=new Mat();
        //File destt=new File(Environment.getExternalStorageDirectory().toString());
        //File file=new File(destt, "cba.jpg");
        Mat pictureToMark= Imgcodecs.imread(pictureFile.getPath());
        Mat marked= picToMark(pictureToMark);
        //String nazwa=file.toString();
        //Imgcodecs.imwrite(nazwa, marked);
        String name = mainContext.getString(R.string.app_name) + getCurrentTimeString();
        pictureFile = new File(Environment.getExternalStorageDirectory(), name+".jpg");
        Imgcodecs.imwrite(pictureFile.getPath(), marked);
        updateImage();
    }

    public void markCard(){
        if(grayscaleImage==null)grayscaleImage=new Mat();
        Mat pictureToMark= Imgcodecs.imread(pictureFile.getPath());
        ObjectReconizer cR=new ObjectReconizer(mainContext, R.raw.cascade_card_back, "cascade_card_back.xml", grayscaleImage);
        MatOfRect cards=cR.getObjects(pictureToMark);
        org.opencv.core.Rect[] cardsArray = cards.toArray();
        for(org.opencv.core.Rect card : cardsArray){
            Imgproc.rectangle(pictureToMark, card.tl(), card.br(), new Scalar(0, 255, 0, 255), 3);
        }
        String name = mainContext.getString(R.string.app_name) + getCurrentTimeString();
        pictureFile = new File(Environment.getExternalStorageDirectory(), name+".jpg");
        Imgcodecs.imwrite(pictureFile.getPath(), pictureToMark);
        updateImage();
    }



    public void updateImage(){
        ((MainActivity)mainContext).getMainImage().setImageURI(Uri.fromFile(pictureFile));
    }


    public Mat picToMark(Mat aInputFrame){
        Imgproc.cvtColor(aInputFrame, grayscaleImage, Imgproc.COLOR_RGBA2RGB);
        ObjectReconizer fR=new ObjectReconizer(mainContext, R.raw.lbpcascade_frontalface, mainContext.getString(R.string.cascadeFrontalFaceXML), grayscaleImage);
        MatOfRect faces=fR.getObjects(aInputFrame);
        org.opencv.core.Rect[] facesArray = faces.toArray();
        if(facesArray.length>0) {
            ObjectReconizer eR=new ObjectReconizer(mainContext, R.raw.haarcascade_lefteye_2splits, mainContext.getString(R.string.cascadeLeftEyeXML), grayscaleImage);
            for (int i = 0; i < facesArray.length; i++) {
                org.opencv.core.Rect face=facesArray[i];
                org.opencv.core.Rect abcd =new org.opencv.core.Rect(face.x, face.y, face.width, face.height);
                Mat roi_color= new Mat(aInputFrame, abcd);
                MatOfRect eyes=eR.getObjects(roi_color);
                org.opencv.core.Rect[] eyesArray=eyes.toArray();
                Imgproc.rectangle(aInputFrame, face.tl(), face.br(), new Scalar(0, 255, 0, 255), 3);
                for (org.opencv.core.Rect eye : eyesArray){
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
