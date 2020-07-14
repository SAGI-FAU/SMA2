package com.sma2.apkinson.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sma2.apkinson.ApplicationState;
import com.sma2.apkinson.DataAccess.DaoMaster;
import com.sma2.apkinson.DataAccess.DaoSession;
import com.sma2.apkinson.DataAccess.ExerciseSessionManager;
import com.sma2.apkinson.MainActivity;
import com.sma2.apkinson.R;
import com.sma2.apkinson.SendData.ConectionWifi;
import com.sma2.apkinson.SendData.SendDataService;
import android.content.SharedPreferences;
import org.greenrobot.greendao.database.Database;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        synchronizeServer(this);
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "apkinsondb");
        Database db = helper.getWritableDb();
        DaoSession daoSession = new DaoMaster(db).newSession();

        setContentView(R.layout.activity_splash_screen);
        //Skip splash screen if in dev-mode
        if (ApplicationState.appUnderDevelopment()) {
            ExerciseSessionManager sessionManager = new ExerciseSessionManager();
            sessionManager.createExerciseSession(this);

            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            ImageView empty_square = findViewById(R.id.empty_square);
            ImageView tulip_elevated = findViewById(R.id.tulip_elevated);
            ImageView tulip_shadow = findViewById(R.id.tulip_shadow);
            TextView char1 = findViewById(R.id.appLetters1);
            TextView char2 = findViewById(R.id.appLetters2);
            TextView char3 = findViewById(R.id.appLetters3);
            TextView char4 = findViewById(R.id.appLetters4);
            TextView char5 = findViewById(R.id.appLetters5);
            TextView char6 = findViewById(R.id.appLetters6);
            TextView char7 = findViewById(R.id.appLetters7);
            TextView char8 = findViewById(R.id.appLetters8);

            Animation anim_empty_square = AnimationUtils.loadAnimation(this, R.anim.anim_empty_square);
            Animation anim_tulip_elevated = AnimationUtils.loadAnimation(this, R.anim.anim_tulip_elevated);
            Animation anim_tulip_shadow = AnimationUtils.loadAnimation(this, R.anim.anim_tulip_shadow);
            Animation anim_char_1 = AnimationUtils.loadAnimation(this, R.anim.anim_char_1);
            Animation anim_char_2 = AnimationUtils.loadAnimation(this, R.anim.anim_char_2);
            Animation anim_char_3 = AnimationUtils.loadAnimation(this, R.anim.anim_char_3);
            Animation anim_char_4 = AnimationUtils.loadAnimation(this, R.anim.anim_char_4);
            Animation anim_char_5 = AnimationUtils.loadAnimation(this, R.anim.anim_char_5);
            Animation anim_char_6 = AnimationUtils.loadAnimation(this, R.anim.anim_char_6);
            Animation anim_char_7 = AnimationUtils.loadAnimation(this, R.anim.anim_char_7);
            Animation anim_char_8 = AnimationUtils.loadAnimation(this, R.anim.anim_char_8);

            empty_square.setAnimation(anim_empty_square);
            tulip_elevated.setAnimation(anim_tulip_elevated);
            tulip_shadow.setAnimation(anim_tulip_shadow);
            char1.setAnimation(anim_char_1);
            char2.setAnimation(anim_char_2);
            char3.setAnimation(anim_char_3);
            char4.setAnimation(anim_char_4);
            char5.setAnimation(anim_char_5);
            char6.setAnimation(anim_char_6);
            char7.setAnimation(anim_char_7);
            char8.setAnimation(anim_char_8);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 6000);
        }



    }

    public void synchronizeServer(final Context context) {

            // función a ejecutar
            ConectionWifi cW= new ConectionWifi(context);
            boolean conection = cW.checkConnection(cW);
            if (conection) {

                SendDataService sds= new SendDataService(context);
                sds.uploadMetadata(sds);
                sds.uploadMedicine(sds);
                sds.uploadAudio(sds);
                sds.uploadMovement(sds);
                sds.uploadVideo(sds);



            }
            else{
                Toast.makeText(context, getString(R.string.wifi), Toast.LENGTH_SHORT).show();
            }

    }

}



