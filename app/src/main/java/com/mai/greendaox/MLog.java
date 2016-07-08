package com.mai.greendaox;

import android.util.Log;

/**
 * Created by mai on 16/5/3.
 */
public class MLog {
    public static void log(String msg){
        if(msg == null)
            return;
        Log.e("Mlog-->mai", msg);
    }

    public static void log(String tag, String msg){
        Log.e("Mlog-->mai-->" + tag, msg);
    }
}
