package com.project.fortuna.meterreading.DB;

import android.content.Context;
import android.database.SQLException;

import java.util.List;

/**
 * Created by Akautsar Efendi on 2/28/2018.
 */

public abstract class BaseDAO {

    protected Context context;
    protected DBHelper db;

    public BaseDAO(Context context) {
        this.context = context;
    }

    public void save(){
        db = new DBHelper(context);
        db.save(this);
    }

    public void update(){
        db = new DBHelper(context);
        db.update(this);
    }

    public void delete(){
        db = new DBHelper(context);
        db.delete(this);
    }

    public void close() {
        db.close();
    }


    public abstract List<?> retrieveAll();

    public abstract Object retrieveByID();
}
