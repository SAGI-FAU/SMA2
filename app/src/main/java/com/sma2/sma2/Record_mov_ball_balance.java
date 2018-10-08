package com.sma2.sma2;


import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;


public class Record_mov_ball_balance extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_capture_movement_game);
        setListeners();
    }

    private void setListeners() {

        final TextView mTextField = findViewById(R.id.accgraph_chrono2);
        new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                mTextField.setText(Long.toString(millisUntilFinished / 1000));
            }

            public void onFinish() {
                mTextField.setText(getApplicationContext().getString(R.string.done));

                open_exercise();
            }
        }.start();

    }


    @Override
    public void onClick(View view) {
        //TODO: onclick
    }

    public void open_exercise(){
        Intent intent_ex1;
        intent_ex1 =new Intent(this, Ex_tapping.class);
        startActivity(intent_ex1);
    }


}
