package com.sma2.sma2.ExerciseFragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sma2.sma2.ExerciseLogic.ScheduledExercise;
import com.sma2.sma2.R;

import java.util.ArrayList;

public class SessionOverviewRecyclerViewAdapter extends RecyclerView.Adapter<SessionOverviewRecyclerViewAdapter.ViewHolder> {
    ArrayList<ScheduledExercise> mScheduledExercises;

    public SessionOverviewRecyclerViewAdapter(ArrayList<ScheduledExercise> scheduledExercises) {
        mScheduledExercises = scheduledExercises;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_scheduled_exercise_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mScheduledExercises.get(position);
        holder.mNameView.setText(mScheduledExercises.get(position).getExercise().getName());
    }

    @Override
    public int getItemCount() {
        return mScheduledExercises.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mNameView;
        public ScheduledExercise mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = view.findViewById(R.id.item_number);
            mNameView = view.findViewById(R.id.exerciseName);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}
