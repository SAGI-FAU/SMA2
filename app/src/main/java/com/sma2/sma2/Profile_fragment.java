package com.sma2.sma2;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sma2.sma2.DataAccess.PatientDA;
import com.sma2.sma2.DataAccess.PatientDataService;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Profile_fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Profile_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile_fragment extends Fragment {

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
        PatientDataService PatientData = new PatientDataService(getActivity().getApplicationContext());
        PatientDA patient = PatientData.getPatient();
        txt_user_name.setText(patient.getUsername());
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
