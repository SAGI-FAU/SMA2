package com.sma2.apkinson.SendData;

import android.content.Context;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sma2.apkinson.DataAccess.MedicineDA;
import com.sma2.apkinson.DataAccess.MedicineDataService;
import com.sma2.apkinson.DataAccess.PatientDA;
import com.sma2.apkinson.DataAccess.PatientDataService;
import com.sma2.apkinson.DatabaseHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.min;

public class SendDataService {
    //public String url_base="http://192.168.1.114:8000/apkinson_mobile/";
    public String url_base="https://gita.udea.edu.co:28080/apkinson_mobile/";

    private Context invocationcontext;
    String _audioBase64;

    public SendDataService(Context context){
        this.invocationcontext = context;

    }
    private String convert_File_string(String path) {


        File file = new File(path);

        byte[] audioBytes;
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n;
            while (-1 != (n = fileInputStream.read(buf)))
                byteArrayOutputStream.write(buf, 0, n);
            audioBytes = byteArrayOutputStream.toByteArray();
            _audioBase64 = Base64.encodeToString(audioBytes, Base64.DEFAULT);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return _audioBase64;
    }

    public void loadResults(final SendDataService sds) {
        RequestQueue queue = Volley.newRequestQueue(sds.invocationcontext);

        //String url = "https://gita.udea.edu.co:28080/apkinson_mobile/CreateMedicine/";
        String url = url_base+"LoadResults/";
        Log.d("url", url);
        final String[] WER = {"0"};
        final DatabaseHelper databaseHelper = new DatabaseHelper(sds.invocationcontext);
        databaseHelper.addData("WER:"+"0");
        Toast.makeText(sds.invocationcontext, "WER not available yet", Toast.LENGTH_SHORT).show();
    }

