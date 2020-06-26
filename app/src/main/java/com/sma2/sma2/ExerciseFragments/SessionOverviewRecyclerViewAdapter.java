package com.sma2.sma2.ExerciseFragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sma2.sma2.DataAccess.ScheduledExercise;
import com.sma2.sma2.R;

import java.util.ArrayList;

public class SessionOverviewRecyclerViewAdapter extends RecyclerView.Adapter<SessionOverviewRecyclerViewAdapter.ViewHolder> {
    private ArrayList<ScheduledExercise> mScheduledExercises;

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
        holder.mIdView.setText(String.valueOf(position + 1));
        holder.mNameView.setText(mScheduledExercises.get(position).getExercise().getName());
        if (mScheduledExercises.get(position).getCompleted()){
            holder.mStatusView.setVisibility(View.VISIBLE);
        } else {
            holder.mStatusView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mScheduledExercises.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mNameView;
        public final CheckBox mStatusView;
        public ScheduledExercise mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = view.findViewById(R.id.itemNumber);
            mNameView = view.findViewById(R.id.exerciseName);
            mStatusView = view.findViewById(R.id.exerciseStatus);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}
