package net.chmielowski.github.screen.fav;

import android.databinding.ObservableBoolean;

import net.chmielowski.github.OnMainThread;
import net.chmielowski.github.data.IFavouriteRepos;
import net.chmielowski.github.data.RepoService;
import net.chmielowski.github.screen.RepositoryViewModel;

import java.util.Collection;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;

public class FavsViewModel {
    private final RepoService service;

    public final ObservableBoolean loading = new ObservableBoolean();
    private final IFavouriteRepos favourites;

    @Inject
    FavsViewModel(@OnMainThread final RepoService service,
                  final IFavouriteRepos favourites) {
        this.service = service;
        this.favourites = favourites;
    }

    public Observable<Collection<RepositoryViewModel>> data() {
        return Observable.just(favourites.all());
    }

    Single<Boolean> cache(final String repo) {
        return service.cacheItem(repo)
                .doOnSubscribe(__ -> loading.set(true))
                .doOnSuccess(__ -> loading.set(false));
    }
}
