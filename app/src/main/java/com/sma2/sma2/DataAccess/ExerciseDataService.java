package com.sma2.sma2.DataAccess;

import android.content.Context;

import com.sma2.sma2.ExerciseLogic.Exercise;

import org.greenrobot.greendao.database.Database;

import java.util.ArrayList;
import java.util.List;

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

    public void updateExercise(ExerciseDA exercise){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, "apkinsondb");
        Database db = helper.getWritableDb();
        DaoSession session = new DaoMaster(db).newSession();
        session.getExerciseDADao().update(exercise);
        db.close();
    }

    public void deleteExercise(ExerciseDA exercise){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, "apkinsondb");
        Database db = helper.getWritableDb();
        DaoSession session = new DaoMaster(db).newSession();
        session.getExerciseDADao().delete(exercise);
        db.close();
    }
    public Exercise getExercise(){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, "apkinsondb");
        Database db = helper.getReadableDb();
        DaoSession session = new DaoMaster(db).newSession();
        ExerciseDADao dao = session.getExerciseDADao();
        ExerciseDA exerciseDA = dao.queryBuilder().list().get(0);
        db.close();
        return new Exercise(exerciseDA);//todo
    }

    public ArrayList<Exercise> getAllExercises(){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, "apkinsondb");
        Database db = helper.getReadableDb();
        DaoSession session = new DaoMaster(db).newSession();
        ExerciseDADao dao = session.getExerciseDADao();
        List<ExerciseDA> exercisesDA = dao.queryBuilder().list();
        ArrayList<Exercise> exercises = new ArrayList<Exercise>();
        for (int i=0; i<exercisesDA.size(); i++) {
            exercises.add(new Exercise(exercisesDA.get(i)));
        }
        db.close();
        return exercises;
    }
}
