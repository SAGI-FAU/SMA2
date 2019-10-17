package com.sma2.sma2.ExerciseFragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.sma2.sma2.DataAccess.FeatureDataService;
import com.sma2.sma2.FeatureExtraction.Speech.features.RadarFeatures;
import com.sma2.sma2.R;
import com.sma2.sma2.SignalRecording.SpeechRecorder;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;


public class ExReadText extends ExerciseFragment implements ButtonFragment.OnButtonInteractionListener {
    private SpeechRecorder recorder;
    private ProgressBar volumeBar;
    private TextExercise textExercise;
    private int Sentence;
    SharedPreferences sharedPref;
    FeatureDataService FeatureDataService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ex_read_text, container, false);
        volumeBar = view.findViewById(R.id.volumeExRT);
        recorder = SpeechRecorder.getInstance(getActivity().getApplicationContext(), new VolumeHandler(volumeBar));
        TextView text = view.findViewById(R.id.txtItemRT);
        textExercise = null;
        sharedPref =PreferenceManager.getDefaultSharedPreferences(getActivity());

        Sentence =getArguments().getInt("sentence");
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
        FeatureDataService=new FeatureDataService(getActivity().getApplicationContext());

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

        return exercises.get(Sentence);
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

    private void  evaluate_features(){
        if (mExercise.getId()==7){ // Compute intonation from longest sentence.
            float int_f0 = RadarFeatures.intonation(filePath);
            File file = new File(filePath);
            Date lastModDate = new Date(file.lastModified());
            FeatureDataService.save_feature(FeatureDataService.intonation_name, lastModDate, int_f0);

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("New Area Speech", true);
            editor.apply();

        }


    }
    private class VolumeHandler extends Handler {
        ProgressBar volumeBar;

        public VolumeHandler(ProgressBar bar){
            volumeBar = bar;
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            final int currentVolume = (int) bundle.getDouble("Volume");

            final String state = bundle.getString("State", "Empty");
            if (state.equals("Finished")){

                evaluate_features();

            }

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
