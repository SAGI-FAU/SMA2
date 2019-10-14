package com.sma2.sma2.DataAccess;

import android.content.Context;

import com.alibaba.fastjson.parser.Feature;
import com.sma2.sma2.R;

import org.greenrobot.greendao.database.Database;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FeatureDataService {


    public String jitter_name="Jitter";
    public String vrate_name="vrate";
    public String intonation_name="intonation";
    public String wer_name="wer";
    public String pronun_name="pronun";
    public String area_speech_name="area speech";
    public String perc_tapping1_name="perc tapping one";
    public String perc_tapping2_name="perc tapping two";

    public String veloc_tapping1_name="veloc tapping one";
    public String veloc_tapping2_name="veloc tapping two";

    public String precision_tapping1_name="precision tapping one";
    public String precision_tapping2_name="precision tapping two";


    public String perc_sliding_name="perc sliding";
    public String area_tapping_name="area tapping";
    public String tremor_name="tremor";
    public String regularity_circles_right_name="regularity_circles_right";
    public String regularity_circles_left_name="regularity_circles_left";
    public String regularity_pronation_right_name="regularity_pronation_right";
    public String regularity_pronation_left_name="regularity_pronation_left";
    public String regularity_kinetic_right_name="regularity_kinetic_right";
    public String regularity_kinetic_left_name="regularity_kinetic_left";
    public String tremor_right_name="tremor_right";
    public String tremor_left_name="tremor_left";

    public String freeze_index_name="freeze index";
    public String posture_name="posture";
    public String N_strides_name="N strides";
    public String duration_strides_name="Duration strides";
    public String area_movement_name="area movement";
    public String area_total_name="area_total";


    private Context invocationcontext;
    private String dbname;
    public FeatureDataService(Context context){
        this.invocationcontext = context;
        this.dbname = context.getResources().getString(R.string.databasename);
    }



    public void save_feature(String feature_name, Date feature_date, Float feature_value){

        FeatureDA feature=new FeatureDA(feature_name, feature_date, feature_value);
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, dbname);
        Database db = helper.getWritableDb();
        DaoSession session = new DaoMaster(db).newSession();
        session.getFeatureDADao().save(feature);
        db.close();
    }

    public void save_feature(FeatureDA feature){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, dbname);
        Database db = helper.getWritableDb();
        DaoSession session = new DaoMaster(db).newSession();
        session.getFeatureDADao().save(feature);
        db.close();
    }



    public List<FeatureDA> get_feature_by_name(String feature_name){


        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, dbname);
        Database db = helper.getReadableDb();
        DaoSession session = new DaoMaster(db).newSession();
        FeatureDADao dao = session.getFeatureDADao();
        List<FeatureDA> features = dao.queryBuilder()
                .where(FeatureDADao.Properties.Feature_name.eq(feature_name))
                .list();

        db.close();

        return features;

    }


    public List<FeatureDA> get_last_10_features_by_name(String feature_name){


        List<FeatureDA> features=get_feature_by_name(feature_name);

        if (features.size()==0){
            FeatureDA feat=new FeatureDA(feature_name);
            List<FeatureDA> list_empty=new ArrayList<>();
            list_empty.add(feat);
            return list_empty;
        }

        else if (features.size()<10) {

            return features;
        }
        else{

            List<FeatureDA> features_sorted=sort_feat_days(features);
            List<FeatureDA> features_sorted_10=new ArrayList<>();

            for (int i=features_sorted.size()-1;i>features_sorted.size()-10;i--){
                features_sorted_10.add(features_sorted.get(i));
            }
            return features_sorted_10;
        }

    }


    public FeatureDA get_last_feat_value(String feature_name){
        List<FeatureDA> features=get_feature_by_name(feature_name);
        if (features.size()==0){
            return new FeatureDA(feature_name);
        }
        else{
            List<FeatureDA> features_sorted=sort_feat_days(features);
            return features_sorted.get(features.size()-1);
        }
    }




    public List<FeatureDA> sort_feat_days(List<FeatureDA> features){


        ArrayList<Date>dateList=new ArrayList<>();
        ArrayList<Date>dateList2=new ArrayList<>();
        ArrayList<Float>values=new ArrayList<>();

        for (int i=0;i<features.size();i++){
            dateList.add(features.get(i).getFeature_date());
            dateList2.add(features.get(i).getFeature_date());
            values.add(features.get(i).getFeature_value());
        }
        Collections.sort(dateList, new Comparator<Date>(){
            public int compare(Date date1, Date date2){
                return date1.compareTo(date2);
            }
        });

        List<FeatureDA> features_sorted=new ArrayList<>();
        for (int i=0;i<dateList.size();i++){
            int index=dateList.indexOf(dateList2.get(i));
            Date date_=dateList2.get(i);

            FeatureDA feat=new FeatureDA(features.get(i).getFeature_name(),date_, features.get(index).getFeature_value() );
            features_sorted.add(feat);

        }

        return  features_sorted;

    }


}
