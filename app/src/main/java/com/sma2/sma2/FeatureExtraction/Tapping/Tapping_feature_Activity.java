package com.sma2.sma2.FeatureExtraction.Tapping;

import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.sma2.sma2.DataAccess.SignalDA;
import com.sma2.sma2.DataAccess.SignalDataService;
import com.sma2.sma2.FeatureExtraction.GetExercises;
import com.sma2.sma2.FeatureExtraction.GraphManager;
import com.sma2.sma2.MainActivityMenu;
import com.sma2.sma2.R;
import com.sma2.sma2.ResultsActivity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;

public class Tapping_feature_Activity extends AppCompatActivity  implements View.OnClickListener {
    TextView  tNumber_Taps,tTapping_time_hits,tMessage, tTapping_perc_hits, tTapping_perc_hits_left, tTapping_perc_hits_right;
    Button bBack;
    ImageView iEmojin, iEmojiLeft,iEmojiRight;;
    String path_tapping = null, path_tapping2 = null, path_bar=null;
    List<String> path_tapping_all= new ArrayList<String>(), path_tapping_all2= new ArrayList<String>();
    List<String> path_bar_all= new ArrayList<String>();
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

        GraphManager graphManager=new GraphManager(this);

        int IDEx=33;
        GetExercises GetEx=new GetExercises(this);
        String name=GetEx.getNameExercise(IDEx);

