package com.example.maciej.opencvtest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by maciej on 30.05.17.
 */

public class ObjectReconizer {
    private Context context;
    private CascadeClassifier cascadeClassifier;
    private Mat grayscaleImage;
    private int cascade;
    private String name;
    private int absoluteFaceSize;

    ObjectReconizer(Context current, int casc, String name, Mat grayscaleImg){
        this.context = current;
        this.cascade = casc;
        this.name = name;
        this.grayscaleImage = grayscaleImg;
        initializeOpenCVDependencies(); ;
    }

    private void initializeOpenCVDependencies() {
        try {
            // Copy the resource into a temp file so OpenCV can load it
            InputStream is = context.getResources().openRawResource(cascade);
            File cascadeDir = context.getDir("cascade", Context.MODE_PRIVATE);
            File mCascadeFile = new File(cascadeDir, name);
            FileOutputStream os = new FileOutputStream(mCascadeFile);
            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();
// Load the cascade classifier
            cascadeClassifier = new CascadeClassifier(mCascadeFile.getAbsolutePath());
            cascadeClassifier.load(mCascadeFile.getAbsolutePath());
            if (cascadeClassifier.empty()) {
                cascadeClassifier = null;
            }
        } catch (Exception e) {

            Log.e("OpenCVActivity", "Error loading cascade", e);
        }
    }
    public MatOfRect getObjects(){
        MatOfRect objects = new MatOfRect();
        if (cascadeClassifier != null) {
            cascadeClassifier.detectMultiScale(grayscaleImage, objects, 1.3, 2, 2,
                    new Size(absoluteFaceSize, absoluteFaceSize), new Size());
        }
        return objects;
    }

    public void setGrayScaleImage(Mat gray){
        grayscaleImage=gray;
    }
}
