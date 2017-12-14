package net.chmielowski.github;

import net.chmielowski.github.screen.details.DetailsActivity;
import net.chmielowski.github.screen.fav.FavsActivity;
import net.chmielowski.github.screen.search.SearchActivity;

import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = ActivityModule.class)
public interface DetailsActivityComponent {
    void inject(SearchActivity activity);

    void inject(DetailsActivity activity);

    void inject(FavsActivity activity);
}