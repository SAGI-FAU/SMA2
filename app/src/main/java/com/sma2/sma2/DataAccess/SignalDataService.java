package com.sma2.sma2.DataAccess;

import android.content.Context;

import com.sma2.sma2.DataAccess.DaoMaster;
import com.sma2.sma2.DataAccess.DaoSession;
import com.sma2.sma2.R;

import org.greenrobot.greendao.database.Database;

import java.util.List;

public class SignalDataService {
    private Database db;
    private String dbname;
    private Context invocationcontext;
    public SignalDataService(Context context){
        this.db=null;
        this.dbname = context.getResources().getString(R.string.databasename);
        this.invocationcontext = context;
    }

    public void saveSignal(SignalDA tmp){
        if (db == null) {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, dbname);
            db = helper.getWritableDb();
        }
        DaoSession session = new DaoMaster(db).newSession();
        session.getSignalDADao().save(tmp);//todo change the real thing
        // db.close();
    }

    public void updateSignal(SignalDA signal){
        if (db == null) {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, dbname);
            db = helper.getWritableDb();
        }
        DaoSession session = new DaoMaster(db).newSession();
        session.getSignalDADao().update(signal);
        // db.close();
    }

    public void deleteSignal(SignalDA signal){
        if (db == null) {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, dbname);
            db = helper.getWritableDb();
        }
        DaoSession session = new DaoMaster(db).newSession();
        session.getSignalDADao().delete(signal);
        // db.close();
    }
    public SignalDA getSignal(Long id){
        if (db == null) {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, dbname);
            db = helper.getWritableDb();
        }
        DaoSession session = new DaoMaster(db).newSession();
        SignalDADao dao = session.getSignalDADao();
        SignalDA signal = dao.queryBuilder()
                .where(SignalDADao.Properties.Id.eq(id))
                .list().get(0);
        // db.close();
        return signal;
    }


    public List<SignalDA> getAllSignals(){
        if (db == null) {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, dbname);
            db = helper.getWritableDb();
        }
        DaoSession session = new DaoMaster(db).newSession();
        SignalDADao dao = session.getSignalDADao();
        List<SignalDA> signals = dao.queryBuilder()
                .list();
        // db.close();
        return signals;

    }

    public List<SignalDA> getSignalsbyID(int ID){
        if (db == null) {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, dbname);
            db = helper.getWritableDb();
        }
        DaoSession session = new DaoMaster(db).newSession();
        SignalDADao dao = session.getSignalDADao();
        List<SignalDA> signals = dao.queryBuilder()
                .where(SignalDADao.Properties.ExerciseID.eq(ID))
                .list();
        // db.close();
        return signals;

    }

    public long countSignalsbyID(int ID){
        if (db == null) {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, dbname);
            db = helper.getWritableDb();
        }
        DaoSession session = new DaoMaster(db).newSession();
        SignalDADao dao = session.getSignalDADao();
        // db.close();
        return dao.queryBuilder()
                .where(SignalDADao.Properties.ExerciseID.eq(ID))
                .count();
    }

}
