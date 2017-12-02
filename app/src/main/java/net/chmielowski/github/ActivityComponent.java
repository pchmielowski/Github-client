package net.chmielowski.github;

import net.chmielowski.github.screen.details.DetailsActivity;
import net.chmielowski.github.screen.list.MainActivity;

import dagger.Subcomponent;

@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(MainActivity activity);

    void inject(DetailsActivity activity);
}
