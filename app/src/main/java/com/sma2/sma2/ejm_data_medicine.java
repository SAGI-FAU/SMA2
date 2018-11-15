package com.sma2.sma2;

public class ejm_data_medicine {

    private String medicine;
    private int doses;
    private int time;
    private Long id;

    public ejm_data_medicine(Long id, String medicine, int doses, int time) {
        //TODO : It is necessary to test but then it can be eliminated.

        this.id=id;
        this.medicine = medicine;
        this.doses = doses;
        this.time = time;
    }

    public String getMedicine() {
        return medicine;
    }

    public Long getId(){return id;}

    public void setMedicine(String medicine) {
        this.medicine = medicine;
    }

    public int getDoses() {
        return doses;
    }

    public void setDoses(int doses) {
        this.doses = doses;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
