package com.sma2.sma2.SignalRecording;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.sma2.sma2.DataAccess.SignalDataService;

import java.io.IOException;
import java.util.Arrays;

public class MovementRecorder implements SensorEventListener {
    private final String TAG = "MovementRecorder";
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mGyroscope;
    private Sensor mMagnetometer;
    private Sensor mOrientation;
    private int mSamplingFrequency_us = SensorManager.SENSOR_DELAY_NORMAL;
    private CSVFileWriter mCSVFileWriter;
    private SignalDataService signalDataService;

    private CombinedSensorDataFrame combinedSensorDataFrame = null;
    private static boolean mEnableLogging = false;
    private final int SYNC_ACCURACY_NS = 200000; // corresponds to maximum difference of 200us in timestamps

    private String[] SENSOR_INFO_HEADER = {"Name", "Vendor", "MaxRange", "Resolution", "Sampling Distance [us]"};

    private String[] SENSOR_DATA_HEADER = {"Timestamp [ns]",
            "aX [m/s^2]", "aY [m/s^2]", "aZ [m/s^2]",
            "gX [rad/s]", "gY [rad/s]", "gZ [rad/s]",
            "mX [uT]", "mY [uT]", "mZ [uT]",
            "r0 [a.u.]", "r1 [a.u.]", "r2 [a.u.]", "r3 [a.u.]"};

