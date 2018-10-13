package com.sma2.sma2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {


    private RadioButton rbEnglish, rbGerman, rbSpanish;
    Intent intent = getIntent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
        setListeners();
    }

    private void setListeners() {
        findViewById(R.id.button_update_medicine).setOnClickListener(this);
        findViewById(R.id.button_menu_settings).setOnClickListener(this);
        rbEnglish= findViewById(R.id.rbEnglish);
        rbGerman = findViewById(R.id.rbGerman);
        rbSpanish= findViewById(R.id.rbSpanish);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_update_medicine:
                //TODO: Implement transition update medicine activity
                break;
            case R.id.button_menu_settings:
                open_main();
                break;
        }


        if(rbEnglish.isChecked()){
            //TODO: Change the app language to English
        }else if (rbGerman.isChecked()) {
            //TODO: Change the app language to German
        }
        else{
            //TODO: Change the app language to Spanish
        }

    }

    public void open_main(){
        Intent intent_main =new Intent(this, MainActivityMenu.class);
        startActivity(intent_main);
    }


}
