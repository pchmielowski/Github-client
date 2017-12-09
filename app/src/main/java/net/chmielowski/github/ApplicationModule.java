package net.chmielowski.github;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.gcm.GcmNetworkManager;

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
    static ConnectivityManager provideConnectivityManager(@ApplicationContext final Context context) {
        return (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
    }

    @Provides
    static GcmNetworkManager provideGcmNetworkManager(@ApplicationContext final Context context) {
        return GcmNetworkManager.getInstance(context);
    }

    @Provides
    static LocalBroadcastManager provideBroadcastManager(@ApplicationContext final Context context) {
        return LocalBroadcastManager.getInstance(context);
    }
}
