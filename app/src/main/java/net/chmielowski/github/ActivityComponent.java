package net.chmielowski.github;

import net.chmielowski.github.screen.fav.FavsActivity;
import net.chmielowski.github.screen.login.LoginActivity;
import net.chmielowski.github.screen.search.SearchActivity;

import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(SearchActivity activity);

    void inject(FavsActivity activity);

    void inject(LoginActivity activity);
}
