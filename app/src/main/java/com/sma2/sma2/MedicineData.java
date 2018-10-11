package com.sma2.sma2;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Objects;

public class MedicineData {
    private String medicineName;
    private int dose;
    private int intakeTime;

    public MedicineData(String medicineName, int dose, int intakeTime) {
        this.medicineName = medicineName;
        this.dose = dose;
        this.intakeTime = intakeTime;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public int getDose() {
        return dose;
    }

    public void setDose(int dose) {
        this.dose = dose;
    }

    public int getIntakeTime() {
        return intakeTime;
    }

    public void setIntakeTime(int intakeTime) {
        this.intakeTime = intakeTime;
    }

    @Override
    public boolean equals(Object o) {
        boolean retVal = false;

        if (o == null) return false;
        if (o == this) return true;

        if(o instanceof MedicineData){
            MedicineData that = (MedicineData) o;
            retVal = (medicineName != null) && (medicineName != null) && (medicineName.equals(that.medicineName)) && (intakeTime == that.intakeTime);
        }
        return retVal;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {

        return Objects.hash(medicineName, dose, intakeTime);
    }
}
