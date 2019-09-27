package com.sma2.sma2;

import android.content.Context;
import android.graphics.Color;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;

public class RadarFigureManager {

    Context CONTEXT;
    public RadarFigureManager(Context context){
        CONTEXT=context;
    }

    public void PlotRadar(RadarChart radarChart, float[] data1, float[] data2, String[] labels){
        RadarData radardata=SetData(data1,data2);

        radarChart.getDescription().setEnabled(false);
        radarChart.animateXY(5000, 5000, Easing.EaseInOutQuad);
        XAxis xAxis=radarChart.getXAxis();
        xAxis.setTextSize(12f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setLabelRotationAngle(90f);

        YAxis yAxis = radarChart.getYAxis();
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(80f);

        Legend l = radarChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
        l.setTextColor(Color.BLACK);
        l.setTextSize(20f);

        radarChart.setExtraOffsets(0,-200,0,-200);
        //radarchart.setBackgroundColor(Color.WHITE);
        radarChart.setScaleY(1f);
        radarChart.setScaleX(1f);
        radarChart.setData(radardata);
        radarChart.invalidate(); // refresh


    }


    public RadarData SetData(float[] data1, float[] data2) {
        int cnt = data1.length;
        ArrayList<RadarEntry> entries1 = new ArrayList<RadarEntry>();
        ArrayList<RadarEntry> entries2 = new ArrayList<RadarEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < cnt; i++) {
            //float val1 = (float) (Math.random() * mul) + min;
            entries1.add(new RadarEntry(data1[i]));

            //float val2 = (float) (Math.random() * mul) + min;
            entries2.add(new RadarEntry(data2[i]));
        }

        String Label_Patient = CONTEXT.getResources().getString(R.string.patient);
        String Label_Control = CONTEXT.getResources().getString(R.string.control);

        RadarDataSet set1 = new RadarDataSet(entries1, Label_Patient);
        set1.setColor(Color.rgb(255, 185, 0));
        set1.setFillColor(Color.rgb(255, 185, 0));
        set1.setDrawFilled(true);
        set1.setFillAlpha(200);
        set1.setLineWidth(2f);
        set1.setValueTextColor(Color.rgb(255, 185, 0));
        set1.setValueTextSize(15f);
        set1.setDrawHighlightCircleEnabled(true);
        set1.setDrawHighlightIndicators(false);

        RadarDataSet set2 = new RadarDataSet(entries2, Label_Control);
        set2.setColor(Color.rgb(0, 200, 200));
        set2.setFillColor(Color.rgb(0, 200, 200));
        set2.setDrawFilled(true);
        set2.setFillAlpha(180);
        set2.setLineWidth(2f);
        set2.setDrawHighlightCircleEnabled(true);
        set2.setDrawHighlightIndicators(false);
        set2.setValueTextColor(Color.rgb(0, 200, 200));
        set2.setValueTextSize(15f);

        RadarData data_radar= new RadarData();

        data_radar.addDataSet(set1);
        data_radar.addDataSet(set2);
        return data_radar;
    }


    public double get_area_chart(float[] data){

        double[] angles= new double[data.length];
        double[] x= new double[data.length+1];
        double[] y= new double[data.length+1];
        double pi=3.14159265;
        float A=0f;
        for (int k=0; k<data.length;k++){
            angles[k]=2*pi*k/data.length;
            x[k]=data[k]*Math.cos(angles[k]);
            y[k]=data[k]*Math.sin(angles[k]);
        }

        x[data.length]=x[0];
        y[data.length]=y[0];

        for (int k=0; k<data.length;k++){
            A+=Math.abs(x[k]*y[k+1]-x[k+1]*y[k]);
        }
        return A*0.5;

    }


}
