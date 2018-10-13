package com.sma2.sma2;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sma2.sma2.FeatureExtraction.Speech.features.Energy;
import com.sma2.sma2.FeatureExtraction.Speech.features.phon_feats;
import com.sma2.sma2.FeatureExtraction.Speech.tools.TransitionDectector;
import com.sma2.sma2.FeatureExtraction.Speech.tools.WAVfileReader;
import com.sma2.sma2.FeatureExtraction.Speech.tools.f0detector;
import com.sma2.sma2.FeatureExtraction.Speech.tools.sigproc;
import com.sma2.sma2.FeatureExtraction.Tapping.Tapping_feature_Activity;
import com.sma2.sma2.FeatureExtraction.Tapping.Tapping_feature_Two_Activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class ResultsActivity extends AppCompatActivity {
    Button bTapping_one, bTapping_two;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        bTapping_one=findViewById(R.id.bTapping_one);
        bTapping_two=findViewById(R.id.bTapping_two);
    }

    public void onButtonClicked_Tapping_one(View view) {
        Intent i =new Intent(ResultsActivity.this, Tapping_feature_Activity.class);
        startActivity(i);
    }


    public void onButtonClicked_Tapping_two(View view) {
        Intent i =new Intent(ResultsActivity.this, Tapping_feature_Two_Activity.class);
        startActivity(i);
    }
}
