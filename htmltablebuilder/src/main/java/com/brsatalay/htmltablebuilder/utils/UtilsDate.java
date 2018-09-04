package com.brsatalay.htmltablebuilder.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilsDate {
    public static String changeStrDatePattern(String dateStr, String oldPattern, String newPattern) {
        Date date = getStrToDateTime(dateStr, oldPattern);
        return getDateToStr(date, newPattern);
    }

    public static Date getStrToDateTime(String strDate) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String tempDateStr = strDate;
        if (strDate.length() == "dd/MM/yyyy".length()) {
            tempDateStr = strDate + " 00:00:00";
        }

        try {
            return format.parse(tempDateStr);
        } catch (ParseException var4) {
            var4.printStackTrace();
            return null;
        }
    }

    public static Date getStrToDateTime(String strDate, String dateFormat) {
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);

        try {
            return format.parse(strDate);
        } catch (ParseException var4) {
            var4.printStackTrace();
            return null;
        }
    }

    public static String getDateToStr(Date tarih, String format) {
        if (tarih == null) {
            return "";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(tarih);
        }
    }
}
