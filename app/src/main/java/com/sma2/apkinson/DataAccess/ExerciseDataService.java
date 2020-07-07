package com.sma2.apkinson.DataAccess;

import android.content.Context;

import org.greenrobot.greendao.database.Database;

import java.util.ArrayList;
import java.util.List;

public class ExerciseDataService {
    private Context invocationcontext;
    private Database db;
    public ExerciseDataService(Context context){
        this.invocationcontext = context;
        db=null;
    }

    public void insertExercise(Exercise tmp){
        if (db == null) {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, "apkinsondb");
            db = helper.getWritableDb();
        }
        DaoSession session = new DaoMaster(db).newSession();
        session.getExerciseDao().insert(tmp);//todo change the real thing
        // db.close();
    }

    public void saveExercise(Exercise tmp){
        if (db == null) {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, "apkinsondb");
            db = helper.getWritableDb();
        }
        DaoSession session = new DaoMaster(db).newSession();
        session.getExerciseDao().save(tmp);//todo change the real thing
        // db.close();
    }

    public void updateExercise(Exercise exercise){
        if (db == null) {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, "apkinsondb");
            db = helper.getWritableDb();
        }
        DaoSession session = new DaoMaster(db).newSession();
        session.getExerciseDao().update(exercise);
        // db.close();
    }

    public void deleteExercise(Exercise exercise){
        if (db == null) {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, "apkinsondb");
            db = helper.getWritableDb();
        }
        DaoSession session = new DaoMaster(db).newSession();
        session.getExerciseDao().delete(exercise);
        // db.close();
    }
    public Exercise getExercise(Long id){
        if (db == null) {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, "apkinsondb");
            db = helper.getWritableDb();
        }
        DaoSession session = new DaoMaster(db).newSession();
        ExerciseDao dao = session.getExerciseDao();
        Exercise exercise = dao.queryBuilder().where(ExerciseDao.Properties.Id.eq(id)).list().get(0);
        // db.close();
        return exercise;
    }

    public ArrayList<Exercise> getAllExercises(){
        if (db == null) {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, "apkinsondb");
            db = helper.getWritableDb();
        }
        DaoSession session = new DaoMaster(db).newSession();
        ExerciseDao dao = session.getExerciseDao();
        List<Exercise> exercises = dao.queryBuilder().list();

        // db.close();
        return new ArrayList<Exercise>(exercises);
    }

    public void closeDB() {
        if (db!=null) {
            db.close();
            db=null;
        }

    }
}
