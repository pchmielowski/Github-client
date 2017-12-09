package net.chmielowski.github;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
class ApplicationModule {
    private final Context context;

    ApplicationModule(final Context context) {
        this.context = context;
    }

    @ApplicationContext
    @Provides
    Context provideContext() {
        return context;
    }
}
