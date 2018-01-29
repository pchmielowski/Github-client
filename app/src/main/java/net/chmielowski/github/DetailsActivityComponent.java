package net.chmielowski.github;

import android.support.v7.app.AppCompatActivity;

import net.chmielowski.github.screen.details.DetailsActivity;
import net.chmielowski.github.screen.fav.FavsActivity;
import net.chmielowski.github.screen.search.SearchActivity;

import dagger.BindsInstance;
import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = ActivityModule.class)
public interface DetailsActivityComponent {
    void inject(SearchActivity activity);

    void inject(DetailsActivity activity);

    void inject(FavsActivity activity);

    @Subcomponent.Builder
    interface Builder {
        DetailsActivityComponent build();

        @BindsInstance
        Builder activity(AppCompatActivity activity);
    }
}
