package com.sma2.sma2.DataAccess;

import android.content.Context;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.QueryBuilder;
import com.sma2.sma2.DataAccess.MedicineDADao.Properties;
import com.sma2.sma2.DataAccess.DaoMaster;
import com.sma2.sma2.DataAccess.DaoSession;
import com.sma2.sma2.R;

import java.util.List;

public class MedicineDataService {

    private Context invocationcontext;
    private String dbname;
    public MedicineDataService(Context context){
        this.invocationcontext = context;
        this.dbname = context.getResources().getString(R.string.databasename);
    }

    //updarte or inserts a entity
    public void saveMedicine(MedicineDA medicine){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, dbname);
        Database db = helper.getWritableDb();
        DaoSession session = new DaoMaster(db).newSession();
        session.getMedicineDADao().save(medicine);
        db.close();
    }

    public void update(MedicineDA medicine){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, dbname);
        Database db = helper.getWritableDb();
        DaoSession session = new DaoMaster(db).newSession();
        session.getMedicineDADao().update(medicine);
        db.close();
    }

    public void delete(MedicineDA medicine){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, dbname);
        Database db = helper.getWritableDb();
        DaoSession session = new DaoMaster(db).newSession();
        medicine.setDeleted(true);
        session.getMedicineDADao().update(medicine);
        db.close();
    }


    public MedicineDA getMedicineById(Long id){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, dbname);
        Database db = helper.getReadableDb();
        DaoSession session = new DaoMaster(db).newSession();
        MedicineDADao dao = session.getMedicineDADao();
        List<MedicineDA> medication = dao.queryBuilder()
                .where(Properties.Id.eq(id))
                .list();

        db.close();
        return medication.get(0);
    }


    public List<MedicineDA> getMedicineByTime(int time){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, dbname);
        Database db = helper.getReadableDb();
        DaoSession session = new DaoMaster(db).newSession();
        MedicineDADao dao = session.getMedicineDADao();
        List<MedicineDA> medication = dao.queryBuilder()
                .where(Properties.IntakeTime.eq(time))
                .list();

        db.close();
        return medication;
    }

    public List<MedicineDA> getAllCurrentMedictation(){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, dbname);
        Database db = helper.getReadableDb();
        DaoSession session = new DaoMaster(db).newSession();
        MedicineDADao dao = session.getMedicineDADao();

        List<MedicineDA> currentMedication = dao.queryBuilder()
                .where(Properties.Deleted.eq(false))
                .list();

        return currentMedication;

    }
    public List<MedicineDA> getAllMedictation(){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, dbname);
        Database db = helper.getReadableDb();
        DaoSession session = new DaoMaster(db).newSession();
        MedicineDADao dao = session.getMedicineDADao();

        List<MedicineDA> currentMedication = dao.queryBuilder()
                .list();

        return currentMedication;

    }

}
