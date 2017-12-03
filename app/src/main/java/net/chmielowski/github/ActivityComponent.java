package net.chmielowski.github;

import net.chmielowski.github.screen.details.DetailsActivity;
import net.chmielowski.github.screen.fav.FavouritesActivity;
import net.chmielowski.github.screen.list.SearchActivity;

import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(SearchActivity activity);

    void inject(DetailsActivity activity);

    void inject(FavouritesActivity activity);
}
