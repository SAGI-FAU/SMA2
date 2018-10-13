package com.sma2.sma2.FeatureExtraction.Tapping.tools;

import java.util.ArrayList;

public class TransitionDectector {
    public TransitionDectector() {
    }

    public ArrayList<ArrayList<Integer>> detect(float[] f0){
        ArrayList<Integer> index_array_onset = new ArrayList<Integer>();
        ArrayList<Integer> index_array_offset = new ArrayList<Integer>();
//        int start;
//        int end;
        for(int i = 1;i < f0.length;i++){
            int round_1 = (int) Math.round(f0[i-1]);
            int round = (int) Math.round(f0[i]);
            if((round_1 == 0) && (round > 0)){
//                start = (int) Math.round(i*0.02*Fs - 0.08*Fs);
//                end = (int) Math.ceil(i*0.02*Fs + 0.08*Fs);
//                float[] transition_frame = Arrays.copyOfRange(signal,start,end);
//                sigproc sigprocObject = new sigproc();
//                List<float[]> dataFrame = sigprocObject.sigframe(transition_frame,Fs,0.04f,0.02f);
                index_array_onset.add(i);
            }else if((round_1 > 0) && (round == 0)){
//                start = (int) Math.ceil(i*0.02*Fs - 0.08*Fs);
//                end = (int) Math.ceil(i*0.02*Fs + 0.08*Fs);
//                float[] transition_frame = Arrays.copyOfRange(signal,start,end);
//                sigproc sigprocObject = new sigproc();
//                List<float[]> dataFrame = sigprocObject.sigframe(transition_frame,Fs,0.04f,0.02f);
                index_array_offset.add(i);
            }
        }

        if(index_array_offset.get(0) < index_array_onset.get(0)){
            index_array_onset.add(0,0);
        }
        if(index_array_offset.get(index_array_offset.size()-1) <  index_array_onset.get(index_array_onset.size()-1)){
            index_array_offset.add(f0.length);
        }

        ArrayList<ArrayList<Integer>> data_return = new ArrayList<ArrayList<Integer>>();
        data_return.add(index_array_onset);
        data_return.add(index_array_offset);
        return data_return;
    }
}
