package com.sma2.sma2.FeatureExtraction.Speech.tools;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TOMAS on 08/10/2018
 *
 * This file includes the following methods:
 * - List to array conversion (method: listtoarray)
 * - Reading text files to get feature matrices
 * - Calculate first order discrete difference from array (method: diff).
 * - Absolute value of an array (method: absArr).
 * - Find (method: find)
 * - Interpolation (method: interpolate).

 */

public class array_manipulation {

    public array_manipulation(){};

    /**
     * Convert lists to arrays
     * @param vals - List to be converted
     * @return Array converted from list
     */
    public float[] listtofloat(List vals)
    {
        float [] means = new float[vals.size()];
        for(int i=0;i<vals.size();i++)
        {
            means[i] = (float) vals.get(i);
        }
        return means;
    }

    public double[] dlisttoarray(List<Double> vals)
    {
        double [] means = new double[vals.size()];
        for(int i=0;i<vals.size();i++)
        {
            means[i] = vals.get(i);
        }
        return means;
    }

    /**
     * Calculate first order discrete difference from array.
     * @param x - Array
     * @return Array with the first order differences from x
     */
    public float[] diff(float[] x)
    {
        float[] diffV = new float[x.length-1];
        for (int i=0;i< (x.length-1);i++)
        {
            diffV[i] = x[i+1]-x[i];
        }
        return diffV;
    }

    /**
     * Absolute value of array elements.
     * @param x - Array
     * @return Array of absolute values of x.
     */
    public float[] absArr(float[] x)
    {
        float[] y = new float[x.length];
        for(int i=0;i<x.length;i++)
        {
            y[i]=Math.abs(x[i]);
        }
        return y;
    }

    /**
     * Sum elements from array.
     * @param x - Array
     * @return Elements summed over the array.
     */
    public float sumArray(float[] x)
    {
        float s = 0;
        for(int i = 0; i<x.length; i++)
        {
            s+=x[i];
        }
        return s;
    }

    /**
     *Power each element from array
     * @param x - Array
     * @return Powered array
     */
    public float[] powerArray(float[] x){
        float[] y = new float[x.length];
        for(int i = 0; i<x.length; i++)
        {
            y[i] = x[i]*x[i];
        }
        return y;
    }

    /**
     * Return elements depending on condition
     * @param x - Array
     * @param val - Condition
     * @param opt - Condition to be applied (Integer values)
     *            0: Find elements in 'x'<'val'
     *            1: Find elements in 'x'<='val'
     *            2: Find elements in 'x'>'val'
     *            3: Find elements in 'x'>='val'
     *            4: Find elements in 'x'='val'
     *            5: Find elements in 'x'!='val'
     * @return List with elements that fulfil the condition 'opt'
     */
    public List find(float[] x,float val,int opt)
    {
        List<Integer> res = new ArrayList<>();
        switch (opt)
        {
            //Less than
            case 0: {
                for (int i = 0; i < x.length;i++) {
                    if (x[i] < val) {
                        res.add(i);
                    }
                }
                break;
            }
            //Less or equal than
            case 1:{
                for (int i = 0; i < x.length;i++) {
                    if (x[i] <= val) {
                        res.add(i);
                    }
                }
                break;
            }
            //Greater than
            case 2:{
                for (int i = 0; i < x.length;i++) {
                    if (x[i] > val) {
                        res.add(i);
                    }
                }
                break;
            }
            //Greater or equal than
            case 3:{
                for (int i = 0; i < x.length;i++) {
                    if (x[i] >= val) {
                        res.add(i);
                    }
                }
                break;
            }
            //Equal to
            case 4:{
                for (int i = 0; i < x.length;i++) {
                    if (x[i] == val) {
                        res.add(i);
                    }
                }
                break;
            }
            //Different than
            case 5:{
                for (int i = 0; i < x.length;i++) {
                    if (x[i] != val) {
                        res.add(i);
                    }
                }
                break;
            }

        }
        return res;
    }

    /***
     * Interpolating method
     * @param start start of the interval
     * @param end end of the interval
     * @param count count of output interpolated numbers
     * @return array of interpolated number with specified count
     */
    public float[] interpolate(float start, float end, int count) {
        if (count < 2) {
            throw new IllegalArgumentException("interpolate: illegal count!");
        }
        float[] array = new float[count + 1];
        for (int i = 0; i <= count; ++ i) {
            array[i] = start + i * (end - start) / count;
        }
        return array;
    }

    /**
     * Read a text file and put it into a List. Could be arrays or features mxn matrices with 'm'
     * samples and 'n' features
     * @param filepath - Path of the text file to read
     * @return List containing the data on the text file
     */
    private List<Float> readFileIntoArray (File filepath) {
        String file = readtxt(filepath);
        List<Float> ret = new ArrayList<>();
        String[] lines = file.split("\n");
        for (String line : lines) {
            String[] values = line.split(" ");
            for (String value : values) ret.add(Float.valueOf(value));
        }
        return ret;
    }
    //Read plain text file (rows: samples, gaussians, etc. columns: features)
    private String readtxt(File file) {

        //Read text from file
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
            String ret = text.toString();

            return ret;
        } catch (IOException e) {
            Log.d("ERROR", e.getMessage());
            e.printStackTrace();
        }

        return "0";
    }


}