    public MovementRecorder(Context context, int samplingFrequency_us, String exercisName) throws IOException {
        mSamplingFrequency_us = samplingFrequency_us;
        mSensorManager = (SensorManager) context.getSystemService(context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        mCSVFileWriter = new CSVFileWriter(exercisName);

        signalDataService = new SignalDataService(context);

        // generate header information
        mCSVFileWriter.writeData(SENSOR_INFO_HEADER);
        mCSVFileWriter.writeData(getSensorInfoStringArray(mAccelerometer));
        mCSVFileWriter.writeData(getSensorInfoStringArray(mGyroscope));
        mCSVFileWriter.writeData(getSensorInfoStringArray(mMagnetometer));
        mCSVFileWriter.writeData(getSensorInfoStringArray(mOrientation));

        mCSVFileWriter.writeData(SENSOR_DATA_HEADER);

    }

    public String getFileName() {
        return mCSVFileWriter.getFileName();
    }

    private String[] getSensorInfoStringArray(Sensor sensor) {
        String[] sensorNotAvailable = {"NaN", "NaN", "NaN", "NaN"};
        if (sensor != null) {
            return (new SensorInformation(sensor, mSamplingFrequency_us).toStringArray());
        } else {
            return sensorNotAvailable;
        }
    }

    public void setSamplingFrequency(int samplingFrequencyMicroSeconds) {
        mSamplingFrequency_us = samplingFrequencyMicroSeconds;
    }

    public void registerListeners() {
        if (mAccelerometer != null) {
            mSensorManager.registerListener(this, mAccelerometer, mSamplingFrequency_us);
        }
        if (mGyroscope != null) {
            mSensorManager.registerListener(this, mGyroscope, mSamplingFrequency_us);
        }
        if (mMagnetometer != null) {
            mSensorManager.registerListener(this, mMagnetometer, mSamplingFrequency_us);
        }
        if (mOrientation != null) {
            mSensorManager.registerListener(this, mOrientation, mSamplingFrequency_us);
        }
    }

    public void startLogging() {
        mEnableLogging = true;
    }

    public void stop() throws IOException {
        mSensorManager.unregisterListener(this);
        mEnableLogging = false;
        mCSVFileWriter.close();
        signalDataService.saveSignal(mCSVFileWriter.getSignalDA());
        mSensorManager = null;
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public static class SensorInformation {

        public String name = "";
        public String vendor = "";
        public float maxRange = 0.0f;
        public float resolution = 0.0f;
        public int samplingFrequency_us = 0;

        public SensorInformation(Sensor sensor, int samplingFrequency_us) {
            this.name = sensor.getName();
            this.maxRange = sensor.getMaximumRange();
            this.vendor = sensor.getVendor();
            this.resolution = sensor.getResolution();
            this.samplingFrequency_us = samplingFrequency_us;
        }

        @Override
        public String toString() {
            String msg = "name = " + this.name;
            msg += ", vendor = " + this.vendor;
            msg += ", maxRange = " + Float.toString(this.maxRange);
            msg += ", resolution = " + Float.toString(this.resolution);
            return msg;
        }

        public String[] toStringArray() {
            String[] str = {this.name, this.vendor, Float.toString(this.maxRange), Float.toString(this.resolution), Integer.toString(this.samplingFrequency_us)};
            return str;
        }
    }

    public static class InternalSensorDataFrame implements Comparable<InternalSensorDataFrame> {

        private double[] mSensorData;
        private long mTimeStamp;
        private int mSensorType;


        public InternalSensorDataFrame(long timestamp, double[] sensorData, int sensorType) {
            mSensorData = sensorData;
            mTimeStamp = timestamp;
            mSensorType = sensorType;
        }

        public int getSensorType() {
            return mSensorType;
        }

        public long getTimeStamp() {
            return mTimeStamp;
        }

        public double[] getSensorData() {
            return mSensorData;
        }

        @Override
        public String toString() {
            String sensorType = "";
            switch (mSensorType) {
                case Sensor.TYPE_ACCELEROMETER:
                    sensorType = "ACC";
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    sensorType = "GYRO";
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    sensorType = "MAG";
                    break;
                case Sensor.TYPE_ROTATION_VECTOR:
                    sensorType = "ROTATION";
                    break;
            }
            return "Time: " + mTimeStamp / 1000 + ", " + sensorType + ": " + Arrays.toString(mSensorData);
        }

        @Override
        public int compareTo(InternalSensorDataFrame dataFrame) {
            return (int) (this.getTimeStamp() - dataFrame.getTimeStamp());
        }
    }

    public static class CombinedSensorDataFrame {

        private double[] mAccData = null;
        private double[] mGyroData = null;
        private double[] mMagData = null;
        private double[] mRotationData = null;
        private long mTimeStamp;

        public CombinedSensorDataFrame(long timestamp) {
            mTimeStamp = timestamp;
        }

        public void addSensorData(InternalSensorDataFrame dataFrame) {
            switch (dataFrame.getSensorType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    mAccData = dataFrame.mSensorData;
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    mGyroData = dataFrame.mSensorData;
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    mMagData = dataFrame.mSensorData;
                    break;
                case Sensor.TYPE_ROTATION_VECTOR:
                    mRotationData = dataFrame.mSensorData;
                    break;
            }
        }

        public long getTimeStamp() {
            return mTimeStamp;
        }

        public double[] getAccData() {
            return mAccData;
        }

        public double[] getGyroData() {
            return mGyroData;
        }

        public double[] getMagData() {
            return mMagData;
        }

        public double[] getRotationData() {
            return mRotationData;
        }

        @Override
        public String toString() {
            String msg = "Timestamp: " + mTimeStamp;
            if (mAccData != null) {
                msg += " Ax: " + String.format("%.2f", mAccData[0]) + " Ay: " + String.format("%.2f", mAccData[1]) + " Az: " + String.format("%.2f", mAccData[2]);
            }
            if (mGyroData != null) {
                msg += " Gx: " + String.format("%.2f", mGyroData[0]) + " Gy: " + String.format("%.2f", mGyroData[1]) + " Gz: " + String.format("%.2f", mGyroData[2]);
            }
            if (mMagData != null) {
                msg += " Mx: " + String.format("%.2f", mMagData[0]) + " My: " + String.format("%.2f", mMagData[1]) + " Mz: " + String.format("%.2f", mMagData[2]);
            }
            if (mRotationData != null) {
                msg += " R0: " + String.format("%.2f", mRotationData[0]) + " R1: " + String.format("%.2f", mRotationData[1]) + " R2: " + String.format("%.2f", mRotationData[2]) + " R3: " + String.format("%.2f", mRotationData[3]);
            }
            return msg;
        }

        public String[] toStringArray() {
            String[] str = {"NaN", "NaN", "NaN", "NaN", "NaN", "NaN", "NaN", "NaN", "NaN", "NaN", "NaN", "NaN", "NaN", "NaN"};
            str[0] = Long.toString(mTimeStamp);
            if (mAccData != null) {
                str[1] = Double.toString(mAccData[0]);
                str[2] = Double.toString(mAccData[1]);
                str[3] = Double.toString(mAccData[2]);
            }
            if (mGyroData != null) {
                str[4] = Double.toString(mGyroData[0]);
                str[5] = Double.toString(mGyroData[1]);
                str[6] = Double.toString(mGyroData[2]);
            }
            if (mMagData != null) {
                str[7] = Double.toString(mMagData[0]);
                str[8] = Double.toString(mMagData[1]);
                str[9] = Double.toString(mMagData[2]);
            }
            if (mRotationData != null) {
                str[10] = Double.toString(mRotationData[0]);
                str[11] = Double.toString(mRotationData[1]);
                str[12] = Double.toString(mRotationData[2]);
                str[13] = Double.toString(mRotationData[2]);
            }
            return str;
        }
    }

    public void onSensorChanged(SensorEvent event) {
        InternalSensorDataFrame df = null;
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                df = new InternalSensorDataFrame(event.timestamp, new double[]{event.values[0], event.values[1], event.values[2]}, event.sensor.getType());
                break;
            case Sensor.TYPE_GYROSCOPE:
                df = new InternalSensorDataFrame(event.timestamp, new double[]{event.values[0], event.values[1], event.values[2]}, event.sensor.getType());
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                df = new InternalSensorDataFrame(event.timestamp, new double[]{event.values[0], event.values[1], event.values[2]}, event.sensor.getType());
                break;
            case Sensor.TYPE_ROTATION_VECTOR:
                df = new InternalSensorDataFrame(event.timestamp, new double[]{event.values[0], event.values[1], event.values[2], event.values[3]}, event.sensor.getType());
                break;
        }


        // if timestamp of current sensor frame matches timestamp of existing combined frame, just add it else create a new combined frame
        if (combinedSensorDataFrame != null && (df.getTimeStamp() / SYNC_ACCURACY_NS == combinedSensorDataFrame.getTimeStamp() / SYNC_ACCURACY_NS)) {
            combinedSensorDataFrame.addSensorData(df);
        } else {
            if (combinedSensorDataFrame != null) {
                //Log.d(TAG, combinedSensorDataFrame.toString());
                if (mEnableLogging) {
                    mCSVFileWriter.writeData(combinedSensorDataFrame.toStringArray());
                }
            }
            combinedSensorDataFrame = new CombinedSensorDataFrame(df.getTimeStamp());
            combinedSensorDataFrame.addSensorData(df);
        }
    }
}

