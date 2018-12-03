package com.sma2.sma2.FeatureExtraction.Movement;

import android.content.Context;
import android.widget.Toast;

import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CSVFileReader {
    private Context CONTEXT;
    private String[] SENSOR_DATA_HEADER = {"Timestamp [ns]",
            "aX [m/s^2]", "aY [m/s^2]", "aZ [m/s^2]",
            "gX [rad/s]", "gY [rad/s]", "gZ [rad/s]",
            "mX [uT]", "mY [uT]", "mZ [uT]",
            "r0 [a.u.]", "r1 [a.u.]", "r2 [a.u.]", "r3 [a.u.]"};
    private int NCols=SENSOR_DATA_HEADER.length;

    public CSVFileReader(Context context){
        CONTEXT=context;
    }


    public List ReadMovementFile(String path){
        List DataCSV=new ArrayList();
        try{
            CSVReader reader = new CSVReader(new FileReader(path));
            DataCSV = reader.readAll();

        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(CONTEXT, "The specified file was not found", Toast.LENGTH_SHORT).show();
        }
        return DataCSV;

    }


    private int getIndex(String header){
        for (int i=0;i<NCols;i++){
            if (SENSOR_DATA_HEADER[i].equals(header)){
                return i;
            }
        }
        return -1;
    }



    public Signal ReadMovementSignal(String path, String header){


        List<Double> Signal_=new ArrayList<>();
        List<Double> TimeStamp=new ArrayList<>(), TimeStampZero=new ArrayList<>();
        List DataCSV=ReadMovementFile(path);
        String SEPARATOR=";";
        String[] line;
        int index;
        for (int j=0;j<DataCSV.size();j++){
            index=getIndex(header);

            if(j>=6){
                line=(String[]) DataCSV.get(j);
                String [] fields = line[0].split(SEPARATOR);

                if (!(fields[index].equals("NaN"))){

                    Signal_.add(Double.parseDouble(fields[index]));
                    TimeStamp.add(Double.parseDouble(fields[0]));
                }
            }

        }
        for (int j=0;j<TimeStamp.size();j++){
            TimeStampZero.add(TimeStamp.get(j)-TimeStamp.get(0));
        }

        return new Signal(TimeStampZero, Signal_);

    }



    public class Signal {
        public final List<Double> TimeStamp;
        public final List<Double> Signal;

        public Signal(List<Double> TimeStamp, List<Double> Signal){
            this.TimeStamp=TimeStamp;
            this.Signal=Signal;
        }


    }



}
