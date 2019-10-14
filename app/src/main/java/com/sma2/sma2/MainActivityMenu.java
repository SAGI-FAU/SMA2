package com.sma2.sma2;


import android.app.ActivityManager;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sma2.sma2.DataAccess.Exercise;
import com.sma2.sma2.DataAccess.MedicineDA;
import com.sma2.sma2.DataAccess.MedicineDataService;
import com.sma2.sma2.ExerciseFragments.ExerciseFragment;
import com.sma2.sma2.ExerciseFragments.ExerciseInstructions;
import com.sma2.sma2.ExerciseList.ArrayListAdapter;

import java.io.File;
import java.util.List;

public class MainActivityMenu extends AppCompatActivity{
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
    };
    private AlertDialog noPermissionsAlertDialog;
    private ViewPager mViewPager;
    private SectionsPagerAdapter sectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(sectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(1);
        FloatingActionButton fab = findViewById(R.id.fab_settings);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_settings();
            }
        });
        noPermissionsAlertDialog= createAlertDialog();
        ask_permissions();

        // create alarm notifications to make exercises
        SharedPreferences sharedPref =PreferenceManager.getDefaultSharedPreferences(this);

        int TimeNotification=sharedPref.getInt("Notification Time", 9);
        Notifications notifications=new Notifications(this);
        notifications.setReminder(this,AlarmReceiver.class, TimeNotification, 0);


        // create alarm notifications to take the medicine
        MedicineDataService MedicineData=new MedicineDataService(this);
        List<MedicineDA> Medicine=MedicineData.getAllCurrentMedictation();
        MedicineDA CurrentMed;
        for (int i = 0; i < Medicine.size(); i++) {
            CurrentMed=Medicine.get(i);
            notifications.setReminder(this,AlarmReceiverMedicine.class, CurrentMed.getIntakeTime(), 0);
        }
    }

    public void open_settings() {
        //TODO: Implement as Dialog
        Intent intent_settings = new Intent(MainActivityMenu.this, SettingsActivity.class);
        startActivity(intent_settings);
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void ask_permissions() {
        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }

    private AlertDialog createAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set dialog message
        alertDialogBuilder
                .setTitle(getResources().getString(R.string.missing_permissions_title))
                .setMessage(getResources().getString(R.string.missing_permissions_text))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.OK),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                       noPermissionsAlertDialog.dismiss();
                    }
                });
        return alertDialogBuilder.create();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            if (position == 0){
                Results results = new Results();
                return results;
            }
            if (position == 1){
                Profile_fragment profile_fragment = new Profile_fragment();
                return profile_fragment;
            }
            if (position == 2){
                Exercise_list exercise_list = new Exercise_list();
                return exercise_list;
            }
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.btnResultsText);
                case 1:
                    return getString(R.string.btnProfileText);
                case 2:
                    return getString(R.string.btnExercisesText);
            }
            return null;
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();

    }



    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_profile_fragment, container, false);
            return rootView;
        }
    }
}
