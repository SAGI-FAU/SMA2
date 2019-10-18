package com.sma2.sma2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.RadarChart;
import com.sma2.sma2.DataAccess.FeatureDA;
import com.sma2.sma2.DataAccess.FeatureDataService;
import com.sma2.sma2.FeatureExtraction.Speech.features.RadarFeatures;

import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Results.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Results#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Results extends Fragment implements View.OnClickListener{


    private Button bHistory;
    private ImageButton bSpeech, bMovement, bTapping;
    private RadarFigureManager RadarManager;
    private ProgressBar progressBar;
    private ImageView iEmojin;
    private TextView tmessage, tmessage_perc;
    int screenWidth, screenHeight;
    private Results.OnFragmentInteractionListener mListener;
    FeatureDA area_speech, area_mov, area_tapping;
    FeatureDataService feat_data_service;


    public Results() {
        // Required empty public constructor
    }

    public static Results newInstance() {
        Results fragment = new Results();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_results, container, false);

        feat_data_service=new FeatureDataService(getActivity().getApplicationContext());
        bHistory=view.findViewById(R.id.button_history);
        progressBar=view.findViewById(R.id.bar_total);
        iEmojin=view.findViewById(R.id.iEmojin_total);
        tmessage=view.findViewById(R.id.tmessage_all);
        tmessage_perc=view.findViewById(R.id.tmessage_all_perc);
        bSpeech=view.findViewById(R.id.bSpeech);
        bMovement=view.findViewById(R.id.bWalking);
        bTapping=view.findViewById(R.id.bTapping_one);

        setListeners();
        RadarManager = new RadarFigureManager(getActivity().getApplicationContext());

        RadarChart radarchart= view.findViewById(R.id.chart_total);

        area_speech=feat_data_service.get_last_feat_value(feat_data_service.area_speech_name);
        float area_speech_value=area_speech.getFeature_value();

        area_mov=feat_data_service.get_last_feat_value(feat_data_service.area_movement_name);
        float area_mov_value=area_mov.getFeature_value();

        area_tapping=feat_data_service.get_last_feat_value(feat_data_service.area_tapping_name);
        float area_tapping_value=area_tapping.getFeature_value();



        float[] data1={area_speech_value,area_tapping_value,area_mov_value};
        float[] data2={100f,100f,100f};

        String Label_1 = getResources().getString(R.string.Speech);
        String Label_2 = getResources().getString(R.string.tapping_ex);
        String Label_3 = getResources().getString(R.string.movement);
        String[] labels={Label_1, Label_2, Label_3};
        RadarManager.PlotRadar(radarchart, data1, data2, labels);


        double area=RadarManager.get_area_chart(data1);
        double max_area=RadarManager.get_area_chart(data2);
        int area_progress=(int)(area*100/max_area);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        boolean new_area_total=sharedPref.getBoolean("New Area Total", false);

        if (new_area_total){
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("New Area Total", false);
            editor.apply();
            Date current = Calendar.getInstance().getTime();
            FeatureDA area_t=new FeatureDA(feat_data_service.area_total_name, current, (float)area_progress );
            feat_data_service.save_feature(area_t);
        }

        RadarManager.put_emojin_and_message(iEmojin, tmessage, tmessage_perc, area_progress, progressBar, getActivity());
        return view;
    }

    private void setListeners() {
        bSpeech.setOnClickListener(this);
        bMovement.setOnClickListener(this);
        bTapping.setOnClickListener(this);
        bHistory.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bSpeech:
                open_results_speech();
                break;
            case R.id.bWalking:
                open_results_movement();
                break;
            case R.id.bTapping_one:
                open_results_tapping();
                break;
            case R.id.button_history:
                open_results_historical();
                break;

        }
    }

    void open_results_speech(){
        Intent i =new Intent(getActivity().getApplicationContext(), ResultsSpeech.class);
        startActivity(i);
    }

    void open_results_historical(){
        Intent i =new Intent(getActivity().getApplicationContext(), ResultsHistorical.class);
        startActivity(i);
    }



    void open_results_tapping(){
        Intent i =new Intent(getActivity().getApplicationContext(), ResultsTapping.class);
        startActivity(i);
    }

    void open_results_movement(){
        Intent i =new Intent(getActivity().getApplicationContext(), ResultsMovement.class);
        startActivity(i);
    }








    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
