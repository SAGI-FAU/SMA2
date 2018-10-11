package com.sma2.sma2.DataAccess;

import android.content.Context;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.QueryBuilder;
import com.sma2.sma2.DataAccess.MedicineDADao.Properties;

import java.util.List;

public class MedicineDataService {

    private Context invocationcontext;
    public MedicineDataService(Context context){
        this.invocationcontext = context;
    }

    private class Medicine{} //todo remove for the real thing

    //updarte or inserts a entity
    public void saveMedicine(Medicine medicine){
//      MedicineDA tmp = new MedicineDA(medicine); todo change the real thing
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, "apkinsondb");
        Database db = helper.getWritableDb();
        DaoSession session = new DaoMaster(db).newSession();
        session.getMedicineDADao().save(new MedicineDA());//todo change the real thing
        db.close();
    }

    public void update(Medicine medicine){
//      MedicineDA tmp = new MedicineDA(medicine); todo change the real thing
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, "apkinsondb");
        Database db = helper.getWritableDb();
        DaoSession session = new DaoMaster(db).newSession();
        session.getMedicineDADao().update(new MedicineDA());//todo change the real thing
        db.close();
    }

    public void delete(Medicine medicine){
        //do not delete, just use a dirty flag(deleted)
        //MedicineDA tmp = new MedicineDA(medicine); //todo change the real thing
        MedicineDA tmp = new MedicineDA();//todo
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, "apkinsondb");
        Database db = helper.getWritableDb();
        DaoSession session = new DaoMaster(db).newSession();
        tmp.setDeleted(true);
        session.getMedicineDADao().update(new MedicineDA());//todo change the real thing
        db.close();
    }


    public MedicineDA getMedicineById(Long id){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, "apkinsondb");
        Database db = helper.getReadableDb();
        DaoSession session = new DaoMaster(db).newSession();
        MedicineDADao dao = session.getMedicineDADao();//todo change the real thing
        List<MedicineDA> medication = dao.queryBuilder()
                .where(Properties.Id.eq(id))
                .list();

        db.close();
        return medication.get(0);
    }

    public List<MedicineDA> getAllCurrentMedictation(){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, "apkinsondb");
        Database db = helper.getReadableDb();
        DaoSession session = new DaoMaster(db).newSession();
        MedicineDADao dao = session.getMedicineDADao();

        List<MedicineDA> currentMedication = dao.queryBuilder()
                .where(Properties.Deleted.eq(false))
                .list();

        return currentMedication;

    }
    public List<MedicineDA> getAllMedictation(){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(invocationcontext, "apkinsondb");
        Database db = helper.getReadableDb();
        DaoSession session = new DaoMaster(db).newSession();
        MedicineDADao dao = session.getMedicineDADao();

        List<MedicineDA> currentMedication = dao.queryBuilder()
                .list();

        return currentMedication;

    }

    public String getAppString(){
        return "Apkinson";
    }


}
