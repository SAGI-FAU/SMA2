package com.sma2.sma2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sma2.sma2.DataAccess.PatientDA;
import com.sma2.sma2.DataAccess.PatientDataService;

public class Profile3Activity extends AppCompatActivity implements View.OnClickListener {

    PatientDA patientData;
    float weight;
    int height;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile3);
        patientData = (PatientDA) getIntent().getSerializableExtra("PatientData");
        findViewById(R.id.button_menu_settings).setOnClickListener(this);
        findViewById(R.id.button_back2).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_back2:
                onBackPressed();
                break;
            case R.id.button_menu_settings:
                if(valid_data()){
                    patientData.setOther_disorder(((TextView)findViewById(R.id.other_disorder)).getText().toString());
                    SharedPreferences prefs = getSharedPreferences("LoginPref",this.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("UserCreated",1);
                    editor.commit();
                    PatientDataService pds = new PatientDataService(getApplicationContext());
                    pds.savePatient(patientData);
                    Intent intent = new Intent(Profile3Activity.this,MainActivityMenu.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
        }
    }

    private boolean valid_data() {
        String weight = ((TextView) findViewById(R.id.weight)).getText().toString();
        String height = ((TextView) findViewById(R.id.height)).getText().toString();
        if(weight.isEmpty()){
            Toast.makeText(this,R.string.weight_error,Toast.LENGTH_SHORT).show();
            return false;
        }else if(height.isEmpty()){
            Toast.makeText(this,R.string.height_eror,Toast.LENGTH_SHORT).show();
            return false;
        }else{
            patientData.setWeight(Float.valueOf(weight));
            patientData.setHeight(Integer.valueOf(height));
            return true;
        }
    }
}