    public void uploadMetadata(final SendDataService sds) {
        //progressBar.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(sds.invocationcontext);

        //String url = "https://gita.udea.edu.co:28080/apkinson_mobile/CreatePacient/";
        String url = url_base+"CreatePacient/";
        Log.d("url", url);

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //Toast.makeText(sds.invocationcontext, response, Toast.LENGTH_SHORT).show();
                        //progressBar.setVisibility(View.INVISIBLE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(sds.invocationcontext, "No response", Toast.LENGTH_SHORT).show();
                //progressBar.setVisibility(View.INVISIBLE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                PatientDataService PatientData = new PatientDataService(sds.invocationcontext);
                MedicineDataService MedicineData = new MedicineDataService(sds.invocationcontext);

                Long NumPatients = PatientData.countPatients();
                if (NumPatients > 0) {
                    PatientDA Patient = PatientData.getPatient();
                    params.put("name_pacient", Patient.getUsername());
                    params.put("id_name", Patient.getGovtId());
                    params.put("gender", Patient.getGender());

                    params.put("smoker", String.valueOf(Patient.getSmoker()));
                    params.put("year_diag", String.valueOf(Patient.getYear_diag()));
                    params.put("other_disorder", Patient.getOther_disorder());
                    params.put("educational_level", String.valueOf(Patient.getEducational_level()));
                    params.put("weight", String.valueOf(Patient.getWeight()));
                    params.put("height", String.valueOf(Patient.getHeight()));

                    params.put("birthday", Patient.getBirthday().toString());
                    Log.d("fecha",Patient.getBirthday().toString());


                }


                return params;
            }
        };
        //time out 10seg
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);


    }

    public void uploadMedicine(final SendDataService sds) {
        //progressBar.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(sds.invocationcontext);

        //String url = "https://gita.udea.edu.co:28080/apkinson_mobile/CreateMedicine/";
        String url = url_base+"CreateMedicine/";
        Log.d("url", url);

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //Toast.makeText(sds.invocationcontext, response, Toast.LENGTH_SHORT).show();
                        //progressBar.setVisibility(View.INVISIBLE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(sds.invocationcontext, "No response", Toast.LENGTH_SHORT).show();

                //progressBar.setVisibility(View.INVISIBLE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                PatientDataService PatientData = new PatientDataService(sds.invocationcontext);

                Long NumPatients = PatientData.countPatients();
                if (NumPatients > 0) {
                    PatientDA Patient = PatientData.getPatient();
                    params.put("id_name", Patient.getGovtId());
                    Log.d("id_name", Patient.getGovtId());
                }
                MedicineDataService MedicineData = new MedicineDataService(sds.invocationcontext);
                List<MedicineDA> Medicine = MedicineData.getAllCurrentMedictation();
                MedicineDA CurrentMed;
                params.put("number_medicine", String.valueOf(Medicine.size()));
                for (int i = 0; i < Medicine.size(); i++) {
                    CurrentMed = Medicine.get(i);

                    params.put("name_medicine" + String.valueOf(i), CurrentMed.getMedicineName());
                    params.put("dose" + String.valueOf(i), String.valueOf(CurrentMed.getDose()));
                    params.put("intaketime" + String.valueOf(i), String.valueOf(CurrentMed.getIntakeTime()));


                }


                return params;
            }
        };
        //time out 10seg
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);


    }

    public void uploadAudio(final SendDataService sds) {
        final List<String> file_aux=new ArrayList<String>();

        RequestQueue queue = Volley.newRequestQueue(sds.invocationcontext);

        //String url = "https://gita.udea.edu.co:28080/apkinson_mobile/";
        String url = url_base;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        files_send_ok(file_aux,sds);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(sds.invocationcontext, "No server response", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                PatientDataService PatientData = new PatientDataService(sds.invocationcontext);
                PatientDA Patient = PatientData.getPatient();


                String path_internal_storage = Environment.getExternalStorageDirectory().getAbsolutePath();
                File f = new File(path_internal_storage + "/Apkinson/AUDIO/");
                //int session_not_send = sesion_phone - sesion;

                File[] files = f.listFiles();



                DatabaseHelper databaseHelper = new DatabaseHelper(sds.invocationcontext);
                List<String> names = databaseHelper.loadData();

                List<String> names_to_send = new ArrayList<String>();



                external_loop: for (int i = 0; i < files.length; i++) {
                    File file = files[i];

                    for (String temp : names) {
                        if (file.getName().equals(temp)) {
                            continue external_loop;
                        }
                    }

                    names_to_send.add(file.getName());

                }
                int ValueMin = min(names_to_send.size(),5);

                for (int i = 0; i < ValueMin; i++) {
                    boolean flag = false;
                    String filename = names_to_send.get(i);
                    System.out.println("file: " + filename);
                    //Sacamos del array files un fichero
                    params.put(filename, convert_File_string(path_internal_storage + "/Apkinson/AUDIO/" + filename));
                    //databaseHelper.addData(file.getName());
                    file_aux.add(filename);
                    }
                params.put("number_session", "1");
                params.put("id_name", Patient.getGovtId());

                return params;
            }
        };

        //time out 10seg
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);



    }

    public void uploadMovement(final SendDataService sds) {


        RequestQueue queue = Volley.newRequestQueue(sds.invocationcontext);

        //String url = "https://gita.udea.edu.co:28080/apkinson_mobile/";
        String url = url_base+"UploadMovement/";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(sds.invocationcontext, "No response", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                PatientDataService PatientData = new PatientDataService(sds.invocationcontext);
                PatientDA Patient = PatientData.getPatient();


                String path_internal_storage = Environment.getExternalStorageDirectory().getAbsolutePath();
                File f = new File(path_internal_storage + "/Apkinson/MOVEMENT/");
                //int session_not_send = sesion_phone - sesion;

                File[] files = f.listFiles();

                DatabaseHelper databaseHelper = new DatabaseHelper(sds.invocationcontext);
                List<String> names = databaseHelper.loadData();

                List<String> names_to_send = new ArrayList<String>();



                external_loop: for (int i = 0; i < files.length; i++) {
                    File file = files[i];

                    for (String temp : names) {
                        if (file.getName().equals(temp)) {
                            continue external_loop;
                        }
                    }

                    names_to_send.add(file.getName());

                }
                int ValueMin = min(names_to_send.size(),10);

                for (int i = 0; i < ValueMin; i++) {
                    boolean flag = false;
                    String filename = names_to_send.get(i);
                    System.out.println("file: " + filename);
                    //Sacamos del array files un fichero
                    params.put(filename, convert_File_string(path_internal_storage + "/Apkinson/MOVEMENT/" + filename));
                    databaseHelper.addData(filename);

                }
                params.put("number_session", "1");
                params.put("id_name", Patient.getGovtId());
                //Log.d("id_name", Patient.getGovtId());

                return params;
            }
        };
        //time out 10seg
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);


    }

    public void uploadVideo(final SendDataService sds) {


        RequestQueue queue = Volley.newRequestQueue(sds.invocationcontext);

        //String url = "https://gita.udea.edu.co:28080/apkinson_mobile/";
        String url = url_base+"UploadVideo/";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(sds.invocationcontext, "No response", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                PatientDataService PatientData = new PatientDataService(sds.invocationcontext);
                PatientDA Patient = PatientData.getPatient();

                String path_internal_storage = Environment.getExternalStorageDirectory().getAbsolutePath();
                File f = new File(path_internal_storage + "/Apkinson/VIDEOS/");
                //int session_not_send = sesion_phone - sesion;

                File[] files = f.listFiles();

                DatabaseHelper databaseHelper = new DatabaseHelper(sds.invocationcontext);
                List<String> names = databaseHelper.loadData();

                List<String> names_to_send = new ArrayList<String>();



                external_loop: for (int i = 0; i < files.length; i++) {
                    File file = files[i];

                    for (String temp : names) {
                        if (file.getName().equals(temp)) {
                            continue external_loop;
                        }
                    }

                    names_to_send.add(file.getName());

                }
                int ValueMin = min(names_to_send.size(),2);

                for (int i = 0; i < ValueMin; i++) {
                    boolean flag = false;
                    String filename = names_to_send.get(i);
                    System.out.println("file: " + filename);
                    //Sacamos del array files un fichero
                    params.put(filename, convert_File_string(path_internal_storage + "/Apkinson/VIDEOS/" + filename));
                    databaseHelper.addData(filename);


                }


                params.put("number_session", "1");
                params.put("id_name", Patient.getGovtId());
                Log.d("id_name", Patient.getGovtId());

                return params;
            }
        };
        //time out 10seg
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);


    }

    public void files_send_ok(List<String> files, final SendDataService sds ){

        DatabaseHelper databaseHelper = new DatabaseHelper(sds.invocationcontext);

        for (int i = 0; i < files.size(); i++) {

            String file=files.get(i);

            databaseHelper.addData(file);
        }

    }


}


