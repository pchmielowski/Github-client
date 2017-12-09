package net.chmielowski.github.utils;

import android.app.Service;
import android.support.v7.app.AppCompatActivity;

import net.chmielowski.github.CustomApplication;

public final class ApplicationUtils {
    public static CustomApplication application(final Service service) {
        return (CustomApplication) service.getApplication();
    }

    public static CustomApplication application(final AppCompatActivity activity) {
        return (CustomApplication) activity.getApplication();
    }
}
