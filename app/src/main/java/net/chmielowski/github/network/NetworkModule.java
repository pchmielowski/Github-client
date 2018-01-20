package net.chmielowski.github.network;

import android.content.Context;

import net.chmielowski.github.ApplicationContext;
import net.chmielowski.networkstate.BasicNetworkState;
import net.chmielowski.networkstate.NetworkIndicatorViewModel;
import net.chmielowski.networkstate.NetworkState;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class NetworkModule {
    @Provides
    @Singleton
    static NetworkState provideNetworkState(@ApplicationContext final Context context) {
        return new BasicNetworkState(context);
    }

    @Provides
    @Singleton
    static NetworkIndicatorViewModel provideNetworkIndicatorViewModel(final NetworkState networkState) {
        return new NetworkIndicatorViewModel(networkState);
    }

}
