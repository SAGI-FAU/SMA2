package com.sma2.sma2.FeatureExtraction;

import android.content.Context;
import android.graphics.Color;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.sma2.sma2.R;

import java.util.ArrayList;

public class GraphManager {

    Context CONTEXT;

    public GraphManager(Context context){

        CONTEXT=context;

    }

    public void BarGraph(GraphView graph, ArrayList<Integer> x, ArrayList<Float> y, double maxY, double maxX, String Title, String Xlabel, String Ylabel){


        BarGraphSeries<DataPoint> series= new BarGraphSeries<>();

            for (int i = 0; i < x.size(); i++) {
                series.appendData(new DataPoint(x.get(i), y.get(i)), true, 5);
            }

        graph.addSeries(series);

        series.setColor(Color.rgb(255, 140, 0));
        series.setSpacing(5);
        graph.getViewport().setMinY(0.0);
        if (maxY>0){
            graph.getViewport().setMaxY(maxY);
        }
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(maxX);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);
        series.setTitle(Title);
        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle(Xlabel);
        gridLabel.setVerticalAxisTitle(Ylabel);

    }

}
