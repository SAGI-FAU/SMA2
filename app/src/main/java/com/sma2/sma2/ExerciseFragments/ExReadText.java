package com.sma2.sma2.ExerciseFragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.sma2.sma2.R;
import com.sma2.sma2.SignalRecording.SpeechRecorder;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;


public class ExReadText extends ExerciseFragment implements ButtonFragment.OnButtonInteractionListener {
    private SpeechRecorder recorder;
    private ProgressBar volumeBar;
    private TextExercise textExercise;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ex_read_text, container, false);
        volumeBar = view.findViewById(R.id.volumeExRT);
        recorder = SpeechRecorder.getInstance(getActivity().getApplicationContext(), new VolumeHandler(volumeBar));
        TextView text = view.findViewById(R.id.txtItemRT);
        textExercise = null;
        try {
            textExercise = loadText();
        } catch (IOException e){
            Log.e("CSV_READING", e.toString());
        }
        text.setText(textExercise.getText());
        ButtonFragment buttonFragment = new ButtonFragment();
        buttonFragment.setmListener(this);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        transaction.replace(R.id.frameExRT, buttonFragment);
        transaction.commit();
        return view;
    }

    private TextExercise loadText() throws IOException {
        InputStreamReader is;
        is = new InputStreamReader(getActivity().getAssets()
                    .open("textExercises.csv"), "UTF-8");
        CSVReader reader = new CSVReaderBuilder(is).build();
        CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
        String[] languages = parser.parseLine(reader.readNext()[0]);
        String[] line;
        List<TextExercise> exercises = new ArrayList<>();
        int locale = getCurrentLocale(languages);
        while ((line = reader.readNext()) != null){
            line = parser.parseLine(TextUtils.join("",line));
            exercises.add(new TextExercise(line[locale], languages[locale], 1));

        }
        Random rand = new Random();
        return exercises.get(rand.nextInt(exercises.size()));
    }

    private int getCurrentLocale(String[] languages) {
        Locale locale = Locale.getDefault();
        for (int i = 0; i < languages.length; i++){
            if (languages[i].contains(locale.getLanguage())){
                return i;
            }
        }
        //If not found, return default
        return 0;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        recorder.release();
        mListener = null;
    }


    @Override
    public void onButtonInteraction(boolean start) {
        if(start){
            filePath = recorder.prepare(mExercise.getName() + "_"
                    + textExercise.getLanguage() + textExercise.getID());
            recorder.record();
            recording = true;
        } else {
            if (!recording){
                return;
            }
            recorder.stopRecording();
            recording = false;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    volumeBar.setProgress(0);
                }
            });
            mListener.onExerciseFinished(filePath);
        }
    }

    private static class VolumeHandler extends Handler {
        ProgressBar volumeBar;

        public VolumeHandler(ProgressBar bar){
            volumeBar = bar;
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            final int currentVolume = (int) bundle.getDouble("Volume");
            post(new Runnable() {
                @Override
                public void run() {
                    volumeBar.setProgress(currentVolume);
                }
            });
        }
    }

    class TextExercise{
        private String text;
        private String language;
        private String ID;

        public TextExercise(String text, String language, int id){
            this.text = text;
            this.language = language;
            this.ID = String.valueOf(id);
        }

        public String getID() {
            return ID;
        }

        public String getText() {
            return text;
        }

        public String getLanguage() {
            return language;
        }
    }
}
