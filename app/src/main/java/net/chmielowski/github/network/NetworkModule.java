package net.chmielowski.github.network;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class NetworkModule {
    @Binds
    abstract NetworkState bindNetworkState(BasicNetworkState impl);

}
