package com.sma2.sma2.SignalRecording;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.Arrays;

public class MovementRecorder implements SensorEventListener {
    private final String TAG = "MovementRecorder";
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mGyroscope;
    private Sensor mMagnetometer;
    private Sensor mOrientation;
    private int mSamplingFrequencyMicroSeconds = SensorManager.SENSOR_DELAY_NORMAL;
    private CSVFileWriter mCSVFileWriter;

    private CombinedSensorDataFrame combinedSensorDataFrame = null;
    private static boolean mEnableLogging = false;
    private final int SYNC_ACCURACY_NS = 100000; // corresponds to maximum difference of 100us in timestamps

    public MovementRecorder(Context context, int samplingfrequency_us) {
        mSamplingFrequencyMicroSeconds = samplingfrequency_us;
        mSensorManager = (SensorManager) context.getSystemService(context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        mCSVFileWriter = new CSVFileWriter();
        mCSVFileWriter.addHeaderString("Sampling Distance [us] = " + samplingfrequency_us + "\r\n");
        if (mAccelerometer != null) {
            mCSVFileWriter.addHeaderString(new SensorInformation(mAccelerometer).toString());
        }
        if (mGyroscope != null) {
            mCSVFileWriter.addHeaderString(new SensorInformation(mGyroscope).toString());
        }
        if (mMagnetometer != null) {
            mCSVFileWriter.addHeaderString(new SensorInformation(mMagnetometer).toString());
        }
        if (mOrientation != null) {
            mCSVFileWriter.addHeaderString(new SensorInformation(mOrientation).toString());
        }
        mCSVFileWriter.writeHeader();

    }

    public void setSamplingFrequency(int samplingFrequencyMicroSeconds) {
        mSamplingFrequencyMicroSeconds = samplingFrequencyMicroSeconds;
    }

    public void registerListeners() {
        if (mAccelerometer != null) {
            mSensorManager.registerListener(this, mAccelerometer, mSamplingFrequencyMicroSeconds);
        }
        if (mGyroscope != null) {
            mSensorManager.registerListener(this, mGyroscope, mSamplingFrequencyMicroSeconds);
        }
        if (mMagnetometer != null) {
            mSensorManager.registerListener(this, mMagnetometer, mSamplingFrequencyMicroSeconds);
        }
        if (mOrientation != null) {
            mSensorManager.registerListener(this, mOrientation, mSamplingFrequencyMicroSeconds);
        }
    }

    public void startLogging() {
        mEnableLogging = true;
    }

    public void stop() {
        mSensorManager.unregisterListener(this);
        mEnableLogging = false;
        mCSVFileWriter.close();
        mSensorManager = null;
        Log.d(TAG, "stop()");
    }


    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public static class SensorInformation {

        public String name = "";
        public String vendor = "";
        public float maxRange = 0.0f;
        public float resolution = 0.0f;

        public SensorInformation(Sensor sensor) {
            this.name = sensor.getName();
            this.maxRange = sensor.getMaximumRange();
            this.vendor = sensor.getVendor();
            this.resolution = sensor.getResolution();
        }

        @Override
        public String toString() {
            String msg = "name = " + this.name;
            msg += ", vendor = " + this.vendor;
            msg += ", maxRange = " + Float.toString(this.maxRange);
            msg += ", resolution = " + Float.toString(this.resolution);
            return msg + "\r\n";
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
        private final String DELIMITER = ";";
        private final String NEW_LINE = "\r\n";

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

        public String toCSVString() {
            String msg = Long.toString(mTimeStamp);
            if (mAccData != null) {
                msg += DELIMITER + Double.toString(mAccData[0]) + DELIMITER + Double.toString(mAccData[1]) + DELIMITER + Double.toString(mAccData[2]);
            } else {
                msg += DELIMITER + "NaN" + DELIMITER + "NaN" + DELIMITER + "NaN";
            }
            if (mGyroData != null) {
                msg += DELIMITER + Double.toString(mGyroData[0]) + DELIMITER + Double.toString(mGyroData[1]) + DELIMITER + Double.toString(mGyroData[2]);
            } else {
                msg += DELIMITER + "NaN" + DELIMITER + "NaN" + DELIMITER + "NaN";
            }
            if (mMagData != null) {
                msg += DELIMITER + Double.toString(mMagData[0]) + DELIMITER + Double.toString(mMagData[1]) + DELIMITER + Double.toString(mMagData[2]);
            } else {
                msg += DELIMITER + "NaN" + DELIMITER + "NaN" + DELIMITER + "NaN";
            }
            if (mRotationData != null) {
                msg += DELIMITER + Double.toString(mRotationData[0]) + DELIMITER + Double.toString(mRotationData[1]) + DELIMITER + Double.toString(mRotationData[2]) + DELIMITER + Double.toString(mRotationData[3]);
            } else {
                msg += DELIMITER + "NaN" + DELIMITER + "NaN" + DELIMITER + "NaN" + DELIMITER + "NaN";
            }
            return msg + NEW_LINE;
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
                    mCSVFileWriter.writeDataFrame(combinedSensorDataFrame);
                }
            }
            combinedSensorDataFrame = new CombinedSensorDataFrame(df.getTimeStamp());
            combinedSensorDataFrame.addSensorData(df);
        }

    }


}

