package com.brsatalay.htmltablebuilder.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class UtilsFile {

    public static String getAssetFile(Context mContext, String fileName){
        BufferedReader reader = null;
        StringBuffer result = new StringBuffer();
        try {
            reader = new BufferedReader(
                    new InputStreamReader(mContext.getAssets().open(fileName), "UTF-8"));

            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                result.append(mLine).append("\n");
            }
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
        return result.toString();
    }

    public static String stringToFile(Context mContext, String fileName, String fileBody){
        try {
            File directory = new File(htmlPath(mContext));

            if (!directory.isDirectory())
                directory.mkdirs();

            File newFile = new File(directory, fileName);

            FileWriter out = new FileWriter(newFile);
            out.write(fileBody);
            out.close();
            return newFile.getAbsolutePath();
        } catch (IOException e) {
            Log.e("UtilsFile$stringToFile", e.getMessage());
            return "";
        }
    }

    private static String htmlPath(Context mContext){
        return mContext.getDir("filesdir", Context.MODE_PRIVATE) + File.separator + "html";
    }
}
