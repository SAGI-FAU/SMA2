package com.sma2.sma2.DataAccess;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class FeatureDA {
    @Id(autoincrement = true)
    private Long id;

    @Property
    private String feature_name;
    @Property
    private Date feature_date;
    @Property
    private String feature_date_str;
    @Property
    private float feature_value;






    public FeatureDA(String feature_name, Date feature_date,
                     float feature_value) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        this.id = null;
        this.feature_name = feature_name;
        this.feature_date = feature_date;
        this.feature_date_str=formatter.format(feature_date);
        this.feature_value = feature_value;
    }

    public FeatureDA(String feature_name) {
        this.id = null;
        this.feature_name = feature_name;
        this.feature_date = null;
        this.feature_date_str=null;
        this.feature_value = 0;
    }

    @Generated(hash = 2040553623)
    public FeatureDA(Long id, String feature_name, Date feature_date,
            String feature_date_str, float feature_value) {
        this.id = id;
        this.feature_name = feature_name;
        this.feature_date = feature_date;
        this.feature_date_str = feature_date_str;
        this.feature_value = feature_value;
    }

    @Generated(hash = 1688047980)
    public FeatureDA() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFeature_name() {
        return this.feature_name;
    }

    public void setFeature_name(String feature_name) {
        this.feature_name = feature_name;
    }

    public Date getFeature_date() {
        return this.feature_date;
    }

    public void setFeature_date(Date feature_date) {
        this.feature_date = feature_date;
    }

    public String getFeature_date_str() {
        return this.feature_date_str;
    }

    public void setFeature_date_str(String feature_date_str) {
        this.feature_date_str = feature_date_str;
    }

    public float getFeature_value() {
        return this.feature_value;
    }

    public void setFeature_value(float feature_value) {
        this.feature_value = feature_value;
    }






}