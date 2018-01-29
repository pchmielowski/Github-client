package net.chmielowski.github;

import android.support.v7.app.AppCompatActivity;

import net.chmielowski.github.screen.fav.FavsActivity;
import net.chmielowski.github.screen.login.LoginActivity;
import net.chmielowski.github.screen.search.SearchActivity;

import dagger.BindsInstance;
import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(SearchActivity activity);

    void inject(FavsActivity activity);

    void inject(LoginActivity activity);

    @Subcomponent.Builder
    interface Builder {
        ActivityComponent build();

        @BindsInstance
        Builder activity(AppCompatActivity activity);
    }
}
