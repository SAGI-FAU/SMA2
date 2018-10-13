package com.sma2.sma2.ExerciseLogic;

import android.net.Uri;

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


    public void createExerciseSession() {
        // create Dummy List
        _createDummyExerciseList();
        _testList.add(new ScheduledExercise(_dummyExerciseList.get(0), 1));
        _testList.add(new ScheduledExercise(_dummyExerciseList.get(1), 1));
        _testList.add(new ScheduledExercise(_dummyExerciseList.get(2), 1));
        _testList.add(new ScheduledExercise(_dummyExerciseList.get(3), 1));
        // Create a new list of exercise and store them in the database with a new (incrementing) session id
        // store the current session id in the as a shared property
    }

    public List<ScheduledExercise> getScheduledExerciseList() {
        // create Dummy List
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

    public void _createDummyExerciseList(){
        _dummyExerciseList.add(new Exercise(1, "Test1",
                "Speech",
                "Pataka",
                "Please say Pataka as many times as possible",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                ExAudioRec.class));
        _dummyExerciseList.add(new Exercise(1, "Test1",
                "Speech",
                "Sustained Vowel",
                "Please hold the sound Ah as long as possible",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                ExAudioRec.class));
        _dummyExerciseList.add(new Exercise(1, "Hand-to-Head",
                "Speech",
                "Reading Text",
                "Please read the following text",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                ExReadText.class));
        _dummyExerciseList.add(new Exercise(1, "Test1",
                "Tapping",
                "Tapping Bug",
                "Tap the bug as fast as you can",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                ExOneFingerTapping.class));
        _dummyExerciseList.add(new Exercise(1, "Test1",
                "Tapping",
                "Hand Rotation",
                "Stretch out your arm and twist it",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                Ex_Hand_Rotation_Rec.class));
        _dummyExerciseList.add(new Exercise(1, "Test1",
                "Tapping",
                "Hand to Head",
                "Stretch out your arm, palm facing up, and then move your hand to the head",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                Ex_Hand_To_Head_Rec.class));
    }
}
