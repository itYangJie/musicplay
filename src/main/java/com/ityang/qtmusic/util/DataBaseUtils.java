package com.ityang.qtmusic.util;

import android.content.Context;

import com.lidroid.xutils.DbUtils;

/**
 * Created by Administrator on 2016/3/2.
 */
public class DataBaseUtils {
    private DataBaseUtils() {

    }

    private static DbUtils dbUtils;

    public static DbUtils getDbUtils(Context ctx) {
        if (dbUtils == null) {
            synchronized (DataBaseUtils.class) {
                if (dbUtils == null) {
                    dbUtils = DbUtils.create(ctx, "likeMusic.db");
                }
            }
        }
        return dbUtils;
    }
}
