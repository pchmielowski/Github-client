package net.chmielowski.github.utils;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateFormatter {

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private DateFormatter() {
        // Utility class
    }

    public static String format(@NonNull final Date date) {
        return format.format(date);
    }

}