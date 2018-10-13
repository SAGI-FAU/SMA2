package com.sma2.sma2.FeatureExtraction.Tapping.tools;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static java.util.Arrays.copyOfRange;

public class WAVfileReader {
    File file = null;

    public void WAVfileReader(){}

    //Get number of samples and sampling frequency from wav file.
    public int[] getdatainfo(String path) {
        BufferedInputStream WAVHeader;
        file = new File(path);
        try {
            WAVHeader = new BufferedInputStream(new FileInputStream(file));
            //Get sampling frequency.
            byte[] HeaderWav = new byte[44];
            WAVHeader.read(HeaderWav, 0, 44);
            byte fs[] = copyOfRange(HeaderWav, 24, 28);
            int Fs = ByteBuffer.wrap(fs).order(ByteOrder.LITTLE_ENDIAN).getInt();
            byte size[] = copyOfRange(HeaderWav, 40, 44);
            int Size = ByteBuffer.wrap(size).order(ByteOrder.LITTLE_ENDIAN).getInt();
            int infosig[] = {Size / 2, Fs};
            return infosig;
        } catch (Exception e) {
            e.printStackTrace();
            return new int[0];
        }
    }

    //read .WAV file and convert amplitudes.
    public float[] readWAV(int size) {
        BufferedInputStream bufferedInputStream;
        int samplesPerValue = 1;
        int bf = 2;//Number of bytes that represent the audio data captured with a resolution of 16-bits
        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            bufferedInputStream.skip(44); //Skip header of .wav
            byte[] byteBuffer = new byte[samplesPerValue * bf];
            float maxS = 32767; //Maximum positive value used to represent 16-bit signed data (2 bytes)
            int ind = 0;
            float sig[] = new float[size];
            while ((bufferedInputStream.read(byteBuffer, 0, samplesPerValue * bf)) > 0) {
                ByteBuffer littleEndianBuffer = ByteBuffer.wrap(byteBuffer);
                littleEndianBuffer.order(ByteOrder.LITTLE_ENDIAN);
                sig[ind] = littleEndianBuffer.getShort() / maxS;
                ind = ind + 1;
            }
            return sig;
        } catch (Exception e) {
            e.printStackTrace();
            return new float[0];
        }
    }
}
