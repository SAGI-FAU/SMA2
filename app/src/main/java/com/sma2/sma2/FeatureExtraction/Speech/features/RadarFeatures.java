package com.sma2.sma2.FeatureExtraction.Speech.features;

import android.util.Log;
import android.os.Environment;

import com.opencsv.CSVReader;
import com.sma2.sma2.DataAccess.MedicineDA;
import com.sma2.sma2.DataAccess.MedicineDataService;
import com.sma2.sma2.FeatureExtraction.Speech.tools.WAVfileReader;
import com.sma2.sma2.FeatureExtraction.Speech.tools.array_manipulation;
import com.sma2.sma2.FeatureExtraction.Speech.tools.f0detector;
import com.sma2.sma2.FeatureExtraction.Speech.tools.sigproc;
import com.sma2.sma2.SignalRecording.CSVFileWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.abs;
import com.opencsv.CSVWriter;


public class RadarFeatures {
    private static WAVfileReader wavFileReader = new WAVfileReader();
    private static f0detector F0Detector = new f0detector();
    private static sigproc SigProc = new sigproc();
    private static array_manipulation ArrM = new array_manipulation();

    /**
     * Computation of jitter (stability of vocal fold vibration). This must be independent of the
     * vowel.
     * @param AudioFile string with the location of the audio file to be used
     * @return relative variation of f0.
     */
    public static float jitter(String AudioFile) {
        int[]  InfoSig = wavFileReader.getdatainfo(AudioFile);
        float[] SignalAh = wavFileReader.readWAV(InfoSig[0]);
        //Normalize signal
        SignalAh = SigProc.normsig(SignalAh);
        float[] f0 = F0Detector.sig_f0(SignalAh, InfoSig[1]);

        //Ensure nonzero values in f0 contour
        List lf0 = ArrM.find(f0,0f,2);
        //Convert list to float array
        //List to array
        float[] newF0 = new float[lf0.size()];
        for(int i=0;i<lf0.size();i++)
        {
            newF0[i] = f0[(int)lf0.get(i)];
        }
        f0 = newF0;

        //Length of the f0 contour
        int N = f0.length;
        if (N==0){
            return 25.0f;
        }

        //Compute mean value of f0
        float value_mean = SigProc.meanval(f0);
        //Compute standard deviation of f0
        float value_std = SigProc.calculateSD(f0,value_mean);

        for(int i=0;i<f0.length;i++)
        {
            if (f0[i] > value_mean+(3*value_std))
                f0[i]= value_mean;
        }

        //Find Max
        float[] temp = Arrays.copyOfRange(f0,0, f0.length);
        Arrays.sort(temp);
        float Mp = temp[temp.length - 1];//Maximum pitch
        //Array with variations between elements of array
        float jitt = 0f;
        for (int i = 0; i < N; i=i+3)//Jitter is computed every 3 f0 periods
        {
            jitt+= abs(f0[i] - Mp);
        }
        jitt = (100*jitt)/(N*Mp);
        return 100-jitt;
    }

    /**
     * This function computes the voice rate from the pataka speech task.
     * @param AudioFile Path of the wav file to be considered for computation of voice rate
     * @return Voice rate as a relative measure.
     */
    public static float voiceRate(String AudioFile) {
        int[] InfoSig = wavFileReader.getdatainfo(AudioFile);
        float[] Signal = wavFileReader.readWAV(InfoSig[0]);
        Signal = SigProc.normsig(Signal);
        float[] F0 = F0Detector.sig_f0(Signal, InfoSig[1]);
        List VoicedSeg = F0Detector.voiced(F0, Signal);

        float vRate=0;
        if(VoicedSeg.size()==0)
            vRate=0;
        else
            vRate = (float) InfoSig[1]*(float) VoicedSeg.size()/ (float) InfoSig[0];
            // Reference (100%) = 6.25
            // 4.61 (mean) + 1.64 (standart deviation)
            // of VoiceRate For PCGITA controls

            if(vRate >= 6.25f)
                vRate = 100;
            else
                vRate = (vRate*100)/ 6.25f;

        return vRate;
    }

    /**
     *  This function computes the amount of variation in the intonation of a speaker. Only the
     *  longest sentence should be considered (7th sentence in main/assets/textExercise.csv).
     * @param AudioFile String with the location (path) of the recording with the longest sentence.
     * @return stdf0 which is the standard deviation of the f0 contour from the longest sentence.
     */
    public static float intonation(String AudioFile) {
        //Read audio file
        int[] InfoSig = wavFileReader.getdatainfo(AudioFile);
        float[] Signal = wavFileReader.readWAV(InfoSig[0]);

        //Eliminate any possible DC level and re-scale speech signal between -1 and 1
        Signal = SigProc.normsig(Signal);

        //Get F0 contour
        float[] F0 = F0Detector.sig_f0(Signal, InfoSig[1]);

        //Get non-zero f0 values
        List<Float> nzf0 = new ArrayList<>();
        for (int i = 0; i < F0.length;i++) {
            if (F0[i] > 0) {
                nzf0.add(F0[i]);
            }
        }
        //Convert list into array of floats
        float nonzf0[] = ArrM.listtofloat(nzf0);

        //Compute standard deviation of non-zero f0 values
        float stdf0 = SigProc.calculateSD(nonzf0);

        //The reference value of the INTONATION IS MISSING. THIS IS NECESSARY TO GET THE RELATIVE VAL
        return 100/(1+stdf0);
    }

    public static void export_speech_feature(String name_file, float feature, String feat_name) throws IOException {
        String directory = Environment.getExternalStorageDirectory() + "/Apkinson/FEATURES/";
        String fileName = directory + feat_name+".csv";


        File direct = new File(directory);
        if (!direct.exists()) {
            direct.mkdirs();
        }
        File file =new File(fileName);

        CSVWriter writer;
        FileWriter mFileWriter;
        try {
            if(file.exists() && !file.isDirectory()){
                mFileWriter = new FileWriter(fileName , true) ;
                writer = new CSVWriter(mFileWriter);
            }
            else {
                writer = new CSVWriter(new FileWriter(fileName));
                String[] header={"File", feat_name};
                writer.writeNext(header);
            }
            String name=name_file.substring(name_file.lastIndexOf("/")+1);
            String[] row={"",""};
            row[0]=name;
            row[1]=Float.toString(feature);

            writer.writeNext(row);
            writer.close();
        } catch(IOException ie) {
            ie.printStackTrace();
        }

    }


    public static ArrayList<Float> get_feat_perf(String feature) throws IOException {
        String directory = Environment.getExternalStorageDirectory() + "/Apkinson/FEATURES/";
        String fileName = directory + feature+".csv";

        FileReader mFileReader=new FileReader(fileName);
        CSVReader reader = new CSVReader(mFileReader);

        List<String[]> records = reader.readAll();

        reader.close();
        int col=records.get(0).length-1;
        ArrayList<Float> perf=new ArrayList<>();
        for (int i=1;i<records.size();i++){
            perf.add(Float.valueOf(records.get(i)[col]));
        }

        return perf;
    }

}
