package com.sma2.sma2;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.Objects;

public class MedicalData {
    private String medicine_name;
    private int dose;

    public MedicalData(String medicine_name, int dose) {
        this.medicine_name = medicine_name;
        this.dose = dose;
    }
    @Override
    public boolean equals(Object o) {
        boolean retVal = false;

        if (o == null) return false;
        if (o == this) return true;

        if(o instanceof MedicalData){
            MedicalData that = (MedicalData) o;
            retVal = (medicine_name != null) && (medicine_name.equals(that.medicine_name));
        }
        return retVal;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {

        return Objects.hash(medicine_name, dose);
    }

    public String getmedicine_name() {
        return medicine_name;
    }

    public void setmedicine_name(String medicine_name) {
        this.medicine_name = medicine_name;
    }

    public int getDose() {
        return dose;
    }

    public void setDose(int dose) {
        this.dose = dose;
    }
}
