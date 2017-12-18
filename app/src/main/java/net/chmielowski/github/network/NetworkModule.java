package net.chmielowski.github.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.gcm.GcmNetworkManager;

import net.chmielowski.github.ApplicationContext;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import static android.content.Context.CONNECTIVITY_SERVICE;

@Module
public abstract class NetworkModule {
    @Binds
    abstract NetworkState bindNetworkState(BasicNetworkState impl);

    @Provides
    public static ConnectivityManager provideConnectivityManager(@ApplicationContext final Context context) {
        return (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
    }

    @Provides
    public static GcmNetworkManager provideGcmNetworkManager(@ApplicationContext final Context context) {
        return GcmNetworkManager.getInstance(context);
    }

    @Provides
    public static LocalBroadcastManager provideBroadcastManager(@ApplicationContext final Context context) {
        return LocalBroadcastManager.getInstance(context);
    }

}
