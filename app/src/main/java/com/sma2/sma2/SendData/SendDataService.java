package com.sma2.sma2.SendData;

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
import com.sma2.sma2.DataAccess.MedicineDA;
import com.sma2.sma2.DataAccess.MedicineDataService;
import com.sma2.sma2.DataAccess.PatientDA;
import com.sma2.sma2.DataAccess.PatientDataService;
import com.sma2.sma2.DatabaseHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SendDataService {
    private Context invocationcontext;
    String _audioBase64;

    public SendDataService(Context context){
        this.invocationcontext = context;

    }
    private String convert_File_string(String path) {


        File file = new File(path);
        //Toast.makeText(MainActivity.this,path,Toast.LENGTH_SHORT).show();
        Log.d("hola", path);

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

    public void uploadMetadata(final SendDataService sds) {
        //progressBar.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(sds.invocationcontext);

        //String url = "https://gita.udea.edu.co:28080/apkinson_mobile/CreatePacient/";
        String url = "http://192.168.1.206:8000/apkinson_mobile/CreatePacient/";
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
        String url = "http://192.168.1.206:8000/apkinson_mobile/CreateMedicine/";
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


        RequestQueue queue = Volley.newRequestQueue(sds.invocationcontext);

        //String url = "https://gita.udea.edu.co:28080/apkinson_mobile/";
        String url = "http://192.168.1.206:8000/apkinson_mobile/";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(sds.invocationcontext, "No response", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                PatientDataService PatientData = new PatientDataService(sds.invocationcontext);
                PatientDA Patient = PatientData.getPatient();


                String path_internal_storage = Environment.getExternalStorageDirectory().getAbsolutePath();
                Log.d("direccion", path_internal_storage);
                File f = new File(path_internal_storage + "/Apkinson/AUDIO/");
                //int session_not_send = sesion_phone - sesion;

                File[] files = f.listFiles();

                DatabaseHelper databaseHelper = new DatabaseHelper(sds.invocationcontext);



                for (int i = 0; i < files.length; i++) {
                    boolean flag = false;
                    File file = files[i];
                    System.out.println("file: " + file.getName());
                    //Sacamos del array files un fichero
                    List<String> names = databaseHelper.loadData();
                    if (names.size() == 0) {
                        params.put(file.getName(), convert_File_string(path_internal_storage + "/Apkinson/AUDIO/" + file.getName()));
                        databaseHelper.addData(file.getName());
                    } else {
                        for (String temp : names) {
                            if (file.getName().equals(temp)) {

                                System.out.println("Debug: Existe");
                                System.out.println("Ya esta: " + file.getName());
                                flag = true;
                                break;
                            }

                        }
                        if (!flag) {
                            params.put(file.getName(), convert_File_string(path_internal_storage + "/Apkinson/AUDIO/" + file.getName()));
                            databaseHelper.addData(file.getName());
                            System.out.println("Debug: Super");
                            System.out.println("Agregado: " + file.getName());

                        }


                    }


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

    public void uploadMovement(final SendDataService sds) {


        RequestQueue queue = Volley.newRequestQueue(sds.invocationcontext);

        //String url = "https://gita.udea.edu.co:28080/apkinson_mobile/";
        String url = "http://192.168.1.206:8000/apkinson_mobile/UploadMovement/";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(sds.invocationcontext, "No response", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                PatientDataService PatientData = new PatientDataService(sds.invocationcontext);
                PatientDA Patient = PatientData.getPatient();


                String path_internal_storage = Environment.getExternalStorageDirectory().getAbsolutePath();
                Log.d("direccion", path_internal_storage);
                File f = new File(path_internal_storage + "/Apkinson/MOVEMENT/");
                //int session_not_send = sesion_phone - sesion;

                File[] files = f.listFiles();

                DatabaseHelper databaseHelper = new DatabaseHelper(sds.invocationcontext);



                for (int i = 0; i < files.length; i++) {
                    boolean flag = false;
                    File file = files[i];
                    System.out.println("file: " + file.getName());
                    //Sacamos del array files un fichero
                    List<String> names = databaseHelper.loadData();
                    if (names.size() == 0) {
                        params.put(file.getName(), convert_File_string(path_internal_storage + "/Apkinson/MOVEMENT/" + file.getName()));
                        databaseHelper.addData(file.getName());
                    } else {
                        for (String temp : names) {
                            if (file.getName().equals(temp)) {

                                System.out.println("Debug: Existe");
                                System.out.println("Ya esta: " + file.getName());
                                flag = true;
                                break;
                            }

                        }
                        if (!flag) {
                            params.put(file.getName(), convert_File_string(path_internal_storage + "/Apkinson/MOVEMENT/" + file.getName()));
                            databaseHelper.addData(file.getName());
                            System.out.println("Debug: Super");
                            System.out.println("Agregado: " + file.getName());

                        }


                    }


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

    public void uploadVideo(final SendDataService sds) {


        RequestQueue queue = Volley.newRequestQueue(sds.invocationcontext);

        //String url = "https://gita.udea.edu.co:28080/apkinson_mobile/";
        String url = "http://192.168.1.206:8000/apkinson_mobile/UploadVideo/";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(sds.invocationcontext, "No response", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                PatientDataService PatientData = new PatientDataService(sds.invocationcontext);
                PatientDA Patient = PatientData.getPatient();


                String path_internal_storage = Environment.getExternalStorageDirectory().getAbsolutePath();
                Log.d("direccion", path_internal_storage);
                File f = new File(path_internal_storage + "/Apkinson/VIDEOS/");
                //int session_not_send = sesion_phone - sesion;

                File[] files = f.listFiles();

                DatabaseHelper databaseHelper = new DatabaseHelper(sds.invocationcontext);



                for (int i = 0; i < files.length; i++) {
                    boolean flag = false;
                    File file = files[i];
                    System.out.println("file: " + file.getName());
                    //Sacamos del array files un fichero
                    List<String> names = databaseHelper.loadData();
                    if (names.size() == 0) {
                        params.put(file.getName(), convert_File_string(path_internal_storage + "/Apkinson/VIDEOS/" + file.getName()));
                        databaseHelper.addData(file.getName());
                    } else {
                        for (String temp : names) {
                            if (file.getName().equals(temp)) {

                                System.out.println("Debug: Existe");
                                System.out.println("Ya esta: " + file.getName());
                                flag = true;
                                break;
                            }

                        }
                        if (!flag) {
                            params.put(file.getName(), convert_File_string(path_internal_storage + "/Apkinson/VIDEOS/" + file.getName()));
                            databaseHelper.addData(file.getName());
                            System.out.println("Debug: Super");
                            System.out.println("Agregado: " + file.getName());

                        }


                    }


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
}


