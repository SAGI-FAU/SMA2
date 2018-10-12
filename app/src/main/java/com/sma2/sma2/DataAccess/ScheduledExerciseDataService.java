package com.sma2.sma2.DataAccess;

import android.content.Context;

import com.sma2.sma2.ExerciseLogic.ScheduledExercise;

import com.sma2.sma2.ExerciseLogic.ScheduledExerciseDao;

import org.greenrobot.greendao.database.Database;

import java.util.ArrayList;
import java.util.List;

public class ScheduledExerciseDataService {
    private Context invocationcontext;
    public ScheduledExerciseDataService(Context context){
        this.invocationcontext = context;
    }

    public void saveScheduledExercise(ScheduledExercise tmp){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, "apkinsondb");
        Database db = helper.getWritableDb();
        DaoSession session = new DaoMaster(db).newSession();
        session.getScheduledExerciseDao().save(tmp);//todo change the real thing
        db.close();
    }

    public void updateScheduledExercise(ScheduledExercise scheduledExercise){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, "apkinsondb");
        Database db = helper.getWritableDb();
        DaoSession session = new DaoMaster(db).newSession();
        session.getScheduledExerciseDao().update(scheduledExercise);
        db.close();
    }

    public void deleteScheduledExercise(ScheduledExercise scheduledExercise){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, "apkinsondb");
        Database db = helper.getWritableDb();
        DaoSession session = new DaoMaster(db).newSession();
        session.getScheduledExerciseDao().delete(scheduledExercise);
        db.close();
    }
    public ScheduledExercise getScheduledExercise(Long id){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, "apkinsondb");
        Database db = helper.getReadableDb();
        DaoSession session = new DaoMaster(db).newSession();
        ScheduledExerciseDao dao = session.getScheduledExerciseDao();
        ScheduledExercise scheduledExercise = dao.queryBuilder().where(ScheduledExerciseDao.Properties.Id.eq(id)).list().get(0);
        db.close();
        return scheduledExercise;
    }

    public ArrayList<ScheduledExercise> getAllScheduledExercises(){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, "apkinsondb");
        Database db = helper.getReadableDb();
        DaoSession session = new DaoMaster(db).newSession();
        ScheduledExerciseDao dao = session.getScheduledExerciseDao();
        List<ScheduledExercise> scheduledExercises = dao.queryBuilder().list();

        db.close();
        return new ArrayList<ScheduledExercise>(scheduledExercises);
    }
}
