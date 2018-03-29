package com.multipz.atmiyalawlab.Util;

import android.os.Environment;

/**
 * Created by Admin on 08-11-2017.
 */

public class CheckForSDCard {
    //Check If SD Card is present or not method
    public boolean isSDCardPresent() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }
}
