package net.chmielowski.github;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import dagger.Module;
import dagger.Provides;

@Module
final class ActivityModule {
    private final AppCompatActivity activity;

    ActivityModule(final AppCompatActivity activity) {
        this.activity = activity;
    }

    @Provides
    AppCompatActivity provideActivity() {
        return activity;
    }

    @ActivityContext
    @Provides
    static Context provideContext(final AppCompatActivity activity) {
        return activity;
    }
}
