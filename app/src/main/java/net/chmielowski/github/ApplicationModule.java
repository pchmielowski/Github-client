package net.chmielowski.github;

import android.content.Context;
import android.net.ConnectivityManager;

import java.util.Objects;

import dagger.Module;
import dagger.Provides;

import static android.content.Context.CONNECTIVITY_SERVICE;

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

    @Provides
    ConnectivityManager provideConnectivityManager() {
        return Objects.requireNonNull((ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE));
    }
}
