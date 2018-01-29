package net.chmielowski.github;

import net.chmielowski.github.data.DataModule;
import net.chmielowski.github.network.NetworkModule;
import net.chmielowski.networkstate.SendNetworkConnectedBroadcast;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = {DataModule.class, ApplicationModule.class, NetworkModule.class})
public interface MainComponent {

    RepositoryComponent plusRepositoryComponent(RepositoryModule module);

    void inject(SendNetworkConnectedBroadcast broadcast);

    ActivityComponent.Builder plusActivityComponent();

    @Component.Builder
    interface Builder {
        MainComponent build();

        @BindsInstance
        Builder application(CustomApplication context);
    }
}
