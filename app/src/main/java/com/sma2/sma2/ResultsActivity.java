package com.sma2.sma2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


import com.sma2.sma2.FeatureExtraction.Movement.BalanceTremorFeatureActivity;
import com.sma2.sma2.FeatureExtraction.Movement.PosturalTremor_feature_Activity;
import com.sma2.sma2.FeatureExtraction.Speech.Speech_features_Activity;
import com.sma2.sma2.FeatureExtraction.Tapping.Tapping_feature_Activity;


public class ResultsActivity extends AppCompatActivity implements View.OnClickListener {
    ImageButton bTapping_one, bPosturalTremor, bSpeech, bBalance;
    Button bBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        bTapping_one=findViewById(R.id.bTapping_one);
        bBack=findViewById(R.id.button_back3);
        bSpeech=findViewById(R.id.bSpeech);
        bPosturalTremor=findViewById(R.id.bPosturalTremor);
        bBalance=findViewById(R.id.bBalance);
        setListeners();
    }


    private void setListeners() {
        bTapping_one.setOnClickListener(this);
        bBack.setOnClickListener(this);
        bSpeech.setOnClickListener(this);
        bPosturalTremor.setOnClickListener(this);
        bBalance.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.bSpeech:
                onButtonClicked_Speech();
                break;
            case R.id.bTapping_one:
                onButtonClicked_Tapping_one();
                break;
            case R.id.bPosturalTremor:
                onButtonClicked_bPosturalTremor();
                break;
            case R.id.bBalance:
                onButtonClicked_bBalance();
                break;

            case R.id.button_back3:
                onButtonBack();
                break;

        }
    }

    private void onButtonClicked_Speech() {
        Intent i =new Intent(ResultsActivity.this, Speech_features_Activity.class);
        startActivity(i);
    }

    private void onButtonClicked_Tapping_one() {
        Intent i =new Intent(ResultsActivity.this, Tapping_feature_Activity.class);
        startActivity(i);
    }

    private void onButtonClicked_bPosturalTremor() {
        Intent i =new Intent(ResultsActivity.this, PosturalTremor_feature_Activity.class);
        startActivity(i);
    }


    private void onButtonClicked_bBalance() {
        Intent i =new Intent(ResultsActivity.this, BalanceTremorFeatureActivity.class);
        startActivity(i);
    }


    private void onButtonBack(){
        Intent i =new Intent(ResultsActivity.this, MainActivityMenu.class);
        startActivity(i);

    }
}
