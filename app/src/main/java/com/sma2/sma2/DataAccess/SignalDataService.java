package com.sma2.sma2.DataAccess;

import android.content.Context;

import org.greenrobot.greendao.database.Database;

public class SignalDataService {
    private Database db;
    private Context invocationcontext;
    public SignalDataService(Context context){
        this.db=null;
        this.invocationcontext = context;
    }

    public void saveSignal(SignalDA tmp){
        if (db == null) {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, "apkinsondb");
            Database db = helper.getWritableDb();
        }
        DaoSession session = new DaoMaster(db).newSession();
        session.getSignalDADao().save(tmp);//todo change the real thing
        // db.close();
    }

    public void updateSignal(SignalDA signal){
        if (db == null) {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, "apkinsondb");
            Database db = helper.getWritableDb();
        }
        DaoSession session = new DaoMaster(db).newSession();
        session.getSignalDADao().update(signal);
        // db.close();
    }

    public void deleteSignal(SignalDA signal){
        if (db == null) {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, "apkinsondb");
            Database db = helper.getWritableDb();
        }
        DaoSession session = new DaoMaster(db).newSession();
        session.getSignalDADao().delete(signal);
        // db.close();
    }
    public SignalDA getSignal(Long id){
        if (db == null) {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, "apkinsondb");
            Database db = helper.getWritableDb();
        }
        DaoSession session = new DaoMaster(db).newSession();
        SignalDADao dao = session.getSignalDADao();
        SignalDA signal = dao.queryBuilder()
                .where(SignalDADao.Properties.Id.eq(id))
                .list().get(0);
        // db.close();
        return signal;
    }
}
