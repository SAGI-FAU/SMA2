package com.sma2.apkinson.FeatureExtraction;

import android.content.Context;
import android.text.TextUtils;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

public class GetExercises {

    Context CONTEXT;
    public GetExercises(Context context){
        CONTEXT=context;

    }

    public String getNameExercise(int id) {
        char SEPARATOR = ';';
        InputStreamReader is;
        int ExID;
        try {
            is = new InputStreamReader(CONTEXT.getAssets()
                    .open("instructions.csv"), "UTF-8");
            CSVReader reader = new CSVReaderBuilder(is).build();
            CSVParser parser = new CSVParserBuilder().withSeparator(SEPARATOR).build();
            String[] languages = parser.parseLine(reader.readNext()[0]);
            int locale = getCurrentLocale(languages);
            String[] instr;
            while ((instr = reader.readNext()) != null) {
                instr = parser.parseLine(TextUtils.join("", instr));
                ExID = Integer.parseInt(instr[0]);//The ID is always on the first position CSV.
                if (ExID==id){
                    if(id <= 10){
                        return instr[locale]+"_"+languages[locale]+"1";
                    }
                    else
                        return instr[locale];
                }
            }

        }
        catch (IOException e){
            e.getMessage();
        }
        return "";

    }

    private int getCurrentLocale(String[] languages) {
        Locale locale = Locale.getDefault();
        for (int i = 0; i < languages.length; i++) {
            if (languages[i].contains(locale.getLanguage())) {
                return i;
            }
        }
        //If not found, return default
        return 0;
    }

}
