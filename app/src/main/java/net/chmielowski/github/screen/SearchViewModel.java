package net.chmielowski.github.screen;

import android.databinding.ObservableBoolean;

import net.chmielowski.github.data.ReposRepository;

import java.util.Collection;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public final class SearchViewModel {
    private final ReposRepository repository;

    private final Subject<CharSequence> querySubject = PublishSubject.create();
    private final Subject<Object> searchSubject = PublishSubject.create();

    public final ObservableBoolean inputVisible = new ObservableBoolean(true);
    public final ObservableBoolean searchVisible = new ObservableBoolean(false);
    public final ObservableBoolean loading = new ObservableBoolean(false);

    @Inject
    SearchViewModel(final ReposRepository repository) {
        this.repository = repository;
    }

    public Observer<CharSequence> queryChanged() {
        return querySubject;
    }

    public Observer<Object> searchClicked() {
        return searchSubject;
    }

    public Observable<Collection<RepositoryViewModel>> searchResults() {
        return observeSearchClicked()
                .withLatestFrom(observeQuery(), (__, s) -> s)
                .flatMapSingle(query ->
                        repository.items(query)
                                .map(repositories -> repositories.stream()
                                        .map(repo -> new RepositoryViewModel(repo, query))
                                        .collect(Collectors.toList()))
                                .doOnSubscribe(__ -> loading.set(true))
                                .doOnSuccess(__ -> loading.set(false))
                                .doOnSuccess(__ -> searchVisible.set(false)));
    }

    public Disposable searchVisibleDisposable() {
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