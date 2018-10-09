package com.sma2.sma2;

import android.net.Uri;

import com.sma2.sma2.ExerciseFragments.ExSustainedVowel;

import java.util.ArrayList;
import java.util.List;

public class ExerciseSessionManager {

    private List<Exercise> _dummyExerciseList = new ArrayList<Exercise>();
    public void createExerciseSession() {
        // Create a new list of exercise and store them in the database with a new (incrementing) session id
        // store the current session id in the as a shared property
    }

    public List<ScheduledExercise> getExerciseSession() {
        // create Dummy List
        _createDummyExerciseList();
        List<ScheduledExercise> testList = new ArrayList<ScheduledExercise>();
        testList.add(new ScheduledExercise(_dummyExerciseList.get(0), 1));
        testList.add(new ScheduledExercise(_dummyExerciseList.get(0), 1));
        testList.add(new ScheduledExercise(_dummyExerciseList.get(0), 1));
        return testList;
    }

    public ScheduledExercise getNextExercise() {
        return getExerciseSession().get(0);
    }

    public void _createDummyExerciseList(){
        _dummyExerciseList.add(new Exercise("Test1", Uri.parse("video/path"), Uri.parse("Instruction/Path"), ExSustainedVowel.class));
        _dummyExerciseList.add(new Exercise("Test2", Uri.parse("video/path"), Uri.parse("Instruction/Path"), ExSustainedVowel.class));
        _dummyExerciseList.add(new Exercise("Test3", Uri.parse("video/path"), Uri.parse("Instruction/Path"), ExSustainedVowel.class));
    }
}
