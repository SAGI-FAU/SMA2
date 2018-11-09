package com.sma2.sma2.ExerciseLogic;

import android.content.Context;
import android.net.Uri;

import com.sma2.sma2.DataAccess.ExerciseDataService;
import com.sma2.sma2.DataAccess.ScheduledExerciseDataService;
import com.sma2.sma2.ExerciseFragments.ExAudioRec;
import com.sma2.sma2.ExerciseFragments.ExFreeWalking;
import com.sma2.sma2.ExerciseFragments.ExImageDescription;
import com.sma2.sma2.ExerciseFragments.ExOneFingerTapping;
import com.sma2.sma2.ExerciseFragments.ExReadText;
import com.sma2.sma2.ExerciseFragments.ExSliding;
import com.sma2.sma2.ExerciseFragments.ExTwoFingerTapping;
import com.sma2.sma2.ExerciseFragments.Ex_Circling_Rec;
import com.sma2.sma2.ExerciseFragments.Ex_Hand_Rotation_Rec;
import com.sma2.sma2.ExerciseFragments.Ex_Hand_To_Head_Rec;
import com.sma2.sma2.ExerciseFragments.Ex_Walking_Rec;
import com.sma2.sma2.ExerciseFragments.Ex_balance_Rec;
import com.sma2.sma2.ExerciseFragments.Ex_postural_Rec;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ExerciseSessionManager {

    private List<Exercise> _dummyExerciseList = new ArrayList<Exercise>(); // TODO: REMOVE
    private List<ScheduledExercise> _testList = new ArrayList<ScheduledExercise>(); // TODO: REMOVE


    public void createExerciseSession(Context context) {
        // create Dummy List
        ScheduledExerciseDataService scheduledExerciseDataService = new ScheduledExerciseDataService(context);
        ExerciseDataService exerciseDataService = new ExerciseDataService(context);
        _testList=scheduledExerciseDataService.getAllScheduledExercises();
        if (_testList.size()>0) {
            for (int i = 0; i < _testList.size(); i++) {
                scheduledExerciseDataService.deleteScheduledExercise(_testList.get(i));
            }
        }
        _createDummyExerciseList(context);
        Calendar c = Calendar.getInstance();
        int day= c.get(Calendar.DAY_OF_WEEK);
        int[] exercisesIDs=new int[] {};
        switch (day){
            case 1:
                exercisesIDs=new int[] {1,2,11,22,33};
                break;
            case 2:
                exercisesIDs=new int[] {3,4,12,23,24,34};
                break;
            case 3:
                exercisesIDs=new int[] {5,6,13,25,26,33};
                break;
            case 4:
                exercisesIDs=new int[] {7,8,14,27,28,34};
                break;
            case 5:
                exercisesIDs=new int[] {9,10,15,29,30,35};
                break;
            case 6:
                exercisesIDs=new int[] {17,20,16,31,35};
                break;
            case 7:
                exercisesIDs=new int[] {18,19,21,32,33};
                break;

        }


        for (int id : exercisesIDs){
            Exercise exercise=exerciseDataService.getExercise((long)id);
            ScheduledExercise ex = new ScheduledExercise(exercise, day);
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

        _dummyExerciseList.add(new Exercise(1, "Sentence 0",
                "Speech",
                "Reading Text",
                "Please read the following text",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                ExReadText.class));
        _dummyExerciseList.add(new Exercise(2, "Sentence 1",
                "Speech",
                "Reading Text",
                "Please read the following text",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                ExReadText.class));
        _dummyExerciseList.add(new Exercise(3, "Sentence 2",
                "Speech",
                "Reading Text",
                "Please read the following text",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                ExReadText.class));
        _dummyExerciseList.add(new Exercise(4, "Sentence 3",
                "Speech",
                "Reading Text",
                "Please read the following text",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                ExReadText.class));
        _dummyExerciseList.add(new Exercise(5, "Sentence 4",
                "Speech",
                "Reading Text",
                "Please read the following text",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                ExReadText.class));
        _dummyExerciseList.add(new Exercise(6, "Sentence 5",
                "Speech",
                "Reading Text",
                "Please read the following text",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                ExReadText.class));
        _dummyExerciseList.add(new Exercise(7, "Sentence 6",
                "Speech",
                "Reading Text",
                "Please read the following text",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                ExReadText.class));
        _dummyExerciseList.add(new Exercise(8, "Sentence 7",
                "Speech",
                "Reading Text",
                "Please read the following text",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                ExReadText.class));
        _dummyExerciseList.add(new Exercise(9, "Sentence 8",
                "Speech",
                "Reading Text",
                "Please read the following text",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                ExReadText.class));
        _dummyExerciseList.add(new Exercise(10, "Sentence 9",
                "Speech",
                "Reading Text",
                "Please read the following text",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                ExReadText.class));
        _dummyExerciseList.add(new Exercise(11, "Pataka",
                "Speech",
                "Pataka",
                "Please say Pataka as many times as possible",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                ExAudioRec.class));
        _dummyExerciseList.add(new Exercise(12, "Pakata",
                "Speech",
                "Pakata",
                "Please say Pakata as many times as possible",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                ExAudioRec.class));
        _dummyExerciseList.add(new Exercise(13, "Petaka",
                "Speech",
                "Petaka",
                "Please say Petaka as many times as possible",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                ExAudioRec.class));
        _dummyExerciseList.add(new Exercise(14, "Pa",
                "Speech",
                "Pa",
                "Please say Pa as many times as possible",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                ExAudioRec.class));
        _dummyExerciseList.add(new Exercise(15, "Ta",
                "Speech",
                "Ta",
                "Please say Ta as many times as possible",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                ExAudioRec.class));
        _dummyExerciseList.add(new Exercise(16, "Ka",
                "Speech",
                "Ka",
                "Please say Ka as many times as possible",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                ExAudioRec.class));
        _dummyExerciseList.add(new Exercise(17, "Sifashu",
                "Speech",
                "Sifashu",
                "Please say Si-fa-shu as many times as possible",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                ExAudioRec.class));
        _dummyExerciseList.add(new Exercise(18, "A",
                "Speech",
                "Sustained Vowel",
                "Please hold the sound Ah as long as possible",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                ExAudioRec.class));
        _dummyExerciseList.add(new Exercise(19, "I",
                "Speech",
                "Sustained Vowel",
                "Please hold the sound Ih as long as possible",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                ExAudioRec.class));

        _dummyExerciseList.add(new Exercise(20, "U",
                "Speech",
                "Sustained Vowel",
                "Please hold the sound Uh as long as possible",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                ExAudioRec.class));

        _dummyExerciseList.add(new Exercise(21, "Cookie theft",
                "Speech",
                "Image description of the Cookie theft picture",
                "Please describe what you see in the following image",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                ExImageDescription.class));


        _dummyExerciseList.add(new Exercise(22, "Balance",
                "Movement",
                "Stand Straight during 30 seconds",
                "Please put the smartphone in your pocket and stand Straight during 30 seconds",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                Ex_balance_Rec.class));

        _dummyExerciseList.add(new Exercise(23, "Circling Left",
                "Movement",
                "Make circles with your arm extended with your left arm",
                "Please make circles with your left arm extended",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                Ex_Circling_Rec.class));

        _dummyExerciseList.add(new Exercise(24, "Circling Right",
                "Movement",
                "Make circles with your arm extended with your right arm",
                "Please make circles with your right arm extended",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                Ex_Circling_Rec.class));

        _dummyExerciseList.add(new Exercise(25, "Hand Rotation Left",
                "Movement",
                "Hand Rotation Left",
                "Stretch out your left arm and twist it",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                Ex_Hand_Rotation_Rec.class));

        _dummyExerciseList.add(new Exercise(26, "Hand Rotation Right",
                "Movement",
                "Hand Rotation Right",
                "Stretch out your right arm and twist it",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                Ex_Hand_Rotation_Rec.class));


        _dummyExerciseList.add(new Exercise(27, "Hand to Nose Left",
                "Movement",
                "Hand to Nose Left",
                "Stretch out your left arm, palm facing up, and then move your hand to the nose",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                Ex_Hand_To_Head_Rec.class));

        _dummyExerciseList.add(new Exercise(28, "Hand to Nose Right",
                "Movement",
                "Hand to Nose Right",
                "Stretch out your right arm, palm facing up, and then move your hand to the nose",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                Ex_Hand_To_Head_Rec.class));

        _dummyExerciseList.add(new Exercise(29, "Postural tremor Left",
                "Movement",
                "Hold the phone with the left arm extended",
                "Please hold the phone with your extended arm as long as possible",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                Ex_postural_Rec.class));

        _dummyExerciseList.add(new Exercise(30, "Postural tremor Right",
                "Movement",
                "Hold the phone with the right arm extended",
                "Please hold the phone with your extended arm as long as possible",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                Ex_postural_Rec.class));

        _dummyExerciseList.add(new Exercise(31, "Walking Path",
                "Movement",
                "Walking",
                "Walk a known distance/path for 4 times",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                Ex_Walking_Rec.class));

        _dummyExerciseList.add(new Exercise(32, "Walking Free",
                "Movement",
                "Walking",
                "Walk a free pth during 2 minutes",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                ExFreeWalking.class));


        _dummyExerciseList.add(new Exercise(33, "Tapping one finger",
                "Tapping",
                "Tapping Bug",
                "Tap the bug as fast as you can",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                ExOneFingerTapping.class));


        _dummyExerciseList.add(new Exercise(34, "Tapping two fingers",
                "Tapping",
                "2-finger Tapping",
                "Tap the bug as fast as you can by alternating between left and right bugs",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                ExTwoFingerTapping.class));

        _dummyExerciseList.add(new Exercise(35, "Sliding",
                "Tapping",
                "Sliding",
                "Please move the bar to the target position",
                Uri.parse("video/path"),
                Uri.parse("Instruction/Path"),
                ExSliding.class));


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
