package com.sma2.sma2.ExerciseLogic;

import android.content.Context;
import android.net.Uri;

import com.sma2.sma2.DataAccess.ExerciseDataService;
import com.sma2.sma2.DataAccess.ScheduledExerciseDataService;
import com.sma2.sma2.ExerciseFragments.ExAudioRec;
import com.sma2.sma2.ExerciseFragments.ExOneFingerTapping;
import com.sma2.sma2.ExerciseFragments.ExReadText;
import com.sma2.sma2.ExerciseFragments.Ex_Hand_Rotation_Rec;
import com.sma2.sma2.ExerciseFragments.Ex_Hand_To_Head_Rec;

import java.util.ArrayList;
import java.util.List;

public class ExerciseSessionManager {

    private List<Exercise> _dummyExerciseList = new ArrayList<Exercise>(); // TODO: REMOVE
    private List<ScheduledExercise> _testList = new ArrayList<ScheduledExercise>(); // TODO: REMOVE


    public void createExerciseSession(Context context) {
        // create Dummy List
        _createDummyExerciseList(context);
        for (Exercise exercise : _dummyExerciseList){
            _testList.add(new ScheduledExercise(exercise, 1));
        }
        ScheduledExerciseDataService scheduledExerciseDataService = new ScheduledExerciseDataService(context);
        if (scheduledExerciseDataService.getAllScheduledExercises().size()==0){
            for (int i=0; i<_testList.size(); i++){
                scheduledExerciseDataService.saveScheduledExercise(_testList.get(i));
            }
        }
        _testList=scheduledExerciseDataService.getAllScheduledExercises();
        // Create a new list of exercise and store them in the database with a new (incrementing) session id
        // store the current session id in the as a shared property
    }

    public List<ScheduledExercise> updateExerciseListFromDB(Context context) {
        ScheduledExerciseDataService scheduledExerciseDataService = new ScheduledExerciseDataService(context);
        _testList=scheduledExerciseDataService.getAllScheduledExercises();
        return _testList;
    }

    public List<ScheduledExercise> getScheduledExerciseList() {
        return _testList;
    }

    public ScheduledExercise getNextExercise() throws IndexOutOfBoundsException {
        for (ScheduledExercise ex : getScheduledExerciseList()) {
            if (ex.getCompletionDate() == -1) {
                return ex;
            }
        }
        throw new IndexOutOfBoundsException("No exercises left");
    }

    public static boolean getSessionStarted(List<ScheduledExercise> scheduledExerciseList) {
        for (ScheduledExercise ex : scheduledExerciseList) {
            if (ex.getCompleted()) {
                return true;
            }
        }
        return false;
    }

    public static boolean getSessionCompleted(List<ScheduledExercise> scheduledExerciseList) {
        for (ScheduledExercise ex : scheduledExerciseList) {
            if (!(ex.getCompleted())) {
                return false;
            }
        }
        return true;
    }

    public void _createDummyExerciseList(Context context){
        _dummyExerciseList.add(new Exercise(null, "Pataka",
                "Speech",
                "Pataka",
                "Please say Pataka as many times as possible",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                ExAudioRec.class));
        _dummyExerciseList.add(new Exercise(null, "Sustained Vowel",
                "Speech",
                "Sustained Vowel",
                "Please hold the sound Ah as long as possible",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                ExAudioRec.class));
        _dummyExerciseList.add(new Exercise(null, "Reading Text",
                "Speech",
                "Reading Text",
                "Please read the following text",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                ExReadText.class));
        _dummyExerciseList.add(new Exercise(null, "Tapping Bug",
                "Tapping",
                "Tapping Bug",
                "Tap the bug as fast as you can",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                ExOneFingerTapping.class));
        _dummyExerciseList.add(new Exercise(null, "Hand Rotation",
                "Tapping",
                "Hand Rotation",
                "Stretch out your arm and twist it",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                Ex_Hand_Rotation_Rec.class));
        _dummyExerciseList.add(new Exercise(null, "Hand to Head",
                "Tapping",
                "Hand to Head",
                "Stretch out your arm, palm facing up, and then move your hand to the head",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                Ex_Hand_To_Head_Rec.class));

        ExerciseDataService exerciseDataService = new ExerciseDataService(context);
        if (exerciseDataService.getAllExercises().size()==0) {
            for (int i = 0; i < _dummyExerciseList.size(); i++) {
                exerciseDataService.insertExercise(_dummyExerciseList.get(i));
            }
            // exerciseDataService.closeDB();
        }
        _dummyExerciseList=exerciseDataService.getAllExercises();
    }
}
