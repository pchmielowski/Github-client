package net.chmielowski.github;

import net.chmielowski.github.screen.details.DetailsActivity;
import net.chmielowski.github.screen.list.ListActivity;

import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(ListActivity activity);

    void inject(DetailsActivity activity);
}
