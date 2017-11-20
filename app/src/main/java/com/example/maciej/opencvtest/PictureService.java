package com.example.maciej.opencvtest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.IOException;
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
    private int resourceId;

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public PictureService(Context context){
        this.mainContext = context;
        this.grayscaleImage = new Mat();
        resourceId = R.raw.king_of_clubs;//default value
    }

    private File getPictureFile(){
        String name = mainContext.getString(R.string.app_name) + getCurrentTimeString();
        return new File(SettingsSingleton.getInstance().getApplicationDirectory(), name+".jpg");
    }

    public void takePic(){
        pictureFile = getPictureFile();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getPictureFile()));
        //pathPic = dest.getPath();
        ((Activity)mainContext).startActivityForResult(intent, REQUEST_IMAGE);
    }

    private String getCurrentTimeString(){
        Date currentTime = Calendar.getInstance().getTime();
        return currentTime.toString();//simpleDateFormat.format(Calendar.getInstance().getTime());
    }

    public void markFace() throws IOException {
        if(grayscaleImage==null)grayscaleImage=new Mat();
        //File destt=new File(Environment.getExternalStorageDirectory().toString());
        //File file=new File(destt, "cba.jpg");
        Mat pictureToMark= Imgcodecs.imread(pictureFile.getPath());
        Mat marked= picToMark(pictureToMark);
        //String nazwa=file.toString();
        //Imgcodecs.imwrite(nazwa, marked);
        pictureFile = getPictureFile();
        Imgcodecs.imwrite(pictureFile.getPath(), pictureToMark);
        updateImage();
    }

    public void markCard() throws IOException {
        if(grayscaleImage==null)grayscaleImage=new Mat();
        Mat pictureToMark= Imgcodecs.imread(pictureFile.getPath());
        double width=pictureToMark.size().width;
        double height=pictureToMark.size().height;
        // Imgproc.resize(pictureToMark, pictureToMark, new Size(200,360 ));
        MatOfRect cards = new MatOfRect();
        while(width>140.0){
            Imgproc.cvtColor(pictureToMark, grayscaleImage, Imgproc.COLOR_RGBA2RGB);
            ObjectReconizer cR=new ObjectReconizer(mainContext, R.raw.newcard, "newcard.xml", grayscaleImage);
            cards=cR.getObjects();
            if(cards.toArray().length!=1){
                width/=2;
                height/=2;
                Imgproc.resize(pictureToMark, pictureToMark, new Size(width,height ));
            }
            else break;
        }
        org.opencv.core.Rect[] cardsArray = cards.toArray();
        if(cardsArray.length==1){
            Mat picToMarkAfterResize = Imgcodecs.imread(pictureFile.getPath());
            double ratio= picToMarkAfterResize.size().width/width;
            Point lt=new Point(cardsArray[0].tl().x*ratio, cardsArray[0].tl().y*ratio);
            Point br=new Point(cardsArray[0].br().x*ratio, cardsArray[0].br().y*ratio);
            Mat card_img = Utils.loadResource(mainContext, resourceId, Imgcodecs.CV_LOAD_IMAGE_COLOR);
            Imgproc.resize(card_img, card_img, new Size((int)br.x - (int)lt.x, (int)br.y - (int)lt.y));
            Mat selectedArea = picToMarkAfterResize.submat((int)lt.y, (int)br.y, (int)lt.x, (int)br.x);
            card_img.copyTo(selectedArea);
            //Imgproc.rectangle(picToMarkAfterResize, new Point(cardsArray[0].tl().x*ratio, cardsArray[0].tl().y*ratio), new Point(cardsArray[0].br().x*ratio, cardsArray[0].br().y*ratio), new Scalar(0, 255, 0, 255), 3);
            pictureToMark=picToMarkAfterResize;
        }
        pictureFile = getPictureFile();
        Imgcodecs.imwrite(pictureFile.getPath(), pictureToMark);
        updateImage();
    }



    public void updateImage(){
        ((PhotoModeActivity)mainContext).getMainImage().setImageURI(Uri.fromFile(pictureFile));
    }


    public Mat picToMark(Mat aInputFrame) throws IOException {
        Imgproc.cvtColor(aInputFrame, grayscaleImage, Imgproc.COLOR_RGBA2RGB);
        ObjectReconizer fR=new ObjectReconizer(mainContext, R.raw.lbpcascade_frontalface, mainContext.getString(R.string.cascadeFrontalFaceXML), grayscaleImage);
        MatOfRect faces=fR.getObjects();
        org.opencv.core.Rect[] facesArray = faces.toArray();
        if(facesArray.length>0) {
            ObjectReconizer eR=new ObjectReconizer(mainContext, R.raw.haarcascade_lefteye_2splits, "haarcascade_lefteye_2splits.xml", grayscaleImage);
            for (int i = 0; i < facesArray.length; i++) {
                org.opencv.core.Rect face=facesArray[i];
                org.opencv.core.Rect abcd =new org.opencv.core.Rect(face.x, face.y, face.width, face.height);
                Mat roi_color= new Mat(aInputFrame, abcd);
                eR.setGrayScaleImage(roi_color);
                MatOfRect eyes=eR.getObjects();
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
                    Mat card = Utils.loadResource(mainContext, resourceId, Imgcodecs.CV_LOAD_IMAGE_COLOR);
                    org.opencv.core.Core.flip(card.t(), card, 1);
                    Imgproc.resize(card, card, new Size(w, h));
                    Mat selectedArea = roi_color.submat(0, h, x, w+x);
                    //Imgproc.rectangle(roi_color, new Point(x, y-h), new Point(x+w, y), new Scalar(0, 0, 255, 255), 3);=
                    card.copyTo(selectedArea);
                }
            }
        }
        return aInputFrame;
    }
}
