package com.multipz.atmiyalawlab.Util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Admin on 11-12-2017.
 */

public class Toaster {
    public static void getToast(Context context, String toast) {
        Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
    }
}
