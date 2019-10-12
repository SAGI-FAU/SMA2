package com.sma2.sma2.FeatureExtraction.Tapping;
import android.content.Context;
import com.sma2.sma2.DataAccess.SignalDA;
import com.sma2.sma2.DataAccess.SignalDataService;
import com.sma2.sma2.FeatureExtraction.GetExercises;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FeatureTapping {
    Context CONTEXT;
    public FeatureTapping(Context context){
        CONTEXT=context;
    }

    public float fittopattern(float value, float pattern){
        float veltapp_perc=0;
        if (value>pattern)
            veltapp_perc=100;
        else
            veltapp_perc=100f*value/pattern;

        return veltapp_perc;
    }

    public  static float sumvector(ArrayList<Double> vector){
        float Sum=0;

        for (int i = 0; i < vector.size(); i ++)
            Sum+=vector.get(i);

        return Sum;
    }

    public  static float Count_ladybug_one(ArrayList<Double> vector){
        float Count=0;

        for (int i = 0; i < vector.size(); i ++){
            if(vector.get(i)==1 || vector.get(i)==2 ) {
                Count++;
            }
        }
        //Count=Count*100/vector.size();
        return Count;
    }


    public String getDatos(int IDEx, String PATH){
        SignalDataService signalDataService =new SignalDataService(CONTEXT);
        String path_bar=null;
        GetExercises GetEx=new GetExercises(CONTEXT);
        String name=GetEx.getNameExercise(IDEx);
        List<SignalDA> signalbar=signalDataService.getSignalsbyname(name);
        if (signalbar.size()>0)
            path_bar=PATH+signalbar.get(signalbar.size()-1).getSignalPath();

        return path_bar;
    }

    public float [] totalfeatures(String PATH){
        String path_tapping, path_tapping2, path_bar;
        path_tapping=getDatos(33,PATH);
        path_tapping2=getDatos(34,PATH);
        path_bar=getDatos(35,PATH);

        ArrayList<Double> Count_Touch_one=read_csv(path_tapping,0);// The index tells me which column I should access
        ArrayList<Double> Distance_one=read_csv(path_tapping,2);

        ArrayList<Double> Count_Touch2=read_csv(path_tapping2,0);
        ArrayList<Double> Distance_left=read_csv(path_tapping2,2);
        ArrayList<Double> Distance_right=read_csv(path_tapping2,3);

        float perc_tapp=100*(Count_ladybug_one(Count_Touch_one)+Count_ladybug_one(Count_Touch2))/(Count_Touch_one.size()+
                Count_Touch2.size());
        float vel_tapp=(Count_Touch_one.size()+Count_Touch2.size())/20;
        float pos_error_tapp = sumvector(Distance_one) + sumvector(Distance_left) + sumvector(Distance_right);

        ArrayList<Double> Position_bar=read_csv(path_bar,0);
        float bar_hits=Position_bar.size();
        float [] data1={perc_tapp,fittopattern(vel_tapp,1.5667f),100-fittopattern(pos_error_tapp,21034f),
                fittopattern(bar_hits,13.75f)};
        return data1;
    }

    public static ArrayList<Double> read_csv(String path, int indice) {
        int Count_band=0;
        ArrayList<Double> accX = new ArrayList<Double>();
        String SEPARATOR=";";
        BufferedReader br = null;

        try {

            br =new BufferedReader(new FileReader(path));
            String line = br.readLine();
            while (null!=line) {
                if(Count_band>=3){
                    String [] fields = line.split(SEPARATOR);
                    accX.add(Double.parseDouble(fields[indice]));

                }
                Count_band++;

                line = br.readLine();
            }

        } catch (Exception e) {

        } finally {
            if (null!=br){
                try {
                    br.close();
                } catch (IOException e) {

                }
            }
        }
        return accX ;

    }
}
