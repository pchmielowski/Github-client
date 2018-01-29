package net.chmielowski.github;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import dagger.Module;
import dagger.Provides;

@Module
abstract class ActivityModule {
    @ActivityContext
    @Provides
    static Context provideContext(final AppCompatActivity activity) {
        return activity;
    }
}
