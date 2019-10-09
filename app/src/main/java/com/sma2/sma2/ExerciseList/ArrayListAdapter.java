package com.sma2.sma2.ExerciseList;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sma2.sma2.DataAccess.Exercise;
import com.sma2.sma2.R;

import java.util.ArrayList;

/**
 * Created by Philipp on 18.08.2016.
 */
public class ArrayListAdapter extends ArrayAdapter<Exercise> {
    private Context context;
    private ArrayList<Exercise> exercises;
    MediaPlayer player;
    View playerView;
    Activity activity;


    public ArrayListAdapter(Context context, ArrayList<Exercise> exercises, Activity activity) {
        super(context, -1, exercises);
        this.context = context;
        this.exercises = exercises;
        this.activity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listItemView = inflater.inflate(R.layout.ex_list_item, parent, false);
        TextView ex_title = listItemView.findViewById(R.id.list_ex_name);
        TextView ex_description = listItemView.findViewById(R.id.list_ex_desc);
        ImageView ex_icon = listItemView.findViewById(R.id.ex_icon_list);
        ex_title.setText(exercises.get(position).getName());
        ex_description.setText(exercises.get(position).getShortDescription());
        if (exercises.get(position).getExerciseType().equals("Speech")) {
            ex_icon.setImageResource(R.drawable.speech_results);
        } else if (exercises.get(position).getExerciseType().equals("Movement")){
            ex_icon.setImageResource(R.drawable.walking_icon);
        } else {
            ex_icon.setImageResource(R.drawable.bug);
        }
        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:Start Exercise here
            }
        });
        return listItemView;
    }
}