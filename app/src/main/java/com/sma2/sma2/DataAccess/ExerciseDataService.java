package com.sma2.sma2.DataAccess;

import android.content.Context;

import com.sma2.sma2.ExerciseLogic.Exercise;
import com.sma2.sma2.ExerciseLogic.ExerciseDao;

import org.greenrobot.greendao.database.Database;

import java.util.ArrayList;
import java.util.List;

public class ExerciseDataService {
    private Context invocationcontext;
    public ExerciseDataService(Context context){
        this.invocationcontext = context;
    }

    public void saveExercise(Exercise tmp){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, "apkinsondb");
        Database db = helper.getWritableDb();
        DaoSession session = new DaoMaster(db).newSession();
        session.getExerciseDao().save(tmp);//todo change the real thing
        db.close();
    }

    public void updateExercise(Exercise exercise){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, "apkinsondb");
        Database db = helper.getWritableDb();
        DaoSession session = new DaoMaster(db).newSession();
        session.getExerciseDao().update(exercise);
        db.close();
    }

    public void deleteExercise(Exercise exercise){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, "apkinsondb");
        Database db = helper.getWritableDb();
        DaoSession session = new DaoMaster(db).newSession();
        session.getExerciseDao().delete(exercise);
        db.close();
    }
    public Exercise getExercise(Long id){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, "apkinsondb");
        Database db = helper.getReadableDb();
        DaoSession session = new DaoMaster(db).newSession();
        ExerciseDao dao = session.getExerciseDao();
        Exercise exercise = dao.queryBuilder().where(ExerciseDao.Properties.Id.eq(id)).list().get(0);
        db.close();
        return exercise;
    }

    public ArrayList<Exercise> getAllExercises(){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, "apkinsondb");
        Database db = helper.getReadableDb();
        DaoSession session = new DaoMaster(db).newSession();
        ExerciseDao dao = session.getExerciseDao();
        List<Exercise> exercises = dao.queryBuilder().list();

        db.close();
        return new ArrayList<Exercise>(exercises);
    }
}
