package org.opencv.samples.circledetect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import android.util.Log;

public class circleDetector {
    float circle[] = new float[3];
    Mat mPyrDownMat = new Mat();
    Mat mHsvMat = new Mat();
    Mat mMask = new Mat();
    Mat mDilatedMask = new Mat();
    Mat mHierarchy = new Mat();
    Mat mgrayMat = new Mat();
    Mat imgCirclesOut = new Mat();
    private Scalar mLowerBound = new Scalar(0);
    private Scalar mUpperBound = new Scalar(0);
    private Scalar mColorRadius = new Scalar(50,50,50,0);
    private Scalar hsvColor =new Scalar(144,57,30,0);//Scalar(170,240,9,0);
    // Minimum contour area in percent for contours filtering
    private static double mMinContourArea = 0.9;
    // Color radius for range checking in HSV color space

    private Mat mSpectrum = new Mat();
    private List<MatOfPoint> mContours = new ArrayList<MatOfPoint>();

    // Cache
    public float[] getcircle(Mat rgbaImage) {
        Imgproc.pyrDown(rgbaImage, mPyrDownMat);//Blurs an image and downsamples it.
        Imgproc.pyrDown(mPyrDownMat, mPyrDownMat);
        Imgproc.cvtColor(mPyrDownMat, mgrayMat, Imgproc.COLOR_BGR2GRAY);
        Imgproc.GaussianBlur( mgrayMat, mgrayMat, new Size(9, 9), 2, 2 );
        Imgproc.HoughCircles( mgrayMat, imgCirclesOut, Imgproc.CV_HOUGH_GRADIENT, 1, mgrayMat.rows()/8, 200, 100, 0, 0 );
        
        for (int i = 0; i < imgCirclesOut.cols(); i++)
        {
        	imgCirclesOut.get(0, i, circle); 
            }
        return circle;
    }

    public void process(Mat rgbaImage) {

		    mLowerBound.val[0] = (hsvColor.val[0] >= mColorRadius.val[0]) ? hsvColor.val[0]-mColorRadius.val[0] : 0;
	        mUpperBound.val[0] = (hsvColor.val[0]+mColorRadius.val[0] <= 179) ? hsvColor.val[0]+mColorRadius.val[0] : 179;

	        mLowerBound.val[1] = (hsvColor.val[1] >= mColorRadius.val[1]) ? hsvColor.val[1]-mColorRadius.val[1] : 0;
	        mUpperBound.val[1] = (hsvColor.val[1]+mColorRadius.val[1] <= 255) ? hsvColor.val[1]+mColorRadius.val[1] : 255;

	        mLowerBound.val[2] = (hsvColor.val[2] >= mColorRadius.val[2]) ? hsvColor.val[2]-mColorRadius.val[2] : 0;
	        mUpperBound.val[2] = (hsvColor.val[2]+mColorRadius.val[2] <= 255) ? hsvColor.val[2]+mColorRadius.val[2] : 255;

	        mLowerBound.val[3] = 0;
	        mUpperBound.val[3] = 255;
        Imgproc.cvtColor(mPyrDownMat, mHsvMat, Imgproc.COLOR_RGB2HSV_FULL);

        Core.inRange(mHsvMat, mLowerBound, mUpperBound, mMask);
      //  Imgproc.erode(mMask, mMask, new Mat());
       // Imgproc.dilate(mMask, mMask, new Mat());
        Imgproc.dilate(mMask, mDilatedMask, new Mat());
     //   Imgproc.erode(mDilatedMask, mDilatedMask, new Mat());
       
        
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();

        Imgproc.findContours(mDilatedMask, contours, mHierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
     
        // Find max contour area
        double maxArea = 0;
        Iterator<MatOfPoint> each = contours.iterator();
        while (each.hasNext()) {
            MatOfPoint wrapper = each.next();
            double area = Imgproc.contourArea(wrapper);
            if (area > maxArea)
                maxArea = area;
        }

        // Filter contours by area and resize to fit the original image size
        mContours.clear();
        each = contours.iterator();
        while (each.hasNext()) {
            MatOfPoint contour = each.next();
            if (Imgproc.contourArea(contour) > mMinContourArea*maxArea) {
                Core.multiply(contour, new Scalar(4,4), contour);
                mContours.add(contour);

            }
        }
    }
    public List<MatOfPoint> getContours() {
        return mContours;
    }

}
