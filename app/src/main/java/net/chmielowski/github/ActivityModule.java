package net.chmielowski.github;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import dagger.Module;
import dagger.Provides;

@Module
final class ActivityModule {
    @NonNull
    private final AppCompatActivity activity;

    ActivityModule(@NonNull final AppCompatActivity activity) {
        this.activity = activity;
    }

    @NonNull
    @Provides
    AppCompatActivity provideActivity() {
        return activity;
    }

    @ActivityContext
    @NonNull
    @Provides
    Context provideContext() {
        return activity;
    }
}
