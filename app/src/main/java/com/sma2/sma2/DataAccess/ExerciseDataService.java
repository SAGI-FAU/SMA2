package com.sma2.sma2.DataAccess;

import android.content.Context;

import com.sma2.sma2.ExerciseLogic.Exercise;

import org.greenrobot.greendao.database.Database;

public class ExerciseDataService {
    private Context invocationcontext;
    public ExerciseDataService(Context context){
        this.invocationcontext = context;
    }

    public void saveExercise(Exercise exercise){
        ExerciseDA tmp = new ExerciseDA(exercise);
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, "apkinsondb");
        Database db = helper.getWritableDb();
        DaoSession session = new DaoMaster(db).newSession();
        session.getExerciseDADao().save(tmp);//todo change the real thing
        db.close();
    }

    public void updateExercise(Exercise exercise){
        ExerciseDA exerciseDA = new ExerciseDA(exercise);
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, "apkinsondb");
        Database db = helper.getWritableDb();
        DaoSession session = new DaoMaster(db).newSession();
        session.getExerciseDADao().update(exerciseDA);
        db.close();
    }

    public void deleteExercise(ExerciseDA exercise){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, "apkinsondb");
        Database db = helper.getWritableDb();
        DaoSession session = new DaoMaster(db).newSession();
        session.getExerciseDADao().delete(exercise);
        db.close();
    }
    public Exercise getExercise(Long id){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, "apkinsondb");
        Database db = helper.getReadableDb();
        DaoSession session = new DaoMaster(db).newSession();
        ExerciseDADao dao = session.getExerciseDADao();
        ExerciseDA exerciseDA = dao.queryBuilder().list().get(id);
        db.close();
        return new Exercise(exerciseDA);
    }
}
