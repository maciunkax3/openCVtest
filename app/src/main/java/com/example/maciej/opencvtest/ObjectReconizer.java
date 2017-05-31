package com.example.maciej.opencvtest;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Size;
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
    int cascadeO;
    String name;
    private Mat grayscaleImage;
    private int absoluteFaceSize;

    ObjectReconizer(Context current, int cascade, String name){
        context=current;
        this.cascadeO=cascade;
        this.name=name;
        initializeOpenCVDependencies(); ;
    }

    private void initializeOpenCVDependencies() {
        try {
            // Copy the resource into a temp file so OpenCV can load it
            InputStream is = context.getResources().openRawResource(cascadeO);
            File cascadeDir = context.getDir("cascade", Context.MODE_PRIVATE);
            File mCascadeFile = new File(cascadeDir, name);
            FileOutputStream os = new FileOutputStream(mCascadeFile);
            grayscaleImage=new Mat();
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
    public MatOfRect getObjects(Mat aInputFrame){
        Imgproc.cvtColor(aInputFrame, grayscaleImage, Imgproc.COLOR_RGBA2RGB);
        MatOfRect objects = new MatOfRect();
        if (cascadeClassifier != null) {
            cascadeClassifier.detectMultiScale(grayscaleImage, objects, 1.3, 2, 2,
                    new Size(absoluteFaceSize, absoluteFaceSize), new Size());
        }
        return objects;
    }
}
