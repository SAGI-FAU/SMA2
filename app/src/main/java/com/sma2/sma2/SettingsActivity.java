package com.sma2.sma2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.sma2.sma2.DataAccess.MedicineDA;
import com.sma2.sma2.DataAccess.MedicineDataService;
import com.sma2.sma2.DataAccess.PatientDA;
import com.sma2.sma2.DataAccess.PatientDataService;
import com.sma2.sma2.SignalRecording.CSVFileWriter;

import java.io.IOException;
import java.text.DateFormat;
import java.util.List;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {


    Intent intent = getIntent();
    Spinner SpinnerNotify;
    int TimeNotification;
    PatientDA patientData;
    PatientDataService PDS;
    SharedPreferences sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
        PDS= new PatientDataService(this);
        patientData = PDS.getPatient();



        setListeners();
    }

    private void setListeners() {
        findViewById(R.id.button_update_profile).setOnClickListener(this);
        findViewById(R.id.button_update_medicine).setOnClickListener(this);
        findViewById(R.id.button_menu_settings).setOnClickListener(this);
        findViewById(R.id.button_export_metadata).setOnClickListener(this);

        SpinnerNotify = findViewById(R.id.SpinnerNotifications);
        String[] hours = new String[]{"00:00","01:00","02:00","03:00","04:00","05:00","06:00","07:00","08:00","09:00","10:00","11:00","12:00","13:00","14:00","15:00","16:00","17:00","18:00","19:00","20:00","21:00","22:00","23:00"};
        ArrayAdapter<String> notify_adapter =  new ArrayAdapter<> (this,android.R.layout.simple_spinner_item,hours);
        notify_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerNotify.setAdapter(notify_adapter);

        sharedPref =this.getPreferences(Context.MODE_PRIVATE);
        TimeNotification=sharedPref.getInt("Notification Time", 9);


        if (TimeNotification>=0){
            SpinnerNotify.setSelection(TimeNotification);
        }

        SpinnerNotify.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TimeNotification = i;

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("Notification Time", TimeNotification);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Notifications notifications=new Notifications(this);
        notifications.cancelReminder(this,AlarmReceiver.class);
        notifications.setReminder(this, AlarmReceiver.class, TimeNotification-1, 0);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_update_medicine:
                open_update();
                break;
            case R.id.button_menu_settings:
                open_main();
                break;
            case R.id.button_update_profile:
                open_profile();
                break;
            case R.id.button_export_metadata:
                try {
                    export_profile();
                    export_medicine();
                    Toast.makeText(this,R.string.export_sucess,Toast.LENGTH_SHORT).show();
                }catch (Exception e) {
                    Toast.makeText(this,R.string.export_failed,Toast.LENGTH_SHORT).show();
                    Log.e("MetadataExport", e.toString());

                }
                break;
        }

    }

    public void open_main(){
        Intent intent_main =new Intent(this, MainActivityMenu.class);
        startActivity(intent_main);
    }

    private void open_update() {
        Intent intent_update =new Intent(this, UpdateMedicine.class);
        startActivity(intent_update);
    }

    private void open_profile() {

        Intent intent_update =new Intent(this, Profile1Activity.class);
        intent_update.putExtra("PatientData", patientData);
        startActivity(intent_update);
    }



    private void export_profile()  throws IOException {

        PatientDataService patientDataService=new PatientDataService(this);
        PatientDA patient=patientDataService.getPatient();
        String PATH = Environment.getExternalStorageDirectory() + "/Apkinson/METADATA/";
        CSVFileWriter mCSVFileWriter = new CSVFileWriter("Profile", PATH);

        String[] Name={"Name", patient.getUsername()};
        String[] Birthday={"Birthday", DateFormat.getDateInstance().format(patient.getBirthday())};
        String[] ID={"Telephone-Number", String.valueOf(patient.getGovtId())};
        String[] Gender={"Gender", patient.getGender()};
        String[] Hand={"Dominant hand", String.valueOf(patient.getHand())};
        String[] Smoker={"Smoker", String.valueOf(patient.getSmoker())};
        String[] Education={"Education Level", String.valueOf(patient.getEducational_level())};
        String[] YearDiag={"Year of Diagnosis", String.valueOf(patient.getYear_diag())};
        String[] Other={"Other disorders", patient.getOther_disorder()};
        String[] Weight={"Weight (kg)", String.valueOf(patient.getWeight())};
        String[] Height={"Height (cm)", String.valueOf(patient.getHeight())};
        mCSVFileWriter.write(Name);
        mCSVFileWriter.write(Birthday);
        mCSVFileWriter.write(ID);
        mCSVFileWriter.write(Gender);
        mCSVFileWriter.write(Hand);
        mCSVFileWriter.write(Smoker);
        mCSVFileWriter.write(Education);
        mCSVFileWriter.write(YearDiag);
        mCSVFileWriter.write(Other);
        mCSVFileWriter.write(Weight);
        mCSVFileWriter.write(Height);
        mCSVFileWriter.close();

    }



    private void export_medicine() throws IOException {

        String PATH = Environment.getExternalStorageDirectory() + "/Apkinson/METADATA/";
        CSVFileWriter mCSVFileWriter = new CSVFileWriter("Medicine", PATH);
        MedicineDataService medicineDataService=new MedicineDataService(this);
        List<MedicineDA> ListMedicine= medicineDataService.getAllMedictation();

        String[] header={"Medicine name", "Dose (mg)", "Intake time", "Deleted"};
        mCSVFileWriter.writeData(header);

        String name;
        String dose;
        String time;
        String deleted;
        String[] row_med={"","","",""};
        MedicineDA current_med;
        for (int i=0;i<ListMedicine.size();i++){
            current_med=ListMedicine.get(i);

            name=current_med.getMedicineName();
            dose=String.valueOf(current_med.getDose());
            time=String.valueOf(current_med.getIntakeTime());
            deleted=String.valueOf(current_med.getDeleted());
            row_med[0]=name;
            row_med[1]=dose;
            row_med[2]=time;
            row_med[3]=deleted;
            mCSVFileWriter.write(row_med);
        }
        mCSVFileWriter.close();


    }
}
