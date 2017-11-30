package net.chmielowski.github;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = MainModule.class)
interface MainComponent {
    void inject(MainActivity activity);
}
