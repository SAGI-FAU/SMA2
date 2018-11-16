package com.sma2.sma2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.sma2.sma2.FeatureExtraction.Tapping.Tapping_feature_Activity;
import com.sma2.sma2.FeatureExtraction.Tapping.Tapping_feature_Two_Activity;


public class ResultsActivity extends AppCompatActivity implements View.OnClickListener {
    Button bTapping_one, bTapping_two, bBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        bTapping_one=findViewById(R.id.bTapping_one);
        bTapping_two=findViewById(R.id.bTapping_two);
        bBack=findViewById(R.id.button_back3);
        setListeners();
    }


    private void setListeners() {
        bTapping_one.setOnClickListener(this);
        bTapping_two.setOnClickListener(this);
        bBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.bTapping_one:
                onButtonClicked_Tapping_one();
                break;
            case R.id.bTapping_two:
                onButtonClicked_Tapping_two();
                break;
            case R.id.button_back3:
                onButtonBack();
                break;
        }
    }

    private void onButtonClicked_Tapping_one() {
        Intent i =new Intent(ResultsActivity.this, Tapping_feature_Activity.class);
        startActivity(i);
    }


    private void onButtonClicked_Tapping_two() {
        Intent i =new Intent(ResultsActivity.this, Tapping_feature_Two_Activity.class);
        startActivity(i);
    }

    private void onButtonBack(){
        Intent i =new Intent(ResultsActivity.this, MainActivityMenu.class);
        startActivity(i);

    }
}
