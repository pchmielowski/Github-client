package net.chmielowski.github;

import android.app.Application;

public final class CustomApplication extends Application {

    private MainComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerMainComponent.create();
    }

    public MainComponent component() {
        return component;
    }
}
