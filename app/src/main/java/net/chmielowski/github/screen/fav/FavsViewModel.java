package net.chmielowski.github.screen.fav;

import android.databinding.ObservableBoolean;

import net.chmielowski.github.OnMainThread;
import net.chmielowski.github.data.Persistence;
import net.chmielowski.github.data.RealmRepo;
import net.chmielowski.github.data.RepoService;
import net.chmielowski.github.screen.RepositoryViewModel;

import java.util.Collection;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;

public class FavsViewModel {
    private final Persistence realm;
    private final RepoService service;

    public final ObservableBoolean loading = new ObservableBoolean();

    @Inject
    FavsViewModel(final Persistence realm,
                  @OnMainThread final RepoService service) {
        this.realm = realm;
        this.service = service;
    }

    public Observable<Collection<RepositoryViewModel>> data() {
        // TODO: move to Service class
        return Observable.just(realm.get(realm ->
                realm.where(RealmRepo.class)
                        .findAll()
                        .stream()
                        .map(RepositoryViewModel::new)
                        .collect(Collectors.toList())));
    }

    Single<Boolean> cache(final String repo) {
        return service.cacheItem(repo)
                .doOnSubscribe(__ -> loading.set(true))
                .doOnSuccess(__ -> loading.set(false));
    }
}
