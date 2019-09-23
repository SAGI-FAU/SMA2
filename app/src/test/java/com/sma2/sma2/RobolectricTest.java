package com.sma2.sma2;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sma2.sma2.BuildConfig;
import com.sma2.sma2.ExerciseLogic.DaoMaster;
import com.sma2.sma2.ExerciseLogic.DaoSession;
import com.sma2.sma2.DataAccess.MedicineDA;
import com.sma2.sma2.DataAccess.MedicineDADao;
import com.sma2.sma2.DataAccess.MedicineDataService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.runners.statements.Fail;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Date;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;

@RunWith(RobolectricTestRunner.class) //run test with roboteletric
@Config(constants = BuildConfig.class, sdk = 26)
public class RobolectricTest {

    MedicineDADao medicineDADao;
    DaoSession daoSession;

    @Before
    public void setUp() {

//use roboteletric to create a valid Application Object
//        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(RuntimeEnvironment.application, null, null);
//        SQLiteDatabase db = openHelper.getWritableDatabase();
//        Assert.assertNotNull(db);
//        daoSession = new DaoMaster(db).newSession();
//        medicineDADao = daoSession.getMedicineDADao();
    }


    @Test
    public void t1() {
//        Log.d("Start", "it begins");
//        MedicineDA myEntity = new MedicineDA(new Long(3), 333, false, new Date(),
//                "xanax", 500, 10);
//        Log.d("Info", myEntity.toString());
//        medicineDADao.insert(myEntity);
//        Log.d("Info", medicineDADao.getTablename());
//        MedicineDA result = medicineDADao.load(new Long(3));
//        assertNotNull(result);
//        assertEquals(result.getId(), myEntity.getId());
//        Log.d("Start", "it ends");

    }
    @Test
    public void testMedicineDataService(){
//        MedicineDataService dataService = new MedicineDataService(RuntimeEnvironment.application.getApplicationContext());

    }
}

