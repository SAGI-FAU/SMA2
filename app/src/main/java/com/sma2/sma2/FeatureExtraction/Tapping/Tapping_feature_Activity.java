package com.sma2.sma2.FeatureExtraction.Tapping;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.sma2.sma2.R;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Tapping_feature_Activity extends AppCompatActivity {
    TextView  tNumber_Taps,tTapping_time_hits,tMessage;
    ImageView iEmojin;
    String path_tapping = null; // To Do
            //"/storage/emulated/0/AppSpeechData/ACC/Tapping_example.csv";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tapping_feature_);

        tNumber_Taps = findViewById(R.id.tNumber_Taps);
        tTapping_time_hits=findViewById(R.id.tTapping_time_hits);
        iEmojin=findViewById(R.id.iEmojin);
        tMessage=findViewById(R.id.tmessage);
        DecimalFormat df = new DecimalFormat("#.00");

        ArrayList<Double> Count_Touch_one=read_csv(path_tapping,0);// The index tells me which column I should access
        ArrayList<Double> Delay_one=read_csv(path_tapping,2);
        ArrayList<Double> Distance_one=read_csv(path_tapping,1);
        double delay_hits_two= delay_hits_Tapping_one(Count_Touch_one,Delay_one);
        float Count_one=Count_ladybug_one(Count_Touch_one);

        float dimention=pixeltomm((float)average_funtion(Distance_one));
        float Hist_Porcentage= Count_one;
        String Distance_error=df.format(dimention);
        String Tapping_time_total=df.format(average_funtion(Delay_one));
        tNumber_Taps.setText( String.valueOf(Count_Touch_one.size()));
        tTapping_time_hits.setText( String.valueOf(df.format(delay_hits_two))+ " ms");
        if( Hist_Porcentage>= 70){
            iEmojin.setImageResource(R.drawable.happy_emojin);

            Animation animation=AnimationUtils.loadAnimation(this,R.anim.zoomin);
            iEmojin.startAnimation(animation);
            tMessage.setText("Good Job");
            Animation animation2=AnimationUtils.loadAnimation(this,R.anim.bounce);
            tMessage.startAnimation(animation2);
        }
        else if (Hist_Porcentage >=30){
            iEmojin.setImageResource(R.drawable.medium_emojin);

            Animation animation=AnimationUtils.loadAnimation(this,R.anim.zoomin);
            iEmojin.startAnimation(animation);
            tMessage.setText("Good Job \nBut, You can get better\n");
            Animation animation2=AnimationUtils.loadAnimation(this,R.anim.bounce);
            tMessage.startAnimation(animation2);
        }
        else{
                iEmojin.setImageResource(R.drawable.sad_emoji);

                Animation animation=AnimationUtils.loadAnimation(this,R.anim.zoomin);
                iEmojin.startAnimation(animation);
                tMessage.setText("Let's improve \nContinue working");
            Animation animation2=AnimationUtils.loadAnimation(this,R.anim.bounce);
            tMessage.startAnimation(animation2);

        }


    }
    public static double average_funtion(ArrayList<Double> v) {
        double prom = 0.0;
        for (int i = 0; i < v.size(); i++)
            prom += v.get(i);

        return prom / (double) v.size();
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
    public  static float Count_ladybug_one(ArrayList<Double> vector){
        float Count=0;

        for (int i = 0; i < vector.size(); i ++){
            if(vector.get(i)==1 ) {
                Count++;
            }
        }
        Count=Count*100/vector.size();
        return Count;
    }
    public float pixeltomm(float px ){
        float m_px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, 1,
                getResources().getDisplayMetrics());
        float mm=px/m_px;
        return mm;
    }
    public double delay_hits_Tapping_one(ArrayList<Double> vector_valida,ArrayList<Double>vector){
        double delay_count=0;
        int size_vector_new=0;
        for (int i = 0; i < vector_valida.size(); i++)
            if (vector_valida.get(i)==1){
                delay_count+=vector.get(i);
                size_vector_new++;
            }

        return delay_count/size_vector_new;
    }
}
