package net.chmielowski.github;

import net.chmielowski.github.data.DataModule;
import net.chmielowski.github.network.SendNetworkConnectedBroadcast;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {DataModule.class, ApplicationModule.class})
public interface MainComponent {

    RepositoryComponent plusRepositoryComponent(RepositoryModule module);

    void inject(SendNetworkConnectedBroadcast broadcast);

    ActivityComponent plusActivityComponent(ActivityModule module);
}
