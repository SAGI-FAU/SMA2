package com.sma2.sma2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.sma2.sma2.DataAccess.ExerciseSessionManager;
import com.sma2.sma2.DataAccess.PatientDA;
import com.sma2.sma2.DataAccess.PatientDataService;
import com.sma2.sma2.DataAccess.ScheduledExercise;
import com.sma2.sma2.DataAccess.SignalDA;
import com.sma2.sma2.DataAccess.SignalDataService;
import com.sma2.sma2.ExerciseFragments.SessionOverview;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Profile_fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Profile_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile_fragment extends Fragment implements View.OnClickListener{

    private OnFragmentInteractionListener mListener;

    public Profile_fragment() {
        // Required empty public constructor
    }

    public static Profile_fragment newInstance() {
        Profile_fragment fragment = new Profile_fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_fragment, container, false);
        TextView txt_user_name = view.findViewById(R.id.profile_txt_name);
        TextView ExercisesText= view.findViewById(R.id.n_exercises);
        ProgressBar daily_progress_bar=view.findViewById(R.id.daily_progress_bar);

        PatientDataService PatientData = new PatientDataService(getActivity().getApplicationContext());
        PatientDA patient = PatientData.getPatient();
        txt_user_name.setText(patient.getUsername());
        Button btn_exercises=view.findViewById(R.id.btn_profile_quickstart);
        ImageButton btn_n_ex=view.findViewById(R.id.crown);
        btn_exercises.setOnClickListener(this);
        btn_n_ex.setOnClickListener(this);


        SignalDataService signalDataService =new SignalDataService(getActivity().getApplicationContext());
        List<SignalDA> signals= signalDataService.getAllSignals();
        int NSignals=signals.size();
        ExercisesText.setText(String.valueOf(NSignals));

        List<ScheduledExercise> list_exercises = new ArrayList<>();

        ExerciseSessionManager sessionManager = new ExerciseSessionManager();
        sessionManager.updateExerciseListFromDB(getActivity().getApplicationContext());

        list_exercises= sessionManager.getScheduledExerciseList();

        int Perc=sessionManager.getPercCompletedExercises(list_exercises, 0);

        daily_progress_bar.setProgress(Perc);
        return view;
    }




    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_profile_quickstart:
                open_exercises();
                break;
            case R.id.crown:
                open_help();
                break;

        }
    }


    void open_help(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        String Title = getResources().getString(R.string.Completed_Exercises);
        builder.setTitle(Title);

        String Text = getResources().getString(R.string.Ex_help);
        builder.setMessage(Text);

        builder.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() { // define the 'Cancel' button
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void open_exercises() {
        Intent intent_exercises = new Intent(getActivity().getApplicationContext(), ExercisesActivity.class);
        startActivity(intent_exercises);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
