package com.sma2.sma2;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.sma2.sma2.SignalRecording.SpeechRecorder;

public class Record_speech_image extends AppCompatActivity implements View.OnClickListener {

    private Intent intent_prev;
    private String Exercise;

    private SpeechRecorder recorder;
    private static ProgressBar bar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_speech_image);
        setListeners();
        bar2 = findViewById(R.id.progressvolume2);
        Handler handler_volume2=new Volume_Handler();
        recorder= SpeechRecorder.getInstance(this, handler_volume2);
        String error_rec;
        intent_prev=getIntent();
        Exercise = intent_prev.getStringExtra("EXERCISE");
        recorder.prepare(Exercise);
        error_rec=recorder.record();
    }

    private void setListeners() {
        findViewById(R.id.button_continue_image).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_continue_image:
                recorder.stopRecording();
                open_exercise();
                break;
        }

    }

    public void open_exercise(){
        Intent intent_ex1 =new Intent(this, Ex_walking.class);
        startActivity(intent_ex1);
    }

    private static class Volume_Handler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final int volume = (int) msg.getData().getDouble("Volume");
            bar2.setProgress(volume);
        }
    }
}
