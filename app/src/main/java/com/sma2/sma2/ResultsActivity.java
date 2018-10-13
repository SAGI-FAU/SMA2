package com.sma2.sma2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.sma2.sma2.FeatureExtraction.Tapping.Tapping_feature_Activity;
import com.sma2.sma2.FeatureExtraction.Tapping.Tapping_feature_Two_Activity;

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
