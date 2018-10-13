package com.sma2.sma2.FeatureExtraction.Tapping;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class Tapping_feature_Two_Activity extends AppCompatActivity {
    TextView  tNumber_Taps, tTapping_time, tTapping_time_hits, tTapping_time_hits2;
    ImageView iEmojiLeft,iEmojiRight;
    String path_tapping = "/storage/emulated/0/AppSpeechData/ACC/Tapping_example2.csv";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tapping_feature__two_);


        tTapping_time_hits=findViewById(R.id.tTapping_time_hits);

        tTapping_time_hits2=findViewById(R.id.tTapping_time_hits_2);
        tNumber_Taps=findViewById(R.id.tNumber_Taps);
        tTapping_time=findViewById(R.id.tTapping_time);
        iEmojiLeft=findViewById(R.id.iEmojin_left);
        iEmojiRight=findViewById(R.id.iEmojin_Right);


        DecimalFormat df = new DecimalFormat("#.00");

        ArrayList<Double> Count_Touch=read_csv(path_tapping,0);// The index tells me which column I should access
        ArrayList<Double> Delay=read_csv(path_tapping,1);
        ArrayList<Double> Distance_one=read_csv(path_tapping,2);
        ArrayList<Double> Distance_two=read_csv(path_tapping,3);

        ArrayList<Double> Count_Touch_one=separator_vector1(Count_Touch,Count_Touch);
        ArrayList<Double> Count_Touch_two=separator_vector2(Count_Touch,Count_Touch);
        float Count_one=Count_ladybug_one(Count_Touch_one);
        float Count_two=Count_ladybug_two(Count_Touch_two);
        ArrayList<Double> Delay_one=separator_vector1(Count_Touch,Delay);
        ArrayList<Double> Delay_two=separator_vector2(Count_Touch,Delay);
        double delay_hits_one= delay_hits_Tapping_one(Count_Touch_one,Delay_one,1);
        double delay_hits_two= delay_hits_Tapping_one(Count_Touch_two,Delay_two,2);
        float dimention=pixeltomm((float)average_funtion(Distance_one));
        float dimention2=pixeltomm((float)average_funtion(Distance_two));
        float Hits_Porcentage_left=Count_one;
        float Hits_Porcentage_right=Count_two;
        float Distance_error_left=dimention;
        float Distance_error_right=dimention2;
        image_control(Hits_Porcentage_left,iEmojiLeft);
        image_control(Hits_Porcentage_right,iEmojiRight);





        tTapping_time_hits.setText(String.valueOf(df.format(delay_hits_one))+" s");
        tTapping_time_hits2.setText(String.valueOf(df.format(delay_hits_two))+" s");
        tTapping_time.setText(String.valueOf(df.format(average_funtion(Delay)))+" s");
        tNumber_Taps.setText(String.valueOf(df.format(Count_Touch.size())));



        //float Count_two=Count_ladybug_two(Count_Touch_two);



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
            if(vector.get(i)==1) {
                Count++;
            }
        }
        Count=Count*100/vector.size();
        return Count;
    }
    public  static float Count_ladybug_two(ArrayList<Double> vector){
        float Count=0;

        for (int i = 0; i < vector.size(); i ++){
            if(vector.get(i)==2) {
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
    public double delay_hits_Tapping_one(ArrayList<Double> vector_valida,ArrayList<Double>vector,int bandera){
        double delay_count=0;
        int size_vector_new=0;
        for (int i = 0; i < vector_valida.size(); i++)
            if (vector_valida.get(i)== bandera){
                delay_count+=vector.get(i);
                size_vector_new++;
            }

        return delay_count/size_vector_new;
    }
    public ArrayList<Double> separator_vector1(ArrayList<Double> v_validation,ArrayList<Double> v){
        ArrayList<Double> New_vector=new ArrayList<Double>();
        for(int i=0;i< v.size();i++ ){
            if(v_validation.get(i)==1 || v_validation.get(i)==0){
                New_vector.add(v.get(i));
            }

        }
        return New_vector;

    }
    public ArrayList<Double> separator_vector2(ArrayList<Double> v_validation,ArrayList<Double> v){
        ArrayList<Double> New_vector=new ArrayList<Double>();
        for(int i=0;i< v.size();i++ ){
            if(v_validation.get(i)==2 || v_validation.get(i)==0){
                New_vector.add(v.get(i));
            }

        }
        return New_vector;

    }
    public void image_control(float Hist_Porcentage, ImageView iEmojin){
        if( Hist_Porcentage>= 70){
            iEmojin.setImageResource(R.drawable.happy_emojin);

            Animation animation=AnimationUtils.loadAnimation(this,R.anim.zoomin);
            iEmojin.startAnimation(animation);

        }
        else if (Hist_Porcentage >=30){
            iEmojin.setImageResource(R.drawable.medium_emojin);

            Animation animation=AnimationUtils.loadAnimation(this,R.anim.zoomin);
            iEmojin.startAnimation(animation);

        }
        else{
            iEmojin.setImageResource(R.drawable.sad_emoji);

            Animation animation=AnimationUtils.loadAnimation(this,R.anim.zoomin);
            iEmojin.startAnimation(animation);


        }

    }

}
