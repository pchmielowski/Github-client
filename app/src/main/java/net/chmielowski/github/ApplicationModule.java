package net.chmielowski.github;

import android.content.Context;

import dagger.Binds;
import dagger.Module;

@Module
abstract class ApplicationModule {

    @ApplicationContext
    @Binds
    abstract Context bindContext(final CustomApplication app);
}
