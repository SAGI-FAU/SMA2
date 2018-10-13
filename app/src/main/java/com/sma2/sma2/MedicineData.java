package com.sma2.sma2;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.sma2.sma2.DataAccess.MedicineDA;

import java.util.Objects;

public class MedicineData {
    private String medicineName;
    private int dose;
    private int intakeTime;
    // not not add setters for this property this property, is for use with database wrapper,
    // defaults to null for auto increment
    private Long id;
    // do not add setters, delete will be handled by MedicineDataServiceTest, defaults to false
    private boolean deleted;

    public MedicineData(String medicineName, int dose, int intakeTime) {
        this.medicineName = medicineName;
        this.dose = dose;
        this.intakeTime = intakeTime;
        this.deleted = false;
        this.id = null;

    }

    //only for use with data access layer, keep consistent with MedicineDA
    public MedicineData(MedicineDA da){
        this.id = da.getId();
        this.medicineName = da.getMedicineName();
        this.dose = da.getDose();
        this.intakeTime = da.getIntakeTime();
        this.id = da.getId();
        this.deleted = da.getDeleted();

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
