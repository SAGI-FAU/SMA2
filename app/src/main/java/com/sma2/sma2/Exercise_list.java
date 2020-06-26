package com.sma2.sma2;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.sma2.sma2.DataAccess.ExerciseSessionManager;

import com.sma2.sma2.DataAccess.Exercise;
import com.sma2.sma2.ExerciseList.ArrayListAdapter;

import java.io.IOException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Exercise_list.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Exercise_list#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Exercise_list extends Fragment {
    private OnFragmentInteractionListener mListener;

    public Exercise_list() {
        // Required empty public constructor
    }

    public static Exercise_list newInstance() {
        Exercise_list fragment = new Exercise_list();
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
        View view = inflater.inflate(R.layout.fragment_exercise_list, container, false);
        ListView exercise_listview = view.findViewById(R.id.exercise_listview);
        ArrayList<Exercise> exercises = null;
        try {
            exercises = ExerciseSessionManager.getExerciseList(getActivity().getApplicationContext());
        } catch (IOException e){
            Log.e("LOADING EXERCISES", e.getMessage());
        }
        ArrayListAdapter adapter = new ArrayListAdapter(getContext(), exercises, getActivity());
        exercise_listview.setAdapter(adapter);
        return view;
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
