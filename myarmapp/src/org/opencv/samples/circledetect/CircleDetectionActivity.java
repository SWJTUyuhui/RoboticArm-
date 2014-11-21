package org.opencv.samples.circledetect;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.imgproc.Imgproc;
import org.opencv.samples.myarm.R;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;

public class CircleDetectionActivity extends Activity implements SensorEventListener{
	private SensorManager sensorManager;
    private static final String  TAG              = "OCVSample::Activity";
    private CameraBridgeViewBase mOpenCvCameraView;
	//Socket socket,socket1;
	OutputStream outputStream;
	String mes="";
	int i=0,h=0,flag=0,m=0,num=0;
	int s1=90,s2=90,s3=90,s4=90;
	private Button zhuaButton,fangButton,midButton,fowardButton,rightfowardButton,leftfowardButton;
	//double[] rectx=new double[num];
	//double[] recty=new double[num];
	//double[] rectz=new double[num];

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
         
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.color_blob_detection_surface_view);

       
        Thread socketServerThread = new Thread(new SocketServerThread());
		socketServerThread.start();
		sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
		// add listener. The listener will be HelloAndroid (this) class
		sensorManager.registerListener(this, 
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
		 zhuaButton = (Button) findViewById(R.id.zhua_Button);
		 fangButton = (Button) findViewById(R.id.fang_Button);
		 midButton = (Button) findViewById(R.id.mid_Button);
		 fowardButton = (Button) findViewById(R.id.foward_Button);
		 rightfowardButton = (Button) findViewById(R.id.rightfoward_Button);
		 leftfowardButton = (Button) findViewById(R.id.leftfoward_Button);
		 zhuaButton.setOnClickListener(new OnClickListener() {
	            public void onClick(View v) {
	                // Send a message using content of the edit text widget
	               // TextView view = (TextView) findViewById(R.id.edit_text_out);
	               s4=120;
	               i=1;
	            	
	                
	            }
	        });
		 fangButton.setOnClickListener(new OnClickListener() {
	            public void onClick(View v) {
	                // Send a message using content of the edit text widget
	               // TextView view = (TextView) findViewById(R.id.edit_text_out);
	               s4=50;
	               i=1;
	                
	            }
	        });
		 midButton.setOnClickListener(new OnClickListener() {
	            public void onClick(View v) {
	                // Send a message using content of the edit text widget
	               // TextView view = (TextView) findViewById(R.id.edit_text_out);
	            	
	            	 mes="x"+500+"y"+500+"w"+500+"a"+90+"b"+90+"c"+90+"g"+0;
	            	 s1=90;s2=90;s3=90;
	               if(flag==1){flag=0;}
	                if(flag==0){flag=1; }
	                i=1;
	            }
	        });
		 fowardButton.setOnClickListener(new OnClickListener() {
	            public void onClick(View v) {
	                // Send a message using content of the edit text widget
	               // TextView view = (TextView) findViewById(R.id.edit_text_out);
	               mes="x"+550+"y"+500+"w"+500;
	               i=1;	
	                
	            }
	        });
		 leftfowardButton.setOnClickListener(new OnClickListener() {
	            public void onClick(View v) {
	                // Send a message using content of the edit text widget
	               // TextView view = (TextView) findViewById(R.id.edit_text_out);
	               mes="x"+500+"y"+500+"w"+650;
	               i=1;
	                
	            }
	        });
		 rightfowardButton.setOnClickListener(new OnClickListener() {
	            public void onClick(View v) {
	                // Send a message using content of the edit text widget
	               // TextView view = (TextView) findViewById(R.id.edit_text_out);
	               mes="x"+500+"y"+500+"w"+350;
	            	
	               i=1;
	            }
	        });
    }
    

	private class SocketServerThread extends Thread {
		
		@Override
		public void run() {
			try {
				
				DatagramSocket socket = new  DatagramSocket (8000);
				InetAddress serverAddress = InetAddress.getByName("192.168.11.3"); 
				//Socket socket = new Socket("192.168.165.156", 8000);	
				
				//OutputStream outputStream = socket.getOutputStream();
			//	PrintStream printStream = new PrintStream(outputStream);
//				socket1 = new Socket("192.168.165.191", 8080);
//				OutputStream outputStream1 = socket1.getOutputStream();
//				PrintStream printStream1 = new PrintStream(outputStream1);
				
	           while(true) {
	        	   //mes="dddddedd";
	        	   byte data[] = mes.getBytes();
	        	   if(i==1){ DatagramPacket packagesend = new DatagramPacket (data , data.length , serverAddress , 8000);
	        	   socket.send(packagesend);i=0;};
	        	   }
				
				//while (true) {}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		
}

	}
    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();

    }

    public void onDestroy() {
        super.onDestroy();

    }


   
  
    
   
public void onAccuracyChanged(Sensor sensor,int accuracy){
		
	}
	
	public void onSensorChanged(SensorEvent event){
		
		// check sensor type
		if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
			
			// assign directions
			float x=event.values[0];
			float y=event.values[1];
		//	float z=event.values[2];
	
	           
	         //   m++;
	         //   if(m==10){i=1;m=0;}
	            if(y>3&&y<7){s1+=5;DisplayToast("up");}
				 if(y<-3&&y>-7){s1-=5;DisplayToast("down");}
				 if(y>7){s2+=5;DisplayToast("back");}
				 if(y<-7){s2-=5;DisplayToast("foward");}
				if(x>5){s3+=5;DisplayToast("left");}
				if(x<-5){s3-=5;DisplayToast("right");}
				 mes="a"+s1+"b"+s2+"c"+s3+"g"+s4;
				 
           
			i=1;

		}
	}
	public void DisplayToast(String str)  
    {  
        Toast toast=Toast.makeText(this, str, Toast.LENGTH_LONG);  
       // toast.setGravity(Gravity.TOP,0,220);  
        toast.show();  
    } 
}
