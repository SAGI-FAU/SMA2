package com.sma2.apkinson;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.File;
import java.io.OutputStreamWriter;

public class Rotation_Vector_Reader {

    private Sensor mRot;
    private double[] linear_acceleration = new double[3];
    private String data_sensors="";
    private SensorManager mSensorManager;
    private String pathData;
    private String format;
    private  String rotx,roty,rotz;
    private File f;
    private OutputStreamWriter fout;
    private double[] gravity = {0,0,0};
    private LineGraphSeries<DataPoint> mSeries1, mSeries2, mSeries3;
    private double graph1LastXValue = 0d;

    //private Context context;

    //Use internal sensors
    public Rotation_Vector_Reader(Context context, SensorManager mSensorManager)
    {
        //super(context);
        this.mSensorManager = mSensorManager;
        mRot = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
    }




    public void onSensorChanged(SensorEvent event) {
        // In this example, alpha is calculated as t / (t + dT),
        // where t is the low-pass filter's time-constant and
        // dT is the event delivery rate.

        final double alpha = 0.8;
        Sensor sensor = event.sensor;

        if (sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            //gravity = {0, 0, 0};
            // Isolate the force of gravity with the low-pass filter.
            gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
            gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
            gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

            // Remove the gravity contribution with the high-pass filter, i.e, mean subtraction

            linear_acceleration[0] = event.values[0] - gravity[0];
            linear_acceleration[1] = event.values[1] - gravity[1];
            linear_acceleration[2] = event.values[2] - gravity[2];


            rotx = String.valueOf(linear_acceleration[0]);
            roty = String.valueOf(linear_acceleration[1]);
            rotz = String.valueOf(linear_acceleration[2]);
            Log.e("sensor accx", String.valueOf(rotx));
            //Log.e("Accelerometer", data_sensors);
        }
    }

    public  void stopRot()
    {
        mSensorManager.unregisterListener((SensorEventListener) this, mRot);
    }




}