        List<SignalDA> signals=signalDataService.getSignalsbyname(name);
        if (signals.size()>0){
            path_tapping=PATH+signals.get(signals.size()-1).getSignalPath();
            if (signals.size()>4){
                for (int i=signals.size()-4;i<signals.size();i++){
                    path_tapping_all.add(PATH+signals.get(i).getSignalPath());
                }
            }
            else{
                for (int i=0;i<signals.size();i++){
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
        float perc_distance=100/(1+dimention);
        String Distance_error=df.format(dimention);
        String Tapping_time_total=df.format(average_funtion(Delay_one));

        float performancef=(Hist_Porcentage+perc_distance)/2;
        String performaces=String.valueOf(df.format(performancef))+"%";
        if(path_tapping==null){
            tNumber_Taps.setText(R.string.Empty);
            tTapping_time_hits.setText(R.string.Empty);
            tTapping_perc_hits.setText(R.string.Empty);

        }
        else{
            tNumber_Taps.setText( String.valueOf(Count_Touch_one.size()));
            tTapping_time_hits.setText( String.valueOf(df.format(delay_hits_two))+ " ms");
            tTapping_perc_hits.setText(performaces);

        }

        if( performancef>= 70){
            iEmojin.setImageResource(R.drawable.happy_emojin);
            Animation animation=AnimationUtils.loadAnimation(this,R.anim.zoomin);
            iEmojin.startAnimation(animation);
            tMessage.setText(R.string.Positive_message);
            Animation animation2=AnimationUtils.loadAnimation(this,R.anim.bounce);
            tMessage.startAnimation(animation2);
        }
        else if (performancef >=30){
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


        ArrayList<Integer> x=new ArrayList<>();
        ArrayList<Float> y=new ArrayList<>();
        for (int i=0;i<5;i++){

            if (i<path_tapping_all.size()){
                Count_Touch_one = read_csv(path_tapping_all.get(i), 0);// The index tells me which column I should access
                Distance_one=read_csv(path_tapping,2);
                dimention=pixeltomm((float)average_funtion(Distance_one));
                perc_distance=100/(1+dimention);
                x.add(i+1);
                y.add((perc_distance+Count_ladybug_one(Count_Touch_one))/2);


            }
            else{
                x.add(i+1);
                y.add((float) 0);
            }

        }

        String Title=getResources().getString(R.string.Perc_Tapping_Hits);
        String Ylabel=getResources().getString(R.string.Perc_Tapping_Hits);
        String Xlabel=getResources().getString(R.string.session);
        GraphView graph =findViewById(R.id.bar_perc);
        graphManager.BarGraph(graph, x, y, 101.0, 5, Title, Xlabel, Ylabel);

        IDEx=34;
        GetEx=new GetExercises(this);
        name=GetEx.getNameExercise(IDEx);
        List<SignalDA> signals2=signalDataService.getSignalsbyname(name);
        if (signals2.size()>0){
            path_tapping2=PATH+signals2.get(signals2.size()-1).getSignalPath();
            if (signals2.size()>4){
                for (int i=signals2.size()-4;i<signals2.size();i++){
                    path_tapping_all2.add(PATH+signals2.get(i).getSignalPath());
                }
            }
            else{
                for (int i=0;i<signals2.size();i++){
                    path_tapping_all2.add(PATH+signals2.get(i).getSignalPath());
                }
            }
        }

        float perc_tapp, vel_tapp, pos_error_tapp;
        ArrayList<Double> Count_Touch2=read_csv(path_tapping2,0);
        ArrayList<Double> Distance_left=read_csv(path_tapping2,2);
        ArrayList<Double> Distance_right=read_csv(path_tapping2,3);
        ArrayList<Double> Count_Touch_left=separator_vector(Count_Touch2,1);
        ArrayList<Double> Count_Touch_right=separator_vector(Count_Touch2,2);

        float dimention_left=pixeltomm((float)average_funtion(Distance_left));
        float perc_distance_left=100/(1+dimention_left);

        float dimention_right=pixeltomm((float)average_funtion(Distance_right));
        float perc_distance_right=100/(1+dimention_right);


        float PercLeft=(Count_ladybug_one(Count_Touch_left)+perc_distance_left)/2;
        float PercRight=(Count_ladybug_one(Count_Touch_right)+perc_distance_right)/2;

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

        ArrayList<Integer> x2=new ArrayList<>();
        ArrayList<Float> y2=new ArrayList<>();


        for (int i=0;i<5;i++){

            if (i<path_tapping_all2.size()){
                Count_Touch2 = read_csv(path_tapping_all2.get(i), 0);
                Distance_left=read_csv(path_tapping2,2);
                Distance_right=read_csv(path_tapping2,3);

                Count_Touch_left=separator_vector(Count_Touch2,1);
                Count_Touch_right=separator_vector(Count_Touch2,2);

                dimention_left=pixeltomm((float)average_funtion(Distance_left));
                perc_distance_left=100/(1+dimention_left);
                dimention_right=pixeltomm((float)average_funtion(Distance_right));
                perc_distance_right=100/(1+dimention_right);


                x2.add(i+1);
                y2.add((Count_ladybug_one(Count_Touch_left)+Count_ladybug_one(Count_Touch_right)+perc_distance_left+perc_distance_right)/4);
            }
            else{
                x2.add(i+1);
                y2.add((float) 0);
            }

        }

        GraphView graph2 =findViewById(R.id.bar_perc2);
        graphManager.BarGraph(graph2, x2, y2, 101.0, 5, Title, Xlabel, Ylabel);

        perc_tapp=100*(Count_ladybug_one(Count_Touch_one)+Count_ladybug_one(Count_Touch2))/(Count_Touch_one.size()+
                Count_Touch2.size());
        vel_tapp=(Count_Touch_one.size()+Count_Touch2.size())/20;
        //float taptotalerror=Distance_one.size()+Distance_left.size()+Distance_right.size();
        pos_error_tapp = sumvector(Distance_one) + sumvector(Distance_left) + sumvector(Distance_right);

        IDEx=35;
        GetEx=new GetExercises(this);
        name=GetEx.getNameExercise(IDEx);
        List<SignalDA> signalbar=signalDataService.getSignalsbyname(name);
        if (signalbar.size()>0){
            path_bar=PATH+signalbar.get(signalbar.size()-1).getSignalPath();
            if (signalbar.size()>4){
                for (int i=signalbar.size()-4;i<signalbar.size();i++){
                    path_bar_all.add(PATH+signalbar.get(i).getSignalPath());
                }
            }
            else{
                for (int i=0;i<signalbar.size();i++){
                    path_bar_all.add(PATH+signalbar.get(i).getSignalPath());
                }
            }
        }

        float bar_hits;
        ArrayList<Double> Position_bar=read_csv(path_bar,0);
        ArrayList<Double> Reach_time=read_csv(path_bar,1);
        bar_hits=Position_bar.size();

        // Radar chart
        RadarChart radarchart= findViewById(R.id.chartap);
        radarchart.getDescription().setEnabled(false);
        radarchart.animateXY(5000, 5000, Easing.EaseInOutQuad);

        RadarData radardata;

        float[] datos2={perc_tapp,fittopattern(vel_tapp,1.5667f),100-fittopattern(pos_error_tapp,21034f),
                fittopattern(bar_hits,13.75f)}; // datos que se van a graficar
        float[] datos1={100f,100f,100f,100f}; // datos de refeerencia
        //double area1=0.5*(datos1[0]+datos1[2])*(datos1[1]+datos1[3]);
        //double area2=0.5*(datos2[0]+datos2[2])*(datos2[1]+datos2[3]);
        radardata=setdata(datos1,datos2);
        String[] labels={"% Tapping hits","Tapping velocity", "Tapping distance error", "Sliding hits"};

        XAxis xAxis=radarchart.getXAxis();
        xAxis.setTextSize(12f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setLabelRotationAngle(90f);

        YAxis yAxis = radarchart.getYAxis();
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(80f);

        Legend l = radarchart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
        l.setTextColor(Color.BLACK);
        l.setTextSize(20f);

        radarchart.setExtraOffsets(0,-400,0,-400);
        //radarchart.setBackgroundColor(Color.WHITE);
        radarchart.setScaleY(1f);
        radarchart.setScaleX(1f);
        radarchart.setData(radardata);
        radarchart.invalidate(); // refresh
    }


    private RadarData setdata(float[] datos1, float[] datos2) {
        int cnt = datos1.length;
        ArrayList<RadarEntry> entries1 = new ArrayList<RadarEntry>();
        ArrayList<RadarEntry> entries2 = new ArrayList<RadarEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < cnt; i++) {
            //float val1 = (float) (Math.random() * mul) + min;
            entries1.add(new RadarEntry(datos1[i]));

            //float val2 = (float) (Math.random() * mul) + min;
            entries2.add(new RadarEntry(datos2[i]));
        }

        RadarDataSet set1 = new RadarDataSet(entries1, "Below Session");
        set1.setColor(Color.rgb(255, 185, 0));
        set1.setFillColor(Color.rgb(255, 185, 0));
        set1.setDrawFilled(true);
        set1.setFillAlpha(200);
        set1.setLineWidth(2f);
        set1.setValueTextColor(Color.rgb(255, 185, 0));
        set1.setValueTextSize(15f);
        set1.setDrawHighlightCircleEnabled(true);
        set1.setDrawHighlightIndicators(false);

        RadarDataSet set2 = new RadarDataSet(entries2, "Current Session");
        set2.setColor(Color.rgb(0, 200, 200));
        set2.setFillColor(Color.rgb(0, 200, 200));
        set2.setDrawFilled(true);
        set2.setFillAlpha(180);
        set2.setLineWidth(2f);
        set2.setDrawHighlightCircleEnabled(true);
        set2.setDrawHighlightIndicators(false);
        set2.setValueTextColor(Color.rgb(0, 200, 200));
        set2.setValueTextSize(15f);

        RadarData dataradar= new RadarData();

        dataradar.addDataSet(set1);
        dataradar.addDataSet(set2);
        return dataradar;
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
        //Count=Count*100/vector.size();
        return Count;
    }

    public  static float sumvector(ArrayList<Double> vector){
        float Sum=0;

        for (int i = 0; i < vector.size(); i ++)
            Sum+=vector.get(i);

        return Sum;
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

    public float fittopattern(float value, float pattern){
        float veltapp_perc=0;
        if (value>pattern)
            veltapp_perc=100;
        else
            veltapp_perc=100f*value/pattern;

        return veltapp_perc;
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
