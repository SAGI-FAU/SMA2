package com.sma2.sma2.FeatureExtraction.Tapping.SpeechFeatures;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Philipp on 20.06.2016.
 */
public class rmsCalculator extends Thread {
    File file = null;
    Context context = null;
    int samplesPerValue = 0;
    String format = "";

    public rmsCalculator(File input, int samplesPerValue, Context context, String format) {
        file = input;
        this.context = context;
        this.samplesPerValue = samplesPerValue;
        this.format = format;
    }

    public void run() {
        try {
            calculation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void calculation() throws Exception {
        BufferedInputStream bufferedInputStream;
        File output;
        List<Double> listRMSvalues = new ArrayList<Double>();
        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            String path = file.getParent();
            output = new File(path + File.separator + "rmsValues" + format + ".data");
            if (output.exists()) {
                output.delete();
            }
            bufferedInputStream.skip(44); //Skip header of .wav
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        byte[] byteBuffer = new byte[samplesPerValue * 2]; //Shorts consist of 2 Bytes
        int i = 0;
        while ((i = bufferedInputStream.read(byteBuffer, 0, samplesPerValue * 2)) > 0) {
            ByteBuffer littleEndianBuffer = ByteBuffer.wrap(byteBuffer);
            littleEndianBuffer.order(ByteOrder.LITTLE_ENDIAN);
            double rmsValue = 0;
            for (int j = 0; j < i; j += 2) {
                rmsValue += Math.pow(littleEndianBuffer.getShort(), 2);
            }
            rmsValue /= (i / 2);
            rmsValue = Math.sqrt(rmsValue);
            listRMSvalues.add(rmsValue);
        }
        double arrayRMSvalues[] = new double[listRMSvalues.size()];
        for (int x = 0; x < arrayRMSvalues.length; x++) {
            arrayRMSvalues[x] = listRMSvalues.get(x);
        }
        RandomAccessFile randomAccessFile = new RandomAccessFile(output, "rw");
        FileChannel fileChannel = randomAccessFile.getChannel();
        ByteBuffer byteBufferData = ByteBuffer.allocate(8 * listRMSvalues.size());
        ByteBuffer byteBufferHead = ByteBuffer.allocate(8);
        byteBufferHead.asDoubleBuffer().put(listRMSvalues.size());
        byteBufferData.asDoubleBuffer().put(arrayRMSvalues);
        fileChannel.write(byteBufferHead);
        fileChannel.write(byteBufferData);
        fileChannel.close();
    }
}
