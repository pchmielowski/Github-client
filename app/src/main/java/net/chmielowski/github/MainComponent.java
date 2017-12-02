package net.chmielowski.github;

import net.chmielowski.github.screen.details.DetailsActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = MainModule.class)
public interface MainComponent {

    void inject(DetailsActivity activity);

    ActivityComponent plusActivityComponent(ActivityModule activityModule);
}
