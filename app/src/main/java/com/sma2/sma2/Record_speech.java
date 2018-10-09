package com.sma2.sma2;


import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.sma2.sma2.SignalRecording.SpeechRecorder;
import android.os.Handler;
import android.widget.ProgressBar;

import us.feras.mdv.MarkdownView;

public class Record_speech extends AppCompatActivity implements View.OnClickListener {
    private Intent intent_prev;
    private String Exercise;

    private SpeechRecorder recorder;
    private static ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_capture_speech_all_tasks);
        setListeners();

        bar = findViewById(R.id.progressvolume1);

        Handler handler_volume=new Volume_Handler();

        recorder= SpeechRecorder.getInstance(this, handler_volume);


        String error_rec;

        intent_prev=getIntent();
        Exercise = intent_prev.getStringExtra("EXERCISE");

        recorder.prepare(Exercise);
        error_rec=recorder.record();

    }

    private void setListeners() {
        findViewById(R.id.button_continue4).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_continue4:
                //TODO: stop speech recording
                recorder.stopRecording();
                //recorder.release();
                open_exercise();
                break;
        }

    }

    public void open_exercise(){

        intent_prev=getIntent();
        Exercise = intent_prev.getStringExtra("EXERCISE");
        Intent intent_ex1;
        if (Exercise.equals("a")){
            intent_ex1 =new Intent(this, Ex_speech1.class);
            startActivity(intent_ex1);
        }
        else if (Exercise.equals("ddk")){
            intent_ex1 =new Intent(this, Ex_speech2.class);
            startActivity(intent_ex1);
        }
        else{
            intent_ex1 =new Intent(this, Ex_speech3.class);
            startActivity(intent_ex1);
        }
    }

    private static class Volume_Handler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final int volume = (int) msg.getData().getDouble("Volume");
            bar.setProgress(volume);
        }
    }

}



