package com.sma2.sma2;

import android.net.Uri;

import com.sma2.sma2.ExerciseFragments.ExAudioRec;

import java.util.ArrayList;
import java.util.List;

public class ExerciseSessionManager {

    private List<Exercise> _dummyExerciseList = new ArrayList<Exercise>(); // TODO: REMOVE
    private List<ScheduledExercise> _testList = new ArrayList<ScheduledExercise>(); // TODO: REMOVE


    public void createExerciseSession() {
        // create Dummy List
        _createDummyExerciseList();
        _testList.add(new ScheduledExercise(_dummyExerciseList.get(0), 1));
//        _testList.add(new ScheduledExercise(_dummyExerciseList.get(1), 1));
//        _testList.add(new ScheduledExercise(_dummyExerciseList.get(2), 1));
        // Create a new list of exercise and store them in the database with a new (incrementing) session id
        // store the current session id in the as a shared property
    }

    public List<ScheduledExercise> getExerciseSession() {
        // create Dummy List
        return _testList;
    }

    public ScheduledExercise getNextExercise() throws IndexOutOfBoundsException {
        for (ScheduledExercise ex : getExerciseSession()) {
            if (ex.getCompletionDate() == -1) {
                return ex;
            }
        }
        throw new IndexOutOfBoundsException("No exercises left");
    }

    public void _createDummyExerciseList(){
        _dummyExerciseList.add(new Exercise("Test1", Uri.parse("video/path"), Uri.parse("Instruction/Path"), ExAudioRec.class));
        _dummyExerciseList.add(new Exercise("Test2", Uri.parse("video/path"), Uri.parse("Instruction/Path"), ExAudioRec.class));
        _dummyExerciseList.add(new Exercise("Test3", Uri.parse("video/path"), Uri.parse("Instruction/Path"), ExAudioRec.class));
    }
}
