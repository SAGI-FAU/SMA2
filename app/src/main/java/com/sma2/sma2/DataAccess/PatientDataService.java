package com.sma2.sma2.DataAccess;

import android.content.Context;

import com.sma2.sma2.DataAccess.DaoMaster;
import com.sma2.sma2.DataAccess.DaoSession;
import com.sma2.sma2.R;

import org.greenrobot.greendao.database.Database;


public class PatientDataService
{

    private Context invocationcontext;
    private String dbname;
    public PatientDataService(Context context){
        this.invocationcontext = context;
        this.dbname = context.getResources().getString(R.string.databasename);
    }

    public void savePatient(PatientDA patient){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, dbname);
        Database db = helper.getWritableDb();
        DaoSession session = new DaoMaster(db).newSession();
        session.getPatientDADao().save(patient);
        db.close();
    }

    public void updatePatient(PatientDA patient){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, dbname);
        Database db = helper.getWritableDb();
        DaoSession session = new DaoMaster(db).newSession();
        session.getPatientDADao().update(patient);
        db.close();
    }

    public void deletePatient(PatientDA patient){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, dbname);
        Database db = helper.getWritableDb();
        DaoSession session = new DaoMaster(db).newSession();
        session.getPatientDADao().delete(patient);
        db.close();
    }
    public PatientDA getPatient(){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, dbname);
        Database db = helper.getReadableDb();
        DaoSession session = new DaoMaster(db).newSession();
        PatientDADao dao = session.getPatientDADao();
        PatientDA patient = dao.queryBuilder().list().get(0);

        db.close();
        return patient;
    }

    public Long countPatients(){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, dbname);
        Database db = helper.getReadableDb();
        DaoSession session = new DaoMaster(db).newSession();
        PatientDADao dao = session.getPatientDADao();
        Long N_pat = dao.queryBuilder().count();
        db.close();
        return N_pat;
    }


}
