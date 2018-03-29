package com.multipz.atmiyalawlab.Util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Constant_method {
    static SimpleDateFormat formatterTime = new SimpleDateFormat("KK:mm aa");
    static SimpleDateFormat formatterDate = new SimpleDateFormat("MMM dd,yyyy");

    public static String Get_id(Context c) {
        TelephonyManager telephonyManager = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);

        @SuppressLint("MissingPermission") String IMEI = telephonyManager.getDeviceId();
        //String id = Secure.getString(c.getContentResolver(),Secure.ANDROID_ID);
        //System.out.println(" Ud id " + id);
        return IMEI;
    }

    public static boolean checkConn(Context ctx) {
        ConnectivityManager connectivity = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    public static String cu_time(String date) {
        try {
            //  date = new SimpleDateFormat("MMM dd,yyyy KK:mm aa", Locale.getDefault()).format(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(date));
            date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new SimpleDateFormat("yyyy-MM-dd").parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
        //	return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault()).format(new Date());
    }
    public static String cu_datecase(String date) {
        try {
            //  date = new SimpleDateFormat("MMM dd,yyyy KK:mm aa", Locale.getDefault()).format(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(date));
            date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new SimpleDateFormat("yyyy-MM-dd").parse(date));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
        //	return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault()).format(new Date());
    }

    public static String cu_datemonth(String date) {
        try {
            //  date = new SimpleDateFormat("MMM dd,yyyy KK:mm aa", Locale.getDefault()).format(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(date));
            date = new SimpleDateFormat("yyyy-MMM-dd", Locale.getDefault()).format(new SimpleDateFormat("yyyy-MM-dd").parse(date));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
        //	return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault()).format(new Date());
    }
    public static String current_date() {
        return new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    }

    public static String current_time(String time) {
        try {
            Date date = new SimpleDateFormat("hh:mm:ss").parse(time);
            return new SimpleDateFormat("hh:mm aa").format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    }

    public static String next_date() {
        return new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date().getDay() + 1);
    }

    public static String getDate(String strDate) {
        Long datetimestamp = Long.parseLong(strDate.replaceAll("\\D", ""));
        Timestamp stamp = new Timestamp(datetimestamp);
        Date date = new Date(stamp.getTime());
        //Integer datetimestamp = Integer.parseInt(strDate.replaceAll("\\D", ""));
        //Date date = new Date(datetimestamp);
        return formatterDate.format(date);
    }

    public static String getDateDDMMYYYY(String strDate) {
        Long datetimestamp = Long.parseLong(strDate.replaceAll("\\D", ""));
        Timestamp stamp = new Timestamp(datetimestamp);
        Date date = new Date(stamp.getTime());
        return new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(date);
    }

    public static String getTime(String strDate) {
        Long datetimestamp = Long.parseLong(strDate.replaceAll("\\D", ""));
        Timestamp stamp = new Timestamp(datetimestamp);
        Date date = new Date(stamp.getTime());
        //Integer datetimestamp = Integer.parseInt(strDate.replaceAll("\\D", ""));
        //Date date = new Date(datetimestamp);
        formatterTime.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
        return formatterTime.format(date);
    }

    public static String getDateFormate(String strDate, String strFormate) {
        Long datetimestamp = Long.parseLong(strDate.replaceAll("\\D", ""));
        Timestamp stamp = new Timestamp(datetimestamp);
        Date date = new Date(stamp.getTime());
        return new SimpleDateFormat(strFormate, Locale.getDefault()).format(date);
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target)
                && android.util.Patterns.EMAIL_ADDRESS.matcher(target)
                .matches();
    }

//	public static boolean isValidMobile(String phone) {
//		return android.util.Patterns.PHONE.matcher(phone).matches();
//	}

    public static boolean isValidMobile(String phone) {
        boolean check = false;
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
//			if(phone.length() < 6 || phone.length() > 13) {
            if (phone.length() != 10) {
                check = false;
//				txtPhone.setError("Not Valid Number");
            } else {
                check = true;
            }
        } else {
            check = false;
        }
        return check;
    }

    public final static boolean isPasswordValid(String email) {

        boolean isValid = false;

        String expression = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,})";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }
}
