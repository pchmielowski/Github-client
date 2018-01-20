package net.chmielowski.github.screen.fav;

import android.databinding.ObservableBoolean;

import net.chmielowski.github.data.Favourites;
import net.chmielowski.github.data.RepositoryDataSource;
import net.chmielowski.github.screen.RepositoryViewModel;

import java.util.Collection;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;

@SuppressWarnings("WeakerAccess")
public class FavsViewModel {

    public final ObservableBoolean loading = new ObservableBoolean();

    private final RepositoryDataSource data;
    private final Favourites favourites;

    @Inject
    FavsViewModel(@RepositoryDataSource.WorkOnBackground final RepositoryDataSource data,
                  final Favourites favourites) {
        this.data = data;
        this.favourites = favourites;
    }

    public Observable<Collection<RepositoryViewModel>> data() {
        return Observable.just(favourites.all());
    }

    Single<Boolean> cache(final String repo) {
        return data.cacheRepository(repo)
                .doOnSubscribe(__ -> loading.set(true))
                .doOnSuccess(__ -> loading.set(false));
    }
}
