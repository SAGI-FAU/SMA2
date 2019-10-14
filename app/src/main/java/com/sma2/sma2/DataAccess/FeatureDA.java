package com.sma2.sma2.DataAccess;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

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
    private float feature_value;
    @Generated(hash = 244341415)
    public FeatureDA(Long id, String feature_name, Date feature_date,
            float feature_value) {
        this.id = id;
        this.feature_name = feature_name;
        this.feature_date = feature_date;
        this.feature_value = feature_value;
    }


    public FeatureDA(String feature_name, Date feature_date,
                     float feature_value) {
        this.id = null;
        this.feature_name = feature_name;
        this.feature_date = feature_date;
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
    public float getFeature_value() {
        return this.feature_value;
    }
    public void setFeature_value(float feature_value) {
        this.feature_value = feature_value;
    }


}