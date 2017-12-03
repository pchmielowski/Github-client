package net.chmielowski.github.screen.list;

import android.databinding.ObservableBoolean;

import net.chmielowski.github.ReposRepository;
import net.chmielowski.github.Repositories;
import net.chmielowski.github.RepositoryViewModel;
import net.chmielowski.github.screen.fav.RealmRepo;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import io.realm.Realm;
import io.realm.RealmResults;

public final class SearchViewModel {
    private final ReposRepository repository;
    private final RealmFacade realmFacade;

    private final Subject<CharSequence> querySubject = PublishSubject.create();
    private final Subject<Object> searchSubject = PublishSubject.create();

    public final ObservableBoolean inputVisible = new ObservableBoolean(true);
    public final ObservableBoolean searchVisible = new ObservableBoolean(false);
    public final ObservableBoolean loading = new ObservableBoolean(false);

    @Inject
    SearchViewModel(final ReposRepository repository, final RealmFacade realmFacade) {
        this.repository = repository;
        this.realmFacade = realmFacade;
    }

    Observer<CharSequence> queryChanged() {
        return querySubject;
    }

    Observer<Object> searchClicked() {
        return searchSubject;
    }

    Observable<Collection<RepositoryViewModel>> searchResults() {
        return observeSearchClicked()
                .withLatestFrom(observeQuery(), (__, s) -> s)
                .flatMapSingle(query ->
                        repository.items(query)
                                .map(repositories -> repositories.stream()
                                        .map(this::cache)
                                        .map(repo -> new RepositoryViewModel(repo, query))
                                        .collect(Collectors.toList()))
                                .doOnSubscribe(__ -> loading.set(true))
                                .doOnSuccess(__ -> loading.set(false))
                                .doOnSuccess(__ -> searchVisible.set(false)));
    }

    private Repositories.Item cache(final Repositories.Item repo) {
        // TODO: move to another class
        realmFacade.executeInTransaction(realm -> {
            final RealmRepo realmRepo = new RealmRepo();
            realmRepo.name = repo.fullName;
            realmRepo.id = repo.id;
            // TODO: what if favorite was set before
            realm.copyToRealm(realmRepo);
        });
        return repo;
    }

    Disposable searchVisibleDisposable() {
        return observeQuery().subscribe(query -> searchVisible.set(!query.isEmpty()));
    }

    private Observable<String> observeQuery() {
        return querySubject
                .doOnComplete(() -> {
                    throw new IllegalStateException("querySubject completed");
                })
                .map(String::valueOf);
    }

    private Observable<Object> observeSearchClicked() {
        return searchSubject
                .doOnComplete(() -> {
                    throw new IllegalStateException("searchSubject completed");
                });
    }
}
