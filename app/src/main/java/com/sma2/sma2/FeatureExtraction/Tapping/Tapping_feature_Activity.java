package com.sma2.sma2.FeatureExtraction.Tapping;

import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.sma2.sma2.DataAccess.SignalDA;
import com.sma2.sma2.DataAccess.SignalDataService;
import com.sma2.sma2.MainActivityMenu;
import com.sma2.sma2.R;
import com.sma2.sma2.ResultsActivity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Tapping_feature_Activity extends AppCompatActivity  implements View.OnClickListener {
    TextView  tNumber_Taps,tTapping_time_hits,tMessage, tTapping_perc_hits, tTapping_perc_hits_left, tTapping_perc_hits_right;
    Button bBack;
    ImageView iEmojin, iEmojiLeft,iEmojiRight;;
    String path_tapping = null, path_tapping2 = null;
    List<String> path_tapping_all= new ArrayList<String>(), path_tapping_all2= new ArrayList<String>();
    private final String PATH = Environment.getExternalStorageDirectory() + "/Apkinson/MOVEMENT/";

    //"/storage/emulated/0/AppSpeechData/ACC/Tapping_example.csv";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // general objects
        setContentView(R.layout.activity_tapping_feature_);
        bBack=findViewById(R.id.button_back4);
        tNumber_Taps = findViewById(R.id.tNumber_Taps);
        tTapping_time_hits=findViewById(R.id.tTapping_time_hits);
        tTapping_perc_hits= findViewById(R.id.tTapping_perc_hits);
        tTapping_perc_hits_left= findViewById(R.id.tTapping_time_hits_left);
        tTapping_perc_hits_right= findViewById(R.id.tTapping_time_hits_right);
        iEmojin=findViewById(R.id.iEmojin);
        iEmojiLeft=findViewById(R.id.iEmojin_left);
        iEmojiRight=findViewById(R.id.iEmojin_right);
        tMessage=findViewById(R.id.tmessage);
        bBack.setOnClickListener(this);
        SignalDataService signalDataService =new SignalDataService(this);
        DecimalFormat df = new DecimalFormat("#.0");

        String name="Tapping one finger";
        List<SignalDA> signals=signalDataService.getSignalsbyname(name);
        if (signals.size()>0){
            path_tapping=PATH+signals.get(signals.size()-1).getSignalPath();
            if (signals.size()>4){
                for (int i=3;i>=0;i--){
                    path_tapping_all.add(PATH+signals.get(i).getSignalPath());
                }
            }
            else{
                for (int i=signals.size()-1;i>=0;i--){
                    path_tapping_all.add(PATH+signals.get(i).getSignalPath());
                }
            }
        }

        ArrayList<Double> Count_Touch_one=read_csv(path_tapping,0);// The index tells me which column I should access
        ArrayList<Double> Delay_one=read_csv(path_tapping,1);
        ArrayList<Double> Distance_one=read_csv(path_tapping,2);
        double delay_hits_two= delay_hits_Tapping_one(Count_Touch_one,Delay_one);
        float Hist_Porcentage=Count_ladybug_one(Count_Touch_one);
        float dimention=pixeltomm((float)average_funtion(Distance_one));
        String Distance_error=df.format(dimention);
        String Tapping_time_total=df.format(average_funtion(Delay_one));

        if(path_tapping==null){
            tNumber_Taps.setText(R.string.Empty);
            tTapping_time_hits.setText(R.string.Empty);
            tTapping_perc_hits.setText(R.string.Empty);

        }
        else{
            tNumber_Taps.setText( String.valueOf(Count_Touch_one.size()));
            tTapping_time_hits.setText( String.valueOf(df.format(delay_hits_two))+ " ms");
            tTapping_perc_hits.setText( String.valueOf(df.format(Hist_Porcentage))+ "%");

        }

        if( Hist_Porcentage>= 70){
            iEmojin.setImageResource(R.drawable.happy_emojin);
            Animation animation=AnimationUtils.loadAnimation(this,R.anim.zoomin);
            iEmojin.startAnimation(animation);
            tMessage.setText(R.string.Positive_message);
            Animation animation2=AnimationUtils.loadAnimation(this,R.anim.bounce);
            tMessage.startAnimation(animation2);
        }
        else if (Hist_Porcentage >=30){
            iEmojin.setImageResource(R.drawable.medium_emojin);
            Animation animation=AnimationUtils.loadAnimation(this,R.anim.zoomin);
            iEmojin.startAnimation(animation);
            tMessage.setText(R.string.Medium_message);
            Animation animation2=AnimationUtils.loadAnimation(this,R.anim.bounce);
            tMessage.startAnimation(animation2);
        }
        else{
            iEmojin.setImageResource(R.drawable.sad_emoji);
            Animation animation=AnimationUtils.loadAnimation(this,R.anim.zoomin);
            iEmojin.startAnimation(animation);
            tMessage.setText(R.string.Negative_message);
            Animation animation2=AnimationUtils.loadAnimation(this,R.anim.bounce);
            tMessage.startAnimation(animation2);
        }

        int j;
        BarGraphSeries<DataPoint> series= new BarGraphSeries<>();
        if (path_tapping_all.size()>0) {
            j = path_tapping_all.size() - 1;
            for (int i = 0; i < path_tapping_all.size(); i++) {
                Count_Touch_one = read_csv(path_tapping_all.get(j), 0);// The index tells me which column I should access
                series.appendData(new DataPoint(i + 1, Count_ladybug_one(Count_Touch_one)), true, 5);
                j = j - 1;
            }
        }
        else{
            series.appendData(new DataPoint(1, 0), true, 5);
            }
        GraphView graph =findViewById(R.id.bar_perc);
        graph.addSeries(series);

        series.setColor(Color.rgb(255, 140, 0));
        series.setSpacing(5);
        graph.getViewport().setMinY(0.0);
        graph.getViewport().setMaxY(101.0);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(5);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);
        series.setTitle(getResources().getString(R.string.Perc_Tapping_Hits));
        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle(getResources().getString(R.string.session));
        gridLabel.setVerticalAxisTitle(getResources().getString(R.string.Perc_Tapping_Hits));


        // two finger tapping
        name="Tapping two fingers";
        List<SignalDA> signals2=signalDataService.getSignalsbyname(name);
        if (signals2.size()>0){
            path_tapping2=PATH+signals2.get(signals2.size()-1).getSignalPath();
            if (signals2.size()>4){
                for (int i=3;i>=0;i--){
                    path_tapping_all2.add(PATH+signals2.get(i).getSignalPath());
                }
            }
            else{
                for (int i=signals2.size()-1;i>=0;i--){
                    path_tapping_all2.add(PATH+signals2.get(i).getSignalPath());
                }
            }
        }


        ArrayList<Double> Count_Touch2=read_csv(path_tapping2,0);

        ArrayList<Double> Count_Touch_left=separator_vector(Count_Touch2,1);
        ArrayList<Double> Count_Touch_right=separator_vector(Count_Touch2,2);

        float PercLeft=Count_ladybug_one(Count_Touch_left);
        float PercRight=Count_ladybug_one(Count_Touch_right);

        if(path_tapping2==null){
            tTapping_perc_hits_left.setText(R.string.Empty);
            tTapping_perc_hits_right.setText(R.string.Empty);
        }
        else{
            tTapping_perc_hits_left.setText( String.valueOf(df.format(PercLeft))+ "%");
            tTapping_perc_hits_right.setText( String.valueOf(df.format(PercRight))+ "%");
        }

        image_control(PercLeft,iEmojiLeft);
        image_control(PercRight,iEmojiRight);




        BarGraphSeries<DataPoint> series2= new BarGraphSeries<>();
        if (path_tapping_all2.size()>0) {
            j = path_tapping_all2.size() - 1;
            for (int i = 0; i < path_tapping_all2.size(); i++) {
                Count_Touch2 = read_csv(path_tapping_all2.get(j), 0);
                Count_Touch_left=separator_vector(Count_Touch2,1);
                Count_Touch_right=separator_vector(Count_Touch2,2);

                series2.appendData(new DataPoint(i + 1, (Count_ladybug_one(Count_Touch_left)+Count_ladybug_one(Count_Touch_right))/2), true, 5);
                j = j - 1;
            }
        }
        else{
            series2.appendData(new DataPoint(1, 0), true, 5);
        }
        GraphView graph2 =findViewById(R.id.bar_perc2);
        graph2.addSeries(series2);

        series2.setColor(Color.rgb(255, 140, 0));
        series2.setSpacing(5);
        graph2.getViewport().setMinY(0.0);
        graph2.getViewport().setMaxY(101.0);
        graph2.getViewport().setMinX(0);
        graph2.getViewport().setMaxX(5);
        graph2.getViewport().setYAxisBoundsManual(true);
        graph2.getViewport().setXAxisBoundsManual(true);
        series2.setTitle(getResources().getString(R.string.Perc_Tapping_Hits));
        GridLabelRenderer gridLabel2 = graph2.getGridLabelRenderer();
        gridLabel2.setHorizontalAxisTitle(getResources().getString(R.string.session));
        gridLabel2.setVerticalAxisTitle(getResources().getString(R.string.Perc_Tapping_Hits));


    }




    public ArrayList<Double> separator_vector(ArrayList<Double> v_validation, int bug) {
        ArrayList<Double> New_vector = new ArrayList<>();
        for (int i = 0; i < v_validation.size(); i++) {
            if (v_validation.get(i) == bug || v_validation.get(i) == 0) {
                New_vector.add(v_validation.get(i));
            }

        }
        return New_vector;
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
            if(vector.get(i)==1 || vector.get(i)==2 ) {
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



    public void image_control(float Hist_Perc, ImageView iEmojin){
        if( Hist_Perc>= 70){
            iEmojin.setImageResource(R.drawable.happy_emojin);
            Animation animation=AnimationUtils.loadAnimation(this,R.anim.zoomin);
            iEmojin.startAnimation(animation);
        }
        else if (Hist_Perc >=30){
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


    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.button_back4:
                onButtonBack();
                break;
        }
    }

    private void onButtonBack(){
        Intent i =new Intent(Tapping_feature_Activity.this, MainActivityMenu.class);
        startActivity(i);

    }

}
