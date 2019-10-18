package com.sma2.sma2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jjoe64.graphview.GraphView;
import com.sma2.sma2.DataAccess.FeatureDA;
import com.sma2.sma2.DataAccess.FeatureDataService;
import com.sma2.sma2.FeatureExtraction.GraphManager;

import java.util.List;

public class ResultsHistorical extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_historical_results);

        GraphView graph_total =findViewById(R.id.plotlineAll);
        GraphView graph_speech =findViewById(R.id.plotlineSpec);
        GraphView graph_mov =findViewById(R.id.plotlineMov);
        GraphView graph_tap =findViewById(R.id.plotlineTap);


        GraphManager graphManager=new GraphManager(this);
        FeatureDataService featureDataService=new FeatureDataService(this);


        List<FeatureDA> area_total=featureDataService.get_last_10_features_by_name(featureDataService.area_total_name);
        List<FeatureDA> area_speech=featureDataService.get_last_10_features_by_name(featureDataService.area_speech_name);
        List<FeatureDA> area_movement=featureDataService.get_last_10_features_by_name(featureDataService.area_movement_name);
        List<FeatureDA> area_tapping=featureDataService.get_last_10_features_by_name(featureDataService.area_tapping_name);



        graphManager.LineGraph(graph_total, area_total, "Date", "Value");



        LinearLayout parent = findViewById(R.id.linear);

        parent.setOrientation(LinearLayout.HORIZONTAL);


        ImageView[] views = new ImageView[area_total.size()];
        for (int i=area_total.size()-1;i>=0;i--){

            ImageView iv = new ImageView(this);


            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                    , ViewGroup.LayoutParams.WRAP_CONTENT);


            iv.setLayoutParams(param);

            FeatureDA feature=area_total.get(i);
            float y=feature.getFeature_value();

            if (y>66){
                iv.setImageResource(R.drawable.happy_emojin);
            }
            else if (y>33){
                iv.setImageResource(R.drawable.medium_emojin);
            }
            else{
                iv.setImageResource(R.drawable.sad_emoji);

            }
            iv.setAdjustViewBounds(true);
            views[i] = iv;
            parent.addView(iv);
        }

        graphManager.LineGraph(graph_speech, area_speech, "Date", "Value");
        //graphManager.LineGraph(graph_mov, area_movement, "Date", "Value");
        graphManager.LineGraph(graph_tap, area_tapping, "Date", "Value");




    }
}
