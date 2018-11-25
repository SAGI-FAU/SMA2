package com.sma2.sma2.FeatureExtraction.Movement;

import java.util.ArrayList;
import java.util.List;

public class MovementProcessing {


    public MovementProcessing(){

    }


    public List<Double> RemoveGravity(List<Double> signal){

        double alpha = 0.8;
        double gravity=0.0;
        List<Double> SignalOut=new ArrayList<>();
        for (int j=0;j<signal.size();j++){
            gravity=alpha * gravity + (1 - alpha) * signal.get(j);
            SignalOut.add(signal.get(j)- gravity);
        }
        return SignalOut;
    }


    public Double ComputePower (List<Double> signal){

        Double Power=0.0;
        for (int j=0;j<signal.size();j++){
            Power+=Math.pow(signal.get(j),2);
        }
        return Power/(double)signal.size();
    }


    public List<Double> getAccR(List<Double> accX, List<Double> accY, List<Double> accZ){
        List<Double> accR=new ArrayList<>();
        double accxi, accyi, acczi;
        for (int j=0;j<accX.size();j++){
            accxi=Math.pow(accX.get(j),2);
            accyi=Math.pow(accY.get(j),2);
            acczi=Math.pow(accZ.get(j),2);
            accR.add(Math.sqrt(accxi+accyi+acczi));
        }
        return accR;
    }

    public Double ComputeTremor(List<Double> AccX, List<Double> AccY, List<Double> AccZ){


        List<Double> AccXn, AccYn, AccZn, AccR;

        AccXn=RemoveGravity(AccX);
        AccYn=RemoveGravity(AccY);
        AccZn=RemoveGravity(AccZ);

        AccR=getAccR(AccXn, AccYn, AccZn);

        return ComputePower(AccR);


    }



}
