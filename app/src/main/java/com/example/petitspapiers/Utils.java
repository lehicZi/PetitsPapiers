package com.example.petitspapiers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class Utils {

    public static void showMessage(final String titre,
                                   final String message,
                                   final Context context)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle(titre);
        builder.setMessage(message);
        builder.show();
    }

    public static void openOtherActivity(final Class<?> classToOpen, final Activity activity)
    {
        final Intent intent = new Intent (activity, classToOpen);
        activity.startActivity(intent);
        activity.finish();
    }

    public static void refreshView(Activity activity, Class<?> classTorefresh){

        Intent i = new Intent(activity, classTorefresh);
        activity.finish();
        activity.overridePendingTransition(0,0);
        activity.startActivity(i);
        activity.overridePendingTransition(0,0);

    }

    public static boolean stringContainsString(final String s1,
                                               final String s2)
    {
        final String s1Comparable = StringUtils.stripAccents(s1).toLowerCase();
        final String s2Comparable = StringUtils.stripAccents(s2).toLowerCase();

        return s1Comparable.contains(s2Comparable);
    }

    public static void copyFile(FileInputStream fromFile, FileOutputStream toFile) throws IOException {
        FileChannel fromChannel = null;
        FileChannel toChannel = null;
        try {
            fromChannel = fromFile.getChannel();
            toChannel = toFile.getChannel();
            fromChannel.transferTo(0, fromChannel.size(), toChannel);
        } finally {
            try {
                if (fromChannel != null) {
                    fromChannel.close();
                }
            } finally {
                if (toChannel != null) {
                    toChannel.close();
                }
            }
        }
    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

