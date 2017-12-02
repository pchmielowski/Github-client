package net.chmielowski.github;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;

public final class CustomApplication extends Application {

    private MainComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerMainComponent.create();
    }

    public ActivityComponent component(final AppCompatActivity activity) {
        return component.plusActivityComponent(new ActivityModule(activity));
    }
}
