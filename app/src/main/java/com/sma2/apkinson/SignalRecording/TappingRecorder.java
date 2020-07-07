package com.sma2.apkinson.SignalRecording;

import android.content.Context;
import android.os.Handler;

import com.sma2.apkinson.DataAccess.PatientDataService;
import com.sma2.apkinson.DataAccess.SignalDA;
import com.sma2.apkinson.DataAccess.SignalDataService;

import java.io.IOException;

/**
 * Created by paulaperez on 8/10/18.
 */

public class TappingRecorder {
    private static Context CONTEXT;
    private static Handler HANDLER;
    private CSVFileWriter mCSVFileWriter;
    private SignalDataService signalDataService;
    private SignalDA signalDA;


    private static TappingRecorder recorder_instance=null;


    private TappingRecorder(Context context) {
        CONTEXT=context;
        signalDataService = new SignalDataService(context);

    }

    public static TappingRecorder getInstance(Context context) {
        if (recorder_instance == null){
            recorder_instance = new TappingRecorder(context);
        }
        return recorder_instance;
    }


    public void TapHeaderWriter(String TaskName, int Flag) throws IOException{
        mCSVFileWriter = new CSVFileWriter(TaskName);
        signalDA = new SignalDA(TaskName, mCSVFileWriter.getFileName());

        if (Flag==0) {
            String[] TAPPING_INFO_HEADER =  {"Task Name","TouchScreen Label",
                    "Label Bug 1","TimeTap","Distance"};

            String[] TAPPING_DESCRIPTION_HEADER =
                    {"One finger tapping", "0,1", "Times between taps", "Euclidean distance"};
            String[] TAPPING_DATA_HEADER =  {"Bug Label","TimesTap [ms]",
                    "Distance Bug1"};
            mCSVFileWriter.writeData(TAPPING_INFO_HEADER);

            mCSVFileWriter.writeData(TAPPING_DESCRIPTION_HEADER);
            mCSVFileWriter.writeData(TAPPING_DATA_HEADER);


        }else{
            String[] TAPPING_INFO_HEADER =  {"Task Name","TouchScreen Label",
                    "Label Bug 1","Label Bug 2","TimeTap","Distance"};

            String[] TAPPING_DESCRIPTION_HEADER =
                    {"Two finger tapping", "0,1,2", "Times between taps", "Euclidean distance of each button"};
            String[] TAPPING_DATA_HEADER =  {"Bug Label","TimesTap [ms]",
                    "Distance Bug 1", "Distance Bug 2"};
            mCSVFileWriter.writeData(TAPPING_INFO_HEADER);

            mCSVFileWriter.writeData(TAPPING_DESCRIPTION_HEADER);
            mCSVFileWriter.writeData(TAPPING_DATA_HEADER);
        }
    }

    public String TappingFileName(){
        return mCSVFileWriter.getFileName() ;
    }



    public void TapWriter(String[] data_sensors){ mCSVFileWriter.writeData(data_sensors); }


    public void CloseTappingDocument() throws IOException{
        mCSVFileWriter.close();
        PatientDataService pd = new PatientDataService(CONTEXT);
        signalDA.setPatientDAId(pd.getPatient().getUserId());
        signalDataService.saveSignal(signalDA);
    }
}

