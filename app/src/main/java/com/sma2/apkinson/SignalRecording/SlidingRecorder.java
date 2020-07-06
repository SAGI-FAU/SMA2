package com.sma2.apkinson.SignalRecording;

import android.content.Context;
import android.os.Handler;

import com.sma2.apkinson.DataAccess.PatientDataService;
import com.sma2.apkinson.DataAccess.SignalDA;
import com.sma2.apkinson.DataAccess.SignalDataService;


import java.io.IOException;

/** * Created by paulaperez on 8/11/18.
 */


public class SlidingRecorder {

    private static Context CONTEXT;
    private static Handler HANDLER;
    private CSVFileWriter mCSVFileWriter;
    private SignalDataService signalDataService;
    private SignalDA signalDA;

    private static SlidingRecorder recorder_instance = null;

    private SlidingRecorder(Context context) {
        CONTEXT = context;
        signalDataService = new SignalDataService(context);


    }

    public static SlidingRecorder getInstance(Context context) {
        if (recorder_instance == null) {
            recorder_instance = new SlidingRecorder(context);
        }
        return recorder_instance;
    }

    public void SlidingHeaderWriter(String TaskName) throws IOException {
        mCSVFileWriter = new CSVFileWriter(TaskName);
        signalDA = new SignalDA(TaskName, mCSVFileWriter.getFileName());


            String[] SLIDING_INFO_HEADER = {"Task Name", "Position", "ReachTime"};

            String[] SLIDING_DESCRIPTION_HEADER =
                    {"Sliding","Bar position", "Time to reach the bar"};
            String[] SLIDING_DATA_HEADER = {"Position [sp]", "ReachTime [ms]"};
            mCSVFileWriter.writeData(SLIDING_INFO_HEADER);

            mCSVFileWriter.writeData(SLIDING_DESCRIPTION_HEADER);
            mCSVFileWriter.writeData(SLIDING_DATA_HEADER);

    }


    public String SlidingFileName(){
        return mCSVFileWriter.getFileName() ;
    }



    public void SlidingWriter(String[] data_sensors){mCSVFileWriter.writeData(data_sensors);}


    public void CloseSlidingDocument() throws IOException{
        mCSVFileWriter.close();
        PatientDataService pd = new PatientDataService(CONTEXT);
        signalDA.setPatientDAId(pd.getPatient().getUserId());
        signalDataService.saveSignal(signalDA);
    }


}
