package com.sma2.sma2.DataAccess;

import android.content.Context;

import org.greenrobot.greendao.database.Database;

public class SignalDataService {
    private Context invocationcontext;
    public SignalDataService(Context context){
        this.invocationcontext = context;
    }

    public void saveSignal(){
//        SignalDA tmp = new SignalDA(signal); todo change the real thing
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, "apkinsondb");
        Database db = helper.getWritableDb();
        DaoSession session = new DaoMaster(db).newSession();
//        session.getSignalDADao().save(tmp);//todo change the real thing
        db.close();
    }

    public void updateSignal(SignalDA signal){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, "apkinsondb");
        Database db = helper.getWritableDb();
        DaoSession session = new DaoMaster(db).newSession();
        session.getSignalDADao().update(signal);
        db.close();
    }

    public void deleteSignal(SignalDA signal){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, "apkinsondb");
        Database db = helper.getWritableDb();
        DaoSession session = new DaoMaster(db).newSession();
        session.getSignalDADao().delete(signal);
        db.close();
    }
//    public SignalDataService.Signal getSignal(){
//        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, "apkinsondb");
//        Database db = helper.getReadableDb();
//        DaoSession session = new DaoMaster(db).newSession();
//        SignalDADao dao = session.getSignalDADao();
//        SignalDA signal = dao.queryBuilder().list().get(0);
//        db.close();
//        return new SignalDataService.Signal();//todo
//    }
}
