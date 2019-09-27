package com.sma2.sma2.DataAccess;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.sma2.sma2.ExerciseFragments.ExAudioRec;
import com.sma2.sma2.ExerciseFragments.ExImageDescription;
import com.sma2.sma2.ExerciseFragments.ExOneFingerTapping;
import com.sma2.sma2.ExerciseFragments.ExReadText;
import com.sma2.sma2.ExerciseFragments.ExSliding;
import com.sma2.sma2.ExerciseFragments.ExTwoFingerTapping;
import com.sma2.sma2.ExerciseFragments.Ex_Circling_Rec;
import com.sma2.sma2.ExerciseFragments.Ex_Hand_Rotation_Rec;
import com.sma2.sma2.ExerciseFragments.Ex_Hand_To_Head_Rec;
import com.sma2.sma2.ExerciseFragments.Ex_Happy;
import com.sma2.sma2.ExerciseFragments.Ex_LeftWink;
import com.sma2.sma2.ExerciseFragments.Ex_RightWink;
import com.sma2.sma2.ExerciseFragments.Ex_Walking_Rec;
import com.sma2.sma2.ExerciseFragments.ExFreeWalking;
import com.sma2.sma2.ExerciseFragments.Ex_balance_Rec;
import com.sma2.sma2.ExerciseFragments.Ex_postural_Rec;
import com.sma2.sma2.ExerciseFragments.FerRecorder;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ExerciseSessionManager {

    private List<Exercise> _dummyExerciseList = new ArrayList<>();
    private List<Exercise> _ExerciseList = new ArrayList<>();
    private List<ScheduledExercise> _testList = new ArrayList<>();
    SharedPreferences sharedPref;

    public void createExerciseSession(Context context) {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_WEEK);
        sharedPref=PreferenceManager.getDefaultSharedPreferences(context);

        // create Dummy List
        ScheduledExerciseDataService scheduledExerciseDataService = new ScheduledExerciseDataService(context);
        ExerciseDataService exerciseDataService = new ExerciseDataService(context);
        _testList = scheduledExerciseDataService.getAllScheduledExercisesbySession(day);
        if (_testList.size() > 0) {
            for (int i = 0; i < _testList.size(); i++) {
                scheduledExerciseDataService.deleteScheduledExercise(_testList.get(i));
            }
        }

        try {
            createExerciseList(context);
        } catch (IOException e) {
            Log.e("CSV_READING", e.toString());
        }

        int[] exercisesIDs = new int[]{};

        String type_exercise=sharedPref.getString("exercises", "daily");


        if (type_exercise.equals("daily")){
            switch (day) {
                case 1:
                    exercisesIDs = new int[]{1, 2, 11, 22, 33,36};
                    break;
                case 2:
                    exercisesIDs = new int[]{3, 4, 12, 23, 24, 34};
                    break;
                case 3:
                    exercisesIDs = new int[]{5, 6, 13, 25, 26, 33};
                    break;
                case 4:
                    exercisesIDs = new int[]{7, 8, 14, 27, 28, 34,37};
                    break;
                case 5:
                    exercisesIDs = new int[]{9, 10, 15, 29, 30, 35};
                    break;
                case 6:
                    exercisesIDs = new int[]{16, 17, 20, 31, 35};
                    break;
                case 7:
                    exercisesIDs = new int[]{18, 19, 21, 32, 33,38};
                    break;

            }
        }
        else if (type_exercise.equals("full")){

            exercisesIDs = new int[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38};

        }
        else if (type_exercise.equals("speech")){
            exercisesIDs = new int[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21};
        }
        else if (type_exercise.equals("movement")){
            exercisesIDs = new int[]{22,23,24,25,26,27,28,29,30,31,32,33,34,35};
        }
        else{
            exercisesIDs = new int[]{1,2,11,14,18,25,26,34,35,36,37,38};

        }

        for (int id : exercisesIDs) {
            Exercise exercise = exerciseDataService.getExercise((long) id);
            ScheduledExercise ex = new ScheduledExercise(exercise, day);
            _testList.add(ex);
            scheduledExerciseDataService.saveScheduledExercise(ex);
        }
        _testList = scheduledExerciseDataService.getAllScheduledExercisesbySession(day);
        // Create a new list of exercise and store them in the database with a new (incrementing) session id
        // store the current session id in the as a shared property
    }

    public void updateExerciseListFromDB(Context context) {
        ScheduledExerciseDataService scheduledExerciseDataService = new ScheduledExerciseDataService(context);
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_WEEK);
        _testList = scheduledExerciseDataService.getAllScheduledExercisesbySession(day);
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


    private void createExerciseList(Context context) throws IOException {
        InputStreamReader is;
        is = new InputStreamReader(context.getAssets()
                .open("instructions.csv"), "UTF-8");
        CSVReader reader = new CSVReaderBuilder(is).build();
        CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
        String[] languages = parser.parseLine(reader.readNext()[0]);
        String[] instr;
        int locale = getCurrentLocale(languages);
        int ExID;
        String ExName;
        String ExType;
        String ExFrag;
        String ExDescr;
        String ExInstr;
        String ExVideo;
        while ((instr = reader.readNext()) != null) {
            instr = parser.parseLine(TextUtils.join("", instr));
            ExID = Integer.parseInt(instr[0]);//The ID is always on the first position CSV.
            ExType = instr[1];//The type of exercise is always on the second position of the CSV.
            ExFrag = instr[2];//Used to select the fragment of a particular task (see if-else statements below)
            //Name, description, and instruction.
            ExName = instr[locale];//Exercise name. The position depends on the detected language.
            ExDescr = instr[locale + 1];//Exercise description.
            ExInstr = instr[locale + 2];//Exercise instruction.
            String videoName = instr[instr.length - 1];
            if (videoName.length() != 0) {
                if (languages[locale].contains("de")){
                    ExVideo = "en" + videoName;
                }
                else{
                    ExVideo = languages[locale] + videoName;
                }
                //
                String test = ExVideo.split(".webm")[0];
                ExVideo = "android.resource://" + context.getPackageName() + "/" + context.getResources().getIdentifier(ExVideo.split(".webm")[0], "raw", context.getPackageName());
            } else {
                ExVideo = "None";
            }

            //The following if-else statements are used to select the fragment of each task
            //ej: ExReadText.class only works for sentences.
            if (ExFrag.equals("readtext"))
            {
                _ExerciseList.add(new Exercise(
                        ExID,
                        ExName,
                        ExType,
                        ExDescr,
                        ExInstr,
                        Uri.parse(ExVideo),
                        Uri.parse("Instruction/Path"),
                        ExReadText.class));
            }
            else if (ExFrag.equals("audiorec"))
            {
                _ExerciseList.add(new Exercise(
                        ExID,
                        ExName,
                        ExType,
                        ExDescr,
                        ExInstr,
                        Uri.parse(ExVideo),
                        Uri.parse("Instruction/Path"),
                        ExAudioRec.class));
            }
            else if (ExFrag.equals("imgdes"))
            {
                _ExerciseList.add(new Exercise(
                        ExID,
                        ExName,
                        ExType,
                        ExDescr,
                        ExInstr,
                        Uri.parse(ExVideo),
                        Uri.parse("Instruction/Path"),
                        ExImageDescription.class));
            }
            else if (ExFrag.equals("balance"))
            {
                _ExerciseList.add(new Exercise(
                        ExID,
                        ExName,
                        ExType,
                        ExDescr,
                        ExInstr,
                        Uri.parse(ExVideo),
                        Uri.parse("Instruction/Path"),
                        Ex_balance_Rec.class));
            }
            else if (ExFrag.equals("circling"))
            {
                _ExerciseList.add(new Exercise(
                        ExID,
                        ExName,
                        ExType,
                        ExDescr,
                        ExInstr,
                        Uri.parse(ExVideo),
                        Uri.parse("Instruction/Path"),
                        Ex_Circling_Rec.class));
            }
            else if (ExFrag.equals("rotation"))
            {
                _ExerciseList.add(new Exercise(
                        ExID,
                        ExName,
                        ExType,
                        ExDescr,
                        ExInstr,
                        Uri.parse(ExVideo),
                        Uri.parse("Instruction/Path"),
                        Ex_Hand_Rotation_Rec.class));
            }
            else if (ExFrag.equals("hand2nose"))
            {
                _ExerciseList.add(new Exercise(
                        ExID,
                        ExName,
                        ExType,
                        ExDescr,
                        ExInstr,
                        Uri.parse(ExVideo),
                        Uri.parse("Instruction/Path"),
                        Ex_Hand_To_Head_Rec.class));
            }
            else if (ExFrag.equals("postural"))
            {
                _ExerciseList.add(new Exercise(
                        ExID,
                        ExName,
                        ExType,
                        ExDescr,
                        ExInstr,
                        Uri.parse(ExVideo),
                        Uri.parse("Instruction/Path"),
                        Ex_postural_Rec.class));
            }
            else if (ExFrag.equals("gaitfourtimes"))
            {
                _ExerciseList.add(new Exercise(
                        ExID,
                        ExName,
                        ExType,
                        ExDescr,
                        ExInstr,
                        Uri.parse(ExVideo),
                        Uri.parse("Instruction/Path"),
                        Ex_Walking_Rec.class));
            }
            else if (ExFrag.equals("gaittwomins"))
            {
                _ExerciseList.add(new Exercise(
                        ExID,
                        ExName,
                        ExType,
                        ExDescr,
                        ExInstr,
                        Uri.parse(ExVideo),
                        Uri.parse("Instruction/Path"),
                        ExFreeWalking.class));
            }
            else if (ExFrag.equals("finger1"))
            {
                _ExerciseList.add(new Exercise(
                        ExID,
                        ExName,
                        ExType,
                        ExDescr,
                        ExInstr,
                        Uri.parse(ExVideo),
                        Uri.parse("Instruction/Path"),
                        ExOneFingerTapping.class));
            }
            else if (ExFrag.equals("finger2"))
            {
                _ExerciseList.add(new Exercise(
                        ExID,
                        ExName,
                        ExType,
                        ExDescr,
                        ExInstr,
                        Uri.parse(ExVideo),
                        Uri.parse("Instruction/Path"),
                        ExTwoFingerTapping.class));
            }
            else if (ExFrag.equals("sliding"))
            {
                _ExerciseList.add(new Exercise(
                        ExID,
                        ExName,
                        ExType,
                        ExDescr,
                        ExInstr,
                        Uri.parse(ExVideo),
                        Uri.parse("Instruction/Path"),
                        ExSliding.class));
            }
            else if (ExFrag.equals("rightwink"))
            {
                _ExerciseList.add(new Exercise(
                        ExID,
                        ExName,
                        ExType,
                        ExDescr,
                        ExInstr,
                        Uri.parse(ExVideo),
                        Uri.parse("Instruction/Path"),
                        Ex_RightWink.class));
                        //FerRecorder_RightWink.class));
            }
            else if (ExFrag.equals("leftwink"))
            {
                _ExerciseList.add(new Exercise(
                        ExID,
                        ExName,
                        ExType,
                        ExDescr,
                        ExInstr,
                        Uri.parse(ExVideo),
                        Uri.parse("Instruction/Path"),
                        Ex_LeftWink.class));
                        //FerRecorder_LeftWink.class));
            }
            else if (ExFrag.equals("happy"))
            {
                _ExerciseList.add(new Exercise(
                        ExID,
                        ExName,
                        ExType,
                        ExDescr,
                        ExInstr,
                        Uri.parse(ExVideo),
                        Uri.parse("Instruction/Path"),
                        Ex_Happy.class));
                //FerRecorder_LeftWink.class));
            }
        }

        ExerciseDataService exerciseDataService = new ExerciseDataService(context);

        ArrayList<Exercise> exerciseDb = exerciseDataService.getAllExercises();


        for (int i = 0; i < exerciseDb.size(); i++) {
            exerciseDataService.deleteExercise(exerciseDb.get(i));
        }

        if (exerciseDataService.getAllExercises().size() == 0) {
            for (int i = 0; i < _ExerciseList.size(); i++) {
                exerciseDataService.insertExercise(_ExerciseList.get(i));
            }
            // exerciseDataService.closeDB();
        }
        _ExerciseList = exerciseDataService.getAllExercises();
    }

    private int getCurrentLocale(String[] languages) {
        Locale locale = Locale.getDefault();
        int defaultOption = 0;
        for (int i = 0; i < languages.length; i++) {
            if (languages[i].contains("en")) {
                defaultOption = i;
            }
            if (languages[i].contains(locale.getLanguage())) {
                return i;
            }
        }
        //If not found, return default
        return defaultOption;
    }


}
