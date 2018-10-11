package com.sma2.sma2.DataAccess;

import android.content.Context;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.QueryBuilder;

public class PatientDataService
{
    private class Patient{
        //todo in real patient class


    }
    private Context invocationcontext;
    public PatientDataService(Context context){
        this.invocationcontext = context;
    }

    public void savePatient(Patient patient){
//        PatientDA tmp = new PatientDA(patient); todo change the real thing
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, "apkinsondb");
        Database db = helper.getWritableDb();
        DaoSession session = new DaoMaster(db).newSession();
        session.getPatientDADao().save(new PatientDA());//todo change the real thing
        db.close();
    }

    public void updatePatient(PatientDA patient){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, "apkinsondb");
        Database db = helper.getWritableDb();
        DaoSession session = new DaoMaster(db).newSession();
        session.getPatientDADao().update(patient);
        db.close();
    }

    public void deletePatient(PatientDA patient){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, "apkinsondb");
        Database db = helper.getWritableDb();
        DaoSession session = new DaoMaster(db).newSession();
        session.getPatientDADao().delete(patient);
        db.close();
    }
    public Patient getPatient(){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, "apkinsondb");
        Database db = helper.getReadableDb();
        DaoSession session = new DaoMaster(db).newSession();
        PatientDADao dao = session.getPatientDADao();
        PatientDA patient = dao.queryBuilder().list().get(0);
        db.close();
        return new Patient();//todo
    }


}
