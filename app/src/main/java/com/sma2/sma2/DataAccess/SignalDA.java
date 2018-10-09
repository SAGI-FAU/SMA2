package com.sma2.sma2.DataAccess;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.Date;

@Entity
public class SignalDA {
    @Id
    private Long id;

    private long patientDAId;
    private long exerciseDAID;

    @NotNull
    private String signalPath;
    private Date recordingTime;
    private Integer sessionCount;


    @ToOne(joinProperty = "patientDAId")
    private PatientDA patient;

    @ToOne(joinProperty = "exerciseID")
    private  ExerciseDA exerciseDA;



}
