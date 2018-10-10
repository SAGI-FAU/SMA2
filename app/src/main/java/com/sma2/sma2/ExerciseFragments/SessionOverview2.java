package com.sma2.sma2.ExerciseFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.sma2.sma2.ExerciseLogic.ScheduledExercise;
import com.sma2.sma2.R;

import java.util.ArrayList;
import java.util.List;


public class SessionOverview extends Fragment {

    private OnSessionStartListener mSessionStartClickedCallback;

    public static SessionOverview newInstance(List<ScheduledExercise> scheduledExerciseList) {
        SessionOverview fragment = new SessionOverview();
        Bundle args = new Bundle();
        args.putParcelableArrayList("SCHEDULED_EXERCISES", (ArrayList<ScheduledExercise>) scheduledExerciseList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_session_overview, container, false);

        // Set On Click handler for Start Button
        Button startButton = view.findViewById(R.id.sessionStart);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSessionStartClickedCallback != null) {
                    mSessionStartClickedCallback.onSessionStartClicked();
                }
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSessionStartListener) {
            mSessionStartClickedCallback = (OnSessionStartListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mSessionStartClickedCallback = null;
    }

    public interface OnSessionStartListener {
        void onSessionStartClicked();
    }
}


public class SessionOverviewListAdapter extends ArrayAdapter<ScheduledExercise> {

    private int resourceLayout;
    private Context mContext;

    public SessionOverviewListAdapter(Context context, int resource, List<ScheduledExercise> scheduledExerciseList) {
        super(context, resource, scheduledExerciseList);
        this.resourceLayout = resource;
        this.mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);

        ScheduledExercise ex = getItem(position);

        TextView name = (TextView) listItem.findViewById(R.id.textView_name);
        name.setText(ex.getExercise().getName());

        return listItem;
    }

    //    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        View v = convertView;
//
//        if (v == null) {
//            LayoutInflater vi;
//            vi = LayoutInflater.from(mContext);
//            v = vi.inflate(resourceLayout, null);
//        }
//
//        Item p = getItem(position);
//
//        if (p != null) {
//            TextView tt1 = (TextView) v.findViewById(R.id.id);
//            TextView tt2 = (TextView) v.findViewById(R.id.categoryId);
//            TextView tt3 = (TextView) v.findViewById(R.id.description);
//
//            if (tt1 != null) {
//                tt1.setText(p.getId());
//            }
//
//            if (tt2 != null) {
//                tt2.setText(p.getCategory().getId());
//            }
//
//            if (tt3 != null) {
//                tt3.setText(p.getDescription());
//            }
//        }
//
//        return v;
//    }

}

