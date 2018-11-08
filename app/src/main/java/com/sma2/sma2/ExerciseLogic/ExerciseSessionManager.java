package com.sma2.sma2.ExerciseLogic;

import android.content.Context;
import android.net.Uri;

import com.sma2.sma2.DataAccess.ExerciseDataService;
import com.sma2.sma2.DataAccess.ScheduledExerciseDataService;
import com.sma2.sma2.ExerciseFragments.ExAudioRec;
import com.sma2.sma2.ExerciseFragments.ExImageDescription;
import com.sma2.sma2.ExerciseFragments.ExOneFingerTapping;
import com.sma2.sma2.ExerciseFragments.ExReadText;
import com.sma2.sma2.ExerciseFragments.ExSliding;
import com.sma2.sma2.ExerciseFragments.ExTwoFingerTapping;
import com.sma2.sma2.ExerciseFragments.Ex_Circling_Rec;
import com.sma2.sma2.ExerciseFragments.Ex_Hand_Rotation_Rec;
import com.sma2.sma2.ExerciseFragments.Ex_Hand_To_Head_Rec;
import com.sma2.sma2.ExerciseFragments.Ex_Walking_Rec;
import com.sma2.sma2.ExerciseFragments.Ex_postural_Rec;

import java.util.ArrayList;
import java.util.List;

public class ExerciseSessionManager {

    private List<Exercise> _dummyExerciseList = new ArrayList<Exercise>(); // TODO: REMOVE
    private List<ScheduledExercise> _testList = new ArrayList<ScheduledExercise>(); // TODO: REMOVE


    public void createExerciseSession(Context context) {
        // create Dummy List
        ScheduledExerciseDataService scheduledExerciseDataService = new ScheduledExerciseDataService(context);
        _testList=scheduledExerciseDataService.getAllScheduledExercises();
        if (_testList.size()>0) {
            for (int i = 0; i < _testList.size(); i++) {
                scheduledExerciseDataService.deleteScheduledExercise(_testList.get(i));
            }
        }

        _createDummyExerciseList(context);
        _testList=scheduledExerciseDataService.getAllScheduledExercises();
        for (Exercise exercise : _dummyExerciseList){
            ScheduledExercise ex = new ScheduledExercise(exercise, 1);
            _testList.add(ex);
            scheduledExerciseDataService.saveScheduledExercise(ex);
        }
        _testList=scheduledExerciseDataService.getAllScheduledExercises();
        // Create a new list of exercise and store them in the database with a new (incrementing) session id
        // store the current session id in the as a shared property
    }

    public void updateExerciseListFromDB(Context context) {
        ScheduledExerciseDataService scheduledExerciseDataService = new ScheduledExerciseDataService(context);
        _testList=scheduledExerciseDataService.getAllScheduledExercises();
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


        _dummyExerciseList.add(new Exercise(null, "Circling",
                "Movement",
                "Make circles with your arm extended",
                "Please make circles with your arm extended",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                Ex_Circling_Rec.class));

        _dummyExerciseList.add(new Exercise(null, "Postural tremor",
                "Movement",
                "Hold the phone with the extended arm",
                "Please hold the phone with your extended arm as long as possible",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                Ex_postural_Rec.class));

        _dummyExerciseList.add(new Exercise(null, "Cookie theft",
                "Speech",
                "Image description of the Cookie theft picture",
                "Please describe what you see in the following image",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                ExImageDescription.class));

        _dummyExerciseList.add(new Exercise(null, "Tapping 2 fingers",
                "Tapping",
                "2-finger Tapping",
                "Tap the bug as fast as you can by alternating between left and right bugs",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                ExTwoFingerTapping.class));

        _dummyExerciseList.add(new Exercise(null, "Sliding",
                "Tapping",
                "Sliding",
                "Please move the bar to the target position",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                ExSliding.class));

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
        _dummyExerciseList.add(new Exercise(null, "Sentence 0",
                "Speech",
                "Reading Text",
                "Please read the following text",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                ExReadText.class));
        _dummyExerciseList.add(new Exercise(null, "Sentence 1",
                "Speech",
                "Reading Text",
                "Please read the following text",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                ExReadText.class));
        _dummyExerciseList.add(new Exercise(null, "Sentence 2",
                "Speech",
                "Reading Text",
                "Please read the following text",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                ExReadText.class));
        _dummyExerciseList.add(new Exercise(null, "Sentence 3",
                "Speech",
                "Reading Text",
                "Please read the following text",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                ExReadText.class));
        _dummyExerciseList.add(new Exercise(null, "Sentence 4",
                "Speech",
                "Reading Text",
                "Please read the following text",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                ExReadText.class));
        _dummyExerciseList.add(new Exercise(null, "Sentence 5",
                "Speech",
                "Reading Text",
                "Please read the following text",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                ExReadText.class));
        _dummyExerciseList.add(new Exercise(null, "Sentence 6",
                "Speech",
                "Reading Text",
                "Please read the following text",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                ExReadText.class));
        _dummyExerciseList.add(new Exercise(null, "Sentence 7",
                "Speech",
                "Reading Text",
                "Please read the following text",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                ExReadText.class));
        _dummyExerciseList.add(new Exercise(null, "Sentence 8",
                "Speech",
                "Reading Text",
                "Please read the following text",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                ExReadText.class));
        _dummyExerciseList.add(new Exercise(null, "Sentence 9",
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
                "Movement",
                "Hand Rotation",
                "Stretch out your arm and twist it",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                Ex_Hand_Rotation_Rec.class));
        _dummyExerciseList.add(new Exercise(null, "Hand to Head",
                "Movement",
                "Hand to Head",
                "Stretch out your arm, palm facing up, and then move your hand to the head",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                Ex_Hand_To_Head_Rec.class));
        _dummyExerciseList.add(new Exercise(null, "Walking",
                "Movement",
                "Walking",
                "Walk a known distance/path for 4 times",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                Ex_Walking_Rec.class));


        ExerciseDataService exerciseDataService = new ExerciseDataService(context);

        ArrayList<Exercise> exerciseDb=exerciseDataService.getAllExercises();


        for (int i = 0; i < exerciseDb.size(); i++) {
            exerciseDataService.deleteExercise(exerciseDb.get(i));
        }

        if (exerciseDataService.getAllExercises().size()==0) {
            for (int i = 0; i < _dummyExerciseList.size(); i++) {
                exerciseDataService.insertExercise(_dummyExerciseList.get(i));
            }
            // exerciseDataService.closeDB();
        }
        _dummyExerciseList=exerciseDataService.getAllExercises();
    }
}
