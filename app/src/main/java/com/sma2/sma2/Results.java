package com.sma2.sma2;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
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
import com.sma2.sma2.FeatureExtraction.Speech.features.RadarFeatures;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Results.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Results#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Results extends Fragment {


    private Button bHistory;
    private RadarFigureManager RadarManager;
    private ProgressBar progressBar;
    private ImageView iEmojin;
    private TextView tmessage, tmessage_perc;
    int screenWidth, screenHeight;


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


        bHistory=view.findViewById(R.id.button_history);
        progressBar=view.findViewById(R.id.bar_total);
        iEmojin=view.findViewById(R.id.iEmojin_total);
        tmessage=view.findViewById(R.id.tmessage_all);
        tmessage_perc=view.findViewById(R.id.tmessage_all_perc);
        getDisplayDimensions();

        RadarManager = new RadarFigureManager(getActivity().getApplicationContext());

        RadarChart radarchart= view.findViewById(R.id.chart_total);

        float area_speech= RadarFeatures.get_last_area("speech");
        float area_tap= RadarFeatures.get_last_area("tapping");
        float area_mov=0f;

        float[] data1={area_speech,area_tap,area_mov};
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
            try {
                RadarFeatures.export_speech_feature("AreaTotal", area_progress, "area total");
            }catch (Exception e) {
                Toast.makeText(getActivity().getApplicationContext(),R.string.area_failed,Toast.LENGTH_SHORT).show();

            }
        }


        LinearLayout.LayoutParams params_line= (LinearLayout.LayoutParams)  iEmojin.getLayoutParams();
        int xRandomBar= (int)(0.01*area_progress*screenWidth-45);

        params_line.setMarginStart(xRandomBar); // The indicator bar position
        params_line.leftMargin=xRandomBar;
        params_line.setMarginStart(xRandomBar);
        iEmojin.setLayoutParams(params_line);


        String perform = getResources().getString(R.string.perform);
        progressBar.setProgress(area_progress);
        String msgp=String.valueOf(area_progress)+"%";
        tmessage_perc.setText(msgp);
        if (area_progress >=66) {
            iEmojin.setImageResource(R.drawable.happy_emojin);
            tmessage.setText(R.string.Positive_message);
        }
        else if (area_progress>=33){
            iEmojin.setImageResource(R.drawable.medium_emojin);
            tmessage.setText(R.string.Medium_message);
        }
        else{
            iEmojin.setImageResource(R.drawable.sad_emoji);
            tmessage.setText(R.string.Negative_message);
        }


        return view;
    }



    private void getDisplayDimensions() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
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
