package net.chmielowski.github.screen.fav;

import android.databinding.ObservableBoolean;

import net.chmielowski.github.OnMainThread;
import net.chmielowski.github.data.RealmRepo;
import net.chmielowski.github.data.RepoService;
import net.chmielowski.github.screen.RepositoryViewModel;
import net.chmielowski.github.screen.search.RealmFacade;

import java.util.Collection;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;

public class FavsViewModel {
    private final RealmFacade realm;
    private final RepoService service;

    public final ObservableBoolean loading = new ObservableBoolean();

    @Inject
    FavsViewModel(final RealmFacade realm,
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

    // TODO: display loading
    Single<Boolean> fetchItem(final String repo) {
        return service.item(repo)
                .doOnSubscribe(__ -> loading.set(true))
                .doOnEvent((__, e) -> loading.set(false))
                .flatMap(item -> Single.just(true))
                .onErrorReturnItem(false);
    }
}
