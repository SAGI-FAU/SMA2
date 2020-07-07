package com.sma2.apkinson.DataAccess;

import android.content.Context;

import com.sma2.apkinson.R;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.QueryBuilder;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FeatureDataService {

    private DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

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

        Date dateflook=new Date();
        try {
            dateflook= formatter.parse(formatter.format(feature_date));
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        FeatureDA feature=new FeatureDA(feature_name, dateflook, feature_value);
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



    private List<FeatureDA> get_feature_by_name(String feature_name){


        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, dbname);
        Database db = helper.getReadableDb();
        DaoSession session = new DaoMaster(db).newSession();
        FeatureDADao dao = session.getFeatureDADao();
        List<FeatureDA> features = dao.queryBuilder()
                .where(FeatureDADao.Properties.Feature_name.eq(feature_name)).orderAsc(FeatureDADao.Properties.Feature_date)
                .list();

        db.close();

        return features;

    }


    private List<FeatureDA> get_feature_by_date_and_name(String name, Date feature_date){


        String dateflook= formatter.format(feature_date);

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, dbname);
        Database db = helper.getReadableDb();
        DaoSession session = new DaoMaster(db).newSession();
        FeatureDADao dao = session.getFeatureDADao();
        QueryBuilder<FeatureDA> qb = dao.queryBuilder();
        qb.where(FeatureDADao.Properties.Feature_name.eq(name),
        qb.and(FeatureDADao.Properties.Feature_name.eq(name),FeatureDADao.Properties.Feature_date_str.eq(dateflook)));
        qb.orderAsc(FeatureDADao.Properties.Feature_date);
        List<FeatureDA> features = qb.list();
        db.close();

        return features;

    }




    public List<FeatureDA> get_last_10_features_by_name(String feature_name){


        List<FeatureDA> features=get_avg_all_feat_per_day(feature_name);

        if (features.size()<10) {
            return features;
        }
        else{

            List<FeatureDA> features_sorted_10=new ArrayList<>();

            for (int i=features.size()-1;i>features.size()-10;i--){
                features_sorted_10.add(features.get(i));
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
            return features.get(features.size()-1);
        }
    }







    public float get_avg_feat(List<FeatureDA> features){
        int cont_great_zero=0;
        float sum_feat=0;
        FeatureDA featurec;
        for (int i=0;i<features.size();i++){
            featurec=features.get(i);

            if (featurec.getFeature_value()!=0){
              sum_feat+=featurec.getFeature_value();
              cont_great_zero+=1;
            }

        }

        if (cont_great_zero==0){

            return 0;
        }
        return sum_feat/cont_great_zero;
    }


    public FeatureDA get_avg_feat_day(String feat_name, Date date){

    List<FeatureDA>features_date=get_feature_by_date_and_name(feat_name, date);
    float avgfeat=get_avg_feat(features_date);
        return new FeatureDA(feat_name, date, avgfeat);
    }



    public List<FeatureDA> get_avg_all_feat_per_day(String feat_name){

        List<FeatureDA> features=get_feature_by_name(feat_name);

        if (features.size()==0){
            return features;
        }

        FeatureDA feat0=features.get(0);
        Date curr0=feat0.getFeature_date();
        String curr0str=formatter.format(curr0);
        List<FeatureDA> features_out=new ArrayList<>();
        FeatureDA featday=get_avg_feat_day(feat_name, curr0);
        features_out.add(featday);

        for (int i=0;i<features.size();i++){
            FeatureDA feat=features.get(i);

            Date curr=feat.getFeature_date();
            String currstr=formatter.format(curr);

            if(!currstr.equals(curr0str)){

                featday=get_avg_feat_day(feat_name, curr);
                features_out.add(featday);
                curr0str=currstr;

            }

        }
        return features_out;
    }

}
