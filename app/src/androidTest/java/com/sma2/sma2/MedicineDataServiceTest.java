package com.sma2.sma2;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.sma2.sma2.DataAccess.DaoMaster;
import com.sma2.sma2.DataAccess.DaoSession;
import com.sma2.sma2.DataAccess.MedicineDA;
import com.sma2.sma2.DataAccess.MedicineDADao;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class MedicineDataServiceTest {

    @Config(constants = BuildConfig.class, sdk = 28)
    @RunWith(RobolectricTestRunner.class)
    public static class TestMedicineDataService {

        MedicineDADao myEntityDao;
        DaoSession daoSession;
        Context appContext;

        @Before
        public void setupTestDataBase() {
//
//            //use roboteletric to create a valid Application Object
//            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(RuntimeEnvironment.application, null, null);
//            Database db = helper.getWritableDb();
//            Assert.assertNotNull(db);
//            DaoSession session = new DaoMaster(db).newSession();
//            session.getMedicineDADao().save(
//                    new MedicineDA(
//                        new Long(1),
//                        1,
//                        false,
//                            new Date(),
//                        "xanax",
//                        500,
//                        11
//
//            ));
//            session.getMedicineDADao().save(
//                    new MedicineDA(
//                        new Long(2),
//                        2,
//                        false,
//                            new Date(),
//                        "ibuprophen",
//                        300,
//                        16
//
//            ));
//            session.getMedicineDADao().save(
//                    new MedicineDA(
//                        new Long(3),
//                        3,
//                        false,
//                            new Date(),
//                        "aspirin",
//                        100,
//                        11
//
//            ));
//            session.getMedicineDADao().save(
//                    new MedicineDA(
//                        new Long(4),
//                        4,
//                        false,
//                            new Date(),
//                        "macumar",
//                        500,
//                        11
//
//            ));

        }
        @Test
        public void useAppContext() throws Exception {
            // Context of the app under test.
            assertEquals("com.sma2.sma2", appContext.getPackageName());
        }


    }
}