package com.sma2.sma2.DataAccess;

import android.content.Context;

import com.sma2.sma2.DataAccess.DaoMaster;
import com.sma2.sma2.DataAccess.DaoSession;
import com.sma2.sma2.ExerciseLogic.ScheduledExercise;

import com.sma2.sma2.ExerciseLogic.ScheduledExerciseDao;

import org.greenrobot.greendao.database.Database;

import java.util.ArrayList;
import java.util.List;

public class ScheduledExerciseDataService {
    private Database db;
    private Context invocationcontext;
    public ScheduledExerciseDataService(Context context){
        this.invocationcontext = context;
        db=null;
    }

    public void saveScheduledExercise(ScheduledExercise tmp){
        if (db == null) {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, "apkinsondb");
            db = helper.getWritableDb();
        }
        DaoSession session = new DaoMaster(db).newSession();
        session.getScheduledExerciseDao().save(tmp);//todo change the real thing
        // db.close();
    }

    public void updateScheduledExercise(ScheduledExercise scheduledExercise){
        if (db == null) {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, "apkinsondb");
            db = helper.getWritableDb();
        }
        DaoSession session = new DaoMaster(db).newSession();
        session.getScheduledExerciseDao().update(scheduledExercise);
        // db.close();
    }

    public void deleteScheduledExercise(ScheduledExercise scheduledExercise){
        if (db == null) {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, "apkinsondb");
            db = helper.getWritableDb();
        }
        DaoSession session = new DaoMaster(db).newSession();
        session.getScheduledExerciseDao().delete(scheduledExercise);
        // db.close();
    }
    public ScheduledExercise getScheduledExercise(Long id){
        if (db == null) {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, "apkinsondb");
            db = helper.getWritableDb();
        }
        DaoSession session = new DaoMaster(db).newSession();
        ScheduledExerciseDao dao = session.getScheduledExerciseDao();
        ScheduledExercise scheduledExercise = dao.queryBuilder().where(ScheduledExerciseDao.Properties.Id.eq(id)).list().get(0);
        // db.close();
        return scheduledExercise;
    }

    public ArrayList<ScheduledExercise> getAllScheduledExercises(){
        if (db == null) {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, "apkinsondb");
            db = helper.getWritableDb();
        }
        DaoSession session = new DaoMaster(db).newSession();
        ScheduledExerciseDao dao = session.getScheduledExerciseDao();
        List<ScheduledExercise> scheduledExercises = dao.queryBuilder().list();

        // db.close();
        return new ArrayList<ScheduledExercise>(scheduledExercises);
    }

    public ArrayList<ScheduledExercise> getAllScheduledExercisesbySession(int SessionId){
        if (db == null) {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, "apkinsondb");
            db = helper.getWritableDb();
        }
        DaoSession session = new DaoMaster(db).newSession();
        ScheduledExerciseDao dao = session.getScheduledExerciseDao();
        List<ScheduledExercise> scheduledExercises = dao.queryBuilder()
                .where(ScheduledExerciseDao.Properties.SessionId.eq(SessionId))
                .list();

        // db.close();
        return new ArrayList<ScheduledExercise>(scheduledExercises);
    }

    public void closeDB() {
        if (db!=null) {
            db.close();
        }
    }
}
