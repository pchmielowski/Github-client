package net.chmielowski.github;

import net.chmielowski.github.network.BasicNetworkState;
import net.chmielowski.github.network.NetworkState;

import dagger.Binds;
import dagger.Module;

@Module
abstract class NetworkModule {
    @Binds
    abstract NetworkState bindNetworkState(BasicNetworkState impl);

}
