package net.chmielowski.github.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.gcm.GcmNetworkManager;

import net.chmielowski.github.ApplicationContext;
import net.chmielowski.networkstate.BasicNetworkState;
import net.chmielowski.networkstate.NetworkIndicatorViewModel;
import net.chmielowski.networkstate.NetworkState;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static android.content.Context.CONNECTIVITY_SERVICE;

@Module
public abstract class NetworkModule {
    @Provides
    @Singleton
    static NetworkState provideNetworkState(
            final ConnectivityManager connectivityManager,
            final GcmNetworkManager networkManager,
            final LocalBroadcastManager broadcastManager) {
        return new BasicNetworkState(
                connectivityManager,
                networkManager,
                broadcastManager
        );
    }

    @Provides
    @Singleton
    static NetworkIndicatorViewModel provideNetworkIndicatorViewModel(final NetworkState networkState) {
        return new NetworkIndicatorViewModel(networkState);
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
