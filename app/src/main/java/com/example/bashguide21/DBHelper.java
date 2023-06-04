package com.example.bashguide21;

import android.content.Context;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DBHelper extends SQLiteAssetHelper {

    public DBHelper(Context context) {
        super(context, "DB.db", null, 5);
        setForcedUpgrade();
    }
}
