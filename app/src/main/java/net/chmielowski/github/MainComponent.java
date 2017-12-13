package net.chmielowski.github;

import net.chmielowski.github.network.SendNetworkConnectedBroadcast;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {DataModule.class, ApplicationModule.class})
public interface MainComponent {

//    ActivityComponent plusActivityComponent(ActivityModule activityModule);

    RepositoryComponent plusRepositoryComponent(RepositoryModule module);

    void inject(SendNetworkConnectedBroadcast broadcast);
}
