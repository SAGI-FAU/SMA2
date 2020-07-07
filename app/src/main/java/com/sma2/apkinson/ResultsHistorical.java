package com.sma2.apkinson;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.sma2.apkinson.DataAccess.FeatureDA;
import com.sma2.apkinson.DataAccess.FeatureDataService;
import com.sma2.apkinson.FeatureExtraction.GraphManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ResultsHistorical extends AppCompatActivity implements View.OnClickListener {

    ArrayList<Long> dates1;

    Spinner spinner;
    List<FeatureDA> Features;
    FeatureDataService featureDataService;
    GraphManager graphManager;
    GraphView graph_total;
    String[] list_features_names= new String[4];
    private ImageButton bHelp;
    FloatingActionButton fab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_historical_results);

        graph_total =findViewById(R.id.plotlineAll);
        spinner = findViewById(R.id.spinnePlot);
        graphManager=new GraphManager(this);
        featureDataService=new FeatureDataService(this);
        bHelp=findViewById(R.id.button_help);
        fab = findViewById(R.id.fab_settings);

        bHelp.setOnClickListener(this);
        fab.setOnClickListener(this);

        Resources r = getResources();
        String[] categories = new String[]{r.getString(R.string.global_results),r.getString(R.string.speech_results),r.getString(R.string.movement_results),r.getString(R.string.taping_results)};

        list_features_names[0]=featureDataService.area_total_name;
        list_features_names[1]=featureDataService.area_speech_name;
        list_features_names[2]=featureDataService.area_movement_name;
        list_features_names[3]=featureDataService.area_tapping_name;
        Features=featureDataService.get_last_10_features_by_name(list_features_names[0]);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item,categories);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Features=featureDataService.get_last_10_features_by_name(list_features_names[i]);
                if (Features.size()>0){
                    plot_history();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Features=featureDataService.get_last_10_features_by_name(list_features_names[0]);
                if (Features.size()>0){
                    plot_history();

                }
            }
        });


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.button_help:
                open_help();
                break;
            case R.id.fab_settings:
                open_settings();
                break;

        }

    }

    public void open_settings() {
        Intent intent_settings = new Intent(ResultsHistorical.this, SettingsActivity.class);
        startActivity(intent_settings);
    }

    private void open_help(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        String Title = getResources().getString(R.string.interpre);
        builder.setTitle(Title);

        String Text = getResources().getString(R.string.HistoricHelp);
        builder.setMessage(Text);

        builder.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() { // define the 'Cancel' button
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }



    ArrayList<Long> get_dates(List<FeatureDA> features){

        ArrayList<Long> dates=new ArrayList<>();

        for (int i = 0; i <features.size(); i++) {
            Date date=features.get(i).getFeature_date();
            long datef=date.getTime();
            dates.add(datef);
        }

        return dates;
    }


    private void plot_history(){

        GridLabelRenderer gridlabel1=graphManager.LineGraph(graph_total, Features, "Date", "Value");


        dates1=get_dates(Features);

        gridlabel1.setLabelFormatter(new DefaultLabelFormatter() {
            SimpleDateFormat dateFormat1 =  new SimpleDateFormat("dd/MM");
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {

                    int val=(int) Math.round(value);
                    if (val<dates1.size()){
                        long datef=dates1.get((val));
                        Date d = new Date(datef);
                        return (dateFormat1.format(d));
                    }

                }
                return "";
            }
        });

        LinearLayout parent = findViewById(R.id.linear);
        parent.setOrientation(LinearLayout.HORIZONTAL);
        parent.removeAllViews();
        ImageView[] views = new ImageView[Features.size()];
        for (int i=0;i<Features.size();i++){
            ImageView iv = new ImageView(this);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                    , ViewGroup.LayoutParams.WRAP_CONTENT);
            param.weight=10/ Features.size();
            iv.setLayoutParams(param);
            FeatureDA feature=Features.get(i);
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


    }

}
