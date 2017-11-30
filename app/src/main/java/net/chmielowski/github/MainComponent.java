package net.chmielowski.github;

import dagger.Component;

@Component
interface MainComponent {
    void inject(MainActivity activity);
}
