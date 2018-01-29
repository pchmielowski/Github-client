package net.chmielowski.github;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import dagger.Binds;
import dagger.Module;

@Module
abstract class ActivityModule {
    @ActivityContext
    @Binds
    abstract Context bindContext(final AppCompatActivity activity);
}
