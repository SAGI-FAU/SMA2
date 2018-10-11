package com.sma2.sma2;

import android.content.Context;
import android.util.Log;

import com.sma2.sma2.DataAccess.MedicineDA;
import com.sma2.sma2.DataAccess.MedicineDataService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class MedicineDataServiceTest {


    @Mock
    Context mMockContext;

    @Test
    public void testMockedContext(){

    }

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    private static final String FAKE_STRING = "Apkinson";

    @Test
    public void readStringFromContext_LocalizedString() {
        // Given a mocked Context injected into the object under test...
//        when(mMockContext.getString(R.string.app_name))
//                .thenReturn(FAKE_STRING);
//        MedicineDataService dataService = new MedicineDataService(mMockContext);
//        List<MedicineDA> test = dataService.getAllMedictation();
//        if(test != null){
//            Log.i("Test", test.toString() );
//
//        }
//        Log.i("Test", "Alles mist" );
//        // ...when the string is returned from the object under test...
//        String result = dataService.getAppString();
//
//        // ...then the result should be the expected one.
//        assertThat(result, is(FAKE_STRING));
    }


}